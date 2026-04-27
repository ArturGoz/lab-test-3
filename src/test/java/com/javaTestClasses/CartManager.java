package com.javaTestClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.JavascriptExecutor;

import java.util.List;

/**
 * Клас для управління кошиком на сайті https://soncesad.com/
 * Дозволяє додавати товари, перевіряти кошик та управляти товарами в кошику
 */
public class CartManager {
    
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;
    
    // Селектори
    private static final By CART_COUNT = By.cssSelector(".ms2_total_count");
    private static final By BUY_BUTTON = By.cssSelector("button.product__buy, button.similar__buy, button.msec-to-cart");
    private static final By PRODUCT_LINK = By.cssSelector(".product__title a, .product .product__img a, a.product-link, .catalog-item a");
    private static final By CART_ICON = By.cssSelector("[class*='cart'], [id*='cart']");
    private static final By PRODUCT_ROW = By.cssSelector(".cart-row, .cart-item, .msCartRow, .order-products tbody tr, .mini-cart__item, .cart-product, .cart_table tr");
    
    public CartManager(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.js = (JavascriptExecutor) driver;
    }
    
    /**
     * Додати перший товар з сторінки в кошик
     * Спробує кілька способів натиснути кнопку "Купити"
     * @return true якщо товар успішно додано, false якщо не вдалось
     */
    public boolean addFirstProduct() {
        try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(BUY_BUTTON));
            List<WebElement> buttons = driver.findElements(BUY_BUTTON);
            
            for (WebElement button : buttons) {
                if (!button.isDisplayed()) continue;
                
                try {
                    scrollIntoView(button);
                    clickElement(button);
                    return true;
                } catch (Exception e) {
                    // Try next button
                }
            }
        } catch (Exception ignored) {
        }
        
        // Try clicking first product link and then buy button
        return addProductFromProductPage();
    }
    
    /**
     * Додати товар через сторінку товару
     */
    private boolean addProductFromProductPage() {
        try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(PRODUCT_LINK));
            WebElement firstLink = null;
            
            for (WebElement link : driver.findElements(PRODUCT_LINK)) {
                if (link.isDisplayed()) {
                    firstLink = link;
                    break;
                }
            }
            
            if (firstLink != null) {
                scrollIntoView(firstLink);
                clickElement(firstLink);
                
                wait.until(ExpectedConditions.presenceOfElementLocated(BUY_BUTTON));
                WebElement buyButton = driver.findElement(BUY_BUTTON);
                scrollIntoView(buyButton);
                clickElement(buyButton);
                
                return true;
            }
        } catch (Exception ignored) {
        }
        
        return false;
    }
    
    /**
     * Отримати кількість товарів у кошику
     * @return кількість товарів або -1 якщо не вдалось отримати
     */
    public int getCartCount() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(CART_COUNT));
            List<WebElement> counts = driver.findElements(CART_COUNT);
            
            if (!counts.isEmpty()) {
                String countText = counts.get(0).getText().trim();
                return Integer.parseInt(countText);
            }
        } catch (Exception ignored) {
        }
        
        return -1;
    }
    
    /**
     * Перевірити, чи кошик порожній
     */
    public boolean isCartEmpty() {
        return getCartCount() == 0;
    }
    
    /**
     * Очікувати, поки кошик буде містити вказану кількість товарів
     */
    public void waitForCartCount(String expectedCount) {
        try {
            wait.until(ExpectedConditions.textToBe(CART_COUNT, expectedCount));
        } catch (Exception ignored) {
        }
    }
    
    /**
     * Перейти на сторінку кошика
     */
    public void navigateToCart() {
        driver.get("https://soncesad.com/korzina.html");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
    }
    
    /**
     * Перевірити наявність товарів у кошику на сторінці кошика
     */
    public boolean isProductInCart() {
        try {
            List<WebElement> products = driver.findElements(PRODUCT_ROW);
            return !products.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Відкрити мінікошик (popup)
     */
    public void openMiniCart() {
        try {
            WebElement cartIcon = driver.findElement(CART_ICON);
            scrollIntoView(cartIcon);
            clickElement(cartIcon);
            
            // Wait for mini cart to appear
            Thread.sleep(500);
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
    
    private void clickElement(WebElement element) {
        try {
            element.click();
        } catch (Exception e) {
            // Fallback to JS click
            js.executeScript("arguments[0].click();", element);
        }
    }
}

