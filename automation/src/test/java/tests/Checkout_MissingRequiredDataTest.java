package tests;

import BaseTest.BaseTest;
import Pages.CartPage;
import Pages.HomePage;
import Pages.RegisterPageUser6;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class Checkout_MissingRequiredDataTest extends BaseTest {

    private RegisterPageUser6 registerPage;
    private WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        super.setUp();

        wait = new WebDriverWait(driver, Duration.ofSeconds(40));

        homePage = new HomePage(driver);
        cartPage = new CartPage(driver);
        registerPage = new RegisterPageUser6(driver);
    }

    @Test
    public void verifyUserCannotProceedWithoutRequiredData() throws InterruptedException {

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

        driver.get("https://ecommerce-playground.lambdatest.io/index.php?route=product/product&product_id=58");

        wait.until(d ->
                ((JavascriptExecutor) d)
                        .executeScript("return document.readyState")
                        .equals("complete")
        );

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1")));

        System.out.println("1- Product page loaded");

        try {
            List<WebElement> selects = driver.findElements(By.tagName("select"));

            for (WebElement select : selects) {
                if (select.isDisplayed()) {
                    select.click();

                    List<WebElement> options = select.findElements(By.tagName("option"));

                    for (WebElement option : options) {
                        if (option.getText().toLowerCase().contains("medium")) {
                            option.click();
                            break;
                        }
                    }
                    break;
                }
            }
        } catch (Exception ignored) {
        }

        By buyNowBtn = By.xpath(
                "//button[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'buy now')]"
                        + " | //a[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'buy now')]"
        );

        By addToCartFallback = By.cssSelector("button#button-cart");

        WebElement actionBtn;

        try {
            actionBtn = wait.until(ExpectedConditions.elementToBeClickable(buyNowBtn));
        } catch (Exception e) {
            actionBtn = wait.until(ExpectedConditions.elementToBeClickable(addToCartFallback));
        }

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", actionBtn);

        try {
            actionBtn.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].click();", actionBtn);
        }

        System.out.println("2- Clicked Buy Now");
        System.out.println(driver.getCurrentUrl());

        Thread.sleep(3000);

        if (!driver.getCurrentUrl().contains("checkout")) {
            cartPage.openDirectly();
        }

        System.out.println("3- Before opening cart");


        System.out.println("4- Cart opened");
        System.out.println(driver.getCurrentUrl());


        driver.get("https://ecommerce-playground.lambdatest.io/index.php?route=checkout/checkout");

        wait.until(ExpectedConditions.urlContains("checkout"));

        System.out.println("Reached Checkout Page");

        ((JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, document.body.scrollHeight);");

        WebElement agreeLabel = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("label[for='input-agree']"))
        );

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", agreeLabel);

        try {
            agreeLabel.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", agreeLabel);
        }

        System.out.println("Agree clicked");

        WebElement continueBtn = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("button-save"))
        );

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", continueBtn);

        try {
            continueBtn.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", continueBtn);
        }

        System.out.println("Continue clicked");

        By errorLocator = By.cssSelector(".alert-danger, .text-danger");

        Assert.assertTrue(
                driver.findElements(errorLocator).size() > 0,
                "User should NOT proceed without required data."
        );
        Thread.sleep(5000);
    }
}package tests;

import BaseTest.BaseTest;
import Pages.CartPage;
import Pages.HomePage;
import Pages.RegisterPageUser6;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class Checkout_MissingRequiredDataTest extends BaseTest {

    private RegisterPageUser6 registerPage;
    private WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        super.setUp();

        wait = new WebDriverWait(driver, Duration.ofSeconds(40));

        homePage = new HomePage(driver);
        cartPage = new CartPage(driver);
        registerPage = new RegisterPageUser6(driver);
    }

    @Test
    public void verifyUserCannotProceedWithoutRequiredData() throws InterruptedException {

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

        driver.get("https://ecommerce-playground.lambdatest.io/index.php?route=product/product&product_id=58");

        wait.until(d ->
                ((JavascriptExecutor) d)
                        .executeScript("return document.readyState")
                        .equals("complete")
        );

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1")));

        System.out.println("1- Product page loaded");

        try {
            List<WebElement> selects = driver.findElements(By.tagName("select"));

            for (WebElement select : selects) {
                if (select.isDisplayed()) {
                    select.click();

                    List<WebElement> options = select.findElements(By.tagName("option"));

                    for (WebElement option : options) {
                        if (option.getText().toLowerCase().contains("medium")) {
                            option.click();
                            break;
                        }
                    }
                    break;
                }
            }
        } catch (Exception ignored) {
        }

        By buyNowBtn = By.xpath(
                "//button[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'buy now')]"
                        + " | //a[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'buy now')]"
        );

        By addToCartFallback = By.cssSelector("button#button-cart");

        WebElement actionBtn;

        try {
            actionBtn = wait.until(ExpectedConditions.elementToBeClickable(buyNowBtn));
        } catch (Exception e) {
            actionBtn = wait.until(ExpectedConditions.elementToBeClickable(addToCartFallback));
        }

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", actionBtn);

        try {
            actionBtn.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].click();", actionBtn);
        }

        System.out.println("2- Clicked Buy Now");
        System.out.println(driver.getCurrentUrl());

        Thread.sleep(3000);

        if (!driver.getCurrentUrl().contains("checkout")) {
            cartPage.openDirectly();
        }

        System.out.println("3- Before opening cart");


        System.out.println("4- Cart opened");
        System.out.println(driver.getCurrentUrl());


        driver.get("https://ecommerce-playground.lambdatest.io/index.php?route=checkout/checkout");

        wait.until(ExpectedConditions.urlContains("checkout"));

        System.out.println("Reached Checkout Page");

        ((JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, document.body.scrollHeight);");

        WebElement agreeLabel = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("label[for='input-agree']"))
        );

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", agreeLabel);

        try {
            agreeLabel.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", agreeLabel);
        }

        System.out.println("Agree clicked");

        WebElement continueBtn = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("button-save"))
        );

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", continueBtn);

        try {
            continueBtn.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", continueBtn);
        }

        System.out.println("Continue clicked");

        By errorLocator = By.cssSelector(".alert-danger, .text-danger");

        Assert.assertTrue(
                driver.findElements(errorLocator).size() > 0,
                "User should NOT proceed without required data."
        );
        Thread.sleep(5000);
    }
}