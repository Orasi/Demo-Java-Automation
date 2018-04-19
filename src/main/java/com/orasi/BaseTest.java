package com.orasi;

import static com.orasi.utils.TestReporter.log;
import static com.orasi.utils.TestReporter.logTrace;
import static org.apache.commons.lang3.BooleanUtils.toBooleanObject;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.orasi.utils.Constants;
import com.orasi.utils.TestReporter;

public class BaseTest {
    private static String environment;
    private String testName;
    private int logLevel;
    private boolean reportToMustard = false;

    @BeforeSuite(alwaysRun = true)
    @Parameters({ "environment", "logLevel", "reportToMustard" })
    public void beforeSuite(@Optional String environment, @Optional String logLevel, @Optional String reportToMustard) {

        if (isNotEmpty(logLevel)) {
            log("Setting Test Reporter log level to [ " + logLevel + " ]");
            this.logLevel = determineLogLevel(logLevel);
            TestReporter.setDebugLevel(determineLogLevel(logLevel));
        }
        logTrace("Entering BaseTest#setup");

        if (isNotEmpty(environment)) {
            log("Setting parameter [ environment ] to [ " + environment + " ]");
            BaseTest.environment = environment;
        } else {
            BaseTest.environment = Constants.TESTNG_PARAM_ENVIRONMENT;
        }

        if (isNotEmpty(reportToMustard)) {
            log("Setting parameter [ reportToMustard ] to [ " + reportToMustard + " ]");
            this.reportToMustard = toBooleanObject(reportToMustard);
        } else {
            log("Parameter [ reportToMustard ] was not set or empty");
        }

        logTrace("Exiting BaseTest#setup");
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method testMethod, Object[] testParams) {
        TestReporter.setDebugLevel(logLevel);
        logTrace("Entering BaseTest#beforeMethod");
        testName = testMethod.getDeclaringClass().getSimpleName() + "#" + testMethod.getName();
        log("Starting test [ " + testName + " ]");

        int id = 1;
        for (Object param : Arrays.asList(testParams)) {
            log("Test parameter [ " + id + " ] value [ " + param.toString() + " ]");
            id++;
        }
        logTrace("Exiting BaseTest#beforeMethod");
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod(Method testMethod, ITestResult testResults) {
        logTrace("Entering BaseTest#afterMethod");
        testName = testMethod.getDeclaringClass().getSimpleName() + "#" + testMethod.getName();
        String status = null;

        switch (testResults.getStatus()) {
            case ITestResult.FAILURE:
                status = "FAIL";
                break;
            case ITestResult.SKIP:
                status = "SKIP";
                break;
            case ITestResult.SUCCESS:
                status = "PASS";
                break;
            default:
                break;
        }

        log("Ending test [ " + testName + " ] with status [ " + status + " ]. Execution time [ " + ((testResults.getEndMillis() - testResults.getStartMillis()) / 1000.0) + " ] seconds");
        logTrace("Exiting BaseTest#afterMethod");
    }

    protected void setEnvironment(String environment) {
        BaseTest.environment = environment;
    }

    public String getEnvironment() {
        return environment;
    }

    protected void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestName() {
        return testName;
    }

    protected void setReportToMustard(boolean reportToMustard) {
        this.reportToMustard = reportToMustard;
    }

    public boolean isReportingToMustard() {
        return reportToMustard;
    }

    private int determineLogLevel(String level) {
        switch (level.toUpperCase()) {
            case "1":
            case "INFO":
                return 1;

            case "2":
            case "DEBUG":
                return 2;

            case "3":
            case "TRACE":
                return 3;

            default:
                return 0;
        }
    }
}
