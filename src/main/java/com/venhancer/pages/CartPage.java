package com.venhancer.pages;

import com.venhancer.utils.messages.CartMessages;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Represents the Cart Page.
 * Contains methods to verify the items and state of the shopping cart.
 */
public class CartPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(CartPage.class);

    /** Maximum prefix length used when matching a product name against the cart body text. */
    private static final int PRODUCT_NAME_PREFIX_LENGTH = 25;
    private static final int BODY_PREVIEW_LENGTH = 400;
    private static final Duration CART_LOAD_TIMEOUT = Duration.ofSeconds(30);

    /**
     * Checks if the cart contains any items by scanning the body text for known keywords.
     *
     * @return true if items are present, false otherwise
     */
    @Step("Check if cart has items")
    public boolean hasItemsInCart() {
        waitForPageLoad();
        log.info(CartMessages.CHECKING_ITEMS);
        try {
            new WebDriverWait(driver, CART_LOAD_TIMEOUT).until(driver -> {
                String text = driver.findElement(By.tagName("body")).getText();
                return containsCartKeyword(text);
            });
            String bodyText = driver.findElement(By.tagName("body")).getText();
            log.debug(CartMessages.BODY_PREVIEW, bodyText.substring(0, Math.min(BODY_PREVIEW_LENGTH, bodyText.length())));
            return containsCartKeyword(bodyText);
        } catch (Exception e) {
            String bodyText = "";
            try {
                bodyText = driver.findElement(By.tagName("body")).getText();
            } catch (Exception inner) {
                log.warn("hasItemsInCart: could not read body text — {}", inner.getMessage());
            }
            log.warn(CartMessages.CHECK_TIMEOUT, bodyText.substring(0, Math.min(BODY_PREVIEW_LENGTH, bodyText.length())));
            return false;
        }
    }

    /**
     * Verifies if a specific product (or part of its name) is listed in the cart.
     * Compares a prefix of the product name against the lowercase page body text.
     *
     * @param productName the full or partial name of the product
     * @return true if the product is found in the cart, false otherwise
     */
    @Step("Check if '{productName}' is in cart")
    public boolean isProductInCart(String productName) {
        if (productName == null || productName.isBlank()) return false;
        String bodyText = driver.findElement(By.tagName("body")).getText().toLowerCase();
        String needle   = productName.substring(0, Math.min(PRODUCT_NAME_PREFIX_LENGTH, productName.length())).toLowerCase();
        boolean found   = bodyText.contains(needle);
        log.info(CartMessages.PRODUCT_IN_CART, needle, found);
        return found;
    }

    /**
     * Retrieves the main title of the cart page (typically an h1 element).
     *
     * @return the string title of the cart page, or an empty string if not found
     */
    @Step("Get cart page title")
    public String getCartPageTitle() {
        try {
            String title = waitForVisible(By.tagName("h1")).getText().trim();
            log.info(CartMessages.PAGE_TITLE, title);
            return title;
        } catch (Exception e) {
            log.warn(CartMessages.TITLE_FAIL);
            return "";
        }
    }

    private boolean containsCartKeyword(String text) {
        return text.contains(CartMessages.KEYWORD_CHECKOUT)
            || text.contains(CartMessages.KEYWORD_ORDER_SUMMARY)
            || text.contains(CartMessages.KEYWORD_CART_TOTAL);
    }
}
