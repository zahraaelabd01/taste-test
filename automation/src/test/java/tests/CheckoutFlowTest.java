package tests;

import BaseTest.BaseTest;
import Pages.RegisterPageUser6;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class CheckoutFlowTest extends BaseTest {

    private WebDriverWait wait;
    private RegisterPageUser6 registerPage;

    @BeforeMethod
    public void setUp() {
        super.setUp();
        wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        registerPage = new RegisterPageUser6(driver);
    }

    @Test
    public void registerLoginAndCheckoutFlow() {

        registerPage.openRegisterPage();

        String email = "user" + System.currentTimeMillis() + "@test.com";
        String password = "Test@12345";

        registerPage.registerUser(
                "Test",
                "User",
                email,
                "01000000000",
                password
        );

        driver.get("https://ecommerce-playground.lambdatest.io/index.php?route=account/login");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("input-email"))).sendKeys(email);
        driver.findElement(By.id("input-password")).sendKeys(password);
        driver.findElement(By.cssSelector("input[type='submit']")).click();

        driver.get("https://ecommerce-playground.lambdatest.io/index.php?route=product/product&product_id=58");

        wait.until(ExpectedConditions.elementToBeClickable(By.id("button-cart"))).click();

        driver.get("https://ecommerce-playground.lambdatest.io/index.php?route=checkout/cart");

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@href,'checkout/checkout') or contains(text(),'Checkout')]")
        )).click();

        wait.until(d -> ((JavascriptExecutor) d)
                .executeScript("return document.readyState").equals("complete"));
    }
}