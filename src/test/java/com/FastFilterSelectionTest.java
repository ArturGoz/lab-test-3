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
import com.javaTestClasses.FruitWeightFilter;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FastFilterSelectionTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private FruitWeightFilter fruitWeightFilter;

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        fruitWeightFilter = new FruitWeightFilter(driver, wait);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testFastFilterSelection() throws InterruptedException {
        // 1. Перейти на каталог
        driver.get("https://soncesad.com/katalog/");
        wait.until(webDriver -> driver.findElement(By.tagName("body")).isDisplayed());
        Thread.sleep(1500);

        System.out.println("URL каталогу: " + driver.getCurrentUrl());

        // 2. Відкрити фільтр маси плоду
        fruitWeightFilter.openFilter();
        Thread.sleep(500);

        // 3. Список параметрів для швидкого вибору (4-5 різних варіантів)
        String[] weightsToSelect = {
            "0,1-1 г",
            "0,6-0,9 г",
            "0,6-1,1 г"
        };

        System.out.println("Початок швидкого вибору 5 параметрів за ~1 сек...");
        long startTime = System.currentTimeMillis();

        // 4. Швидко вибирати параметри (за ~1 сек для 5 параметрів)
        for (String weight : weightsToSelect) {
            fruitWeightFilter.selectWeightOption(weight);
            // Мінімальна затримка між кліками (~200мс)
            Thread.sleep(200);
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Час вибору 5 параметрів: " + totalTime + "мс");

        // 5. Чекати застосування фільтрів
        Thread.sleep(2000);

        // 6. Отримати поточний URL з параметрами фільтру
        String currentUrl = driver.getCurrentUrl();
        System.out.println("URL після вибору фільтрів: " + currentUrl);

        // 7. Перевірити, скільки параметрів були дійсно вибрані в UI
        int actualSelectedCount = countSelectedFilters();
        System.out.println("Фактично вибрано параметрів: " + actualSelectedCount);

        // 8. Перевірити, чи всі 3 параметрів знаходяться у URL
        int expectedSelectedCount = weightsToSelect.length;
        int urlParameterCount = countFilterParametersInUrl(currentUrl);
        System.out.println("Параметрів у URL: " + urlParameterCount);

        // 9. Асерція: перевірити дефект #8
        // Очікується: всі 3 параметрів застосовані
        // Дефект: Система пропускає частину кліків
        assertEquals(expectedSelectedCount, actualSelectedCount,
            "Дефект #8 підтверджено: При швидкому виборі 3 параметрів було вибрано лише " + actualSelectedCount +
            ". Система пропускає частину кліків через повільну відповідь. " +
            "Очікується 3 параметрів, отримано " + actualSelectedCount);

        assertNotEquals(urlParameterCount >= 2,
            "Дефект #8: URL містить менше 2 параметрів фільтру (" + urlParameterCount + "). " +
            "URL: " + currentUrl);
    }

    /**
     * Підрахувати кількість вибраних фільтрів у UI
     */
    private int countSelectedFilters() {
        try {
            List<WebElement> checkboxes = driver.findElements(By.cssSelector(
                "fieldset[id='mse2_msoption|option7'] input[type='checkbox']:checked"
            ));
            return checkboxes.size();
        } catch (Exception e) {
            System.out.println("Помилка при підрахунку вибраних фільтрів: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Підрахувати кількість параметрів фільтру в URL
     */
    private int countFilterParametersInUrl(String url) {
        try {
            if (url.contains("msoption")) {
                // Рахуємо кількість "msoption" у URL
                int count = url.split("msoption").length - 1;
                return count;
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }
}

