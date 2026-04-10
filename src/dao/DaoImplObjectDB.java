package dao;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import model.Employee;
import model.Product;

public class DaoImplObjectDB implements Dao {

	private EntityManagerFactory emf;
	private EntityManager em;

	@Override
	public void connect() {
		emf = Persistence.createEntityManagerFactory("./objects/users.odb");
		em = emf.createEntityManager();
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		Employee employee = null;

		try {
			employee = em.createQuery(
					"SELECT e FROM Employee e WHERE e.employeeId = :employeeId AND e.password = :password",
					Employee.class)
					.setParameter("employeeId", employeeId)
					.setParameter("password", password)
					.getSingleResult();
		} catch (Exception e) {
			employee = null;
		}

		return employee;
	}

	@Override
	public ArrayList<Product> getInventory() {
		return new ArrayList<Product>();
	}

	@Override
	public boolean writeInventory(ArrayList<Product> inventory) {
		return false;
	}

	@Override
	public void addProduct(Product product) {
	}

	@Override
	public void updateProduct(Product product) {
	}

	@Override
	public void deleteProduct(int productId) {
	}

	@Override
	public void disconnect() {
		if (em != null && em.isOpen()) {
			em.close();
		}
		if (emf != null && emf.isOpen()) {
			emf.close();
		}
	}
}