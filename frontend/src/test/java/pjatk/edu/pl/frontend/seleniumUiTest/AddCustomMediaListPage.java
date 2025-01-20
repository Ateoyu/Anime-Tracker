package pjatk.edu.pl.frontend.seleniumUiTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AddCustomMediaListPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(id = "title")
    public WebElement inputTitle;

    @FindBy(id = "description")
    public WebElement inputDescription;

    @FindBy(id = "createAnimeListSubmit")
    public WebElement inputCreateAnimeListSubmit;

    @FindBy(id = "animeListTable")
    public WebElement tableAnimeList;

    @FindBy(css = "html > body > main > section > table > tbody > tr:last-child")
    public WebElement lastChildOfTableAnimeList;


    public AddCustomMediaListPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        PageFactory.initElements(driver, this);
    }

    public AddCustomMediaListPage open() {
        this.driver.get("http://localhost:8080/animeLists");
        return this;
    }

    public AddCustomMediaListPage inputTitle(String title) {
        inputTitle.sendKeys(title);
        return this;
    }

    public AddCustomMediaListPage inputDescription(String description) {
        inputDescription.sendKeys(description);
        return this;
    }

    public AddCustomMediaListPage submitForm() {
        inputCreateAnimeListSubmit.click();
        return new AddCustomMediaListPage(driver);
    }

    public CustomMediaListViewPage clickLastMediaList() {
         lastChildOfTableAnimeList.findElement(By.cssSelector("a")).click();
         return new CustomMediaListViewPage(driver);
    }

    public boolean isTableVisible() {
        return tableAnimeList.isDisplayed();
    }

    public boolean isLastChildCorrect(String expectedTitle, String expectedDescription) {
        WebElement lastRow = wait.until(ExpectedConditions.visibilityOf(lastChildOfTableAnimeList));
        return lastRow.getText().contains(expectedTitle) &&
                lastRow.getText().contains(expectedDescription);
    }


}
