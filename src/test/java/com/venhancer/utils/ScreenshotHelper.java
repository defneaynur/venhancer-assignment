package com.venhancer.utils;

import com.venhancer.driver.DriverManager;
import com.venhancer.utils.messages.TestMessages;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Utility for capturing screenshots on test failure.
 * Saves a PNG file to {@code target/screenshots/} and attaches it to the Allure report.
 */
public final class ScreenshotHelper {

    private static final Logger log = LoggerFactory.getLogger(ScreenshotHelper.class);

    private ScreenshotHelper() {}

    /**
     * Captures a screenshot and saves it to {@code target/screenshots/} as a PNG file.
     *
     * @param testName the name of the failing test, used as the file name prefix
     */
    public static void save(String testName) {
        try {
            TakesScreenshot ts = (TakesScreenshot) DriverManager.getDriver();
            File src  = ts.getScreenshotAs(OutputType.FILE);
            File dest = new File("target/screenshots/" + testName + "_" + System.currentTimeMillis() + ".png");
            dest.getParentFile().mkdirs();
            Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            log.info(TestMessages.SCREENSHOT_OK, dest.getAbsolutePath());
        } catch (IOException e) {
            log.warn(TestMessages.SCREENSHOT_FAIL, e.getMessage());
        }
    }

    /**
     * Captures a screenshot and attaches it to the current Allure test result.
     *
     * @return the screenshot bytes, or an empty array if capture failed
     */
    @Attachment(value = "Failure Screenshot", type = "image/png")
    public static byte[] attachToAllure() {
        try {
            return ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            log.warn(TestMessages.SCREENSHOT_FAIL, e.getMessage());
            return new byte[0];
        }
    }
}
