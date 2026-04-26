package com.venhancer.pages;

import com.venhancer.pages.locators.CommonLocators;
import com.venhancer.pages.locators.SearchResultsLocators;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * Represents the Search Results Page.
 * Contains methods to verify search results, apply filters, and select products.
 */
public class SearchResultsPage extends BasePage {

    /**
     * Checks whether any product cards are displayed on the search results page.
     *
     * @return true if at least one product is displayed, false otherwise
     */
    @Step("Check if search results are displayed")
    public boolean areResultsDisplayed() {
        try {
            List<WebElement> products = wait.until(
                    ExpectedConditions.presenceOfAllElementsLocatedBy(SearchResultsLocators.PRODUCT_CARDS));
            return !products.isEmpty();
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Gets the total count of currently loaded product cards on the page.
     *
     * @return the number of products
     */
    @Step("Get search result count")
    public int getResultCount() {
        return driver.findElements(SearchResultsLocators.PRODUCT_CARDS).size();
    }

    /**
     * Finds the first available (unchecked) filter button on the left sidebar,
     * scrolls to it, clicks it, and waits for the results to reload.
     */
    @Step("Apply first available filter")
    public void applyFirstAvailableFilter() {
        dismissIfPresent(CommonLocators.POPUP_CLOSE);

        List<WebElement> filterBtns = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(SearchResultsLocators.FILTER_BTN_UNCHECKED));

        WebElement firstFilter = filterBtns.get(0);
        scrollToElement(firstFilter);
        safeClick(firstFilter);

        waitForPageLoad();
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(SearchResultsLocators.PRODUCT_CARDS));
    }

    /**
     * Selects the first organic product from the search results, bypassing ads.
     * Extracts the product link and navigates directly to it to avoid
     * click-interceptions or new tab complexities.
     *
     * @return a new instance of ProductDetailPage
     */
    @Step("Click first product from results")
    public ProductDetailPage clickFirstProduct() {
        List<WebElement> products = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(SearchResultsLocators.PRODUCT_CARDS));

        dismissIfPresent(CommonLocators.POPUP_CLOSE);

        WebElement anchor;
        try {
            anchor = products.get(0).findElement(By.tagName("a"));
        } catch (NoSuchElementException e) {
            anchor = products.get(1).findElement(By.tagName("a"));
        }
        String href = anchor.getAttribute("href");
        driver.get(href);
        waitForPageLoad();

        return new ProductDetailPage();
    }
}
