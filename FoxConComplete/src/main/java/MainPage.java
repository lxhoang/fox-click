import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;

import javax.mail.internet.*;

import java.util.concurrent.TimeUnit;

/**
 * Created by macbookpro on 7/13/14.
 */
public class MainPage {

    @FindBy(xpath = "//ul[@id='toplink']/li[3]/a/span")
    private WebElement numberClickOnDay;

    /*@FindBy(xpath = "//a[@class='fancybox-item fancybox-close']")
    private WebElement buttonCloseImage;*/

//    @FindBy(xpath = "//div[@id='clickbox']/a/img")
//    private WebElement imageClick;

    @FindBy(xpath = "//div[@class='main-content']/div[2]/a/img")
    private WebElement imageClick;

    WebDriver webDriver;

    public MainPage(WebDriver webDriver){
        this.webDriver = webDriver;
        webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS); // last is 15
        webDriver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS); //last is 40
//        webDriver.manage().window().maximize();
        PageFactory.initElements(webDriver, this);
    }

    public void open_MainPage(String url){
        webDriver.get(url);
        if(isAlertPresent(webDriver,3)){
            webDriver.switchTo().alert().accept();
        }

    }

    public LoadingPage clickOnMainImage() throws InterruptedException {

        // --- Handle clicking main image while network is disconnected
//        // Handle by clicking Lien He Chung TOi
//        webDriver.findElement(By.xpath("//div[@class='top-menu container']/ul/li[2]/a")).click();
//        if(isAlertPresent(webDriver,3)){
//        webDriver.switchTo().alert().accept();
//        }

        Thread.sleep(new Random().nextInt(500) + 2000);

//        webDriver.findElement(By.xpath("//div[@id='clickbox']/a/img")).click();
        /*int refreshTimes = 1;
        while (isMainPageNotLoaded() && refreshTimes < 3){
            webDriver.navigate().to(webDriver.getCurrentUrl());
            if(isAlertPresent(webDriver,3)){
                webDriver.switchTo().alert().accept();
            }

            Thread.sleep(1000 * 30 * refreshTimes);
            System.out.println("REFRESH PAGE " + refreshTimes + " TIMES");
            refreshTimes++;
        }*/

        webDriver.findElement(By.xpath("//div[@id='clickbox']/a/img")).click();

        if(isAlertPresent(webDriver,2)){
            webDriver.switchTo().alert().accept();
        }
        // --- End Handle clicking main image while network is disconnected

        // Store the current window handle
        String windowHandleBefore = webDriver.getWindowHandle();

        // Switch to new window opened
        for(String windowHandle : webDriver.getWindowHandles()){
            if(!windowHandle.equals(windowHandleBefore)){
                webDriver.switchTo().window(windowHandle);
            }
        }
        return new LoadingPage(webDriver);
    }


    public int getNumberClickToday(){
        String str = numberClickOnDay.getText();

        str = str.substring(1,str.length()-1);
        int num = Integer.parseInt(str);

        return num;
    }

    public String getWindowHandle(){
        return webDriver.getWindowHandle();
    }

    public boolean isMainPageNotLoaded(){
//        return webDriver.findElements(By.xpath("//span[@id='msg_click']")).isEmpty();
        return webDriver.findElements(By.xpath("//div[@id='clickbox']/a/img")).isEmpty();

    }

    /**
     *
     * @param webDriver
     * @param timeOutSeconds
     * @return
     */
    public boolean isAlertPresent(WebDriver webDriver, int timeOutSeconds){
        try{
            WebDriverWait wait = new WebDriverWait(webDriver,timeOutSeconds);
            wait.until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (Exception e){
            return false;
        }
    }






}
