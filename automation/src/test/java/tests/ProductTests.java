package tests;
import BaseTest.BaseTest;
import Pages.HomePage;
import org.testng.annotations.Test;

public class ProductTests extends BaseTest {

    @Test
    public void testMultipleMegaMenuLinks() {
        HomePage homePage = new HomePage(driver);

        // Test going to Apple
        homePage.navigateToMegaMenuProduct("Apple");
        // TODO: Add your assertions here (e.g., check header or URL)

        // Go back or navigate again to test HTC
        driver.get("https://ecommerce-playground.lambdatest.io/");
        homePage.navigateToMegaMenuProduct("HTC");

        // Go back or navigate again to test Nokia
        driver.get("https://ecommerce-playground.lambdatest.io/");
        homePage.navigateToMegaMenuProduct("Nokia");
    }

}


