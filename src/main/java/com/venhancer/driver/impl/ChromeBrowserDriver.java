package com.venhancer.driver.impl;

import io.github.bonigarcia.wdm.WebDriverManager;
import com.venhancer.driver.BrowserDriver;
import com.venhancer.driver.ConsentBypassScript;
import com.venhancer.utils.messages.DriverMessages;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * {@link BrowserDriver} implementation that launches a visible Chrome window.
 * Subclasses can override {@link #options()} to customise Chrome flags (e.g. headless mode).
 */
public class ChromeBrowserDriver implements BrowserDriver {

    private static final Logger log = LoggerFactory.getLogger(ChromeBrowserDriver.class);

    /**
     * Resolves the chromedriver binary, applies browser options, and injects the
     * consent bypass script via CDP so cookie overlays never appear.
     *
     * @return a fully initialised {@link ChromeDriver}
     */
    @Override
    public WebDriver create() {
        log.info(DriverMessages.CHROME_INIT);
        WebDriverManager.chromedriver().setup();
        ChromeDriver driver = new ChromeDriver(options());
        injectConsent(driver);
        return driver;
    }

    /**
     * Builds the {@link ChromeOptions} used when launching the browser.
     * Override in subclasses to add or replace arguments.
     *
     * @return configured ChromeOptions
     */
    protected ChromeOptions options() {
        ChromeOptions opts = new ChromeOptions();
        opts.addArguments("--start-maximized", "--disable-notifications", "--disable-popup-blocking");
        return opts;
    }

    /**
     * Injects {@link ConsentBypassScript#CDP_SOURCE} via the Chrome DevTools Protocol so the
     * consent localStorage key is present before any page script runs.
     * Logs a warning and continues if the CDP command is unavailable (e.g. version mismatch).
     *
     * @param driver the initialised ChromeDriver to inject into
     */
    protected void injectConsent(ChromeDriver driver) {
        try {
            driver.executeCdpCommand(
                "Page.addScriptToEvaluateOnNewDocument",
                Map.of("source", ConsentBypassScript.CDP_SOURCE)
            );
            log.debug(DriverMessages.CDP_CONSENT_OK);
        } catch (Exception e) {
            log.warn(DriverMessages.CDP_CONSENT_SKIP, e.getMessage());
        }
    }
}
