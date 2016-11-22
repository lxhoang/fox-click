import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.awt.font.GlyphMetrics;
import java.nio.file.WatchEvent;

/**
 * Created by lxhoang on 12/15/14.
 */
public class GmailPageUnsigned extends BasePage {

    @FindBy(id="Email")
    private WebElement tf_username;

    @FindBy(id="Passwd")
    private WebElement tf_password;

    @FindBy(id="signIn")
    private WebElement btn_signIn;

    public GmailPageUnsigned(WebDriver webDriver) {
        super(webDriver);
        webDriver.get("http://gmail.com");
        if(!webDriver.getTitle().equals("Gmail")){
            throw new IllegalStateException("This is not unsigned gmail " + webDriver.getTitle());
        }
    }

    public GmailSignedIn signInWithSpecifiedAccount(){
        tf_username.sendKeys("xh.vietnam@gmail.com");
        tf_password.sendKeys("hongminh0606");
        btn_signIn.click();
        return new GmailSignedIn(webDriver);
    }


}
