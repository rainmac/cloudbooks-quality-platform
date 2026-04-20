package me.reiner.cloudbooks.ui.selenium.tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import me.reiner.cloudbooks.ui.selenium.base.BaseTest;
import me.reiner.cloudbooks.ui.selenium.pages.InventoryPage;
import me.reiner.cloudbooks.ui.selenium.pages.LoginPage;
import utils.ConfigManager;

public class LoginTest extends BaseTest {
	
	@Test
	public void successfulLogin() throws InterruptedException {
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(ConfigManager.get("SAUCEDEMO_USERNAME"), ConfigManager.get("SAUCEDEMO_PASSWORD"));
		
		InventoryPage inventoryPage = new InventoryPage(driver);
		Assert.assertTrue(inventoryPage.getShoppingCartLink().isDisplayed());
		
		Thread.sleep(Duration.ofSeconds(5));
	}

}
