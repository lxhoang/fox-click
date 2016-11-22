import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by lxhoang on 12/15/14.
 */
public class GmailSignedIn extends BasePage {

//    @FindBy(css = "div.z0 div")
//    private WebElement btn_compose;

    @FindBy(css="div.nM div[role='button']")
    private WebElement btn_compose;

    @FindBy(name = "to")
    private WebElement txt_to;

    @FindBy(name = "subjectbox")
    private WebElement txt_subject;

    @FindBy(css=".aDh div[role='button']")
    private WebElement btn_Send;

    @FindBy(css="td.Ap  div[role='textbox']")
    private WebElement messageBody;

    public GmailSignedIn(WebDriver webDriver) {
        super(webDriver);
    }

    public GmailSignedIn(WebDriver webDriver, String title) {
        super(webDriver);
//        if(!webDriver.getTitle().contains("xh.vietnam")){
//            throw new IllegalStateException("This is not signed gmail " + webDriver.getTitle());
//        }

    }

    public void sendMail(String content, String currentDate){
        WebDriverWait wait = new WebDriverWait(webDriver, 20);
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(btn_compose));
        btn_compose.click();
//        ;ducthanh@live.co.uk
        txt_to.sendKeys("xh.dhhv@gmail.com;ducthanh@live.co.uk");
        txt_subject.sendKeys("SUCCESSFUL " + currentDate);
        messageBody.sendKeys(content);
        btn_Send.click();

    }


}
