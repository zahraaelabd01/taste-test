package tests;

import BaseTest.BaseTest;
import Pages.SearchResultsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortPriceLowToHighTest extends BaseTest {

    @Test(description = "TC-4.4.2: Sort by Price - Low to High")
    public void tc4_4_2_sortPriceLowToHigh() {
        SearchResultsPage searchPage = new SearchResultsPage(driver);
        searchPage.openHome();
        searchPage.searchFor("Apple");
        Assert.assertTrue(searchPage.hasResults(), "Search for Apple should return results before sorting");

        searchPage.sortBy("Price (Low > High)");

        List<Double> prices = searchPage.getProductPrices();
        List<Double> sortedCopy = new ArrayList<>(prices);
        Collections.sort(sortedCopy);

        Assert.assertEquals(prices, sortedCopy, "Prices should be ascending after Price: Low to High sort");
    }
}