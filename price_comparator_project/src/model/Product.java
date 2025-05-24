package model;

// represents a product with price, packaging, and category details
public class Product {
    public String productId;
    public String productName;
    public String productCategory;
    public String brand;
    public double packageQuantity;
    public String packageUnit;
    public double price;
    public String currency;


    // constructor to initialize all product fields
    public Product(String productId, String productName, String productCategory, String brand,
                   double packageQuantity, String packageUnit, double price, String currency) {
        this.productId = productId;
        this.productName = productName;
        this.productCategory = productCategory;
        this.brand = brand;
        this.packageQuantity = packageQuantity;
        this.packageUnit = packageUnit;
        this.price = price;
        this.currency = currency;
    }

    // returns a formatted string representation of the product
    @Override
    public String toString() {
        return String.format("%s | %s | %s | %s | %.2f %s | %.2f %s",
                productId, productName, productCategory, brand,
                packageQuantity, packageUnit, price, currency);
    }
}