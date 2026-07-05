package Pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ProductPage {
    WebDriver driver;
    WebDriverWait wait;

    // ---- Listing page ----
    By productList = By.cssSelector(".product-layout");

    // ---- Product detail page ----
    By quantityInput   = By.id("input-quantity");
    By addToCartButton = By.id("button-cart");
    By buyNowButton    = By.cssSelector("button[onclick*='buynow'], .btn-buynow");
    By compareButton   = By.cssSelector("button[onclick*='compare']");
    By errorAlert      = By.cssSelector(".alert-dismissible, .text-danger");
    By stockStatus     = By.xpath("//li[contains(.,'Availability')]");
    By sizeDropdown    = By.cssSelector("select[name^='option']");

    By reviewNameInput   = By.id("input-name");
    By reviewTextInput   = By.id("input-review");
    By reviewRatingRadios = By.cssSelector("input[name='rating']");
    By reviewSubmitBtn   = By.id("button-review");
    By reviewWarning     = By.cssSelector("#form-review .alert, .text-danger");
    By reviewCountText   = By.id("review-total");
    By tabReviews        = By.linkText("Reviews");

    public ProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isProductListDisplayed() {
        return !driver.findElements(productList).isEmpty();
    }

    // ---- Navigate from listing to a specific product ----
    public void openProduct(String productName) {
        By productLink = By.xpath(
                "//img[@alt='" + productName + "']/ancestor::a[contains(@href,'product_id')]");
        wait.until(ExpectedConditions.elementToBeClickable(productLink)).click();
    }

    // ---- Add-to-cart shortcut icon from listing page (hover-triggered) ----
    public void hoverAndClickCartShortcut(String productName) {
        By productImage = By.xpath("//img[@alt='" + productName + "']");
        WebElement image = wait.until(ExpectedConditions.visibilityOfElementLocated(productImage));
        new org.openqa.selenium.interactions.Actions(driver).moveToElement(image).perform();

        By cartShortcutIcon = By.xpath(
                "//img[@alt='" + productName + "']/ancestor::div[contains(@class,'carousel')]//a[contains(@class,'cart') or @title='Add to Cart']");
        wait.until(ExpectedConditions.elementToBeClickable(cartShortcutIcon)).click();
    }

    // ---- Quantity ----
    public void enterQuantity(String qty) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(quantityInput));
        field.clear();
        field.sendKeys(qty);
    }

    public String getQuantityFieldValue() {
        return driver.findElement(quantityInput).getAttribute("value");
    }

    public void clickAddToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton)).click();
    }

    public String getErrorMessageText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorAlert)).getText();
    }

    // ---- Buy Now / Compare ----
    public void clickBuyNow() {
        wait.until(ExpectedConditions.elementToBeClickable(buyNowButton)).click();
    }

    public void clickCompareIcon() {
        wait.until(ExpectedConditions.elementToBeClickable(compareButton)).click();
    }

    // ---- Stock / size ----
    public String getStockStatusText() {
        return driver.findElement(stockStatus).getText();
    }

    public boolean isSizeSelectorDisplayed() {
        return !driver.findElements(sizeDropdown).isEmpty()
                && driver.findElement(sizeDropdown).isDisplayed();
    }

    // ---- Reviews ----
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
        driver.findElements(reviewRatingRadios).stream()
                .filter(r -> r.getAttribute("value").equals(String.valueOf(stars)))
                .findFirst()
                .ifPresent(WebElement::click);
    }

    public void submitReview() {
        driver.findElement(reviewSubmitBtn).click();
    }

    public String getReviewCountText() {
        return driver.findElement(reviewCountText).getText();
    }
}