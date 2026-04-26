package com.venhancer.pages;

import com.venhancer.pages.locators.HomeLocators;
import com.venhancer.utils.messages.HomeMessages;
import io.qameta.allure.Step;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the Home Page of the application.
 * Contains methods to interact with the search bar and navigate the application.
 */
public class HomePage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(HomePage.class);
    private static final String SEARCH_URL_FRAGMENT = "/ara";

    /**
     * Navigates to the given URL and waits for the page to fully stabilise.
     * Hepsiburada performs an automatic reload (cookie / A-B redirect) shortly
     * after the initial load, so we wait for the URL to stop changing.
     *
     * @param url the full URL to open
     */
    @Step("Open home page: {url}")
    public void open(String url) {
        log.info(HomeMessages.NAVIGATING, url);
        driver.get(url);
        waitForPageLoad();
        waitForUrlStable();       // waits until URL stops changing after A-B redirect
        waitForPageLoad();        // second readyState=complete after reload
        waitForClickable(HomeLocators.SEARCH_INPUT);
        log.info(HomeMessages.PAGE_READY);
    }

    /**
     * Types {@code keyword} into the search box and presses ENTER.
     * The entire find → click → type → verify chain is wrapped in a single retry loop
     * so that an A/B redirect firing mid-interaction simply restarts the sequence.
     *
     * @param keyword the search query to enter
     * @return a new instance of SearchResultsPage
     */
    @Step("Search for '{keyword}'")
    public SearchResultsPage search(String keyword) {
        log.info(HomeMessages.SEARCH_START, keyword);

        wait.ignoring(StaleElementReferenceException.class).until(d -> {
            WebElement input = d.findElement(HomeLocators.SEARCH_INPUT);
            new Actions(d).moveToElement(input).click().perform();
            if (!"input".equalsIgnoreCase(d.switchTo().activeElement().getTagName())) {
                return false;
            }
            WebElement active = d.switchTo().activeElement();
            active.clear();
            active.sendKeys(keyword);
            String value = active.getAttribute("value");
            return value != null && value.contains(keyword);
        });

        driver.switchTo().activeElement().sendKeys(Keys.ENTER);

        waitForUrlContains(SEARCH_URL_FRAGMENT);
        waitForPageLoad();
        return new SearchResultsPage();
    }

    // ------------------------------------------------------------------ //
    //  Helpers                                                             //
    // ------------------------------------------------------------------ //

    /**
     * Polls the current URL every 500 ms until two consecutive reads are identical
     * and {@code document.readyState} is {@code "complete"}.
     * Needed because Hepsiburada fires an automatic redirect shortly after landing.
     */
    private void waitForUrlStable() {
        log.debug(HomeMessages.URL_STABILISING);
        final String[] prev = {""};
        wait.until(d -> {
            String current = d.getCurrentUrl();
            boolean stable = current.equals(prev[0])
                && "complete".equals(((JavascriptExecutor) d).executeScript("return document.readyState"));
            prev[0] = current;
            if (stable) log.debug(HomeMessages.URL_STABLE, current);
            return stable;
        });
    }
}
