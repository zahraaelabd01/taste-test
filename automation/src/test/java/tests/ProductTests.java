package tests;
import BaseTest.BaseTest;
import Pages.HomePage;
import Pages.ProductPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ProductTests extends BaseTest {

    @Test
    public void testNavigateToAppleProducts() {
        driver.get("https://ecommerce-playground.lambdatest.io/");
        HomePage homePage = new HomePage(driver); // Initialize HomePage with the driver

        ProductPage productPage = homePage.navigateToMegaMenuProduct("Apple");

        Assert.assertTrue(productPage.isProductListDisplayed(),
                "Product list page did not load after selecting Apple from Mega Menu");
    }

}


