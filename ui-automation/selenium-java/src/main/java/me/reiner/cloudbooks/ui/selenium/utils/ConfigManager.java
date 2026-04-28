package me.reiner.cloudbooks.ui.selenium.utils;

import io.github.cdimascio.dotenv.Dotenv;

public class ConfigManager {

	private static final Dotenv dotenv = Dotenv.configure()
				.directory("./")
				.ignoreIfMissing()
				.load();
	
	private ConfigManager() {}
	
	public static String get(String key) {
		String value = dotenv.get(key, System.getenv(key));
		if (value == null) {
			throw new RuntimeException("Missing config key: " + key);
		}
		return value;
	}
	
	public static String get(String key, String defaultValue) {
        String value = dotenv.get(key, System.getenv(key));
        return (value != null) ? value : defaultValue;
    }
	
	// ─── Typed helpers ───────────────────────────────
    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = get(key);
        return (value != null) ? Boolean.parseBoolean(value) : defaultValue;
    }
	
	public static String getBaseUrl() 		{ return get("BASE_URL", "https://www.saucedemo.com"); }
	public static boolean isHeadless()   	{ return Boolean.parseBoolean(get("HEADLESS_BROWSER", "true")); }
	
	// ─── Logging-specific getters ────────────────────
    public static String getLogLevel()          { return get("LOG_LEVEL", "INFO"); }
    public static String getLogDir()            { return get("LOG_DIR", "target/logs"); }
    public static boolean isConsoleLogEnabled() { return getBoolean("LOG_CONSOLE_ENABLED", true); }
    public static boolean isFileLogEnabled()    { return getBoolean("LOG_FILE_ENABLED", true); }
}
