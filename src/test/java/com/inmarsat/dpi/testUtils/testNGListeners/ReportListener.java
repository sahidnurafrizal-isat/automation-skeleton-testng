package com.inmarsat.dpi.testUtils.testNGListeners;

import com.inmarsat.dpi.test.BaseTest;
import com.inmarsat.dpi.utils.ScreenshotUtils;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.inmarsat.dpi.testUtils.ExtentManager.getReporter;

/**
 * @Project: InmarsatGUI
 * @Author: Damian Malecki
 * @Contact: damian.malecki@pwc.com
 * @Created: 03.07.2018
 **/
public class ReportListener extends BaseTest implements IReporter {

    private ExtentReports extent;

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        extent = getReporter();

        for (ISuite suite : suites) {
            Map<String, ISuiteResult> result = suite.getResults();

            for (ISuiteResult r : result.values()) {
                ITestContext context = r.getTestContext();

                buildTestNodes(context.getPassedTests(), LogStatus.PASS);
                buildTestNodes(context.getFailedTests(), LogStatus.FAIL);
                buildTestNodes(context.getSkippedTests(), LogStatus.SKIP);

            }
        }
        extent.flush();
        extent.close();
    }

    private void buildTestNodes(IResultMap tests, LogStatus status) {
        ExtentTest test;

        if (tests.size() > 0) {
            for (ITestResult result : tests.getAllResults()) {
                test = extent.startTest(result.getMethod().getMethodName());
                test.getTest().setStartedTime(getTime(result.getStartMillis()));
                test.getTest().setEndedTime(getTime(result.getEndMillis()));

                for (String group : result.getMethod().getGroups())
                    test.assignCategory(group);

                if (result.getStatus() == ITestResult.FAILURE) {
                    String screenshotPath = System.getProperty("user.dir") + "/FailedTestsScreenshots/" +
                            result.getMethod().getMethodName() + ".png";

                    test.log(status, result.getThrowable().getMessage(), test.addBase64ScreenShot(
                            ScreenshotUtils.encodeToBase64String(screenshotPath, "png")));
                } else if (result.getStatus() == ITestResult.SKIP) {
                    test.log(status, "Test " + status.toString().toLowerCase() + "ed " + result.getThrowable());
                } else {
                    test.log(status, "Test " + status.toString().toLowerCase() + "ed ");
                }
                extent.endTest(test);
            }
        }
    }

    private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }

}
