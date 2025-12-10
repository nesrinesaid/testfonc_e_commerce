package com.ecommerce.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class SearchResultsPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By productItems = By.cssSelector("ol.products.list.items.product-items li.product-item");
    private final By productName = By.cssSelector("a.product-item-link");

    public SearchResultsPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public boolean hasResults() {
        List<WebElement> items = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productItems));
        return !items.isEmpty();
    }

    public ProductPage openProductByName(String name) {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productItems));
        List<WebElement> items = driver.findElements(productItems);
        for (WebElement item : items) {
            WebElement title = item.findElement(productName);
            if (title.getText().toLowerCase().contains(name.toLowerCase())) {
                title.click();
                return new ProductPage(driver, wait);
            }
        }
        // Fallback to first item if specific name not found
        items.get(0).findElement(productName).click();
        return new ProductPage(driver, wait);
    }
}

