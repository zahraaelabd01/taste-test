package tests.AuthTest;

import BaseTest.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ForgottenPassTest extends BaseTest {
    @BeforeMethod
    public void GoToForgotPage(){
        loginPage=homePage.MyAccountClick();
        forgottenPassPage=loginPage.ForgetClick();
        Assert.assertEquals(forgottenPassPage.getActualTitle(),forgottenPassPage.getExpTitle());
        System.out.println("Forgotten Password Page Opened");
    }
    @Test
    public void EmptyEmailFieldTest() {
        forgottenPassPage.clickContinue();
        Assert.assertEquals(forgottenPassPage.getEmailErrorMsg(),forgottenPassPage.getExpEmailErrorMsg());
        System.out.println("Empty field Test Pass");
    }

    @Test
    public void UnregisteredEmailTest() {
        forgottenPassPage.setEmail("emaildoesnotexist@test.com");
        forgottenPassPage.clickContinue();
        Assert.assertEquals(forgottenPassPage.getEmailErrorMsg(),forgottenPassPage.getExpEmailErrorMsg());
        System.out.println("Unregistered email Test pass");
    }

    @Test
    public void SuccessfulPasswordResetTest() {
        forgottenPassPage.setEmail("testhabeeba008@gmail.com");
        forgottenPassPage.clickContinue();
        Assert.assertEquals(forgottenPassPage.getSuccessMsg(),forgottenPassPage.getExpSuccessMsg());
        System.out.println("Password reset request successfully submitted.");
    }
}
