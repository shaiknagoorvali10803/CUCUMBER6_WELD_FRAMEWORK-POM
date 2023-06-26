package StepDefinitions;

import Utils.ScenarioContext;
import Utils.WebDriverProvider;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import pageActions.ForgotPasswordActions;
import pageActions.HomePageActions;
import pageActions.LoginPageActions;

import javax.inject.Inject;
import java.io.IOException;

public class LoginPageDefinitions {
    @Inject
    private WebDriverProvider driverProvider;
    @Inject
    private LoginPageActions objLogin;
    @Inject
    private HomePageActions objHomePage;
    @Inject
    private ForgotPasswordActions objForgotPasswordPage;
    @Inject
    private ScenarioContext scenario;

    @Given("User is on HRMLogin page {string}")
    public void loginTest(String url) throws InterruptedException {
        driverProvider.getInstance().get(url);
        driverProvider.getInstance().manage().timeouts().getPageLoadTimeout();
        Thread.sleep(6000);
    }

    @When("User enters username as {string} and password as {string}")
    public void goToHomePage(String userName, String passWord) throws IOException {
        objLogin.login(userName, passWord);
    }

    @Then("User should be able to login sucessfully and new page open")
    public void verifyLogin() throws IOException, InterruptedException {
        System.out.println("HomePage Text is : " + objHomePage.getHomePageText());
        Assert.assertTrue(objHomePage.getHomePageText().contains("Dashboard"));
        scenario.getScenario().attach(((TakesScreenshot) driverProvider.getInstance()).getScreenshotAs(OutputType.BYTES), "image/png", "screenshot");

    }

    @Then("User should be able to see error message {string}")
    public void verifyErrorMessage(String expectedErrorMessage) throws IOException {
        Assert.assertEquals(objLogin.getErrorMessage(), expectedErrorMessage);
        scenario.getScenario().attach(((TakesScreenshot) driverProvider.getInstance()).getScreenshotAs(OutputType.BYTES), "image/png", "screenshot");

    }

    @Then("User should be able to see LinkedIn Icon")
    public void verifyLinkedInIcon() {
        Assert.assertTrue(objLogin.getLinkedInIcon());
    }

    @Then("User should be able to see FaceBook Icon")
    public void verifyFaceBookIcon() {
        Assert.assertTrue(objLogin.getFaceBookIcon());
    }

    @When("User clicks on Forgot your Password Link")
    public void goToForgotYourPasswordPage() throws IOException {
        objLogin.clickOnForgetYourPasswordLink();

    }

    @Then("User should navigate to a new page")
    public void verfiyForgetYourPasswordPage() throws IOException {
        Assert.assertEquals(objForgotPasswordPage.getForgotPasswordPageText(), "Reset Password");
    }

}