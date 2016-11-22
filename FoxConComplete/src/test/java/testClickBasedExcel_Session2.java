import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.*;

import javax.mail.MessagingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by lxhoang on 10/13/14.
 */
public class testClickBasedExcel_Session2 {

    // private variables
    WebDriver webDriver;
    MainPage mainPage;
    MainPageSteps mainPageSteps;
    int numCountClick;
    File originalExcel, copiedExcel;
    CommonAction commonAction;
    ChromeOptions options;

    long startTime, endTime, executingTime;
    String startDateTime, endDateTime, currentDate;
    final DateFormat dateFormat = new SimpleDateFormat("M/dd/yyyy HH:mm:ss");
    final DateFormat dateFormatNotIncludeHour = new SimpleDateFormat("EEEE, MMMM-dd, yyyy");


    @BeforeClass
    public void beforeClass(){
        commonAction = new CommonAction();

        // Copy excel from original file
        originalExcel = new File(System.getProperty("user.dir") + "\\ExcelFile\\OriginalClickData_Session2.xlsx");
        copiedExcel = new File(System.getProperty("user.dir") + "\\ExcelFile\\CopyClickData_Session2.xlsx");
        commonAction.copyOriginalClickData(originalExcel,copiedExcel);

        startDateTime = dateFormat.format(new Date());
        currentDate = dateFormatNotIncludeHour.format(new Date());
        startTime = System.currentTimeMillis();
    }

    @BeforeMethod
    public void beforeMethod(){
        options = new ChromeOptions();
        options.addArguments("start-maximized");
        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/BrowserDrivers/chromedriver.exe");
        webDriver = new ChromeDriver(options);
//        webDriver = new FirefoxDriver();
        mainPageSteps = new MainPageSteps(webDriver);
        webDriver.manage().timeouts().pageLoadTimeout(2, TimeUnit.MINUTES);
        webDriver.manage().timeouts().implicitlyWait(60,TimeUnit.SECONDS);

    }

    @DataProvider
    public Object[][] provideData() throws IOException {
        Object[][] objects;
        objects = commonAction.getTableArrayFromFile(copiedExcel);
        return objects;
    }

    @AfterMethod
    public void afterMethod(){
        commonAction.closeBrowser(webDriver);
    }


    @Test(dataProvider = "provideData", invocationCount = 1)
    public void clickBaseOnExcel_S2(String baseUrl, String numberCountClick) throws InterruptedException {

        // STEP 1 : Navigate to site
        mainPageSteps.isMainPageOpened(baseUrl);

        // STEP 2: Click on random image and close popup in loading page
        mainPageSteps.clickImageAndClosePopup(Integer.parseInt(numberCountClick));

    }

    /*@Test(dependsOnMethods = "clickBaseOnExcel") // Verify and Run auto if actual click does not match with expected click
    public void checkActualClickOnExcelAndSendEmail() throws IOException, InterruptedException {

        // STEP 1: Write actual click to excel file

        FileInputStream fileInputStream = new FileInputStream(System.getProperty("user.dir") + "\\ExcelFile\\CopyClickData_Session2.xlsx");
        File file = new File(System.getProperty("user.dir") + "\\ExcelFile\\CopyClickData_Session2.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        fileInputStream.close();
        XSSFSheet workSheet = workbook.getSheetAt(0);

        Row row;
        Cell cell;
        int lastRow = workSheet.getLastRowNum();

        String[][] data = commonAction.getTableArrayFromFile(file);

        for(int i = 0 ; i < lastRow; i++){

            // TODO: will investigate case : navigate timeout here
//            System.out.println(data[i][0]);
            mainPageSteps.isMainPageOpened(data[i][0]);

            String strActualClick = mainPageSteps.getCurrentClick() + "";
            row = workSheet.getRow(i);
            cell = row.createCell(2);
            cell.setCellValue(strActualClick);
        }

        // Export to output
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        workbook.write(fileOutputStream);
        fileOutputStream.close();

        // STEP 2: Check actual click and expected click. Re-execute auto click if actual is less than expectation
        for(int i = 0; i <= lastRow; i++){
            row = workSheet.getRow(i);
            row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
            row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
            String url = row.getCell(0).getStringCellValue();

            int expectedClick = Integer.parseInt(row.getCell(1).getStringCellValue());
            int actualClick = Integer.parseInt(row.getCell(2).getStringCellValue());

            int count = 0;

            while(actualClick < expectedClick && count < 3){

                re_ExecuteClick(url, expectedClick + "");

                Thread.sleep(1000 * count * 60); // sleep minutes between re-execution avoid time out

                actualClick = mainPageSteps.getCurrentClick();
                System.out.println("Actual click after excel : " + actualClick);
                count++;

                // --- Writing updated actual click to excel file ---
                FileInputStream fileInputStream1 = new FileInputStream(System.getProperty("user.dir") + "\\ExcelFile\\CopyClickData_Session2.xlsx");
                File file1 = new File(System.getProperty("user.dir") + "\\ExcelFile\\CopyClickData_Session2.xlsx");
                XSSFWorkbook workbook1 = new XSSFWorkbook(fileInputStream1);
                fileInputStream1.close();
                XSSFSheet workSheet1 = workbook1.getSheetAt(0);

                Row row1;
                Cell cell1;

                row1 = workSheet1.getRow(i);
                cell1 = row1.createCell(2);
                cell1.setCellValue(actualClick);


                // Export to output
                FileOutputStream fileOutputStream1 = new FileOutputStream(file1);
                workbook1.write(fileOutputStream1);
                fileOutputStream1.close();

                // --- End Writing updated actual click to excel file ---

            } // bracket of while

        }

        // STEP 3: Send mail with attached excel file

        endTime = System.currentTimeMillis();
        executingTime = endTime - startTime;
        String executionTime = "\nExecuting time is: " + executingTime/1000/60 + " minutes " + executingTime/1000%60 + " seconds !";

        endDateTime = dateFormat.format(new Date());
        String elapsedDateTime = "\nExecuting date time from " + startDateTime + " to " + endDateTime;
        System.out.println("Elapse time : " + elapsedDateTime);

        String[] attachFiles = { copiedExcel.getAbsolutePath() };

        try {
            commonAction.sendMailWithAttachment("xh.dhhv@gmail.com","lalala","xh.dhhv@gmail.com",
                    "Successful Session 2 at " + currentDate , // subject
                    executionTime + elapsedDateTime , // message
                    attachFiles); // attached files

        } catch (MessagingException e) {
            e.printStackTrace();
        }


    }

    public void re_ExecuteClick(String baseUrl, String numberCountClick) throws InterruptedException {

        // STEP 1 : Navigate to site
        mainPageSteps.isMainPageOpened(baseUrl);

        // STEP 2: Click on random image and close popup in loading page
        mainPageSteps.clickImageAndClosePopup(Integer.parseInt(numberCountClick));

    }*/

}
