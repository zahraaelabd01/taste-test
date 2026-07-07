package tests;

import BaseTest.BaseTest;
import Pages.SearchResultsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class EmptySearchTest extends BaseTest {

    @Test(description = "TC-4.3.1: Empty search should not silently return all products (see BUG-003)")
    public void tc4_3_1_emptySearch() {
        SearchResultsPage searchPage = new SearchResultsPage(driver);
        searchPage.openHome();
        searchPage.submitEmptySearch();

        boolean handledCorrectly = searchPage.isNoResultsMessageDisplayed() || searchPage.getProductCount() == 0;
        Assert.assertTrue(handledCorrectly,
                "BUG-003: empty search returned " + searchPage.getProductCount() + " products instead of a validation message");
    }
}