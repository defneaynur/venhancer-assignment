package com.venhancer.utils.messages;

/** Log message templates for WebDriver initialisation and configuration. */
public final class DriverMessages {

    public static final String CHROME_INIT          = "Initializing Chrome driver";
    public static final String CHROME_HEADLESS_INIT = "Configuring Chrome in headless mode";
    public static final String FIREFOX_INIT         = "Initializing Firefox driver";
    public static final String CDP_CONSENT_OK       = "CDP consent injection successful";
    public static final String CDP_CONSENT_SKIP     = "CDP consent injection skipped (version mismatch): {}";

    private DriverMessages() {}
}
