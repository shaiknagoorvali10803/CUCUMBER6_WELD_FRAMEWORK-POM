package pageActions;

import Utils.SeleniumUtil;
import Utils.WebDriverProvider;
import org.openqa.selenium.support.PageFactory;
import pageobjects.ForgotPasswordLocators;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
@Singleton
public class ForgotPasswordActions {
    @Inject
   private ForgotPasswordLocators forgotPasswordLocators;
    @Inject
    WebDriverProvider driverProvider;
    @PostConstruct
    private void setup()  {
      PageFactory.initElements(driverProvider.getInstance(),forgotPasswordLocators);
    }
    public String getForgotPasswordPageText() {
        return SeleniumUtil.getValueByElement(driverProvider.getInstance(),  forgotPasswordLocators.ForgotPasswordHeading);
                //forgotPasswordLocators.ForgotPasswordHeading.getText();
    }
}