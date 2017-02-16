package com.axibase.selenium;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileInputStream;
import java.util.Properties;

public class testCase {
    public static void main (String[] args) {
        System.setProperty("webdriver.chrome.driver", "chromedriver");
        WebDriver driver = new ChromeDriver();

        String url = "https://data.cdc.gov/api/views/mr8w-325u/rows.json?accessType=DOWNLOAD";

        Properties pr = new Properties();
        try {
            FileInputStream f = new FileInputStream("src/main/resources/data.properties");
            pr.load(f);
        } catch (Exception e) {
            System.out.println("property file is not loaded");
        }
        String collector = pr.getProperty("collector");
        String username = pr.getProperty("username");
        String password = pr.getProperty("password");

        //authentication
        driver.get(collector);
        WebElement field = driver.findElement(By.id("username"));
        field.sendKeys(username);
        field = driver.findElement(By.name("password"));
        field.sendKeys(password);
        field.submit();

        WebElement link = driver.findElement(By.linkText("Jobs"));
        link.click();

        link = driver.findElement(By.linkText("autosocrata"));
        link.click();
        link = driver.findElement(By.linkText("Create Configuration"));
        link.click();

        field = driver.findElement(By.name("path"));
        field.sendKeys(url);

        link = driver.findElement(By.id("btnAdd"));
        link.click();

        link = driver.findElement(By.id("btnTest"));
        link.click();

        System.out.print(driver.findElements(By.xpath(
                "//*[@id=\"testRow_2\"]/td/table/tbody/tr[3]/td[2]/pre/span")).size()); // = 6364

        driver.quit();
    }
}
//*[@id="testRow_2"]/td/table/tbody/tr[3]/td[2]/pre/span[6364]