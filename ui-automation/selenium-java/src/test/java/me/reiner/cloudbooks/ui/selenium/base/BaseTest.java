package me.reiner.cloudbooks.ui.selenium.base;

import java.time.Duration;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import me.reiner.cloudbooks.ui.selenium.utils.ConfigManager;
import me.reiner.cloudbooks.ui.selenium.utils.LoggerUtil;
import me.reiner.cloudbooks.ui.selenium.utils.LoggingConfigurator;

public class BaseTest {
	
	protected WebDriver driver;
	protected String baseUrl = ConfigManager.getBaseUrl();
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
        log.debug("Initializing WebDriver...");
        
		ChromeOptions options = new ChromeOptions();
		
		if (ConfigManager.isHeadless()) {
			options.addArguments("--headless=new");
		}
		
		driver = new ChromeDriver(options);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().window().maximize();
		driver.get(baseUrl);
		
		log.info("WebDriver initialized successfully");
	}
	
	@AfterMethod
	public void cleanup(ITestResult result) {
		String testName = result.getName();

        if (result.getStatus() == ITestResult.FAILURE) {
            log.error("TEST FAILED: {} | Reason: {}",
                testName, result.getThrowable().getMessage());
            // captureScreenshot(testName);
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            log.info("TEST PASSED: {}", testName);
        } else {
            log.warn("TEST SKIPPED: {}", testName);
        }

        log.info("========== Ending Test: {} ==========", testName);
        
		driver.close();
		
		LoggerUtil.clearContext();
	}

}
