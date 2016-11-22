import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by lxhoang on 7/18/14.
 */
public class MainPageSteps {

    private WebDriver webDriver;
    private MainPage mainPage;
    private LoadingPage loadingPage;
    private String url;

    public MainPageSteps(WebDriver webDriver) {
        webDriver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
        webDriver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);

        this.webDriver = webDriver;

        mainPage = new MainPage(webDriver);
        loadingPage = new LoadingPage(webDriver);
    }

    /**
     * Open main page
     * @param url
     */
    public void isMainPageOpened(String url){
        this.url = url;
        mainPage.open_MainPage(url);
    }

    public int getCurrentClick(){
        return mainPage.getNumberClickToday();
    }

    public void clickImageAndClosePopup(int limitCountClick) throws InterruptedException {
        int numCountClick = mainPage.getNumberClickToday();
        Random random = new Random();
        // get window handle of main page before clicking image
        String mainPageWindowHandle = mainPage.getWindowHandle();
        String reducedURL = "[--" + url.substring(48) + "--]";

        while (numCountClick < limitCountClick){

            for(int i = numCountClick ; i < limitCountClick; i++){
//                System.out.println("Current click : " + numCountClick);
                System.out.println("Current click of URL " + reducedURL + ": " + i);


                // Click
                Thread.sleep(new Random().nextInt(2000) + 2000);

                loadingPage = mainPage.clickOnMainImage();
                loadingPage.confirmPopupAndCloseLoadingPage(numCountClick,mainPageWindowHandle);

                // Switch to main page window handle
                webDriver.switchTo().window(mainPageWindowHandle);

                // Sleep a while
//                Thread.sleep(random.nextInt(500) + 500);
            }

            // refresh page and get current click
            webDriver.navigate().refresh();
            if(mainPage.isAlertPresent(webDriver,2)){
                webDriver.switchTo().alert().accept();
            }
            numCountClick = mainPage.getNumberClickToday();

        }


    }


    /*public void clickRandomImageAndClosePopUpOfURL_v2(String baseURL, int limitCountClick){
        int numCountClick = mainPage.getNumberClickToday();
        int jumpI = 0;

        // get window handle of main page before clicking image
        String mainPageWindowHandle = mainPage.getWindowHandle();

        while(numCountClick <= limitCountClick){

            // Handle for not click out of limitation by decreasing jump I
            if((limitCountClick - numCountClick >= 20)){
                // Random jump I from 15 to 20
                Random random = new Random();
                jumpI  = random.nextInt(3) + 28;
            } else {
                jumpI = limitCountClick - numCountClick; // decrease jump I equal 'limit click - current click'
            }


            ArrayList<int[]> arrayListImage = new ArrayList<int[]>();

            // Create list random image which does not contains duplicated int[]
            for(int i = 0 ; i < jumpI; i++){
                boolean blIsDuplicated = false;
                int[] randomImage = generateRandomTableTRTD();

                if(arrayListImage.isEmpty()){
                    arrayListImage.add(randomImage);
                } else {
                    // Check duplicated or not
                    for(int[] ints : arrayListImage){
                        if(Arrays.equals(ints,randomImage)){
                            blIsDuplicated = true;
                        }
                    }

                    if(blIsDuplicated == false){
                        arrayListImage.add(randomImage);
                    } else {
                        i=i-1;
                    }

                }

            }

            // ======================= TIME FOR CLICKING ACTION ===================================
            for(int i = 0 ; i < jumpI ; i++){

                loadingPage = mainPage.clickImageBasedOnSpecified_Table_TR_TD(arrayListImage.get(i));
                loadingPage.confirmPopupAndCloseLoadingPage(numCountClick);

                // Switch to main page
                webDriver.switchTo().window(mainPageWindowHandle);

            }

            // Close other windows except main page
            for(String windowHandle : webDriver.getWindowHandles()){
                if(!windowHandle.equals(mainPageWindowHandle)){
                    webDriver.switchTo().window(windowHandle);

                    System.out.println("Alert is displayed: " + isAlertPresent(webDriver));
                    if(isAlertPresent(webDriver)){
                        webDriver.switchTo().alert().accept();
                    }
                    webDriver.close();
                }
                webDriver.switchTo().window(mainPageWindowHandle);
            }

            // Refresh main page and return current click count after chain of click
//            webDriver.navigate().refresh();
//            webDriver.navigate().to(baseURL);// navigate to URL instead of refreshing main page
            tryToRefreshMainPage(3);

            numCountClick = mainPage.getNumberClickToday();

            // Try to print current number, if number click = 60 -> quit loop
            System.out.println("current number click : " + numCountClick);
            if(numCountClick==limitCountClick){
                System.out.println("TOTAL CLICK IS THE SAME EXPECTATION....");
                break;
            }

        } // bracket of while

    }*/

    public void tryToRefreshMainPage(int times){
        webDriver.manage().timeouts().pageLoadTimeout(180, TimeUnit.SECONDS);
        webDriver.navigate().refresh();
        boolean isMainPageNotLoaded = mainPage.isMainPageNotLoaded();
        while (isMainPageNotLoaded == true && times > 0){
            System.out.println("Times of refreshing: " + times);
            webDriver.navigate().refresh();
            times--;
        }
    }



    /**
     * check if alert is present
     * @param webDriver
     * @return
     */
