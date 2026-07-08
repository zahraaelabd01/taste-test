package tests;
import BaseTest.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ChangePassTest extends BaseTest {
    @BeforeMethod
    public void OpenChangePassPage() {
        myAccountPage = login();
        changePass = myAccountPage.clickChangePassword();
        Assert.assertEquals(changePass.getActualTitle(), changePass.getExpTitle());
        System.out.println("Change password page opened");
    }

    @Test
    public void EmptyPasswordFieldsTest() {
        changePass.clickContinue();
        Assert.assertEquals(changePass.getActualPassErrorMsg(), changePass.getExpPassErrorMsg());
        System.out.println("---Empty field Error messages pass---");
    }

    @Test
    public void PasswordMismatchTest() {
        changePass.setPassword("new123");
        changePass.setConfirmPassword("12345");
        changePass.clickContinue();
        Assert.assertEquals(changePass.getActualConfirmPassErrorMsg(), changePass.getExpConfirmPassErrorMsg());
        System.out.println("---Password mismatch test pass---");
    }

    @Test
    public void PasswordChangeTest() {
        changePass.setPassword("test1234");
        changePass.setConfirmPassword("test1234");
        myAccountPage = changePass.clickContinue();
        Assert.assertEquals(changePass.getActualSuccessUpdateMsg(), changePass.getExpSuccessUpdateMsg());
        System.out.println("Password updated test pass");

        //return password back to default
        changePass = myAccountPage.clickChangePassword();
        changePass.setPassword("1234");
        changePass.setConfirmPassword("1234");
        changePass.clickContinue();
    }

}