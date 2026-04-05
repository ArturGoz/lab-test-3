package com;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.javaTestClasses.CatalogNavigator;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CatalogPriceFilterTranslationTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private CatalogNavigator catalogNavigator;

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        catalogNavigator = new CatalogNavigator(driver, wait);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testPriceFilterTranslationUkrainian() throws InterruptedException {
        // 1. Відкрити каталог цибулі
        catalogNavigator.openOnionCatalog();
        Thread.sleep(1000);

        // 2. Знайти елементи з текстом "From" та "To"
        List<WebElement> labels = driver.findElements(By.cssSelector("label"));

        WebElement fromLabel = null;
        WebElement toLabel = null;

        for (WebElement label : labels) {
            String text = label.getText().trim();
            if (text.toLowerCase().startsWith("from")) {
                fromLabel = label;
            }
            if (text.toLowerCase().startsWith("to")) {
                toLabel = label;
            }
        }

        // 3. Перевірити, що елементи знайдені
        assertTrue(fromLabel != null, "Елемент 'From' не знайдений");
        assertTrue(toLabel != null, "Елемент 'To' не знайдений");

        // 4. Отримати текст з елементів
        String fromText = fromLabel.getText();
        String toText = toLabel.getText();

        System.out.println("From label text: " + fromText);
        System.out.println("To label text: " + toText);

        // 5. Перевірити, що текст перекладений українською
        assertNotEquals("Від", fromText.trim(), "Текст 'From' повинен бути перекладений на 'Від'");
        assertNotEquals("До", toText.trim(), "Текст 'To' повинен бути перекладений на 'До'");
    }
}

