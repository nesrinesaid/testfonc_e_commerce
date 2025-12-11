# E-Commerce Functional Test Suite

Automated functional testing suite for e-commerce platform using Selenium WebDriver and TestNG.

## Description

This project implements automated functional tests for an e-commerce website (Magento demo store) using the Page Object Model (POM) design pattern. The test suite covers key e-commerce workflows including:

- Navigation between main sections
- Product search functionality
- Adding products to cart (with size/color selection)
- Guest checkout flow with order confirmation

## Prerequisites

Before running the tests, ensure you have the following installed:

- **Java**: JDK 17 or higher
  ```bash
  java -version
  ```

- **Maven**: Version 3.6 or higher
  ```bash
  mvn -version
  ```

- **Web Browsers**: At least one of the following:
  - Google Chrome (latest version)
  - Mozilla Firefox (latest version)
  - Microsoft Edge (latest version)

## Project Structure

```
testfonc-ecommerce/
├── src/
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── ecommerce/
│       │           ├── config/
│       │           │   ├── DriverFactory.java      # WebDriver factory
│       │           │   └── TestConfig.java         # Configuration loader
│       │           ├── pages/
│       │           │   ├── HomePage.java           # Home page object
│       │           │   ├── SearchResultsPage.java  # Search results page
│       │           │   ├── ProductPage.java        # Product details page
│       │           │   ├── CartPage.java           # Shopping cart page
│       │           │   └── CheckoutPage.java       # Checkout page
│       │           └── tests/
│       │               ├── BaseTest.java           # Base test class
│       │               └── EcommerceFlowTest.java  # Test cases
│       └── resources/
│           ├── application.properties              # Test configuration
│           └── testng.xml                          # TestNG suite configuration
├── pom.xml                                         # Maven configuration
└── README.md                                       # This file
```

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/nesrinesaid/testfonc_e_commerce.git
   cd testfonc_e_commerce
   ```

2. Install dependencies:
   ```bash
   mvn clean install -DskipTests
   ```

## Configuration

Test configuration is managed through `src/test/resources/application.properties`:

```properties
baseUrl=https://magento.softwaretestingboard.com
browser=chrome
implicitWaitSeconds=5
explicitWaitSeconds=15
```

### Available Configuration Options

- **baseUrl**: The URL of the application under test
- **browser**: Browser to use (chrome, firefox, edge)
- **implicitWaitSeconds**: Implicit wait timeout in seconds
- **explicitWaitSeconds**: Explicit wait timeout in seconds

### Overriding Configuration

You can override configuration values using Maven system properties:

```bash
mvn test -DbaseUrl=https://example.com -Dbrowser=firefox
```

## Running Tests

### Run All Tests

Execute all test cases in the suite:

```bash
mvn clean test
```

### Run with Specific Browser

Run tests with Firefox:
```bash
mvn clean test -Dbrowser=firefox
```

Run tests with Edge:
```bash
mvn clean test -Dbrowser=edge
```

### Run Specific Test Class

```bash
mvn test -Dtest=EcommerceFlowTest
```

### Run Specific Test Method

```bash
mvn test -Dtest=EcommerceFlowTest#searchProduct
```

### Run with TestNG XML

```bash
mvn test -DsuiteXmlFile=src/test/resources/testng.xml
```

## Test Coverage

### Test Cases

1. **navigationBetweenMainSections**
   - Verifies navigation between main category sections
   - Tests "What's New" and "Women" categories

2. **searchProduct**
   - Tests product search functionality
   - Validates search results are returned

3. **addProductToCart**
   - Searches for a product
   - Selects size and color options (if available)
   - Adds product to cart
   - Verifies cart contains items

4. **guestCheckoutFlow**
   - Complete guest checkout workflow
   - Fills shipping information
   - Selects shipping method
   - Places order
   - Validates order confirmation

## Test Reports

After running tests, reports are generated in the following locations:

### TestNG HTML Report
```
target/surefire-reports/index.html
```

### Surefire Reports
```
target/surefire-reports/
├── emailable-report.html     # Email-friendly report
├── index.html                # Main report
└── testng-results.xml        # XML results
```

### Viewing Reports

Open the HTML report in a browser:
```bash
# On macOS
open target/surefire-reports/index.html

# On Linux
xdg-open target/surefire-reports/index.html

# On Windows
start target/surefire-reports/index.html
```

## Troubleshooting

### WebDriver Issues

If you encounter WebDriver errors:

1. Ensure your browser is up to date
2. WebDriverManager automatically downloads the correct driver version
3. Check your internet connection (required for driver download)

### Test Failures

Common issues and solutions:

1. **Element not found errors**
   - Website structure may have changed
   - Increase wait times in application.properties
   - Check if the website is accessible

2. **Timeout errors**
   - Slow internet connection
   - Increase `explicitWaitSeconds` in configuration
   - Check if website is responsive

3. **Browser not launching**
   - Verify browser is installed
   - Try a different browser
   - Check browser compatibility with Selenium version

### Clean Build

If you encounter build issues:
```bash
mvn clean install -U
```

## Dependencies

Main dependencies used in this project:

- **Selenium WebDriver**: 4.25.0 - Browser automation
- **TestNG**: 7.10.2 - Testing framework
- **WebDriverManager**: 5.9.2 - Automatic driver management
- **SLF4J**: 2.0.9 - Logging framework

## Best Practices

This project follows these best practices:

1. **Page Object Model (POM)**: Separates page logic from test logic
2. **Explicit Waits**: Uses WebDriverWait for reliable element interactions
3. **Configuration Management**: Externalized configuration in properties file
4. **Exception Handling**: Proper error handling with meaningful messages
5. **Independent Tests**: Each test can run independently
6. **Stable Selectors**: Uses reliable element locators

## Contributing

When contributing to this project:

1. Follow the existing code structure
2. Maintain the Page Object Model pattern
3. Add proper wait conditions for dynamic elements
4. Include meaningful assertions in tests
5. Update documentation for new features

## License

This project is created for educational and testing purposes.

## Contact

For questions or issues, please open an issue in the GitHub repository.

## Version History

- **0.1.0-SNAPSHOT**: Initial release
  - Basic e-commerce test flows
  - Page Object Model implementation
  - Multi-browser support
  - Complete checkout flow with order confirmation
