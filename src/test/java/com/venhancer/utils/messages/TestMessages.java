package com.venhancer.utils.messages;

/** Log message templates shared across all test classes. */
public final class TestMessages {

    public static final String SETUP           = "Setting up test — browser: {}";
    public static final String PASSED          = "Test PASSED: {}";
    public static final String FAILED          = "Test FAILED: {}";
    public static final String SCREENSHOT_OK   = "Screenshot saved: {}";
    public static final String SCREENSHOT_FAIL = "Could not save screenshot: {}";
    public static final String STARTED         = "=== Test started: {} ===";
    public static final String FINISHED        = "=== Test finished: {} ===";
    public static final String APPLYING_FILTER = "Applying first available filter";
    public static final String CART_TITLE      = "Cart title: '{}'";

    private TestMessages() {}
}
