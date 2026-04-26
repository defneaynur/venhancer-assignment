package com.venhancer.pages.locators;

import org.openqa.selenium.By;

/**
 * Selenium locators for elements on the Search Results page.
 */
public final class SearchResultsLocators {

    /**
     * Organic product list items only — identified by sequential {@code id} attributes
     * ({@code i0}, {@code i1}, …) to exclude sponsored/ad cards.
     */
    public static final By PRODUCT_CARDS = By.cssSelector("ul[class*='productListContent'] > li[id^='i']");

    /** Filter toggle buttons in the left sidebar that have not yet been selected. */
    public static final By FILTER_BTN_UNCHECKED = By.cssSelector(
        "button[data-test-id='not_checked']"
    );

    private SearchResultsLocators() {}
}
