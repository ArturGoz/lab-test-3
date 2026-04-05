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
import com.javaTestClasses.SearchForm;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmptySearchQueryTest {

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
    public void testEmptySearchQuery() throws InterruptedException {
        // 1. Перейти на головну сторінку
        String homeUrl = "https://soncesad.com";
        driver.get(homeUrl);
        wait.until(webDriver -> driver.findElement(By.tagName("body")).isDisplayed());
        Thread.sleep(1000);

        // 2. Отримати поточний URL
        String urlBefore = driver.getCurrentUrl();
        System.out.println("URL до пошуку: " + urlBefore);

        // 3. Залишити поле пошуку ПОРОЖНІМ та натиснути кнопку пошуку
        try {
            WebElement searchButton = driver.findElement(By.cssSelector(".header__search-button"));
            searchButton.click();
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("Помилка при натисканні кнопки пошуку: " + e.getMessage());
        }

        // 4. Отримати URL після спроби пошуку
        String urlAfter = driver.getCurrentUrl();
        System.out.println("URL після пошуку: " + urlAfter);

        // 5. Перевірити дефект:
        // Дефект #6: Сайт НЕ повинен переходити на search.html при порожньому запиті
        // Очікуваний результат: URL залишається без змін або дається попередження
        
        boolean hasSearchResults = urlAfter.contains("search.html") || urlAfter.contains("query=");
        
        assertTrue(hasSearchResults,
            "Дефект #6 підтверджено: При натисканні пошуку з ПОРОЖНІМ полем сайт переходить на сторінку результатів. " +
            "Очікувано: сайт повинен залишатися на поточній сторінці або видати попередження. " +
            "URL до: " + urlBefore + ", URL після: " + urlAfter);
    }
}

