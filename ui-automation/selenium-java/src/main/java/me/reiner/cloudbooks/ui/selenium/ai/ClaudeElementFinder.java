package me.reiner.cloudbooks.ui.selenium.ai;

import java.io.IOException;

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
	
	private static volatile ClaudeElementFinder instance;
	private static String WEB_ELEMENT_NOT_FOUND = "WEB ELEMENT NOT FOUND!";
	
	private AnthropicClient client;
	private Model model = Model.CLAUDE_HAIKU_4_5;
	private long maxToken = 1024L;
	
	private ClaudeElementFinder() {
		client = AnthropicOkHttpClient.builder()
			    .apiKey(ConfigManager.getAIHealerApiKey())
			    .build();
	}
	
	public static ClaudeElementFinder getInstance() {
		if (instance == null) {
			synchronized (ClaudeElementFinder.class) {
				if (instance == null) {
					instance = new ClaudeElementFinder();
				}
			}
		}
		
		return instance;
	}

	@Override
	public Locator findLocator(WebDriver driver, LocatorContext elementContext) throws IOException {
		
		String pageSource = PageSourceUtils.getCleanPageSource(driver);
		
		String message = "Act as a Selenium Architect. Find the most stable, unique locator for the target.\r\n"
				+ "### Priority Ranking\r\n"
				+ "1. [data-testid, data-cy] > 2. [aria-label, role] > 3. [Static ID] > 4. [Unique Text] > 5. [Semantic Class]\r\n"
				+ "\r\n"
				+ "### Constraints\r\n"
				+ "- NO absolute/positional XPaths (e.g., /div[1]/span[2]).\r\n"
				+ "- NO utility-only CSS (e.g., Tailwind 'mb-4', 'flex').\r\n"
				+ "- NO dynamic/randomized IDs.\r\n"
				+ "\r\n"
				+ "### Element Context\r\n"
				+ "- Target: " + elementContext.getSemanticLabel() + "\r\n"
				+ "- Details: " + elementContext.getVisualHint() + " | " + elementContext.getAriaRole() + "\r\n"
				+ "- Surrounding: " + String.join(", ", elementContext.getNearbyText()) + " | Form: " + elementContext.getFormContext() + "\r\n"
				+ "\r\n"
				+ "### HTML Snapshot\r\n"
				+ "```html\r\n"
				+ pageSource + "\r\n"
				+ "```\r\n"
				+ "\r\n"
				+ "### JSON Response (No Prose)\r\n"
				+ "{\r\n"
				+ "  \"strategy\": \"css\" | \"xpath\" | \"id\" | \"name\",\r\n"
				+ "  \"value\": \"string\",\r\n"
				+ "  \"confidence\": 0.0,\r\n"
				+ "}\r\n"
				+ "If no match: {\"strategy\": null, \"value\": null, \"confidence\": 0}";
		
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
        	throw new NoSuchElementException("Unable to find Web Element using AI!");
        }
        
        return foundElement;
	}

}
