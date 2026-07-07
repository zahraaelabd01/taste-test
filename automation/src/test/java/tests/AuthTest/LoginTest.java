package tests.AuthTest;
import BaseTest.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class LoginTest extends BaseTest {
    @BeforeMethod
    public void GoToLoginPage() {
        loginPage = homePage.MyAccountClick();
        Assert.assertEquals(loginPage.getExpTitle(),loginPage.getActualTitle());
        System.out.println("Login Page Opened");
        System.out.println("************************");
    }
    @Test(description = "TC_AUTH_010")
    public void ValidLoginTest(){
        loginPage.setEmail("testhabeeba008@gmail.com");
        System.out.println("Email Done");
        loginPage.setPassword("1234");
        System.out.println("Pass Done");
        myAccountPage=loginPage.LoginClick();
        System.out.println("---Valid Login Test pass---");
        System.out.println("*******************************");
    }
    @Test(description = "TC_AUTH_011")
    public void InValidLoginTest(){
        loginPage.setEmail("test1@gmail.com");
        System.out.println("Invalid Email Done");
        loginPage.setPassword("WrongPass");
        System.out.println("Invalid Pass Done");
        myAccountPage=loginPage.LoginClick();
        Assert.assertEquals(loginPage.getActualErrorMsg(),loginPage.getExpErrorMsg());
        System.out.println("---InValid Login Test pass---");
        System.out.println("*******************************");
    }
    @Test(description = "TC_AUTH_012")
    public void accountLock(){
        for (int i=0 ; i<=8;i++){
            loginPage.setEmail("testhabeeba@gmail.com");
            loginPage.setPassword("WrongPass");
            myAccountPage=loginPage.LoginClick();
        }
        loginPage.setEmail("testhabeeba@gmail.com");
        loginPage.setPassword("1234");
        myAccountPage=loginPage.LoginClick();
        Assert.assertEquals(loginPage.getActualErrorMsg(),loginPage.getExpAccLimitErrorMsg());
        System.out.println("---accountLock Test pass---");
        System.out.println("*******************************");
    }
    @Test(enabled = false ,description ="TC_AUTH_013 - Bug: Empty fields are triggering the Account Limit error!" )
    public void EmptyFieldTest(){
        SoftAssert softAssert = new SoftAssert();
        loginPage.setEmail("");
        loginPage.setPassword("");
        myAccountPage=loginPage.LoginClick();
        softAssert.assertEquals(loginPage.getActualErrorMsg(),loginPage.getExpEmptyFieldErrorMsg(),"Bug: Empty fields are triggering the Account Limit error!");
        softAssert.assertAll();
        System.out.println("---EmptyField Test pass---");
        System.out.println("*******************************");
    }

}
