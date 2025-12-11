package com.ecommerce.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class ProductPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By sizeOptions = By.cssSelector("div.swatch-attribute.size .swatch-option");
    private final By colorOptions = By.cssSelector("div.swatch-attribute.color .swatch-option");
    private final By addToCartButton = By.id("product-addtocart-button");
    private final By addedToCartToast = By.cssSelector("div.message-success");
    private final By miniCartToggle = By.cssSelector("a.action.showcart");
    private final By viewCartLink = By.cssSelector("a.action.viewcart");

    public ProductPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    /**
     * Selects the first available size option if present
     */
    private void selectSizeIfAvailable() {
        try {
            List<WebElement> sizes = driver.findElements(sizeOptions);
            if (!sizes.isEmpty()) {
                // Select first available size that is not disabled
                for (WebElement size : sizes) {
                    if (size.isDisplayed() && size.isEnabled()) {
                        wait.until(ExpectedConditions.elementToBeClickable(size)).click();
                        Thread.sleep(500); // Brief pause for UI update
                        break;
                    }
                }
            }
        } catch (TimeoutException e) {
            // Size selection not required for this product
            System.out.println("Size selection not available or not required for this product");
        } catch (Exception e) {
            System.out.println("Error selecting size: " + e.getMessage());
        }
    }

    /**
     * Selects the first available color option if present
     */
    private void selectColorIfAvailable() {
        try {
            List<WebElement> colors = driver.findElements(colorOptions);
            if (!colors.isEmpty()) {
                // Select first available color that is not disabled
                for (WebElement color : colors) {
                    if (color.isDisplayed() && color.isEnabled()) {
                        wait.until(ExpectedConditions.elementToBeClickable(color)).click();
                        Thread.sleep(500); // Brief pause for UI update
                        break;
                    }
                }
            }
        } catch (TimeoutException e) {
            // Color selection not required for this product
            System.out.println("Color selection not available or not required for this product");
        } catch (Exception e) {
            System.out.println("Error selecting color: " + e.getMessage());
        }
    }

    public CartPage addToCartAndOpenCart() {
        try {
            // Select size and color if available
            selectSizeIfAvailable();
            selectColorIfAvailable();
            
            // Add to cart
            wait.until(ExpectedConditions.elementToBeClickable(addToCartButton)).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(addedToCartToast));
            
            // Open cart
            wait.until(ExpectedConditions.elementToBeClickable(miniCartToggle)).click();
            wait.until(ExpectedConditions.elementToBeClickable(viewCartLink)).click();
            return new CartPage(driver, wait);
        } catch (TimeoutException e) {
            throw new RuntimeException("Failed to add product to cart: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during add to cart: " + e.getMessage(), e);
        }
    }
}

