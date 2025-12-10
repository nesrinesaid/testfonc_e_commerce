package com.ecommerce.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CartPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By cartItems = By.cssSelector("div.cart.item");
    private final By proceedToCheckoutButton = By.cssSelector("button[data-role='proceed-to-checkout']");

    public CartPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public boolean hasItems() {
        return !driver.findElements(cartItems).isEmpty();
    }

    public CheckoutPage proceedToCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(proceedToCheckoutButton)).click();
        return new CheckoutPage(driver, wait);
    }
}

