import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CashRegister {
    // GUI components
    JFrame mainFrame; // Main application window
    JTextArea receiptDisplay; // Area to display the receipt
    JTextArea productInputField; // Field to input product name
    JTextArea quantityInputField; // Field to input product quantity
    JButton addProductButton; // Button to add product to receipt
    JButton completePaymentButton; // Button to complete the payment
    JButton clearLastRowButton; // Button to clear the last row
    JButton clearAllButton; // Button to clear all rows
    String lastSelectedProduct = null; // To track the last selected product
    double totalAmount = 0; // Total amount for the current receipt
    double totalGross = 0; // Total gross amount
    int totalProducts = 0; // Total number of products
    double totalTaxes = 0; // Total taxes
    int receiptNumber = 1; // To track the receipt number
    private Receipt currentReceipt; // Current receipt object

    // List to hold products
    ArrayList<Product> productList = new ArrayList<>();

    public CashRegister() {
        // Initialize main frame
        mainFrame = new JFrame("IOT24 OOP Course: Individual Assignement - Tarek Aaraichi");
        
        loadProductsFromFile("Products.txt"); // Load products from file
        
        createReceiptArea(); // Create area to display receipt
        createQuickButtonsArea(); // Create quick buttons for products
        createAddArea(); // Create area for adding products
        
        // Initialize the receipt with the header
        run(); 

        // Frame settings
        mainFrame.getContentPane().setBackground(Color.LIGHT_GRAY);
        mainFrame.setResizable(false);
        mainFrame.setSize(815, 600);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(null);
        mainFrame.setVisible(true);
        
        currentReceipt = new Receipt(); // Initialize the Receipt object
    }

    // Method to create area for adding products
    private void createAddArea() {
        // Input field for product name
        productInputField = new JTextArea();
        productInputField.setBounds(20, 500, 200, 30);
        productInputField.setFont(new Font("Arial BLACK", Font.PLAIN, 14));
        productInputField.setBackground(new Color(255, 239, 204)); // Set to Light Beige background 
        productInputField.setEditable(false); // Make the text area non-editable
        productInputField.setText("Please, select a product!");
        mainFrame.add(productInputField);

        // Label for quantity input
        JLabel quantityLabel = new JLabel("Quantity");
        quantityLabel.setBounds(230, 475, 50, 30);
        quantityLabel.setForeground(Color.DARK_GRAY);
        mainFrame.add(quantityLabel);

        // Label for product name input
        JLabel productLabel = new JLabel("Product name");
        productLabel.setBounds(20, 475, 250, 30);
        productLabel.setForeground(Color.DARK_GRAY);
        mainFrame.add(productLabel);

        // Input field for quantity
        quantityInputField = new JTextArea();
        quantityInputField.setBounds(230, 500, 50, 30);
        quantityInputField.setFont(new Font("Arial Black", Font.PLAIN, 14));
        quantityInputField.setBackground(new Color(255, 239, 204));
        mainFrame.add(quantityInputField);

        // Button to add product to receipt
        addProductButton = new JButton("Add");
        addProductButton.setBounds(290, 490, 70, 65);
        addProductButton.setFont(new Font("Arial White", Font.BOLD, 12));
        mainFrame.add(addProductButton);

        // Button to complete payment
        completePaymentButton = new JButton("Pay");
        completePaymentButton.setBounds(600, 490, 100, 65); 
        completePaymentButton.setFont(new Font("Arial White", Font.BOLD, 12));
        mainFrame.add(completePaymentButton);

        // Button to clear the last row
        JButton clearLastRowButton = new JButton("Clear Last Row");
        clearLastRowButton.setBounds(370, 490, 130, 30);
        clearLastRowButton.setFont(new Font("Arial White", Font.BOLD, 12));
        mainFrame.add(clearLastRowButton);

        // Button to clear all rows
        JButton clearAllButton = new JButton("Clear All");
        clearAllButton.setBounds(370, 525, 130, 30);
        clearAllButton.setFont(new Font("Arial White", Font.BOLD, 12));
        mainFrame.add(clearAllButton);

        // Action listener for adding product to receipt
        addProductButton.addActionListener(event -> {
            String quantityText = quantityInputField.getText().trim(); // Get the text from the quantity input field
            
            // Check if both product and quantity inputs are empty
            if ((lastSelectedProduct == null || lastSelectedProduct.isEmpty()) && quantityText.isEmpty()){
                JOptionPane.showMessageDialog(mainFrame, "Please select a product then enter a quantity first.", "Warning", JOptionPane.WARNING_MESSAGE);
                clearProductInputAndFocus();     

            } else if (lastSelectedProduct != null) { // Check if a product has been selected
                if (!quantityText.isEmpty()) {
                    try {
                        int quantity = Integer.parseInt(quantityText); // Try to parse the quantity
                        if (quantity > 0) { // Check if quantity is greater than zero
                            Product matchedProduct = null;

                            // Loop to find the product based on the last clicked product name
                            for (Product product : productList) {
                                if (product.getProductName().equals(lastSelectedProduct)) {
                                    matchedProduct = product; // Assign matched product
                                    break;
                                }
                            }

                            if (matchedProduct != null) { // Check if product was found
                                addProductToReceipt(matchedProduct, quantity); // Add or update the product in the receipt
                                clearProductInputAndFocus();

                            } else {
                                // Show error dialog if product is not found
                                JOptionPane.showMessageDialog(mainFrame, "Product not found.", "Error", JOptionPane.ERROR_MESSAGE);
                                //productInputField.setText("Please, select a product!"); // Clear product name input
                                //productInputField.requestFocus();
                                //quantityInputField.setText(""); // Clear quantity input
                                clearProductInputAndFocus();
                            }

                        } else {
                            // Show error dialog for quantity less than or equal to zero
                            JOptionPane.showMessageDialog(mainFrame, "Quantity must be greater than zero.", "Error", JOptionPane.ERROR_MESSAGE);
                            quantityInputField.setText(""); // Clear invalid quantity input
                            quantityInputField.requestFocus(); // Focus on the quantity input field
                        }

                    } catch (NumberFormatException ex) {
                        // Show error dialog for invalid number format
                        JOptionPane.showMessageDialog(mainFrame, "Invalid quantity. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                        quantityInputField.setText(""); // Clear invalid quantity input
                        quantityInputField.requestFocus(); // Focus on the quantity input field
                    }

                } else {
                    // Show error dialog for empty quantity input
                    JOptionPane.showMessageDialog(mainFrame, "Please enter a quantity.", "WARNING", JOptionPane.WARNING_MESSAGE);
                    quantityInputField.requestFocus(); // Focus on the quantity input field
                }

            } else {
                // Show error dialog for no product selected
                JOptionPane.showMessageDialog(mainFrame, "No product selected. Please select a product first.", "Warning", JOptionPane.WARNING_MESSAGE);
                clearProductInputAndFocus();    
            }
        });

        // ActionListener for "Clear Last Row" button
        clearLastRowButton.addActionListener(event -> {
            if (!currentReceipt.getReceiptRows().isEmpty()) {
                // Remove the last row from the active receipt
                currentReceipt.getReceiptRows().remove(currentReceipt.getReceiptRows().size() - 1);
                // Update the receipt display after removal
                updateReceiptDisplay();
                productInputField.requestFocus();

            } else {
                // Show a message if there are no rows to delete
                JOptionPane.showMessageDialog(mainFrame, "No rows to clear!", "Warning", JOptionPane.WARNING_MESSAGE);
                clearProductInputAndFocus();
            }
        });

        // ActionListener for "Clear All" button
        clearAllButton.addActionListener(event -> {
            if (!currentReceipt.getReceiptRows().isEmpty()) {
                // Clear all rows from the active receipt
                currentReceipt.getReceiptRows().clear();     
                // Update the receipt display after clearing all rows
                updateReceiptDisplay();
                productInputField.requestFocus();

            } else {
                // Show a message if there are no rows to clear
                JOptionPane.showMessageDialog(mainFrame, "The receipt is already empty!", "Info", JOptionPane.INFORMATION_MESSAGE);
                clearProductInputAndFocus();
            }
        });

        // Action listener for Pay button
        completePaymentButton.addActionListener(event -> {
            // Check if the receipt display is empty
            if (currentReceipt.getReceiptRows().isEmpty()) {
                // Show a message dialog prompting the user to add a product
                JOptionPane.showMessageDialog(mainFrame, "Please add a product to the receipt before proceeding to payment.", "Warning", JOptionPane.WARNING_MESSAGE);
                clearProductInputAndFocus();
                return; // Exit the method if no products are added
            }
   
            // Display totals
            receiptDisplay.append("----------------------------------------------------\n");
            receiptDisplay.append(String.format("%-42s %6.2f%n", "Gross Total: \n", totalGross));
            receiptDisplay.append(String.format("%-42s %6.2f%n", "Taxes Total: \n", totalTaxes));
            receiptDisplay.append(String.format("%-42s %6.2f%n", "Total Amount: \n", totalAmount));
            receiptDisplay.append(String.format("%-42s %d%n", "Total Products: \n", totalProducts));
            receiptDisplay.append("----------------------------------------------------\n");
            receiptDisplay.append("Thank you for your purchase and welcome back! :)\n");
        
            // Ask if the user wants to start a new receipt
            JOptionPane.showMessageDialog(mainFrame, "Start a new receipt?", "Payment completed!", JOptionPane.PLAIN_MESSAGE);
            
            // Reset fields for a new receipt
            receiptDisplay.setText(""); // Clear the receipt display
            lastSelectedProduct = null; // Reset last selected product
            totalAmount = 0; // Reset total amount
            totalGross = 0; // Reset gross total
            totalTaxes = 0; // Reset total taxes
            totalProducts = 0; // Reset product count
            receiptNumber++;
            currentReceipt.getReceiptRows().clear(); // Clear the current receipt
            updateReceiptDisplay();
            clearProductInputAndFocus();
        });
    }

    private void clearProductInputAndFocus() {
            productInputField.setText("Please, select a product!"); // Clear product name input
            quantityInputField.setText(""); // Clear quantity input
            productInputField.requestFocus(); // Get the input focus on the component
    }

    // Method to load products from a file
    private void loadProductsFromFile(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim(); // Read each line from the file
                if (line.isEmpty()) break; // Stop if an empty line is encountered
                String[] parts = line.split(","); // Split the line by commas
                String productName = parts[0].trim(); // Get product name
                double productPrice = Double.parseDouble(parts[1].trim()); // Get product price
                float productTaxes = Float.parseFloat(parts[2].trim().replace("%", "")) / 100; // Get product taxes
                productList.add(new Product(productName, productPrice, productTaxes)); // Add product to the list
            }

        } catch (FileNotFoundException e) {
            System.out.println("File was not found.");

        } catch (NumberFormatException e) {
            System.out.println("Invalid number format in the file."); // Handle number format errors
        }
    }

    // Method to create the area for displaying the receipt
    private void createReceiptArea() {
        receiptDisplay = new JTextArea(); // Create a JTextArea for the receipt display
        receiptDisplay.setEditable(false); // Make it non-editable
        receiptDisplay.setFont(new Font("Arial", Font.PLAIN, 14)); // Set font
        receiptDisplay.setBackground(Color.white); // Set background color
        // receiptDisplay.setBackground(new Color(255, 255, 240)); // Ivory
        //receiptDisplay.setBackground(new Color(240, 248, 255)); // Alice Blue
        //receiptDisplay.setBackground(new Color(255, 239, 204)); // Set to light beige background
        receiptDisplay.setLineWrap(true); // Enable line wrapping
        receiptDisplay.setWrapStyleWord(true); // Wrap at word boundaries

        // Scroll pane to hold the receipt display
        JScrollPane scrollPane = new JScrollPane(receiptDisplay); // 
        scrollPane.setBounds(520, 20, 270, 450); // Set bounds for the scroll pane 20, 20, 500, 450
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); // Always show vertical scroll bar
        mainFrame.add(scrollPane); // Add the scroll pane to the main frame
    }

    // Method to create buttons for quick product selection
    private void createQuickButtonsArea() {
        // Create a panel for product buttons
        JPanel productButtonPanel = new JPanel(); 
        productButtonPanel.setLayout(new GridLayout(0, 4, 5, 5)); // Set layout to grid
        //productButtonPanel.setBounds(20, 480, 750, 50); // Set bounds for the panel
        JScrollPane productCatalogScroll = new JScrollPane(productButtonPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED); // Add scroll pane to handle overflow
        productCatalogScroll.setBounds(20, 20, 500, 450); // Set bounds for scroll pane 520, 50, 270, 450
        productButtonPanel.setBackground(Color.GREEN); // Set background color to green
        mainFrame.add(productCatalogScroll); // Add scroll pane to the main frame

        // Create buttons for each product in the product list
        for (Product product : productList) {
            JButton productButton = new JButton(product.getProductName()); // Create button with product name
            //productButton.setPreferredSize(new Dimension(70, 30)); // Set button size
            productButton.setPreferredSize(new Dimension(100, 40)); // Set button size
            productButtonPanel.add(productButton); // Add button to the panel
            
            // Add action listener for product button
            productButton.addActionListener(event -> {
                lastSelectedProduct = product.getProductName(); // Set the last selected product
                productInputField.setText(lastSelectedProduct); // Display the product name in the input field
                quantityInputField.requestFocus(); // Focus on the quantity input field
            });
        }        

    }

    // Method to update the receipt display area
    private void updateReceiptDisplay() {
        // Clear the existing display
        receiptDisplay.setText("");
        //run();
        receiptDisplay.append("Receipt #" + receiptNumber + "\n"); // Display the receipt number
        receiptDisplay.append("Date: " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "\n"); // Display current time
        receiptDisplay.append("----------------------------------------------------\n"); // Separator

        // Loop through all receipt rows and add them to the display
        for (ReceiptRow row : currentReceipt.getReceiptRows()) {
            receiptDisplay.append(row.toString()); // Append each row to the display
        }
        // Update totals
        updateTotals();
    }

    // Method to add product to the receipt
    private void addProductToReceipt(Product product, int quantity) {
            // Create a ReceiptRow for the product
        boolean productExists = false; // Flag to check if product exists
        for (ReceiptRow newRow : currentReceipt.getReceiptRows()) { // Check if the product exists
            if (newRow.getProductName().equals(product.getProductName())) {
                newRow.setProductQuantity(newRow.getProductQuantity() + quantity); // Update quantity by adding the entered quantity
                productExists = true; // Mark as product exists
                break;
            }
        }
        if (!productExists) {
            currentReceipt.addReceiptRow(new ReceiptRow(product.getProductName(), quantity, product.getProductPrice(), product.getProductTaxRate())); // Add new row if not exists
        }
        updateReceiptDisplay(); // Update the display of the receipt
    }

    // Method to update totals based on current receipt rows
    private void updateTotals() {
        totalAmount = 0; // Reset total amount
        totalGross = 0; // Reset gross total
        totalTaxes = 0; // Reset total taxes
        totalProducts = 0; // Reset total products

        // Loop through each receipt row and accumulate totals
        for (ReceiptRow row : currentReceipt.getReceiptRows()) {
            totalProducts += row.getProductQuantity(); // Accumulate product quantities
            totalGross += row.calculateNetPrice(); // Accumulate gross totals
            totalTaxes += row.calculateProductTaxes(); // Accumulate taxes
            totalAmount += row.calculateTotal(); // Accumulate total amounts
        }
    }

    // Method to initialize the receipt header and display
    public void run() {
        // Append receipt header
        receiptDisplay.setText("Receipt #" + receiptNumber + "\n"); // Display receipt number
        receiptDisplay.append("Date: " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "\n"); // Display timestamp
        receiptDisplay.append("----------------------------------------------------\n"); // Separator
        //productInputField.requestFocus();
        clearProductInputAndFocus();
    }
}
