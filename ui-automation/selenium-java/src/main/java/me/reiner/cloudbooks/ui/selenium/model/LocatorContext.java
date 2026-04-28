package me.reiner.cloudbooks.ui.selenium.model;

import java.util.List;

public class LocatorContext {
	private String semanticLabel;
    private String visualHint;
    private String ariaRole;
    private List<String> nearbyText;
    private String formContext;
    
	public String getSemanticLabel() {
		return semanticLabel;
	}
	
	public void setSemanticLabel(String semanticLabel) {
		this.semanticLabel = semanticLabel;
	}
	
	public String getVisualHint() {
		return visualHint;
	}
	
	public void setVisualHint(String visualHint) {
		this.visualHint = visualHint;
	}
	
	public String getAriaRole() {
		return ariaRole;
	}
	
	public void setAriaRole(String ariaRole) {
		this.ariaRole = ariaRole;
	}
	
	public List<String> getNearbyText() {
		return nearbyText;
	}
	
	public void setNearbyText(List<String> nearbyText) {
		this.nearbyText = nearbyText;
	}
	
	public String getFormContext() {
		return formContext;
	}
	
	public void setFormContext(String formContext) {
		this.formContext = formContext;
	}
}
