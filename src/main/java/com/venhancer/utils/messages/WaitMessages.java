package com.venhancer.utils.messages;

/** Log message templates for the WaitHelper utility. */
public final class WaitMessages {

    public static final String VISIBLE            = "Waiting for visible: {}";
    public static final String CLICKABLE          = "Waiting for clickable: {}";
    public static final String ALL_VISIBLE        = "Waiting for all visible: {}";
    public static final String PAGE_LOAD          = "Waiting for page load";
    public static final String URL_CONTAINS       = "Waiting for URL to contain: {}";
    public static final String PRESENT            = "Element present: {}";
    public static final String NOT_PRESENT        = "Element not present: {}";
    public static final String DISMISSED          = "Dismissed overlay: {}";
    public static final String NOTHING_TO_DISMISS = "No overlay to dismiss: {}";

    private WaitMessages() {}
}
