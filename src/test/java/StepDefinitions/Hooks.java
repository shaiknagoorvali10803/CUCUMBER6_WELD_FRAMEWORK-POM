package StepDefinitions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import Utils.*;
import io.cucumber.java8.En;
import io.cucumber.java8.Scenario;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;


public class Hooks implements En {
	@Inject
	private WebDriverProvider driverFactory;
	@Inject
	private ScenarioContext scenarioContext;
	@Inject
	VideoRecorder videoRecorder;
	public Hooks() throws IOException {
		Before("@Chrome and not (@Headless or @IE or @Safari or @Firefox or @Edge)", (final Scenario scenario) -> {
			if (!BooleanUtils.toBoolean(System.getProperty("buildToolRun"))) {
				videoRecorder.startRecording();
			}
			driverFactory.generateWebDriver(WebDriverProvider.BrowserType.CHROME);
			// initialize context
			scenarioContext.setScenario(scenario);
		});

		Before("(@Chrome and @Headless) and not (@IE or @Safari or @Firefox or @Edge)", (final Scenario scenario) -> {
			if (!BooleanUtils.toBoolean(System.getProperty("buildToolRun"))) {
				videoRecorder.startRecording();
			}
			driverFactory.generateWebDriver(WebDriverProvider.BrowserType.CHROME, true);
			// initialize context
			scenarioContext.setScenario(scenario);
		});

		Before("@Edge and not (@Chrome or @Safari or @Firefox)", (final Scenario scenario) -> {
			if (!BooleanUtils.toBoolean(System.getProperty("buildToolRun"))) {
				videoRecorder.startRecording();
			}
			driverFactory.generateWebDriver(WebDriverProvider.BrowserType.EDGE);
			// initialize context
			scenarioContext.setScenario(scenario);
		});

		Before("@Safari and not (@Chrome or @IE or @Firefox or @Edge)", (final Scenario scenario) -> {
			driverFactory.generateWebDriver(WebDriverProvider.BrowserType.SAFARI);
			// initialize context
			scenarioContext.setScenario(scenario);
		});

		Before("@Firefox and not (@Headless or @Chrome or @Safari or @IE or @Edge)", (final Scenario scenario) -> {
			driverFactory.generateWebDriver(WebDriverProvider.BrowserType.FIRE_FOX);
			// initialize context
			scenarioContext.setScenario(scenario);
		});

		Before("(@Firefox and @Headless) and not (@Chrome or @Safari or @IE or @Edge)", (final Scenario scenario) -> {
			driverFactory.generateWebDriver(WebDriverProvider.BrowserType.FIRE_FOX, true);
			// initialize context
			scenarioContext.setScenario(scenario);
		});

		Before("not (@Chrome or @IE or @Safari or @Firefox or @Edge)", (final Scenario scenario) -> {
			driverFactory.generateWebDriver(WebDriverProvider.BrowserType.CHROME);
			// initialize context
			scenarioContext.setScenario(scenario);
		});

		After((final Scenario scenario) -> {
			scenarioContext = new ScenarioContext();
			if (scenario.isFailed()) {
				try {
					captureScreenshot(scenario);
				} catch (ClassCastException | IOException e) {
					throw new LoggingException(e);
				}
			}
			driverFactory.getInstance().close();
			});
	}

	private String captureScreenshot(final Scenario scenario) throws IOException {
		final Date now = new Date();
		final String dateString = new SimpleDateFormat("dd-MMM-yyy").format(now);
		final String dateAndTimeString = new SimpleDateFormat("dd-MMM-yyyy hh-mm-ss a z").format(now);
		final TakesScreenshot screenShot = (TakesScreenshot) driverFactory.getInstance();
		final File source = screenShot.getScreenshotAs(OutputType.FILE);
		final String dest = System.getProperty("user.dir") + File.separator + "target" + File.separator + "cucumber-html-reports" + File.separator
				+ dateString + File.separator + "Error_" + dateAndTimeString + ".png";
		final File destination = new File(dest);
		FileUtils.copyFile(source, destination);
		//ExtentCucumberAdapter.addTestStepScreenCaptureFromPath(destination.getAbsolutePath());
		return dest;
	}

}
