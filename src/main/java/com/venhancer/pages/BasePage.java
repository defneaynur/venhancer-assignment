package com.venhancer.pages;

import com.venhancer.driver.DriverManager;
import com.venhancer.utils.JsHelper;
import com.venhancer.utils.WaitHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

/**
 * The base abstract class for all page object models.
 * Provides common synchronization and interaction helpers used across the framework.
 */
public abstract class BasePage {

    private static final Logger log = LoggerFactory.getLogger(BasePage.class);

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    /**
     * Initializes the WebDriver and sets up default wait configurations.
     */
    protected BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(WaitHelper.DEFAULT_TIMEOUT));
        PageFactory.initElements(driver, this);
    }

    /**
     * Waits until the element located by the given locator is visible on the page.
     *
     * @param locator the By locator of the element
     * @return the visible WebElement
     */
    protected WebElement waitForVisible(By locator) {
        return WaitHelper.forVisible(driver, locator);
    }

    /**
     * Waits until the given element is visible on the page.
     *
     * @param element the WebElement to wait for
     * @return the visible element
     */
    protected WebElement waitForVisible(WebElement element) {
        return WaitHelper.forVisible(driver, element);
    }

    /**
     * Waits until the element located by the given locator is clickable.
     *
     * @param locator the By locator of the element
     * @return the clickable WebElement
     */
    protected WebElement waitForClickable(By locator) {
        return WaitHelper.forClickable(driver, locator);
    }

    /**
     * Waits until the given element is clickable.
     *
     * @param element the WebElement to wait for
     * @return the clickable element
     */
    protected WebElement waitForClickable(WebElement element) {
        return WaitHelper.forClickable(driver, element);
    }

    /**
     * Waits until all elements matching the given locator are visible.
     *
     * @param locator the By locator for the elements
     * @return a list of all matching visible elements
     */
    protected List<WebElement> waitForAllVisible(By locator) {
        return WaitHelper.forAllVisible(driver, locator);
    }

    /**
     * Checks if an element is present in the DOM (not necessarily visible).
     *
     * @param locator the By locator of the element
     * @return true if the element is present, false otherwise
     */
    protected boolean isElementPresent(By locator) {
        return WaitHelper.isPresent(driver, locator);
    }

    /**
     * Waits until the browser's document readyState is 'complete'.
     */
    protected void waitForPageLoad() {
        WaitHelper.forPageLoad(driver);
    }

    /**
     * Waits until the current URL contains the given fragment.
     *
     * @param fragment the URL substring to wait for
     */
    protected void waitForUrlContains(String fragment) {
        WaitHelper.forUrlContains(driver, fragment);
    }

    /**
     * Checks if a specific popup/overlay is present and visible, and dismisses it if so.
     *
     * @param locator the By locator of the popup's close button or overlay
     */
    protected void dismissIfPresent(By locator) {
        WaitHelper.dismissIfPresent(driver, locator);
    }

    /**
     * Safely retrieves the visible (and trimmed) text of a given web element.
     *
     * @param element the WebElement to retrieve text from
     * @return the trimmed string text
     */
    protected String getText(WebElement element) {
        return waitForVisible(element).getText().trim();
    }

    /**
     * Waits for the element located by the given locator to be clickable, then clicks it.
     *
     * @param locator the By locator of the element
     */
    protected void click(By locator) {
        waitForClickable(locator).click();
    }

    /**
     * Waits for the given element to be clickable, then clicks it.
     *
     * @param element the WebElement to click
     */
    protected void click(WebElement element) {
        waitForClickable(element).click();
    }

    /**
     * Clears the given input field and types the specified text into it.
     *
     * @param element the input WebElement
     * @param text    the text to enter
     */
    protected void type(WebElement element, String text) {
        waitForVisible(element).clear();
        element.sendKeys(text);
    }

    /**
     * Attempts a normal Selenium click; falls back to a JavaScript click if the element
     * is intercepted by an overlay ({@link ElementNotInteractableException}) or is not
     * interactable ({@link ElementNotInteractableException}).
     * <p>
     * Prefer {@link #safeClick(By)} when the element may be stale, since that overload
     * re-fetches the element before clicking.
     *
     * @param element the WebElement to click
     */
    protected void safeClick(WebElement element) {
        try {
            element.click();
            log.debug("safeClick: normal click succeeded on {}", element);
        } catch (ElementNotInteractableException e) {
            log.warn("safeClick: click blocked ({}), falling back to JS click", e.getClass().getSimpleName());
            JsHelper.click(driver, element);
        }
    }

    /**
     * Re-fetches the element by {@code locator} (guarding against stale references),
     * then attempts a normal Selenium click; falls back to a JavaScript click if the
     * element is intercepted or not interactable.
     *
     * @param locator the By locator used to find the element
     */
    protected void safeClick(By locator) {
        WebElement element = waitForClickable(locator);
        try {
            element.click();
            log.debug("safeClick: normal click succeeded on {}", locator);
        } catch (ElementNotInteractableException e) {
            log.warn("safeClick: click blocked ({}), falling back to JS click on {}", e.getClass().getSimpleName(), locator);
            JsHelper.click(driver, element);
        }
    }

    /**
     * Scrolls the page until the specified element is in view.
     *
     * @param element the WebElement to scroll to
     */
    protected void scrollToElement(WebElement element) {
        JsHelper.scrollTo(driver, element);
    }
}
