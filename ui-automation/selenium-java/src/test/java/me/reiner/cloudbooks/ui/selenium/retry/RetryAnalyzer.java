package me.reiner.cloudbooks.ui.selenium.retry;

import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import io.github.cdimascio.dotenv.Dotenv;
import me.reiner.cloudbooks.ui.selenium.utils.LoggerUtils;

public class RetryAnalyzer implements IRetryAnalyzer {

	protected static final Logger log = LoggerUtils.getLogger(RetryAnalyzer.class);
	
	private int retryCount = 0;
    private static final int MAX_RETRY = resolveMaxRetry();

    private static int resolveMaxRetry() {
    	
        String sysProp = System.getProperty("retry.count");
        
        if (sysProp != null) return Integer.parseInt(sysProp.trim());

        try {
            String envVal = Dotenv.load().get("RETRY_COUNT");
            
            if (envVal != null) return Integer.parseInt(envVal.trim());
        } 
        catch (Exception ignored) {}

        return 2;
    }

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY) {
            retryCount++;
            log.info("[RETRY] '{}' attempt {} of {}", result.getName(), retryCount, MAX_RETRY);
            
            return true;
        }
        return false;
    }
    
}
