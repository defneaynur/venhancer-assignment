package com.venhancer.utils;

import com.venhancer.utils.messages.WaitMessages;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

/**
 * Centralised explicit-wait facade for the framework.
 * All methods create a fresh {@link WebDriverWait} internally so callers never
 * need to instantiate one directly.
 */
public final class WaitHelper {

    private static final Logger log = LoggerFactory.getLogger(WaitHelper.class);

    /** Default wait timeout used for most element interactions (seconds). */
    public static final int DEFAULT_TIMEOUT  = 20;
    /** Short wait timeout used for quick presence checks (seconds). */
    public static final int SHORT_TIMEOUT    = 5;
    /** Tight timeout for dismissing optional overlays without blocking the test (seconds). */
    public static final int DISMISS_TIMEOUT  = 4;

    private WaitHelper() {}

    /**
     * Waits until the element located by {@code locator} is visible on the page.
     *
     * @param driver  the active WebDriver
     * @param locator the By locator of the element
     * @return the visible WebElement
     */
    public static WebElement forVisible(WebDriver driver, By locator) {
        log.debug(WaitMessages.VISIBLE, locator);
        return of(driver, DEFAULT_TIMEOUT).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits until the given {@link WebElement} is visible.
     *
     * @param driver  the active WebDriver
     * @param element the element to wait for
     * @return the visible element
     */
    public static WebElement forVisible(WebDriver driver, WebElement element) {
        log.debug(WaitMessages.VISIBLE, element);
        return of(driver, DEFAULT_TIMEOUT).until(ExpectedConditions.visibilityOf(element));
    }

    public static WebElement forClickable(WebDriver driver, By locator) {
        log.debug(WaitMessages.CLICKABLE, locator);
        return of(driver, DEFAULT_TIMEOUT).until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Waits until the given {@link WebElement} is clickable.
     *
     * @param driver  the active WebDriver
     * @param element the element to wait for
     * @return the clickable element
     */
    public static WebElement forClickable(WebDriver driver, WebElement element) {
        log.debug(WaitMessages.CLICKABLE, element);
        return of(driver, DEFAULT_TIMEOUT).until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Waits until all elements matching {@code locator} are visible.
     *
     * @param driver  the active WebDriver
     * @param locator the By locator for the elements
     * @return a list of all matching visible elements
     */
    public static List<WebElement> forAllVisible(WebDriver driver, By locator) {
        log.debug(WaitMessages.ALL_VISIBLE, locator);
        return of(driver, DEFAULT_TIMEOUT).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    /**
     * Returns {@code true} if the element located by {@code locator} becomes visible
     * within {@link #SHORT_TIMEOUT} seconds; {@code false} if the wait times out.
     *
     * @param driver  the active WebDriver
     * @param locator the By locator of the element
     * @return true if the element is visible within the short timeout, false otherwise
     */
    public static boolean isPresent(WebDriver driver, By locator) {
        try {
            of(driver, SHORT_TIMEOUT).until(ExpectedConditions.visibilityOfElementLocated(locator));
            log.debug(WaitMessages.PRESENT, locator);
            return true;
        } catch (TimeoutException e) {
            log.debug(WaitMessages.NOT_PRESENT, locator);
            return false;
        }
    }

    /**
     * Waits until {@code document.readyState} equals {@code "complete"}.
     *
     * @param driver the active WebDriver
     */
    public static void forPageLoad(WebDriver driver) {
        log.debug(WaitMessages.PAGE_LOAD);
        of(driver, DEFAULT_TIMEOUT).until(d ->
                "complete".equals(((JavascriptExecutor) d).executeScript("return document.readyState")));
    }

    /**
     * Waits until the current URL contains the given {@code fragment}.
     *
     * @param driver   the active WebDriver
     * @param fragment the URL substring to wait for
     */
    public static void forUrlContains(WebDriver driver, String fragment) {
        log.debug(WaitMessages.URL_CONTAINS, fragment);
        of(driver, DEFAULT_TIMEOUT).until(ExpectedConditions.urlContains(fragment));
    }

    public static void dismissIfPresent(WebDriver driver, By locator) {
        try {
            of(driver, DISMISS_TIMEOUT).until(ExpectedConditions.elementToBeClickable(locator)).click();
            log.info(WaitMessages.DISMISSED, locator);
        } catch (TimeoutException e) {
            log.debug(WaitMessages.NOTHING_TO_DISMISS, locator);
        }
    }

    private static WebDriverWait of(WebDriver driver, int seconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(seconds));
    }
}
