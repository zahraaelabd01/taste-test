package Pages.Authentication;

import Pages.MyAccount.MyAccountPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private By ActualTitle = By.xpath("//h2[text()=\"Returning Customer\"]");
    private String ExpectedTitle ="Returning Customer";
    private By Email =By.id("input-email");
    private By Password =By.id("input-password");
    private By LoginButton=By.xpath("//input[@value=\"Login\"]");
    private By ActualErrorMsg=By.cssSelector(".alert.alert-danger.alert-dismissible");
    private String ExpErrorMsg="Warning: No match for E-Mail Address and/or Password.";
    private String ExpAccLimitErrorMsg="Warning: Your account has exceeded allowed number of login attempts. Please try again in 1 hour.";
    private String ExpEmptyFieldErrorMsg= "Email address or password field can't be empty";
    private By ForgottenPassLink= By.xpath("//div[@class=\"form-group\"]/a");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait= new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public String getExpTitle() {
        return ExpectedTitle;
    }

    public String getExpErrorMsg() {
        return ExpErrorMsg;
    }

    public String getActualErrorMsg() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(ActualErrorMsg)).getText();
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
        driver.findElement(ForgottenPassLink).click();
        return new ForgottenPassPage(driver);
    }

    public String getExpAccLimitErrorMsg() {
        return ExpAccLimitErrorMsg;
    }

    public String getExpEmptyFieldErrorMsg() {
        return ExpEmptyFieldErrorMsg;
    }
}
