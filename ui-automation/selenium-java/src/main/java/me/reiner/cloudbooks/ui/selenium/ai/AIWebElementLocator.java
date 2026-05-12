package me.reiner.cloudbooks.ui.selenium.ai;

import org.openqa.selenium.WebDriver;

import me.reiner.cloudbooks.ui.selenium.model.Locator;
import me.reiner.cloudbooks.ui.selenium.model.LocatorContext;

public interface AIWebElementLocator {
	
	public Locator findLocator(WebDriver driver, LocatorContext elementContext) throws Exception;

}
