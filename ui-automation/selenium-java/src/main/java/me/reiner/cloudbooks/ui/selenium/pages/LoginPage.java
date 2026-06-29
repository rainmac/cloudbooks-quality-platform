package me.reiner.cloudbooks.ui.selenium.pages;

import java.io.IOException;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import me.reiner.cloudbooks.ui.selenium.utils.LoggerUtils;
import me.reiner.cloudbooks.ui.selenium.utils.SmartLocator;

public class LoginPage extends BasePage {
	
	private SmartLocator smartLocator;
	private static final Logger log = LoggerUtils.getLogger(LoginPage.class);
	
	
	public LoginPage(WebDriver driver) throws IOException {
		super(driver);
		smartLocator = new SmartLocator(driver, "LoginPage");
		log.debug("LoginPage initialized");
	}
	
	public void login(String username, String password) throws Exception {
		log.debug("Entering username: {}", username);
		smartLocator.findElement("txtUsername").sendKeys(username);
		smartLocator.findElement("txtPassword").sendKeys(password);
		smartLocator.findElement("btnLogin").click();
	}

}
