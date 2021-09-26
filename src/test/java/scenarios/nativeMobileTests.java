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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import setup.BaseTest;
import utils.TestProperties;

public class nativeMobileTests extends BaseTest {

    User user;

    @Parameters({"userName", "userMail", "userPassword"})
    @BeforeClass(groups = {"native"})
    public void setUser(@Optional("") String name, @Optional("") String mail, @Optional("") String password) {
        user = new User(name, mail, password);
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

        // Sing in
        getPo().getWelement("emailField").sendKeys(user.getUserMail());
        getPo().getWelement("passwordFiled").sendKeys(user.getUserPassword());
        getPo().getWelement("signInBtn").click();

        // Page should be 'BudgetActivity'
        WebElement pageTitle = getPo().getWelement("pageTitle");
        assertEquals(pageTitle.getText(), TestProperties.get("registerUserFinalPage"));
    }

    @Test(groups = {"native"}, description = "Test should try to empty sign in and check Android toast message")
    public void emptySignIn()
        throws NoSuchFieldException, IllegalAccessException, InstantiationException, IOException, InterruptedException,
        TesseractException {

        // Make toast message
        getPo().getWelement("signInBtn").click();

        // OCR convert screenshot to text
        List<String> screenTexts = new ArrayList<>();
        for (int i = 0; i < TestProperties.getInt("screensNumber"); i++) {
            File scrFile = ((TakesScreenshot)getDriver()).getScreenshotAs(OutputType.FILE);
            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath(TestProperties.get("pathToTesseract"));
            tesseract.setLanguage(TestProperties.get("lang"));
            tesseract.setPageSegMode(1);
            tesseract.setOcrEngineMode(1);
            screenTexts.add(tesseract.doOCR(scrFile));
        }

        // Page should contain toast message
        assertTrue(screenTexts.stream().anyMatch(text -> text.contains(TestProperties.get("toastMessage"))));
    }

}
