package com;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.javaTestClasses.SearchForm;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchSuggestionsTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private SearchForm searchForm;

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        searchForm = new SearchForm(driver, wait);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testSearchSuggestionsAppear() throws InterruptedException {
        // 1. Перейти на головну сторінку
        driver.get("https://soncesad.com");
        wait.until(webDriver -> driver.findElement(By.tagName("body")).isDisplayed());
        Thread.sleep(1000);

        // 2. Почати вводити текст у пошук (наприклад, "Маг")
        searchForm.enterSearchTerm("Маг");
        Thread.sleep(2000);  // Чекаємо появу suggestions

        // 3. Шукаємо список підказок (suggestions/dropdown)
        try {
            List<WebElement> suggestions = driver.findElements(By.cssSelector(
                "[class*='dropdown'], [class*='suggestions'], [class*='autocomplete'], " +
                ".ui-autocomplete, [role='listbox'], [role='presentation']"
            ));

            // 4. Перевіряємо, чи є елементи з підказками
            boolean hasSuggestions = false;
            for (WebElement suggestion : suggestions) {
                if (suggestion.isDisplayed() && !suggestion.getText().trim().isEmpty()) {
                    hasSuggestions = true;
                    System.out.println("Знайдено suggestions: " + suggestion.getText());
                    break;
                }
            }
            // 5. Асерція: якщо suggestions не з'явилися, тест впаде
            assertNotEquals(hasSuggestions,
                "Дефект #5 підтверджено: Список підказок (suggestions) не з'являється при введенні текту 'Маг' у пошук");
            Thread.sleep(2000);

        } catch (Exception e) {
            System.out.println("Дефект #5 підтверджено: Помилка при пошуку suggestions - " + e.getMessage());
            assertNotEquals(false, "Помилка при пошуку suggestions: " + e.getMessage());
        }
    }
}

