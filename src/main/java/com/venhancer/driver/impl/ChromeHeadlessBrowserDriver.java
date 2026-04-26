package com.venhancer.driver.impl;

import com.venhancer.utils.messages.DriverMessages;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link ChromeBrowserDriver} variant that runs Chrome in headless mode.
 * Intended for CI environments where a display server is unavailable.
 * Inherits CDP consent injection from the parent class.
 */
public class ChromeHeadlessBrowserDriver extends ChromeBrowserDriver {

    private static final Logger log = LoggerFactory.getLogger(ChromeHeadlessBrowserDriver.class);

    /**
     * Returns {@link ChromeOptions} configured for headless execution with a fixed 1920×1080 viewport.
     *
     * @return headless-mode ChromeOptions
     */
    @Override
    protected ChromeOptions options() {
        log.info(DriverMessages.CHROME_HEADLESS_INIT);
        ChromeOptions opts = new ChromeOptions();
        opts.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage",
                "--disable-notifications", "--window-size=1920,1080");
        return opts;
    }
}
