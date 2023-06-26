package TestCases;

import Utils.SeleniumUtil;
import Utils.WebDriverProvider;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;

@Singleton
public class ChromeFileDownLoad {

    @Inject
    WebDriverProvider driverProvider;
    @Test
    public void ChromeTest(){
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setHeadless(false);
        chromeOptions.addArguments("--proxy-server='direct://'");
        chromeOptions.addArguments("--proxy-bypass-list=*");
        chromeOptions.addArguments("--ignore-certificate-errors");
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);

        // to launch chrome in incognito mode
        chromeOptions.addArguments("start-maximized");
        chromeOptions.addArguments("--incognito");
        chromeOptions.addArguments("--remote-allow-origins=*");
        chromeOptions.addArguments("--disable-notifications");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);

        // to set default download path
        String downloadFilepath = System.getProperty("user.dir");
        HashMap<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.default_directory", downloadFilepath);
        chromePrefs.put("safebrowsing_for_trusted_sources_enabled", false);
        chromePrefs.put("safebrowsing.enabled", false);
        chromeOptions.setExperimentalOption("prefs", chromePrefs);

        WebDriver driver = new ChromeDriver(chromeOptions);
        driver.get("http://www.seleniumhq.org/download/");
        SeleniumUtil.waitUntilClickByElement(driver, driver.findElement(By.linkText("32 bit Windows IE")));
        driver.findElement(By.linkText("32 bit Windows IE")).click();
        SeleniumUtil.waitByTime(5000);
        driver.close();
    }
}
