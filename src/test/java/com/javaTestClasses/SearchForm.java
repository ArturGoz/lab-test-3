package com.javaTestClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.Keys;
import org.openqa.selenium.JavascriptExecutor;

/**
 * Клас для роботи з пошуком на сайті https://soncesad.com/
 * Дозволяє вводити пошукові терміни та виконувати пошук
 */
public class SearchForm {
    
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;
    
    // Селектори для пошуку
    private static final By SEARCH_INPUT = By.cssSelector("input[name='query'][placeholder*='Шукайте']");
    private static final By SEARCH_BUTTON = By.cssSelector(".header__search-button");
    private static final By SEARCH_FORM = By.id("mse2_form");
    
    public SearchForm(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.js = (JavascriptExecutor) driver;
    }
    
    /**
     * Ввести текст у поле пошуку
     * @param searchTerm - текст для пошуку
     */
    public void enterSearchTerm(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            throw new IllegalArgumentException("Пошуковий термін не може бути порожним");
        }
        
        try {
            WebElement searchInput = wait.until(ExpectedConditions.presenceOfElementLocated(SEARCH_INPUT));
            searchInput.clear();
            searchInput.sendKeys(searchTerm);
            System.out.println("Введено пошуковий термін: " + searchTerm);
        } catch (Exception e) {
            throw new RuntimeException("Помилка при введенні пошукового терміну: " + e.getMessage());
        }
    }
    
    /**
     * Натиснути кнопку пошуку
     */
    public void clickSearchButton() {
        try {
            WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(SEARCH_BUTTON));
            scrollIntoView(searchButton);
            clickElement(searchButton);
            System.out.println("Кнопка пошуку натиснута");
        } catch (Exception e) {
            throw new RuntimeException("Помилка при натисканні кнопки пошуку: " + e.getMessage());
        }
    }
    
    /**
     * Виконати пошук за текстом (ввести термін та натиснути кнопку)
     * @param searchTerm - текст для пошуку
     */
    public void search(String searchTerm) throws InterruptedException {
        enterSearchTerm(searchTerm);
        Thread.sleep(500);
        clickSearchButton();
    }
    
    /**
     * Виконати пошук за допомогою Enter
     * @param searchTerm - текст для пошуку
     */
    public void searchByEnter(String searchTerm) throws InterruptedException {
        enterSearchTerm(searchTerm);
        Thread.sleep(500);
        
        WebElement searchInput = driver.findElement(SEARCH_INPUT);
        searchInput.sendKeys(Keys.ENTER);
        System.out.println("Пошук виконаний за допомогою Enter");
    }
    
    /**
     * Очистити поле пошуку
     */
    public void clearSearchInput() {
        try {
            WebElement searchInput = driver.findElement(SEARCH_INPUT);
            searchInput.clear();
            System.out.println("Поле пошуку очищене");
        } catch (Exception e) {
            System.out.println("Помилка при очищенні поля пошуку: " + e.getMessage());
        }
    }
    
    /**
     * Отримати поточне значення поля пошуку
     * @return текст у полі пошуку
     */
    public String getSearchInputValue() {
        try {
            WebElement searchInput = driver.findElement(SEARCH_INPUT);
            return searchInput.getAttribute("value");
        } catch (Exception e) {
            return "";
        }
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

