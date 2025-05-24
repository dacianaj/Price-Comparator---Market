package repository;

import model.Discount;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


// provides functionality for loading discounts from a CSV file
public class DiscountRepository {

    // reads discounts from the given file and returns them as a list
    public static List<Discount> loadDiscounts(String filePath) {
        List<Discount> discounts = new ArrayList<>();

        // try-with-resources to automatically close the file after reading
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean skipHeader = true;

            // read the file line by line
            while ((line = reader.readLine()) != null) {
                // skip the first line
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                // split the line into fields using semicolon as separator
                String[] parts = line.split(";");
                if (parts.length != 9) continue;  // skip the line if it doesn't contain exactly 9 values

                // create a Discount object
                Discount d = new Discount(
                        parts[0], parts[1], parts[2],
                        Double.parseDouble(parts[3]), parts[4],
                        parts[5], parts[6], parts[7],
                        Double.parseDouble(parts[8])
                );
                discounts.add(d); // add the discount to the list
            }

        } catch (IOException e) {
            System.out.println("Eroare la citire: " + e.getMessage());
        }

        return discounts;
    }
}