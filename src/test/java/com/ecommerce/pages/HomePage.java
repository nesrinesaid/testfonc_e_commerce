package com.ecommerce.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {
    private final WebDriver driver;
    private final WebDriverWait wait;


    public HomePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    private WebElement findSearchInputElement() {
        // Try multiple selectors in order of likelihood for Magento 2
        By[] selectors = {
            By.id("search"),
            By.cssSelector("#search"),
            By.cssSelector("input[name='q']"),
            By.cssSelector("input#search[name='q']"),
            By.cssSelector(".block-search input#search"),
            By.cssSelector("form.form.minisearch input#search"),
            By.cssSelector("input[type='text'][placeholder*='Search']"),
            By.xpath("//input[@id='search' and @name='q']")
        };
        
        for (By selector : selectors) {
            try {
                // Wait for element to be present and visible
                WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(selector));
                if (element != null && element.isDisplayed()) {
                    return element;
                }
            } catch (Exception e) {
                // Continue to next selector
            }
        }
        throw new org.openqa.selenium.NoSuchElementException("Could not find search input element with any of the tried selectors");
    }

    public boolean isLoaded() {
        try {
            WebElement element = findSearchInputElement();
            return element != null && element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public SearchResultsPage search(String query) {
        // Find the search input element
        WebElement searchElement = findSearchInputElement();
        wait.until(ExpectedConditions.elementToBeClickable(searchElement));
        searchElement.clear();
        searchElement.sendKeys(query);
        
        // Try multiple selectors for search button - Magento 2 specific
        By[] buttonSelectors = {
            By.cssSelector("button[title='Search']"),
            By.cssSelector("button.action.search"),
            By.cssSelector(".block-search button[type='submit']"),
            By.cssSelector("form.minisearch button"),
            By.cssSelector("#search_mini_form button[type='submit']"),
            By.xpath("//button[@title='Search']")
        };
        
        boolean buttonClicked = false;
        for (By buttonSelector : buttonSelectors) {
            try {
                WebElement button = wait.until(ExpectedConditions.elementToBeClickable(buttonSelector));
                button.click();
                buttonClicked = true;
                break;
            } catch (Exception e) {
                // Continue to next selector
            }
        }
        
        if (!buttonClicked) {
            // Fallback: try pressing Enter on the search input
            searchElement.sendKeys(org.openqa.selenium.Keys.ENTER);
        }
        
        return new SearchResultsPage(driver, wait);
    }

    public void goToCategory(String linkText) {
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText(linkText))).click();
    }
}

