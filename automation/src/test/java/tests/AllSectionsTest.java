package tests;

import BaseTest.BaseTest;
import Pages.RegisterPageUser6;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class AllSectionsTest extends BaseTest {

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

        // 1. REGISTER
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

        // 2. LOGIN
        driver.get("https://ecommerce-playground.lambdatest.io/index.php?route=account/login");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("input-email"))).sendKeys(email);
        driver.findElement(By.id("input-password")).sendKeys(password);
        driver.findElement(By.cssSelector("input[type='submit']")).click();

        // 3. OPEN HOME
        driver.get("https://ecommerce-playground.lambdatest.io/index.php?route=common/home");

        // 4. CLICK ON PRODUCT (first product)
        WebElement product = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("(//div[contains(@class,'product-layout')])[1]")
                )
        );

        product.click();

        // 5. ADD TO CART
        WebElement addToCart = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("button-cart"))
        );

        addToCart.click();

        // 6. OPEN CART
        driver.get("https://ecommerce-playground.lambdatest.io/index.php?route=checkout/cart");

        // 7. CHECKOUT
        WebElement checkoutBtn = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//a[contains(@href,'checkout/checkout') or contains(text(),'Checkout')]")
                )
        );

        checkoutBtn.click();
    }
}