package Pages.Authentication;
import Pages.MyAccount.MyAccountPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AccountSuccessPage {
    WebDriver driver;
    By SuccessMsg = By.cssSelector(".page-title.my-3");
    String ExpSuccessMsg="Your Account Has Been Created!";
    By continueBtn = By.cssSelector(".buttons.mb-4");

    public AccountSuccessPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getSuccessMsg() {
        return driver.findElement(SuccessMsg).getText();
    }

    public MyAccountPage ClickContinue() {
        driver.findElement(continueBtn).click();
        return new MyAccountPage(driver);
    }

    public String getExpSuccessMsg() {
        return ExpSuccessMsg;
    }
}
