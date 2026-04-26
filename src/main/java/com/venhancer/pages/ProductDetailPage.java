package com.venhancer.pages;

import com.venhancer.driver.ConsentBypassScript;
import com.venhancer.pages.locators.CommonLocators;
import com.venhancer.pages.locators.ProductDetailLocators;
import com.venhancer.utils.ConfigReader;
import com.venhancer.utils.messages.ProductDetailMessages;
import io.qameta.allure.Step;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Represents the Product Detail Page.
 * Contains methods to read product details and add items to the cart.
 */
public class ProductDetailPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(ProductDetailPage.class);
    private static final Duration CART_CONFIRMATION_TIMEOUT = Duration.ofSeconds(10);

    /**
     * Checks if the product detail page is fully loaded and displayed.
     *
     * @return true if the product title is visible, false otherwise
     */
    @Step("Check if product page is displayed")
    public boolean isProductPageDisplayed() {
        return isElementPresent(ProductDetailLocators.PRODUCT_TITLE);
    }

    /**
     * Retrieves the name of the product.
     * Retries on {@link org.openqa.selenium.StaleElementReferenceException} because
     * the page updates its DOM dynamically after the initial load.
     *
     * @return the product name string
     */
    @Step("Get product name")
    public String getProductName() {
        return wait.until(driver -> {
            try {
                WebElement el = driver.findElement(ProductDetailLocators.PRODUCT_TITLE);
                String name = el.getText();
                if (name != null && !name.trim().isEmpty()) {
                    log.info(ProductDetailMessages.PRODUCT_NAME, name);
                    return name;
                }
                return null;
            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                return null;
            }
        });
    }

    /**
     * Adds the current product to the cart.
     * Injects the consent bypass script, clicks "Add to Cart", waits for the
     * success popup, retries once if not confirmed, then navigates to the cart page.
     *
     * @return a new instance of CartPage
     */
    @Step("Add product to cart")
    public CartPage addToCart() {
        log.info(ProductDetailMessages.ADD_TO_CART_START);
        ((JavascriptExecutor) driver).executeScript(ConsentBypassScript.RUNTIME_JS);

        dismissIfPresent(CommonLocators.POPUP_CLOSE);

        WebElement addBtn = waitForClickable(ProductDetailLocators.ADD_TO_CART_BTN);
        scrollToElement(addBtn);
        safeClick(addBtn);

        boolean confirmed = waitForCartConfirmation();

        if (!confirmed) {
            log.warn(ProductDetailMessages.ADD_TO_CART_RETRY);
            dismissIfPresent(CommonLocators.POPUP_CLOSE);
            try {
                safeClick(ProductDetailLocators.ADD_TO_CART_BTN);
                confirmed = waitForCartConfirmation();
            } catch (Exception e) {
                log.warn(ProductDetailMessages.ADD_TO_CART_RETRY_FAIL, e.getMessage());
            }

            if (confirmed) log.info(ProductDetailMessages.ADD_TO_CART_RETRY_OK);
            else           log.warn(ProductDetailMessages.ADD_TO_CART_WARN);
        } else {
            log.info(ProductDetailMessages.ADD_TO_CART_OK);
        }

        String cartUrl = ConfigReader.get("cart.url");
        log.info(ProductDetailMessages.NAVIGATING_TO_CART, cartUrl);
        driver.get(cartUrl);
        waitForPageLoad();
        ((JavascriptExecutor) driver).executeScript(ConsentBypassScript.RUNTIME_JS);
        return new CartPage();
    }

    /**
     * Waits up to 10 seconds for the "Go to Cart" success popup to appear.
     *
     * @return true if the popup appeared, false if the wait timed out
     */
    private boolean waitForCartConfirmation() {
        try {
            new WebDriverWait(driver, CART_CONFIRMATION_TIMEOUT)
                    .until(ExpectedConditions.visibilityOfElementLocated(ProductDetailLocators.SEPETE_GIT_POPUP));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}
