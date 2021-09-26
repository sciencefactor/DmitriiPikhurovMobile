package pageObjects;

import io.appium.java_client.AppiumDriver;
import java.util.List;
import org.openqa.selenium.WebElement;
import setup.IPageObject;

import java.lang.reflect.Field;

public class PageObject implements IPageObject {

    Object somePageObject; // it should be set of web page or EPAM Test App WebElements

    public PageObject(String appType, AppiumDriver appiumDriver) throws Exception {
        System.out.println("Current app type: "+appType);
        switch(appType){
            case "web":
                somePageObject = new WebPageObject(appiumDriver);
                break;
            case "native":
                somePageObject = new NativePageObject(appiumDriver);
                break;
            default: throw new Exception("Can't create a page object for "+appType);
        }

    }


    @Override
    public WebElement getWelement(String weName) throws NoSuchFieldException, IllegalAccessException {
        // use reflection technique
        Field field = getField(weName);
        return (WebElement) field.get(somePageObject);

    }

    @Override
    public List<WebElement> getWelements(String weName) throws NoSuchFieldException, IllegalAccessException {
        // use reflection technique
        Field field = getField(weName);
        return (List<WebElement>) field.get(somePageObject);
    }

    private Field getField(String weName) throws NoSuchFieldException {
        System.out.printf("Interact with %s element%n", weName);
        Field field = somePageObject.getClass().getDeclaredField(weName);
        field.setAccessible(true);
        return field;
    }
}
