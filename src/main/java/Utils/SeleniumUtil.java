
package Utils;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import io.cucumber.java8.Scenario;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;

import javax.imageio.ImageIO;

import static org.junit.Assert.fail;


public class SeleniumUtil {

    private static final String SPINNER_XPATH = "//app-block-ui/div/p-blockui";
    public static final String ERROR_MSG = "Some error has occurred while performing operation::{}";
    public static final String IS_ENTERED = " is entered";
    public static final String AND_PASSWORD = " and Password: ";
    public static final String USERNAME = "Username: ";
    private static final Logger LOGGER = LoggerFactory.getLogger(SeleniumUtil.class);
    public static final int DRIVER_WAIT_TIME_IN_SECS = 30;
    private static final String DROPDOWN_ITEM_SELECTOR_IN_OVERLAY = "//ul/li[*]/span[text()='%s']";
    private static final String DROPDOWN_PARTIAL_MATCH_ITEM_SELECTOR_IN_OVERLAY = "//ul/li[*]/span[contains(text(),'%s')]";
    private static final String DROPDOWN_OVERLAY = "//ul";
    private static final String DROPDOWN_CLICKABLE_LABEL = "div/label";
    private static final String DEFAULT_RACFID = "Q119M";
    private static final String DEFAULT_RACFID_PASSWORD = "csx123";
    private static final String DEFAULT_SXLOGON_STAGING_URL = "https://sxlogon-staging.csx.com";
    private static final String LOGINPAGE_USERNAME_TEXTBOX_OBJECT_ID = "username";
    private static final String LOGINPAGE_PASSWORD_TEXTBOX_OBJECT_ID = "password";
    private static final String LOGINPAGE_LOGIN_BUTTON_OBJECT_XPATH = "//*[@id=\"okta-login-section\"]/div[7]/input";
    public static final String downloadPath = System.getProperty("user.dir");
    private static int maxSyncTime = 60;

    private SeleniumUtil() {
    }

    /**
     * ---------------------------Maximize Window------------------------------------------
     */
    public static void maximizeWindow(final WebDriver driver) {
        driver.manage().window().maximize();
    }

    /**
     * ---------------------------Resize window for Mobile------------------------------------------
     */
    public static void resizeWindowForMobile(final WebDriver driver) {
        driver.manage().window().setSize(new Dimension(50, 750));
    }

    /**
     * ---------------------------Resize window for Tablet------------------------------------------
     */
    public static void resizeWindowForTablet(final WebDriver driver) {
        driver.manage().window().setSize(new Dimension(700, 750));
    }

    /**
     * ------------------------------- System Date in custom
     * Format--------------------------------------
     */
    public static String todayDate(String dateTimeFormat) {
        LocalDateTime dateTime = LocalDateTime.now();
        return dateTime.format(DateTimeFormatter.ofPattern(dateTimeFormat)); // give any custom format
        // like "MM/dd/yyyy
        // | HH:mm"
    }

    /**
     * ------------------------------- Add days to System Date in custom
     * Format--------------------------------------
     */
    public static String addDays(String dateTimeFormat, int days) {
        LocalDateTime dateTime = LocalDateTime.now().plusDays(days);
        return dateTime.format(DateTimeFormatter.ofPattern(dateTimeFormat)); // give any custom format
        // like "MM/dd/yyyy
        // | HH:mm"
    }

    /**
     * ------------------------------- minus days to System Date in custom
     * Format--------------------------------------
     */
    public static String minusDays(String dateTimeFormat, int days) {
        LocalDateTime dateTime = LocalDateTime.now().minusDays(days);
        return dateTime.format(DateTimeFormatter.ofPattern(dateTimeFormat)); // give any custom format
        // like "MM/dd/yyyy
        // | HH:mm"
    }

    /**
     * ------------------------------- Add hours to System Date in custom
     * Format--------------------------------------
     */
    public static String addHours(String dateTimeFormat, int hours) {
        LocalDateTime dateTime = LocalDateTime.now().plusHours(hours);
        return dateTime.format(DateTimeFormatter.ofPattern(dateTimeFormat)); // give any custom format
        // like "MM/dd/yyyy
        // | HH:mm"
    }


    /**
     * ------------------------------- Add minutes to System Date in custom
     * Format--------------------------------------
     */
    public static String addMinutes(String dateTimeFormat, int minutes) {
        LocalDateTime dateTime = LocalDateTime.now().plusMinutes(minutes);
        return dateTime.format(DateTimeFormatter.ofPattern(dateTimeFormat)); // give any custom format
        // like "MM/dd/yyyy
        // | HH:mm"
    }


    /**
     * ------------------------------- minus hours to System Date in custom
     * Format--------------------------------------
     */
    public static String minusHours(String dateTimeFormat, int hours) {
        LocalDateTime dateTime = LocalDateTime.now().minusHours(hours);
        return dateTime.format(DateTimeFormatter.ofPattern(dateTimeFormat)); // give any custom format
        // like "MM/dd/yyyy
        // | HH:mm"
    }

    /**
     * ------------------------------- Oracle Database connection
     * --------------------------------------------------------
     *
     * @throws SQLException
     */
    public static List<String> oracleConnectionColRetrieve(String dbUrl, String username, String password, String sql, int colNum) throws SQLException {
        Connection dbConnection = null;
        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            LOGGER.error(ERROR_MSG, e);
            fail();
        }
        try {
            dbConnection = DriverManager.getConnection(dbUrl, username, password);
        } catch (SQLException e) {
            LOGGER.error(ERROR_MSG, e);
            fail();
        }

