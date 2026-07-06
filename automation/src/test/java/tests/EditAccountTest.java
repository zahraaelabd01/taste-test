package tests;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class EditAccountTest extends BaseTest.BaseTest {
    @BeforeMethod
    public void GoToEditAccountPagePage() {
        myAccountPage=login();
        editAccountPage=myAccountPage.clickEditAccount();
        Assert.assertEquals(editAccountPage.getActualTitle(),editAccountPage.getExpectedTitle());
        System.out.println("Edit Account Page Opened");
        System.out.println("************************");
    }
    @Test
    public void SuccessfulAccountUpdateTest(){
        editAccountPage.setFName("test");
        editAccountPage.setlName("edit");
        editAccountPage.setEmail("test9999922@gmail.com");
        editAccountPage.setTelephone("12345678");
        editAccountPage.clickContinue();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.not(ExpectedConditions.titleIs("My Account Information")));
        Assert.assertEquals(editAccountPage.getUpdateSuccessMsg(),editAccountPage.getExpUpdateSuccessMsg());
        System.out.println("Successful Account Update Test pass");
        System.out.println("************************");
    }
    @Test
    public void RegisteredEmailTest(){
        editAccountPage.setFName("test");
        editAccountPage.setlName("edit");
        editAccountPage.setEmail("test@gmail.com");
        editAccountPage.setTelephone("12345678");
        editAccountPage.clickContinue();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Assert.assertEquals(editAccountPage.getRegisteredEmail(),editAccountPage.getExpRegisteredEmail());
        System.out.println("Registered Email Error Message Test pass");
        System.out.println("************************");
    }
}
