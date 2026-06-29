package me.reiner.cloudbooks.ui.selenium.pages;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import me.reiner.cloudbooks.ui.selenium.utils.SmartLocator;

public class InventoryPage extends BasePage {
	
	private SmartLocator smartElement;
	
	public InventoryPage(WebDriver driver) throws IOException {
		super(driver);
		smartElement = new SmartLocator(driver, "InventoryPage");
	}
	
	public WebElement shoppingCartLink() throws Exception {
		return smartElement.findElement("lnkShoppingCart");
	}

}
