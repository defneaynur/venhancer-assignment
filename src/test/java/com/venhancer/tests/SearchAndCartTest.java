package com.venhancer.tests;

import com.venhancer.base.E2EBaseTest;
import com.venhancer.pages.CartPage;
import com.venhancer.pages.HomePage;
import com.venhancer.pages.ProductDetailPage;
import com.venhancer.pages.SearchResultsPage;
import com.venhancer.utils.AssertionHelper;
import com.venhancer.utils.ConfigReader;
import com.venhancer.utils.messages.TestMessages;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * End-to-end test verifying the primary search-to-cart flow on Hepsiburada.
 * Validates that a user can search for a product, filter the results, select
 * an organic product, view its details, and successfully add it to their cart.
 *
 * <p>Test methods are chained via {@code dependsOnMethods} and share a single browser
 * session (inherited from {@link E2EBaseTest}) to simulate a real user journey.
 */
@Epic("Hepsiburada Shopping")
@Feature("Search and Cart Flow")
public class SearchAndCartTest extends E2EBaseTest {

    private static final Logger log = LoggerFactory.getLogger(SearchAndCartTest.class);

    private HomePage          homePage;
    private SearchResultsPage resultsPage;
    private ProductDetailPage productPage;
    private CartPage          cartPage;
    private String            productName;

    /**
     * Navigates to the base URL and initialises the Home Page before the test flow begins.
     * TestNG guarantees that the parent {@code @BeforeClass} (setUpSession) runs first.
     */
    @BeforeClass(alwaysRun = true)
    public void openHomePage() {
        String baseUrl = ConfigReader.get("base.url", "https://www.hepsiburada.com");
        log.info(TestMessages.STARTED, getClass().getSimpleName());
        homePage = new HomePage();
        homePage.open(baseUrl);
    }

    /**
     * Executes a product search using the keyword defined in {@code config.properties}.
     * Verifies that search results are displayed and at least one product is listed.
     */
    @Test
    @Story("Search returns results")
    @Description("User enters a search keyword and expects to see a non-empty product listing.")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldReturnSearchResults() {
        String keyword = ConfigReader.get("search.keyword", "laptop");
        resultsPage = homePage.search(keyword);

        AssertionHelper.assertTrue(
            resultsPage.areResultsDisplayed(),
            "Search results should be displayed for keyword: " + keyword
        );
        AssertionHelper.assertTrue(
            resultsPage.getResultCount() > 0,
            "At least one product should be listed"
        );
    }

    /**
     * Applies the first available filter from the left-hand sidebar.
     * Verifies that the product list updates and remains visible after filtering.
     */
    @Test(dependsOnMethods = "shouldReturnSearchResults")
    @Story("Filter search results")
    @Description("User applies the first unchecked sidebar filter and expects products to still be visible.")
    @Severity(SeverityLevel.NORMAL)
    public void shouldFilterResults() {
        resultsPage.applyFirstAvailableFilter();

        AssertionHelper.assertTrue(
            resultsPage.areResultsDisplayed(),
            "Products should still be visible after applying a filter"
        );
    }

    /**
     * Clicks the first organic product from the filtered search results.
     * Verifies that the Product Detail Page loads with a valid product name.
     */
    @Test(dependsOnMethods = "shouldFilterResults")
    @Story("Open product detail page")
    @Description("User clicks the first organic result and expects the product detail page to load.")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldOpenProductDetailPage() {
        productPage = resultsPage.clickFirstProduct();

        AssertionHelper.assertTrue(
            productPage.isProductPageDisplayed(),
            "Product detail page should be displayed"
        );
        productName = productPage.getProductName();
        AssertionHelper.assertNotEmpty(productName, "Product name should not be empty");
    }

    /**
     * Adds the currently viewed product to the shopping cart.
     * Verifies that the Cart Page opens and contains at least one item.
     */
    @Test(dependsOnMethods = "shouldOpenProductDetailPage")
    @Story("Add product to cart")
    @Description("User clicks 'Add to Cart' and expects the cart page to show items.")
    @Severity(SeverityLevel.BLOCKER)
    public void shouldAddProductToCart() {
        cartPage = productPage.addToCart();

        AssertionHelper.assertTrue(
            cartPage.hasItemsInCart(),
            "Cart should contain at least one item after adding: " + productName
        );
    }

    /**
     * Verifies that the specific product added in the previous step is listed in the cart.
     */
    @Test(dependsOnMethods = "shouldAddProductToCart")
    @Story("Verify cart contents")
    @Description("User verifies that the product added in the previous step appears in the cart.")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldHaveCorrectProductInCart() {
        AssertionHelper.assertTrue(
            cartPage.isProductInCart(productName),
            "The added product should be visible in the cart: " + productName
        );
        log.info(TestMessages.CART_TITLE, cartPage.getCartPageTitle());
        log.info(TestMessages.FINISHED, getClass().getSimpleName());
    }
}
