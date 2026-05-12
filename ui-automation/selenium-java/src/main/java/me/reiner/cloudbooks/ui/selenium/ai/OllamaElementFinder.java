package me.reiner.cloudbooks.ui.selenium.ai;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import me.reiner.cloudbooks.ui.selenium.config.ConfigManager;
import me.reiner.cloudbooks.ui.selenium.model.Locator;
import me.reiner.cloudbooks.ui.selenium.model.LocatorContext;
import me.reiner.cloudbooks.ui.selenium.utils.JsonUtils;
import me.reiner.cloudbooks.ui.selenium.utils.LoggerUtil;
import me.reiner.cloudbooks.ui.selenium.utils.PageSourceUtils;

public class OllamaElementFinder implements AIWebElementLocator {
	
	private static final Logger log = LoggerUtil.getLogger(OllamaElementFinder.class);
	
	private final String AI_SERVER = "http://localhost:11434/api/generate";
    private final String AI_MODEL = ConfigManager.getAIHealerModel();
	
	private OllamaElementFinder() {}
	
	private static class Holder {
        private static final OllamaElementFinder INSTANCE = new OllamaElementFinder();
    }
	
	public static OllamaElementFinder getInstance() {
        return Holder.INSTANCE;
    }
	
	
	@Override
	public Locator findLocator(WebDriver driver, LocatorContext elementContext) throws Exception {
		
		String pageSource = PageSourceUtils.getCleanPageSource(driver);
		
		String promptMessage = "Act as a Selenium Architect. Find the most stable, unique locator for the target then generate the JSON Response.\r\n"
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
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode root = mapper.createObjectNode();
		root.put("model", AI_MODEL);
		root.put("prompt", promptMessage);
		root.put("stream", false);
		String jsonInputString = mapper.writeValueAsString(root);
		
		log.debug("Ollama prompt: " + jsonInputString);
		
		log.info("Connecting to Ollama server : " + AI_SERVER);
		
		URL url = new URI(AI_SERVER).toURL();
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int code = conn.getResponseCode();
        log.debug("Ollama response code: " + code);

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
        StringBuilder responseBuilder = new StringBuilder();
        String responseLine = null;
        while ((responseLine = br.readLine()) != null) {
            responseBuilder.append(responseLine.trim());
        }

        JSONObject jsonResponse = new JSONObject(responseBuilder.toString());
        String responseText = jsonResponse.getString("response");
        String cleanedAIResponseText = JsonUtils.cleanJsonResponse(responseText);
        log.debug("AI Response: " + responseText);
        log.debug("AI Response (Cleaned): " + cleanedAIResponseText);
        
        Locator foundElement = mapper.readValue(cleanedAIResponseText, Locator.class);
        
        if (foundElement.getConfidence() < 0.60) {
        	throw new NoSuchElementException("Unable to find Web Element using Ollama AI!");
        }
        
        return foundElement;

	}

}
