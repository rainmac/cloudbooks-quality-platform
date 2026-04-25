package me.reiner.cloudbooks.ui.selenium.base;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import me.reiner.cloudbooks.ui.selenium.utils.ConfigManager;

public class BaseTest {
	
	protected WebDriver driver;
	protected String baseUrl = ConfigManager.getBaseUrl();
	
	@BeforeMethod
	public void setup() {
		ChromeOptions options = new ChromeOptions();
		
		if (ConfigManager.isHeadless()) {
			options.addArguments("--headless=new");
		}
		
		driver = new ChromeDriver(options);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().window().maximize();
		driver.get(baseUrl);
	}
	
	@AfterMethod
	public void cleanup() {
		driver.close();
	}

}
