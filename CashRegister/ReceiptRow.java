public class ReceiptRow {
    // The name of the product
    private String productName;
    // The quantity of the product purchased
    private int productQuantity;
    // The price of a single unit of the product
    private double productPrice;
    // The tax rate for the product, stored as a fraction (e.g., 0.2 for 20%)
    private float productTaxRate;

    /**
     * Constructor to initialize a receipt row with product details.
     * 
     * @param name The name of the product.
     * @param quantity The quantity of the product purchased.
     * @param price The price of the product per unit.
     * @param taxes The tax rate for the product as a fraction.
     */
    public ReceiptRow(String name, int quantity, double price, float taxes) {
        this.productName = name;  // Initialize product name
        this.productQuantity = quantity;  // Initialize product quantity
        this.productPrice = price;  // Initialize product price
        this.productTaxRate = taxes;  // Initialize tax rate
    }

    // Getters

    /**
     * Getter for the product name.
     * 
     * @return The name of the product.
     */
    public String getProductName() {
        return productName;  // Return product name
    }

    /**
     * Getter for the product quantity.
     * 
     * @return The quantity of the product.
     */
    public int getProductQuantity() {
        return productQuantity;  // Return product quantity
    }

    /**
     * Getter for the product price.
     * 
     * @return The price of the product per unit.
     */
    public double getProductPrice() {
        return productPrice;  // Return product price
    }

    // Setters

    /**
     * Setter for the product quantity.
     * 
     * @param quantity The new quantity of the product.
     */
    public void setProductQuantity(int quantity) {
        this.productQuantity = quantity;  // Update product quantity
    }

    /**
     * Setter for the product price.
     * 
     * @param productPrice The new price of the product per unit.
     */
    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;  // Update product price
    }

    /**
     * Calculates the total price for the product (excluding taxes).
     * 
     * @return The net total price for the product.
     */
    public double calculateTotal() {
        return productQuantity * productPrice;  // Return total price (quantity * price)
    }

    /**
     * Calculates the total tax amount for this product.
     * 
     * @return The total tax for the product.
     */
    public double calculateProductTaxes() {
        return productPrice * productTaxRate;  // Return calculated tax amount
    }

    /**
     * Calculates the total net amount for this product.
     * 
     * @return The total net price for the product.
     */
    public double calculateNetPrice() {
        return calculateTotal() - calculateProductTaxes();  // Return calculated gross amount
    }

    /**
     * Converts the receipt row details to a string format.
     * 
     * @return A formatted string representing the receipt row.
     */
    @Override
    public String toString() {
        return String.format("%-25s %3d * %6.2f = %7.2f (Taxes: %6.2f)%n",
                productName, 
                productQuantity, 
                productPrice, 
                calculateTotal(), 
                calculateProductTaxes());  // Format the string for output
    }
}