package pjatk.edu.pl.frontend.seleniumUiTest;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CustomMediaListViewPage {
    private final WebDriver driver;


    @FindBy(id = "mainGridContainer")
    public WebElement mainGridContainer;

    public CustomMediaListViewPage(WebDriver driver) {
        this.driver = driver;

        PageFactory.initElements(driver, this);
    }
}
