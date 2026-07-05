package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage {

    WebDriver driver;

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
    }

    By checkoutButton = By.linkText("Checkout");

    public void openWebsite() {
        driver.get("https://ecommerce-playground.lambdatest.io/");
    }

    public void clickCheckout() {
        driver.findElement(checkoutButton).click();
    }
}