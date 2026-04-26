package com.venhancer.driver.impl;

import io.github.bonigarcia.wdm.WebDriverManager;
import com.venhancer.driver.BrowserDriver;
import com.venhancer.utils.messages.DriverMessages;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link BrowserDriver} implementation that launches a visible Firefox window.
 */
public class FirefoxBrowserDriver implements BrowserDriver {

    private static final Logger log = LoggerFactory.getLogger(FirefoxBrowserDriver.class);

    /**
     * Resolves the geckodriver binary and starts Firefox with the configured options.
     *
     * @return a fully initialised {@link FirefoxDriver}
     */
    @Override
    public WebDriver create() {
        log.info(DriverMessages.FIREFOX_INIT);
        WebDriverManager.firefoxdriver().setup();
        return new FirefoxDriver(options());
    }

    /**
     * Builds the {@link FirefoxOptions} used when launching the browser.
     *
     * @return configured FirefoxOptions
     */
    protected FirefoxOptions options() {
        FirefoxOptions opts = new FirefoxOptions();
        opts.addArguments("--start-maximized");
        return opts;
    }
}
