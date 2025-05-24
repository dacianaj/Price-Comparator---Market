package service;

import model.Product;

import java.util.*;


// provides logic for finding the cheapest products from a list
public class ShoppingService {


    /**
     * finds the cheapest matching product for each item in the shopping list.
     * matching is case-insensitive and based on partial name matches.
     *
     * @param allProducts  the full list of available products from all stores
     * @param shoppingList the list of product names to search for (e.g., "lapte", "banane")
     * @return a map where each key is an item from the shopping list, and the value is the cheapest matching Product
     */

    public Map<String, Product> findCheapestProducts(List<Product> allProducts, List<String> shoppingList) {
        Map<String, Product> result = new HashMap<>();


        // loop through each item from the user's shopping list
        for (String item : shoppingList) {
            Product cheapest = null;

            // compare each product in the database to find matches
            for (Product p : allProducts) {
                // searching based on the name (ignore capslock)
                if (p.productName.toLowerCase().contains(item.toLowerCase())) {
                    if (cheapest == null || p.price < cheapest.price) {
                        cheapest = p;
                    }
                }
            }

            // if a matching product was found, store it in the result map
            if (cheapest != null) {
                result.put(item, cheapest);
            }
        }

        return result;
    }
}