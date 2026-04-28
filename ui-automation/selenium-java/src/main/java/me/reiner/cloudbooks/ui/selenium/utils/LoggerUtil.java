package me.reiner.cloudbooks.ui.selenium.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

public class LoggerUtil {
	
    private LoggerUtil() {}

    public static Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }

    public static void setTestContext(String testName, String browser, String environment) {
        ThreadContext.put("testName", testName);
        ThreadContext.put("browser", browser);
        ThreadContext.put("environment", environment);
    }

    public static void clearContext() {
        ThreadContext.clearAll();
    }
}
