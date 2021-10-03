package scenarios;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import entities.User;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import setup.BaseTest;
import utils.TestProperties;

public class nativeMobileTests extends BaseTest {

    User user;

    @BeforeClass(groups = {"native"})
    public void setUser() {
        user = new User(TestProperties.get("userName"),TestProperties.get("userEmail") , TestProperties.get("userPassword"));
    }

    @Test(groups = {"native"}, description = "Test should create new user account and sing in with it")
    public void registerNewAccount()
        throws IllegalAccessException, NoSuchFieldException, InstantiationException {
        // Register new user
        getPo().getWelement("registerButton").click();
        getPo().getWelement("registrationEmail").sendKeys(user.getUserMail());
        getPo().getWelement("registrationUsername").sendKeys(user.getUserName());
        getPo().getWelement("registrationPassword").sendKeys(user.getUserPassword());
        getPo().getWelement("registrationConfirmPassword").sendKeys(user.getUserPassword());
        getPo().getWelement("registrationRegisterNewAccount").click();

        // Registration complete on main page
        WebElement pageTitle = getPo().getWelement("mainPageTitle");
        assertEquals(pageTitle.getText(), TestProperties.get("mainPageTitle"));

        // Sing in
        getPo().getWelement("emailField").sendKeys(user.getUserMail());
        getPo().getWelement("passwordFiled").sendKeys(user.getUserPassword());
        getPo().getWelement("signInBtn").click();

        // Page should be 'BudgetActivity'
        pageTitle = getPo().getWelement("pageTitle");
        assertEquals(pageTitle.getText(), TestProperties.get("registerUserFinalPage"));
    }

    @Test(groups = {"native"}, description = "Test should try to empty sign in and check Android toast message")
    public void emptySignIn()
        throws NoSuchFieldException, IllegalAccessException, InstantiationException {

        // Make toast message
        getPo().getWelement("signInBtn").click();

        // OCR convert screenshot to text

        Boolean isToastShown = new WebDriverWait(getDriver(), 5).until(driver -> {
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath(TestProperties.get("pathToTesseract"));
            tesseract.setLanguage(TestProperties.get("lang"));
            tesseract.setPageSegMode(1);
            tesseract.setOcrEngineMode(1);
            try {
                String screenText = tesseract.doOCR(scrFile);
                return screenText.contains(TestProperties.get("toastMessage"));
            } catch (TesseractException e) {
                e.printStackTrace();
            }
            return false;
        });

        // Page should contain toast message
        assertTrue(isToastShown);

    }

}
