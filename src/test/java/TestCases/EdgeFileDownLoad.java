package TestCases;

import Utils.SeleniumUtil;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import javax.inject.Singleton;
import java.util.HashMap;

@Singleton
public class EdgeFileDownLoad {
    @Test
    public void EdgeTest(){
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.setHeadless(true);
        // chromeOptions.addArguments("--whitelist-ip *");
        edgeOptions.addArguments("--proxy-server='direct://'");
        edgeOptions.addArguments("--proxy-bypass-list=*");
        edgeOptions.addArguments("--ignore-certificate-errors");
        //edgeOptions.addArguments("--headless");
        edgeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);

        // to launch chrome in incognito mode
        edgeOptions.addArguments("start-maximized");
        edgeOptions.addArguments("-inprivate");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(ChromeOptions.CAPABILITY, edgeOptions);

        // to set default download path
        String downloadFilepath = System.getProperty("user.dir");
        HashMap<String, Object> edgePrefs = new HashMap<>();
        edgePrefs.put("profile.default_content_settings.popups", 0);
        edgePrefs.put("download.default_directory", downloadFilepath);
        edgeOptions.setExperimentalOption("prefs", edgePrefs);
        WebDriver driver = new EdgeDriver(edgeOptions);
        driver.get("http://www.seleniumhq.org/download/");
        SeleniumUtil.waitUntilClickByElement(driver, driver.findElement(By.linkText("32 bit Windows IE")));
        driver.findElement(By.linkText("32 bit Windows IE")).click();
        SeleniumUtil.waitByTime(5000);
        driver.close();
    }
}
