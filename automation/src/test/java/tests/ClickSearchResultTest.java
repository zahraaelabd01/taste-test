package tests;

import BaseTest.BaseTest;
import Pages.SearchResultsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ClickSearchResultTest extends BaseTest {

    @Test(description = "TC-4.2.3: Clicking a search result opens the correct product page")
    public void tc4_2_3_clickResultOpensProductPage() {
        SearchResultsPage searchPage = new SearchResultsPage(driver);
        searchPage.openHome();
        searchPage.searchFor("Canon EOS 5D");

        Assert.assertTrue(searchPage.hasResults(), "Search for Canon EOS 5D should return results");
        searchPage.clickFirstProduct();

        Assert.assertTrue(driver.getCurrentUrl().contains("route=product/product"),
                "Should navigate to the product detail page");
    }
}