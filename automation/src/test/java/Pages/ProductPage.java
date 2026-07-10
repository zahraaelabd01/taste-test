package Pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class ProductPage {
    WebDriver driver;
    WebDriverWait wait;

    // ---- Listing page ----
    By productList = By.cssSelector(".product-layout");

    // ---- Product detail page ----
    By addToCartButton = By.cssSelector("button[title='Add to Cart']");
    By buyNowButton    = By.cssSelector("button[title='Buy now']");
    By stockStatus     = By.cssSelector(".badge.badge-danger");
    By tabReviews        =By.xpath("//a[contains(@class,'nav-link') and normalize-space()='Reviews']");
    By reviewNameInput   = By.id("input-name");
    By reviewTextInput   = By.id("input-review");
    By reviewSubmitBtn   = By.id("button-review");
    By reviewWarning     = By.cssSelector(".alert-dismissible, .text-danger");
    By reviewCountText   = By.cssSelector(".total-review");


    public ProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isProductListDisplayed() {
        return !driver.findElements(productList).isEmpty();
    }


    public void openProduct(String productName) {
        By productLink = By.xpath(
                "//img[@alt='" + productName + "']/ancestor::a[contains(@href,'product_id')]");
        wait.until(ExpectedConditions.elementToBeClickable(productLink)).click();
    }


    private WebElement getVisibleQuantityContainer() {
        List<WebElement> inputs = driver.findElements(By.cssSelector("input[aria-label='Qty']"));
        for (WebElement input : inputs) {
            if (input.isDisplayed()) {
                return input.findElement(By.xpath("./ancestor::div[contains(@class,'input-group')][1]"));
            }
        }
        throw new NoSuchElementException("No visible quantity input group found");
    }

    public void clickIncreaseQuantity() {
        WebElement container = getVisibleQuantityContainer();
        WebElement button = container.findElement(By.cssSelector("button[aria-label='Increase quantity']"));
        String before = getQuantityFieldValue();
        wait.until(ExpectedConditions.elementToBeClickable(button)).click();
        wait.until(d -> !getQuantityFieldValue().equals(before)); // wait for value to actually change
    }

    public void clickDecreaseQuantity() {
        WebElement container = getVisibleQuantityContainer();
        WebElement button = container.findElement(By.cssSelector("button[aria-label='Decrease quantity']"));
        wait.until(ExpectedConditions.elementToBeClickable(button)).click();
    }

    public String getQuantityFieldValue() {
        WebElement container = getVisibleQuantityContainer();
        return container.findElement(By.cssSelector("input[aria-label='Qty']")).getAttribute("value");
    }


    public boolean isAddToCartEnabled() {
        return driver.findElement(addToCartButton).isEnabled();
    }

    public boolean isBuyNowEnabled() {
        return driver.findElement(buyNowButton).isEnabled();
    }

    public String getStockStatusText() {
        return driver.findElement(stockStatus).getText();
    }


    // ---- Tabs ----
    public void clickTab(String tabName) {
        By tabLocator = By.xpath("//a[contains(@class,'nav-link') and normalize-space()='" + tabName + "']");
        wait.until(ExpectedConditions.elementToBeClickable(tabLocator)).click();
    }

    public void openReviewsTab() {
        wait.until(ExpectedConditions.elementToBeClickable(tabReviews)).click();
    }

    public void enterReviewName(String name) {
        driver.findElement(reviewNameInput).sendKeys(name);
    }

    public void enterReviewText(String text) {
        driver.findElement(reviewTextInput).sendKeys(text);
    }

    public void selectRating(int stars) {
        WebElement input = driver.findElement(By.cssSelector("input[name='rating'][value='" + stars + "']"));
        String inputId = input.getAttribute("id");
        WebElement label = driver.findElement(By.cssSelector("label[for='" + inputId + "']"));
        wait.until(ExpectedConditions.elementToBeClickable(label)).click();
    }

    public void submitReview() {
        driver.findElement(reviewSubmitBtn).click();
    }

    public String getReviewCountText() {
        return driver.findElement(reviewCountText).getText();
    }

    public String getReviewWarningText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(reviewWarning)).getText();
    }
}