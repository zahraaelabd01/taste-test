package BaseTest;

import Pages.*;
import Pages.Authentication.ForgottenPassPage;
import Pages.Authentication.LoginPage;
import Pages.Authentication.RegisterPage;
import Pages.MyAccount.ChangePasswordPage;
import Pages.MyAccount.EditAccountPage;
import Pages.MyAccount.LogoutSuccessPage;
import Pages.MyAccount.MyAccountPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class BaseTest {

    protected WebDriver driver;
    protected CartPage cartPage;
    protected ProductPage productPage;
    protected HomePage homePage ;
    protected LoginPage loginPage ;
    protected MyAccountPage myAccountPage;
    protected RegisterPage registerPage;
    protected LogoutSuccessPage logoutPage;
    protected EditAccountPage editAccountPage;
    protected ChangePasswordPage changePass;
    protected ForgottenPassPage forgottenPassPage;
    protected NavigationPage navigationPage;
    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
        driver.get("https://ecommerce-playground.lambdatest.io/");
        homePage=new HomePage(driver);
        cartPage=new CartPage(driver);
        navigationPage = new NavigationPage(driver);
    }

   @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
    @Test
    public MyAccountPage login() {
        loginPage = homePage.MyAccountClick();
        loginPage.setEmail("testhabeeba008@gmail.com");
        loginPage.setPassword("1234");
        return loginPage.LoginClick();
    }
}