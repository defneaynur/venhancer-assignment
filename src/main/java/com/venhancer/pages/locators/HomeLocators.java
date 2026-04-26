package com.venhancer.pages.locators;

import org.openqa.selenium.By;

/**
 * Selenium locators for elements on the Hepsiburada home page.
 */
public final class HomeLocators {

    /** The main search bar input field used to enter product queries. */
    public static final By SEARCH_INPUT = By.cssSelector(
        "input[data-test-id='search-bar-input']"
    );

    private HomeLocators() {}
}
