package scenarios;

import static io.appium.java_client.touch.offset.PointOption.point;
import static org.testng.Assert.assertTrue;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.UnsupportedCommandException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import setup.BaseTest;
import utils.TestProperties;

public class webMobileTests extends BaseTest {

    @Test(groups = {"web"}, description = "Test should make web search request and check result")
    public void searchRequest()
        throws InterruptedException, NoSuchFieldException, IllegalAccessException, InstantiationException, IOException {
        getDriver().get(TestProperties.get("searchEngine"));

        // Make sure that page has been loaded completely
        new WebDriverWait(getDriver(), 10).until(
                wd -> ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete")
        );

        // Make search request
        WebElement searchField = getPo().getWelement("searchField");
        searchField.click();
        searchField.sendKeys(TestProperties.get("searchKeyword"));
        try {
            getDriver().getKeyboard().sendKeys(Keys.RETURN);
        } catch (UnsupportedCommandException e) {
            searchField.sendKeys(Keys.ENTER);
        }

        // Check search result
        Thread.sleep(4000);
        List<WebElement> searchResult = getPo().getWelements("searchResult");
        assertTrue(searchResult.stream()
                    .anyMatch(result ->result.getText().contains(TestProperties.get("searchKeyword"))));
    }

}
