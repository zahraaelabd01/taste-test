package Pages.Authentication;

import Pages.MyAccount.MyAccountPage;
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
    String ExpErrorMsg="Warning: No match for E-Mail Address and/or Password.";
    String ExpAccLimitErrorMsg="Warning: Your account has exceeded allowed number of login attempts. Please try again in 1 hour.";
    String ExpEmptyFieldErrorMsg= "Email address or password field can't be empty";
    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getExpTitle() {
        return ExpectedTitle;
    }

    public String getExpErrorMsg() {
        return ExpErrorMsg;
    }

    public String getActualErrorMsg() {
        return driver.findElement(ActualErrorMsg).getText();
    }

    public String getActualTitle() {
        return driver.findElement(ActualTitle).getText();
    }

    public void setEmail(String email) {
        driver.findElement(Email).clear();
        driver.findElement(Email).sendKeys(email);
    }

    public void setPassword(String  password) {
        driver.findElement(Password).clear();
        driver.findElement(Password).sendKeys(password);
    }
    public MyAccountPage LoginClick(){
        driver.findElement(LoginButton).click();
        return new MyAccountPage(driver);
    }
    public ForgottenPassPage ForgetClick(){
        driver.findElement(ForgottenPass).click();
        return new ForgottenPassPage(driver);
    }

    public String getExpAccLimitErrorMsg() {
        return ExpAccLimitErrorMsg;
    }

    public String getExpEmptyFieldErrorMsg() {
        return ExpEmptyFieldErrorMsg;
    }
}
