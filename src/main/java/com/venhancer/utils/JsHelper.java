package com.venhancer.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Thin wrapper around {@link JavascriptExecutor} for common JS operations.
 * Useful when native Selenium interactions are blocked by overlays or animations.
 */
public final class JsHelper {

    private JsHelper() {}

    /**
     * Clicks an element via JavaScript, bypassing visibility or interception issues.
     *
     * @param driver  the active WebDriver
     * @param element the element to click
     */
    public static void click(WebDriver driver, WebElement element) {
        js(driver).executeScript("arguments[0].click();", element);
    }

    /**
     * Scrolls the page so that the given element is centred in the viewport.
     *
     * @param driver  the active WebDriver
     * @param element the element to scroll into view
     */
    public static void scrollTo(WebDriver driver, WebElement element) {
        js(driver).executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }

    /**
     * Executes an arbitrary JavaScript snippet without returning a value.
     *
     * @param driver the active WebDriver
     * @param script the JavaScript code to execute
     */
    public static void execute(WebDriver driver, String script) {
        js(driver).executeScript(script);
    }

    /**
     * Executes a JavaScript snippet and returns its result.
     *
     * @param driver the active WebDriver
     * @param script the JavaScript code to execute
     * @return the value returned by the script, or {@code null}
     */
    public static Object executeWithReturn(WebDriver driver, String script) {
        return js(driver).executeScript(script);
    }

    private static JavascriptExecutor js(WebDriver driver) {
        return (JavascriptExecutor) driver;
    }
}
