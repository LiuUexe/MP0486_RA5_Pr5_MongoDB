package main;

import model.Product;
import model.Sale;
import model.Amount;
import model.Client;
import model.Employee;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import dao.Dao;
import dao.DaoImplHibernate;
import dao.DaoImplMongoDB;

public class Shop {
	private Amount cash = new Amount(100.00);
	private ArrayList<Product> inventory;
	private int numberProducts;
	private ArrayList<Sale> sales;
	private int numberSales;

	// PDF → atributo debe ser tipo Dao
	private Dao dao;

	final static double TAX_RATE = 1.04;

	public Shop() {
		dao = new DaoImplMongoDB(); // Usamos MongoDb por defecto
		dao.connect();
		inventory = new ArrayList<Product>();
		sales = new ArrayList<Sale>();
	}

	public Amount getCash() {
		return cash;
	}

	public void setCash(Amount cash) {
		this.cash = cash;
	}

	public ArrayList<Product> getInventory() {
		return inventory;
	}

	public void setInventory(ArrayList<Product> inventory) {
		this.inventory = inventory;
	}

	public int getNumberProducts() {
		return numberProducts;
	}

	public void setNumberProducts(int numberProducts) {
		this.numberProducts = numberProducts;
	}

	public ArrayList<Sale> getSales() {
		return sales;
	}

	public void setSales(ArrayList<Sale> sales) {
		this.sales = sales;
	}

	public int getNumberSales() {
		return numberSales;
	}

	public void setNumberSales(int numberSales) {
		this.numberSales = numberSales;
	}

	public static void main(String[] args) {
		Shop shop = new Shop();

		shop.loadInventory();
		shop.initSession();

		Scanner scanner = new Scanner(System.in);
		int opcion = 0;
		boolean exit = false;

		do {
			System.out.println("\n===========================");
			System.out.println("Menu principal miTienda.com");
			System.out.println("===========================");
			System.out.println("1) Contar caja");
			System.out.println("2) Añadir producto");
			System.out.println("3) Añadir stock");
			System.out.println("4) Marcar producto proxima caducidad");
			System.out.println("5) Ver inventario");
			System.out.println("6) Venta");
			System.out.println("7) Ver ventas");
			System.out.println("8) Ver venta total");
			System.out.println("9) Eliminar producto");
			System.out.println("10) Salir programa");
			System.out.print("Seleccione una opción: ");
			opcion = scanner.nextInt();

			switch (opcion) {
			case 1:
				shop.showCash();
				break;

			case 2:
				shop.addProduct();
				break;

			case 3:
				shop.addStock();
				break;

			case 4:
				shop.setExpired();
				break;

			case 5:
				shop.showInventory();
				break;

			case 6:
				shop.sale();
				break;

			case 7:
				shop.showSales();
				break;

			case 8:
				shop.showSalesAmount();
				break;

			case 9:
				shop.removeProduct();
				break;

			case 10:
				shop.writeInventory();

				System.out.println("Cerrando programa ...");
				exit = true;
				break;
			}

		} while (!exit);
	}

	private void initSession() {
		Employee employee = new Employee("test");
		boolean logged = false;

		do {
			Scanner scanner = new Scanner(System.in);
			System.out.println("Introduzca numero de empleado: ");
			int employeeId = scanner.nextInt();

			System.out.println("Introduzca contraseña: ");
			String password = scanner.next();

			logged = employee.login(employeeId, password);
			if (logged) {
				System.out.println("Login correcto ");
			} else {
				System.out.println("Usuario o password incorrectos ");
			}
		} while (!logged);

	}

	public void loadInventory() {
		dao.connect();
		this.readInventory();
		this.showInventory();
	}

	private void readInventory() {
		inventory = dao.getInventory();
	}

	public boolean writeInventory() {
		return dao.writeInventory(inventory);
	}

	private void showCash() {
		System.out.println("Dinero actual: " + cash);
	}

	public void addProduct() {
		if (isInventoryFull()) {
			System.out.println("No se pueden añadir más productos");
			return;
		}

		Scanner scanner = new Scanner(System.in);
		System.out.print("Nombre: ");
		String name = scanner.nextLine();
		System.out.print("Precio mayorista: ");
		double wholesalerPrice = scanner.nextDouble();
		System.out.print("Stock: ");
		int stock = scanner.nextInt();

		addProduct(new Product(name, true, new Amount(wholesalerPrice), stock));
	}

	public void addProduct(Product product) {
	    if (isInventoryFull()) {
	        System.out.println("No se pueden añadir más productos");
	        return;
	    }

	    dao.addProduct(product);

	    if (product.getId() > 0) {
	        inventory.add(product);
	        numberProducts++;
	    } else {
	        System.out.println("ERROR: el producto no se ha guardado en la base de datos");
	    }
	}



