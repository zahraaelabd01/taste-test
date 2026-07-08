package tests;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


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
        Assert.assertEquals(editAccountPage.getUpdateSuccessMsg(),editAccountPage.getExpUpdateSuccessMsg());
        System.out.println("---Successful Account Update Test pass---");
        System.out.println("************************");

        //Return email back to default
        editAccountPage= myAccountPage.clickEditAccount();
        editAccountPage.setEmail("testhabeeba008@gmail.com");
        editAccountPage.clickContinue();
    }
    @Test
    public void RegisteredEmailTest(){
        editAccountPage.setFName("test");
        editAccountPage.setlName("edit");
        editAccountPage.setEmail("test@gmail.com");
        editAccountPage.setTelephone("12345678");
        editAccountPage.clickContinue();
        Assert.assertEquals(editAccountPage.getRegisteredEmail(),editAccountPage.getExpRegisteredEmail());
        System.out.println("---Registered Email Error Message Test pass---");
        System.out.println("************************");
    }
    @Test
    public void EmptyFieldsTest() {

        editAccountPage.setFName("");
        editAccountPage.setlName("");
        editAccountPage.setEmail("");
        editAccountPage.setTelephone("");
        editAccountPage.clickContinue();
        Assert.assertEquals(editAccountPage.getFNameErrorMsg(),editAccountPage.getExpFNameErrorMsg(),
                "First Name error message did not match!");
        Assert.assertEquals(editAccountPage.getLNameErrorMsg(),editAccountPage.getExpLNameErrorMsg(),
                "Last Name error message did not match!");
        Assert.assertEquals(editAccountPage.getEmailErrorMsg(),editAccountPage.getExpEmailErrorMsg(),
                "Email error message did not match!");
        Assert.assertEquals(editAccountPage.getTeleErrorMsg(),editAccountPage.getExpTeleErrorMsg(),
                "Telephone error message did not match!");
        System.out.println("---Empty fields Error Messages test pass--- ");
    }
}
