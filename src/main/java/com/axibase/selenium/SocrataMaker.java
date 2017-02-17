package com.axibase.selenium;

/*
 * Created by boriss on 07.02.17.
 * parse some datasets from catalog.data.gov with some info via ATSD Collector
 */

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
/*
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
*/

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SocrataMaker {
    private static final int commandsNumber = 3;
    private static final int metacommandsNumber = 100;
    private static final int columnsNumber = 6;

    private static File file = new File("SocrataMaker.log");

    public static void log (String msg) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd_HH:mm:ss").format(Calendar.getInstance().getTime());
            FileUtils.writeStringToFile(file, timeStamp + ": " + msg + "\n", true);
        } catch (Exception ignored) {}
    }

    public static void logRefresh () {
        try {
            PrintWriter out = new PrintWriter(file.getAbsoluteFile());
            out.print("");
        } catch (Exception ignored) {}
    }

    public static void main (String[] args) throws InterruptedException, IOException {
/*
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                "/home/user/Downloads/phantomjs-1.9.8-linux-x86_64/bin/phantomjs");
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS,
                new String[] {"--web-security=no", "--ignore-ssl-errors=yes"});
        WebDriver driver = new PhantomJSDriver(caps);
*/
        logRefresh();

        log("Initializing...");

        System.setProperty("webdriver.chrome.driver", "chromedriver");
        WebDriver driver = new ChromeDriver();

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
        String[] url = pr.getProperty("url").split(";");
        String[] caturl = new String[url.length];
        Arrays.fill(caturl, "");

        //find url in catalog.data.gov
        String[] pair = pr.getProperty("urlBase").split(";");
        String[] urlBase = new String[pair.length];
        String[] caturlBase = new String[pair.length];
        for (int i = 0; i < pair.length; i++) {
            String[] split = pair[i].split(",");
            urlBase[i] = split[0];
            caturlBase[i] = split[1];
        }

        Pattern pattern = Pattern.compile(".*(?=/)");

        log("Start parsing catalog url...");

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

        log("parsing finished");
        log("entering collector...");

        //authentication
        driver.get(collector);
        WebElement field = driver.findElement(By.id("username"));
        field.sendKeys(username);
        field = driver.findElement(By.name("password"));
        field.sendKeys(password);
        field.submit();

        log("success");

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

            log("adding new Socrata job...");

            field = driver.findElement(By.name("path"));
            field.sendKeys(url[i]);

            link = driver.findElement(By.id("btnAdd"));
            link.click();

            if (!driver.findElement(By.id("name")).getAttribute("value").equals("")) {
                link = driver.findElement(By.id("btnTest"));
                link.click();

                field = driver.findElement(By.xpath("//*[@id=\"tblSummaryInfo\"]/tbody/tr[2]/td[2]"));
                String name = field.getText();

                log("Socrata job for entity " + name + " added");

                log("parsing dataset...");

                //[dataset]
                int l, j;
                String cat;
                int datasetlen = driver.findElements(By.xpath("//*[@id=\"tblSummaryInfo\"]/tbody/tr")).size();
                String data = "\n", desc = "";
                WebElement temp;
                //contents
                for (l = 2; l <= datasetlen; l++) {
                    temp = driver.findElement(By.xpath("//*[@id=\"tblSummaryInfo\"]/tbody/tr[" + l + "]/td[1]"));
                    if (!temp.getText().equals("Description")) {
                        data += temp.getText();
                        data += " = ";
                        temp = driver.findElement(By.xpath("//*[@id=\"tblSummaryInfo\"]/tbody/tr[" + l + "]/td[2]"));
                        data += temp.getText();
                        data += "\n";
                    } else {
                        temp = driver.findElement(By.xpath("//*[@id=\"tblSummaryInfo\"]/tbody/tr[" + l + "]/td[2]"));
                        temp.click();
                        temp = driver.findElement(By.xpath(
                                "//*[@id=\"tblSummaryInfo\"]/tbody/tr[" + l + "]/td[2]/div/div[2]/div"));
                        desc = temp.getText();
                    }
                }

                log("parsing columns...");

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

                log("parsing time...");

                //[time]
                field = driver.findElement(By.id("timeField_0"));
                String time = field.getAttribute("value");

                field = driver.findElement(By.id("timeFormat_0"));
                String format = field.getAttribute("value");

                log("parsing series");

                //[series]
                field = driver.findElement(By.id("metricPrefix_0"));
                String prefix = field.getAttribute("value");

                field = driver.findElement(By.id("includedFields_0"));
                String included = field.getAttribute("value");

                field = driver.findElement(By.id("excludedFields_0"));
                String excluded = field.getAttribute("value");

                field = driver.findElement(By.id("annotationFields_0"));
                String annotation = field.getAttribute("value");

                log("parsing commands...");

                //[commands]
                int count = 0;
                j = 1;
                String atrClass;
                String[] commands = new String[commandsNumber];
                Arrays.fill(commands, "");
                do {
                    temp = driver.findElement(By.xpath(
                            "//*[@id=\"testRow_2\"]/td/table/tbody/tr[3]/td[2]/pre/span[" + j + "]"));
                    cat = temp.getText();
                    atrClass = temp.getAttribute("class");

                    if (atrClass.equals("cm-keyword")) {
                        if (cat.equals("series")) {
                            cat = "\n" + cat;
                            count++;
                        } else {
                            break;
                        }
                    }
                    if ((atrClass.equals("cm-keyword")) || atrClass.equals("cm-attribute")) {
                        cat += " ";
                    }
                    j++;
                    if (count <= commandsNumber) {
                        commands[count - 1] += cat;
                    }
                } while (count <= commandsNumber);

                log("parsing meta-commands...");

                //[meta-commands]
                count = 0;
                String[] metacommands = new String[metacommandsNumber];
                Arrays.fill(metacommands, "");
                String tempString = "";
                for (j = driver.findElements(By.xpath(
                        "//*[@id=\"testRow_2\"]/td/table/tbody/tr[3]/td[2]/pre/span")).size(); j > 0; j--) {
                    temp = driver.findElement(By.xpath(
                            "//*[@id=\"testRow_2\"]/td/table/tbody/tr[3]/td[2]/pre/span[" + j + "]"));
                    cat = temp.getText();
                    atrClass = temp.getAttribute("class");

                    if ((atrClass.equals("cm-keyword")) || atrClass.equals("cm-attribute")) {
                        cat += " ";
                    }
                    if (atrClass.equals("cm-keyword")) {
                        cat = "\n" + cat;
                        if (!cat.equals("\nseries ")) {
                            if (count < metacommandsNumber) {
                                metacommands[count] = cat + tempString;
                            } else  {
                                break;
                            }
                            tempString = "";
                            count++;
                        } else {
                            break;
                        }
                    } else {
                        tempString = cat + tempString;
                    }
                }
                int mcN = count;

                log("writing in file...");

                //write in file
                File file = new File("reports/" + name + ".md");
                int[] offset = new int[columnsNumber];
                int max;
                try {
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    PrintWriter out = new PrintWriter(file.getAbsoluteFile());
                    try {
                        out.print("[dataset]\n```property" +
                                "\nURL = " + url[i] +
                                "\nCatalog URL = " + caturl[i] +
                                data + "```\n\n");
                        out.print("[description]\n```ls\n" + desc + "\n```\n\n");
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

                        out.print("\n[time]\n```ls" +
                                "\nValue = " + time +
                                "\nFormat & Zone = " + format + "\n```\n");
                        out.print("\n[series]\n```ls" +
                                "\nMetric Prefix = " + prefix +
                                "\nIncluded Fields = " + included +
                                "\nExcluded Fields = " + excluded +
                                "\nAnnotation Fields = " + annotation + "\n```\n");
                        out.print("\n[commands]\n```ls");
                        for (int k = 0; k < commands.length; k++) {
                            out.print(commands[k].equals("") ? "" : commands[k] + "\n");
                        }
                        out.print("\n```\n");
                        out.print("\n[meta-commands]\n```ls");
                        for (int k = mcN-1; k >= 0; k--) {
                            out.print(metacommands[k].equals("") ? "" : metacommands[k] + "\n");
                        }
                        out.print("\n```");
                    } finally {
                        out.close();
                    }
                } catch (IOException e) {
                    throw new RuntimeException();
                }
                log("finished!");
            }
        }
        driver.quit();
    }
}
