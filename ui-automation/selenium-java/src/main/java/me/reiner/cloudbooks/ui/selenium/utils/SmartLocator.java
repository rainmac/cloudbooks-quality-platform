package me.reiner.cloudbooks.ui.selenium.utils;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import me.reiner.cloudbooks.ui.selenium.ai.AIWebElementLocator;
import me.reiner.cloudbooks.ui.selenium.ai.ClaudeElementFinder;
import me.reiner.cloudbooks.ui.selenium.ai.OllamaElementFinder;
import me.reiner.cloudbooks.ui.selenium.config.ConfigManager;
import me.reiner.cloudbooks.ui.selenium.model.Locator;
import me.reiner.cloudbooks.ui.selenium.model.LocatorContext;
import me.reiner.cloudbooks.ui.selenium.model.LocatorElement;

public class SmartLocator {
	
	private static final Logger log = LoggerUtils.getLogger(SmartLocator.class);
	
	private WebDriver driver;
	private ObjectMapper mapper = new ObjectMapper();
	private JsonNode pageElements;
	
	public SmartLocator(WebDriver driver, String page) throws IOException {
		this.driver = driver;
		
		String jsonFileName = page.replaceAll("([a-z])([A-Z])", "$1-$2").toLowerCase();
		ObjectNode jsonRootNode = (ObjectNode) mapper.readTree(
										new File("src/main/resources/locators/" + jsonFileName + ".json"));
		pageElements = jsonRootNode.at("/locators");		
	}
	
	public WebElement findElement(String elementKey) throws Exception {
	
		//	1. Read Locator JSON file
		JsonNode elementLocatorRoot = pageElements.get(elementKey);
		
		if (elementLocatorRoot == null) {
			log.info("Element '" + elementKey + "' is not found in the Json Locator file.");
			throw new NoSuchElementException("Element '" + elementKey + "' is not found in the Json Locator file.");
		}
		
		LocatorElement elementLocator = mapper.treeToValue(elementLocatorRoot, LocatorElement.class);
		
		//	2. Try Primary Locator and fallback locators
		List<Locator> locators = elementLocator.getLocators();
		Exception lastException = null;
		
		for (int i=0; i<locators.size(); i++) {
			Locator loc = locators.get(i);
			
			try {
				return getWebElement(loc);
			}
			catch(TimeoutException ex) {
				log.info("Locator " + i + " for element '" + elementKey + "' not found: " + loc.getStrategy() + "=" + loc.getValue());
				lastException = ex;
			}
		}
		
		log.warn("Web Element " + elementKey + " not found using all its defined locators: " + lastException);
		
		if (ConfigManager.isAIHealerEnabled()) {
			log.info("Locating web element " + elementKey + " locator using AI");
			Locator aiElementLocator = findElementLocatorUsingAI(driver, elementLocator.getContext());
			
			if (aiElementLocator == null) {
				log.warn("Web Element " + elementKey + " not found using AI");
				throw new NoSuchElementException("Web Element " + elementKey + " not found using all its locators including AI.");
			}
			
			try {
				return getWebElement(aiElementLocator);
			}
			catch(TimeoutException ex) {
				log.info("Element '" + elementKey + "' not found using AI.");
			}
		}
		
		throw new NoSuchElementException("Web Element " + elementKey + " not found using all its locators.");
		
	}
	
	public Locator findElementLocatorUsingAI(WebDriver driver, LocatorContext elementLocator) throws Exception {
		
		AIWebElementLocator ai;
		log.info("Locating web element using {}.", ConfigManager.getAIHealerProvider());
		
		if (ConfigManager.getAIHealerProvider().equals("CLAUDE_CODE")) {
			ai = ClaudeElementFinder.getInstance();
			return ai.findLocator(driver, elementLocator);
			
		}
		else if (ConfigManager.getAIHealerProvider().equals("OLLAMA")) {
			ai = OllamaElementFinder.getInstance();
			return ai.findLocator(driver, elementLocator);
		}
		
		return null;
	}
	
	private By toBy(Locator loc) {
        return switch (loc.getStrategy().toLowerCase()) {
            case "id"        		-> By.id(loc.getValue());
            case "name"      		-> By.name(loc.getValue());
            case "classname" 		-> By.className(loc.getValue());
            case "tagname"   		-> By.tagName(loc.getValue());
            case "linktext"  		-> By.linkText(loc.getValue());
            case "partiallinktext"	-> By.partialLinkText(loc.getValue());
            case "css"       		-> By.cssSelector(loc.getValue());
            case "xpath"     		-> By.xpath(loc.getValue());
            default 				-> throw new IllegalArgumentException("Unknown strategy: " + loc.getStrategy());
        };
    }
	
	private WebElement getWebElement(Locator locator) throws TimeoutException {
		By by = toBy(locator);
		WebElement el = new WebDriverWait(driver, Duration.ofSeconds(5))
										.until(ExpectedConditions.presenceOfElementLocated(by));
		return el;
	}

}
