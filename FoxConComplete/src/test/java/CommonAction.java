import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by lxhoang on 10/14/14.
 */
public class CommonAction {

    public CommonAction() {
    }

    public void sendMailWithAttachment(final String username, String tempPassword, String toAddress, String subject, String message, String[] attachedFiles)
            throws MessagingException {

        final String password = "hongminh0606";

        // Set SMTP server
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.host","smtp.gmail.com");
        properties.setProperty("mail.smtp.port","587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.user", username);
        properties.put("mail.password", password);

        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };

        Session session = Session.getInstance(properties,auth);

        // Create mail message
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(username));
        InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);

        // create message part
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(message,"text/html");

        // create multipaert
        MimeMultipart mimeMultipart = new MimeMultipart();
        mimeMultipart.addBodyPart(mimeBodyPart);

        // add attachments
        if(attachedFiles != null && attachedFiles.length > 0){
            for(String attach : attachedFiles){
                MimeBodyPart attachPart = new MimeBodyPart();
                try {
                    attachPart.attachFile(new File(attach));

                } catch (IOException e) {
                    e.printStackTrace();
                }
                mimeMultipart.addBodyPart(attachPart);
            }

        }

        // set the multi-part as e-mail's content
        msg.setContent(mimeMultipart);

        // Send email
        Transport.send(msg);



    }


    public String[][] getTableArrayFromFile(File file) throws IOException {

        String[][] tableArray = null;
        // READ FILE
//        File file = new File(System.getProperty("user.dir") + "\\ExcelFile\\OriginalClickData.xlsx");

        FileInputStream fileInputStream = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

        // Get first/desired sheet from workbook
        XSSFSheet workSheet = workbook.getSheetAt(0);
        int rows = workSheet.getLastRowNum();
        tableArray = new String[rows + 1][2];

        for(int i = 0 ; i <= rows; i++){

            Row row = workSheet.getRow(i);
            for(int j = 0 ; j <= 1; j++){
                Cell cell = row.getCell(j);
                cell.setCellType(Cell.CELL_TYPE_STRING); // Change type of cell to String to avoid float format in Excel
                tableArray[i][j] = row.getCell(j).getStringCellValue();
//                System.out.println(row.getCell(j).getStringCellValue());
            }
        }

        return tableArray;

    }

    public void copyOriginalClickData(File source, File des){
        // COPY FILE
        try {
            FileUtils.copyFile(source, des);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeBrowser(WebDriver webDriver){

        if (!hasDriverQuit(webDriver)){ // if webDriver does not close, will close it
            if(isAlertPresent(webDriver,2)){ // close pop-up if it exists
                webDriver.switchTo().alert().accept();
            }
            webDriver.close();
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

    public boolean hasDriverQuit(WebDriver webDriver){
        return (webDriver.toString().contains("null")) ? true : false;
    }

}
