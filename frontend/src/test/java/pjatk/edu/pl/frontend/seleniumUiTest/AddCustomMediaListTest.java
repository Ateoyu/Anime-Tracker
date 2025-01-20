package pjatk.edu.pl.frontend.seleniumUiTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class AddCustomMediaListTest {
    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        this.driver = new ChromeDriver();
    }

    @AfterEach
    public void tearDown() {
        this.driver.close();
    }

    @Test
    public void addCustomMediaListTest() {
        String title = "Test Title";
        String description = "Test Description";
        AddCustomMediaListPage addCustomMediaListPage = new AddCustomMediaListPage(driver)
                .open()
                .inputTitle(title)
                .inputDescription(description)
                .submitForm();

        assertTrue(addCustomMediaListPage.isTableVisible(), "Table should be visible");
        assertTrue(addCustomMediaListPage.isLastChildCorrect(title, description), "Last row should contain the newly added media list");
    }
}
