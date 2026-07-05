package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductPage {
    WebDriver driver;
    By Title = By.cssSelector("h1.h4");

    public ProductPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isProductListDisplayed() {
        return !driver.findElements(By.cssSelector(".product-layout")).isEmpty();
    }

}
