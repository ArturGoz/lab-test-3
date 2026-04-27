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
import com.javaTestClasses.PriceFilter;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PriceFilterTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private PriceFilter priceFilter;

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Ініціалізація нашого класу для роботи з ціною
        priceFilter = new PriceFilter(driver, wait);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testNegativePriceAssertion() throws InterruptedException {
        driver.get("https://soncesad.com/katalog/");

        // Вводимо -1000 за допомогою JS (це оминає помилку NotInteractable)
        String expectedValue = "-1000";
        priceFilter.forceSetNegativePrice(expectedValue);

        // Невелика пауза, щоб побачити результат (можна прибрати потім)
        Thread.sleep(2000);

        // Отримуємо те, що реально записалося в поле
        String actualValue = priceFilter.getMinPriceValue();

        System.out.println("Очікувано: " + expectedValue);
        System.out.println("Фактично в полі: " + actualValue);

        // Assert: якщо фактичне значення -1000, тест пройде.
        // Якщо система автоматично змінила його на 0 (виправила баг), тест впаде.
        assertEquals(expectedValue, actualValue,
                "Система мала б дозволити введення -1000 (або ми перевіряємо наявність багу)");
    }
}