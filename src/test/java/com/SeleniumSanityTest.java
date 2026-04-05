package com;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SeleniumSanityTest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        // Let Selenium Manager provide a compatible driver (no WebDriverManager call)
        ChromeOptions options = new ChromeOptions();
        // Run headless by default in CI; comment out if you want to see the browser
      //  options.addArguments("--headless=new");
        // Allow remote origins to avoid Chrome startup errors on some versions
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void openExampleDotCom() {
        driver.get("https://example.com");
        String title = driver.getTitle();
        System.out.println("Page title: " + title);
        assertTrue(title.toLowerCase().contains("example"));
    }
}


