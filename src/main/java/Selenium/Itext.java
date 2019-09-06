package Selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class Itext {
  public static void main(String[] args) {



//    WebDriver driver = new ChromeDriver();
//    driver.get("https://mail.aliyun.com/alimail");
//    driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
//
//    driver.switchTo().frame("alibaba-login-box");
//    driver.findElement(By.xpath("//*[@id=\"fm-login-id\"]")).sendKeys("huangcw886");
//    driver.findElement(By.xpath("//*[@id=\"fm-login-password\"]")).sendKeys("wei135790");
//    driver.findElement(By.xpath("//*[@id=\"fm-login-submit\"]")).click();
//
//    WebElement input = driver.findElement(By.id("sqm_12"));
//    input.sendKeys(Keys.ENTER);

    WebDriver driver = new ChromeDriver();
    driver.get("https://www.runoob.com/html/html-lists.html");
    driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

    driver.findElement(By.xpath("//*[@id=\"leftcolumn\"]/a[15]")).click();


    WebDriverWait wait = new WebDriverWait(driver,10,1);

    wait.until(new ExpectedCondition<WebElement>(){
      @Override
      public WebElement apply(WebDriver text) {
        return text.findElement(By.xpath("/html/body/div[3]/div/div[2]/div/div[2]/div[2]/a[1]"));
      }
    }).click();

    wait.until(new ExpectedCondition<WebElement>(){
      @Override
      public WebElement apply(WebDriver text) {
        return text.findElement(By.xpath("/html/body/div[3]/div/div[2]/div/div[4]/div[2]/a[2]"));
      }
    }).click();
    WebElement element = driver.findElement(By.xpath("//*[@id=\"content\"]/p[3]/a"));
    element.click();
    System.out.println(element.getText());
  }
}

