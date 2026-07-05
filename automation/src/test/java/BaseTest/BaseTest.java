package BaseTest;

import Pages.*;
import Pages.Authentication.LoginPage;
import Pages.Authentication.RegisterPage;
import Pages.MyAccount.MyAccountPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    protected WebDriver driver;
    protected CartPage cartPage;
    protected ProductPage productPage;
    protected HomePage homePage ;
    protected LoginPage loginPage ;
    protected MyAccountPage myAccountPage;
    protected RegisterPage registerPage;


    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--start-maximized");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}