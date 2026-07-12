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
        String email = "user" + System.currentTimeMillis() + "@test.com";
        registerPage.setFirstName("Test");
        registerPage.setLastName("One");
        registerPage.setEmail(email);
        registerPage.setTelephone("13802603245");
        registerPage.setPassword("1234");
        registerPage.setConfirmPassword("1234");
        registerPage.clickPrivacyPolicy();
        AccountSuccessPage successPage=registerPage.clickContinue();
        Assert.assertEquals(successPage.getSuccessMsg(),successPage.getExpSuccessMsg());
        System.out.println("---valid Register test pass---");
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
        System.out.println("---Empty fields Error Messages test pass--- ");
    }
    @Test(description = "TC_AUTH_009")
    public void PasswordMismatchTest() {
        registerPage.setFirstName("Test");
        registerPage.setLastName("one");
        registerPage.setEmail("test999@email.com");
        registerPage.setTelephone("13802603245");
        registerPage.setPassword("12345");
        registerPage.setConfirmPassword("DifferentPass");
        registerPage.clickPrivacyPolicy();
        registerPage.clickContinue();
        Assert.assertEquals(registerPage.getActualConfirmPassErrorMsg(),
                registerPage.getExpectedConfirmPassErrorMsg());
        System.out.println("---Password mismatch test pass--- ");
        System.out.println("************************");

    }
    @Test(description = "TC_REGISTER_006")
    public void DuplicateEmailTest(){
        registerPage.setFirstName("Test");
        registerPage.setLastName("One");
        registerPage.setEmail("vorow6112118@parsitv.com");
        registerPage.setTelephone("13802603245");
        registerPage.setPassword("1234");
        registerPage.setConfirmPassword("1234");
        registerPage.clickPrivacyPolicy();
        registerPage.clickContinue();
        Assert.assertEquals(registerPage.getRegisteredEmailErrorMsg(),registerPage.getExpRegisteredEmailErrorMsg());
        System.out.println("---Duplicate Email test pass---");
        System.out.println("************************");
    }
    @Test ( description = "TC_AUTH_003 - Bug:System accepts invalid data in registration fields")
    public void InvalidDataTypeTest() {
        registerPage.setFirstName("1234+4");
        registerPage.setLastName("h---abc");
        registerPage.setEmail("test9999@email.com");
        registerPage.setTelephone("abcdefghij");
        registerPage.setPassword("12345");
        registerPage.setConfirmPassword("12345");
        registerPage.clickPrivacyPolicy();
        registerPage.clickContinue();
        Assert.assertEquals(registerPage.getActualFNameErrorMsg(),registerPage.getExpectedFNameErrorMsg());
        Assert.assertEquals(registerPage.getActualLNameErrorMsg(),registerPage.getExpectedLNameErrorMsg());
        Assert.assertEquals(registerPage.getActualTelephoneErrorMsg(),registerPage.getExpectedTelephoneErrorMsg());
    }
}
