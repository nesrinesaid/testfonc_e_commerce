package com.ecommerce.tests;

import com.ecommerce.config.DriverFactory;
import com.ecommerce.config.TestConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public abstract class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        driver = DriverFactory.createDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TestConfig.getImplicitWaitSeconds()));
        driver.get(TestConfig.getBaseUrl());
        wait = new WebDriverWait(driver, Duration.ofSeconds(TestConfig.getExplicitWaitSeconds()));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

