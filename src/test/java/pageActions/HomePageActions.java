package pageActions;


import Utils.SeleniumUtil;
import Utils.WebDriverProvider;
import org.openqa.selenium.support.PageFactory;
import pageobjects.HomePageLocators;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

@Singleton
public class HomePageActions {
    @Inject
    private HomePageLocators homePageLocators;
    @Inject
    WebDriverProvider driverProvider;

    @PostConstruct
    public void setUp()  {
        PageFactory.initElements(driverProvider.getInstance(), homePageLocators);
    }

    public String getHomePageText() {
        System.out.println("HomePage Text is : " + SeleniumUtil.getValueByElement(driverProvider.getInstance(), homePageLocators.homePageUserName));
        SeleniumUtil.waitUntilVisibleByElementByTIme(driverProvider.getInstance(), homePageLocators.homePageUserName, 30);
        return homePageLocators.homePageUserName.getText();
    }

}