import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

class StockItem {
    String code;
    String name;
    int stock;
    double unitPrice;

    public StockItem(String code, String name, int stock, double unitPrice) {
        this.code = code;
        this.name = name;
        this.stock = stock;
        this.unitPrice = unitPrice;
    }
}

public class InventoryManagerUI extends JFrame {
    private JTextField codeField, nameField, stockField, priceField;
    private JButton insertBtn, modifyBtn, removeBtn;
    private DefaultTableModel stockTableModel;
    private JTable stockTable;

    private final Map<String, StockItem> stockData = new HashMap<>();
    private final Map<String, Integer> rowLookup = new HashMap<>();

    public InventoryManagerUI() {
        setTitle("Smart Inventory Manager");
        setSize(850, 480);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        add(buildFormPanel(), BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);
    }

    private JPanel buildFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Product Details"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel codeLabel = new JLabel("Item Code:");
        JLabel nameLabel = new JLabel("Item Name:");
        JLabel stockLabel = new JLabel("Stock:");
        JLabel priceLabel = new JLabel("Unit Price:");

        codeField = new JTextField(12);
        nameField = new JTextField(12);
        stockField = new JTextField(12);
        priceField = new JTextField(12);

        insertBtn = new JButton("Add Item");
        modifyBtn = new JButton("Update Item");
        removeBtn = new JButton("Delete Item");

        // Row 1
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(codeLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(codeField, gbc);
        gbc.gridx = 2;
        formPanel.add(nameLabel, gbc);
        gbc.gridx = 3;
        formPanel.add(nameField, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(stockLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(stockField, gbc);
        gbc.gridx = 2;
        formPanel.add(priceLabel, gbc);
        gbc.gridx = 3;
        formPanel.add(priceField, gbc);

        // Row 3
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.add(insertBtn);
        buttonPanel.add(modifyBtn);
        buttonPanel.add(removeBtn);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 4;
        formPanel.add(buttonPanel, gbc);

        // Button Actions
        insertBtn.addActionListener(e -> insertItem());
        modifyBtn.addActionListener(e -> modifyItem());
        removeBtn.addActionListener(e -> removeItem());

        return formPanel;
    }

    private JScrollPane buildTablePanel() {
        String[] headers = {"Item Code", "Item Name", "Stock", "Unit Price"};
        stockTableModel = new DefaultTableModel(headers, 0);
        stockTable = new JTable(stockTableModel);
        stockTable.setRowHeight(22);
        stockTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        stockTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 15));

        stockTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = stockTable.getSelectedRow();
                codeField.setText(stockTableModel.getValueAt(row, 0).toString());
                nameField.setText(stockTableModel.getValueAt(row, 1).toString());
                stockField.setText(stockTableModel.getValueAt(row, 2).toString());
                priceField.setText(stockTableModel.getValueAt(row, 3).toString());
            }
        });

        return new JScrollPane(stockTable);
    }

    private void insertItem() {
        String code = codeField.getText().trim();
        if (stockData.containsKey(code)) {
            showMessage("Item code already exists.");
            return;
        }

        StockItem item = parseFields(code);
        if (item == null) return;

        stockData.put(code, item);
        stockTableModel.addRow(new Object[]{item.code, item.name, item.stock, item.unitPrice});
        rowLookup.put(code, stockTableModel.getRowCount() - 1);

        resetFields();
    }

    private void modifyItem() {
        String code = codeField.getText().trim();
        if (!stockData.containsKey(code)) {
            showMessage("Item not found.");
            return;
        }

        StockItem updated = parseFields(code);
        if (updated == null) return;

        stockData.put(code, updated);
        int row = rowLookup.get(code);
        stockTableModel.setValueAt(updated.name, row, 1);
        stockTableModel.setValueAt(updated.stock, row, 2);
        stockTableModel.setValueAt(updated.unitPrice, row, 3);

        resetFields();
    }

    private void removeItem() {
        String code = codeField.getText().trim();
        if (!stockData.containsKey(code)) {
            showMessage("Item not found.");
            return;
        }

        int row = rowLookup.get(code);
        stockTableModel.removeRow(row);
        stockData.remove(code);
        rowLookup.remove(code);

        // Recalculate row indexes
        rowLookup.replaceAll((key, val) -> val > row ? val - 1 : val);

        resetFields();
    }

    private StockItem parseFields(String code) {
        try {
            String name = nameField.getText().trim();
            int stock = Integer.parseInt(stockField.getText().trim());
            double price = Double.parseDouble(priceField.getText().trim());
            return new StockItem(code, name, stock, price);
        } catch (NumberFormatException e) {
            showMessage("Please enter valid numbers for Stock and Unit Price.");
            return null;
        }
    }

    private void resetFields() {
        codeField.setText("");
        nameField.setText("");
        stockField.setText("");
        priceField.setText("");
    }

    private void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(InventoryManagerUI::new);
    }
}