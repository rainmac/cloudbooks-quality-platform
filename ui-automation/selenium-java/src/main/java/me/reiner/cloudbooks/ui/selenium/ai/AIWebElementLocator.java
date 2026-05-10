package me.reiner.cloudbooks.ui.selenium.ai;

import java.io.IOException;

import org.openqa.selenium.WebDriver;

import me.reiner.cloudbooks.ui.selenium.model.Locator;
import me.reiner.cloudbooks.ui.selenium.model.LocatorContext;

public interface AIWebElementLocator {
	
	public Locator findLocator(WebDriver driver, LocatorContext elementContext) throws IOException;

}
