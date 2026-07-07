package Pages.MyAccount;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class EditAccountPage {
    WebDriver driver;
    WebDriverWait wait;
    By ActualTitle = By.cssSelector(".page-title.h3.mb-3");
    String ExpectedTitle = "My Account Information";
    By FName = By.xpath("//input[@name=\"firstname\"]");
    By LName = By.xpath("//input[@name=\"lastname\"]");
    By Email = By.id("input-email");
    By Telephone = By.id("input-telephone");
    By ContinueBtn = By.xpath("//input[@value=\"Continue\"]");
    By BackBtn = By.xpath("//a[text()=\" Back\"]");
    By FNameErrorMsg = By.xpath("//div[contains(text(),\"First Name\")]");
    String ExpFNameErrorMsg = "First Name must be between 1 and 32 characters!";
    By LNameErrorMsg = By.xpath("//div[contains(text(),\"Last Name\")]");
    String ExpLNameErrorMsg = "Last Name must be between 1 and 32 characters!";
    By EmailErrorMsg = By.xpath("//div[contains(text(),\"E-Mail Address\")]");
    String ExpEmailErrorMsg = "E-Mail Address does not appear to be valid!";
    By TeleErrorMsg = By.xpath("//div[contains(text(),\"Telephone\")]");
    String ExpTeleErrorMsg = "Telephone must be between 3 and 32 characters!";
    By UpdateSuccessMsg= By.cssSelector(".alert.alert-success.alert-dismissible");
    String ExpUpdateSuccessMsg="Success: Your account has been successfully updated.";
    By RegisteredEmail = By.cssSelector(".alert.alert-danger.alert-dismissible");
    String ExpRegisteredEmail ="Warning: E-Mail address is already registered!";
    public EditAccountPage(WebDriver driver) {
        this.driver = driver;
        this.wait= new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void clickBack() {
        driver.findElement(BackBtn).click();
    }
    public void clickContinue() {
        driver.findElement(ContinueBtn).click();
    }

    public String getExpectedTitle() {
        return ExpectedTitle;
    }
    public String getActualTitle() {
        return driver.findElement(ActualTitle).getText();
    }

    public void setFName(String fName) {
        driver.findElement(FName).clear();
        driver.findElement(FName).sendKeys(fName);
    }

    public void setlName(String sName) {
        driver.findElement(LName).clear();
        driver.findElement(LName).sendKeys(sName);
    }

    public void setTelephone(String telephone) {
        driver.findElement(Telephone).clear();
        driver.findElement(Telephone).sendKeys(telephone);
    }

    public void setEmail(String email) {
        driver.findElement(Email).clear();
        driver.findElement(Email).sendKeys(email);
    }

    public String getFNameErrorMsg() {
        return driver.findElement(FNameErrorMsg).getText();
    }

    public String getExpFNameErrorMsg() {
        return ExpFNameErrorMsg;
    }

    public String getLNameErrorMsg() {
        return driver.findElement(LNameErrorMsg).getText();
    }

    public String getExpLNameErrorMsg() {
        return ExpLNameErrorMsg;
    }

    public String getEmailErrorMsg() {
        return driver.findElement(EmailErrorMsg).getText();
    }

    public String getExpEmailErrorMsg() {
        return ExpEmailErrorMsg;
    }

    public String getTeleErrorMsg() {
        return driver.findElement(TeleErrorMsg).getText();
    }

    public String getExpTeleErrorMsg() {
        return ExpTeleErrorMsg;
    }

    public String getUpdateSuccessMsg() {
        wait.until(ExpectedConditions.not(ExpectedConditions.titleIs("My Account Information")));
        return driver.findElement(UpdateSuccessMsg).getText();
    }

    public String getExpUpdateSuccessMsg() {
        return ExpUpdateSuccessMsg;
    }

    public String getRegisteredEmail() {
        return driver.findElement(RegisteredEmail).getText();
    }

    public String getExpRegisteredEmail() {
        return ExpRegisteredEmail;
    }
}
