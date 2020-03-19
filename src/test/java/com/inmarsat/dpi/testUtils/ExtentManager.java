package com.inmarsat.dpi.testUtils;

//import com.inmarsat.gui.datasets.GlobalDataSet;
import com.relevantcodes.extentreports.ExtentReports;
import org.testng.Assert;

import java.io.File;

/**
 * @Project: InmarsatGUI
 * @Author: Damian Malecki
 * @Contact: damian.malecki@pwc.com
 * @Created: 03.07.2018
 **/
public class ExtentManager {

    private static ExtentReports extentReport;

    private static String userDir = System.getProperty("user.dir");

    private static String pathToReportDirectory = userDir + "\\ExtentReports";

    private static String reportPath = pathToReportDirectory + "\\" + "ExtentReportResults.html";

    public synchronized static ExtentReports getReporter() {
        if (extentReport == null) {
            createInstance();
            loadConfig();
        }
        return extentReport;
    }

    private static ExtentReports createInstance() {
        extentReport = new ExtentReports(reportPath, true);
//        extentReport.addSystemInfo("Environment", GlobalDataSet.getEnvironment());
//        extentReport.addSystemInfo("Browser", GlobalDataSet.getBrowser());
//        extentReport.addSystemInfo("SetID", GlobalDataSet.getSetId());
        return extentReport;
    }

    private static void loadConfig() {
        extentReport.loadConfig(new File(userDir + "\\extent-config.xml"));
    }

    public static void createReportDirectory() {
        File testDirectory = new File(pathToReportDirectory);
        if (!testDirectory.exists()) {
            if (!testDirectory.exists()) {
                Assert.assertTrue(testDirectory.mkdir());
            }
        }
    }

}
