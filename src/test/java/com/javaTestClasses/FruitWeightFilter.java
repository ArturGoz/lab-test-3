package com.javaTestClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.JavascriptExecutor;
import java.util.List;

/**
 * Клас для роботи з фільтром "Маса плоду/грона" на каталозі сайту https://soncesad.com/
 * Дозволяє розкривати фільтр та вибирати параметри маси плоду
 */
public class FruitWeightFilter {
    
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;
    
    // Селектори для фільтру маси плоду
    private static final By FILTER_FIELDSET = By.id("mse2_msoption|option7");
    private static final By FILTER_TITLE = By.cssSelector("fieldset[id='mse2_msoption|option7'] span.filter_title");
    private static final By FILTER_CHECKBOXES = By.cssSelector("fieldset[id='mse2_msoption|option7'] input[type='checkbox']");
    private static final By FILTER_LABELS = By.cssSelector("fieldset[id='mse2_msoption|option7'] label");
    
    public FruitWeightFilter(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.js = (JavascriptExecutor) driver;
    }
    
    /**
     * Відкрити/розширити фільтр "Маса плоду/грона"
     */
    public void openFilter() {
        try {
            // Чекаємо на заголовок фільтру
            WebElement title = wait.until(ExpectedConditions.elementToBeClickable(FILTER_TITLE));
            scrollIntoView(title);

            // Перевіряємо, чи він закритий (зазвичай це визначається класом 'closed' або висотою)
            // Найпростіше — просто клікнути, якщо список чекбоксів не видимий
            WebElement fieldset = driver.findElement(FILTER_FIELDSET);
            if (!fieldset.getAttribute("class").contains("active") || !isAnyOptionVisible()) {
                clickElement(title);
                System.out.println("Клікнули по заголовку фільтру 'Маса плоду/грона'");
            }

            // Важливо: почекати, поки анімація відкриття завершиться
            wait.until(ExpectedConditions.visibilityOfElementLocated(FILTER_LABELS));
            System.out.println("Фільтр розгорнуто, опції видимі");
        } catch (Exception e) {
            System.out.println("Помилка при відкриванні фільтру: " + e.getMessage());
        }
    }

    private boolean isAnyOptionVisible() {
        try {
            return driver.findElement(FILTER_LABELS).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Вибрати параметр маси за його текстом
     * @param weightText - текст параметру (наприклад: "0,1-1 г", "1-2 г", тощо)
     */
    public void selectWeightOption(String weightText) {
        try {
            List<WebElement> labels = driver.findElements(FILTER_LABELS);
            
            for (WebElement label : labels) {
                if (label.getText().trim().equals(weightText)) {
                    scrollIntoView(label);
                    clickElement(label);
                    System.out.println("Вибрано: " + weightText);
                    Thread.sleep(500);
                    return;
                }
            }
            
            System.out.println("Параметр '" + weightText + "' не знайдений у фільтрі");
        } catch (Exception e) {
            System.out.println("Помилка при виборі параметру: " + e.getMessage());
        }
    }
    
    /**
     * Вибрати кілька параметрів маси
     * @param weightOptions - масив текстів параметрів
     */
    public void selectMultipleWeightOptions(String... weightOptions) {
        for (String option : weightOptions) {
            selectWeightOption(option);
        }
    }
    
    /**
     * Отримати всі доступні параметри маси у фільтрі
     */
    public List<WebElement> getAllWeightOptions() {
        try {
            return driver.findElements(FILTER_LABELS);
        } catch (Exception e) {
            System.out.println("Помилка при отриманні параметрів: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Перевірити, чи вибран певний параметр
     * @param weightText - текст параметру
     * @return true якщо параметр вибран
     */
    public boolean isWeightOptionSelected(String weightText) {
        try {
            List<WebElement> checkboxes = driver.findElements(FILTER_CHECKBOXES);
            
            for (WebElement checkbox : checkboxes) {
                String value = checkbox.getAttribute("value");
                if (value != null && value.equals(weightText)) {
                    return checkbox.isSelected();
                }
            }
        } catch (Exception e) {
            System.out.println("Помилка при перевірці вибору: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Очистити всі вибрані параметри у фільтрі
     */
    public void clearAllSelections() {
        try {
            List<WebElement> checkboxes = driver.findElements(FILTER_CHECKBOXES);
            
            for (WebElement checkbox : checkboxes) {
                if (checkbox.isSelected()) {
                    clickElement(checkbox);
                    Thread.sleep(300);
                }
            }
            
            System.out.println("Всі вибори у фільтрі очищені");
        } catch (Exception e) {
            System.out.println("Помилка при очищенні фільтру: " + e.getMessage());
        }
    }
    
    /**
     * Отримати назву фільтру
     */
    public String getFilterTitle() {
        try {
            WebElement titleElement = driver.findElement(FILTER_TITLE);
            return titleElement.getText();
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Перевірити, чи фільтр видимий на сторінці
     */
    public boolean isFilterVisible() {
        try {
            WebElement fieldset = driver.findElement(FILTER_FIELDSET);
            return fieldset.isDisplayed();
        } catch (Exception e) {
            return false;
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

