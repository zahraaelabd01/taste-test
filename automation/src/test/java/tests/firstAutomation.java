package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class firstAutomation {

    WebDriver driver = new FirefoxDriver();

    @BeforeMethod
    public void openWebsite() {
        driver.navigate().to("https://the-internet.herokuapp.com/");
        driver.findElement( By.linkText("Form Authentication")).click();
    }



    @Test(priority = 1)
    public void validLogin() {
        driver.findElement(By.id("username")).sendKeys("tomsmith");
        driver.findElement(By.id("password")).sendKeys("SuperSecretPassword!");
        driver.findElement(By.className("radius")).click();
        String URL = driver.getCurrentUrl();
        if (URL.contains("https://the-internet.herokuapp.com/secure")) {
            System.out.println("valid Test Passed");
            // driver.findElement(By.className("radius")).click();
            driver.findElement(By.cssSelector(".button.secondary.radius")).click();
        } else {
            System.out.println("valid Test Failed");
        }
    }


    @AfterMethod
    public void shutDown(){
        driver.quit();
    }



}

