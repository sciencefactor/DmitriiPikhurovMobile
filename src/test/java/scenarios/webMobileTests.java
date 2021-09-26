package scenarios;

import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
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
        getPo().getWelement("searchField").click();
        getPo().getWelement("searchField").sendKeys(TestProperties.get("searchKeyword"));
        getPo().getWelement("searchField").sendKeys(Keys.ENTER);

        // Check search result
        List<WebElement> searchResult = getPo().getWelements("searchResult");
        searchResult.stream()
                    .limit(TestProperties.getInt("searchMaxCheckedResults"))
                    .forEach(result -> assertTrue(result.getText().contains(TestProperties.get("searchKeyword"))));
    }

}
