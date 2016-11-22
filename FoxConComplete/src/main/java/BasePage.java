import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by lxhoang on 12/15/14.
 */
public class BasePage {

    WebDriver webDriver;

    public BasePage(WebDriver webDriver) {
        this.webDriver = webDriver;
        webDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        PageFactory.initElements(webDriver, this);
    }

    public String getContentFromExcelFile(File file) throws IOException {
        String result = "|                              URL                                           |     Expected     |    Actual    |\n\n";
        // STEP 1: Write actual click to excel file
        FileInputStream fileInputStream = new FileInputStream(file);

        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        fileInputStream.close();
        XSSFSheet workSheet = workbook.getSheetAt(0);

        Row row = null;
        Cell cell = null;
        int lastRow = workSheet.getLastRowNum();
//        System.out.println("Last row is "  + lastRow);


        for(int i = 0; i <= lastRow; i++){
            row = workSheet.getRow(i);
            for(int j=0; j <= 2; j++){
                cell = row.getCell(j);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                result += cell.getStringCellValue() + " ";
            }
            result += "\n";
        }

        return result;
    }

}
