package me.reiner.cloudbooks.ui.selenium.config;

import io.github.cdimascio.dotenv.Dotenv;

public class ConfigManager {

	static {
        Dotenv dotenv = Dotenv.configure()
                .directory("./")
                .ignoreIfMissing() 
                .load();

        dotenv.entries().forEach(entry -> {
            if (System.getProperty(entry.getKey()) == null) {
                System.setProperty(entry.getKey(), entry.getValue());
            }
        });
    }
	
	private ConfigManager() {}
	
	public static String get(String key, String defaultValue) {
		// 1. JVM system property
        String value = System.getProperty(key);
        if (value != null) return value;

        // 2. OS environment variable (GitHub Actions sets these)
        value = System.getenv(key);
        if (value != null) return value;

        // 3. Hardcoded default — never throw
        return defaultValue;
    }
	
	// ─── Typed helpers ───────────────────────────────
    public static boolean getBoolean(String key, boolean defaultValue) {
    	String value = get(key, String.valueOf(defaultValue));
        
    	return Boolean.parseBoolean(value);
    }
    
    public static String getBrowser() {
        String value = System.getProperty("browser");
        
        if (value == null) value = get("BROWSER", "chrome");
        
        return value.trim().toLowerCase();
    }
    
    public static boolean isHeadless() {
        String sysProp = System.getProperty("headless");
        
        if (sysProp != null) return Boolean.parseBoolean(sysProp);
        
        return Boolean.parseBoolean(get("HEADLESS", "true"));
    }
    
    public static String getEnv() {
        String value = System.getProperty("env");
        
        if (value == null) value = get("ENV", "QA");
        
        return value.trim().toLowerCase();
    }
	
	public static String getBaseUrl() 			{ return get("BASE_URL", "https://www.saucedemo.com"); }
	
	// ─── Logging-specific getters ────────────────────
    public static String getLogLevel()          { return get("LOG_LEVEL", "INFO"); }
    public static String getLogDir()            { return get("LOG_DIR", "target/logs"); }
    public static boolean isConsoleLogEnabled() { return getBoolean("LOG_CONSOLE_ENABLED", true); }
    public static boolean isFileLogEnabled()    { return getBoolean("LOG_FILE_ENABLED", true); }
    
    // ─── Application-specific getters ─────────────────
    public static String getUsername()			{ return get("SAUCEDEMO_USERNAME", ""); }	
    public static String getPassword()			{ return get("SAUCEDEMO_PASSWORD", ""); }	
}
