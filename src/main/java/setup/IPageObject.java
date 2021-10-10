package setup;

import java.util.List;
import org.openqa.selenium.WebElement;

public interface IPageObject {

    WebElement getWelement(String weName) throws NoSuchFieldException, IllegalAccessException, InstantiationException;

    WebElement getWelementUntil(String weName, int seconds);

    List<WebElement> getWelements(String weName)
        throws NoSuchFieldException, IllegalAccessException, InstantiationException;

    List<WebElement> getWelementsUntil(String weName, int seconds);
}
