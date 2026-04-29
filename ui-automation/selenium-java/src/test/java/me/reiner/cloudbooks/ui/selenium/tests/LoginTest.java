package me.reiner.cloudbooks.ui.selenium.tests;

import java.io.IOException;
import java.time.Duration;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import me.reiner.cloudbooks.ui.selenium.base.BaseTest;
import me.reiner.cloudbooks.ui.selenium.pages.InventoryPage;
import me.reiner.cloudbooks.ui.selenium.pages.LoginPage;
import me.reiner.cloudbooks.ui.selenium.utils.ConfigManager;

@Epic("Authentication")
@Feature("Login")
public class LoginTest extends BaseTest {
	
	@Test(description = "Valid user can log in successfully")
    @Story("User login")
    @Severity(SeverityLevel.CRITICAL)
	public void successfulLogin() throws InterruptedException, IOException {
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(ConfigManager.getUsername(), ConfigManager.getPassword());
		
		InventoryPage inventoryPage = new InventoryPage(driver);
		Assert.assertTrue(inventoryPage.shoppingCartLink().isDisplayed());
		
		
		Thread.sleep(Duration.ofSeconds(3));
	}

}
