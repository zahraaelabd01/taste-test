package tests;
import BaseTest.BaseTest;
import org.testng.Assert;

import org.testng.annotations.Test;
public class AddToCartTests extends BaseTest {
    static final String SONY_VAIO = "Sony VAIO";
    static final String IPHONE = "iPhone";
    @Test
    public void tc002_addFromProductDetailPage() {
        int before = cartPage.getHeaderCartItemCount();
        cartPage.hoverAndAddToCart(IPHONE);
        int after = cartPage.getHeaderCartItemCount();
        Assert.assertEquals(after, before + 1, "Cart icon count should increase by 1");
        cartPage.openDirectly();
        Assert.assertTrue(cartPage.isProductInCart(IPHONE), "Item should appear in cart");
    }

    @Test
    public void tc005_outOfStockProductCannotBeAdded() {

        cartPage.openProductDetailPage(SONY_VAIO);

        boolean disabledOrHidden = !cartPage.isAddToCartButtonPresentOnDetailPage()
                || !cartPage.isAddToCartButtonEnabledOnDetailPage();
        if (disabledOrHidden) {
            return; // correctly blocked
        }

        cartPage.clickAddToCartOnDetailPage();
        cartPage.openDirectly();
        Assert.assertFalse(cartPage.isProductInCart(SONY_VAIO),
                "BUG-001: Out-of-stock product '" + SONY_VAIO + "' was added instead of being blocked");
    }
}