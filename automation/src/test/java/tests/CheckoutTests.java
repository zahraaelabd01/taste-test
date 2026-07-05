package tests;

import BaseTest.BaseTest;
import Pages.CartPage;
import Pages.CheckoutPage;
import Pages.HomePage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CheckoutTests extends BaseTest {

    private CheckoutPage checkoutPage;

    @BeforeMethod
    public void initializePages() {

        homePage = new HomePage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);

    }

    // TC: Verify user cannot proceed to checkout with empty cart
    @Test
    public void verifyUserCannotCheckoutWithEmptyCart() {

        cartPage.openDirectly();

        Assert.assertTrue(cartPage.isEmptyCartMessageDisplayed());

        Assert.assertEquals(
                cartPage.getPageHeadingText(),
                "Shopping Cart"
        );
    }
}