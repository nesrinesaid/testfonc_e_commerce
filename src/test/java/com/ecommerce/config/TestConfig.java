package com.ecommerce.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestConfig {
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream input = TestConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                PROPS.load(input);
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to load application.properties", e);
        }
    }

    private TestConfig() {
    }

    public static String getBaseUrl() {
        return System.getProperty("baseUrl", PROPS.getProperty("baseUrl", "https://magento.softwaretestingboard.com"));
    }

    public static String getBrowser() {
        return System.getProperty("browser", PROPS.getProperty("browser", "chrome"));
    }

    public static int getImplicitWaitSeconds() {
        return Integer.parseInt(System.getProperty("implicitWaitSeconds", PROPS.getProperty("implicitWaitSeconds", "5")));
    }

    public static int getExplicitWaitSeconds() {
        return Integer.parseInt(System.getProperty("explicitWaitSeconds", PROPS.getProperty("explicitWaitSeconds", "15")));
    }
}

