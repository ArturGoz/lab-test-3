package com.javaTestClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.JavascriptExecutor;

/**
 * Клас для перемикання мови на сайті https://soncesad.com/
 * Підтримує українську та російську мови
 */
public class LanguageSwitcher {
    
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;
    
    // Селектори для перемикання мови
    private static final By RUS_LINK = By.cssSelector(".header__lang a.lang.ru, a.lang.ru[href*='/ru']");
    private static final By UK_LINK = By.cssSelector(".header__lang a.lang.uk, a.lang.uk[href*='/uk'], .header__lang a.lang.ua, a.lang.ua[href*='/ua']");
    
    public LanguageSwitcher(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.js = (JavascriptExecutor) driver;
    }
    
    /**
     * Переключитися на українську мову
     */
    public void switchToUkrainian() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(UK_LINK));
            driver.findElement(UK_LINK).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        } catch (Exception e) {
            // Fallback: navigate directly to /uk/ URL
            try {
                driver.get(driver.getCurrentUrl().split("\\?")[0].replaceAll("/ru/|/$", "/uk/"));
                wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
            } catch (Exception ignored) {
            }
        }
    }
    
    /**
     * Переключитися на російську мову
     */
    public void switchToRussian() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(RUS_LINK));
            driver.findElement(RUS_LINK).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        } catch (Exception e) {
            // Fallback: navigate directly to /ru/ URL
            try {
                driver.get(driver.getCurrentUrl().split("\\?")[0].replaceAll("/uk/|/$", "/ru/"));
                wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
            } catch (Exception ignored) {
            }
        }
    }
    
    /**
     * Переключитися на вказану мову за мовним кодом
     * @param languageCode - код мови: "uk", "ru"
     */
    public void switchLanguage(String languageCode) {
        if ("uk".equalsIgnoreCase(languageCode)) {
            switchToUkrainian();
        } else if ("ru".equalsIgnoreCase(languageCode)) {
            switchToRussian();
        } else {
            throw new IllegalArgumentException("Невідомий код мови: " + languageCode);
        }
    }
    
    /**
     * Закрити куки повідомлення (якщо воно з'явиться)
     */
    public void closeCookieNotice() {
        try {
            WebElement cookie = driver.findElement(By.cssSelector("button.cookie-accept, .cookie__accept, .cc_btn_accept_all"));
            if (cookie.isDisplayed()) {
                cookie.click();
            }
        } catch (Exception ignored) {
        }
    }
}

