package com.ecommerce.tests;

import com.ecommerce.pages.*;
import org.testng.Assert;
import org.testng.annotations.Test;

public class EcommerceFlowTest extends BaseTest {

    @Test(description = "Navigation entre pages principales")
    public void navigationBetweenMainSections() {
        HomePage home = new HomePage(driver, wait);
        Assert.assertTrue(home.isLoaded(), "La page d'accueil n'est pas chargée.");

        home.goToCategory("What's New");
        Assert.assertTrue(driver.getTitle().toLowerCase().contains("new"), "La navigation vers What's New a échoué.");

        driver.navigate().back();
        home.goToCategory("Women");
        Assert.assertTrue(driver.getTitle().toLowerCase().contains("women"), "La navigation vers Women a échoué.");
    }

    @Test(description = "Recherche d'un produit et vérification des résultats")
    public void searchProduct() {
        HomePage home = new HomePage(driver, wait);
        Assert.assertTrue(home.isLoaded(), "La page d'accueil n'est pas chargée.");

        SearchResultsPage results = home.search("jacket");
        Assert.assertTrue(results.hasResults(), "Aucun résultat de recherche retourné.");
    }

    @Test(description = "Ajout d'un produit au panier")
    public void addProductToCart() {
        HomePage home = new HomePage(driver, wait);
        SearchResultsPage results = home.search("jacket");
        Assert.assertTrue(results.hasResults(), "Aucun résultat de recherche retourné.");

        ProductPage product = results.openProductByName("jacket");
        CartPage cart = product.addToCartAndOpenCart();
        Assert.assertTrue(cart.hasItems(), "Le panier devrait contenir au moins un article.");
    }

    @Test(description = "Parcours checkout invité jusqu'au paiement")
    public void guestCheckoutFlow() {
        HomePage home = new HomePage(driver, wait);
        ProductPage product = home.search("bag").openProductByName("bag");
        CartPage cart = product.addToCartAndOpenCart();
        Assert.assertTrue(cart.hasItems(), "Le panier devrait contenir au moins un article.");

        CheckoutPage checkout = cart.proceedToCheckout();
        Assert.assertTrue(checkout.isLoaded(), "La page checkout n'est pas affichée.");

        checkout.fillGuestShippingForm("qa+guest@example.com");
        checkout.selectShippingMethodAndContinue();
        checkout.placeOrder();
        
        Assert.assertTrue(checkout.isOrderConfirmed(), "La commande n'a pas été confirmée.");
        String orderNumber = checkout.getOrderNumber();
        System.out.println("Order placed successfully with order number: " + orderNumber);
    }
}

