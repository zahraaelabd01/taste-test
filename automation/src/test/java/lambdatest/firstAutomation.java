package lambdatest;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class firstAutomation {

    WebDriver driver = new FirefoxDriver();

    @BeforeMethod
    public void openWebsite() {
        driver.navigate().to("https://ecommerce-playground.lambdatest.io/index.php?route=common/home");
    }



    @Test(priority = 1)
    public void validLogin() {
      if((driver.getCurrentUrl() ).contains("https://ecommerce-playground.lambdatest.io/index.php?route=common/home") ){

          System.out.println("Lambda opened successfully");
      } else {
          System.out.println("Lambda not opened ");
      }
    }


    @AfterMethod
    public void shutDown(){
        driver.quit();
    }



}

