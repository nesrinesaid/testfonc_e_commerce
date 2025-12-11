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

    private final By sizeOptions = By.cssSelector("div.swatch-attribute.size div.swatch-option");
    private final By colorOptions = By.cssSelector("div.swatch-attribute.color div.swatch-option");
    private final By addToCartButton = By.cssSelector("#product-addtocart-button");
    private final By miniCartToggle = By.cssSelector("a.showcart");

    public ProductPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    /**
     * Finds the success toast message using multiple fallback selectors
     */
    private WebElement findSuccessToast() {
        By[] toastSelectors = {
            By.cssSelector("div[data-bind*='message']"),
            By.cssSelector("div.message-success"),
            By.cssSelector("div.messages div.message")
        };
        
        for (By selector : toastSelectors) {
            try {
                WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(selector));
                if (element.isDisplayed()) {
                    return element;
                }
            } catch (Exception e) {
                // Continue to next selector
            }
        }
        throw new org.openqa.selenium.NoSuchElementException("Could not find success toast with any of the tried selectors");
    }

    /**
     * Finds the view cart link using multiple fallback selectors
     */
    private WebElement findViewCartLink() {
        By[] cartLinkSelectors = {
            By.cssSelector("a.viewcart"),
            By.cssSelector("div.minicart-wrapper a[href*='checkout/cart']")
        };
        
        for (By selector : cartLinkSelectors) {
            try {
                return wait.until(ExpectedConditions.elementToBeClickable(selector));
            } catch (Exception e) {
                // Continue to next selector
            }
        }
        throw new org.openqa.selenium.NoSuchElementException("Could not find view cart link with any of the tried selectors");
    }

    /**
     * Selects the first available size option if present
     */
    private void selectSizeIfAvailable() {
        try {
            List<WebElement> sizes = driver.findElements(sizeOptions);
            if (!sizes.isEmpty()) {
                // Select first available size that is not disabled
                boolean sizeSelected = false;
                for (WebElement size : sizes) {
                    if (size.isDisplayed() && size.isEnabled()) {
                        WebElement clickableSize = wait.until(ExpectedConditions.elementToBeClickable(size));
                        clickableSize.click();
                        sizeSelected = true;
                        break;
                    }
                }
                
                // If we clicked a size, wait for at least one size to have 'selected' class
                if (sizeSelected) {
                    wait.until(driver -> {
                        List<WebElement> updatedSizes = driver.findElements(sizeOptions);
                        return updatedSizes.stream().anyMatch(s -> {
                            String classAttr = s.getAttribute("class");
                            return classAttr != null && classAttr.contains("selected");
                        });
                    });
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
                boolean colorSelected = false;
                for (WebElement color : colors) {
                    if (color.isDisplayed() && color.isEnabled()) {
                        WebElement clickableColor = wait.until(ExpectedConditions.elementToBeClickable(color));
                        clickableColor.click();
                        colorSelected = true;
                        break;
                    }
                }
                
                // If we clicked a color, wait for at least one color to have 'selected' class
                if (colorSelected) {
                    wait.until(driver -> {
                        List<WebElement> updatedColors = driver.findElements(colorOptions);
                        return updatedColors.stream().anyMatch(c -> {
                            String classAttr = c.getAttribute("class");
                            return classAttr != null && classAttr.contains("selected");
                        });
                    });
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
            findSuccessToast(); // Wait for success message
            
            // Open cart
            wait.until(ExpectedConditions.elementToBeClickable(miniCartToggle)).click();
            findViewCartLink().click(); // Use helper method
            return new CartPage(driver, wait);
        } catch (TimeoutException e) {
            throw new RuntimeException("Failed to add product to cart: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during add to cart: " + e.getMessage(), e);
        }
    }
}

