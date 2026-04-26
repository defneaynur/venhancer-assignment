package com.venhancer.utils.messages;

/** Log message templates for the Product Detail page. */
public final class ProductDetailMessages {

    public static final String PRODUCT_NAME           = "Product name: '{}'";
    public static final String ADD_TO_CART_START      = "Adding product to cart";
    public static final String ADD_TO_CART_OK         = "Add-to-cart confirmed: success popup appeared";
    public static final String ADD_TO_CART_RETRY      = "Success popup not seen on first attempt, retrying with regular click";
    public static final String ADD_TO_CART_RETRY_OK   = "Add-to-cart confirmed on retry";
    public static final String ADD_TO_CART_RETRY_FAIL = "Retry click failed: {}";
    public static final String ADD_TO_CART_WARN       = "Could not confirm add-to-cart; proceeding to cart anyway";
    public static final String NAVIGATING_TO_CART     = "Navigating to cart: {}";

    private ProductDetailMessages() {}
}