	public void removeProduct() {
		if (inventory.size() == 0) {
			System.out.println("No se pueden eliminar productos, inventario vacio");
			return;
		}
		Scanner scanner = new Scanner(System.in);
		System.out.print("Seleccione un nombre de producto: ");
		String name = scanner.next();
		Product product = findProduct(name);

		if (product != null) {

			// Primero eliminar en BBDD
			dao.deleteProduct(product.getId());

			// Después en la memoria local
			inventory.remove(product);

			System.out.println("El producto " + name + " ha sido eliminado");
		} else {
			System.out.println("No se ha encontrado el producto con nombre " + name);
		}
	}

	public void addStock() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Seleccione un nombre de producto: ");
		String name = scanner.next();
		Product product = findProduct(name);

		if (product != null) {
			System.out.print("Seleccione la cantidad a añadir: ");
			int stock = scanner.nextInt();

			product.setStock(product.getStock() + stock);

			System.out.println("Stock actualizado a " + product.getStock());

			// Actualización en BD
			updateProduct(product);

		} else {
			System.out.println("No se ha encontrado el producto con nombre " + name);
		}
	}

	private void setExpired() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Seleccione un nombre de producto: ");
		String name = scanner.next();

		Product product = findProduct(name);

		if (product != null) {
			product.expire();
			System.out.println("Nuevo precio: " + product.getPrice());
			dao.updateProduct(product); // para guardar el nuevo precio

		}
	}

	public void showInventory() {
		System.out.println("Contenido actual de la tienda:");
		for (Product product : inventory) {
			System.out.println(product);
		}
	}

	public void sale() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Realizar venta, escribir nombre cliente");
		String nameClient = sc.nextLine();
		Client client = new Client(nameClient);

		ArrayList<Product> shoppingCart = new ArrayList<Product>();
		Amount totalAmount = new Amount(0.0);

		String name = "";
		while (!name.equals("0")) {
			System.out.println("Introduce el nombre del producto, escribir 0 para terminar:");
			name = sc.nextLine();

			if (name.equals("0")) {
				break;
			}

			Product product = findProduct(name);

			if (product != null && product.isAvailable()) {
				totalAmount.setValue(totalAmount.getValue() + product.getPrice());

				product.setStock(product.getStock() - 1);

				if (product.getStock() == 0) {
					product.setAvailable(false);
				}
				dao.updateProduct(product); // guardar cambios en BD
				shoppingCart.add(product);

				System.out.println("Producto añadido.");
			} else {
				System.out.println("Producto no encontrado o sin stock.");
			}
		}

		totalAmount.setValue(totalAmount.getValue() * TAX_RATE);
		System.out.println("Venta total: " + totalAmount);

		if (!client.pay(totalAmount)) {
			System.out.println("Cliente debe: " + client.getBalance());
		}

		Sale sale = new Sale(client, shoppingCart, totalAmount);
		sales.add(sale);

		cash.setValue(cash.getValue() + totalAmount.getValue());
	}

	private void showSales() {
		System.out.println("Lista de ventas:");
		for (Sale sale : sales) {
			System.out.println(sale);
		}

		Scanner sc = new Scanner(System.in);
		System.out.println("Exportar fichero ventas? S / N");
		String option = sc.nextLine();
		if ("S".equalsIgnoreCase(option)) {
			this.writeSales();
		}
	}

	private void writeSales() {
		LocalDate today = LocalDate.now();
		String fileName = "sales_" + today.toString() + ".txt";

		File f = new File(System.getProperty("user.dir") + File.separator + "files" + File.separator + fileName);

		try (FileWriter fw = new FileWriter(f, true); PrintWriter pw = new PrintWriter(fw)) {

			int counterSale = 1;
			for (Sale sale : sales) {

				pw.println(counterSale + ";Client=" + sale.getClient() + ";Date=" + sale.formatDate() + ";");

				StringBuilder productsLine = new StringBuilder();
				for (Product p : sale.getProducts()) {
					productsLine.append(p.getName()).append(",").append(p.getPrice()).append(";");

				}

				pw.println(counterSale + ";Products=" + productsLine + ";");
				pw.println(counterSale + ";Amount=" + sale.getAmount() + ";");

				counterSale++;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void showSalesAmount() {
		Amount totalAmount = new Amount(0.0);
		for (Sale sale : sales) {
			totalAmount.setValue(totalAmount.getValue() + sale.getAmount().getValue());
		}

		System.out.println("Total de ventas:");
		System.out.println(totalAmount);
	}

	public boolean isInventoryFull() {
		return numberProducts == 10;
	}

	public Product findProduct(String name) {
		for (Product p : inventory) {
			if (p != null && p.getName().equalsIgnoreCase(name)) {
				return p;
			}
		}
		return null;
	}

	public void updateProduct(Product product) {
		dao.updateProduct(product);
	}

	public void deleteProduct(int id) {
		dao.deleteProduct(id);
	}

}
