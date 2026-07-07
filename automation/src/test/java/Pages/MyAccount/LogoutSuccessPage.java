package Pages.MyAccount;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LogoutSuccessPage {
    WebDriver driver;
    WebDriverWait wait ;
    By ActualTitle = By.cssSelector(".page-title.my-3");
    String ExpTitle="Account Logout";
    By ContinueBtn= By.xpath("//a[text()='Continue']");

    public LogoutSuccessPage(WebDriver driver) {
        this.driver = driver;
        this.wait= new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public String getActualTitle() {
        return driver.findElement(ActualTitle).getText();
    }

    public String getExpTitle() {
        return ExpTitle;
    }
    public void ContinueClick(){
        driver.findElement(ContinueBtn).click();
        wait.until(ExpectedConditions.not(ExpectedConditions.titleIs("Account Logout")));
    }
}
