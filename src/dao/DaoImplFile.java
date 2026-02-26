package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;

import model.Amount;
import model.Employee;
import model.Product;

public class DaoImplFile implements Dao {

	ArrayList<Product> inventory = new ArrayList<Product>();

	@Override
	public void connect() {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<Product> getInventory() {

		ArrayList<Product> list = new ArrayList<Product>();

		File f = new File(System.getProperty("user.dir") + File.separator + "files/inputInventory.txt");

		try {
			// wrap in proper classes
			FileReader fr;
			fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);

			// read first line
			String line = br.readLine();

			// process and read next line until end of file
			while (line != null) {
				// split in sections
				String[] sections = line.split(";");

				String name = "";
				double wholesalerPrice = 0.0;
				int stock = 0;

				// read each sections
				for (int i = 0; i < sections.length; i++) {
					// split data in key(0) and value(1)
					String[] data = sections[i].split(":");

					switch (i) {
					case 0:
						// format product name
						name = data[1];
						break;

					case 1:
						// format price
						wholesalerPrice = Double.parseDouble(data[1]);
						break;

					case 2:
						// format stock
						stock = Integer.parseInt(data[1]);
						break;

					default:
						break;
					}
				}
				// add product to inventory
				list.add(new Product(name, true, new Amount(wholesalerPrice),  stock));

				line = br.readLine();

			}
			fr.close();
			br.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}

	@Override
	public void addProduct(Product product) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean writeInventory(ArrayList<Product> inventory) {
		LocalDate today = LocalDate.now();
		String fileName = "inventory_" + today.toString() + ".txt";

		String userDesktop = System.getProperty("user.home") + File.separator + "Desktop";
		File f = new File(userDesktop + File.separator + fileName);

		try (FileWriter fw = new FileWriter(f, false); PrintWriter pw = new PrintWriter(fw)) {

			int counter = 1;
			for (Product product : inventory) {
				pw.println(counter + ";Product:" + product.getName() + ";Stock:" + product.getStock() + ";Price:"
						+ product.getPrice() + ";");
				counter++;
			}

			pw.println("Total number of products:" + inventory.size() + ";");
			System.out.println("Inventario exportado correctamente a: " + f.getAbsolutePath());
			return true;

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub

	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateProduct(Product product) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteProduct(int id) {
		// TODO Auto-generated method stub

	}

}
