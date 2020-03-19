package com.inmarsat.dpi.test;

import com.inmarsat.dpi.utils.ScreenshotUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import static com.inmarsat.dpi.testUtils.ExtentManager.createReportDirectory;
import static com.inmarsat.dpi.utils.ScreenshotUtils.createScreenshotsDirectory;

/**
 * @Project: InmarsatDPITest
 * @Author: Sahid Nur Afrizal
 * @Contact: sahid.nurafrizal@inmarsat.com
 * @Created: 19.03.2020
 **/
public class BaseTest {

    private static Logger logger = LoggerFactory.getLogger(BaseTest.class);

    @BeforeSuite(alwaysRun = true)
    public void initiateSuite() {
        createReportDirectory();
        createScreenshotsDirectory();
    }

    @BeforeMethod(alwaysRun = true)
    public void startWebsiteAndLogin(ITestResult result) {
        logger.info("STARTING TEST: " + result.getMethod().getMethodName());

    }

    @AfterMethod(alwaysRun = true)
    public void afterEachMethod(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            try {
//                ScreenshotUtils.getScreenshot(getDriver(), result.getMethod().getMethodName());
                logger.error("TEST FAILED: " + result.getMethod().getMethodName());
            } catch (Exception e) {}
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            logger.info("TEST PASSED: " + result.getMethod().getMethodName());
        } else {
            logger.warn("TEST SKIPPED OR IGNORED: " + result.getMethod().getMethodName());
        }
    }


    @AfterSuite(alwaysRun = true)
    public void quitDriver() {

    }

}

