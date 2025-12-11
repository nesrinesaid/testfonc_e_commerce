package com.ecommerce.tests;

import com.ecommerce.config.DriverFactory;
import com.ecommerce.config.TestConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public abstract class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        driver = DriverFactory.createDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TestConfig.getImplicitWaitSeconds()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.get(TestConfig.getBaseUrl());
        wait = new WebDriverWait(driver, Duration.ofSeconds(TestConfig.getExplicitWaitSeconds()));
        
        // Wait for page to be ready and JavaScript to finish
        try {
            wait.until(webDriver -> {
                if (webDriver == null) return false;
                org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) webDriver;
                String readyState = (String) js.executeScript("return document.readyState");
                if (!"complete".equals(readyState)) return false;
                // Also check if jQuery is loaded and active (common in Magento)
                try {
                    Object jQueryActive = js.executeScript("return typeof jQuery !== 'undefined' ? jQuery.active : 0");
                    if (jQueryActive instanceof Number && ((Number) jQueryActive).intValue() > 0) {
                        return false; // jQuery AJAX still active
                    }
                } catch (Exception ignored) {
                    // jQuery might not be present, that's okay
                }
                return true;
            });
        } catch (Exception e) {
            // Continue even if readyState check fails
        }
        
        // Dismiss any cookie consent banners or modals that might block elements
        try {
            // Try to find and close common cookie consent elements
            String[] cookieSelectors = {
                "button[id*='cookie']",
                "button[class*='cookie']",
                "button[id*='accept']",
                "button[class*='accept']",
                "a[id*='cookie']",
                ".cookie-banner button",
                "#cookie-banner button"
            };
            
            for (String selector : cookieSelectors) {
                try {
                    By cookieBy = org.openqa.selenium.By.cssSelector(selector);
                    var elements = driver.findElements(cookieBy);
                    if (!elements.isEmpty() && elements.get(0).isDisplayed()) {
                        elements.get(0).click();
                        // Wait for the element to disappear using the selector, not the cached element
                        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated(cookieBy));
                        break;
                    }
                } catch (Exception ignored) {
                    // Continue to next selector
                }
            }
        } catch (Exception e) {
            // Ignore if cookie banner handling fails
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

