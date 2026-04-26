package com.venhancer.utils.messages;

/** Log message templates and page-specific keyword constants for the Cart page. */
public final class CartMessages {

    public static final String CHECKING_ITEMS  = "Checking if cart has items";
    public static final String BODY_PREVIEW    = "Cart page body (first 400 chars): {}";
    public static final String CHECK_TIMEOUT   = "Cart check timed out. Page text (first 400 chars): {}";
    public static final String PRODUCT_IN_CART = "Product '{}' in cart: {}";
    public static final String PAGE_TITLE      = "Cart page title: '{}'";
    public static final String TITLE_FAIL      = "Could not read cart page title";

    /**
     * Keywords present in the cart page body when items exist.
     * Used to detect a populated cart without relying on fragile element locators.
     */
    public static final String KEYWORD_CHECKOUT      = "tamamla";
    public static final String KEYWORD_ORDER_SUMMARY = "Sipariş Özeti";
    public static final String KEYWORD_CART_TOTAL    = "Sepet Tutarı";

    private CartMessages() {}
}
