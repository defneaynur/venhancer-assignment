package com.venhancer.pages.locators;

import org.openqa.selenium.By;

/**
 * Selenium locators and constants for the Product Detail page.
 */
public final class ProductDetailLocators {

    /** The main heading element that displays the product name. Uses multiple selectors for resilience. */
    public static final By PRODUCT_TITLE = By.cssSelector(
        "h1[data-test-id='title'], h1[class*='product-name'], h1[itemprop='name'], h1"
    );

    /** The primary "Add to Cart" button. Covers both known data-test-id variants. */
    public static final By ADD_TO_CART_BTN = By.cssSelector(
        "[data-test-id='addToCart'], [data-test-id='add-to-cart']"
    );

    /**
     * The "Go to Cart" confirmation element that appears after a successful add-to-cart action.
     * Covers button text, link text, and data-test-id variants.
     */
    public static final By SEPETE_GIT_POPUP = By.xpath(
        "//button[contains(.,'Sepete git') or contains(.,'Sepete Git')] | "
        + "//a[contains(.,'Sepete git') or contains(.,'Sepete Git')] | "
        + "//*[@data-test-id='go-to-basket'] | //*[@data-test-id='goToCart']"
    );

    private ProductDetailLocators() {}
}
