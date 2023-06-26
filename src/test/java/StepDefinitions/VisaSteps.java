package StepDefinitions;

import Utils.ScenarioContext;
import Utils.SeleniumUtil;
import Utils.WebDriverProvider;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import pageActions.GooglePage;
import pageActions.VisaRegistrationPage;

import javax.inject.Inject;
import java.time.LocalDate;

public class VisaSteps {

    @Inject
    WebDriverProvider driverProvider;
    @Inject
    VisaRegistrationPage registrationPage;
    @Inject
    GooglePage googlePage;
    @Inject
    ScenarioContext scenarioContext;
    @Given("I am on VISA registration form")
    public void launchSite() {
       driverProvider.getInstance().navigate().to("https://vins-udemy.s3.amazonaws.com/sb/visa/udemy-visa.html");
        SeleniumUtil.insertScreenshot(driverProvider.getInstance(), scenarioContext);
       //scenarioContext.getScenario().attach(((TakesScreenshot)  driverProvider.getInstance()).getScreenshotAs(OutputType.BYTES), "image/png", "screenshot");
        //Allure.addAttachment("Screenshot", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
         }

    @When("I select my from country {string} and to country {string}")
    public void selectCountry(String from, String to) {
        registrationPage.setCountryFromAndTo(from, to);
    }

    @And("I enter my dob as {string}")
    public void enterDob(String dob) {
        registrationPage.setBirthDate(LocalDate.parse(dob));
    }

    @And("I enter my name as {string} and {string}")
    public void enterNames(String fn, String ln) {
        registrationPage.setNames(fn, ln);
    }

    @And("I enter my contact details as {string} and {string}")
    public void enterContactDetails(String email, String phone) {
        this.registrationPage.setContactDetails(email, phone);
    }

    @And("I enter the comment {string}")
    public void enterComment(String comment) {
        this.registrationPage.setComments(comment);
        SeleniumUtil.insertScreenshot(driverProvider.getInstance(), scenarioContext);
        //scenarioContext.getScenario().attach(((TakesScreenshot)  driverProvider.getInstance()).getScreenshotAs(OutputType.BYTES), "image/png", "screenshot");
    }

    @And("I submit the form")
    public void submit() {
        //Allure.addAttachment("Screenshot", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
        this.registrationPage.submit();
        System.out.println("hashcode scenario Context "+scenarioContext.getScenario().hashCode());
        SeleniumUtil.insertScreenshot(driverProvider.getInstance(), scenarioContext);
        //scenarioContext.getScenario().attach(((TakesScreenshot)  driverProvider.getInstance()).getScreenshotAs(OutputType.BYTES), "image/png", "screenshot");
        System.out.println("hashcode driver "+ driverProvider.getInstance().hashCode());
        }

    @Then("I should see get the confirmation number")
    public void verifyConfirmationNumber() throws InterruptedException {
        boolean isEmpty = StringUtils.isEmpty(this.registrationPage.getConfirmationNumber().trim());
        Assert.assertFalse(isEmpty);
        Thread.sleep(2000);
    }

   }
