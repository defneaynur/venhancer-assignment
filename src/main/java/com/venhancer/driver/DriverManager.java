package com.venhancer.driver;

import org.openqa.selenium.WebDriver;

/**
 * Thread-safe holder for the active WebDriver instance.
 * Uses a {@link ThreadLocal} so that parallel test threads each get their own driver.
 */
public final class DriverManager {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverManager() {}

    /**
     * Returns the WebDriver bound to the current thread, or {@code null} if none has been initialised.
     *
     * @return the current thread's WebDriver instance
     */
    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    /**
     * Creates and stores a new WebDriver for the current thread using the given browser name.
     * The browser name is resolved via {@link BrowserDriverFactory}.
     *
     * @param browser the browser identifier (e.g. "chrome", "firefox", "chrome-headless")
     */
    public static void initDriver(String browser) {
        driverThreadLocal.set(BrowserDriverFactory.forBrowser(browser).create());
    }

    /**
     * Quits the WebDriver and removes it from the current thread's storage.
     * Safe to call even when no driver is initialised.
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
        }
    }
}
