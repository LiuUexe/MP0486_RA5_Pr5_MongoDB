package model;

import javax.persistence.*;

@Entity
@Table(name = "inventory")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "name")
	private String name;

	@Column(name = "wholesalerprice")
	private Amount wholesalerPrice;

	@Column(name = "available")
	private boolean available;

	@Column(name = "stock")
	private int stock;

	@Column(name = "price")
	private double price;

	@Transient
	private static int totalProducts;

	public final static double EXPIRATION_RATE = 0.60;

	public Product() {
	}

	public Product(String name, boolean available, Amount wholesalerPrice, int stock) {
		super();
		this.id = totalProducts + 1;
		this.name = name;
		this.available = available;
		this.wholesalerPrice = wholesalerPrice;
		this.stock = stock;
		
		totalProducts++;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public Amount getWholesalerPrice() {
		return wholesalerPrice;
	}

	public void setWholesalerPrice(Amount wholesalerPrice) {
		this.wholesalerPrice = wholesalerPrice;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public static int getTotalProducts() {
		return totalProducts;
	}

	public static void setTotalProducts(int totalProducts) {
		Product.totalProducts = totalProducts;
	}

	public void expire() {
		this.price = this.price * EXPIRATION_RATE;
	}

	@Override
	public String toString() {
		return "Product [name=" + name + ", price=" + price + ", available=" + available + ", stock=" + stock + "]";
	}
}
