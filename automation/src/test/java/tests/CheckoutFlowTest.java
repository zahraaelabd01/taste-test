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
    @BeforeMethod
    public void setUp() {
        super.setUp();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @Test
    public void registerLoginAndCheckoutFlow() {

        myAccountPage=login();
        driver.get("https://ecommerce-playground.lambdatest.io/index.php?route=product/product&product_id=58");

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id=\"entry_216842\"]/button"))).click();

        driver.get("https://ecommerce-playground.lambdatest.io/index.php?route=checkout/cart");

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@class=\"buttons d-flex\"]/a[contains(@href,'checkout/checkout') or contains(text(),'Checkout')]")
        )).click();

        wait.until(d -> ((JavascriptExecutor) d)
                .executeScript("return document.readyState").equals("complete"));
    }
}