package com.axibase.selenium;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class testCase {

    private static final int pagesNumber = 20;
    public static void main (String[] args) {

/*
        System.setProperty("webdriver.chrome.driver", "chromedriver");
        WebDriver driver = new ChromeDriver();
*/

        Properties pr = new Properties();
        try {
            FileInputStream f = new FileInputStream("src/main/resources/data.properties");
            pr.load(f);
        } catch (Exception e) {
            System.out.println("property file data.properties is not loaded");
        }
        String collector = pr.getProperty("collector");
        String username = pr.getProperty("username");
        String password = pr.getProperty("password");
        String[] url = pr.getProperty("url").split(";");
        String[] caturl = new String[url.length];


        String[] pair = pr.getProperty("urlBase").split(";");
        String[] urlBase = new String[pair.length];
        String[] caturlBase = new String[pair.length];
        for (int i = 0; i < pair.length; i++) {
            String[] split = pair[i].split(",");
            urlBase[i] = split[0];
            caturlBase[i] = split[1];
        }

        Pattern pattern = Pattern.compile(".*(?=/)");

        for (int i = 0; i < url.length; i++) {
            for (int j = 0; j < urlBase.length; j++) {
                Matcher matcher = pattern.matcher(urlBase[j]);
                if (matcher.find()) {
                    if (url[i].equals(matcher.group())) {
                        caturl[i] = caturlBase[j];
                    }
                }
            }
        }

        for (int i = 0; i < url.length; i++) {
            System.out.print(url[i] + " " + caturl[i] + "\n");
        }


/*
        Pattern pattern = Pattern.compile(".*(?=/)");
        int unfoundNumber = url.length;
        WebElement json, ref;
        for (int i = 1; i <= pagesNumber; i++) {
            driver.get("https://catalog.data.gov/dataset?res_format=JSON&_res_format_limit=0&page=" + i);
            for (int l = 1; l <= 20; l++) {
                try {
                    json = driver.findElement(By.xpath(
                            "//*[@id=\"content\"]/div[2]/div/section[1]/div[2]/ul/li["+l+"]/div/ul/li[3]/a"));
                    ref = driver.findElement(By.xpath(
                            "//*[@id=\"content\"]/div[2]/div/section[1]/div[2]/ul/li["+l+"]/div/h3/a"));
                    Matcher matcher = pattern.matcher(json.getAttribute("href"));
                    if (matcher.find()) {
                        for (int k = 0; k < url.length; k++) {
                            String a = matcher.group();
                            String b = json.getAttribute("href");
                            if (url[k].equals(matcher.group())) {
                                caturl[k] = ref.getAttribute("href");
                                unfoundNumber--;
                                break;
                            }
                        }
                    }
                    if (unfoundNumber == 0) break;
                } catch (Exception ignored) {}
            }
            if (unfoundNumber == 0) break;
        }
*/
/*
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
*/
        //driver.quit();
    }
}
//*[@id="testRow_2"]/td/table/tbody/tr[3]/td[2]/pre/span[6364]