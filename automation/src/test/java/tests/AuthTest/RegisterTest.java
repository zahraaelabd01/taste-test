package tests.AuthTest;

import Pages.Authentication.AccountSuccessPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RegisterTest extends BaseTest.BaseTest {
    @BeforeMethod
    public void GoToRegisterPage() {
        registerPage = homePage.RegisterClick();
        Assert.assertEquals(registerPage.getExpectedTitle(),registerPage.getActualTitle());
        System.out.println("Register Page Opened");
        System.out.println("************************");
    }
    @Test(description = "TC_AUTH_001")
    public void validRegister(){
        registerPage.setFirstName("Test");
        registerPage.setLastName("One");
        registerPage.setEmail("vorow6112118@parsitv.com");
        registerPage.setTelephone("13802603245");
        registerPage.setPassword("1234");
        registerPage.setConfirmPassword("1234");
        registerPage.clickPrivacyPolicy();
        AccountSuccessPage successPage=registerPage.clickContinue();
        Assert.assertEquals(successPage.getSuccessMsg(),successPage.getExpSuccessMsg());
        System.out.println("valid Register test pass");
        System.out.println("************************");
    }
    @Test(description = "TC_AUTH_005")
    public void EmptyFieldsTest() {

        registerPage.clickContinue();
        Assert.assertEquals(registerPage.getActualFNameErrorMsg(),
                registerPage.getExpectedFNameErrorMsg(),
                "First Name error message did not match!");
        Assert.assertEquals(registerPage.getActualLNameErrorMsg(),
                registerPage.getExpectedLNameErrorMsg(),
                "Last Name error message did not match!");
        Assert.assertEquals(registerPage.getActualEmailErrorMsg(),
                registerPage.getExpectedEmailErrorMsg(),
                "Email error message did not match!");
        Assert.assertEquals(registerPage.getActualTelephoneErrorMsg(),
                registerPage.getExpectedTelephoneErrorMsg(),
                "Telephone error message did not match!");
        Assert.assertEquals(registerPage.getActualPassErrorMsg(),
                registerPage.getExpectedPassErrorMsg(),
                "Password error message did not match!");
        System.out.println("Empty fields test pass ");
    }



}
