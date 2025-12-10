package com.ecommerce.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CheckoutPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By emailField = By.id("customer-email");
    private final By firstNameField = By.name("firstname");
    private final By lastNameField = By.name("lastname");
    private final By streetField = By.name("street[0]");
    private final By cityField = By.name("city");
    private final By countryDropdown = By.name("country_id");
    private final By stateDropdown = By.name("region_id");
    private final By postcodeField = By.name("postcode");
    private final By phoneField = By.name("telephone");
    private final By shippingMethodRadio = By.cssSelector("input[type='radio'][name='ko_unique_1'], input[type='radio'][name='ko_unique_2']");
    private final By nextButton = By.cssSelector("button.continue");
    private final By paymentSection = By.id("payment");

    public CheckoutPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public boolean isLoaded() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(emailField)).isDisplayed();
    }

    public void fillGuestShippingForm(String email) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailField)).sendKeys(email);
        driver.findElement(firstNameField).sendKeys("Test");
        driver.findElement(lastNameField).sendKeys("Automation");
        driver.findElement(streetField).sendKeys("123 Test Street");
        driver.findElement(cityField).sendKeys("Paris");
        new Select(driver.findElement(countryDropdown)).selectByVisibleText("France");
        new Select(driver.findElement(stateDropdown)).selectByIndex(1);
        driver.findElement(postcodeField).sendKeys("75001");
        driver.findElement(phoneField).sendKeys("0102030405");
    }

    public void selectShippingMethodAndContinue() {
        wait.until(ExpectedConditions.elementToBeClickable(shippingMethodRadio)).click();
        wait.until(ExpectedConditions.elementToBeClickable(nextButton)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(paymentSection));
    }
}

