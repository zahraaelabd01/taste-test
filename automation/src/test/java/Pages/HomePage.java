package Pages;

import Pages.Authentication.LoginPage;
import Pages.Authentication.RegisterPage;
import Pages.MyAccount.LogoutSuccessPage;
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
    By register = By.xpath("//a[contains(@href,\"account/register\")]");
    By MyAccount= By.xpath("//a[contains(@class, 'dropdown-toggle') and contains(., 'My account')]");

    By megaMenuLocator= By.xpath("//a[normalize-space()='Mega Menu']");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public LoginPage MyAccountClick(){
        driver.findElement(MyAccount).click();
        return new LoginPage(driver);
    }
    public RegisterPage RegisterClick(){
        WebElement MyAccountDropdown= wait.until(ExpectedConditions.visibilityOfElementLocated(MyAccount));
        Actions action = new Actions(driver);
        action.moveToElement(MyAccountDropdown).perform();
        WebElement registerLinkElement = wait.until(ExpectedConditions.visibilityOfElementLocated(register));
        registerLinkElement.click();
        return new RegisterPage(driver);
    }


    public ProductPage navigateToMegaMenuProduct(String subPageName) {
        WebElement megaMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(megaMenuLocator));
        //   /\ This solves the problem of trying to interact with an element before the page has finished loading
        new Actions(driver).moveToElement(megaMenu).pause(Duration.ofMillis(300)).perform();  // simulate hover with a slight pause to allow the menu to render
     //simulate user input    moving mouse            waiting 300ms
        String dynamicXpath = String.format(
                "//ul[contains(@class,'mega-menu-content')]//a[normalize-space()='%s']", subPageName);
         //inside the <ul> element that contain mege-menu-content
        // find <a> link whose visible text matches the name passed
        WebElement targetLink;
        try {
            targetLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(dynamicXpath)));
        } catch (org.openqa.selenium.TimeoutException e) {
            // JS fallback if native hover didn't register
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                    "arguments[0].dispatchEvent(new MouseEvent('mouseover', {bubbles:true}));", megaMenu);
            targetLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(dynamicXpath)));
        }

        targetLink.click();
        return new ProductPage(driver);
    }
}
