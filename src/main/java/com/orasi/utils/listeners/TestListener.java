package com.orasi.utils.listeners;

import static org.openqa.selenium.OutputType.BYTES;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.testng.IInvokedMethod;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;
import org.testng.internal.ConstructorOrMethod;
import org.testng.xml.XmlSuite;

import com.orasi.BaseTest;
import com.orasi.utils.Constants;
import com.orasi.utils.Preamble;
import com.orasi.utils.TestReporter;
import com.orasi.utils.mustard.Mustard;
import com.orasi.web.OrasiDriver;
import com.orasi.web.WebBaseTest;

import ru.yandex.qatools.allure.annotations.Attachment;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.annotations.TestCaseId;

public class TestListener extends TestListenerAdapter implements IReporter {
    private OrasiDriver driver = null;
    private String runLocation = "";
    private boolean reportToMustard = true;

    private void init(ITestResult result) {

        Object currentClass = result.getInstance();

        try {
            reportToMustard = ((BaseTest) currentClass).isReportingToMustard();
            runLocation = ((WebBaseTest) currentClass).getRunLocation().toLowerCase();
        } catch (Exception e) {
        }

        try {
            driver = ((WebBaseTest) currentClass).getDriver();
        } catch (Exception e) {
        }

    }

    @Override
    public void onTestFailure(ITestResult result) {
        init(result);
        String slash = Constants.DIR_SEPARATOR;

        String destDir = Constants.SCREENSHOT_FOLDER + slash + result.getInstanceName().replace(".", slash);
        DateFormat dateFormat = new SimpleDateFormat("dd_MMM_yyyy_hh_mm_ssaa");

        String destFile = destDir + slash + dateFormat.format(new Date()) + ".png";

        Reporter.setCurrentTestResult(result);

        if (driver != null) {
            WebDriver augmentDriver = driver.getWebDriver();
            // if (!(augmentDriver instanceof HtmlUnitDriver)) {
            if (runLocation == "remote") {
                augmentDriver = new Augmenter().augment(driver.getWebDriver());
            }

            new File(destDir).mkdirs();

            // Capture a screenshot for TestNG reporting
            TestReporter.logScreenshot(augmentDriver, destFile, slash);
            // Capture a screenshot for Allure reporting
            failedScreenshot(augmentDriver);
            // }

            // Log any console errors
            TestReporter.logConsoleLogging();

            if (reportToMustard) {
                String screenshot = null;
                File file = new File(destFile);

                try (FileInputStream fileInputStreamReader = new FileInputStream(file);) {

                    byte[] bytes = new byte[(int) file.length()];

                    fileInputStreamReader.read(bytes);
                    screenshot = Base64.getEncoder().encodeToString(bytes);
                } catch (IOException throwAway) {
                    // Screenshot attempt failed
                    TestReporter.logTrace("Failed to convert screenshot for mustard:" + throwAway.getMessage());
                }

                Mustard.postResultsToMustard(driver, result, runLocation, screenshot);
            }
        } else if (reportToMustard) {
            Mustard.postResultsToMustard(result);
        }

    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // will be called after test will be skipped
        init(result);
        if (reportToMustard) {
            if (driver != null) {
                Mustard.postResultsToMustard(driver, result, runLocation, null);
            } else {
                Mustard.postResultsToMustard(result);
            }
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        // will be called after test will pass
        init(result);
        if (reportToMustard) {
            if (driver != null) {
                Mustard.postResultsToMustard(driver, result, runLocation, null);
            } else {
                Mustard.postResultsToMustard(result);
            }
        }
    }

    @Override
    public void generateReport(List<XmlSuite> xmlSuites,
            List<ISuite> suites, String outputDirectory) {
        // TODO Auto-generated method stub

    }

    @Attachment(type = "image/png")
    public static byte[] failedScreenshot(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(BYTES);
    }

    private Preamble getPreambleAnnotation(IInvokedMethod method) {
        if (!method.isTestMethod()) {
            return null;
        }
        ConstructorOrMethod com = method.getTestMethod().getConstructorOrMethod();
        if (com.getMethod() == null) {
            return null;
        }
        Method m = com.getMethod();
        return m.getAnnotation(Preamble.class);
    }

    private TestCaseId getTestCaseIdAnnotation(IInvokedMethod method) {
        if (!method.isTestMethod()) {
            return null;
        }
        ConstructorOrMethod com = method.getTestMethod().getConstructorOrMethod();
        if (com.getMethod() == null) {
            return null;
        }
        Method m = com.getMethod();
        return m.getAnnotation(TestCaseId.class);
    }

    private Stories getStoriesAnnotation(IInvokedMethod method) {
        if (!method.isTestMethod()) {
            return null;
        }
        ConstructorOrMethod com = method.getTestMethod().getConstructorOrMethod();
        if (com.getMethod() == null) {
            return null;
        }
        Method m = com.getMethod();
        return m.getAnnotation(Stories.class);
    }
}