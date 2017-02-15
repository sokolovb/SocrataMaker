package com.axibase.selenium;

/*
 * Created by boriss on 07.02.17.
 * parse some datasets from catalog.data.gov with some info via ATSD Collector
 */

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
/*
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
*/

import java.io.*;
import java.util.Arrays;
import java.util.Properties;

public class SocrataMaker {
    private static final int commandsNumber = 3;
    private static final int metacommandsNumber = 100;
    private static final int columnsNumber = 6;
    private static final int pagesNumber = 5;
    public static void main (String[] args) throws InterruptedException, IOException {
/*
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                "/home/user/Downloads/phantomjs-1.9.8-linux-x86_64/bin/phantomjs");
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS,
                new String[] {"--web-security=no", "--ignore-ssl-errors=yes"});
        WebDriver driver = new PhantomJSDriver(caps);
*/
        System.setProperty("webdriver.chrome.driver", "chromedriver");
        WebDriver driver = new ChromeDriver();

        String[] url = new String[pagesNumber*20];
        String[] caturl = new String[pagesNumber*20];
        WebElement el;
        int num = 0;
        for (int i = 2; i <= pagesNumber; i++) {
            driver.get("https://catalog.data.gov/dataset?res_format=JSON&_res_format_limit=0&page=" + i);
            for (int l = 1; l <= 20; l++) {
                try {
                    el = driver.findElement(By.xpath(
                            "//*[@id=\"content\"]/div[2]/div/section[1]/div[2]/ul/li["+l+"]/div/ul/li[3]/a"));
                    url[num] = el.getAttribute("href");
                    el = driver.findElement(By.xpath(
                            "//*[@id=\"content\"]/div[2]/div/section[1]/div[2]/ul/li["+l+"]/div/h3/a"));
                    caturl[num] = el.getAttribute("href");
                    num++;
                } catch (Exception ignored) {}
            }
        }

        //load properties
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

        for (int i = 0; i < url.length; i++) {

            WebElement link = driver.findElement(By.linkText("Jobs"));
            link.click();

            //if previuos socrata job failed
            try {
                Alert alert = driver.switchTo().alert();
                alert.accept();
            } catch (Exception ignored) {}

            link = driver.findElement(By.linkText("autosocrata"));
            link.click();
            link = driver.findElement(By.linkText("Create Configuration"));
            link.click();

            field = driver.findElement(By.name("path"));
            field.sendKeys(url[i]);

            link = driver.findElement(By.id("btnAdd"));
            link.click();

            if (!driver.findElement(By.id("name")).getAttribute("value").equals("")) {
                link = driver.findElement(By.id("btnTest"));
                link.click();

                //write in file
                field = driver.findElement(By.xpath("//*[@id=\"tblSummaryInfo\"]/tbody/tr[2]/td[2]"));
                String name = field.getText();
                File file = new File("reports/" + name + ".md");
                if (!file.exists()) {
                    file.createNewFile();
                }
                PrintWriter out = new PrintWriter(file.getAbsoluteFile());


                //[dataset]
                int l, j;
                String cat;
                int datasetlen = driver.findElements(By.xpath("//*[@id=\"tblSummaryInfo\"]/tbody/tr")).size();
                String data = "\n";
                WebElement temp;
                //contents
                for (l = 2; l <= datasetlen; l++) {
                    temp = driver.findElement(By.xpath("//*[@id=\"tblSummaryInfo\"]/tbody/tr[" + l + "]/td[1]"));
                    data += temp.getText();
                    data += " = ";
                    temp = driver.findElement(By.xpath("//*[@id=\"tblSummaryInfo\"]/tbody/tr[" + l + "]/td[2]"));
                    data += temp.getText();
                    data += "\n";
                }

                out.print("[dataset]\n```property" +
                        "\nURL = " + url[i] +
                        "\nCatalog URL = " + caturl[i] +
                        data + "```\n\n");


                //[columns]
                int rowsNumber = driver.findElements(By.xpath("//*[@id=\"tblColumnInfos\"]/tbody/tr")).size();
                String[][] cols = new String[rowsNumber][columnsNumber];
                //table head
                for (l = 0; l < columnsNumber; l++) {
                    temp = driver.findElement(By.xpath("//*[@id=\"tblColumnInfos\"]/tbody/tr[2]/th[" + (l + 1) + "]"));
                    cols[0][l] = temp.getText();
                }
                //contents
                for (j = 1; j < rowsNumber - 1; j++) {
                    for (l = 0; l < columnsNumber; l++) {
                        temp = driver.findElement(By.xpath(
                                "//*[@id=\"tblColumnInfos\"]/tbody/tr[" + (j + 2) + "]/td[" + (l + 1) + "]"));
                        cols[j][l] = temp.getText();
                    }
                }

                int[] offset = new int[columnsNumber];
                int max;
                out.print("[columns]\n```ls\n");
                for (int i1 = 0; i1 < columnsNumber; i1++) {
                    max = 0;
                    for (int i2 = 0; i2 < rowsNumber - 1; i2++) {
                        if (cols[i2][i1].length() > max) {
                            max = cols[i2][i1].length();
                        }
                    }
                    offset[i1] = max;
                }
                for (int i1 = 0; i1 < rowsNumber - 1; i1++) {
                    if (i1 == 1) {
                        out.print("| ");
                        for (int i2 = 0; i2 < columnsNumber; i2++) {
                            for (int i3 = 0; i3 < offset[i2]; i3++) {
                                out.print("=");
                            }
                            out.print(" | ");
                        }
                        out.print("\n");
                    }
                    out.print("| ");
                    for (int i2 = 0; i2 < columnsNumber; i2++) {
                        out.printf("%-" + offset[i2] + "s | ", cols[i1][i2]);
                    }
                    out.print("\n");
                }
                out.print("```\n");

                //[time]
                field = driver.findElement(By.id("timeField_0"));
                String time = field.getAttribute("value");

                field = driver.findElement(By.id("timeFormat_0"));
                String format = field.getAttribute("value");

                out.print("\n[time]\n```ls" +
                        "\nValue = " + time +
                        "\nFormat & Zone = " + format + "\n```\n");

                //[series]
                field = driver.findElement(By.id("metricPrefix_0"));
                String prefix = field.getAttribute("value");

                field = driver.findElement(By.id("includedFields_0"));
                String included = field.getAttribute("value");

                field = driver.findElement(By.id("excludedFields_0"));
                String excluded = field.getAttribute("value");

                field = driver.findElement(By.id("annotationFields_0"));
                String annotation = field.getAttribute("value");

                out.print("\n[series]\n```ls" +
                        "\nMetric Prefix = " + prefix +
                        "\nIncluded Fields = " + included +
                        "\nExcluded Fields = " + excluded +
                        "\nAnnotation Fields = " + annotation + "\n```\n");

                //[commands]
                int count = 0;
                j = 1;
                String[] commands = new String[commandsNumber];
                Arrays.fill(commands, "");
                do {
                    temp = driver.findElement(By.xpath(
                            "//*[@id=\"testRow_2\"]/td/table/tbody/tr[3]/td[2]/pre/span[" + j + "]"));
                    cat = temp.getText();

                    if ((temp.getAttribute("class").equals("cm-keyword")) && (temp.getText().equals("series"))) {
                        cat = "\n" + cat;
                        count++;
                    }
                    if ((temp.getAttribute("class").equals("cm-keyword")) ||
                            temp.getAttribute("class").equals("cm-attribute")) {
                        cat += " ";
                    }
                    j++;
                    if (count <= commandsNumber) {
                        commands[count - 1] += cat;
                    }
                } while (count <= commandsNumber);

                out.print("\n[commands]\n```ls");
                for (int k = 0; k < commandsNumber; k++) {
                    out.print(commands[k] + "\n");
                }
                out.print("```\n");

                //[meta-commands]
                boolean cond = true, isNotSeries = false;
                j = 1;
                count = 0;
                String[] metacommands = new String[metacommandsNumber];
                Arrays.fill(metacommands, "");
                while (cond) {
                    try {
                        temp = driver.findElement(By.xpath(
                                "//*[@id=\"testRow_2\"]/td/table/tbody/tr[3]/td[2]/pre/span[" + j + "]"));
                        cat = temp.getText();
                        if ((temp.getAttribute("class").equals("cm-keyword")) && (!temp.getText().equals("series"))) {
                            isNotSeries = true;
                            cat = "\n" + cat;
                            count++;
                        }
                        if ((temp.getAttribute("class").equals("cm-keyword")) && (temp.getText().equals("series"))) {
                            isNotSeries = false;
                        }
                        if ((temp.getAttribute("class").equals("cm-keyword")) ||
                                temp.getAttribute("class").equals("cm-attribute")) {
                            cat += " ";
                        }
                        if (isNotSeries) {
                            metacommands[count - 1] += cat;
                        }
                        j++;
                    } catch (Exception e) {
                        cond = false;
                    }
                }

                out.print("\n[meta-commands]\n```ls");
                for (int k = 0; k < count; k++) {
                    out.print(metacommands[k] + "\n");
                }
                out.print("```\n");
                out.close();

            }
        }
        driver.quit();
    }
}
