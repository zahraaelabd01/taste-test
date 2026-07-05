package Pages;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class CartPage {

    WebDriver driver;
    WebDriverWait wait;

    static final String BaseURL = "https://ecommerce-playground.lambdatest.io/index.php?route=common/home";
    static final String CartURL = "https://ecommerce-playground.lambdatest.io/index.php?route=checkout/cart";

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

   // Homepage access

    By headerCartTrigger = By.cssSelector("a[href='#cart-total-drawer']");
    public void openHome() {
        driver.get(BaseURL);
    }

    private static final By ADD_TO_CART_IN_TILE = By.xpath(".//button[normalize-space()='Add to Cart'] | .//a[normalize-space()='Add to Cart']"
            + " | .//button[contains(@onclick,'cart.add')] | .//a[contains(@onclick,'cart.add')]");

    private static final By PRODUCT_LINK_IN_TILE = By.cssSelector("a[href*='route=product/product']");


    private static String xpathLiteral(String value) {
        if (!value.contains("'")) {
            return "'" + value + "'";
        }
        if (!value.contains("\"")) {
            return "\"" + value + "\"";
        }
        String[] parts = value.split("'", -1);
        StringBuilder sb = new StringBuilder("concat(");
        for (int i = 0; i < parts.length; i++) {
            sb.append("'").append(parts[i]).append("'");
            if (i < parts.length - 1) {
                sb.append(", \"'\", ");
            }
        }
        return sb.append(")").toString();
    }


    private By productTileByName(String productName) {
        String titleXpath = "//*[self::h4 or self::h3 or self::a][normalize-space(text())="
                + xpathLiteral(productName) + "]";
        return By.xpath(titleXpath + "/ancestor::div[.//button[normalize-space()='Add to Cart']"
                + " or .//a[normalize-space()='Add to Cart']"
                + " or .//button[contains(@onclick,'cart.add')]"
                + " or .//a[contains(@onclick,'cart.add')]][1]");
    }


    private WebElement waitForVisibleTile(String productName) {
        By locator = productTileByName(productName);
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        return driver.findElements(locator).get(0);
    }


    public void hoverAndAddToCart(String productName) {
        openHome();
        By locator = productTileByName(productName);
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        List<WebElement> tiles = driver.findElements(locator);

        for (int i = 0; i < tiles.size(); i++) {
            WebElement tile = tiles.get(i);
            List<WebElement> addBtns = tile.findElements(ADD_TO_CART_IN_TILE);
            if (addBtns.isEmpty()) {
                continue; // this duplicate has no usable control; try the next one
            }
            int before = getHeaderCartItemCount();
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", tile);
            try {
                new Actions(driver).moveToElement(tile).pause(Duration.ofMillis(300)).perform();
            } catch (Exception ignored) {
                // Best-effort hover only; the JS click below works regardless of hover state.
            }
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addBtns.get(0));
            try {
                wait.until(d -> getHeaderCartItemCount() != before);
                return; // success
            } catch (TimeoutException e) {
                // This duplicate didn't actually add (e.g. out of stock behind the scenes) — try the next one.
            }
        }
        throw new NoSuchElementException("Could not add '" + productName + "' to cart from any of the "
                + tiles.size() + " matching listing(s) on the page.");
    }




   // ProductPage Access

    By productTitle = By.cssSelector("h1");

    By addToCartButtonOnDetailPage = By.xpath("//button[normalize-space()='Add to Cart']"
            + " | //a[normalize-space()='Add to Cart'] | //*[@id='button-cart']");
    By requiredOptionSelects = By.cssSelector("select[id*='option']");
    public void openProductDetailPage(String productName) {
        openHome();
        WebElement tile = waitForVisibleTile(productName);
        WebElement link = tile.findElement(PRODUCT_LINK_IN_TILE);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", link);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
        wait.until(ExpectedConditions.presenceOfElementLocated(productTitle));
    }



    public boolean isAddToCartButtonPresentOnDetailPage() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.presenceOfElementLocated(addToCartButtonOnDetailPage));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isAddToCartButtonEnabledOnDetailPage() {
        return isAddToCartButtonPresentOnDetailPage() && driver.findElement(addToCartButtonOnDetailPage).isEnabled();
    }


    private void selectRequiredOptionsOnDetailPage() {
        for (WebElement selectEl : driver.findElements(requiredOptionSelects)) {
            Select select = new Select(selectEl);
            for (WebElement option : select.getOptions()) {
                String text = option.getText() == null ? "" : option.getText().trim();
                boolean looksLikePlaceholder = text.isEmpty()
                        || text.equalsIgnoreCase("---")
                        || text.toLowerCase().contains("please select");
                if (!looksLikePlaceholder) {
                    select.selectByVisibleText(option.getText());
                    break;
                }
            }
        }
    }

    public void clickAddToCartOnDetailPage() {
        int before = getHeaderCartItemCount();
        selectRequiredOptionsOnDetailPage();
        WebElement addToCartBtn = wait.until(ExpectedConditions.presenceOfElementLocated(addToCartButtonOnDetailPage));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", addToCartBtn);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addToCartBtn);
        try {
            wait.until(d -> getHeaderCartItemCount() != before);
        } catch (TimeoutException e) {
            // Some flows legitimately don't increase the count (e.g. an out-of-stock product
            // that's blocked server-side) — that's a valid outcome for the caller to assert on.
        }
    }

    // CartPage Access

    By pageHeading = By.xpath("//h1[normalize-space()='Shopping Cart']");
    By emptyCartMessage = By.xpath("//*[contains(text(),'Your shopping cart is empty')]");
    By cartRows = By.cssSelector("table tbody tr");
    By productLinkInRow = By.cssSelector("a[href*='route=product/product']");
    By quantityInputInRow = By.cssSelector("input[name*='quantity']");
    By removeButtonInRow = By.xpath(".//button[contains(@onclick,'cart.remove')]"
            + " | .//a[contains(@onclick,'cart.remove')]"
            + " | .//button[@title='Remove' or @data-original-title='Remove' or @aria-label='Remove']"
            + " | .//a[@title='Remove' or @data-original-title='Remove' or @aria-label='Remove']"
            + " | .//button[contains(@class,'btn-danger')]"
            + " | .//a[contains(@class,'btn-danger')]");
    By updateButtonInRow = By.xpath(".//button[contains(@onclick,'cart.update')] | .//button[@title='Update' or @data-original-title='Update']");
    public void openDirectly() {
        driver.get(CartURL);
    }

    public boolean isEmptyCartMessageDisplayed() {
        return !driver.findElements(emptyCartMessage).isEmpty();
    }

    public String getPageHeadingText() {
        return driver.findElement(pageHeading).getText();
    }

    public String getHeaderCartSummaryText() {
        List<WebElement> triggers = driver.findElements(headerCartTrigger);
        for (WebElement el : triggers) {
            if (el.isDisplayed()) {
                return el.getText().trim();
            }
        }
        return triggers.isEmpty() ? "" : triggers.get(0).getText().trim();
    }

    public int getHeaderCartItemCount() {
        String summary = getHeaderCartSummaryText();
        String digits = summary.replaceAll("[^0-9].*", "");
        return digits.isEmpty() ? 0 : Integer.parseInt(digits);
    }

    private WebElement rowForProduct(String productName) {
        List<WebElement> rows = driver.findElements(cartRows);
        for (WebElement row : rows) {
            try {
                List<WebElement> links = row.findElements(productLinkInRow);
                for (WebElement link : links) {
                    if (link.getText().trim().equalsIgnoreCase(productName)) {
                        return row;
                    }
                }
            } catch (StaleElementReferenceException e) {
                // The cart table re-rendered under us (e.g.mid AJAX update) — this row is
                // no longer valid; skip it rather than crash the whole lookup.
            }
        }
        throw new NoSuchElementException("Cart row not found for product: " + productName);
    }


    public boolean isProductInCart(String productName) {
        try {
            rowForProduct(productName);
            return true;
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    public void removeProduct(String productName) {
        List<WebElement> removeBtns = rowForProduct(productName).findElements(removeButtonInRow);
        if (!removeBtns.isEmpty()) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", removeBtns.get(0));
        } else {
            // No dedicated remove control on this row (some layouts only show +/- steppers) —
            // setting quantity to 0 is OpenCart's standard way of treating this as a removal.
            updateQuantity(productName, "0");
        }

        wait.until(d -> !isProductInCart(productName));
    }



    public void updateQuantity(String productName, String newValue) {
        WebElement row = rowForProduct(productName);
        WebElement qtyInput = row.findElement(quantityInputInRow);
        qtyInput.click();
        qtyInput.clear();
        qtyInput.sendKeys(newValue);
        qtyInput.sendKeys(Keys.ENTER);
        List<WebElement> updateBtn = row.findElements(updateButtonInRow);
        if (!updateBtn.isEmpty()) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", updateBtn.get(0));
        }
    }


}