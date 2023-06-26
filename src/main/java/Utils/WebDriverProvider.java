package Utils;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Optional;


@Singleton
public class WebDriverProvider {
  private static final Logger LOGGER = LoggerFactory.getLogger(WebDriverProvider.class);
  private static final String USER_DIR = "user.dir";

  private WebDriver instance;
  private BrowserType instanceBrowserType;
  public enum BrowserType {
    CHROME("Google Chrome"),
    EDGE("EDGE"),
    FIRE_FOX("Firefox"),
    SAFARI("Safari");

    private String displayName;

    private BrowserType(String displayName) {
      this.displayName = displayName;
    }

    @Override
    public String toString() {
      return displayName;
    }
  }

  public WebDriver getInstance() {
    if (instance == null) {
      generateWebDriver(BrowserType.CHROME);
    }

    return instance;
  }

  public void generateWebDriver(BrowserType browserType) {
    generateWebDriver(browserType, null);
  }

  public void generateWebDriver(BrowserType browserType, final Boolean headless) {
    WebDriver driver = null;
    boolean isHeadless = Optional.ofNullable(headless).isPresent() ? headless : isHeadlessRun();
    BrowserType bt = getBrowserTypeUsingSystemVar();
    if (Optional.ofNullable(bt).isPresent()) {
      browserType = bt;
    }

    switch (browserType) {
      case CHROME:
        driver = generateChromeWebDriver(isHeadless);
        break;
      case EDGE:
        driver = generateEdgeDriver(isHeadless);
        break;
      case FIRE_FOX:
        driver = generateFirefoxDriver(isHeadless);
        break;
      case SAFARI:
        driver = generateSafariDriver();
        break;
      default:
        driver = generateChromeWebDriver(isHeadless);
        break;
    }

    instance = driver;
    instanceBrowserType = browserType;
  }

  private boolean isHeadlessRun() {
    return BooleanUtils.toBoolean(System.getProperty("headless"));
  }

  private BrowserType getBrowserTypeUsingSystemVar() {
    String browserValue = System.getProperty("browser");
    LOGGER.info("System property browser = " + browserValue);
    if (StringUtils.isNotBlank(browserValue)) {
      switch (browserValue) {
        case "chrome":
          return BrowserType.CHROME;
        case "firefox":
          return BrowserType.FIRE_FOX;
        case "edge":
          return BrowserType.EDGE;
        case "safari":
          return BrowserType.SAFARI;
        default:
          return null;
      }
    }

    return null;
  }

  private static WebDriver generateChromeWebDriver(boolean headless) {
    ChromeOptions chromeOptions = new ChromeOptions();
    chromeOptions.setHeadless(headless);
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
    if (BooleanUtils.toBoolean(System.getProperty("buildToolRun"))) {
      try {
        return new RemoteWebDriver(new URL(" http://localhost:4444/wd/hub"), chromeOptions);
      } catch (MalformedURLException e) {
        LOGGER.info("Given remote web driver url is wrong");
      }
    }
    return new ChromeDriver(chromeOptions);
  }

  private static WebDriver generateEdgeDriver(boolean headless) {
    EdgeOptions edgeOptions = new EdgeOptions();
    edgeOptions.setHeadless(headless);
    // chromeOptions.addArguments("--whitelist-ip *");
    edgeOptions.addArguments("--proxy-server='direct://'");
    edgeOptions.addArguments("--proxy-bypass-list=*");
    edgeOptions.addArguments("--ignore-certificate-errors");
    //edgeOptions.addArguments("--headless");
    edgeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
    edgeOptions.addArguments("--remote-allow-origins=*");
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
    if (BooleanUtils.toBoolean(System.getProperty("buildToolRun"))) {
      try {
        return new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), edgeOptions);
      } catch (MalformedURLException e) {
        LOGGER.info("Given remote web driver url is wrong");
      }
    }
    return new EdgeDriver(edgeOptions);
  }

  private static WebDriver generateFirefoxDriver(boolean headless) {
    String downloadFilepath = System.getProperty("user.dir");
    FirefoxProfile profile = new FirefoxProfile();
    profile.setAcceptUntrustedCertificates(true);
    profile.setAssumeUntrustedCertificateIssuer(false);
    profile.setPreference("browser.download.folderList", 2);
    profile.setPreference("browser.download.dir", downloadFilepath);
    profile.setPreference("browser.download.useDownloadDir", true);
    profile.setPreference("browser.download.viewableInternally.enabledTypes", "");
    profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/xlsx;application/msword;application/ms-doc;application/doc;application/pdf;text/plain;application/text;text/xml;application/xml");
    FirefoxOptions firefoxOptions = new FirefoxOptions();
    firefoxOptions.setProfile(profile);
    firefoxOptions.setHeadless(headless);
    firefoxOptions.addArguments("--disable-web-security");
    firefoxOptions.addArguments("--allow-running-insecure-content");
    firefoxOptions.addArguments("--whitelist-ip *");
    firefoxOptions.addArguments("--ignore-certificate-errors", "--ignore-ssl-errors");
    firefoxOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
    // to launch chrome in incognito mode
    firefoxOptions.addArguments("start-maximized");
    firefoxOptions.addArguments("--private");
    if (BooleanUtils.toBoolean(System.getProperty("buildToolRun"))) {
      try {
        return new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), firefoxOptions);
      } catch (MalformedURLException e) {
        LOGGER.info("Given remote web driver url is wrong");
      }
    }
    return new FirefoxDriver(firefoxOptions);
  }

  private static WebDriver generateSafariDriver() {
    System.setProperty("webdriver.safari.driver", System.getProperty(USER_DIR) + "/drivers/mac/safaridriver");
    return new SafariDriver();
  }

  public BrowserType getInstanceBrowserType() {
    // default it to Chrome
    BrowserType result = BrowserType.CHROME;
    if (instance != null) {
      result = instanceBrowserType;
    }
    return result;
  }
}
