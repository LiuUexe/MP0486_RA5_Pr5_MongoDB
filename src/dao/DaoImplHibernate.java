package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import model.Employee;
import model.Product;

public class DaoImplHibernate implements Dao {

	private SessionFactory sessionFactory;

	@Override
	public void connect() {
		// Lee src/main/resources/hibernate.cfg.xml
		sessionFactory = new Configuration().configure().buildSessionFactory();
	}

	@Override
	public void disconnect() {
		if (sessionFactory != null) {
			sessionFactory.close();
		}
	}

	@Override
	public ArrayList<Product> getInventory() {
		Session session = sessionFactory.openSession();
		List<Product> list = session.createQuery("FROM Product", Product.class).list();
		session.close();
		return new ArrayList<>(list);
	}

	@Override
	public void addProduct(Product product) {
	    Session session = sessionFactory.openSession();
	    Transaction tx = session.beginTransaction();

	    try {
	        session.save(product);
	        session.flush(); // FUERZA EL INSERT EN BBDD
	        System.out.println("ID generado: " + product.getId());
	        tx.commit();
	    } catch (Exception e) {
	        tx.rollback();
	        e.printStackTrace();
	    } finally {
	        session.close();
	    }
	}


	@Override
	public void updateProduct(Product product) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();

		session.update(product);

		tx.commit();
		session.close();
	}

	@Override
	public void deleteProduct(int productId) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();

		Product p = session.get(Product.class, productId);
		if (p != null) {
			session.delete(p);
		}

		tx.commit();
		session.close();
	}

	@Override
	public boolean writeInventory(ArrayList<Product> inventory) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();

		try {
			for (Product p : inventory) {
				session.saveOrUpdate(p);
			}
			tx.commit();
			return true;
		} catch (Exception e) {
			tx.rollback();
			return false;
		} finally {
			session.close();
		}
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		// Enunciado: "Resto de métodos interface sin implementación"
		throw new UnsupportedOperationException("Not implemented");
	}
}
