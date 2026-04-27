package com.javaTestClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PriceFilter {
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    private static final By MIN_PRICE_INPUT = By.id("mse2_ms|price_0");

    public PriceFilter(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.js = (JavascriptExecutor) driver;
    }

    public void forceSetNegativePrice(String value) {
        // Чекаємо, поки елемент просто з'явиться в коді
        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(MIN_PRICE_INPUT));

        // Використовуємо JS, щоб вставити значення, навіть якщо поле "неактивне"
        js.executeScript("arguments[0].value = arguments[1];", input, value);
    }

    public String getMinPriceValue() {
        WebElement input = driver.findElement(MIN_PRICE_INPUT);
        return input.getAttribute("value");
    }
}