package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    WebDriver driver;
    By ActualTitle = By.xpath("//h2[text()=\"Returning Customer\"]");
    String ExpectedTitle ="Returning Customer";
    By Email =By.id("input-email");
    By Password =By.id("input-password");
    By LoginButton=By.xpath("//input[@value=\"Login\"]");
    By ForgottenPass=By.xpath("//div[@class=\"form-group\"]/a");
    By ActualErrorMsg=By.cssSelector(".alert.alert-danger.alert-dismissible");
    String ExpErrormsg=" Warning: No match for E-Mail Address and/or Password.";

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getExpTitle() {
        return ExpectedTitle;
    }

    public String getExpErrormsg() {
        return ExpErrormsg;
    }

    public String getActualErrorMsg() {
        return driver.findElement(ActualErrorMsg).getText();
    }

    public String getActualTitle() {
        return driver.findElement(ActualTitle).getText();
    }

    public void setEmail(String email) {
        driver.findElement(Email).sendKeys(email);
    }

    public void setPassword(String  password) {
        driver.findElement(Password).sendKeys(password);
    }
}
