package com.ecommerce.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * Factory class for creating WebDriver instances.
 * Uses Selenium Manager (built into Selenium 4.6+) for automatic driver management.
 */
public class DriverFactory {

    private DriverFactory() {
    }

    public static WebDriver createDriver() {
        String browser = TestConfig.getBrowser().toLowerCase();
        switch (browser) {
            case "firefox":
                FirefoxOptions ffOptions = new FirefoxOptions();
                ffOptions.addArguments("--width=1400", "--height=1000");
                return new FirefoxDriver(ffOptions);
            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--window-size=1400,1000");
                return new EdgeDriver(edgeOptions);
            case "chrome":
            default:
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--window-size=1400,1000");
                chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
                return new ChromeDriver(chromeOptions);
        }
    }
}

