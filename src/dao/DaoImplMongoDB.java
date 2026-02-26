package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.print.Doc;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.hibernate.boot.model.relational.Database;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;

import model.Amount;
import model.Employee;
import model.Product;

public class DaoImplMongoDB implements Dao {
	MongoCollection<Document> employees;
	MongoCollection<Document> inventory;
	MongoCollection<Document> historical;
	

	@Override
	public void connect() {
		String uri = "mongodb://localhost:27017";
		MongoClientURI mongoClientURI = new MongoClientURI(uri);
		MongoClient mongoClient = new MongoClient(mongoClientURI);

		MongoDatabase mongoDatabase = mongoClient.getDatabase("shop");

		employees = mongoDatabase.getCollection("employee");
		inventory = mongoDatabase.getCollection("inventory");
		historical = mongoDatabase.getCollection("historical_inventory");

	}
	
	@Override
	public Employee getEmployee(int employeeId, String password) {

		Employee employee = null;
		Document document = employees.find(eq("employeeId", employeeId)).first();
		
		try {
			if (document != null && password.equals(document.getString("password"))) {
				employee = new Employee(document.getInteger("employeeId"), document.getString("name"),
						document.getString("password"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return employee;
	}

	@Override
	public ArrayList<Product> getInventory() {
		ArrayList<Product> products = new ArrayList<>();
		
		Iterable<Document> docs = inventory.find();
		
		try {
			for (Document document : docs) {
				Document priceDocument = (Document) document.get("wholesalerprice");
				
				double value = priceDocument.getDouble("value");
				String currency = priceDocument.getString("currency");
				products.add(new Product(document.getString("name"), document.getBoolean("available"), new Amount(value), document.getInteger("stock")));
			}
			
		} catch (MongoException e) {
			e.printStackTrace();
		}
		return products;
	}

	@Override
	public boolean writeInventory(ArrayList<Product> list) {
		int counterProduct = 1;
		
		try {
			for (Product product : list) {
				Document priceDoc = new Document(
						"value", product.getPrice())
						.append("currency", "€");
				
				Date now = new Date();
				
				Document doc = new Document("_id", new ObjectId())
						.append("id", counterProduct)
						.append("name", product.getName())
						.append("wholesalerprice", priceDoc)
						.append("available", product.isAvailable())
						.append("stock", product.getStock())
						.append("created_at", now);
				
				historical.insertOne(doc);
				counterProduct++;
			}
			
			return true;
			
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void addProduct(Product product) {
		Document findId = inventory.find().sort(Sorts.descending("id")).first();
		
		Document price = new Document("value", product.getWholesalerPrice().getValue()).append("currency", "€");
		
		Document doc = new Document("_id", new ObjectId())
				.append("name", product.getName())
				.append("available", true)
				.append("wholesalerprice", price)
				.append("stock", product.getStock())
				.append("id", findId.getInteger("id") + 1);
		
		inventory.insertOne(doc);

	}

	@Override
	public void updateProduct(Product product) {
		inventory.updateOne(eq("name", product.getName()), set("stock", product.getStock()));

	}

	@Override
	public void deleteProduct(int product) {
		inventory.deleteOne(eq("id", product));
		
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub

	}

	

}