//    public boolean isAlertPresent(WebDriver webDriver, int timeOutSeconds){
//        try{
//            WebDriverWait wait = new WebDriverWait(webDriver,timeOutSeconds);
//            wait.until(ExpectedConditions.alertIsPresent());
//            return true;
//        } catch (Exception e){
//            return false;
//        }
//    }

    /**
     * Take snapshot and sendEmail
     */
    public void takeSnapShotWhenCompleteAndSendEmail(WebDriver webDriver, long executingTime, String elapseDateTime){
        sendEmail("xh.dhhv@gmail.com","lalala","xh.dhhv@gmail.com",executingTime,elapseDateTime);
    }

    public void takeSnapShotWhenCompleteAndSendEmail(String receivedMail, WebDriver webDriver, long executingTime, String elapseDateTime){
        sendEmail("xh.dhhv@gmail.com","lalala",receivedMail,executingTime,elapseDateTime);
    }

    /**
     * send mail via Gmail
     * @param fromMail
     * @param credentialPass
     * @param toMail
     *
     */
    public void sendEmail(String fromMail, String credentialPass, String toMail, long executingTime, String elapsedDateTime){
        final String username = fromMail;
        final String password = "hongminh0606"; // don't want to show out

        int numberClickCount = mainPage.getNumberClickToday();

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(toMail));

            // Get shortcut of URL
            String baseURL = webDriver.getCurrentUrl();
            String shortcutURL = baseURL.substring(baseURL.length()-4,baseURL.length());

            // Set mail subject
            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String currentDate = dateFormat.format(date);

            message.setSubject( "[" + shortcutURL + "] " + "SUCCESSFUL " +
                    numberClickCount + " clicks at date: " + currentDate);

			//Create message body part
			BodyPart messageBodyPart;
			Multipart multipart = new MimeMultipart();

			// Add image as attachment

             messageBodyPart = new MimeBodyPart();
             messageBodyPart.setContent("<h2> URL: " + webDriver.getCurrentUrl() + " clicked " + mainPage.getNumberClickToday() + " times </h2>"
                         + "\n" + elapsedDateTime + "\n"
                         + " \nExecuting time is: " + executingTime/1000/60 + " minutes " + executingTime/1000%60 + " seconds !"
                         + "<img src='cid:image_id'>", "text/html");
             multipart.addBodyPart(messageBodyPart);
             message.setContent(multipart);


            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com",username,password);
            transport.sendMessage(message,message.getAllRecipients());
            transport.close();

            System.out.println("Send Email Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
