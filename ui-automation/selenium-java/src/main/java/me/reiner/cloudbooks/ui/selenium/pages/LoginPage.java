package me.reiner.cloudbooks.ui.selenium.pages;

import java.io.IOException;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import me.reiner.cloudbooks.ui.selenium.utils.LoggerUtil;
import me.reiner.cloudbooks.ui.selenium.utils.SmartLocator;

public class LoginPage extends BasePage {
	
	private SmartLocator smartLocator;
	private static final Logger log = LoggerUtil.getLogger(LoginPage.class);
	
	
	public LoginPage(WebDriver driver) throws IOException {
		super(driver);
		smartLocator = new SmartLocator(driver, "LoginPage");
		log.debug("LoginPage initialized");
	}
	
	public void login(String username, String password) throws IOException {
		log.debug("Entering username: {}", username);
		smartLocator.findElement("txtUsername").sendKeys("standard_user");
		smartLocator.findElement("txtPassword").sendKeys("secret_sauce");
		smartLocator.findElement("btnLogin").click();
	}

}
