import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import model.Product;
import model.Discount;
import repository.ProductRepository;
import repository.DiscountRepository;
import service.ShoppingService;

public class CSVReader {
    public static void main(String[] args) {
        String[] filePaths = {
                "lidl_2025_05_01.csv",
                "profi_2025_05_01.csv",
                "kaufland_2025_05_01.csv",
                "lidl_2025_05_08.csv",
                "profi_2025_05_08.csv",
                "kaufland_2025_05_08.csv",
                "lidl_discounts_2025_05_01.csv",
                "profi_discounts_2025_05_01.csv",
                "kaufland_discounts_2025_05_01.csv",
                "lidl_discounts_2025_05_08.csv",
                "profi_discounts_2025_05_08.csv",
                "kaufland_discounts_2025_05_08.csv",
        };

        List<Product> allProducts = new ArrayList<>(); // new list to upload all products from CSV
        List<Discount> allDiscounts = new ArrayList<>(); // new list to upload all discounts from CSV
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // interpret data by year-month-day

        for (String filePath : filePaths) {
            File f = new File(filePath);
            if (!f.exists()) continue; // check if the file exists from filePaths, if one file does not exist, the code continues to check the next file

            if (filePath.contains("discount")) {  // check if the file contains discount
                allDiscounts.addAll(DiscountRepository.loadDiscounts(filePath)); // load discounts and add them into the list allDiscounts
            } else {
                allProducts.addAll(ProductRepository.loadProducts(filePath)); // load products
            }
        }

        // creating a menu
        Scanner scanner = new Scanner(System.in);
        int option;

        do {
            System.out.println("\n=== Price Comparator - Market ===");
            System.out.println("1. Cheapest products ");
            System.out.println("2. Top 10 discounts");
            System.out.println("3. New discounts (last 24h)");
            System.out.println("4. Price history (all products)");
            System.out.println("5. Best value per unit");
            System.out.println("6. Price alert (manual list)");
            System.out.println("0. Exit");
            System.out.print("Choose option: "); // read the chosen option from the menu
            option = scanner.nextInt();
            scanner.nextLine();

            // option 1
            switch (option) {
                case 1 -> {
                    List<String> shoppingList = allProducts.stream() // going through the list allProducts
                            .map(p -> p.productName.toLowerCase())  //for every product extracts name and convert it in lower case letters
                            .distinct() //eliminate duplicates, if there are 3 products 'lapte' in the list, the name appears just once in the list
                            .collect(Collectors.toList()); // result is converted into a new list

                    ShoppingService service = new ShoppingService(); // new object that contains de method findCheapestProducts
                    Map<String, Product> cheapest = service.findCheapestProducts(allProducts, shoppingList); // creat a map and the name of the product is associated with the lowest price

                    System.out.println("\n=== Cheapest Products ===");
                    for (Map.Entry<String, Product> entry : cheapest.entrySet()) {
                        System.out.println(entry.getKey() + " → " + entry.getValue());
                    }
                }

                // optiion 2
                case 2 -> {
                    // sort all discount from high to low based on the percentage of discounts
                    allDiscounts.sort((a, b) -> Double.compare(b.percentageOfDiscount, a.percentageOfDiscount));
                    System.out.println("\n=== Top 10 discounts ===");
                    // show the first 10 discounts  using Math.min - no more than 10 products
                    for (int i = 0; i < Math.min(10, allDiscounts.size()); i++) {
                        System.out.println(allDiscounts.get(i));
                    }
                }

                // option 3
                case 3 -> {
                    System.out.println("\n=== Discounts added in the last 24 hours ===");
                    LocalDate now = LocalDate.now();

                    boolean foundNew = false; //  check if anything is printed

                    for (Discount d : allDiscounts) {
                        LocalDate from = LocalDate.parse(d.fromDate, formatter); // assumes fromDate is in format yyyy-MM-dd

                        if (from.isAfter(now.minusDays(1))) { // check if discount was added in the last 24 hours
                            System.out.println(d);
                            foundNew = true;
                        }
                    }

                    if (!foundNew) {
                        System.out.println("No new discounts found in the last 24 hours.");
                    }
                }

                // option 4
                case 4 -> {
                    System.out.println("\n=== Filtered price history ===");

                    // creating filters for products based on store, category and barnd
                    System.out.print("Introduce store keyword (e.g. lidl/profi/kaufland): ");
                    String filterStore = scanner.nextLine().toLowerCase();

                    System.out.print("Introduce product category (e.g. lactate, panificație, legume și fructe, paste făinoase, alimente de bază): ");
                    String filterCategory = scanner.nextLine().toLowerCase();

                    System.out.print("Introduce brand (e.g. Zuzu,Dorna,Generic, Delaco, Cappy, Heidi, Recas, Fuchs, Pufina, K-Classic): ");
                    String filterBrand = scanner.nextLine().toLowerCase();

                    //creating a map which shows the price history
                    Map<String, Map<String, Double>> history = new TreeMap<>();


                    for (String file : filePaths) {
                        if (file.contains("discount")) continue; // ignore the files with discounts
                        if (!file.toLowerCase().contains(filterStore)) continue; // keep the files which contain the selected store

                        List<Product> products = ProductRepository.loadProducts(file);
                        // group the prices by date
                        String rawDate = file.replaceAll("[^0-9\\-]", "");
                        String date = rawDate.length() >= 10 ? rawDate.substring(0, 10) : "fara data";


                        for (Product p : products) {
                            // check if the product corresponds to the selected filters
                            boolean matchesCategory = p.productCategory.toLowerCase().equals(filterCategory);
                            boolean matchesBrand = p.brand.toLowerCase().equals(filterBrand);

                            if (matchesCategory && matchesBrand) {
                                history.putIfAbsent(p.productName, new TreeMap<>()); // add the name of the product in history
                                history.get(p.productName).put(date, p.price); // add  date and price
                            }
                        }
                    }

                    // if the product did not matched with the selected filters than a message is shown
                    if (history.isEmpty()) {
                        System.out.println(" Nu s-au găsit rezultate pe baza filtrului.");

                    }
                    else {
                        // display history prices for each product
                        for (Map.Entry<String, Map<String, Double>> entry : history.entrySet()) {
                            System.out.println("\n  " + entry.getKey());
                            for (Map.Entry<String, Double> dateEntry : entry.getValue().entrySet()) {
                                System.out.println("   " + dateEntry.getKey() + " → " + dateEntry.getValue() + " RON");
                            }
                        }
                    }
                }

                // option 5
                case 5 -> {
                    System.out.println("\n===  Best value per unit ===");

                    //create a map to store the best product category based on price per unit
                    Map<String, Product> best = new HashMap<>();
                    for (Product p : allProducts) {

                        // skip products with 0 quantity to avoid division by 0
                        if (p.packageQuantity == 0) continue;
                        double perUnit = p.price / p.packageQuantity; // calculate the price per unit
                        Product current = best.get(p.productCategory); // get the current best product for this category
                        if (current == null || perUnit < current.price / current.packageQuantity) {
                            best.put(p.productCategory, p);
                        }
                    }

                    // print the best value product per category
                    for (Map.Entry<String, Product> entry : best.entrySet()) {
                        Product p = entry.getValue();
                        double perUnit = p.price / p.packageQuantity; // recalculate price per unit for
                        System.out.printf("%s → %s (%.2f lei/%s)%n", entry.getKey(), p.productName, perUnit, p.packageUnit); // show category, product name, price per unit, and unit type
                    }
                }

                // option 6
                case 6 -> {
                    System.out.println("\n=== Manual price alerts ===");

                    // define alert thresholds for specific keywords (product names) and their maximum acceptable prices
                    Map<String, Double> alerts = Map.of(
                            "lapte", 6.00,
                            "ouă", 5.00,
                            "banane", 3.00,
                            "detergent", 10.00
                    );

                    for (Product p : allProducts) {
                        for (Map.Entry<String, Double> alert : alerts.entrySet()) {
                            String keyword = alert.getKey().toLowerCase(); // keyword to search in product name

                            // check if the product name contains the keyword and the price is below the alert threshold
                            if (p.productName.toLowerCase().contains(keyword) && p.price <= alert.getValue()) {
                                // print an alert message to the user
                                System.out.printf("ALERTĂ: %s este %.2f RON (sub %.2f RON)%n",
                                        p.productName, p.price, alert.getValue());
                            }
                        }
                    }
                }

                case 0 -> System.out.println(" Exit."); // print exist message when the user selects 0
                default -> System.out.println(" Invalid option. Try again.");
            }

        } while (option != 0);

        scanner.close();
    }
}
