package StepDefinitions;

import Utils.ScenarioContext;
import Utils.SeleniumUtil;
import Utils.WebDriverProvider;
import com.google.common.util.concurrent.Uninterruptibles;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import pageActions.GooglePage;

import javax.inject.Inject;
import java.io.IOException;
import java.time.Duration;
public class GoogleSteps {
    @Inject
    private WebDriverProvider driverProvider;
    @Inject
    private ScenarioContext scenarioContext;
    @Inject
    GooglePage googlePage;

   @Given("I am on the google site")
    public void launchSite() {
        this.googlePage.goTo();
       scenarioContext.getScenario().attach(((TakesScreenshot) driverProvider.getInstance()).getScreenshotAs(OutputType.BYTES), "image/png", "screenshot");
        }
    @When("I enter {string} as a keyword")
    public void enterKeyword(String keyword) throws IOException, InterruptedException {
        this.googlePage.search(keyword);
       }

    @Then("I should see search results page")
    public void clickSearch() throws IOException, InterruptedException {
        Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(4));
        Assert.assertTrue(this.googlePage.isAt());
        SeleniumUtil.insertScreenshot(driverProvider.getInstance(), scenarioContext);
        //scenarioContext.getScenario().attach(((TakesScreenshot) driverProvider.getInstance()).getScreenshotAs(OutputType.BYTES), "image/png", "screenshot");
        //Allure.addAttachment("Screenshot", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
        }
    @Then("I should see at least {int} results")
    public void verifyResults(int count) throws InterruptedException, IOException {
        Assert.assertTrue(this.googlePage.getCount() >= count);
        SeleniumUtil.scrollToView(driverProvider.getInstance(), By.xpath("//div[contains(text(),'Images')]"));
        SeleniumUtil.clickElementbyXPath(driverProvider.getInstance(),"//div[contains(text(),'Images')]");
        Thread.sleep(3000);
        SeleniumUtil.scrollToView(driverProvider.getInstance(), By.xpath("//a[normalize-space()='Videos']"));
        SeleniumUtil.clickUsingJavaScript(driverProvider.getInstance(), By.xpath("//a[normalize-space()='Videos']"));
        SeleniumUtil.insertScreenshot(driverProvider.getInstance(), scenarioContext);
        Thread.sleep(3000);
    }
 }
