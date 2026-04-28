package me.reiner.cloudbooks.ui.selenium.tests;

import java.io.IOException;
import java.time.Duration;

import org.testng.Assert;
import org.testng.annotations.Test;

import me.reiner.cloudbooks.ui.selenium.base.BaseTest;
import me.reiner.cloudbooks.ui.selenium.pages.InventoryPage;
import me.reiner.cloudbooks.ui.selenium.pages.LoginPage;
import me.reiner.cloudbooks.ui.selenium.utils.ConfigManager;

public class LoginTest extends BaseTest {
	
	@Test
	public void successfulLogin() throws InterruptedException, IOException {
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(ConfigManager.getUsername(), ConfigManager.getPassword());
		
		InventoryPage inventoryPage = new InventoryPage(driver);
		Assert.assertTrue(inventoryPage.shoppingCartLink().isDisplayed());
		
		
		Thread.sleep(Duration.ofSeconds(3));
	}

}
