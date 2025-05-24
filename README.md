# Price-Comparator---Market
A service that helps users compare prices of everyday grocery items  across different supermarket chains.


## Project Structure

- `model/` – contains classes for products and discounts  
- `repository/` – responsible for loading data from CSV files  
- `service/` – core comparison and filtering logic  
- `CSVReader.java` – main entry point of the application  
- `.csv` – raw data files with product prices and discounts


## How to run the application

1. Make sure you have Java 17 or higher installed.
2. Open the project in IntelliJ IDEA (or another Java-compatible IDE).
3. Run the `CSVReader.java` class from the `main` method.


## Features

- Display the cheapest available products across all stores
- Show the top 10 biggest discounts
- Detect and list new discounts added in the last 24 hours
- View price history of filtered products by store, brand, and category
- Identify the best value per unit for each product category
- Set up manual price alerts for specific products
