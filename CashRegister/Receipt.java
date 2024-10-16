import java.util.ArrayList;

public class Receipt {
    // List to hold the rows of the receipt (each row represents a product and its details)
    private ArrayList<ReceiptRow> receiptRows;

    /**
     * Constructor to initialize an empty receipt.
     * Creates an empty list of receipt rows.
     */
    public Receipt() {
        receiptRows = new ArrayList<>();  // Initialize the list of rows
    }

    /**
     * Adds a new row to the receipt.
     * 
     * @param row The ReceiptRow object representing a product added to the receipt.
     */
    public void addReceiptRow(ReceiptRow row) {
        receiptRows.add(row);  // Add the row to the receipt
    }

    /**
     * Getter for the list of rows in the receipt.
     * 
     * @return The list of ReceiptRow objects.
     */
    public ArrayList<ReceiptRow> getReceiptRows() {
        return receiptRows;  // Return the list of rows
    }

    /**
     * Calculates the total price (before taxes) for all products in the receipt.
     * 
     * @return The total price of all items.
     */
    public double calculateTotal() {
        double total = 0;
        for (ReceiptRow row : receiptRows) {
            total += row.calculateTotal();  // Sum up the total for each row
        }
        return total;
    }

    /**
     * Calculates the total tax amount for all products in the receipt.
     * 
     * @return The total tax amount.
     */
    public double calculateTotalTaxes() {
        double totalTaxes = 0;
        for (ReceiptRow row : receiptRows) {
            totalTaxes += row.calculateProductTaxes();  // Sum up the tax amount for each row
        }
        return totalTaxes;
    }

    /**
     * Calculates the total number of products in the receipt (across all rows).
     * 
     * @return The total quantity of products.
     */
    public int calculateTotalProducts() {
        int totalProducts = 0;
        for (ReceiptRow row : receiptRows) {
            totalProducts += row.getProductQuantity();  // Sum up the quantity for each row
        }
        return totalProducts;
    }

    /**
     * Calculates the total gross amount (total price including taxes).
     * 
     * @return The total gross amount (price + taxes).
     */
    public double calculateTotalGross() {
        return calculateTotal() - calculateTotalTaxes();  // Sum of total price and taxes
    }

    /**
     * Converts the receipt to a string format.
     * 
     * @return A string representation of the receipt (each row formatted).
     */

    @Override
    public String toString() {
        String receiptString = "";  // Start with an empty string to hold the receipt
        for (ReceiptRow row : receiptRows) {  // Go through each row (item) in the receipt
            receiptString += row.toString();  // Add the row's details to the receipt string
        }
        return receiptString;  // Return the complete receipt as a string
    }

    /**
     * Clears all the rows in the receipt (resetting it).
     */
    public void clearReceiptRows() {
        receiptRows.clear();  // Clear all rows from the receipt
    }
}