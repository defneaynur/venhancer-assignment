package com.venhancer.driver;

import org.openqa.selenium.WebDriver;

/**
 * Strategy interface — each browser type knows how to create its own WebDriver.
 * Adding a new browser = implement this interface + register in BrowserDriverFactory.
 */
public interface BrowserDriver {
    WebDriver create();
}
