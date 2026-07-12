package tests;

import BaseTest.BaseTest;
import Pages.CartPage;
import Pages.HomePage;
import Pages.RegisterPageUser6;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class RefreshPageTest extends BaseTest {

    private WebDriverWait wait;
    private RegisterPageUser6 registerPage;
    private CartPage cartPage;
    private HomePage homePage;

    @BeforeMethod
    public void setUp() {
        super.setUp();
        wait = new WebDriverWait(driver, Duration.ofSeconds(40));
       // registerPage = new RegisterPageUser6(driver);
        cartPage = new CartPage(driver);
        homePage = new HomePage(driver);
    }

    @Test
    public void fullCheckoutFlowThenRefresh() throws InterruptedException {

        myAccountPage=login();

        //registerPage.openRegisterPage();

        //String email = "user" + System.currentTimeMillis() + "@test.com";
        //String password = "Test@12345";

//        registerPage.registerUser(
//                "Test",
//                "User",
//                email,
//                "01000000000",
//                password
//        );

        driver.get("https://ecommerce-playground.lambdatest.io/index.php?route=product/product&product_id=58");

        wait.until(d -> ((JavascriptExecutor) d)
                .executeScript("return document.readyState").equals("complete"));

        WebElement addToCart = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("button-cart"))
        );

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addToCart);

        cartPage.openDirectly();

        WebElement checkoutBtn = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//a[contains(@href,'checkout/checkout') or contains(.,'Checkout')]")
                )
        );

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkoutBtn);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("input-payment-address-1")));

        WebElement company = driver.findElement(By.id("input-payment-company"));
        company.sendKeys("TestCompany");

        WebElement address1 = driver.findElement(By.id("input-payment-address-1"));
        address1.sendKeys("asddfghh");

        WebElement address2 = driver.findElement(By.id("input-payment-address-2"));
        address2.sendKeys("address2 test");

        WebElement city = driver.findElement(By.id("input-payment-city"));
        city.sendKeys("cairo");

        WebElement postcode = driver.findElement(By.id("input-payment-postcode"));
        postcode.sendKeys("1234");

        WebElement country = driver.findElement(By.id("input-payment-country"));
        country.click();
        country.sendKeys("Egypt");
        country.sendKeys(Keys.ENTER);

        Thread.sleep(2000);

        WebElement region = driver.findElement(By.id("input-payment-zone"));
        region.click();
        region.sendKeys("maadi");
        region.sendKeys(Keys.ENTER);

        Thread.sleep(2000);

        driver.navigate().refresh();

        Thread.sleep(5000);
    }
}