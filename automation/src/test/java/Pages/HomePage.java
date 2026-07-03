package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {
    WebDriver driver;
    By register = By.xpath("//div//a[text()=\"Continue\"]");
    By myaccount= By.xpath("//div[@id='widget-navbar-217834']//a[normalize-space()='My account']");


    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public LoginPage MyAccountClick(){
        driver.findElement(myaccount).click();
        return new LoginPage(driver);
    }
    public RegisterPage RegisterClick(){
        driver.findElement(myaccount).click();
        driver.findElement(register).click();
        return new RegisterPage(driver);
    }
}
