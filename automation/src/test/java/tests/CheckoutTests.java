package tests;

import BaseTest.BaseTest;
import Pages.CartPage;
import Pages.CheckoutPage;
import Pages.HomePage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class CheckoutTests extends BaseTest {

    private CheckoutPage checkoutPage;
    private WebDriverWait wait;

    @BeforeMethod
    public void initializePages() {

        super.setUp();

        homePage = new HomePage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);

        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }


    @Test
    public void verifyUserCannotCheckoutWithEmptyCart() {


        cartPage.openDirectly();


        wait.until(driver ->
                driver.getTitle().toLowerCase().contains("shopping cart")
        );


        Assert.assertEquals(
                cartPage.getPageHeadingText().trim(),
                "Shopping Cart",
                "Wrong page title"
        );


        Assert.assertTrue(
                cartPage.isEmptyCartMessageDisplayed(),
                "Empty cart message is NOT displayed"
        );
    }
}