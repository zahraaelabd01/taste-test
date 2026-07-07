package Pages.MyAccount;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LogoutSuccessPage {
    WebDriver driver;
    By ActualTitle = By.cssSelector(".page-title.my-3");
    String ExpTitle="Account Logout";
    By ContinueBtn= By.xpath("//a[text()='Continue']");

    public LogoutSuccessPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getActualTitle() {
        return driver.findElement(ActualTitle).getText();
    }

    public String getExpTitle() {
        return ExpTitle;
    }
    public void ContinueClick(){
        driver.findElement(ContinueBtn).click();
    }
}
