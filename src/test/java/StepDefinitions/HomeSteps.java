package StepDefinitions;

import Utils.WebDriverProvider;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import pageActions.HomePage;

import javax.inject.Inject;


public class HomeSteps {
    @Inject
    private WebDriverProvider driverProvider;
    @Inject
    HomePage homePage;
    protected WebDriver driver;

    @Given("I am Google Page")
    public void launchSite() {
        homePage.goTo();
         }

    @When("Search for the Word {string}")
    public void enterKeyword(String keyword) {
        this.homePage.search(keyword);
    }

}
