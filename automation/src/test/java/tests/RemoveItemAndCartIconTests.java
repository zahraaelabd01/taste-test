package tests;

import BaseTest.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RemoveItemAndCartIconTests extends BaseTest {

    static final String PALM_TREO_PRO = "Palm Treo Pro";

    @Test(description = "TC_CART_007: Remove product from cart")
    public void tc007_removeProductFromCart() {
        cartPage.hoverAndAddToCart(PALM_TREO_PRO);
        cartPage.openDirectly();
        Assert.assertTrue(cartPage.isProductInCart(PALM_TREO_PRO), "Product should be in cart before removal");

        cartPage.removeProduct(PALM_TREO_PRO);
        Assert.assertFalse(cartPage.isProductInCart(PALM_TREO_PRO), "Product should be removed from cart");
    }

    @Test(description = "TC_CART_009: Cart icon counter updates when an item is added")
    public void tc009_cartIconCounterUpdatesOnAdd() {
        int before = cartPage.getHeaderCartItemCount();
        cartPage.hoverAndAddToCart(PALM_TREO_PRO);
        int after = cartPage.getHeaderCartItemCount();
        Assert.assertTrue(after > before, "Cart icon counter should increase after adding an item");
    }

    @Test(description = "TC_CART_012: Cart icon count persists across page navigation")
    public void tc012_cartIconPersistsAcrossNavigation() {
        cartPage.hoverAndAddToCart(PALM_TREO_PRO);
        int countBeforeNav = cartPage.getHeaderCartItemCount();

        cartPage.openHome();
        int countAfterNav = cartPage.getHeaderCartItemCount();

        Assert.assertEquals(countAfterNav, countBeforeNav, "Cart icon count should persist across page navigation");
    }
}