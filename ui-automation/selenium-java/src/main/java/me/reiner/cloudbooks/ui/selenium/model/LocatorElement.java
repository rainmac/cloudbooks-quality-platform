package me.reiner.cloudbooks.ui.selenium.model;

import java.util.ArrayList;
import java.util.List;

public class LocatorElement {

	private String description;
    private LocatorContext context;
    private Locator primary;
    private Locator fallback1;
    private Locator fallback2;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocatorContext getContext() {
		return context;
	}

	public void setContext(LocatorContext context) {
		this.context = context;
	}

	public Locator getPrimary() {
		return primary;
	}

	public void setPrimary(Locator primary) {
		this.primary = primary;
	}

	public Locator getFallback1() {
		return fallback1;
	}

	public void setFallback1(Locator fallback1) {
		this.fallback1 = fallback1;
	}

	public Locator getFallback2() {
		return fallback2;
	}

	public void setFallback2(Locator fallback2) {
		this.fallback2 = fallback2;
	}
    
	public List<Locator> getLocators() {
		List<Locator> locatorList = new ArrayList<>();
        if (primary  != null) locatorList.add(primary);
        if (fallback1  != null) locatorList.add(fallback1);
        if (fallback2 != null) locatorList.add(fallback2);
        
        return locatorList;
	}
}
