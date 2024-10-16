public class Product {
    // Private fields for storing product details
    private String productName;    // Name of the product
    private double productPrice;   // Price of the product
    private float productTaxRate;  // Tax rate (stored as a fraction)

    /**
     * Constructor to initialize a product with a name, price, and tax rate.
     * 
     * @param productName The name of the product.
     * @param productPrice The price of the product.
     * @param productTaxRate The tax rate of the product (as a fraction).
     */
    public Product(String productName, double productPrice, float productTaxRate) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productTaxRate = productTaxRate;
    }

    /**
     * Getter for the product's name.
     * 
     * @return The name of the product.
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Setter for the product's name.
     * 
     * @param productName The new name to assign to the product.
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * Getter for the product's price.
     * 
     * @return The price of the product.
     */
    public double getProductPrice() {
        return productPrice;
    }

    /**
     * Setter for the product's price.
     * 
     * @param productPrice The new price to assign to the product.
     */
    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    /**
     * Getter for the product's tax rate.
     * 
     * @return The tax rate of the product (as a fraction).
     */
    public float getProductTaxRate() {
        return productTaxRate;
    }

    /**
     * Setter for the product's tax rate.
     * 
     * @param productTaxRate The new tax rate to assign to the product (as a fraction).
     */
    public void setProductTaxRate(float productTaxRate) {
        this.productTaxRate = productTaxRate;
    }
}