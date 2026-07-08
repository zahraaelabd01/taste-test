package tests;

import BaseTest.BaseTest;
import Pages.RegisterPageUser6;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class VerifyStockInCartTest extends BaseTest {

    private WebDriverWait wait;
    private RegisterPageUser6 registerPage;

    @BeforeMethod
    public void setUp() {
        super.setUp();
        wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        registerPage = new RegisterPageUser6(driver);
    }

    @Test
    public void verifyOutOfStockBlockCheckout() {

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

        driver.get("https://ecommerce-playground.lambdatest.io/index.php?route=product/product&product_id=30");

        WebElement stockLabel = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[contains(text(),'Out Of Stock') or contains(text(),'Out of Stock')]")
                )
        );

        Assert.assertTrue(stockLabel.isDisplayed(),
                "Product should be explicitly shown as Out Of Stock on the product page");

        try {
            WebElement addToCart = driver.findElement(By.id("button-cart"));
            if (addToCart.isEnabled()) {
                addToCart.click();
            }
        } catch (Exception ignored) {
        }

        driver.get("https://ecommerce-playground.lambdatest.io/index.php?route=checkout/cart");

        boolean errorAlertExists = driver.findElements(
                By.xpath("//div[contains(@class,'alert-danger') or contains(text(),'not available in the desired quantity') or contains(text(),'***')]")
        ).size() > 0;

        boolean checkoutButtonDisabled = false;
        try {
            WebElement checkoutBtn = driver.findElement(By.xpath("//a[contains(@href,'checkout/checkout') or contains(text(),'Checkout')]"));
            String isDisabled = checkoutBtn.getAttribute("disabled");
            String cssClass = checkoutBtn.getAttribute("class");
            if (isDisabled != null || (cssClass != null && cssClass.contains("disabled"))) {
                checkoutButtonDisabled = true;
            }
        } catch (Exception ignored) {
            checkoutButtonDisabled = true;
        }

        Assert.assertTrue(errorAlertExists || checkoutButtonDisabled,
                "The system must block checkout either by showing a red danger alert in the cart or by disabling the checkout button.");
    }
}