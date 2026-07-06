package Pages.Authentication;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RegisterPage {
    WebDriver driver;
    By ActualTitle = By.cssSelector(".page-title.h3");
    String ExpectedTitle ="Register Account";
    By FName = By.xpath("//input[@name=\"firstname\"]");
    By LName = By.xpath("//input[@name=\"lastname\"]");
    By Email = By.xpath("//input[@name=\"email\"]");
    By Telephone = By.xpath("//input[@name=\"telephone\"]");
    By Password =By.cssSelector("#input-password");
    By ConfirmPassword =By.cssSelector("#input-confirm");
    By yesRadioBtn= By.cssSelector("#input-newsletter-yes");
    By NoRadioBtn= By.cssSelector("#input-newsletter-no");
    By PolicyCheckbox= By.xpath("//label[@for='input-agree']");
    By ContinueBtn= By.xpath("//input[@type=\"submit\"]");
    By PolicyErrorMsg= By.xpath("div[class=\"alert alert-danger alert-dismissible\"]");
    By FNameErrorMsg= By.xpath("//input[@name=\"firstname\"]/following-sibling::div");
    String ExpFNameErrorMsg="First Name must be between 1 and 32 characters!";
    By LNameErrorMsg= By.xpath("//input[@name=\"lastname\"]/following-sibling::div");
    String ExpLNameErrorMsg="Last Name must be between 1 and 32 characters!";
    By EmailErrorMsg= By.xpath("//input[@name=\"email\"]/following-sibling::div");
    String ExpEmailErrorMsg="E-Mail Address does not appear to be valid!";
    By TelephoneErrorMsg= By.xpath("//input[@name=\"telephone\"]/following-sibling::div");
    String ExpTeleErrorMsg="Telephone must be between 3 and 32 characters!";
    By PassErrorMsg= By.xpath("//input[@name=\"password\"]/following-sibling::div");
    String ExpPassErrorMsg="Password must be between 4 and 20 characters!";
    By ConfirmPassErrorMsg= By.xpath("//input[@name=\"confirm\"]/following-sibling::div");
    String ExpConfirmPassErrorMsg="Password confirmation does not match password!";


    public RegisterPage(WebDriver driver) {
        this.driver = driver;
    }

    public void setFirstName(String firstName) {
        driver.findElement(FName).clear();
        driver.findElement(FName).sendKeys(firstName);
    }

    public void setLastName(String lastName) {
        driver.findElement(LName).clear();
        driver.findElement(LName).sendKeys(lastName);
    }

    public void setEmail(String email) {
        driver.findElement(Email).clear();
        driver.findElement(Email).sendKeys(email);
    }

    public void setTelephone(String phone) {
        driver.findElement(Telephone).clear();
        driver.findElement(Telephone).sendKeys(phone);
    }

    public void setPassword(String password) {
        driver.findElement(Password).clear();
        driver.findElement(Password).sendKeys(password);
    }

    public void setConfirmPassword(String confirmPassword) {
        driver.findElement(ConfirmPassword).clear();
        driver.findElement(ConfirmPassword).sendKeys(confirmPassword);
    }

    public void clickYesNewsletter() {
        driver.findElement(yesRadioBtn).click();
    }

    public void clickNoNewsletter() {
        driver.findElement(NoRadioBtn).click();
    }

    public void clickPrivacyPolicy() {
        if (PrivacyPolicySelected()){
            return;
        }
        driver.findElement(PolicyCheckbox).click();
    }
    public boolean PrivacyPolicySelected(){
        return driver.findElement(PolicyCheckbox).isSelected();
    }

    public AccountSuccessPage clickContinue() {
        driver.findElement(ContinueBtn).click();
        return  new AccountSuccessPage(driver);
    }

    public String getActualTitle() {
        return driver.findElement(ActualTitle).getText();
    }

    public String getActualPolicyErrorMsg() {
        return driver.findElement(PolicyErrorMsg).getText();
    }

    public String getActualFNameErrorMsg() {
        return driver.findElement(FNameErrorMsg).getText();
    }

    public String getActualLNameErrorMsg() {
        return driver.findElement(LNameErrorMsg).getText();
    }

    public String getActualEmailErrorMsg() {
        return driver.findElement(EmailErrorMsg).getText();
    }

    public String getActualTelephoneErrorMsg() {
        return driver.findElement(TelephoneErrorMsg).getText();
    }

    public String getActualPassErrorMsg() {
        return driver.findElement(PassErrorMsg).getText();
    }

    public String getActualConfirmPassErrorMsg() {
        return driver.findElement(ConfirmPassErrorMsg).getText();
    }

    public String getExpectedTitle() {
        return ExpectedTitle;
    }

    public String getExpectedFNameErrorMsg() {
        return ExpFNameErrorMsg;
    }

    public String getExpectedLNameErrorMsg() {
        return ExpLNameErrorMsg;
    }

    public String getExpectedEmailErrorMsg() {
        return ExpEmailErrorMsg;
    }

    public String getExpectedTelephoneErrorMsg() {
        return ExpTeleErrorMsg;
    }

    public String getExpectedPassErrorMsg() {
        return ExpPassErrorMsg;
    }

    public String getExpectedConfirmPassErrorMsg() {
        return ExpConfirmPassErrorMsg;
    }
}
