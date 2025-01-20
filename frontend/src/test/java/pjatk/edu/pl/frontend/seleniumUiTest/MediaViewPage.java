package pjatk.edu.pl.frontend.seleniumUiTest;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MediaViewPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(id = "mediaListSelect")
    public Select selectMediaList;

    @FindBy(id = "addMediaToListSubmit")
    public WebElement buttonAddMediaListSubmit;

    public MediaViewPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        PageFactory.initElements(driver, this);
    }

    public MediaViewPage open(int mediaId) {
        driver.get("http://localhost:8080/anime/" + mediaId);
        return this;
    }

    public MediaViewPage selectLastOptionFromMediaListSelect() {
        wait.withTimeout(Duration.ofSeconds(3));
        selectMediaList.getOptions().getLast().click();
        return this;
    }

    public MediaViewPage submitForm() {
        buttonAddMediaListSubmit.click();
        return this;
    }
}
