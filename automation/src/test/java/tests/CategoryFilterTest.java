package tests;

import BaseTest.BaseTest;
import Pages.SearchResultsPage;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class CategoryFilterTest extends BaseTest {

    @Test(description = "TC-4.5.2: Category filter from search dropdown is ignored (see BUG-005)")
    public void tc4_5_2_categoryFilterIgnored() {
        SearchResultsPage searchPage = new SearchResultsPage(driver);
        searchPage.openHome();
        searchPage.searchFor("Apple");
        int baselineCount = searchPage.getProductCount();

        searchPage.openHome();
        searchPage.searchWithCategory("Apple", "Cameras");
        int filteredCount = searchPage.getProductCount();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(filteredCount < baselineCount,
                "BUG-005: Cameras filter had no effect - still showing " + filteredCount + " products");
        softAssert.assertFalse(searchPage.getProductNamesList().stream().anyMatch(n -> n.contains("MacBook Pro")),
                "BUG-005: non-camera product 'MacBook Pro' still showing up under Cameras filter");
        softAssert.assertAll();
    }
}