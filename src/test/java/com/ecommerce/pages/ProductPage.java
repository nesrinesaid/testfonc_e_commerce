package com.ecommerce.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProductPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By addToCartButton = By.id("product-addtocart-button");
    private final By addedToCartToast = By.cssSelector("div.message-success");
    private final By miniCartToggle = By.cssSelector("a.action.showcart");
    private final By viewCartLink = By.cssSelector("a.action.viewcart");

    public ProductPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public CartPage addToCartAndOpenCart() {
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(addedToCartToast));
        wait.until(ExpectedConditions.elementToBeClickable(miniCartToggle)).click();
        wait.until(ExpectedConditions.elementToBeClickable(viewCartLink)).click();
        return new CartPage(driver, wait);
    }
}

