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

    private final By emailField = By.cssSelector("#customer-email, input[name='username']");
    private final By firstNameField = By.cssSelector("input[name='firstname']");
    private final By lastNameField = By.cssSelector("input[name='lastname']");
    private final By streetField = By.cssSelector("input[name='street[0]']");
    private final By cityField = By.cssSelector("input[name='city']");
    private final By countryDropdown = By.cssSelector("select[name='country_id']");
    private final By stateDropdown = By.cssSelector("select[name='region_id']");
    private final By postcodeField = By.cssSelector("input[name='postcode']");
    private final By phoneField = By.cssSelector("input[name='telephone']");
    // Updated to use stable selector instead of dynamic ko_unique_*
    private final By shippingMethodRadio = By.cssSelector("input[type='radio'][name='shipping_method'], tr.row input[type='radio'][name*='shipping']");
    private final By nextButton = By.cssSelector("button.continue, button.action.continue");
    private final By paymentSection = By.cssSelector("#payment, div.payment-method");
    private final By placeOrderButton = By.cssSelector("button.action.primary.checkout, button[title='Place Order']");
    private final By orderConfirmationMessage = By.cssSelector("span.base, h1.page-title");
    private final By orderNumber = By.cssSelector("div.checkout-success a.order-number, a[href*='sales/order/view']");

    public CheckoutPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public boolean isLoaded() {
        try {
            // Try to find either the email field or the shipping form container
            try {
                return wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(emailField),
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#shipping"))
                )) != null;
            } catch (Exception e) {
                return false;
            }
        } catch (Exception e) {
            System.out.println("Checkout page not loaded: " + e.getMessage());
            return false;
        }
    }

    public void fillGuestShippingForm(String email) {
        try {
            // Fill email
            WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(emailField));
            emailInput.clear();
            emailInput.sendKeys(email);
            
            // Fill form fields
            wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField)).sendKeys("Test");
            driver.findElement(lastNameField).sendKeys("Automation");
            driver.findElement(streetField).sendKeys("123 Test Street");
            driver.findElement(cityField).sendKeys("Paris");
            
            // Select country
            WebElement countrySelect = driver.findElement(countryDropdown);
            Select country = new Select(countrySelect);
            country.selectByVisibleText("France");
            
            // Wait for state dropdown to become visible after country selection (AJAX update)
            try {
                WebElement stateSelect = wait.until(ExpectedConditions.visibilityOfElementLocated(stateDropdown));
                Select state = new Select(stateSelect);
                // Wait for options to populate
                wait.until(driver -> state.getOptions().size() > 1);
                if (state.getOptions().size() > 1) {
                    state.selectByIndex(1);
                }
            } catch (Exception e) {
                System.out.println("State selection not available or not required");
            }
            
            driver.findElement(postcodeField).sendKeys("75001");
            driver.findElement(phoneField).sendKeys("0102030405");
            
            // Wait for shipping methods to load (they appear after address is complete)
            wait.until(ExpectedConditions.presenceOfElementLocated(shippingMethodRadio));
        } catch (Exception e) {
            throw new RuntimeException("Failed to fill guest shipping form: " + e.getMessage(), e);
        }
    }

    public void selectShippingMethodAndContinue() {
        try {
            // Wait for shipping methods to load and be clickable
            wait.until(ExpectedConditions.presenceOfElementLocated(shippingMethodRadio));
            
            List<WebElement> shippingMethods = driver.findElements(shippingMethodRadio);
            if (!shippingMethods.isEmpty()) {
                // Find first enabled shipping method
                for (WebElement method : shippingMethods) {
                    try {
                        if (method.isEnabled() && method.isDisplayed()) {
                            wait.until(ExpectedConditions.elementToBeClickable(method)).click();
                            break;
                        }
                    } catch (Exception e) {
                        // Try next method
                    }
                }
            } else {
                throw new RuntimeException("No shipping methods available");
            }
            
            // Wait for next button to be enabled and clickable
            WebElement nextBtn = wait.until(ExpectedConditions.elementToBeClickable(nextButton));
            nextBtn.click();
            
            // Wait for payment section to load and be visible
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
            
            // Scroll to button if needed to ensure it's in viewport
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", placeOrderBtn);
            
            // Wait for scroll to complete and button to be stable
            wait.until(ExpectedConditions.elementToBeClickable(placeOrderBtn));
            
            placeOrderBtn.click();
        } catch (TimeoutException e) {
            throw new RuntimeException("Place order button not found or not clickable: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to place order: " + e.getMessage(), e);
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

