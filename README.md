# Hepsiburada Automation Test — Venhancer Assignment

End-to-end Selenium WebDriver automation for [hepsiburada.com](https://www.hepsiburada.com) covering product search, filtering, and cart operations.

---

## Technologies

| Technology | Version | Purpose |
|---|---|---|
| Java | 11+ | Language |
| Selenium WebDriver | 4.18.1 | Browser automation |
| TestNG | 7.9.0 | Test runner |
| WebDriverManager | 5.7.0 | Automatic driver binary management |
| Allure TestNG | 2.27.0 | Test reporting |
| AspectJ Weaver | 1.9.22 | `@Step` instrumentation for page objects |
| Logback | 1.5.6 | Structured logging |
| Maven | 3.8+ | Build & dependency management |

---

## Project Structure

```
src/
├── main/java/com/venhancer/
│   ├── driver/
│   │   ├── BrowserDriver.java              # Strategy interface — one impl per browser
│   │   ├── BrowserDriverFactory.java       # Registry: browser name → BrowserDriver
│   │   ├── ConsentBypassScript.java        # GDPR consent localStorage payload
│   │   ├── DriverManager.java              # ThreadLocal WebDriver holder
│   │   └── impl/
│   │       ├── ChromeBrowserDriver.java    # Chrome + CDP consent injection
│   │       ├── ChromeHeadlessBrowserDriver # Chrome headless (CI-friendly)
│   │       └── FirefoxBrowserDriver.java   # Firefox
│   ├── pages/
│   │   ├── BasePage.java                   # Shared wait/interaction helpers
│   │   ├── HomePage.java                   # Search entry point
│   │   ├── SearchResultsPage.java          # Results listing + filter interaction
│   │   ├── ProductDetailPage.java          # Product page + add to cart
│   │   ├── CartPage.java                   # Cart verification
│   │   └── locators/
│   │       ├── CommonLocators.java         # Shared (popup close button)
│   │       ├── HomeLocators.java           # Home page selectors
│   │       ├── SearchResultsLocators.java  # Search results selectors
│   │       ├── ProductDetailLocators.java  # Product detail selectors
│   │       └── CartLocators.java           # Cart page selectors
│   └── utils/
│       ├── ConfigReader.java               # Reads config.properties
│       ├── JsHelper.java                   # JavascriptExecutor helpers
│       ├── WaitHelper.java                 # Explicit wait facade
│       └── messages/
│           ├── CartMessages.java           # Log strings + cart keyword constants
│           ├── DriverMessages.java         # Driver log strings
│           ├── HomeMessages.java           # Home page log strings
│           ├── ProductDetailMessages.java  # Product detail log strings
│           └── WaitMessages.java           # Wait helper log strings
└── test/
    ├── java/com/venhancer/
    │   ├── base/
    │   │   ├── BaseTest.java               # Fresh browser per @Test method
    │   │   └── E2EBaseTest.java            # Shared browser across @Test methods
    │   ├── tests/
    │   │   └── SearchAndCartTest.java      # Full search-to-cart E2E flow
    │   └── utils/
    │       ├── AssertionHelper.java        # Logging wrapper around TestNG Assert
    │       ├── ScreenshotHelper.java       # Screenshot capture + Allure attachment
    │       └── messages/
    │           ├── AssertionMessages.java  # Assertion log strings
    │           └── TestMessages.java       # Test lifecycle log strings
    └── resources/
        ├── config.properties               # Browser, URL, keyword config
        ├── allure.properties               # Allure results directory config
        ├── testng.xml                      # TestNG suite definition
        └── logback.xml                     # Console + file logging config
```

---

## Architecture

### Design Patterns

| Pattern | Where |
|---|---|
| **Page Object Model** | `pages/` — each page is a dedicated class |
| **Strategy** | `BrowserDriver` interface + `impl/` — adding a browser = one new class |
| **Factory** | `BrowserDriverFactory` — maps browser names to driver suppliers |
| **ThreadLocal** | `DriverManager` — parallel-test-safe WebDriver storage |

### Key Decisions

- **Locators in dedicated classes** — all `By` selectors live in `pages/locators/` so page logic and CSS selectors never mix.
- **Message classes** — every log string is a constant; no inline strings in production code.
- **No `Thread.sleep`** — all synchronisation uses `WebDriverWait` with explicit conditions.
- **CDP consent injection** — Hepsiburada's GDPR overlay is bypassed by setting the `efl-saved-consent` localStorage key via Chrome DevTools Protocol before any page script runs.
- **URL stability wait** — Hepsiburada fires an A-B redirect after landing; `waitForUrlStable()` polls until two consecutive URL reads are identical.
- **Config-driven** — browser, base URL, search keyword, and cart URL are all in `config.properties`.

---

## How to Run

### Prerequisites

- Java 11 or newer on `PATH`
- Maven 3.8+ on `PATH`
- Google Chrome installed (latest)

### Run all tests

```bash
mvn clean test
```

### Generate Allure HTML report

```bash
mvn allure:report
```

Report is written to `target/site/allure-maven-plugin/index.html`.

### Open Allure report in browser (live server)

```bash
mvn allure:serve
```

### Run with a different browser or keyword

Edit `src/test/resources/config.properties`:

```properties
browser=chrome-headless   # chrome | chrome-headless | firefox
base.url=https://www.hepsiburada.com
search.keyword=laptop
cart.url=https://checkout.hepsiburada.com/sepetim
```

---

## Test Scenario

| # | Step | Assertion |
|---|---|---|
| 1 | Navigate to `base.url` | Search input is clickable |
| 2 | Search for configured keyword | Results are displayed, count > 0 |
| 3 | Apply first unchecked sidebar filter | Products still visible after filter |
| 4 | Click first organic product | Product detail page loads with a title |
| 5 | Click **Sepete Ekle** (Add to Cart) | Success popup appears |
| 6 | Navigate to cart page | Cart body contains checkout keywords |
| 7 | Verify product in cart | Product name prefix found in cart body |

All 5 TestNG test methods share a single browser session (`E2EBaseTest`) and are chained with `dependsOnMethods` to simulate a real user journey.

---

## Test Results

Last run — **2026-04-26**, Chrome 147, Hepsiburada live site:

| Test | Status | Details |
|---|---|---|
| `shouldReturnSearchResults` | ✅ PASS | Keyword: *laptop* |
| `shouldFilterResults` | ✅ PASS | First available sidebar filter applied |
| `shouldOpenProductDetailPage` | ✅ PASS | *Apple MacBook Air M4 16GB 256GB SSD* |
| `shouldAddProductToCart` | ✅ PASS | Confirmed on retry click |
| `shouldHaveCorrectProductInCart` | ✅ PASS | Cart title: *Sepetim* |

**Total: 5 / 5 passed — 63 seconds**

---

## Allure Report

The Allure report shows the full test breakdown by Epic → Feature → Story:

```
Epic:    Hepsiburada Shopping
Feature: Search and Cart Flow
  Story: Search returns results          → shouldReturnSearchResults
  Story: Filter search results           → shouldFilterResults
  Story: Open product detail page        → shouldOpenProductDetailPage
  Story: Add product to cart             → shouldAddProductToCart
  Story: Verify cart contents            → shouldHaveCorrectProductInCart
```

Each test shows step-by-step execution traced from page object methods (`@Step`) down to individual wait and interaction calls. On failure, a screenshot is automatically attached to the failing test in the report.

---

## Assumptions

- Tests run against the live hepsiburada.com site — network access is required.
- A guest session is sufficient; no login is needed for the cart flow.
- WebDriverManager resolves and downloads the correct ChromeDriver/GeckoDriver automatically.
- Locators use `data-test-id` attributes where available; these are more stable than class names but may still need updating if Hepsiburada changes their markup.
- The filter step clicks the first unchecked checkbox in the facet panel. The specific filter type (brand, price, rating) depends on what the site renders for the given keyword.
- Chrome CDP version warnings at startup are harmless — consent injection falls back gracefully and the tests continue.
