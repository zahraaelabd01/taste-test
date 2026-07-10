package tests;

import BaseTest.BaseTest;
import Pages.HomePage;
import Pages.ProductPage;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;




public class ProductTests extends BaseTest {

    private static final String CATEGORY = "Apple";
    private static final String PRODUCT = "iPod Nano";

    private ProductPage goToProduct(String productName) {
        driver.get("https://ecommerce-playground.lambdatest.io/");
        HomePage homePage = new HomePage(driver);
        ProductPage productPage = homePage.navigateToMegaMenuProduct(CATEGORY);
        productPage.openProduct(productName);
        return productPage;
    }



    @Test(priority = 1)
    public void testValidQuantityAccepted() {
        ProductPage productPage = goToProduct(PRODUCT);
        productPage.clickIncreaseQuantity();

        Assert.assertEquals(productPage.getQuantityFieldValue(), "2",
                " increasing quantity did not update the field to 2");
    }


    @DataProvider(name = "boundaryQuantities")
    public Object[][] boundaryQuantities() {
        return new Object[][]{
                {"0", "zero quantity"},
                {"-1", " negative quantity"}
        };
    }

    @Test(priority = 2)
    public void testDecreaseBelowMinimumBlocked() {
        ProductPage productPage = goToProduct(PRODUCT);
        // starting value is 1 (min="1"), so decreasing should either
        // do nothing or be disabled — not go to 0
        productPage.clickDecreaseQuantity();

        Assert.assertEquals(productPage.getQuantityFieldValue(), "1",
                " quantity went below the minimum of 1 when it shouldn't have");
    }

    @Test(priority = 3)
    public void testAddToCartButtonEnabled() {
        ProductPage productPage = goToProduct(PRODUCT);

        Assert.assertTrue(productPage.isAddToCartEnabled(),
                " Add to Cart button is disabled on load (known bug for this product)");
    }


    @Test(priority = 4)
    public void testBuyNowButtonEnabled() {
        ProductPage productPage = goToProduct(PRODUCT);

        Assert.assertTrue(productPage.isBuyNowEnabled(),
                " Buy Now button is disabled on load (known bug for this product)");
    }


    @Test(priority = 5)
    public void testStockStatusDisplayed() {
        ProductPage productPage = goToProduct(PRODUCT);
        String status = productPage.getStockStatusText();

        Assert.assertFalse(status.isEmpty(),
                " stock/availability status not displayed");
        // this product shows a delivery estimate ("2-3 Days") rather than
        // "In Stock"/"Out of Stock" text - documenting actual behavior here.
    }


    @Test(priority = 6)
    public void testTabSwitching() {
        ProductPage productPage = goToProduct(PRODUCT);

        productPage.clickTab("Reviews");
        productPage.clickTab("Description");
        // If no exception is thrown, both tabs were clickable/present.
        // Add isDisplayed() checks here once panel container ids are confirmed.
    }


    @Test(priority = 7)
    public void testReviewSubmitNoName() {
        ProductPage productPage = goToProduct(PRODUCT);
        productPage.openReviewsTab();

        productPage.enterReviewText("This is a perfectly valid review text with enough characters to pass validation rules.");
        productPage.selectRating(4);
        productPage.submitReview();

        String warning = productPage.getReviewWarningText();
        Assert.assertTrue(warning.toLowerCase().contains("name"),
                " expected a name-related warning, got: " + warning);
    }


    @Test(priority = 8)
    public void testReviewCountIncrements() {
        ProductPage productPage = goToProduct(PRODUCT);
        productPage.openReviewsTab();

        String beforeCount = productPage.getReviewCountText();

        productPage.enterReviewName("Saif");
        productPage.enterReviewText("This is a perfectly valid review text with enough characters to pass validation rules.");
        productPage.selectRating(5);
        productPage.submitReview();

        String afterCount = productPage.getReviewCountText();

        Assert.assertNotEquals(beforeCount, afterCount,
                " review count did not increment after submitting a valid review ");
    }

    @Test(priority = 9)
    public void productslist() {
        driver.get("https://ecommerce-playground.lambdatest.io/");
        HomePage homePage = new HomePage(driver);
        ProductPage productPage = homePage.navigateToMegaMenuProduct(CATEGORY);
        Assert.assertTrue(productPage.isProductListDisplayed() , "Product list is not displayed");
    }
}

