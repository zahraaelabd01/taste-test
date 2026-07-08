package tests;

import BaseTest.BaseTest;
import Pages.RegisterPageUser6;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

public class AllSectionsTest extends BaseTest {

    private WebDriverWait wait;
    private RegisterPageUser6 registerPage;

    @BeforeMethod
    public void setUp() {
        super.setUp();
        wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        registerPage = new RegisterPageUser6(driver);
    }

    @Test
    public void registerLoginAndCheckoutFlow() throws InterruptedException {

        driver.get("https://ecommerce-playground.lambdatest.io/index.php?route=common/home");

        WebElement product = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("(//div[contains(@class,'product-layout')])[1]")
                )
        );
        product.click();

        WebElement addToCart = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("button-cart"))
        );
        addToCart.click();

        wait.until(d -> ((JavascriptExecutor) d)
                .executeScript("return document.readyState").equals("complete"));

        driver.get("https://ecommerce-playground.lambdatest.io/index.php?route=checkout/cart");

        WebElement checkoutBtn = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//a[contains(@href,'checkout/checkout') or contains(text(),'Checkout')]")
                )
        );
        checkoutBtn.click();

        wait.until(ExpectedConditions.urlContains("checkout/checkout"));
        dumpDebug("01-checkout-page-loaded");

        List<WebElement> radios = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("input[name='account']"))
        );

        boolean registerSelected = false;
        for (WebElement radio : radios) {
            if ("register".equals(radio.getAttribute("value"))) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", radio);
                registerSelected = true;
                break;
            }
        }
        if (!registerSelected) {
            dumpDebug("FAIL-no-register-radio");
            throw new NoSuchElementException(
                    "Could not find the 'register' account-type radio button - checkout page layout may have changed."
            );
        }

        dumpDebug("02-register-radio-clicked");

        Thread.sleep(3000);

        wait.until(ExpectedConditions.and(
                ExpectedConditions.presenceOfElementLocated(By.id("input-payment-firstname")),
                ExpectedConditions.visibilityOfElementLocated(By.id("input-payment-firstname"))
        ));

        String email = "user" + System.currentTimeMillis() + "@test.com";
        String password = "Test@12345";

        fillFieldWithWait("input-payment-firstname", "Sarah");
        fillFieldWithWait("input-payment-lastname", "Test");
        fillFieldWithWait("input-payment-email", email);
        fillFieldWithWait("input-payment-telephone", "01000000000");
        fillFieldWithWait("input-payment-password", password);
        fillFieldWithWait("input-payment-confirm", password);
        fillFieldWithWait("input-payment-address-1", "123 Main Street");
        fillFieldWithWait("input-payment-city", "Cairo");
        fillFieldWithWait("input-payment-postcode", "12345");

        WebElement countryDropdown = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("input-payment-country"))
        );
        Select countrySelect = new Select(countryDropdown);

        WebElement zoneDropdownBefore = driver.findElement(By.id("input-payment-zone"));
        int zoneOptionCountBefore = new Select(zoneDropdownBefore).getOptions().size();

        countrySelect.selectByVisibleText("Egypt");

        wait.until(d -> {
            WebElement zoneEl = d.findElement(By.id("input-payment-zone"));
            int currentCount = new Select(zoneEl).getOptions().size();
            return currentCount != zoneOptionCountBefore && currentCount > 1;
        });

        WebElement zoneDropdown = driver.findElement(By.id("input-payment-zone"));
        Select zoneSelect = new Select(zoneDropdown);
        if (zoneSelect.getOptions().size() > 1) {
            zoneSelect.selectByIndex(1);
        } else {
            zoneSelect.selectByIndex(0);
        }

        dumpDebug("04-fields-filled-and-zone-selected");

        checkBoxIfPresent("input-account-agree");
        checkBoxIfPresent("input-agree");

        dumpDebug("05-checkboxes-handled");

        WebElement continueBtn = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("button-save"))
        );

        try {
            continueBtn.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", continueBtn);
        }

        dumpDebug("06-continue-clicked");

        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.urlContains("checkout/success"),
                    ExpectedConditions.visibilityOfElementLocated(By.name("shipping_method")),
                    ExpectedConditions.visibilityOfElementLocated(By.name("payment_method")),
                    ExpectedConditions.textToBePresentInElementLocated(
                            By.tagName("body"), "Your order has been placed")
            ));
        } catch (TimeoutException e) {
            dumpDebug("FAIL-did-not-progress-past-billing");
            throw new AssertionError(
                    "Checkout did not progress past the billing step - the 'Continue' click may have fired before all required fields were validated. Check for inline validation error messages on the page.", e
            );
        }

        dumpDebug("07-progressed-past-billing");
    }

    private void dumpDebug(String label) {
        try {
            String dir = "debug-output";
            Files.createDirectories(Paths.get(dir));
            String stamp = String.valueOf(System.currentTimeMillis());
            String base = dir + "/" + stamp + "_" + label;

            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(screenshot.toPath(), Paths.get(base + ".png"));

            String pageSource = driver.getPageSource();
            Files.write(Paths.get(base + ".html"), pageSource.getBytes());

            System.out.println("[DEBUG] Saved " + base + ".png and .html (URL: " + driver.getCurrentUrl() + ")");
        } catch (Exception e) {
            System.out.println("[DEBUG] Failed to dump debug info for '" + label + "': " + e.getMessage());
        }
    }

    private void fillFieldWithWait(String elementId, String value) {
        WebElement element = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.id(elementId))
        );

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", element);

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].value = arguments[1];" +
                        "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                        "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                element, value
        );

        String actualValue = element.getAttribute("value");
        if (actualValue == null || !actualValue.equals(value)) {
            element.clear();
            element.sendKeys(value);
        }
    }

    private void checkBoxIfPresent(String elementId) {
        try {
            WebElement checkbox = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.id(elementId))
            );
            if (!checkbox.isSelected()) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
            }
        } catch (TimeoutException e) {
            System.out.println("Checkbox with id '" + elementId + "' was not found/visible - skipping, but this may cause the 'Continue' click to fail validation.");
        }
    }
}