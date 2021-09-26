package setup;

import java.util.List;
import org.openqa.selenium.WebElement;

public interface IPageObject {

    WebElement getWelement(String weName) throws NoSuchFieldException, IllegalAccessException, InstantiationException;

    List<WebElement> getWelements(String weName)
        throws NoSuchFieldException, IllegalAccessException, InstantiationException;
}
