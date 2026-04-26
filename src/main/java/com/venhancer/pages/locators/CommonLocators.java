package com.venhancer.pages.locators;

import org.openqa.selenium.By;

/**
 * Selenium locators shared across multiple pages (e.g. generic overlays and modals).
 */
public final class CommonLocators {

    /** Close button for any modal or cookie/consent overlay that may block interactions. */
    public static final By POPUP_CLOSE = By.cssSelector(
        "[data-test-id='modal-close'], button[aria-label='Kapat'], button[aria-label='Close']"
    );

    private CommonLocators() {}
}
