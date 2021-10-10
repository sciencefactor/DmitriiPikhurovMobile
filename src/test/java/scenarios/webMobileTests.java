package scenarios;

import static org.testng.Assert.assertTrue;

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
    public void searchRequest() {
        getDriver().get(TestProperties.get("searchEngine"));

        // Make sure that page has been loaded completely
        new WebDriverWait(getDriver(), 10).until(
                wd -> ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete")
        );

        // Make search request
        WebElement searchField = getPo().getWelementUntil("searchField", 6);
        searchField.click();
        searchField.sendKeys(TestProperties.get("searchKeyword"));
        getDriver().getKeyboard().sendKeys(Keys.RETURN);
        // Check search result
        List<WebElement> searchResult = getPo().getWelementsUntil("searchResult", 6);
        assertTrue(searchResult.stream()
                    .anyMatch(result ->result.getText().contains(TestProperties.get("searchKeyword"))));
    }

}
