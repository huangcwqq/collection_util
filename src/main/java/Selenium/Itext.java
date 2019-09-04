package Selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

public class Itext {
  public static void main(String[] args) {

    WebDriver driver = new ChromeDriver();
    driver.get("http://47.75.248.43:8081/xwiki/bin/login/XWiki/XWikiLogin");
    driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

    //    driver.switchTo().frame("alibaba-login-box");
    //    driver.findElement(By.xpath("//*[@id=\"fm-login-id\"]")).sendKeys("huangcw886");
    //    driver.findElement(By.xpath("//*[@id=\"fm-login-password\"]")).sendKeys("wei135790");
    //    driver.findElement(By.xpath("//*[@id=\"fm-login-submit\"]")).click();

    driver.findElement(By.xpath("//*[@id=\"j_username\"]")).sendKeys("huangcw");
    driver.findElement(By.xpath("//*[@id=\"j_password\"]")).sendKeys("wei135790");
    driver.findElement(By.xpath("//*[@id=\"loginForm\"]/div[2]/div[2]/dl/dt[4]/input")).click();

    String title = driver.getTitle();
    System.out.printf(title);

    driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
    driver.findElement(By.className("btn btn-xs btn-default")).click();
    driver.findElement(By.xpath("//*[@id=\"document:xwiki:creekson.WebHome\"]/i")).click();
    driver.findElement(By.xpath("//*[@id=\"document:xwiki:creekson.用户模块.WebHome\"]/i]")).click();

//    WebElement input = driver.findElement(By.id("sqm_12"));
//    input.sendKeys(Keys.ENTER);
  }
}

