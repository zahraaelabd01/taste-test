package tests;

import BaseTest.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CartPageTests extends BaseTest {

    @Test
    public void tc013_cartPageLoadsOnDirectUrl() {
        cartPage.openDirectly();

        Assert.assertTrue(driver.getCurrentUrl().contains("route=checkout/cart"), "Should navigate directly to the Cart page");
        Assert.assertEquals(cartPage.getPageHeadingText(), "Shopping Cart");
        Assert.assertTrue(cartPage.isEmptyCartMessageDisplayed(), "Empty cart should show its message on direct load");
    }
}