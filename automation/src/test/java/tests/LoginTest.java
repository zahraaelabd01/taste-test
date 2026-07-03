package tests;

import Pages.HomePage;
import Pages.LoginPage;
import lambdatest.BaseTest.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {
    @BeforeMethod
    public void GoToLoginPage() {
        loginPage = homePage.MyAccountClick();
        Assert.assertEquals(loginPage.getExpTitle(),loginPage.getActualTitle());
        System.out.println("Login Page Opened");
    }
    @Test
    public void ValidLoginTest(){

        loginPage.setEmail("testhabeeba008@gmail.com");
        System.out.println("Email Done");
        loginPage.setPassword("1234");
        System.out.println("Pass Done");
        myAccountPage=loginPage.LoginClick();
        System.out.println("*******************************");
    }
    @Test
    public void InValidLoginTest(){
        loginPage.setEmail("test1@gmail.com");
        System.out.println("Invalid Email Done");
        loginPage.setPassword("WrongPass");
        System.out.println("Invalid Pass Done");
        myAccountPage=loginPage.LoginClick();
        Assert.assertEquals(loginPage.getActualErrorMsg(),loginPage.getExpErrorMsg());
        System.out.println("Test pass");
        System.out.println("*******************************");
    }
    @Test
    public void accountLock(){
        for (int i=0 ; i<=8;i++){
            loginPage.setEmail("testhabeeba@gmail.com");
            loginPage.setPassword("WrongPass");
            myAccountPage=loginPage.LoginClick();
        }
        loginPage.setEmail("testhabeeba@gmail.com");
        loginPage.setPassword("1234");
        myAccountPage=loginPage.LoginClick();
        Assert.assertEquals(loginPage.getExpAccLimitErrorMsg(),loginPage.getActualErrorMsg());

    }


}
