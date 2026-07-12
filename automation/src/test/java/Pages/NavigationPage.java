package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class NavigationPage {
    private WebDriver driver;

    private By logo = By.id("logo");
    private By header = By.tagName("header");
    private By navbar = By.id("navbar");
    private By footer = By.tagName("footer");
    private By navbarLinks = By.cssSelector("#navbar a");
    private By footerLinks = By.cssSelector("footer a");
    private By breadcrumbs = By.className("breadcrumb");
    private By breadcrumbLinks = By.cssSelector(".breadcrumb li a");

    public NavigationPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isHeaderDisplayed() {
        return driver.findElement(header).isDisplayed();
    }

    public boolean isNavbarDisplayed() {
        return driver.findElement(navbar).isDisplayed();
    }

    public boolean isFooterDisplayed() {
        return driver.findElement(footer).isDisplayed();
    }

    public boolean isBreadcrumbDisplayed() {
        return driver.findElement(breadcrumbs).isDisplayed();
    }

    public void clickLogo() {
        driver.findElement(logo).click();
    }

    public String getCurrentPageUrl() {
        return driver.getCurrentUrl();
    }


    public void navigateToUrl(String url) {
        driver.navigate().to(url);
    }


    public void navigateBack() {
        driver.navigate().back();
    }

    public void navigateForward() {
        driver.navigate().forward();
    }

    public void clickFirstMenuLink() {
        List<WebElement> links = driver.findElements(navbarLinks);
        if (!links.isEmpty()) {
            links.get(0).click();
        }
    }

    public int checkBrokenLinks() {
        List<WebElement> allLinks = driver.findElements(By.tagName("a"));
        int brokenLinksCount = 0;

        for (WebElement link : allLinks) {
            String url = link.getAttribute("href");
            if (url == null || url.isEmpty() || url.startsWith("javascript")) {
                continue;
            }
            try {
                HttpURLConnection huc = (HttpURLConnection) (new URL(url).openConnection());
                huc.setRequestMethod("HEAD");
                huc.connect();
                int respCode = huc.getResponseCode();
                if (respCode >= 400) {
                    brokenLinksCount++;
                    System.out.println("Broken Link found: " + url + " with Status Code: " + respCode);
                }
            } catch (Exception e) {
                brokenLinksCount++;
            }
        }
        return brokenLinksCount;
    }
}