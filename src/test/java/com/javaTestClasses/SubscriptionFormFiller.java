package com.javaTestClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.JavascriptExecutor;

import java.util.Random;

/**
 * Клас для заповнення та подання форми розсилки на сайті https://soncesad.com/
 * Генерує рандомну почту та підтверджує розсилку
 */
public class SubscriptionFormFiller {
    
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;
    private Random random;
    
    // Селектори форми розсилки
    private static final By EMAIL_INPUT = By.cssSelector("input[type='email'], input[name*='email'], input[placeholder*='email'], input[placeholder*='пошта'], input[placeholder*='почта']");
    private static final By NAME_INPUT = By.cssSelector("input[type='text'][name*='name'], input[name*='name'][placeholder*='Ім'], input[name*='name'][placeholder*='Имя']");
    private static final By CHECKBOX = By.cssSelector("input[type='checkbox']");
    private static final By SUBMIT_BUTTON = By.xpath("//div[contains(@class, 'offset-md-3')]//button[@type='submit'], //button[contains(., 'Підписатися')]");
    
    private static final String FIXED_NAME = "John Doe";
    private static final String EMAIL_DOMAIN = "@gmailc.om";
    
    public SubscriptionFormFiller(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.js = (JavascriptExecutor) driver;
        this.random = new Random();
    }
    
    /**
     * Генерувати рандомну почту формату artN@gmailc.om (N від 1 до 1000)
     * @return рандомна почта
     */
    public String generateRandomEmail() {
        int randomNumber = random.nextInt(1000) + 1;
        return "art" + randomNumber + EMAIL_DOMAIN;
    }
    
    /**
     * Заповнити поле імені
     * @param name - ім'я для заповнення
     */
    public void fillName(String name) {
        try {
            WebElement nameField = wait.until(ExpectedConditions.presenceOfElementLocated(NAME_INPUT));
            nameField.clear();
            nameField.sendKeys(name);
        } catch (Exception e) {
            System.out.println("Помилка при заповненні імені: " + e.getMessage());
        }
    }
    
    /**
     * Заповнити поле почти рандомною поштою
     */
    public void fillEmailWithRandomAddress() {
        String randomEmail = generateRandomEmail();
        fillEmail(randomEmail);
        System.out.println("Заповнена почта: " + randomEmail);
    }
    
    /**
     * Заповнити поле почти
     * @param email - почта для заповнення
     */
    public void fillEmail(String email) {
        try {
            WebElement emailField = wait.until(ExpectedConditions.presenceOfElementLocated(EMAIL_INPUT));
            emailField.clear();
            emailField.sendKeys(email);
        } catch (Exception e) {
            System.out.println("Помилка при заповненні почти: " + e.getMessage());
        }
    }
    
    /**
     * Закрити куки та інші модальні вікна
     */
    public void closeCookies() {
        try {
            WebElement cookie = driver.findElement(By.cssSelector("button.cookie-accept, .cookie__accept, .cc_btn_accept_all, [class*='accept']"));
            if (cookie.isDisplayed()) {
                cookie.click();
            }
        } catch (Exception ignored) {
        }
    }
    
    /**
     * Чекати та натиснути чекбокс для розсилки
     */
    public void checkSubscriptionCheckbox() {
        try {
            WebElement checkbox = wait.until(ExpectedConditions.presenceOfElementLocated(CHECKBOX));
            scrollIntoView(checkbox);
            
            // Перевірити, чи не вже чекнута
            if (!checkbox.isSelected()) {
                clickElement(checkbox);
                System.out.println("Чекбокс розсилки чекнута");
            }
        } catch (Exception e) {
            System.out.println("Помилка при натисканні чекбокса: " + e.getMessage());
        }
    }
    
    /**
     * Знайти та натиснути кнопку підтвердження розсилки
     */
    public void submitSubscriptionForm() {
        try {
            // Спробуємо знайти кнопку за основним селектором
            WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(SUBMIT_BUTTON));
            scrollIntoView(submitButton);
            clickElement(submitButton);
            System.out.println("Форма розсилки відправлена (основний селектор)");
        } catch (Exception e) {
            System.out.println("Первинна спроба не вдалась, спробую fallback селектор...");
            try {
                // Fallback: шукаємо за текстом кнопки
                By fallbackButton = By.xpath("//button[contains(., 'Підписатися')]");
                WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(fallbackButton));
                scrollIntoView(submitButton);
                clickElement(submitButton);
                System.out.println("Форма розсилки відправлена (fallback селектор)");
            } catch (Exception e2) {
                System.out.println("Помилка при відправленні форми: " + e2.getMessage());
            }
        }
    }
    
    /**
     * Заповнити форму розсилки повністю та відправити
     * Використовує фіксоване ім'я та рандомну почту
     */
    public void fillAndSubmitSubscriptionForm() throws InterruptedException {
        closeCookies();
        Thread.sleep(500);
        
        fillName(FIXED_NAME);
        Thread.sleep(300);
        
        fillEmailWithRandomAddress();
        Thread.sleep(300);
        
        checkSubscriptionCheckbox();
        Thread.sleep(300);
        
        submitSubscriptionForm();
        Thread.sleep(300);
    }
    
    private void scrollIntoView(WebElement element) {
        try {
            js.executeScript("arguments[0].scrollIntoView({block:'center',behavior:'auto'})", element);
            Thread.sleep(300);
        } catch (Exception ignored) {
        }
    }
    
    private void clickElement(WebElement element) {
        try {
            element.click();
        } catch (Exception e) {
            // Fallback to JS click
            js.executeScript("arguments[0].click();", element);
        }
    }
}

