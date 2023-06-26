package pageActions;

import Utils.ScenarioContext;
import Utils.WebDriverProvider;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

@Singleton
public class GooglePage {
    @Inject
    private WebDriverProvider driverProvider;
    private WebDriverWait wait;
    @Inject
    ScenarioContext scenarioContext;
    @FindBy(name = "q")
    private WebElement searchBox;
    @FindBy(css = "div.g")
    private List<WebElement> results;

    @FindBy(name = "btnK")
    private List<WebElement> searchBtns;
    @PostConstruct
    private void setup() {
        PageFactory.initElements(this.driverProvider.getInstance(), this);
        wait = new WebDriverWait(driverProvider.getInstance(), Duration.ofSeconds(30));
       }
    public void goTo(){
        this.driverProvider.getInstance().navigate().to("https://www.google.com");
    }
    public void search(final String keyword){
        this.searchBox.sendKeys(keyword);
        this.searchBox.sendKeys(Keys.TAB);
        this.searchBtns
                .stream()
                .filter(e -> e.isDisplayed() && e.isEnabled())
                .findFirst()
                .ifPresent(WebElement::click);
    }
    public int getCount(){
        return this.results.size();
    }
    public boolean isAt() {
        return this.wait.until((d) -> this.searchBox.isDisplayed());
    }

}
