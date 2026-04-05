package com.javaTestClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.JavascriptExecutor;

import java.util.List;

/**
 * Клас для роботи з фільтрами каталогу на сайті https://soncesad.com/
 * Дозволяє фільтрувати товари за ціною та іншими параметрами
 */
public class CatalogFilter {
    
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;
    
    // Селектори для фільтрів
    private static final By PRICE_FROM_INPUT = By.cssSelector("input[name*='price_from'], input[id*='price_from'], input[placeholder*='от'], input[placeholder*='від']");
    private static final By PRICE_TO_INPUT = By.cssSelector("input[name*='price_to'], input[id*='price_to'], input[placeholder*='до']");
    private static final By FILTER_BUTTON = By.cssSelector("button[class*='filter'], button[class*='apply']");
    private static final By FILTER_CHECKBOX = By.cssSelector("input[type='checkbox'][class*='filter']");
    private static final By FILTER_CONTAINER = By.cssSelector("[class*='filter'], [class*='sidebar']");
    
    public CatalogFilter(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.js = (JavascriptExecutor) driver;
    }
    
    /**
     * Встановити діапазон ціни для фільтрування
     * @param priceFrom - мінімальна ціна
     * @param priceTo - максимальна ціна
     */
    public void setPriceRange(int priceFrom, int priceTo) {
        try {
            // Set price from
            WebElement fromInput = wait.until(ExpectedConditions.presenceOfElementLocated(PRICE_FROM_INPUT));
            fromInput.clear();
            fromInput.sendKeys(String.valueOf(priceFrom));
            
            // Set price to
            List<WebElement> toInputs = driver.findElements(PRICE_TO_INPUT);
            if (!toInputs.isEmpty()) {
                toInputs.get(0).clear();
                toInputs.get(0).sendKeys(String.valueOf(priceTo));
            }
            
            // Apply filter
            applyFilter();
            
        } catch (Exception e) {
            throw new RuntimeException("Помилка при встановленні діапазону ціни: " + e.getMessage());
        }
    }
    
    /**
     * Встановити мінімальну ціну
     */
    public void setPriceFrom(int price) {
        try {
            WebElement fromInput = wait.until(ExpectedConditions.presenceOfElementLocated(PRICE_FROM_INPUT));
            fromInput.clear();
            fromInput.sendKeys(String.valueOf(price));
        } catch (Exception e) {
            throw new RuntimeException("Помилка при встановленні мінімальної ціни: " + e.getMessage());
        }
    }
    
    /**
     * Встановити максимальну ціну
     */
    public void setPriceTo(int price) {
        try {
            List<WebElement> toInputs = driver.findElements(PRICE_TO_INPUT);
            if (!toInputs.isEmpty()) {
                toInputs.get(0).clear();
                toInputs.get(0).sendKeys(String.valueOf(price));
            }
        } catch (Exception e) {
            throw new RuntimeException("Помилка при встановленні максимальної ціни: " + e.getMessage());
        }
    }
    
    /**
     * Отримати значення мінімальної ціни з фільтра
     */
    public int getPriceFromValue() {
        try {
            WebElement fromInput = driver.findElement(PRICE_FROM_INPUT);
            String value = fromInput.getAttribute("value");
            return value.isEmpty() ? 0 : Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Отримати значення максимальної ціни з фільтра
     */
    public int getPriceToValue() {
        try {
            List<WebElement> toInputs = driver.findElements(PRICE_TO_INPUT);
            if (!toInputs.isEmpty()) {
                String value = toInputs.get(0).getAttribute("value");
                return value.isEmpty() ? 0 : Integer.parseInt(value);
            }
        } catch (Exception e) {
        }
        return 0;
    }
    
    /**
     * Перевірити, чи можна вводити від'ємні значення у поле ціни
     */
    public boolean canEnterNegativePrice() {
        try {
            WebElement fromInput = driver.findElement(PRICE_FROM_INPUT);
            fromInput.clear();
            fromInput.sendKeys("-100");
            
            String value = fromInput.getAttribute("value");
            return value.contains("-");
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Застосувати фільтри
     */
    public void applyFilter() {
        try {
            List<WebElement> buttons = driver.findElements(FILTER_BUTTON);
            for (WebElement button : buttons) {
                if (button.isDisplayed()) {
                    scrollIntoView(button);
                    button.click();
                    
                    // Wait for filter to be applied
                    Thread.sleep(1000);
                    break;
                }
            }
        } catch (Exception ignored) {
            // If no button found, filter might be applied automatically
        }
    }
    
    /**
     * Отримати список фільтрів-чекбоксів
     */
    public List<WebElement> getFilterCheckboxes() {
        try {
            return driver.findElements(FILTER_CHECKBOX);
        } catch (Exception e) {
            return List.of();
        }
    }
    
    /**
     * Вибрати фільтр за індексом
     */
    public void selectFilterByIndex(int index) {
        try {
            List<WebElement> checkboxes = getFilterCheckboxes();
            if (index >= 0 && index < checkboxes.size()) {
                WebElement checkbox = checkboxes.get(index);
                scrollIntoView(checkbox);
                checkbox.click();
                
                // Wait for filter to be processed
                Thread.sleep(1000);
                applyFilter();
            }
        } catch (Exception e) {
            throw new RuntimeException("Помилка при виборі фільтра: " + e.getMessage());
        }
    }
    
    /**
     * Вибрати кілька фільтрів за списком індексів
     */
    public void selectMultipleFilters(int... indices) {
        for (int index : indices) {
            selectFilterByIndex(index);
            
            // Small delay between selections
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    /**
     * Очистити всі фільтри
     */
    public void clearAllFilters() {
        try {
            List<WebElement> checkboxes = getFilterCheckboxes();
            for (WebElement checkbox : checkboxes) {
                if (checkbox.isSelected()) {
                    checkbox.click();
                }
            }
            
            // Clear price inputs
            setPriceFrom(0);
            setPriceTo(0);
            
            applyFilter();
        } catch (Exception ignored) {
        }
    }
    
    private void scrollIntoView(WebElement element) {
        try {
            js.executeScript("arguments[0].scrollIntoView({block:'center',behavior:'auto'})", element);
            Thread.sleep(300);
        } catch (Exception ignored) {
        }
    }
}

