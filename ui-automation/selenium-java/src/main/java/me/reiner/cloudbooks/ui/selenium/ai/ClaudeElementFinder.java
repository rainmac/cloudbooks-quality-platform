package me.reiner.cloudbooks.ui.selenium.ai;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import com.anthropic.client.AnthropicClient;
import com.anthropic.client.okhttp.AnthropicOkHttpClient;
import com.anthropic.models.messages.Message;
import com.anthropic.models.messages.MessageCreateParams;
import com.anthropic.models.messages.Model;
import com.fasterxml.jackson.databind.ObjectMapper;

import me.reiner.cloudbooks.ui.selenium.config.ConfigManager;
import me.reiner.cloudbooks.ui.selenium.model.Locator;
import me.reiner.cloudbooks.ui.selenium.model.LocatorContext;
import me.reiner.cloudbooks.ui.selenium.utils.JsonUtils;
import me.reiner.cloudbooks.ui.selenium.utils.PageSourceUtils;

public class ClaudeElementFinder implements AIWebElementLocator {
	
	private static final String WEB_ELEMENT_NOT_FOUND = "WEB ELEMENT NOT FOUND!";
	
	private AnthropicClient client;
	private Model model = Model.of(ConfigManager.getAIHealerModel());
	private long maxToken = 1024L;
	
	private ClaudeElementFinder() {
		client = AnthropicOkHttpClient.builder()
			    .apiKey(ConfigManager.getAIHealerApiKey())
			    .build();
	}
	
	private static class Holder {
        private static final ClaudeElementFinder INSTANCE = new ClaudeElementFinder();
    }
	
	public static ClaudeElementFinder getInstance() {
        return Holder.INSTANCE;
    }

	@Override
	public Locator findLocator(WebDriver driver, LocatorContext elementContext) throws Exception {
		
		String pageSource = PageSourceUtils.getCleanPageSource(driver);
		String message = LocatorPromptBuilder.buildLocatorPrompt(elementContext, pageSource);
		
		// Build the message parameters
        MessageCreateParams params = MessageCreateParams.builder()
                .model(model)
                .maxTokens(maxToken)
                .addUserMessage(message)
                .build();

        Message response = client.messages().create(params);
        
        String generatedAILocator = response.content().stream()
        	    .flatMap(block -> block.text().stream())
        	    .map(textBlock -> textBlock.text())
        	    .findFirst()
        	    .orElse(WEB_ELEMENT_NOT_FOUND);
        
        String cleanedAILocator = JsonUtils.cleanJsonResponse(generatedAILocator);
        
        ObjectMapper mapper = new ObjectMapper();
        Locator foundElement = mapper.readValue(cleanedAILocator, Locator.class);
        
        if (generatedAILocator.equals(WEB_ELEMENT_NOT_FOUND) || foundElement.getConfidence() < 0.60) {
        	throw new NoSuchElementException("Unable to find Web Element using Claude Code AI!");
        }
        
        return foundElement;
	}

}
