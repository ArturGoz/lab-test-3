package com.javaTestClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.Keys;

import java.util.List;

/**
 * Клас для пошуку товарів на сайті https://soncesad.com/
 * Дозволяє виконувати пошук за назвою товару
 */
public class ProductSearch {
    
    private WebDriver driver;
    private WebDriverWait wait;
    
    // Селектори для пошуку
    private static final By SEARCH_INPUT = By.cssSelector("input[name*='search'], input[id*='search'], input[placeholder*='поиск'], input[placeholder*='пошук']");
    private static final By SEARCH_BUTTON = By.cssSelector("button[type='submit'], [class*='search-btn'], .search-button");
    private static final By SEARCH_SUGGESTIONS = By.cssSelector("[class*='dropdown'], [class*='suggestions'], [class*='autocomplete']");
    private static final By SEARCH_RESULTS_HEADER = By.cssSelector("h1, [class*='results']");
    
    public ProductSearch(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }
    
    /**
     * Виконати пошук за назвою товару
     * @param productName - назва товару для пошуку
     */
    public void search(String productName) {
        if (productName == null || productName.isEmpty()) {
            throw new IllegalArgumentException("Назва товару не може бути порожною");
        }
        
        try {
            WebElement searchInput = wait.until(ExpectedConditions.presenceOfElementLocated(SEARCH_INPUT));
            searchInput.clear();
            searchInput.sendKeys(productName);
            
            // Small delay for suggestions to appear
            Thread.sleep(500);
            
            // Press Enter to search
            searchInput.sendKeys(Keys.ENTER);
            
            // Wait for page to reload
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        } catch (Exception e) {
            throw new RuntimeException("Помилка при виконанні пошуку: " + e.getMessage());
        }
    }
    
    /**
     * Пошук та очікування результатів
     * @param productName - назва товару
     */
    public void searchAndWaitForResults(String productName) {
        search(productName);
        
        try {
            // Wait for results to load
            wait.until(ExpectedConditions.presenceOfElementLocated(SEARCH_RESULTS_HEADER));
        } catch (Exception e) {
            // Results might not have header, just wait for page to be ready
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        }
    }
    
    /**
     * Перевірити, чи пошук повернув результати
     */
    public boolean hasSearchResults() {
        try {
            List<WebElement> results = driver.findElements(By.cssSelector(".product, [class*='product-item'], [class*='item']"));
            return !results.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Отримати кількість результатів пошуку
     */
    public int getSearchResultCount() {
        try {
            List<WebElement> results = driver.findElements(By.cssSelector(".product, [class*='product-item'], [class*='item']"));
            return results.size();
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Отримати першу посилання на товар з результатів
     */
    public WebElement getFirstResultLink() {
        try {
            return driver.findElement(By.cssSelector(".product a, [class*='product-item'] a, [class*='item'] a"));
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Очистити поле пошуку
     */
    public void clearSearch() {
        try {
            WebElement searchInput = driver.findElement(SEARCH_INPUT);
            searchInput.clear();
        } catch (Exception ignored) {
        }
    }
    
    /**
     * Отримати значення, що введено у пошук
     */
    public String getSearchInputValue() {
        try {
            WebElement searchInput = driver.findElement(SEARCH_INPUT);
            return searchInput.getAttribute("value");
        } catch (Exception e) {
            return "";
        }
    }
}

