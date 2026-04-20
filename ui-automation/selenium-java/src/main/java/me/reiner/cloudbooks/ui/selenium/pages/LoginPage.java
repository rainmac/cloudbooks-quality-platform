package me.reiner.cloudbooks.ui.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import me.reiner.cloudbooks.ui.selenium.base.BasePage;

public class LoginPage extends BasePage {
	
	@FindBy(id = "user-name")
	private WebElement txtUserName;
	
	@FindBy(id = "password")
	private WebElement txtPassword;
	
	@FindBy(id = "login-button")
	private WebElement btnLogin;
	
	
	public LoginPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public void login(String username, String password) {
		txtUserName.sendKeys("standard_user");
		txtPassword.sendKeys("secret_sauce");
		btnLogin.click();
	}

}
