package me.reiner.cloudbooks.ui.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import me.reiner.cloudbooks.ui.selenium.base.BasePage;

public class InventoryPage extends BasePage {
	
	@FindBy(className = "shopping_cart_link")
	WebElement lnkShoppingCart;
	
	public InventoryPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public WebElement getShoppingCartLink() {
		return lnkShoppingCart;
	}

}
