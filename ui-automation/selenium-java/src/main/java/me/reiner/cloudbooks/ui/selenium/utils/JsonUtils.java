package me.reiner.cloudbooks.ui.selenium.utils;

public class JsonUtils {
	
	public static String cleanJsonResponse(String rawResponse) {
	    // Matches ```json, ```JSON, or just ``` followed by any content, and ending with ```
	    // Includes (?s) to allow matching across multiple lines (DOTALL mode)
	    return rawResponse.replaceAll("(?s)^```(?:json)?\\n?|\\n?```$", "").trim();
	}

}
