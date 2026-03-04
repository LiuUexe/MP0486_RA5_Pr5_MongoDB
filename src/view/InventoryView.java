package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FlowLayout;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import main.Shop;
import model.Product;

public class InventoryView extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTable tableInventory;

    /**
     * Crea la vista de inventario.
     */
    public InventoryView(Shop shop) {
        setTitle("Inventario de la Tienda");
        setBounds(100, 100, 800, 400);
        setModal(true);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(10, 10));

        // Título
        JLabel lblTitle = new JLabel("Inventario de Productos");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(lblTitle, BorderLayout.NORTH);

        // Columnas de la tabla
        String[] columnNames = {
            "ID",
            "Nombre",
            "Precio (€)",
            "Disponible",
            "Stock"
        };

        // Modelo de la tabla
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Las celdas no son editables
            }
        };

        // Crear tabla
        tableInventory = new JTable(model);
        tableInventory.setFont(new Font("Tahoma", Font.PLAIN, 14));
        tableInventory.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 14));
        tableInventory.setRowHeight(25);

        // Obtener inventario del shop
        ArrayList<Product> products = shop.getInventory();

        // Llenar tabla con datos
        for (Product p : products) {
        	Object[] row = {
        		    p.getId(),
        		    p.getName(),
        		    String.format("%.2f", p.getWholesalerPrice().getValue()),
        		    p.isAvailable() ? "Sí" : "No",
        		    p.getStock()
        		};

            model.addRow(row);
        }

        JScrollPane scrollPane = new JScrollPane(tableInventory);
        contentPanel.add(scrollPane, BorderLayout.CENTER);


        JLabel lblTotal = new JLabel("Total de productos: " + products.size());
        lblTotal.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        contentPanel.add(lblTotal, BorderLayout.SOUTH);


        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton btnBack = new JButton("Volver atrás");
        btnBack.setFont(new Font("Tahoma", Font.PLAIN, 14));
        btnBack.addActionListener(e -> dispose()); // Cierra el diálogo
        buttonPane.add(btnBack);
    }
}
