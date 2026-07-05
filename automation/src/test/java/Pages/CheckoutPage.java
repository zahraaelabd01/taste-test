package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage {

    WebDriver driver;

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
    }

    By checkoutBtn = By.xpath("//a[contains(text(),'Checkout')]");
    By firstName = By.id("input-payment-firstname");
    By lastName = By.id("input-payment-lastname");
    By address = By.id("input-payment-address-1");
    By city = By.id("input-payment-city");
    By postCode = By.id("input-payment-postcode");
    By confirmOrder = By.id("button-confirm");
    By successMessage = By.cssSelector(".alert-success");

    public void clickCheckout() {
        driver.findElement(checkoutBtn).click();
    }

    public void enterFirstName(String name) {
        driver.findElement(firstName).sendKeys(name);
    }

    public void enterLastName(String name) {
        driver.findElement(lastName).sendKeys(name);
    }

    public void enterAddress(String add) {
        driver.findElement(address).sendKeys(add);
    }

    public void enterCity(String cityName) {
        driver.findElement(city).sendKeys(cityName);
    }

    public void enterPostCode(String code) {
        driver.findElement(postCode).sendKeys(code);
    }

    public void confirmOrder() {
        driver.findElement(confirmOrder).click();
    }

    public String getSuccessMessage() {
        return driver.findElement(successMessage).getText();
    }
}