        Statement stmt = dbConnection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<String> col = new ArrayList<>();
        col = null;
        while (rs.next()) {
            col.add(rs.getString(colNum));
        }
        return col;
    }

    /**
     * Oracle Database connection, run SQL query and returns Resultset
     * --------------------------------------------------------
     *
     * @throws SQLException
     */
    public static Connection dbConnection;

    public static ResultSet oracleExecuteSQLQuery(String dbUrl, String username, String password, String sqlQuery) throws SQLException {
        dbConnection = null;
        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            LOGGER.error(ERROR_MSG, e);
            fail();
        }
        try {
            dbConnection = DriverManager.getConnection(dbUrl, username, password);
        } catch (SQLException e) {
            LOGGER.error(ERROR_MSG, e);
            fail();
        }
        Statement stmt = dbConnection.createStatement();
        ResultSet rs = stmt.executeQuery(sqlQuery);
        return rs;
    }

    public static void closeDBConnection() throws SQLException {
        if (!(dbConnection.equals(null))) {
            dbConnection.close();
        }
    }

    /**
     * ------------------------------- File download confirmation --------------------------------------
     */

    public static boolean isFileDownloaded(String partialFileName) {
        File dir = new File(downloadPath);
        File[] files = dir.listFiles();
        boolean flag = false;
        for (int i = 1; i < files.length; i++) {
            if (files[i].getName().contains(partialFileName)) {
                flag = true;
            }
        }
        return flag;

    }

    /**
     * ------------------------------- Get Newest File --------------------------------------
     */

    public static String getTheNewestFile(String filePath, String ext) {
        //File theNewestFile = null;
        File dir = new File(filePath);
        FileFilter fileFilter = new WildcardFileFilter("*." + ext);
        File[] files = dir.listFiles(fileFilter);
        String name = "";

        if (files.length > 0) {
            /** The newest file comes first **/
            Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
            //theNewestFile = files[0];
            name = files[0].getName();
        }

        return name;
    }

    /**
     * -------------------------------Delete file --------------------------------------
     */

    public static void deleteExistingFile(String partialFileName) {
        File dir = new File(downloadPath);
        File[] files = dir.listFiles();
        for (int i = 1; i < files.length; i++) {
            if (files[i].getName().contains(partialFileName)) {
                files[i].delete();
            }
        }
    }

    /**
     * -------------------------------Get file name --------------------------------------
     */
    public static String getFilename(String partialFileName) {
        File dir = new File(downloadPath);
        File[] files = dir.listFiles();
        String reportFileName = "";
        for (int i = 1; i < files.length; i++) {
            if (files[i].getName().contains(partialFileName)) {
                reportFileName = files[i].getName();
            }
        }
        return reportFileName;
    }

    /**
     * -------------------------------Delete file --------------------------------------
     */

    public static void deleteExistingManpowerReport(String partialFileName) {
        File dir = new File(downloadPath);
        File[] files = dir.listFiles();
        for (int i = 1; i < files.length; i++) {
            if (files[i].getName().contains(partialFileName)) {
                files[i].delete();
            }
        }
    }

    /**
     * -------------------------------Get file name --------------------------------------
     */
    public static String getReportname(String partialFileName) {
        File dir = new File(downloadPath);
        File[] files = dir.listFiles();
        String reportFileName = "";
        for (int i = 1; i < files.length; i++) {
            if (files[i].getName().contains(partialFileName)) {
                reportFileName = files[i].getName();
            }
        }
        return reportFileName;
    }

    /**
     * -------------------------------Create New Tab and Switch to new tab
     * --------------------------------------
     */
    public static void switchNewTab(WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript("window.open()");
        List<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));
    }


    /**
     * -------------------------------Get PDF Content --------------------------------------
     */
    public static String getPDFContent(WebDriver driver, String url) throws IOException {
        driver.get(url);
        URL pdfUrl = new URL(driver.getCurrentUrl());
        InputStream inputStream = pdfUrl.openStream();
        BufferedInputStream bufferedIPS = new BufferedInputStream(inputStream);
        PDDocument doc = PDDocument.load(bufferedIPS);
        String content = new PDFTextStripper().getText(doc);
        String pdfContent = content.toLowerCase();
        doc.close();
        return pdfContent;
    }

    /**
     * -------------------------------Simple Date Format--------------------------------------
     */
    public static String todayDate() {
        return new SimpleDateFormat("MM/dd/yyyy HH:mm").format(new Date());
    }

    /**
     * ------------------------------------------------------------------------------------------------------------------
     * Click on the WebElement With ID and handling WebDriverWait to handle NoSuchElementException
     * Passing Element as String
     */
    public static void clickElementByID(final WebDriver driver, final String elementID) {
        waitUntilClickById(driver, elementID);
        scrollToElement(driver, driver.findElement(By.id(elementID)));
        highLighterMethod(driver, driver.findElement(By.id(elementID)));
        driver.findElement(By.id(elementID)).click();
    }

    /**
     * ------------------------------------------------------------------------------------------------------------------
     * Click on the WebElement With XPATH and handling WebDriverWait to handle NoSuchElementException
     * Passing Element as String
     */
    public static void clickElementbyXPath(final WebDriver driver, final String elementID) {
        try {
            waitUntilClickByXpath(driver, elementID);
            scrollToElement(driver, driver.findElement(By.xpath(elementID)));
            driver.findElement(By.xpath(elementID)).click();
        } catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
    }

    /**
     * ------------------------------------------------------------------------------------------------------------------
     * Click on the WebElement With XPATH and handling WebDriverWait to handle NoSuchElementException
     * Passing Element as WebElement
     */
    public static void clickElementbyWebElement(final WebDriver driver, final WebElement elementID) {
        try {
            waitUntilClickByElement(driver, elementID);
            scrollToElement(driver, elementID);
            elementID.sendKeys("");
            elementID.click();
        } catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        } catch (final Exception e) {
            // If clickElementbyWebElement method failed to click on the element with
            // sendKyes
            // then this catch block will be executed with calling
            // clickElementbyWebElementWithOutSendKeys method
            clickElementbyWebElementWithOutSendKeys(driver, elementID);
        }
    }

    /**
     * ------------------------------------------------------------------------------------------------------------------
     * Click on the WebElement With XPATH and handling WebDriverWait to handle NoSuchElementException
     * Passing Element as WebElement This method doesn't use sendKeys
     */
    public static void clickElementbyWebElementWithOutSendKeys(final WebDriver driver, final WebElement elementID) {
        try {
            waitUntilClickByElement(driver, elementID);
            scrollToElement(driver, elementID);
            elementID.click();
        } catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
    }

    /**
     * ---------------------------------------------------------------------------------------------------
     * Submit Element by XPATH
     */
    public static void submitElementbyXPath(final WebDriver driver, final String elementID) {
        final JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", driver.findElement(By.xpath(elementID)));
    }

    /**
     * ----------------------------------------------------------------------------------------------------
     * Scroll till Web Element
     */
    public static void scrollToWebElement(final WebDriver driver, final WebElement webelement) {
        final JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].scrollIntoView(true);", webelement);
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * Validate Element
     */
    public static void validateElement(final WebDriver driver, final String xpath, final String testCaseName, final String expectedResult) {
        try {
            waitUntilVisibleByXpath(driver, xpath);
            final WebElement element = driver.findElement(By.xpath(xpath));
            scrollToElement(driver, element);
            highLighterMethod(driver, element);
            Assert.assertEquals(expectedResult.trim(), element.getText().trim());
        } catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * Set value to Element
     */
    public static void setValueToElementByXpath(final WebDriver driver, final String xpath, final String inputValue) {
        try {
            waitUntilClickByXpath(driver, xpath);
            final WebElement element = driver.findElement(By.xpath(xpath));
            scrollToElement(driver, element);
            highLighterMethod(driver, element);
            element.clear();
            element.sendKeys(inputValue);
        } catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
    }

    public static void setValueToElementByXpath(final WebDriver driver, final WebElement element, final String inputValue) {
        try {
            waitUntilClickByElement(driver, element);
            scrollToElement(driver, element);
            highLighterMethod(driver, element);
            element.clear();
            element.sendKeys(inputValue);
        } catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
    }

    /**
     * ---------------------------------------------------------------------------------------------------------
     * Set Value to WebElement using XPATH and handling WebDriverWait to handle NoSuchElementException
     */
    public static void setValueToElement(final WebDriver driver, final WebElement webElement, final String inputValue) {
        try {
            waitUntilClickByElement(driver, webElement);
            scrollToElement(driver, webElement);
            highLighterMethod(driver, webElement);
            webElement.clear();
            webElement.sendKeys(inputValue);
        } catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * get value by element
     */
    public static String getValueByElement(final WebDriver driver, final WebElement webElement) {
        try {
            waitUntilVisibleByElement(driver, webElement);
            scrollToElement(driver, webElement);
            highLighterMethod(driver, webElement);
            if (waitUntilVisibleByElement(driver, webElement).isDisplayed()) {
                return webElement.getText().trim();
            }
        } catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return null;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * Is element not empty-
     */
    public static boolean isElementNotEmpty(final WebDriver driver, final WebElement webElement, final String elementName) {
        return StringUtils.isNotEmpty(getValueByElement(driver, webElement));
    }

    /**
     * ---------------------------------------------------------------------------- Get Text Value of
     * the Input Element using XPATH and handling WebDriverWait to handle NoSuchElementException
     * //Handled the webelement from - ShipmentEnquiryObjectRepo and Actions class
     */
    public static String getInputElementValue(final WebDriver driver, final WebElement webElement) {
        try {
            waitUntilVisibleByElement(driver, webElement);
            scrollToElement(driver, webElement);
            highLighterMethod(driver, webElement);
            if (waitUntilVisibleByElement(driver, webElement).isDisplayed()) {
                return webElement.getAttribute("value").trim();
            }
        } catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return null;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * get Enable Disable by element
     */
    public static Boolean getEnableDisableByElement(final WebDriver driver, final WebElement webElement) {
        try {
            waitUntilVisibleByElement(driver, webElement);
            if (waitUntilVisibleByElement(driver, webElement) != null) {
                return webElement.isEnabled();
            }
        } catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return false;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * is Disable by element
     */
    public static Boolean isDisableByElement(final WebDriver driver, final WebElement webElement) {
        try {
            waitUntilVisibleByElement(driver, webElement);
            if (waitUntilVisibleByElement(driver, webElement) != null) {
                final String attribute = webElement.getAttribute("disabled");
                return attribute != null && attribute.equalsIgnoreCase("true");
            }
        } catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return false;
    }

    /**
     * ----------------------------------------------------------------------------------------------
     * this method checks if the input's enabled status has changed by overriding the apply method and
     * applying the condition that we are looking for pass testIsEnabled true if checking whether the
     * input has become enabled pass testIsEnabled false if checking whether the input has become
     * disabled
     */
    public static Boolean isEnabledDisabledWaitingForChange(final WebDriver driver, final WebElement webElement, final Boolean testIsEnabled) {
        final WebDriverWait driverWait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS));
        Boolean isEnabled = null;
        try {
            isEnabled = driverWait.until(driver1 -> {
                final Boolean isElementEnabled = webElement.isEnabled();
                return testIsEnabled ? isElementEnabled : !isElementEnabled;
            });
        } catch (final org.openqa.selenium.TimeoutException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return isEnabled;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * is Spinner Enabled
     */
    public static Boolean isSpinnerEnabled(final WebDriver driver, final WebElement webElement) {
        try {
            waitUntilVisibleByElement(driver, webElement);
            if (waitUntilVisibleByElement(driver, webElement) != null) {
                final String webElementCSS = getElementsCSS(driver, webElement, "webelemnt CSS");
                if (webElementCSS != null && !webElementCSS.isEmpty()) {
                    return webElementCSS.contains("loadingSpinner");
                } else {
                    return false;
                }
            }
        } catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return false;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * Is disabled by element CSS
     */
    public static Boolean isDisabledByElementCSS(final WebDriver driver, final WebElement webElement) {
        int i = 20;
        try {
            waitUntilVisibleByElement(driver, webElement);
            if (waitUntilVisibleByElement(driver, webElement) != null) {
                final String webElementCSS = getElementsCSS(driver, webElement, "webelemnt CSS");
                if (webElementCSS != null && !webElementCSS.isEmpty()) {
                    return webElementCSS.contains("ui-state-disabled");
                } else {
                    return false;
                }
            }
        } catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return false;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * Mouse over Element
     */
    public static void mouseOverElement(final WebDriver driver, final WebElement webElement) {
        try {
            waitUntilVisibleByElement(driver, webElement);
            highLighterMethod(driver, webElement);
            final Actions actObj = new Actions(driver);
            actObj.moveToElement(webElement).build().perform();
        } catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * Check & Uncheck box
     */
    public static void clickCheckedUnCheckedElement(final WebDriver driver, final WebElement elementID) {
        try {
            waitUntilClickByElement(driver, elementID);
            final boolean chk = elementID.isEnabled();
            if (chk == true) {
                elementID.click();
            }
        } catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * Mouse over element select
     */
    public static void mouseOverElementSelect(final WebDriver driver, final WebElement webElement) {
        try {
            waitUntilVisibleByElement(driver, webElement);
            waitUntilVisibleByElement(driver, webElement).isSelected();
            final Actions actObj = new Actions(driver);
            actObj.moveToElement(webElement).build().perform();
        } catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
    }

    /**
     * Navigate to URL
     */
    public static void navigateToURL(final WebDriver driver, final String url) {
        driver.get(url);
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * Reload page
     */
    public static void reloadPage(final WebDriver driver) {
        driver.navigate().refresh();
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * Navigate to URL in new tab
     */
    public static void navigateToURLInNewTab(final WebDriver driver, final String url) {
        driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "t");
        final ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(0));
        driver.get(url);
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * Is element present
     */
    public static boolean isElementPresent(final WebDriver driver, final String xPath) {
        try {
            waitUntilElementPresenceByXpath(driver, xPath);
            return driver.findElements(By.xpath(xPath)).size() > 0;
        } catch (final NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
            return false;
        }
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * Is Anchor present
     */
    public static boolean isAnchorPresent(final WebDriver driver, final String text) {
        return SeleniumUtil.isElementPresent(driver, "//a[contains(text(),'" + text + "')]");
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * get CSS classes by element
     */
    public static String getCssClassesByElement(final WebDriver driver, final WebElement webElement, final String elementName) {
        try {
            waitUntilVisibleByElement(driver, webElement);
            if (waitUntilVisibleByElement(driver, webElement).isDisplayed()) {
                highlightElement(driver, webElement);
                return webElement.getAttribute("class").trim();
            }
        } catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return null;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * Get CSS value by element
     */
    public static String getCSSValueByElement(final WebDriver driver, final WebElement webElement, final String cssAttributeName, final String elementName) {
        try {
            waitUntilVisibleByElement(driver, webElement);
            if (waitUntilVisibleByElement(driver, webElement).isDisplayed()) {
                highlightElement(driver, webElement);
                return webElement.getCssValue(cssAttributeName);
            }
        } catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return null;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * get data table size by xpath
     */
    public static int getDataTableSizeByXpath(final WebDriver driver, final String tableXPath, final String testCaseName) {
        final List<WebElement> rows = driver.findElements(By.xpath(tableXPath + "/tbody/tr"));
        return rows.size();
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * get mobile view card size by xpath
     */
    public static int getMobileViewCardsSizeByXpath(final WebDriver driver, final String cardXPath, final String testCaseName) {
        final List<WebElement> rows = driver.findElements(By.xpath(cardXPath));
        return rows.size();
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * To switch to a different window
     */
    public static void switchToOtherWindow(final WebDriver driver, final int windowNumber) {
        final List<String> browserTabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(browserTabs.get(windowNumber));
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * is element displayed
     */
    public static boolean isElementDisplayed(final WebElement webElement) {
        try {
            return webElement.isDisplayed();
        } catch (final NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
            return false;
        }
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * is element not displayed
     */
    public static boolean isElementNotDisplayed(final WebElement webElement) {
        try {
            return !webElement.isDisplayed();
        } catch (final NoSuchElementException ele) {
            return true;
        }
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * get element's CSS
     */
    public static String getElementsCSS(final WebDriver driver, final WebElement webElement, final String elementName) {
        try {
            waitUntilVisibleByElement(driver, webElement);
            if (waitUntilVisibleByElement(driver, webElement).isDisplayed()) {
                return webElement.getAttribute("class");
            }
        } catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return null;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * get web element from string path
     */
    public static WebElement getWebElementFromStringPath(final WebDriver driver, final String xpath) {
        return driver.findElement(By.xpath(xpath));
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * Xpath eists
     */
    public static boolean xPathExists(final WebDriver driver, final String xpath) {
        try {
            waitUntilVisibleByXpath(driver, xpath);
            return true;
        } catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
            return false;
        }
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * get value by element no log
     */
    public static String getValueByElementNoLog(final WebDriver driver, final WebElement webElement, final String elementName) {
        try {
            waitUntilVisibleByElement(driver, webElement);
            if (waitUntilVisibleByElement(driver, webElement).isDisplayed()) {
                return webElement.getText().trim();
            }
        } catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return null;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * check visibility by element
     */
    public static Boolean checkVisibilityByElement(final WebDriver driver, final WebElement webElement, final String elementName) {
        Boolean isVisible = false;
        try {
            waitUntilVisibleByElement(driver, webElement);
            if (waitUntilVisibleByElement(driver, webElement) != null) {
                isVisible = true;
            }
        } catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            // fail();
        }
        return isVisible;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * is element enabled
     */
    public static Boolean isElementEnabled(final WebDriver driver, final WebElement webElement, final String elementName) {
        Boolean isEnabled = false;
        try {
            waitUntilVisibleByElement(driver, webElement);
            if (waitUntilVisibleByElement(driver, webElement) != null) {
                isEnabled = webElement.isEnabled();
            }
        } catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return isEnabled;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * is element not present
     */
    public static Boolean isElementNotPresent(final WebDriver driver, final String xPath) {
        try {
            waitUntilElementNotPresenceByXpath(driver, xPath);
            return waitUntilElementNotPresenceByXpath(driver, xPath);
        } catch (final Exception ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
            return true;
        }
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * is element not present with wait
     */
    public static Boolean isElementNotPresentWithWait(final WebDriver driver, final String xPath, final int waitTimeinSec) {
        try {
            waitUntilElementNotPresenceByXpathByTime(driver, xPath, waitTimeinSec);
            return waitUntilElementNotPresenceByXpathByTime(driver, xPath, waitTimeinSec);
        } catch (final Exception ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
            return true;
        }
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * is button enabled
     */
    public static Boolean isButtonEnabled(final WebDriver driver, final WebElement webElement) {
        try {
            waitUntilVisibleByElement(driver, webElement);
            if (waitUntilVisibleByElement(driver, webElement).isDisplayed()) {
                return webElement.isEnabled();
            }
        } catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return null;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * get value by xpath
     */
    public static String getValueByXpath(final WebDriver driver, final String xPath) {
        try {
            waitUntilVisibleByXpath(driver, xPath);
            if (waitUntilVisibleByXpath(driver, xPath).isDisplayed()) {
                final WebElement webElement = driver.findElement(By.xpath(xPath));
                return webElement.getText().trim();
            }
        } catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return null;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * is form control input field filled and valid
     */
    public static boolean isFormControlInputFieldFilledAndValid(final WebDriver driver, final WebElement webElement) {
        final String fieldCSS = SeleniumUtil.getElementsCSS(driver, webElement, "tsFailedPaymentOopsImage");
        return fieldCSS.contains("ui-state-filled") && !fieldCSS.contains("ng-invalid");
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * This method checks the value in list and return boolean reusult.
     */
    public static boolean checkValueInList(final WebDriver driver, final List<WebElement> webElement, final String expectedValueInList,
                                           final String elementName) {
        final Boolean isValueExist = false;
        try {
            waitUntilVisibleByElements(driver, webElement);
            if (waitUntilVisibleByElements(driver, webElement) != null) {
                final List<WebElement> elements = webElement;
                for (int i = 0; i < elements.size(); i++) {
                    final String listValue = elements.get(i).getText().trim();
                    if (listValue.contains(expectedValueInList)) {
                        return true;
                    }
                }
                return isValueExist;
            }
        } catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return isValueExist;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * get URL of new tab
     */
    public static String getUrlOfNewTab(final WebDriver driver, final int driverWaitTimeInSecs) {
        final WebDriverWait driverWait = new WebDriverWait(driver, Duration.ofSeconds(driverWaitTimeInSecs));
        // get window handlers as list
        final List<String> browserTabs = new ArrayList<>(driver.getWindowHandles());
        // Ship to new tab and check if correct page opened
        String shipmentInstructionsTitle = null;
        driver.switchTo().window(browserTabs.get(2));
        if (driverWait.until(ExpectedConditions.titleContains("Main Page"))) {
            shipmentInstructionsTitle = driver.getTitle();
        }
        driver.switchTo().window(browserTabs.get(0));
        return shipmentInstructionsTitle;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * is text there
     */
    public static String isTextThere(final WebDriver driver, final WebElement element, final Boolean testIsEnabled, final String elementName,
                                     final String attributeName) {
        final WebDriverWait driverWait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS));
        try {
            driverWait.until(ExpectedConditions.visibilityOf(element));
            driverWait.until(ExpectedConditions.elementToBeClickable(element));
            Thread.sleep(3000);
            return driverWait.until(drivers -> {
                if (element.getAttribute(attributeName).length() > 0) {
                    element.click();
                    return element.getAttribute(attributeName);
                }
                return null;
            });
        } catch (org.openqa.selenium.TimeoutException | InterruptedException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return null;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * get element value by ID
     */
    public static String getElementValueById(final WebDriver driver, final String elementId, final String elementName) {
        try {
            final JavascriptExecutor js = (JavascriptExecutor) driver;
            return (String) js.executeScript("return document.getElementById(" + "'" + elementId + "'" + ").value");
        } catch (final org.openqa.selenium.TimeoutException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return null;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * select value in PrimeNgDropdown
     */
    public static void selectValueInPrimeNgDropDown(final WebDriver driver, final WebElement dropDownElementOrParent, final String value) {
        final WebElement dropDownElement = findLabelPrimeNgDropdown(driver, dropDownElementOrParent);
        final WebElement element = dropDownElement.findElement(By.xpath(String.format(DROPDOWN_ITEM_SELECTOR_IN_OVERLAY, value)));
        clickElementbyWebElement(driver, element);
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * select partial match value in PrimeNgDropdown
     */
    public static void selectPartialMatchValueInPrimeNgDropDown(final WebDriver driver, final WebElement dropDownElementOrParent, final String value) {
        final WebElement dropDownElement = findLabelPrimeNgDropdown(driver, dropDownElementOrParent);
        final WebElement element = dropDownElement.findElement(By.xpath(String.format(DROPDOWN_PARTIAL_MATCH_ITEM_SELECTOR_IN_OVERLAY, value)));
        clickElementbyWebElement(driver, element);
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * find Label PrimeNgDropdown
     */
    private static WebElement findLabelPrimeNgDropdown(final WebDriver driver, final WebElement dropDownElementOrParent) {
        WebElement dropDownElement = null;
        if (dropDownElementOrParent != null && "p-dropdown".equalsIgnoreCase(dropDownElementOrParent.getTagName())) {
            dropDownElement = dropDownElementOrParent;
        } else if (dropDownElementOrParent != null) {
            dropDownElement = dropDownElementOrParent.findElement(By.tagName("p-dropdown"));
        } else {
            dropDownElement = driver.findElement(By.tagName("p-dropdown"));
        }
        final WebDriverWait driverWait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS));
        driverWait.until(ExpectedConditions.visibilityOf(dropDownElement));
        driverWait.until(ExpectedConditions.elementToBeClickable(dropDownElement.findElement(By.xpath(DROPDOWN_CLICKABLE_LABEL))));
        clickElementbyWebElement(driver, dropDownElement.findElement(By.xpath(DROPDOWN_CLICKABLE_LABEL)));
        try {
            Thread.sleep(300);
        } catch (final InterruptedException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        driverWait.until(ExpectedConditions.visibilityOf(dropDownElement.findElement(By.xpath(DROPDOWN_OVERLAY))));
        return dropDownElement;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * get PrimeNgDropdown value
     */
    public static String getPrimeNgDropDownValue(final WebDriver driver, final int driverWaitTimeInSecs, final WebElement dropDownElementOrParent,
                                                 final String elementName, final String dropDownLabel) {
        WebElement dropDownElement = null;
        if (dropDownElementOrParent != null && "p-dropdown".equalsIgnoreCase(dropDownElementOrParent.getTagName())) {
            dropDownElement = dropDownElementOrParent;
        } else if (dropDownElementOrParent != null) {
            dropDownElement = dropDownElementOrParent.findElement(By.tagName("p-dropdown"));
        } else {
            dropDownElement = driver.findElement(By.tagName("p-dropdown"));
        }
        waitUntilVisibleByElement(driver, dropDownElement);
        return getValueByElement(driver, dropDownElement.findElement(By.xpath(dropDownLabel)));
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * is PrimeNg check box checked
     */
    public static boolean isPrimeNgCheckboxChecked(final WebDriver driver, final WebElement parentElement) {
        WebElement checkBox = null;
        if (parentElement != null) {
            checkBox = parentElement.findElement(By.tagName("p-checkbox"));
        } else {
            checkBox = driver.findElement(By.tagName("p-checkbox"));
        }
        waitUntilVisibleByElement(driver, checkBox);
        return checkBox.findElement(By.xpath("div/div[2]")).getAttribute("class").contains("ui-state-active");
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * high lighter method
     */
    public static void highLighterMethod(final WebDriver driver, final WebElement element) {
        final JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", element);
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * Remove high lighter
     */
    public static void removeHighLighter(final WebDriver driver, final WebElement element) {
        final JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('style', 'background: white;');", element);
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * get place holder text
     */
    public static String getPlaceHolderText(final WebElement element) {
        return element.getAttribute("placeholder");
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * get current screen URL
     */
    public static String getCurrentScreenUrl(final WebDriver driver) {
        waitUntilVisibleByXpath(driver,"//th[contains(text(),' Equipment ')]");
        return driver.getCurrentUrl();

    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * focus out of text area
     */
    public static void focusOutOfTextArea(final WebElement webElement) {
        final WebElement destWebElement = webElement;
        destWebElement.sendKeys(Keys.TAB);
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * is spinner loaded
     */
    public static Boolean isSpinnerLoaded(final WebDriver driver, final WebElement webElement, final String elementName) {
        try {
          waitUntilInvisibleByElement(driver,webElement);
            if (waitUntilInvisibleByElement(driver,webElement) != null) {
                final String webElementCSS = getElementsCSS(driver, webElement, "webelemnt CSS");
                if (webElementCSS != null && !webElementCSS.isEmpty()) {
                    return webElementCSS.contains("loadingSpinner");
                } else {
                    return false;
                }
            }
        } catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return false;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * is angular spinner loaded
     */
    public static void waitForApiCallInAngular(final WebDriver driver) {
        final Wait<WebDriver> wait = new FluentWait<>(driver).pollingEvery(Duration.ofMillis(200))
                .withTimeout(Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS)).ignoring(NoSuchElementException.class);
        // making sure that spinner is present
        wait.until(d -> ExpectedConditions.visibilityOf(d.findElement(By.xpath(SPINNER_XPATH))));

        // we have to wait until spinner goes away
        waitUntilInvisibleByElement(driver,driver.findElement(By.xpath(SPINNER_XPATH)));
       }

    /**
     * -------------------------------------------------------------------------------------------------------
     * get hidden element value by xpath
     */
    public static String getHiddenElementValueByXPath(final WebDriver driver, final String xPath, final String elementName) {
        try {
            final JavascriptExecutor js = (JavascriptExecutor) driver;
            final WebElement hiddenDiv = driver.findElement(By.xpath(xPath));
            String value = hiddenDiv.getText();
            final String script = "return arguments[0].innerHTML";
            return value = (String) ((JavascriptExecutor) driver).executeScript(script, hiddenDiv);
        } catch (final org.openqa.selenium.TimeoutException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return null;
    }

    public static void sxoLogon(final WebDriver driver, final String url, final String racfId, final String pwd) {
        navigateToURL(driver, url);
        LOGGER.info("Launched sxo url: " + url);
        driver.findElement(By.id(LOGINPAGE_USERNAME_TEXTBOX_OBJECT_ID)).sendKeys(racfId);
        driver.findElement(By.id(LOGINPAGE_PASSWORD_TEXTBOX_OBJECT_ID)).sendKeys(pwd);
        LOGGER.info(USERNAME + racfId + AND_PASSWORD + pwd + IS_ENTERED);
        driver.findElement(By.xpath(LOGINPAGE_LOGIN_BUTTON_OBJECT_XPATH)).click();
        LOGGER.info("Login button is clicked");
        try {
            Thread.sleep(5000);
        } catch (final InterruptedException e) {
            LOGGER.info("Logging Failed");
        }
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * SXO LOGON Method with Default Staging URL and Credentials
     */
    public static void sxoLogon(final WebDriver driver) {
        navigateToURL(driver, DEFAULT_SXLOGON_STAGING_URL);
        LOGGER.info("Launched sxo url: " + DEFAULT_SXLOGON_STAGING_URL);
        driver.findElement(By.id(LOGINPAGE_USERNAME_TEXTBOX_OBJECT_ID)).sendKeys(DEFAULT_RACFID);
        driver.findElement(By.id(LOGINPAGE_PASSWORD_TEXTBOX_OBJECT_ID)).sendKeys(DEFAULT_RACFID_PASSWORD);
        LOGGER.info(USERNAME + DEFAULT_RACFID + AND_PASSWORD + DEFAULT_RACFID_PASSWORD + IS_ENTERED);
        driver.findElement(By.xpath(LOGINPAGE_LOGIN_BUTTON_OBJECT_XPATH)).click();
        LOGGER.info("Login button is clicked");
        try {
            Thread.sleep(5000);
        } catch (final InterruptedException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     *
     * @param driver
     * @param url    SXO LOGON Method with URL Passing. This method used default Racf ID and Pwd.
     */
    public static void sxoLogon(final WebDriver driver, final String url) {
        navigateToURL(driver, url);
        LOGGER.info("Launched sxo url:{} ", url);
        driver.findElement(By.id(LOGINPAGE_USERNAME_TEXTBOX_OBJECT_ID)).sendKeys(DEFAULT_RACFID);
        driver.findElement(By.id(LOGINPAGE_PASSWORD_TEXTBOX_OBJECT_ID)).sendKeys(DEFAULT_RACFID_PASSWORD);
        LOGGER.info(USERNAME + DEFAULT_RACFID + AND_PASSWORD + DEFAULT_RACFID_PASSWORD + IS_ENTERED);
        driver.findElement(By.xpath(LOGINPAGE_LOGIN_BUTTON_OBJECT_XPATH)).click();
        LOGGER.info("Login button is clicked");
        try {
            Thread.sleep(5000);
        } catch (final InterruptedException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
    }

    /**
     * ----This Method is for Sorting of Ascending&Descending Order---------------
     */

    public static void verifyAscendingAndDescending(final WebDriver driver, final String XPATH, final String elementName) {
        try {
            final JavascriptExecutor js = (JavascriptExecutor) driver;
            final List<WebElement> AllAscendingAndDescending = driver.findElements(By.xpath(XPATH));
            for (final WebElement AscAndDsc : AllAscendingAndDescending) {
                final String value = AscAndDsc.getText();
                final String script = "return arguments[0].innerHTML";
                LOGGER.info(elementName + ":" + value);
            }
        } catch (final org.openqa.selenium.TimeoutException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
    }

    public static void clearTextField(final WebDriver driver, final WebElement webElement, final String inputValue) {
        try {
            waitUntilClickByElement(driver,webElement);
            webElement.sendKeys(Keys.CONTROL, Keys.chord("a"));
            webElement.sendKeys(Keys.BACK_SPACE);
        } catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
    }

    /**
     * -------------------------------Add days to current date--------------------------------------
     */
    public static Date addDays(final int daysCount) {
        Date date = new Date();
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, daysCount);
        date = cal.getTime();
        return date;
    }

    /**
     * -------------------------------Returns list of weblements using the by condition passed
     */
    public static List<WebElement> getListOfElements(final WebDriver driver, final By by) {
        try {
           waitForElements(driver,by);
            Thread.sleep(3000);
        } catch (org.openqa.selenium.TimeoutException | InterruptedException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        List<WebElement> ls = driver.findElements(by);
        return ls;
    }

    /**
     * -------------------------------Remove special characters from string to get double/float
     * value--------------------------------------
     */
    public static String removeCharactersInString(final String originalString, final String toRemove) {
        String changedString = new String(originalString);
        for (char c : toRemove.toCharArray()) {
            changedString = changedString.replace(c, (char) 32);
        }
        changedString = changedString.replaceAll(" ", "");
        return changedString;
    }

    /**
     *
     * ***************************************************************************
     *
     * New Methods added BY NAGOORVALI
     *
     * ***************************************************************************
     */

    /**
     * -------------------------------Return the BGColor of the element in terms of Hexa Decimal Value --------------------------------------
     */

    public static String getElementBGColor(final WebDriver driver, final WebElement element){
        String color = element.getCssValue("background-color");
        String[] hexValue = color.replace("rgba(", "").replace(")", "").split(",");
        hexValue[0] = hexValue[0].trim();
        int hexValue1 = Integer.parseInt(hexValue[0]);
        hexValue[1] = hexValue[1].trim();
        int hexValue2 = Integer.parseInt(hexValue[1]);
        hexValue[2] = hexValue[2].trim();
        int hexValue3 = Integer.parseInt(hexValue[2]);
        String actualColor = String.format("#%02x%02x%02x", hexValue1, hexValue2, hexValue3);
        System.err.println("BGColor is :" + actualColor);
        return actualColor;
    }


    /**
     * -------------------------------wait methods --------------------------------------
     */
    public static void waitUntilClickById(final WebDriver driver, final String elementID) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(elementID)));
        wait.until(ExpectedConditions.elementToBeClickable(By.id(elementID)));
    }

    public static void waitUntilClickByXpath(final WebDriver driver, final String xapth) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xapth)));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xapth)));
    }
    public static void waitUntilClickByXpath(final WebDriver driver, final String xapth,int time) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(time));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xapth)));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xapth)));
    }
    public static void waitUntilClickByElement(final WebDriver driver, final WebElement webElement) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS));
        wait.until(ExpectedConditions.visibilityOf((webElement)));
        wait.until(ExpectedConditions.elementToBeClickable(webElement));
    }
    public static void waitUntilClickByElementByTime(final WebDriver driver, final WebElement webElement,int time) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(time));
        wait.until(ExpectedConditions.visibilityOf((webElement)));
        wait.until(ExpectedConditions.elementToBeClickable(webElement));
    }
    public static WebElement waitUntilClickByLocator(final WebDriver driver, final By locator) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS));
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    public static void waitUntilClickByLocatorByTime(final WebDriver driver, final By locator,int time) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(time));
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

   public static WebElement waitUntilVisibleByXpath(final WebDriver driver, final String Xpath) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Xpath)));
    }

    public static WebElement waitUntilVisibleByLocator(final WebDriver driver, final By Locator) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(Locator));
    }
    public static WebElement waitUntilVisibleByXpathByTIme(final WebDriver driver, final String Xpath,int time) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(time));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Xpath)));
    }

    public static WebElement waitUntilVisibleByElement(final WebDriver driver, final WebElement element) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS));
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public static WebElement waitUntilVisibleByElementByTIme(final WebDriver driver, final WebElement element, int time) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(time));
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public static WebElement waitUntilElementPresenceByXpath(final WebDriver driver, final String Xpath) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS));
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(Xpath)));
    }

    public static Boolean waitUntilElementNotPresenceByXpath(final WebDriver driver, final String Xpath) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS));
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Xpath)));
    }

    public static Boolean waitUntilElementNotPresenceByXpathByTime(final WebDriver driver, final String Xpath, int time) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(time));
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Xpath)));
    }

    public static List<WebElement> waitUntilVisibleByElements(final WebDriver driver, final List<WebElement> webElements) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS));
        return wait.until(ExpectedConditions.visibilityOfAllElements(webElements));
    }

  public static Boolean waitUntilInvisibleByElement(final WebDriver driver, final WebElement element) {
    final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS));
    return wait.until(ExpectedConditions.invisibilityOf(element));
  }

   public static void waitByTime(int time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            LOGGER.info("Fail in wait due to : " + e);
            Thread.currentThread().interrupt();
        }
    }

   public static FluentWait webDriverFluentWait(WebDriver driver) {
        return new FluentWait(driver)
                .withTimeout(Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS))
                .pollingEvery(Duration.ofSeconds(3))
                .ignoring(NoSuchElementException.class, NoSuchFrameException.class);
    }

    public static FluentWait webDriverFluentWait(WebDriver driver, int time) {
        return new FluentWait(driver)
                .withTimeout(Duration.ofSeconds(time))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class, NoSuchFrameException.class);
    }

    public static List<WebElement> waitForElementsByTime(WebDriver driver, By element, int timeout) {
        new WebDriverWait(driver, Duration.ofSeconds(timeout)).until(ExpectedConditions.visibilityOfElementLocated(element));
        return driver.findElements(element);
    }

    public static List<WebElement> waitForElements(WebDriver driver, By element) {
        return waitForElementsByTime(driver, element, DRIVER_WAIT_TIME_IN_SECS);
    }

    public static void fluentWaitForElementLoading(WebDriver driver, final By byElement) {
        try {
            Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                    .withTimeout(Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS))
                    .pollingEvery(Duration.ofMillis(5))
                    .ignoring(NoSuchElementException.class)
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(TimeoutException.class);
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(byElement));
            if (!element.isDisplayed()) {
                LOGGER.info("Element " + byElement + " is not displayed");
            }
        } catch (Exception e) {
            LOGGER.error("Failed to find the element and the reason is " + e);
        }
    }

    public static void waitTillLoadingCompletes(WebDriver driver, final WebElement element) {
        try {
            long start = System.currentTimeMillis();
            int elapsedTime = 0;
            waitByTime(250);
            while (element.isDisplayed()) {
                LOGGER.info("Waiting for Loading Screens to go away");
                if (elapsedTime > maxSyncTime) {
                    break;
                }
                waitByTime(500);
                elapsedTime = Math.round((System.currentTimeMillis() - start) / 1000F);
            }
        } catch (Exception e) {
            if ((e.toString().contains("NoSuchElementException") || e.getMessage().contains("NoStale")))
                LOGGER.info("Loading screenshot is not present now");
            else {
                LOGGER.error(Level.FINEST + "Fail in waitTillLoadingCompletes " + e);
            }
        }
    }

    /**
     * -------------------------------Take ScreenShot --------------------------------------
     */

    public static String captureScreenshot(final WebDriver driver, final Scenario scenario) {
        final Date now = new Date();
        final String dateString = new SimpleDateFormat("dd-MMM-yyy").format(now);
        final String dateAndTimeString = new SimpleDateFormat("dd-MMM-yyyy hh-mm-ss a z").format(now);
        final TakesScreenshot screenShot = (TakesScreenshot) driver;
        final File source = screenShot.getScreenshotAs(OutputType.FILE);
        final String dest = System.getProperty("user.dir") + File.separator + "target" + File.separator + "cucumber-html-reports" + File.separator
                + dateString + File.separator + "Error_" + dateAndTimeString + ".png";
        final File destination = new File(dest);
        try {
            FileUtils.copyFile(source, destination);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return dest;
    }

    public static String takeScreenshot(WebDriver driver, String screenshotName) {
        String destination = null;
        String imgPath = null;
        int maxRetryCount = 5;
        int retryCounter = 0;
        while (driver instanceof TakesScreenshot) {
            String dateName = new SimpleDateFormat("dd-MMM-yyyy hh-mm-ss a z").format(new Date());
            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            try {
                imgPath = "\\TestsScreenshots\\" + screenshotName + dateName + ".png";
                destination = System.getProperty("user.dir") + imgPath;
                File finalDestination = new File(destination);
                FileUtils.copyFile(source, finalDestination);
                LOGGER.info("Screenshot destination : " + destination);
                return imgPath;
            } catch (IOException e) {
                LOGGER.error("takeScreenshot Exception : " + e.getMessage());
                if (++retryCounter > maxRetryCount) {
                    Assert.assertTrue( "Exception while taking screenshot : " + e.getMessage(),false);
                    break;
                }
            }
        }
        return destination;
    }

    /**
     * -------------------------------Compare Image on UI with Local Image --------------------------------------
     */

    public static boolean compareImages(WebDriver driver, String savedImage, WebElement locator) {
        try {
            //get the saved image, the expected one
            BufferedImage expectedSatellite =
                    ImageIO.read(new File(System.getProperty("user.dir") + "/images/" + savedImage + ".png"));
            //take the new screenshot
            Screenshot screenshot = new AShot().takeScreenshot(driver, locator);
            BufferedImage actualSatellite = screenshot.getImage();
            //compare both images
            ImageDiffer imgDiff = new ImageDiffer();
            ImageDiff diff = imgDiff.makeDiff(expectedSatellite, actualSatellite);
            return diff.hasDiff();
        } catch (IOException e) {
            System.out.println("OPTA: Not able to compare images - " + e.getMessage());
            e.printStackTrace();
            return true;
        }
    }


    /**
     * -------------------------------Check for Value Change of UI Element Using JScript Executor--------------------------
     */

    public static String checkValueChanged(WebDriver driver, String expectedValue, String path, int waitTime) throws InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String elementValue = (String) js
                .executeScript("return document.evaluate(\"" + path + "\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue.value;");
        while (!(elementValue.equals(expectedValue)) && (waitTime > 0)) {
            Thread.sleep(1000);
            waitTime--;
            elementValue = (String) js
                    .executeScript("return document.evaluate(\"" + path + "\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue.value;");
        }
        return elementValue;
    }

    /**
     * -------------------------------utlitity Methods for clicking on elements--------------------------
     */

    public static void clickElementWithWait(WebDriver driver, String locator, int time) {
        waitUntilClickByLocatorByTime(driver, By.xpath(locator), time);
        WebElement element = driver.findElement(By.xpath(locator));
        scrollToElement(driver,element);
        element.click();

    }

    public static void clickElementWithWait(WebDriver driver, WebElement element, int time) {
        waitUntilClickByElementByTime(driver, element, time);
        scrollToElement(driver,element);
        element.click();
    }

    public static void clickUsingJavaScript(WebDriver driver, By locator) {
        LOGGER.info("BeforeWaitForElement in clickUsingJavaScript::" + locator);
        try {
            WebElement elm = waitUntilClickByLocator(driver, locator);
            if (!isElementVisible(driver, locator)) {
                scrollToElement(driver, elm);
            }
            highlightElement(driver, locator);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click()", elm);
            //waitTillLoadingCompletes(driver, driver.findElement(locator));
        } catch (Exception e) {
            LOGGER.info("Unable to highlight : " + e);
        }
    }

    public static void clickUsingJavaScript(WebDriver driver, WebElement element) {
        LOGGER.info("BeforeWaitForElement in clickUsingJavaScript::" + element);
        try {
            waitUntilClickByElement(driver, element);
            if (!isElementVisible(driver, element)) {
                scrollToElement(driver, element);
            }
            highlightElement(driver, element);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click()", element);
            waitTillLoadingCompletes(driver, element);
        } catch (Exception e) {
            LOGGER.info("Unable to highlight : " + e);
        }
    }

    public static void clickWithWait(WebDriver driver, By locator) {
        LOGGER.info("BeforeWaitForElement in buttonClick ::" + locator);
         try {
            WebElement elm = waitUntilClickByLocator(driver,locator);
            if (!isElementVisible(driver, locator)) {
                scrollToView(driver, locator);
            }
            elm =waitUntilClickByLocator(driver, locator);
            if (isElementVisible(driver, locator)) {
                scrollToView(driver, locator);
                highlightElement(driver, locator);
                elm.click();
            } else {
                waitUntilClickByLocator(driver, locator);
                scrollToView(driver, locator);
                highlightElement(driver, locator);
                elm.click();
            }
            waitTillLoadingCompletes(driver, driver.findElement(locator));
        } catch (Exception e) {
            LOGGER.error(Level.FINEST + "Exception in clicking::" + e);
        }
    }
    public static void buttonClick(WebDriver driver, WebElement element) {
        try {
            LOGGER.info("BeforeWaitForElement in buttonClick::");
            webDriverFluentWait(driver).until(ExpectedConditions.elementToBeClickable(element));
            scrollToElement(driver, element);
            highlightElement(driver, element);
            waitByTime(1000);
            element.click();
            waitTillLoadingCompletes(driver, element);
        } catch (Exception e) {
            LOGGER.error("Exception in buttonClick ::" + e);
        }
    }

    public static void doubleClick(WebDriver driver, By locator) {
        Actions actions = new Actions(driver);
        waitUntilClickByLocator(driver, locator);
        waitByTime(1000);
        WebElement elementLocator = driver.findElement(locator);
        if (!isElementVisible(driver, locator)) {
            scrollToView(driver, locator);
        }
        waitUntilClickByLocator(driver, locator);
        if (isElementVisible(driver, locator)) {
            highlightElement(driver, locator);
            actions.moveToElement(elementLocator).build();
            actions.doubleClick(elementLocator).build().perform();
        } else {
            waitUntilClickByLocator(driver, locator);
            highlightElement(driver, locator);
            actions.moveToElement(elementLocator).build();
            actions.doubleClick(elementLocator).build().perform();
        }
        waitTillLoadingCompletes(driver, driver.findElement(locator));
    }

    public static void singleClick(WebDriver driver, By locator) {
        Actions actions = new Actions(driver);
        waitUntilClickByLocator(driver, locator);
        waitByTime(1000);
        WebElement elementLocator = driver.findElement(locator);
        if (!isElementVisible(driver, locator)) {
            scrollToView(driver, locator);
        }
        waitUntilClickByLocator(driver, locator);
        if (isElementVisible(driver, locator)) {
            actions.moveToElement(elementLocator).build();
            highlightElement(driver, locator);
            actions.click(elementLocator).build().perform();
        } else {
            waitUntilClickByLocator(driver, locator);
            highlightElement(driver, locator);
            actions.moveToElement(elementLocator).build();
            actions.click(elementLocator).build().perform();
        }
        waitTillLoadingCompletes(driver, driver.findElement(locator));
    }

    public static void clickWithMultipleTimes(WebDriver driver, By locator, int clicks) {
        try {
            WebElement elm = waitUntilClickByLocator(driver, locator);
            LOGGER.info("BeforeWaitForElement in buttonClick ::" + locator);
            if (!isElementVisible(driver, locator)) {
                scrollToView(driver, locator);
            }
            for (int i = 0; i < clicks; i++) {
                waitUntilClickByLocator(driver, locator);
                if (isElementVisible(driver, locator)) {
                    elm.click();
                } else {
                    waitUntilClickByLocator(driver, locator);
                    elm.click();
                }
                waitTillLoadingCompletes(driver, driver.findElement(locator));
            }
            waitByTime(2000);
        } catch (Exception e) {
            LOGGER.error("Exception in clickWithMultipleTimes::" + e);
        }
    }


    /**
     * -------------------------------utlitity Methods for Entering Value in Input Type Fields--------------------------
     */

    public static void clearAndEnterText(WebDriver driver, By locator, String text) {
        LOGGER.info("Before clearAndEnterText::" + locator + ", with text: " + text);
        WebElement webElementEnter = waitUntilClickByLocator(driver, locator);
        if (!isElementVisible(driver, locator)) {
            scrollToElement(driver, webElementEnter);
        }
        highlightElement(driver, locator);
        webElementEnter.click();
        webElementEnter.clear();
        String value = webElementEnter.getAttribute("value");
        if (!value.isEmpty()) {
            webElementEnter.sendKeys(Keys.CONTROL + "a");
            webElementEnter.sendKeys(Keys.DELETE);
        }
        Actions action = new Actions(driver);
        action.sendKeys(webElementEnter, text).build().perform();
    }

    public static void clearAndEnterValue(WebDriver driver, By locator, String text) {
        LOGGER.info("Before clearAndEnterText::" + locator + ", with text: " + text);
        WebElement webElementEnter = waitUntilClickByLocator(driver, locator);
        if (!isElementVisible(driver, locator)) {
            scrollToElement(driver, webElementEnter);
        }
        highlightElement(driver, locator);
        webElementEnter.click();
        webElementEnter.clear();
        Actions action = new Actions(driver);
        LOGGER.info("entering with text: " + text);
        action.sendKeys(webElementEnter, text).build().perform();
    }

    public static void enterText(WebDriver driver, By locator, String text) {
        LOGGER.info("Before enterText::" + locator + ", with text::" + text);
        if (!isElementVisible(driver, locator)) {
            scrollToView(driver, locator);
        }
        WebElement webElementEnter = waitUntilClickByLocator(driver, locator);
        highlightElement(driver, locator);
        webElementEnter.sendKeys(text);
    }

    public static void enterText(WebDriver driver, WebElement element, String text) {
        LOGGER.info("Before enterText::" + element + ", with text::" + text);
        if (!isElementVisible(driver, element)) {
            scrollToElement(driver, element);
        }
        highlightElement(driver, element);
        element.sendKeys(text);
    }

    /**
     * -------------------------------utlitity Methods to check Alert present or not--------------------------
     */


    public static boolean isAlertPresent(WebDriver driver) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            throw new NoAlertPresentException();
        }
    }


    /**
     * -------------------------------utlitity Methods for checking  whether element visible or Displaying --------------------------
     */

    public static boolean isElementPresent(WebDriver driver, By locator) {
        try {
            LOGGER.info("Before isElementPresent::" + locator);
            waitUntilVisibleByLocator(driver,locator);
            driver.findElement(locator);
            return true;
        } catch (Exception e) {
            LOGGER.info("Exception isElementPresent::" + locator + " " + e);
            return false;
        }
    }

    public static boolean isElementVisible(WebDriver driver, By locator) {
        try {
            webDriverFluentWait(driver).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
            return true;
        } catch (Exception e) {
            LOGGER.info("Exception isElementVisible::" + locator + " " + e);
            return false;
        }
    }

    public static boolean isElementVisible(WebDriver driver, WebElement element) {
        try {
            webDriverFluentWait(driver).until(ExpectedConditions.visibilityOf(element));
            return true;
        } catch (Exception e) {
            LOGGER.info("Exception isElementVisible::" + element + " " + e);
            return false;
        }
    }

    public static boolean isElementVisible(WebDriver driver, By locator, int time) {
        try {
            webDriverFluentWait(driver, time).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
            return true;
        } catch (Exception e) {
            LOGGER.info("Exception isElementVisible::" + locator + " " + e);
            return false;
        }
    }

    /**
     * -------------------------------utlitity Methods scrolling to particular element --------------------------
     */
    public static void scrollToView(WebDriver driver, By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        WebElement element = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public static void scrollToElement(WebDriver driver, WebElement webelement) {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].scrollIntoView(true);", webelement);
        jse.executeScript("window.scrollBy(0,100)", "");
        LOGGER.info("ScrollToElement::" + webelement + "Done");
    }

    /**
     * -------------------------------utlitity Methods for Handling Checkboxes --------------------------
     */

    public static void uncheckCheckbox(WebDriver driver, By locator) {
        waitUntilClickByLocator(driver, locator);
        if (Boolean.TRUE.equals(driver.findElement(locator).isSelected())) {
            highlightElement(driver, locator);
            clickWithWait(driver, locator);
        }
    }

    public static void checkCheckbox(WebDriver driver, By locator) {
        waitUntilClickByLocator(driver, locator);
        if (Boolean.FALSE.equals(driver.findElement(locator).isSelected())) {
            highlightElement(driver, locator);
            clickWithWait(driver, locator);
        }
    }


    /**
     * -------------------------------utlitity Methods for Handling Checkboxes --------------------------
     */

    public static void highlightElement(WebDriver driver, WebElement webElement) {
        try {
            if (driver instanceof JavascriptExecutor) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].style.border='4px solid orange'", webElement);
            }
        } catch (Exception e) {
            LOGGER.error("Fail in highlightElement " + e);
        }
    }

    public static void highlightElement(WebDriver driver, By locator) {
        highlightElement(driver, driver.findElement(locator));
    }

    public static void insertScreenshot(final WebDriver driver,final  ScenarioContext scenario){
        scenario.getScenario().attach(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES), "image/png", "screenshot");

    }

}
