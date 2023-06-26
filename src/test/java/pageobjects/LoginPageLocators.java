package pageobjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import javax.inject.Singleton;

@Singleton
public class LoginPageLocators {

    @FindBy(name = "username")
    public WebElement userName;

    @FindBy(name = "password")
    public WebElement password;

    @FindBy(id = "logInPanelHeading")
    public WebElement titleText;

    @FindBy(xpath = "//*[@id='app']/div[1]/div/div[1]/div/div[2]/div[2]/form/div[3]/button")
    public WebElement login;

    @FindBy(xpath = "//p[@class='oxd-text oxd-text--p oxd-alert-content-text']")
    public  WebElement errorMessage;

    @FindBy(xpath = "//*[@href='https://www.linkedin.com/company/orangehrm/mycompany/']")
    public  WebElement linkedInIcon;

    @FindBy(xpath = "//*[@href='https://www.facebook.com/OrangeHRM/']") //Invalid Xpath
    public  WebElement faceBookIcon;

    @FindBy(xpath = "//*[@id='app']/div[1]/div/div[1]/div/div[2]/div[2]/form/div[4]/p")
    public  WebElement ForgotYourPasswordLink;

}
