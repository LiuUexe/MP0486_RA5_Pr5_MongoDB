package view;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.Font;
import java.awt.event.*;

import main.Shop;
import utils.Constants;

public class ShopView extends JFrame implements ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;
	private Shop shop;

	private JPanel contentPane;
	private JButton btnShowCash;
	private JButton btnAddProduct;
	private JButton btnAddStock;
	private JButton btnRemoveProduct;
	private JButton btnViewInventory; // ← NUEVO botón
	private JButton btnExportInventory;

	public ShopView() {
		setTitle("MiTienda.com - Menú principal");
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);

		// Crear tienda
		shop = new Shop();
		shop.loadInventory();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblShowCash = new JLabel("Seleccione o pulse una opción:");
		lblShowCash.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblShowCash.setBounds(57, 20, 300, 14);
		contentPane.add(lblShowCash);

		// 0. Exportar inventario
		btnExportInventory = new JButton("0. Exportar inventario");
		btnExportInventory.setHorizontalAlignment(SwingConstants.LEFT);
		btnExportInventory.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnExportInventory.setBounds(99, 40, 236, 40);
		btnExportInventory.addActionListener(this);
		contentPane.add(btnExportInventory);

		// 1. Contar caja
		btnShowCash = new JButton("1. Contar caja");
		btnShowCash.setHorizontalAlignment(SwingConstants.LEFT);
		btnShowCash.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnShowCash.setBounds(99, 90, 236, 40);
		btnShowCash.addActionListener(this);
		contentPane.add(btnShowCash);

		// 2. Añadir producto
		btnAddProduct = new JButton("2. Añadir producto");
		btnAddProduct.setHorizontalAlignment(SwingConstants.LEFT);
		btnAddProduct.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnAddProduct.setBounds(99, 140, 236, 40);
		btnAddProduct.addActionListener(this);
		contentPane.add(btnAddProduct);

		// 3. Añadir stock
		btnAddStock = new JButton("3. Añadir stock");
		btnAddStock.setHorizontalAlignment(SwingConstants.LEFT);
		btnAddStock.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnAddStock.setBounds(99, 190, 236, 40);
		btnAddStock.addActionListener(this);
		contentPane.add(btnAddStock);

		// 4. Ver inventario
		btnViewInventory = new JButton("4. Ver inventario");
		btnViewInventory.setHorizontalAlignment(SwingConstants.LEFT);
		btnViewInventory.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnViewInventory.setBounds(99, 240, 236, 40);
		btnViewInventory.addActionListener(this);
		contentPane.add(btnViewInventory);

		// 9. Eliminar producto
		btnRemoveProduct = new JButton("9. Eliminar producto");
		btnRemoveProduct.setHorizontalAlignment(SwingConstants.LEFT);
		btnRemoveProduct.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnRemoveProduct.setBounds(99, 290, 236, 40);
		btnRemoveProduct.addActionListener(this);
		contentPane.add(btnRemoveProduct);

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == '1')
			openCashView();
		if (e.getKeyChar() == '2')
			openProductView(Constants.OPTION_ADD_PRODUCT);
		if (e.getKeyChar() == '3')
			openProductView(Constants.OPTION_ADD_STOCK);
		if (e.getKeyChar() == '4')
			openInventoryView(); // ← NUEVO
		if (e.getKeyChar() == '9')
			openProductView(Constants.OPTION_REMOVE_PRODUCT);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnShowCash)
			openCashView();
		if (e.getSource() == btnAddProduct)
			openProductView(Constants.OPTION_ADD_PRODUCT);
		if (e.getSource() == btnAddStock)
			openProductView(Constants.OPTION_ADD_STOCK);
		if (e.getSource() == btnViewInventory)
			openInventoryView(); // ← NUEVO
		if (e.getSource() == btnRemoveProduct)
			openProductView(Constants.OPTION_REMOVE_PRODUCT);
		if (e.getSource() == btnExportInventory) {
			boolean success = shop.writeInventory();
			if (success) {
				JOptionPane.showMessageDialog(this, "Inventario exportado correctamente.", "Exportación completada",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "Error al exportar el inventario.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	// ----- Métodos para abrir las vistas -----

	public void openCashView() {
		CashView dialog = new CashView(shop);
		dialog.setSize(400, 300);
		dialog.setModal(true);
		dialog.setVisible(true);
	}

	public void openProductView(int option) {
		ProductView dialog = new ProductView(shop, option);
		dialog.setSize(400, 400);
		dialog.setModal(true);
		dialog.setVisible(true);
	}

	// NUEVO método para abrir el inventario
	public void openInventoryView() {
		InventoryView dialog = new InventoryView(shop);
		dialog.setSize(800, 400);
		dialog.setModal(true);
		dialog.setVisible(true);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyChar() == '0') {
			boolean success = shop.writeInventory();
			if (success) {
				JOptionPane.showMessageDialog(this, "Inventario exportado correctamente.", "Exportación completada",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "Error al exportar el inventario.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}

	}

}
