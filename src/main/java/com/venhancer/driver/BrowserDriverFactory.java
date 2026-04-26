package com.venhancer.driver;

import com.venhancer.driver.impl.ChromeBrowserDriver;
import com.venhancer.driver.impl.ChromeHeadlessBrowserDriver;
import com.venhancer.driver.impl.FirefoxBrowserDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Registry that maps browser name strings to their factory suppliers.
 * Adding a new browser = implement BrowserDriver + one line here.
 */
public final class BrowserDriverFactory {

    private static final Logger log = LoggerFactory.getLogger(BrowserDriverFactory.class);

    private static final Map<String, Supplier<BrowserDriver>> REGISTRY = Map.of(
        "chrome",          ChromeBrowserDriver::new,
        "chrome-headless", ChromeHeadlessBrowserDriver::new,
        "firefox",         FirefoxBrowserDriver::new
    );

    private BrowserDriverFactory() {}

    /**
     * Returns the BrowserDriver for the given name.
     * Logs a warning and falls back to Chrome if the name is not registered.
     *
     * @param browser the browser identifier (e.g. "chrome", "firefox", "chrome-headless")
     * @return the matching BrowserDriver, or a ChromeBrowserDriver as fallback
     */
    public static BrowserDriver forBrowser(String browser) {
        Supplier<BrowserDriver> supplier = REGISTRY.get(browser.toLowerCase());
        if (supplier == null) {
            log.warn("Unknown browser '{}' — falling back to Chrome", browser);
            return new ChromeBrowserDriver();
        }
        return supplier.get();
    }
}
