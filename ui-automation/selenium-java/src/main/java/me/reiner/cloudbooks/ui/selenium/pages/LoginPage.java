package me.reiner.cloudbooks.ui.selenium.pages;

import java.io.IOException;

import org.openqa.selenium.WebDriver;

import me.reiner.cloudbooks.ui.selenium.utils.SmartElement;

public class LoginPage extends BasePage {
	
	private SmartElement smartElement;
	
	
	public LoginPage(WebDriver driver) throws IOException {
		super(driver);
		smartElement = new SmartElement(driver, "LoginPage");
	}
	
	public void login(String username, String password) throws IOException {
		smartElement.findElement("txtUsername").sendKeys("standard_user");
		smartElement.findElement("txtPassword").sendKeys("secret_sauce");
		smartElement.findElement("btnLogin").click();
	}

}
