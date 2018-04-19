package com.orasi.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.internal.BuildInfo;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.orasi.AutomationException;

public class WebException extends AutomationException {

    public static final String SESSION_ID = "Session ID";
    public static final String DRIVER_INFO = "Driver info";
    public WebDriver driver = null;

    private static final long serialVersionUID = -8710980695994382082L;

    public WebException(String message) {
        super(message);
    }

    public WebException(String message, WebDriver driver) {
        super(message);
        this.driver = driver;
    }

    public WebException(String message, OrasiDriver driver) {
        super(message);
        this.driver = driver.getWebDriver();
    }

    public WebException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return createMessage(super.getMessage());
    }

    private String createMessage(String originalMessageString) {
        String supportMessage = getSupportUrl() == null ? "" : "For documentation on this error, please visit: " + getSupportUrl() + "\n";

        return (originalMessageString == null ? "" : originalMessageString + "\n")
                + supportMessage
                + getBuildInformation() + "\n"
                + getDriverInfo(driver) + "\n"
                + getGridInfo(driver);
    }

    public String getSupportUrl() {
        return null;
    }

    public BuildInfo getBuildInformation() {
        return new BuildInfo();
    }

    public String getDriverInfo(WebDriver driver) {
        String info = "N/A";
        String browserName = "N/A";
        String browserVersion = "N/A";
        String osInfo = "N/A";
        String sessionId = "N/A";

        if (driver instanceof OrasiDriver) {
            driver = ((OrasiDriver) driver).getWebDriver();
        }

        if (driver != null) {
            browserName = browserName(driver);
            browserVersion = browserVersion(driver);
            osInfo = platformOS(driver);
            sessionId = sessionId(driver);
        }
        info = String.format("Browser info: name: '%s', version: '%s', os.info: '%s', driver session id: '%s'",
                browserName,
                browserVersion,
                osInfo,
                sessionId).trim();
        return info;
    }

    private String browserName(WebDriver driver) {
        /*
         * if (driver instanceof HtmlUnitDriver) {
         * return "Html Unit";
         * }
         */
        return ((RemoteWebDriver) driver).getCapabilities().getBrowserName();
    }

    private String browserVersion(WebDriver driver) {
        /*
         * if (driver instanceof HtmlUnitDriver) {
         * return "N/A";
         * }
         */
        return ((RemoteWebDriver) driver).getCapabilities().getVersion();
    }

    private String sessionId(WebDriver driver) {
        /*
         * if (driver instanceof HtmlUnitDriver) {
         * return "N/A";
         * }
         */
        String sessionId = ((RemoteWebDriver) driver).getSessionId() != null ? ((RemoteWebDriver) driver).getSessionId().toString().trim() : "N/A";
        return sessionId;
    }

    private String platformOS(WebDriver driver) {
        /*
         * if (driver instanceof HtmlUnitDriver) {
         * return "N/A";
         * }
         */
        return ((RemoteWebDriver) driver).getCapabilities().getPlatform().name() + " "
                + ((RemoteWebDriver) driver).getCapabilities().getPlatform().getMajorVersion() + "."
                + ((RemoteWebDriver) driver).getCapabilities().getPlatform().getMinorVersion();
    }

    public String getGridInfo(WebDriver driver) {
        String info = "Selenium Node info: N/A";
        String[] gridNodeInfo = null;

        if (driver != null) {
            gridNodeInfo = getHostNameAndPort(driver);

            info = String.format("Selenium Node info: hostname: '%s', port: '%s', session id: '%s'",
                    gridNodeInfo[0],
                    gridNodeInfo[1],
                    gridNodeInfo[2] == "N/A" ? sessionId(driver) : gridNodeInfo[2]).trim();
        }
        return info;
    }

    private String[] getHostNameAndPort(WebDriver driver) {
        String[] hostAndPort = new String[3];
        String sessionId = "N/A";

        if (driver == null) {
            return new String[] { "N/A", "N/A", "N/A" };
        }

        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            String hostName = (((HttpCommandExecutor) ((RemoteWebDriver) driver).getCommandExecutor()).getAddressOfRemoteServer().getHost());
            int port = 4444;
            HttpHost host = new HttpHost(hostName, port);
            sessionId = sessionId(driver);
            URL sessionURL = new URL("http://" + hostName + ":" + port + "/grid/api/testsession?session=" + sessionId);
            BasicHttpEntityEnclosingRequest r = new BasicHttpEntityEnclosingRequest("POST", sessionURL.toExternalForm());
            HttpResponse response = client.execute(host, r);
            JSONObject object = extractObject(response);
            URL myURL = new URL(object.getString("proxyId"));
            if ((myURL.getHost() != null) && (myURL.getPort() != -1)) {
                hostAndPort[0] = myURL.getHost();
                hostAndPort[1] = Integer.toString(myURL.getPort());
                hostAndPort[2] = sessionId;
            }
        } catch (Exception throw_away) {
            return new String[] { "N/A", "N/A", "N/A" };
        }

        return hostAndPort;
    }

    private static JSONObject extractObject(HttpResponse resp) throws IOException, JSONException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
        StringBuffer s = new StringBuffer();
        String line;
        while ((line = rd.readLine()) != null) {
            s.append(line);
        }
        rd.close();
        JSONObject objToReturn = new JSONObject(s.toString());
        return objToReturn;
    }
}
