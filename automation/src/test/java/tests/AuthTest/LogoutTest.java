package tests.AuthTest;

import BaseTest.BaseTest;
import Pages.MyAccount.MyAccountPage;
import com.beust.ah.A;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class LogoutTest extends BaseTest {

    @BeforeMethod
    public void GoToLogoutPage(){
       myAccountPage = login();
       logoutPage=myAccountPage.LogoutClick();
        Assert.assertEquals(logoutPage.getActualTitle(),logoutPage.getExpTitle());
        System.out.println("Logout Success Page opened");
        System.out.println("************************");
   }
   @Test(enabled = false, description ="TC_AUTH_015 - BUG: Clicking continue did not redirect the user to the Login page!" )
    public void RedirectAfterLogoutTest(){
        logoutPage.ContinueClick();
       WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
       wait.until(ExpectedConditions.not(ExpectedConditions.titleIs("Account Logout")));
        String AfterTitle = driver.getTitle();
       System.out.println(AfterTitle);
        Assert.assertEquals( AfterTitle,"Account Login","BUG: Clicking continue did not redirect the user to the Login page!");
   }
}
