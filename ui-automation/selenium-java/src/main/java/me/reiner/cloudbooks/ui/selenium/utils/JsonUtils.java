package me.reiner.cloudbooks.ui.selenium.utils;

public class JsonUtils {
	
	public static String cleanJsonResponse(String rawResponse) {
	    return rawResponse.replaceAll("(?s).*```json\\s*|\\s*```.*", "").trim();
	}

}
