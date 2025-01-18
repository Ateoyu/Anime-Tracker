package pjatk.edu.pl.frontend.seleniumUiTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class AddMediaToCustomMediaListTest {
    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
    }

    @AfterEach
    public void tearDown() {
        driver.close();
    }

    @Test
    public void addMediaToCustomMediaList() {
        int mediaId = 99426;

        AddCustomMediaListPage addInitialTestList = new AddCustomMediaListPage(driver)
                .open()
                .inputTitle("Add Media To List Test")
                .inputDescription("Add Media To List Test")
                .submitForm();

        MediaViewPage mediaViewPage = new MediaViewPage(driver)
                .open(mediaId)
                .selectLastOptionFromMediaListSelect()
                .submitForm();

        CustomMediaListViewPage customMediaListViewPage = new AddCustomMediaListPage(driver).clickLastMediaList();

        assertTrue(driver.findElement(By.xpath("//*[text() = 'A Place Further Than the Universe']")).isDisplayed());
    }
}
