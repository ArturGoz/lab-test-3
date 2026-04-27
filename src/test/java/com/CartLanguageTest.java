package com;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.javaTestClasses.CartManager;
import com.javaTestClasses.LanguageSwitcher;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CartLanguageTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private CartManager cartManager;
    private LanguageSwitcher languageSwitcher;

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        cartManager = new CartManager(driver, wait);
        languageSwitcher = new LanguageSwitcher(driver, wait);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testCartPersistsAfterLanguageSwitch() throws InterruptedException {
        // 1. Перейти на сайт з українською мовою
        driver.get("https://soncesad.com");
        wait.until(webDriver -> driver.findElement(org.openqa.selenium.By.tagName("body")).isDisplayed());
        Thread.sleep(1000);
        
        // Закрити kuки повідомлення, якщо є
        languageSwitcher.closeCookieNotice();

        // 2. Додати товар в кошик
        boolean productAdded = cartManager.addFirstProduct();
        System.out.println("Товар додано: " + productAdded);
        Thread.sleep(1000);

        // 3. Перевірити, що в кошику є 1 товар
        int cartCountBeforeLangSwitch = cartManager.getCartCount();
        System.out.println("Кількість товарів в кошику до зміни мови: " + cartCountBeforeLangSwitch);
        assertEquals(1, cartCountBeforeLangSwitch, "Кошик повинен містити 1 товар");

        // 4. Змінити мову на російську
        languageSwitcher.switchToRussian();
        Thread.sleep(1500);

        // 5. Перевірити, що товар залишився в кошику
        int cartCountAfterLangSwitch = cartManager.getCartCount();
        System.out.println("Кількість товарів в кошику після зміни мови: " + cartCountAfterLangSwitch);
        assertNotEquals(1, cartCountAfterLangSwitch, "Кошик повинен містити 1 товар навіть після зміни мови");
    }
}

