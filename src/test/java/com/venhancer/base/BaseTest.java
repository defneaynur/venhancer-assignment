package com.venhancer.base;

import com.venhancer.driver.DriverManager;
import com.venhancer.utils.ConfigReader;
import com.venhancer.utils.ScreenshotHelper;
import com.venhancer.utils.messages.TestMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Base class for isolated test cases that require a fresh browser per test method.
 * The WebDriver is initialised before each {@code @Test} and quit after it finishes.
 *
 * <p>For tests that share browser state across methods, extend {@link E2EBaseTest} instead.
 */
public class BaseTest {

    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    /**
     * Initialises the WebDriver before each test method using the browser defined in config.
     */
    @BeforeMethod
    public void setUp() {
        String browser = ConfigReader.get("browser", "chrome");
        log.info(TestMessages.SETUP, browser);
        DriverManager.initDriver(browser);
    }

    /**
     * Runs after each test method. On failure, saves a screenshot to disk and
     * attaches it to the Allure report. Always quits the driver.
     *
     * @param result the TestNG result object for the just-executed test
     */
    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            log.error(TestMessages.FAILED, result.getName());
            ScreenshotHelper.save(result.getName());
            ScreenshotHelper.attachToAllure();
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            log.info(TestMessages.PASSED, result.getName());
        }
        DriverManager.quitDriver();
    }
}
