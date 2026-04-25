package me.reiner.cloudbooks.ui.selenium.utils;

import io.github.cdimascio.dotenv.Dotenv;

public class ConfigManager {

	private static final Dotenv dotenv = Dotenv.configure()
				.ignoreIfMissing()
				.load();
	
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
	
	public static String getBaseUrl() 		{ return get("BASE_URL", "https://www.saucedemo.com"); }
	public static boolean isHeadless()   	{ return Boolean.parseBoolean(get("HEADLESS_BROWSER", "true")); }
}
