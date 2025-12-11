package com.ecommerce.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

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
    // Updated to use stable selector instead of dynamic ko_unique_*
    private final By shippingMethodRadio = By.cssSelector("input[type='radio'][name*='shipping_method']");
    private final By nextButton = By.cssSelector("button.continue");
    private final By paymentSection = By.id("payment");
    private final By placeOrderButton = By.cssSelector("button.action.primary.checkout");
    private final By orderConfirmationMessage = By.cssSelector("span.base");
    private final By orderNumber = By.cssSelector("div.checkout-success a.order-number");

    public CheckoutPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public boolean isLoaded() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(emailField)).isDisplayed();
        } catch (TimeoutException e) {
            System.out.println("Checkout page email field not loaded: " + e.getMessage());
            return false;
        }
    }

    public void fillGuestShippingForm(String email) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(emailField)).sendKeys(email);
            driver.findElement(firstNameField).sendKeys("Test");
            driver.findElement(lastNameField).sendKeys("Automation");
            driver.findElement(streetField).sendKeys("123 Test Street");
            driver.findElement(cityField).sendKeys("Paris");
            new Select(driver.findElement(countryDropdown)).selectByVisibleText("France");
            
            // Wait for state dropdown to populate after country selection
            Thread.sleep(1000);
            new Select(driver.findElement(stateDropdown)).selectByIndex(1);
            
            driver.findElement(postcodeField).sendKeys("75001");
            driver.findElement(phoneField).sendKeys("0102030405");
        } catch (Exception e) {
            throw new RuntimeException("Failed to fill guest shipping form: " + e.getMessage(), e);
        }
    }

    public void selectShippingMethodAndContinue() {
        try {
            // Wait for shipping methods to load
            wait.until(ExpectedConditions.presenceOfElementLocated(shippingMethodRadio));
            Thread.sleep(1000); // Additional wait for shipping methods to fully load
            
            List<WebElement> shippingMethods = driver.findElements(shippingMethodRadio);
            if (!shippingMethods.isEmpty()) {
                // Select the first available shipping method
                wait.until(ExpectedConditions.elementToBeClickable(shippingMethods.get(0))).click();
            } else {
                throw new RuntimeException("No shipping methods available");
            }
            
            wait.until(ExpectedConditions.elementToBeClickable(nextButton)).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(paymentSection));
        } catch (TimeoutException e) {
            throw new RuntimeException("Failed to select shipping method: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error selecting shipping method: " + e.getMessage(), e);
        }
    }

    public void placeOrder() {
        try {
            // Wait for place order button to be clickable
            WebElement placeOrderBtn = wait.until(ExpectedConditions.elementToBeClickable(placeOrderButton));
            placeOrderBtn.click();
        } catch (TimeoutException e) {
            throw new RuntimeException("Place order button not found or not clickable: " + e.getMessage(), e);
        }
    }

    public boolean isOrderConfirmed() {
        try {
            WebElement confirmationMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(orderConfirmationMessage));
            String msgText = confirmationMsg.getText().toLowerCase();
            return msgText.contains("thank you") || msgText.contains("success") || msgText.contains("order");
        } catch (TimeoutException e) {
            System.out.println("Order confirmation message not found: " + e.getMessage());
            return false;
        }
    }

    public String getOrderNumber() {
        try {
            WebElement orderNumElement = wait.until(ExpectedConditions.visibilityOfElementLocated(orderNumber));
            return orderNumElement.getText();
        } catch (TimeoutException e) {
            System.out.println("Order number not found: " + e.getMessage());
            return null;
        }
    }
}

