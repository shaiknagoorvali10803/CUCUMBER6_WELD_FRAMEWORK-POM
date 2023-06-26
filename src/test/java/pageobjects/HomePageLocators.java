package pageobjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import javax.inject.Singleton;

@Singleton
public class HomePageLocators {

    @FindBy(xpath = "//h6[contains(@class,'oxd-topbar-header-breadcrumb-module')]")
    public  WebElement homePageUserName;

}
