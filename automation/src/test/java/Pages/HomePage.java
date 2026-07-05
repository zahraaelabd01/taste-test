package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class HomePage {
    WebDriver driver;
    private WebDriverWait wait;
    By register = By.xpath("//div//a[text()=\"Continue\"]");
    By MyAccount= By.xpath("//div[@id='widget-navbar-217834']//a[normalize-space()='My account']");
    By MegaMenu= By.cssSelector("icon-left.both.nav-link.dropdown-toggle");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public LoginPage MyAccountClick(){
        driver.findElement(MyAccount).click();
        return new LoginPage(driver);
    }
    public RegisterPage RegisterClick(){
        driver.findElement(MyAccount).click();
        driver.findElement(register).click();
        return new RegisterPage(driver);
    }

    public void navigateToMegaMenuProduct(String subPageName) {
        // 1. Locate and hover over the Mega Menu parent
        WebElement megaMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(MegaMenu));
        Actions actions = new Actions(driver);
        actions.moveToElement(megaMenu).perform();

        // 2. Dynamically build the locator using the page name you passed in
        String dynamicXpath = String.format("//ul[contains(@class, 'dropdown-menu')]//a[text()='%s']", subPageName);
        WebElement targetLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(dynamicXpath)));

        // 3. Click the page
        targetLink.click();
    }
}
