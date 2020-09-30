package com.inmarsat.dpi.utils;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ScreenshotUtils {

    private static Logger logger = LogManager.getLogger(ScreenshotUtils.class.getName());

    private static String userDir = System.getProperty("user.dir");

    private static String pathToScreenshotsDirectory = userDir + "\\FailedTestsScreenshots";

    public static void getScreenshot(WebDriver driver, String screenshotName) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String destination = pathToScreenshotsDirectory + "\\" + screenshotName + ".png";
        File finalDestination = new File(destination);
        try {
            FileUtils.copyFile(source, finalDestination);
        } catch (IOException e) {
            logger.error("CREATING THE SCREENSHOT HAS FAILED: " + e.getMessage());
        }
    }

    public static String getBase64Screenshot(WebDriver driver) {
        TakesScreenshot newScreen = (TakesScreenshot) driver;
        String scnShot = newScreen.getScreenshotAs(OutputType.BASE64);
        return "data:image/png;base64, " + scnShot;
    }

    public static String encodeToBase64String(String path, String type) {
        File file = new File(path);
        String imageString = "";
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            BufferedImage image = ImageIO.read(file);
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();

            BASE64Encoder encoder = new BASE64Encoder();
            imageString = encoder.encode(imageBytes);
        } catch (IOException e) {
            logger.error("METHOD encodeToBase64String failed with: " + e.getMessage());
        }
        return "data:image/png;base64," + imageString;
    }

    public static void createScreenshotsDirectory() {
        File testDirectory = new File(pathToScreenshotsDirectory);
        if (!testDirectory.exists()) {
            Assert.assertTrue(testDirectory.mkdir());
        }
    }

}
