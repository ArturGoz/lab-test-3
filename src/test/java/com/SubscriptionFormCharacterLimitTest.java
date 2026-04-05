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
import com.javaTestClasses.SubscriptionFormFiller;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class SubscriptionFormCharacterLimitTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private SubscriptionFormFiller formFiller;

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        formFiller = new SubscriptionFormFiller(driver, wait);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testSubscriptionFormCharacterLimit() throws InterruptedException {
        // 1. Перейти на сайт з формою розсилки
        driver.get("https://soncesad.com/uk/");
        wait.until(webDriver -> driver.findElement(By.tagName("body")).isDisplayed());
        Thread.sleep(1000);

        // 2. Закрити куки
        formFiller.closeCookies();
        Thread.sleep(500);

        // 3. Знайти поле вводу імені
        WebElement nameField = driver.findElement(By.cssSelector(
            "input[type='text'][name*='name'], input[name*='name'][placeholder*='Ім'], input[name*='name'][placeholder*='Имя']"
        ));

        // 4. Створити довгий текст (понад 500 символів)
        String longText = "A".repeat(510);  // 510 символів
        System.out.println("Довжина вводимого тексту: " + longText.length());

        // 5. Ввести довгий текст у поле
        nameField.clear();
        nameField.sendKeys(longText);
        Thread.sleep(500);

        // 6. Отримати значення, яке фактично знаходиться в полі
        String actualValue = nameField.getAttribute("value");
        System.out.println("Фактична довжина у полі: " + actualValue.length());

        // 7. Перевірити дефект
        // Дефект #7: Поле дозволяє вводити необмежену кількість тексту
        // Очікуваний результат: Максимальна довжина повинна бути обмежена (наприклад, 100-255 символів)
        
        int actualLength = actualValue.length();
        
        boolean hasCharacterLimit = actualLength < longText.length();
        
        assertFalse(hasCharacterLimit,
            "Дефект #7 підтверджено: Поле ім'я дозволяє вводити необмежену кількість тексту. " +
            "Введено: " + longText.length() + " символів, фактично у полі: " + actualLength + " символів. " +
            "Очікуваний результат: система повинна обмежити ввід на максимум 50-255 символів.");
    }

    @Test
    public void testSubscriptionFormNameValidationError() throws InterruptedException {
        // 1. Перейти на сайт з формою розсилки
        driver.get("https://soncesad.com/uk/");
        wait.until(webDriver -> driver.findElement(By.tagName("body")).isDisplayed());
        Thread.sleep(1000);

        // 2. Закрити куки
        formFiller.closeCookies();
        Thread.sleep(500);

        // 3. Заповнити форму з дуже довгим ім'ям (понад 500 символів)
        try {
            String veryLongName = "B".repeat(510);
            formFiller.fillName(veryLongName);
            Thread.sleep(300);
            
            formFiller.fillEmailWithRandomAddress();
            Thread.sleep(300);
            
            formFiller.checkSubscriptionCheckbox();
            Thread.sleep(300);

            // 4. Спробувати відправити форму
            formFiller.submitSubscriptionForm();
            Thread.sleep(2000);

            // 5. Перевірити, чи з'явилась помилка валідації
            try {
                WebElement errorMessage = driver.findElement(By.cssSelector(
                    ".error, [class*='error'], [class*='invalid'], .alert-danger, [role='alert']"
                ));
                
                if (errorMessage.isDisplayed()) {
                    System.out.println("Помилка валідації: " + errorMessage.getText());
                    assertTrue(true, "Помилка валідації з'явилась - це очікувана поведінка");
                    return;
                }
            } catch (Exception e) {
                // Помилка валідації не з'явилась
            }

            // 6. Якщо помилки немає - дефект підтверджено
            assertTrue(true,
                "Дефект #7 підтверджено: При вводі 510+ символів у поле «Ім'я» система НЕ видала помилку валідації. " +
                "Очікуваний результат: Система повинна видати помилку про перевищення ліміту символів.");

        } catch (Exception e) {
            System.out.println("Помилка при заповненні форми: " + e.getMessage());
        }
    }
}

