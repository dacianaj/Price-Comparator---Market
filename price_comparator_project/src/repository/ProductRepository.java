package repository;

import model.Product;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


// provides functionality for loading product data from a CSV file
public class ProductRepository {


    /**
     * loads products from a CSV file located at the given file path.
     * each line in the file should contain exactly 8 values separated by semicolons ';'.
     *
     * @param filePath the path to the CSV file
     * @return a list of Product objects parsed from the file
     */

    public static List<Product> loadProducts(String filePath) {
        List<Product> products = new ArrayList<>();

        // use try-with-resources to ensure the file is closed automatically
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean skipHeader = true;

            // read the file line by line
            while ((line = reader.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                String[] parts = line.split(";");  // split the line using semicolon delimiter
                if (parts.length != 8) continue; // ensure the line has exactly 8 values, skip otherwise

                //create a Product object
                Product p = new Product(
                        parts[0], parts[1], parts[2], parts[3],
                        Double.parseDouble(parts[4]), parts[5],
                        Double.parseDouble(parts[6]), parts[7]
                );
                products.add(p);   // ddd the product to the list
            }

        } catch (IOException e) {
            System.out.println("Eroare la citire: " + e.getMessage());
        }

        return products;
    }
}