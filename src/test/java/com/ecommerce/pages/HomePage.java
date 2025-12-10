package com.ecommerce.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By searchInput = By.id("search");
    private final By searchButton = By.cssSelector("button[title='Search']");

    public HomePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public boolean isLoaded() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput)).isDisplayed();
    }

    public SearchResultsPage search(String query) {
        wait.until(ExpectedConditions.elementToBeClickable(searchInput)).clear();
        driver.findElement(searchInput).sendKeys(query);
        driver.findElement(searchButton).click();
        return new SearchResultsPage(driver, wait);
    }

    public void goToCategory(String linkText) {
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText(linkText))).click();
    }
}

