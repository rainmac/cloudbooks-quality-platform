package me.reiner.cloudbooks.ui.selenium.pages;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import me.reiner.cloudbooks.ui.selenium.utils.SmartElement;

public class InventoryPage extends BasePage {
	
	private SmartElement smartElement;
	
	public InventoryPage(WebDriver driver) throws IOException {
		super(driver);
		smartElement = new SmartElement(driver, "InventoryPage");
	}
	
	public WebElement shoppingCartLink() throws IOException {
		return smartElement.findElement("lnkShoppingCart");
	}

}
