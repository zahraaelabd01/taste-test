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
    @Test
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

}
