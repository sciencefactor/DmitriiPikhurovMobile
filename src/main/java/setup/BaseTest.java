package setup;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pageObjects.PageObject;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import utils.EpamMobileCloudApi;
import utils.TestProperties;

public class BaseTest implements IDriver {

    private static AppiumDriver appiumDriver; // singleton
    static IPageObject po;

    @Override
    public AppiumDriver getDriver() { return appiumDriver; }

    public IPageObject getPo() {
        return po;
    }

    @Parameters({"platformName","appType","deviceName","udid", "browserName","app",
                    "appPackage", "appActivity", "bundleId"})
    @BeforeSuite(alwaysRun = true)
    public void setUp(String platformName,
                      String appType,
                      @Optional("") String deviceName,
                      @Optional("") String udid,
                      @Optional("") String browserName,
                      @Optional("") String app,
                      @Optional("") String appPackage,
                      @Optional("") String appActivity,
                      @Optional("") String bundleId
    ) throws Exception {
        System.out.println("Before: app type - " + appType);
        TestProperties.loadProperties(platformName, appType, "private");
        if(appType.equalsIgnoreCase("native")) {
            EpamMobileCloudApi.uploadApplication();
            EpamMobileCloudApi.installApplication(udid);
        }
        setAppiumDriver(platformName, deviceName, udid, browserName, app , appPackage, appActivity, bundleId);
        setPageObject(appType, appiumDriver);
    }

    @AfterMethod(alwaysRun = true)
    public void report(ITestResult result) {
        String message = String.format(result.getTestClass().getName()
            + " - Test: %s ", result.getMethod().getMethodName());
        if (result.isSuccess()) {
            System.out.println("[INFO] " + message + "passed");
        } else {
            System.out.println("[FAIL] " + message + "failed");
        }
    }

    @AfterSuite(alwaysRun = true)
    public void tearDown() throws Exception {
        appiumDriver.closeApp();
        if(EpamMobileCloudApi.isApplicationInstalled()) {
            EpamMobileCloudApi.deleteApplication();
        }
    }

    private void setAppiumDriver(String platformName,
                                 String deviceName,
                                 String udid,
                                 String browserName,
                                 String app,
                                 String appPackage,
                                 String appActivity,
                                 String bundleId){
        DesiredCapabilities capabilities = new DesiredCapabilities();
        //mandatory Android capabilities
        capabilities.setCapability("platformName",platformName);
        capabilities.setCapability("deviceName",deviceName);
        capabilities.setCapability("udid", udid);

        if(app.endsWith(".apk")) capabilities.setCapability("app", (new File(app)).getAbsolutePath());

        capabilities.setCapability("browserName", browserName);
        capabilities.setCapability("chromedriverDisableBuildCheck","true");

        capabilities.setCapability("appPackage", appPackage);
        capabilities.setCapability("appActivity", appActivity);
        capabilities.setCapability("bundleId", bundleId);

        // Make .click() method for iOS and Android working same way interpreting it as a tap
        capabilities.setCapability("nativeWebTap", true);

        // Let appium use keyboard in Chrome as in Safari
        capabilities.setCapability("appium:chromeOptions", ImmutableMap.of("w3c", false));

        try {
            String epamCloudURL = String.format(System.getProperty("ts.appium"), TestProperties.getURL("epamMobileCloudToken"));
            appiumDriver = new AppiumDriver(new URL(epamCloudURL), capabilities);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // Timeouts tuning
        appiumDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

    }

    private void setPageObject(String appType, AppiumDriver appiumDriver) throws Exception {
        po = new PageObject(appType, appiumDriver);
    }


}
