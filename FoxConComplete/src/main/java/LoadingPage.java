import com.google.common.base.Function;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.*;

import javax.xml.ws.WebEndpoint;
import java.awt.event.KeyEvent;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by macbookpro on 7/14/14.
 */
public class LoadingPage {

    private WebDriver webDriver;

    public LoadingPage(WebDriver webDriver){
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver,this);
    }

    public String getWindowHandle(){
        return webDriver.getWindowHandle();
    }

    public void confirmPopupAndCloseLoadingPage(int numClickCount, String mainPageWindowHandle){

        // Switch to Loading page handle
        for(String currentWindowHandle : webDriver.getWindowHandles()){
//            System.out.println("CURRENT WINDOWS HANDLE : " + currentWindowHandles);
            if(currentWindowHandle != mainPageWindowHandle){
                webDriver.switchTo().window(currentWindowHandle);
            }
        }

        long timeOutSeconds = 20; // default timeOut for waiting alert present 20 seconds
        Random random = new Random();
        int waitRandomTimeAfterPopUpPresent = random.nextInt(1000) + 2000;
        // at the first time, alert need wait about 2 minutes to display
        if(numClickCount == 0 ){
            timeOutSeconds = 60;
        }

        try {
            WebDriverWait wait = new WebDriverWait(webDriver,timeOutSeconds);
            wait.until(ExpectedConditions.alertIsPresent());

            // Sleep while before close pop-up
            Thread.sleep(random.nextInt(500) + 300);
            // Type enter to confirm pop-up
            webDriver.switchTo().alert().accept();
        } catch (Throwable e){
            System.out.println("ERROR ABOUT TIMEOUT" + e.getMessage());
        } finally {

            try {
                // Sleep after close popup
                Thread.sleep(waitRandomTimeAfterPopUpPresent);

                // always execute closing Loading page except main page
                if(isAlertPresent(webDriver,1)){
                    webDriver.switchTo().alert().accept();
                }
                webDriver.close();

//                Thread.sleep(waitRandomTimeAfterPopUpPresent);
            } catch (Exception abc) {
                System.out.println(abc.getMessage());
            }
        }

    }


    public boolean isAlertPresent(WebDriver webDriver, int timeOutSeconds){
        try{
            WebDriverWait wait = new WebDriverWait(webDriver,timeOutSeconds);
            wait.until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public void close (){
        webDriver.close();
    }





}
