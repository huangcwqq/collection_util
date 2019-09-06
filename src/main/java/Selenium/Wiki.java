package Selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class Wiki {
    public static void main(String[] args){
        WebDriver driver = new ChromeDriver();
    driver.get("http://47.75.248.43:8081/xwiki/bin/login/XWiki/XWikiLogin");
    driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

    driver.findElement(By.xpath("//*[@id=\"j_username\"]")).sendKeys("huangcw");
    driver.findElement(By.xpath("//*[@id=\"j_password\"]")).sendKeys("wei135790");
    driver.findElement(By.xpath("//*[@id=\"loginForm\"]/div[2]/div[2]/dl/dt[4]/input")).click();

    String title = driver.getTitle();
    System.out.printf(title);

        WebDriverWait wait = new WebDriverWait(driver,10,1);

    driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
    System.out.println(driver.findElement(By.xpath("//*[@id=\"step-0\"]")).getText());
    System.out.println(driver.findElement(By.xpath("//*[@id=\"step-0\"]/div[2]")).getText());
    System.out.println(driver.findElement(By.xpath("//*[@id=\"step-0\"]/div[3]")).getText());
    driver.findElement(By.xpath("//*[@id=\"step-0\"]/button[1]")).click();
//    wait.until(
//            new ExpectedCondition<WebElement>() {
//              @Override
//              public WebElement apply(WebDriver text) {
//                return text.findElement(By.xpath("//*[@id=\"step-0\"]/button[1]"));
//              }
//            }).click();
    driver.findElement(By.xpath("//*[@id=\"document:xwiki:creekson.WebHome\"]/i")).click();
    driver.findElement(By.xpath("//*[@id=\"document:xwiki:creekson.用户模块.WebHome\"]/i]")).click();

    }

}
