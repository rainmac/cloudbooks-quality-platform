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

import me.reiner.cloudbooks.ui.selenium.model.Locator;
import me.reiner.cloudbooks.ui.selenium.model.LocatorElement;

public class SmartLocator {
	
	private static final Logger log = LoggerUtil.getLogger(SmartLocator.class);
	
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
	
	public WebElement findElement(String elementKey) throws IOException {
	
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
				By by = toBy(loc);
				WebElement el = new WebDriverWait(driver, Duration.ofSeconds(5))
											.until(ExpectedConditions.presenceOfElementLocated(by));
				return el;
			}
			catch(TimeoutException ex) {
				log.info("Locator " + i + " for element '" + elementKey + "' not found: " + loc.getStrategy() + "=" + loc.getValue());
				lastException = ex;
			}
		}
		
		throw new RuntimeException("Web Element " + elementKey + " not found using all its locators: ", lastException);
	
		// TODO:
		//	4. Use AI to find the match locator
		//	5. Create Git PR to update the locators
		
		
	}
	
	private By toBy(Locator loc) {
        return switch (loc.getStrategy().toLowerCase()) {
            case "id"        -> By.id(loc.getValue());
            case "class"     -> By.className(loc.getValue());
            case "css"       -> By.cssSelector(loc.getValue());
            case "xpath"     -> By.xpath(loc.getValue());
            case "name"      -> By.name(loc.getValue());
            case "linktext"  -> By.linkText(loc.getValue());
            default -> throw new IllegalArgumentException("Unknown strategy: " + loc.getStrategy());
        };
    }

}
