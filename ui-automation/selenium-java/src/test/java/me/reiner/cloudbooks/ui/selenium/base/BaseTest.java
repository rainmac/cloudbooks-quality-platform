package me.reiner.cloudbooks.ui.selenium.base;

import java.io.ByteArrayInputStream;
import java.time.Duration;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import io.qameta.allure.Allure;
import me.reiner.cloudbooks.ui.selenium.config.ConfigManager;
import me.reiner.cloudbooks.ui.selenium.driver.DriverFactory;
import me.reiner.cloudbooks.ui.selenium.utils.LoggerUtil;
import me.reiner.cloudbooks.ui.selenium.utils.LoggingConfigurator;

public class BaseTest {
	
	protected WebDriver driver;
	protected static final Logger log = LoggerUtil.getLogger(BaseTest.class);
	
	@BeforeSuite(alwaysRun = true)
	public void globalSetup() {
	    LoggingConfigurator.configure();
	    log.info("Framework initialized | ENV={} | BROWSER={}",
	    		ConfigManager.getEnv(),
	    		ConfigManager.getBrowser());
	}
	
	@BeforeMethod
	public void setup(java.lang.reflect.Method method) {
		String testName = method.getName();
		LoggerUtil.setTestContext(testName, ConfigManager.getBrowser(), ConfigManager.getEnv());

		log.info("========== Starting Test: {} ==========", testName);
	    log.debug("Initializing WebDriver — browser: {}, headless: {}",
	        ConfigManager.getBrowser(), ConfigManager.isHeadless());

	    driver = DriverFactory.createDriver(
	        ConfigManager.getBrowser(),
	        ConfigManager.isHeadless()
	    );

	    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	    driver.manage().window().maximize();
	    driver.get(ConfigManager.getBaseUrl());

	    log.info("WebDriver initialized successfully");
	}
	
	@AfterMethod
	public void cleanup(ITestResult result) {
		String testName = result.getName();

        if (result.getStatus() == ITestResult.FAILURE) {
            log.error("TEST FAILED: {} | Reason: {}", testName, result.getThrowable().getMessage());
            attachScreenshot(result.getName());
        } 
        else if (result.getStatus() == ITestResult.SUCCESS) {
            log.info("TEST PASSED: {}", testName);
        } 
        else {
            log.warn("TEST SKIPPED: {}", testName);
        }

        log.info("========== Ending Test: {} ==========", testName);
        
		driver.close();
		
		LoggerUtil.clearContext();
	}
	
	private void attachScreenshot(String testName) {
        try {
            byte[] screenshot = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(
                "Screenshot — " + testName,
                "image/png",
                new ByteArrayInputStream(screenshot),
                "png"
            );
        } 
        catch (Exception e) {
            log.error("[Allure] Failed to capture screenshot: " + e.getMessage());
        }
    }

}
