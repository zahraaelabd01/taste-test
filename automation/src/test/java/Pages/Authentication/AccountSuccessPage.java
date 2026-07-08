package Pages.Authentication;
import Pages.MyAccount.MyAccountPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AccountSuccessPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private By SuccessMsg = By.cssSelector(".page-title.my-3");
    private String ExpSuccessMsg="Your Account Has Been Created!";
    private By continueBtn = By.cssSelector(".buttons.mb-4");

    public AccountSuccessPage(WebDriver driver) {
        this.driver = driver;
        this.wait= new WebDriverWait(driver, Duration.ofSeconds(10));

    }

    public String getSuccessMsg() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(SuccessMsg)).getText();
    }

    public MyAccountPage ClickContinue() {
        driver.findElement(continueBtn).click();
        return new MyAccountPage(driver);
    }

    public String getExpSuccessMsg() {
        return ExpSuccessMsg;
    }
}
