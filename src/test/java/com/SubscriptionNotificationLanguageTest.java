package com;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.javaTestClasses.SubscriptionFormFiller;
import com.javaTestClasses.SubscriptionNotificationChecker;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SubscriptionNotificationLanguageTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private SubscriptionFormFiller formFiller;
    private SubscriptionNotificationChecker notificationChecker;

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        formFiller = new SubscriptionFormFiller(driver, wait);
        notificationChecker = new SubscriptionNotificationChecker(driver, wait);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testSubscriptionNotificationLanguageIssue() throws InterruptedException {
        // 1. Перейти на укр. версію сайту
        driver.get("https://soncesad.com");
        wait.until(webDriver -> driver.findElement(org.openqa.selenium.By.tagName("body")).isDisplayed());
        Thread.sleep(1500);

        // 2. Заповнити форму розсилки
        formFiller.fillAndSubmitSubscriptionForm();

        // 3. Чекати 1-2 секунди для появи нотифікації
        Thread.sleep(1000);

        // 4. Перевірити, що нотифікація з'явилась
        boolean notificationPresent = notificationChecker.isNotificationPresent();
        assertTrue(notificationPresent, "Нотифікація про підписку не з'явилась");

        // 5. Отримати текст нотифікації
        String notificationText = notificationChecker.getNotificationMessage();
        assertNotNull(notificationText, "Текст нотифікації не можна отримати");

        System.out.println("Текст нотифікації: " + notificationText);

        // 6. Перевірити, що текст містить російське повідомлення
        // (Дефект: повідомлення повинно бути українською, але з'являється російською)
        assertTrue(notificationText.contains("Письмо для активации подписки отправлено на указанный E-mail"),
                "Нотифікація містить російське повідомлення замість українського. " +
                "Текст: " + notificationText);

        System.out.println("Дефект підтверджено: Нотифікація відображається російською мовою на укр. версії");
    }
}

