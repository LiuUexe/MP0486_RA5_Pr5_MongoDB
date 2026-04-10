package test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import model.Employee;

public class InsertEmployee {

	public static void main(String[] args) {

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("./objects/users.odb");
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();

		Employee employee = new Employee(123, "Victor", "test");
		em.persist(employee);

		em.getTransaction().commit();

		em.close();
		emf.close();

		System.out.println("Empleado insertado correctamente");
	}
}