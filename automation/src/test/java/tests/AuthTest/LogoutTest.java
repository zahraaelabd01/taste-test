package tests.AuthTest;
import BaseTest.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


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
        String AfterTitle = driver.getTitle();
       System.out.println(AfterTitle);
        Assert.assertEquals( AfterTitle,"Account Login","BUG: Clicking continue did not redirect the user to the Login page!");
   }
   @Test(description = "TC_AUTH_017")
    public void UserSessionAfterLogout(){
       logoutPage.ContinueClick();
       driver.get("https://ecommerce-playground.lambdatest.io/index.php?route=account/account");
       Assert.assertEquals(driver.getTitle(), "Account Login");
       System.out.println("User session test pass");
       System.out.println("************************");
   }
}
