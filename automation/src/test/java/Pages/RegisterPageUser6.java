package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RegisterPageUser6 {

    WebDriver driver;

    public RegisterPageUser6(WebDriver driver) {
        this.driver = driver;
    }

    By firstName = By.id("input-firstname");
    By lastName = By.id("input-lastname");
    By email = By.id("input-email");
    By telephone = By.id("input-telephone");
    By password = By.id("input-password");
    By confirmPassword = By.id("input-confirm");
    By continueBtn = By.cssSelector("input[type='submit']");

    public void openRegisterPage() {
        driver.get("https://ecommerce-playground.lambdatest.io/index.php?route=account/register");
    }

    public void registerUser(String fName, String lName, String mail, String phone, String pass) {

        driver.findElement(firstName).sendKeys(fName);
        driver.findElement(lastName).sendKeys(lName);
        driver.findElement(email).sendKeys(mail);
        driver.findElement(telephone).sendKeys(phone);
        driver.findElement(password).sendKeys(pass);
        driver.findElement(confirmPassword).sendKeys(pass);

        By agreeLabel = By.cssSelector("label[for='input-agree']");
        driver.findElement(agreeLabel).click();

        driver.findElement(continueBtn).click();
    }
}