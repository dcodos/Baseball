package com.gbasbaseball;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.List;

public class SeleniumTest  {
    public static void main(String[] args) {
// The Firefox driver supports javascript
        WebDriver driver = new FirefoxDriver();

        // Go to the Google Suggest home page
        driver.get("http://songza.com/listen/00s-club-bangers-songza/");

        WebElement query = driver.findElement(By.className("miniplayer-control-play-pause"));
        query.click();

        driver.quit();
    }
}
