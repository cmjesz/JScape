package application;

import java.util.ArrayList;
import javafx.scene.web.WebEngine;

public class BrowserLogic {

	private WebEngine browser;
	private ArrayList<String> backward, forward;
	
	public BrowserLogic(WebEngine browser) {
		this.browser = browser;
		backward = new ArrayList<>();
		forward = new ArrayList<>();
	}
	
	
	/*
	 * Protected Methods
	 * 
	 * 
	 */
	protected String navigateHome() {
		// TODO: Need to have settings in order to make this configurable.
    	String url = "https://www.google.com/";
    	navigateToUrl(url);
    	return url;
	}
	
	protected String navigateForward() {
		String url = "";
		
		if (forward.size() > 1) {
    		url = forward.remove(forward.size() - 1);
    		navigateToUrl(url);
    	}
    	else if (forward.size() == 1) {
    		url = forward.remove(0);
    		navigateToUrl(url);
    	}
		
    	return url;
	}
	
	protected String navigateBackward() {
		String url = "";
		
		if (backward.size() > 2) {
    		addToForward(backward.remove(backward.size() - 1));
	    	url = backward.get(backward.size() - 1);
	    	navigateToUrl(url);
    	}
    	else if (backward.size() == 2) {
    		addToForward(backward.remove(backward.size() - 1));
    		url = backward.get(0);
    		navigateToUrl(url);
    	}
		
    	return url;
	}
	
	protected String navigate(String url) {
		if (!url.isEmpty()) {
			String fixedUrl = handleURLPrefix(url);
			navigateToUrl(fixedUrl);
			return fixedUrl;
		}
		return url;
	}
	
	protected String refresh() {
		String url = "";
		
		if (backward.size() > 1) {
    		url = backward.get(backward.size() - 1);
    		navigateToUrl(url);
    	}
    	else if (backward.size() == 1) {
    		url = backward.get(0);
    		navigateToUrl(url);
    	}
		
    	return url;
	}
	
	protected boolean shouldBackwardBeDisabled() {
		if (backward.size() > 1) {
    		return false;
    	}
		return true;
	}
	
	protected boolean shouldForwardBeDisabled() {
		if (forward.size() > 0) {
    		return false;
    	}
		return true;
	}
	
    protected void navigationSuccessful(String url) {
		addToHistory(url);
	}
	
    
	/*
	 * Private Methods
	 * 
	 * 
	 */
	private void navigateToUrl(String url) {
		browser.load(url);
    	navigationSuccessful(url);
	}
    
    private String handleURLPrefix(String url) {
    	if (!url.startsWith("http")) {
    		return "https://" + url;
		}
    	return url;
    }
	
	private boolean addToHistory(String url) {
		// TODO: Add in setting to disable preservation of forward pages 
		// and check that here. Clear forward if set to disabled
		boolean shouldAdd = true;
		if (backward.size() > 1) {
			if (url.equals(backward.get(backward.size() - 1))) {
				// URL is already the last item in history, DO NOT ADD
				shouldAdd = false;
			}
		}
		else if (backward.size() == 1) {
			if (url.equals(backward.get(0))) {
				// URL is already the last item in history, DO NOT ADD
				shouldAdd = false;
			}
		}
		
		if (shouldAdd) {
			backward.add(url);
		}
		return shouldAdd;
	}
	
	private boolean addToForward(String url) {
		boolean shouldAdd = true;
		if (forward.size() > 1) {
			if (url.equals(forward.get(forward.size() - 1))) {
				// URL is already the last item in forward, DO NOT ADD
				shouldAdd = false;
			}
		}
		else if (forward.size() == 1) {
			if (url.equals(forward.get(0))) {
				// URL is already the last item in forward, DO NOT ADD
				shouldAdd = false;
			}
		}
		
		if (shouldAdd) {
			forward.add(url);
		}
		return shouldAdd;
	}
}
