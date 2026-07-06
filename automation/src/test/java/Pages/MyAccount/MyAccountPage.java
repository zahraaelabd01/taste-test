package Pages.MyAccount;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MyAccountPage {
    WebDriver driver;
    By ActualTitle = By.xpath("//h2[text()=\"My Account\"]");
    String ExpectedTitle ="My Account";
    By accountEdit = By.xpath("//div[@id='content']//a[contains(@href, 'account/edit')]");
    By passwordBtn = By.xpath("//div[@id='content']//a[contains(@href, 'account/password')]");
    By addressBookBtn = By.xpath("//div[@id='content']//a[contains(@href, 'account/address')]");
    By wishListBtn = By.xpath("//div[@id='content']//a[contains(@href, 'account/wishlist')]");
    By newsletterBtn = By.xpath("//div[@id='content']//a[contains(@href, 'account/newsletter')]");

    By orderHistoryBtn = By.xpath("//div[@id='content']//a[contains(@href, 'account/order')]");
    By downloadsBtn = By.xpath("//div[@id='content']//a[contains(@href, 'account/download')]");
    By rewardPointsBtn = By.xpath("//div[@id='content']//a[contains(@href, 'account/reward')]");
    By returnsBtn = By.xpath("//div[@id='content']//a[contains(@href, 'account/return')]");
    By transactionsBtn = By.xpath("//div[@id='content']//a[contains(@href, 'account/transaction')]");
    By recurringPaymentsBtn = By.xpath("//div[@id='content']//a[contains(@href, 'account/recurring')]");

    By accountAffiliate = By.xpath("//div[@id='content']//a[contains(@href, 'account/affiliate')]");
    public MyAccountPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getActualTitle(){
        return driver.findElement(ActualTitle).getText();
    }

    public String getExpectedTitle() {
        return ExpectedTitle;
    }

    public EditAccountPage clickEditAccount() {
        driver.findElement(accountEdit).click();
        return new EditAccountPage(driver);
    }

    public void clickChangePassword() {
        driver.findElement(passwordBtn).click();
    }

    public void clickAddressBook() {
        driver.findElement(addressBookBtn).click();
    }

    public void clickWishList() {
        driver.findElement(wishListBtn).click();
    }

    public void clickNewsletter() {
        driver.findElement(newsletterBtn).click();
    }

    public void clickOrderHistory() {
        driver.findElement(orderHistoryBtn).click();
    }

    public void clickDownloads() {
        driver.findElement(downloadsBtn).click();
    }

    public void clickRewardPoints() {
        driver.findElement(rewardPointsBtn).click();
    }

    public void clickReturns() {
        driver.findElement(returnsBtn).click();
    }

    public void clickTransactions() {
        driver.findElement(transactionsBtn).click();
    }

    public void clickRecurringPayments() {
        driver.findElement(recurringPaymentsBtn).click();
    }
    public void clickAccountAffiliate() {
        driver.findElement(accountAffiliate).click();
    }

}
