package tests;

import BaseTest.BaseTest;
import Pages.SearchResultsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SearchValidProductTest extends BaseTest {

    @Test(description = "TC-4.1.2: Search with a valid exact product name")
    public void tc4_1_2_searchValidProductName() {
        SearchResultsPage searchPage = new SearchResultsPage(driver);
        searchPage.openHome();
        searchPage.searchFor("MacBook Pro");

        Assert.assertTrue(searchPage.hasResults(), "Search should return at least one result");
        Assert.assertTrue(searchPage.getFirstProductName().toLowerCase().contains("macbook pro"),
                "First result should be a MacBook Pro");
        Assert.assertTrue(searchPage.getProductPrices().contains(2000.00),
                "Expected price $2,000.00 in the results");
    }
}