package me.reiner.cloudbooks.ui.selenium.ai;

import me.reiner.cloudbooks.ui.selenium.model.LocatorContext;

/**
 * Utility class for building AI locator detection prompts.
 */
public class LocatorPromptBuilder {
	
	private static final String LOCATOR_PROMPT_TEMPLATE = """
			Act as a Selenium Architect. Find the most stable, unique locator for the target then generate the JSON Response.
			### Priority Ranking
			1. [data-testid, data-cy] > 2. [aria-label, role] > 3. [Static ID] > 4. [Unique Text] > 5. [Semantic Class]
			
			### Constraints
			- NO absolute/positional XPaths (e.g., /div[1]/span[2]).
			- NO utility-only CSS (e.g., Tailwind 'mb-4', 'flex').
			- NO dynamic/randomized IDs.
			
			### Element Context
			- Target: %s
			- Details: %s | %s
			- Surrounding: %s | Form: %s
			
			### HTML Snapshot
			```html
			%s
			```
			
			### JSON Response (No Prose)
			{
			  "strategy": "css" | "xpath" | "id" | "name",
			  "value": "string",
			  "confidence": 0.0,
			}
			If no match: {"strategy": null, "value": null, "confidence": 0}
			""";
	
	private LocatorPromptBuilder() { }
	
	/**
	 * Builds the locator detection prompt with element context and page source.
	 * 
	 * @param elementContext The context information for the target element
	 * @param pageSource The cleaned HTML page source
	 * @return Formatted prompt string for AI locator detection
	 */
	public static String buildLocatorPrompt(LocatorContext elementContext, String pageSource) {
		return String.format(LOCATOR_PROMPT_TEMPLATE,
				elementContext.getSemanticLabel(),
				elementContext.getVisualHint(),
				elementContext.getAriaRole(),
				String.join(", ", elementContext.getNearbyText()),
				elementContext.getFormContext(),
				pageSource
		);
	}
}
