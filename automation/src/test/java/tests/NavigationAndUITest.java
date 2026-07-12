package tests;

import BaseTest.BaseTest;
import Pages.NavigationPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class NavigationAndUITest extends BaseTest {
    private NavigationPage navigationPage;
    private final String BASE_URL = "https://your-taste-test-app.com"; // استبدله برابط المشروع الفعلي

    @BeforeMethod
    public void setUpNavigationTest() {
        navigationPage = new NavigationPage(driver);
        driver.get(BASE_URL);
    }

    @Test(priority = 1)
    public void testUIStructureVisibility() {
        Assert.assertTrue(navigationPage.isHeaderDisplayed(), "Header is missing!");
        Assert.assertTrue(navigationPage.isNavbarDisplayed(), "Navbar is missing!");
        Assert.assertTrue(navigationPage.isFooterDisplayed(), "Footer is missing!");
    }

    @Test(priority = 2)
    public void testLogoRedirectToHome() {
        navigationPage.navigateToUrl(BASE_URL + "/about");
        navigationPage.clickLogo();
        Assert.assertEquals(navigationPage.getCurrentPageUrl(), BASE_URL + "/", "Logo did not redirect to Home Page!");
    }

    @Test(priority = 3)
    public void testMenuNavigationAndBreadcrumbs() {
        navigationPage.clickFirstMenuLink();


        Assert.assertTrue(navigationPage.isBreadcrumbDisplayed(), "Breadcrumb should be visible after navigating!");
    }

    @Test(priority = 4)
    public void testBrowserBackAndForwardRouting() {
        String firstPage = navigationPage.getCurrentPageUrl();
        navigationPage.navigateToUrl(BASE_URL + "/contact");
        String secondPage = navigationPage.getCurrentPageUrl();
        Assert.assertNotEquals(firstPage, secondPage);
        navigationPage.navigateBack();
        Assert.assertEquals(navigationPage.getCurrentPageUrl(), firstPage, "Back navigation failed!");
        navigationPage.navigateForward();
        Assert.assertEquals(navigationPage.getCurrentPageUrl(), secondPage, "Forward navigation failed!");
    }

    @Test(priority = 5)
    public void testForBrokenLinksInUI() {
        int brokenLinksCount = navigationPage.checkBrokenLinks();
        Assert.assertEquals(brokenLinksCount, 0, "There are broken links structural elements inside the page!");
    }
}
