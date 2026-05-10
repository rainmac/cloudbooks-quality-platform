package me.reiner.cloudbooks.ui.selenium.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;
import org.openqa.selenium.WebDriver;

public class PageSourceUtils {
	
	public static String getCleanPageSource(WebDriver driver) {
		
		String rawHtml = driver.getPageSource();
		
        // 1. Define the allowed tags
        Safelist interactiveSafelist = new Safelist()
            .addTags("a", "button", "input", "select", "div", "span", "form") // Structural & Interactive
            .addAttributes("a", "href", "id", "class", "name")
            .addAttributes("button", "id", "class", "name", "type", "value")
            .addAttributes("input", "id", "class", "name", "type", "value", "placeholder")
            .addAttributes("select", "id", "class", "name")
            .addAttributes("textarea", "id", "class", "name")
            // Use a regex to allow all data-* attributes for better AI context
            .addAttributes(":all", "data-.*") 
            .addAttributes(":all", "id", "name", "aria-label", "placeholder");

        // 2. Trim the HTML based on the Safelist
        String trimmedHtml = Jsoup.clean(rawHtml, interactiveSafelist);

        // 3. Pretty print or output settings to further minimize tokens
        Document doc = Jsoup.parseBodyFragment(trimmedHtml);
        doc.outputSettings().prettyPrint(false); // Remove extra whitespace/newlines
        
        return doc.body().html();
    }

}
