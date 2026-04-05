package com.javaTestClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Клас для перевірки повідомлень про активацію підписки
 * Знаходить та повертає текст повідомлення з div.jGrowl-message
 */
public class SubscriptionNotificationChecker {
    
    private WebDriver driver;
    private WebDriverWait wait;
    
    // Селектори для повідомлення про активацію підписки
    private static final By NOTIFICATION_MESSAGE = By.cssSelector("div.jGrowl-notification div.jGrowl-message");
    private static final By NOTIFICATION_CONTAINER = By.cssSelector("div.jGrowl-notification.alert.ui-state-highlight.af-message-success");
    
    public SubscriptionNotificationChecker(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }
    
    /**
     * Отримати текст повідомлення про активацію підписки
     * @return текст повідомлення
     */
    public String getNotificationMessage() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(NOTIFICATION_MESSAGE));
            WebElement messageElement = driver.findElement(NOTIFICATION_MESSAGE);
            return messageElement.getText();
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Перевірити, чи повідомлення про активацію підписки присутнє
     * @return true якщо повідомлення видиме, false якщо ні
     */
    public boolean isNotificationPresent() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(NOTIFICATION_CONTAINER));
            WebElement notification = driver.findElement(NOTIFICATION_CONTAINER);
            return notification.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Перевірити, чи повідомлення є успішним (має клас af-message-success)
     * @return true якщо повідомлення успішне
     */
    public boolean isSuccessMessage() {
        try {
            WebElement notification = driver.findElement(NOTIFICATION_CONTAINER);
            String classAttribute = notification.getAttribute("class");
            return classAttribute.contains("af-message-success");
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Закрити повідомлення
     */
    public void closeNotification() {
        try {
            WebElement closeButton = driver.findElement(By.cssSelector("button.jGrowl-close"));
            closeButton.click();
        } catch (Exception ignored) {
        }
    }
}

