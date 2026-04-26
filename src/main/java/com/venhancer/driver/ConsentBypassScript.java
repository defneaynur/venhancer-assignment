package com.venhancer.driver;

/**
 * Efilli consent payload injected into localStorage so the cookie/GDPR overlay
 * never blocks the automation.  Single source of truth — both CDP pre-injection
 * (fires before any page script) and runtime JS injection share this constant.
 */
public final class ConsentBypassScript {

    private static final String CONSENT_JSON =
        "{"
        + "\"updatedAt\":1777037811808,"
        + "\"categories\":{"
            + "\"essential\":true,"
            + "\"functional\":true,"
            + "\"marketing\":true,"
            + "\"other\":true"
        + "},"
        + "\"browserData\":{"
            + "\"userAgent\":\"Mozilla/5.0\","
            + "\"pageLoad\":1000,"
            + "\"language\":\"tr-TR\","
            + "\"networkType\":\"4g\","
            + "\"screen\":{"
                + "\"devicePixelRatio\":1,"
                + "\"height\":1080,"
                + "\"width\":1920"
            + "},"
            + "\"uuid\":\"automation-uuid\""
        + "}"
        + "}";

    /** Raw statement — used as the CDP source (runs before page scripts). */
    public static final String CDP_SOURCE =
        "window.localStorage.setItem('efl-saved-consent', '" + CONSENT_JSON + "');";

    /** Safe version wrapped in try/catch for runtime JavascriptExecutor calls. */
    public static final String RUNTIME_JS =
        "try{" + CDP_SOURCE + "}catch(e){}";

    private ConsentBypassScript() {}
}
