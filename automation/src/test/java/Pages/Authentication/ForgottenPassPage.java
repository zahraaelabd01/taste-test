package Pages.Authentication;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ForgottenPassPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private By ActualTitle = By.cssSelector(".page-title.h3.mb-3");
    private String ExpTitle ="Forgot Your Password?";
    private By Email = By.cssSelector("#input-email");
    private By ContinueBtn = By.xpath("//div[@class=\"float-right\"]/button");
    private By BackBtn = By.xpath("//div[@class=\"float-left\"]/a");
    private By EmailErrorMsg = By.cssSelector(".alert.alert-danger.alert-dismissible");
    private String ExpEmailErrorMsg="Warning: The E-Mail Address was not found in our records, please try again!";
    private By SuccessMsg= By.cssSelector(".alert.alert-success.alert-dismissible");
    private String ExpSuccessMsg ="An email with a confirmation link has been sent your email address.";

    public ForgottenPassPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    public String getActualTitle() {
        return driver.findElement(ActualTitle).getText();
    }

    public String getExpTitle() {
        return ExpTitle;
    }

    public String getExpEmailErrorMsg() {
        return ExpEmailErrorMsg;
    }

    public String getExpSuccessMsg() {
        return ExpSuccessMsg;
    }
    public void setEmail(String email) {
        driver.findElement(Email).sendKeys(email);
    }
    public String getSuccessMsg() {
      return wait.until(ExpectedConditions.visibilityOfElementLocated(SuccessMsg)).getText();
    }
    public String getEmailErrorMsg() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(EmailErrorMsg)).getText();
    }
    public LoginPage clickBack() {
        driver.findElement(BackBtn).click();
        return new LoginPage(driver);
    }
    public LoginPage clickContinue() {
        driver.findElement(ContinueBtn).click();
        return new LoginPage(driver);
    }
}
