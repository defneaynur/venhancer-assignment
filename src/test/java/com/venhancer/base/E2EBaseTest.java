package com.venhancer.base;

import com.venhancer.driver.DriverManager;
import com.venhancer.utils.ConfigReader;
import com.venhancer.utils.ScreenshotHelper;
import com.venhancer.utils.messages.TestMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

/**
 * Base for E2E flow tests — the browser opens once per class and stays open
 * across all {@code @Test} methods. Use this when test methods depend on each other's state.
 *
 * <p>For independent tests that each need a fresh browser, extend {@link BaseTest} instead.
 */
public abstract class E2EBaseTest {

    private static final Logger log = LoggerFactory.getLogger(E2EBaseTest.class);

    /**
     * Initialises the WebDriver session before executing any test methods in the class.
     * Uses the browser configured in {@code config.properties}.
     */
    @BeforeClass(alwaysRun = true)
    public void setUpSession() {
        String browser = ConfigReader.get("browser", "chrome");
        log.info(TestMessages.SETUP, browser);
        DriverManager.initDriver(browser);
    }

    /**
     * Safely quits the WebDriver session after all tests in the class have executed.
     */
    @AfterClass(alwaysRun = true)
    public void tearDownSession() {
        DriverManager.quitDriver();
    }

    /**
     * Runs after each test method. On failure, saves a screenshot to disk and
     * attaches it to the Allure report.
     *
     * @param result the result of the executed test method
     */
    @AfterMethod(alwaysRun = true)
    public void handleResult(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            log.error(TestMessages.FAILED, result.getName());
            ScreenshotHelper.save(result.getName());
            ScreenshotHelper.attachToAllure();
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            log.info(TestMessages.PASSED, result.getName());
        }
    }
}
