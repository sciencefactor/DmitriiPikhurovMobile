package scenarios;

import static org.testng.Assert.assertEquals;

import entities.User;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import setup.BaseTest;
import utils.TestDataGenerate;
import utils.TestProperties;

public class nativeMobileTests extends BaseTest {

    User user;

    @BeforeClass(groups = {"native"})
    public void setUser() {
        user = TestDataGenerate.user();
        System.out.println(user);
    }

    @Test(groups = {"native"}, description = "Test should create new user account and sing in with it")
    public void registerNewAccount()
        throws IllegalAccessException, NoSuchFieldException, InstantiationException {

        // Register new user
        getPo().getWelement("registerButton").click();
        getPo().getWelementUntil("registrationEmail", 5).sendKeys(user.getUserMail());
        getPo().getWelement("registrationUsername").sendKeys(user.getUserName());
        getPo().getWelement("registrationPassword").sendKeys(user.getUserPassword());
        getPo().getWelement("registrationConfirmPassword").sendKeys(user.getUserPassword());
        getPo().getWelement("agreementSwitch").click();
        getPo().getWelement("registrationRegisterNewAccount").click();

        // Registration complete on main page
        WebElement pageTitle = getPo().getWelementUntil("mainPageTitle", 5);
        assertEquals(pageTitle.getText(), TestProperties.get("mainPageTitle"));

        // Sing in
        getPo().getWelement("emailField").sendKeys(user.getUserMail());
        getPo().getWelement("passwordFiled").sendKeys(user.getUserPassword());
        getPo().getWelement("signInBtn").click();

        // Page should be 'BudgetActivity'
        pageTitle = getPo().getWelementUntil("pageTitle", 5);
        assertEquals(pageTitle.getText(), TestProperties.get("registerUserFinalPage"));
    }

}
