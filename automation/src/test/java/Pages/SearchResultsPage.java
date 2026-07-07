package Pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchResultsPage {

    WebDriver driver;
    WebDriverWait wait;

    static final String BaseURL = "https://ecommerce-playground.lambdatest.io/index.php?route=common/home";

    By searchInput = By.cssSelector("input[name='search']");
    By searchButton = By.cssSelector("#search button[type='submit']");
    By categoryDropdown = By.cssSelector("select[name='category_id']");
    By sortDropdown = By.cssSelector("select[id^='input-sort']");
    By productCards = By.cssSelector(".product-layout");
    By productNames = By.cssSelector(".product-layout .caption h4 a");
    By productPrices = By.cssSelector(".product-layout .price");
    By noResultsMsg = By.cssSelector("#content > p");

    public SearchResultsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void openHome() {
        driver.get(BaseURL);
    }

    public void searchFor(String keyword) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(searchInput));
        input.clear();
        input.sendKeys(keyword);
        input.sendKeys(Keys.ENTER);
        waitForResults();
    }

    public void searchWithCategory(String keyword, String category) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(searchInput));
        input.clear();
        input.sendKeys(keyword);
        driver.findElement(By.cssSelector(".search-category button.dropdown-toggle")).click();
        By categoryOption = By.xpath("//div[contains(@class, 'search-category')]//a[normalize-space()='" + category + "']");
        wait.until(ExpectedConditions.elementToBeClickable(categoryOption)).click();
        driver.findElement(searchButton).click();
        waitForResults();
    }

    public void submitEmptySearch() {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(searchInput));
        input.clear();
        driver.findElement(searchButton).click();
        waitForResults();
    }

    public void sortBy(String optionText) {
        Select select = new Select(wait.until(ExpectedConditions.elementToBeClickable(sortDropdown)));
        select.selectByVisibleText(optionText);
        waitForResults();
    }


    private void waitForResults() {
        try {
            wait.until(d -> !driver.findElements(productCards).isEmpty() || !driver.findElements(noResultsMsg).isEmpty());
        } catch (org.openqa.selenium.TimeoutException e) {
        }
    }

    public boolean hasResults() {
        return !driver.findElements(productCards).isEmpty();
    }

    public int getProductCount() {
        return driver.findElements(productCards).size();
    }

    public String getFirstProductName() {
        List<WebElement> names = driver.findElements(productNames);
        return names.isEmpty() ? "" : names.get(0).getText();
    }

    public List<String> getProductNamesList() {
        List<String> names = new ArrayList<>();
        for (WebElement el : driver.findElements(productNames)) {
            names.add(el.getText());
        }
        return names;
    }

    public List<Double> getProductPrices() {
        List<Double> prices = new ArrayList<>();
        Pattern pricePattern = Pattern.compile("\\$([\\d,]+\\.\\d{2})");
        for (WebElement el : driver.findElements(productPrices)) {
            Matcher m = pricePattern.matcher(el.getText());
            if (m.find()) {
                prices.add(Double.parseDouble(m.group(1).replace(",", "")));
            }
        }
        return prices;
    }

    public boolean isNoResultsMessageDisplayed() {
        return !driver.findElements(noResultsMsg).isEmpty();
    }

    public void clickFirstProduct() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(productNames));
        driver.findElements(productNames).get(0).click();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
