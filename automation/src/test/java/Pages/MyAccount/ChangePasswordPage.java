package Pages.MyAccount;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ChangePasswordPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private By ActualTitle = By.cssSelector(".page-title.h3.mb-3");
    private String ExpTitle ="Change Password";
    private By Password = By.xpath("//input[@name=\"password\"]");
    private By ConfirmPassword = By.xpath("//input[@name=\"confirm\"]");
    private By ContinueBtn = By.xpath("//input[@value=\"Continue\"]");
    private By BackBtn = By.xpath("//a[text()=\" Back\"]");
    private By PassErrorMs = By.xpath("//input[@name=\"password\"]/following-sibling::div ");
    private String ExpPassErrorMs="Password must be between 4 and 20 characters!";
    private By ConfirmPassErrorMsg = By.xpath("//input[@name=\"confirm\"]/following-sibling::div ");
    private String ExpConfirmPassErrorMs="Password confirmation does not match password!";
    private By SuccessUpdateMsg= By.cssSelector(".alert.alert-success.alert-dismissible");
    private String ExpSuccessUpdateMsg ="Success: Your password has been successfully updated.";


    public ChangePasswordPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    public void setPassword(String pass) {
        driver.findElement(Password).clear();
        driver.findElement(Password).sendKeys(pass);
    }

    public void setConfirmPassword(String confirmPass) {
        driver.findElement(ConfirmPassword).clear();
        driver.findElement(ConfirmPassword).sendKeys(confirmPass);
    }

    public String getActualTitle() {
        return driver.findElement(ActualTitle).getText();
    }

    public String getActualPassErrorMsg() {
        return driver.findElement(PassErrorMs).getText();
    }

    public String getActualConfirmPassErrorMsg() {
        return driver.findElement(ConfirmPassErrorMsg).getText();
    }

    public String getActualSuccessUpdateMsg() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(SuccessUpdateMsg)).getText();
    }

    public String getExpTitle() {
        return ExpTitle;
    }

    public String getExpPassErrorMsg() {
        return ExpPassErrorMs;
    }

    public String getExpConfirmPassErrorMsg() {
        return ExpConfirmPassErrorMs;
    }

    public MyAccountPage clickContinue() {
        driver.findElement(ContinueBtn).click();
        return new MyAccountPage(driver);
    }

    public MyAccountPage clickBack() {
        driver.findElement(BackBtn).click();
        return new MyAccountPage(driver);
    }

    public String getSuccessUpdateMsg() {
        return driver.findElement(SuccessUpdateMsg).getText();
    }
    public String getExpSuccessUpdateMsg() {
        return ExpSuccessUpdateMsg;
    }
}
