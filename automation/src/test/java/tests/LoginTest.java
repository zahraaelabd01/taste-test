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
    }

}
