package com.ecommerce.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.io.File;

/**
 * Factory class for creating WebDriver instances.
 * Uses Selenium Manager (built into Selenium 4.6+) for automatic driver management.
 * Falls back to system-installed drivers if Selenium Manager cannot download drivers.
 */
public class DriverFactory {

    private DriverFactory() {
    }

    public static WebDriver createDriver() {
        String browser = TestConfig.getBrowser().toLowerCase();
        
        // Set system property to use system chromedriver if Selenium Manager fails
        // This is necessary as a fallback in CI environments with network restrictions
        // where Selenium Manager cannot download the driver from the internet
        if ("chrome".equals(browser) && System.getProperty("webdriver.chrome.driver") == null) {
            String chromedriverPath = findSystemChromeDriver();
            if (chromedriverPath != null) {
                System.setProperty("webdriver.chrome.driver", chromedriverPath);
            }
        }
        
        switch (browser) {
            case "firefox":
                FirefoxOptions ffOptions = new FirefoxOptions();
                ffOptions.addArguments("--width=1400", "--height=1000");
                // Run headless in CI environment (no display)
                if (System.getenv("CI") != null || System.getenv("DISPLAY") == null) {
                    ffOptions.addArguments("--headless");
                }
                return new FirefoxDriver(ffOptions);
            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--window-size=1400,1000");
                // Run headless in CI environment (no display)
                if (System.getenv("CI") != null || System.getenv("DISPLAY") == null) {
                    edgeOptions.addArguments("--headless");
                }
                return new EdgeDriver(edgeOptions);
            case "chrome":
            default:
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--window-size=1400,1000");
                chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
                // Run headless in CI environment (no display)
                if (System.getenv("CI") != null || System.getenv("DISPLAY") == null) {
                    chromeOptions.addArguments("--headless=new");
                }
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--disable-gpu");
                return new ChromeDriver(chromeOptions);
        }
    }
    
    /**
     * Find system-installed chromedriver
     */
    private static String findSystemChromeDriver() {
        String[] possiblePaths = {
            "/usr/bin/chromedriver",
            "/usr/local/bin/chromedriver",
            "/opt/chromedriver/chromedriver"
        };
        
        for (String path : possiblePaths) {
            File file = new File(path);
            if (file.exists() && file.canExecute()) {
                return path;
            }
        }
        return null;
    }
}

