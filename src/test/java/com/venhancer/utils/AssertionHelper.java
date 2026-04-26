package com.venhancer.utils;

import com.venhancer.utils.messages.AssertionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

/**
 * Logging wrapper around TestNG {@link Assert}.
 * Every assertion also emits an INFO (pass) or ERROR (fail) log line so test
 * results are visible in the SLF4J output without reading the TestNG report.
 */
public final class AssertionHelper {

    private static final Logger log = LoggerFactory.getLogger(AssertionHelper.class);

    private AssertionHelper() {}

    /**
     * Asserts that {@code condition} is {@code true}.
     *
     * @param condition the boolean expression to evaluate
     * @param message   the failure message shown in the report
     */
    public static void assertTrue(boolean condition, String message) {
        logResult(condition, message);
        Assert.assertTrue(condition, message);
    }

    /**
     * Asserts that {@code condition} is {@code false}.
     *
     * @param condition the boolean expression to evaluate
     * @param message   the failure message shown in the report
     */
    public static void assertFalse(boolean condition, String message) {
        logResult(!condition, message);
        Assert.assertFalse(condition, message);
    }

    /**
     * Asserts that {@code value} is neither {@code null} nor empty.
     *
     * @param value   the string to check
     * @param message the failure message shown in the report
     */
    public static void assertNotEmpty(String value, String message) {
        boolean pass = value != null && !value.isEmpty();
        logResult(pass, message);
        Assert.assertFalse(value == null || value.isEmpty(), message);
    }

    /**
     * Asserts that {@code actual} equals {@code expected}.
     * Logs the expected/actual values on failure for easier diagnosis.
     *
     * @param actual   the value produced by the system under test
     * @param expected the value the test expects
     * @param message  the failure message shown in the report
     */
    public static void assertEqual(Object actual, Object expected, String message) {
        boolean pass = expected.equals(actual);
        logResult(pass, message);
        if (!pass) log.error(AssertionMessages.EQUAL_DETAIL, expected, actual);
        Assert.assertEquals(actual, expected, message);
    }

    private static void logResult(boolean pass, String message) {
        if (pass) log.info(AssertionMessages.PASS, message);
        else      log.error(AssertionMessages.FAIL, message);
    }
}
