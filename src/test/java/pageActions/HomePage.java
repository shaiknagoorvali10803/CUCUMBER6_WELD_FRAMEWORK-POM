package pageActions;

import Utils.WebDriverProvider;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
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
public class HomePage {
@Inject
    WebDriverProvider driverProvider;
    private WebDriverWait wait;
    @FindBy(name = "q")
    private WebElement searchBox;
    @FindBy(name = "btnK")
    private List<WebElement> searchBtns;
    @PostConstruct
    public void setUp() {
        PageFactory.initElements(driverProvider.getInstance(), this);
    }

    public void goTo(){
        driverProvider.getInstance().get("https://www.google.com");
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

   public boolean isAt() {
        return this.wait.until((d) -> this.searchBox.isDisplayed());
    }

}
