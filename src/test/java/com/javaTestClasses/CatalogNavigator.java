package com.javaTestClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Клас для навігації на каталог товарів на сайті https://soncesad.com/
 * Дозволяє відкривати різні сторінки каталогу
 */
public class CatalogNavigator {
    
    private WebDriver driver;
    private WebDriverWait wait;
    
    private static final String CATALOG_BASE_URL = "https://soncesad.com/katalog/";
    
    public CatalogNavigator(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }
    
    /**
     * Перейти на сторінку каталогу цибулі
     */
    public void openOnionCatalog() {
        navigateToCatalog("czibulini");
    }
    
    /**
     * Перейти на вказаний каталог товарів
     * @param catalogSlug - URL slug каталогу (наприклад: "czibulini", "bulbi", "nasinnya")
     */
    public void navigateToCatalog(String catalogSlug) {
        if (catalogSlug == null || catalogSlug.isEmpty()) {
            throw new IllegalArgumentException("Slug каталогу не може бути порожним");
        }
        
        String catalogUrl = CATALOG_BASE_URL + catalogSlug + "/";
        driver.get(catalogUrl);
        
        // Очікуємо завантаження сторінки
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
    }
    
    /**
     * Перейти на каталог за повним URL
     * @param fullUrl - повний URL каталогу
     */
    public void navigateToCatalogByUrl(String fullUrl) {
        if (fullUrl == null || fullUrl.isEmpty()) {
            throw new IllegalArgumentException("URL не може бути порожним");
        }
        
        driver.get(fullUrl);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
    }
    
    /**
     * Отримати поточний URL каталогу
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}

