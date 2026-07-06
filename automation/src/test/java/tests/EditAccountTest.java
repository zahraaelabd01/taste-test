package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

public class EditAccountTest extends BaseTest.BaseTest {
    @BeforeMethod
    public void GoToEditAccountPagePage() {
        // = homePage.MyAccountClick();
        Assert.assertEquals(loginPage.getExpTitle(),loginPage.getActualTitle());
        System.out.println("Login Page Opened");
        System.out.println("************************");
    }
}
