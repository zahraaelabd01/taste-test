package tests;

import BaseTest.BaseTest;
import Pages.CartPage;
import Pages.CheckoutPage;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CheckoutTests extends BaseTest {

    @BeforeMethod
    public void openHome() {

        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);

        cartPage.openHome();
    }

    @Test
    public void EmptyCartTest() throws InterruptedException {

        cartPage.openDirectly();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Assert.assertTrue(cartPage.isEmptyCartMessageDisplayed());

    }

}
