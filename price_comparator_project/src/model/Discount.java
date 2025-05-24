package model;

// represents a discount applied to a product
public class Discount {
    public String productId;
    public String productName;
    public String brand;
    public double packageQuantity;
    public String packageUnit;
    public String productCategory;
    public String fromDate;
    public String toDate;
    public double percentageOfDiscount;

    // constructor to initialize all fields
    public Discount(String productId, String productName, String brand,
                    double packageQuantity, String packageUnit,
                    String productCategory, String fromDate, String toDate,
                    double percentageOfDiscount) {
        this.productId = productId;
        this.productName = productName;
        this.brand = brand;
        this.packageQuantity = packageQuantity;
        this.packageUnit = packageUnit;
        this.productCategory = productCategory;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.percentageOfDiscount = percentageOfDiscount;
    }

    // formatted string for displaying discount info
    @Override
    public String toString() {
        return String.format("%s | %s | %s | %.2f %s | %s | %s - %s | %.1f%%",
                productId, productName, brand, packageQuantity, packageUnit,
                productCategory, fromDate, toDate, percentageOfDiscount);
    }
}