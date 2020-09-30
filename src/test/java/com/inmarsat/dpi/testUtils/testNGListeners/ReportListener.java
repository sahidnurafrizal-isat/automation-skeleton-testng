package com.inmarsat.dpi.testUtils.testNGListeners;

import com.inmarsat.dpi.test.BaseTest;
import com.inmarsat.dpi.utils.ScreenshotUtils;
import com.inmarsat.dpi.utils.StringGenerator;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.inmarsat.dpi.testUtils.ExtentManager.getReporter;

public class ReportListener extends BaseTest implements IReporter {

    private ExtentReports extent;
    private static Logger logger = LogManager.getLogger(ReportListener.class.getName());

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

        copyReport();
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

    private void copyReport() {
        File reportSrcFile = new File(System.getProperty("user.dir") + "\\ExtentReports\\ExtentReportResults.html");
        File reportDstFile = new File(System.getProperty("user.dir") + "\\ExecutionHistory\\Report_"+StringGenerator.getFormattedDateForReport() + ".html");

        File reportDstDirectory = new File("ExecutionHistory");
        if(!reportDstDirectory.exists()) {
            Assert.assertTrue(reportDstDirectory.mkdirs());
        }

        try{
            Files.copy(reportSrcFile.toPath(), reportDstFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }catch(IOException ex){
            logger.error(ex.getMessage());
        }
    }

}
