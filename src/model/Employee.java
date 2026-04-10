package model;

import javax.persistence.Entity;
import javax.persistence.Transient;

import main.Logable;
import dao.Dao;
import dao.DaoImplObjectDB;

@Entity
public class Employee extends Person implements Logable {
	private int employeeId;
	private String password;

	@Transient
	private Dao dao = new DaoImplObjectDB();

	public Employee(String name) {
		super(name);
	}

	public Employee(int employeeId, String name, String password) {
		super(name);
		this.employeeId = employeeId;
		this.password = password;
	}

	public Employee() {
		super();
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public boolean login(int user, String password) {
		boolean success = false;

		dao.connect();

		if (dao.getEmployee(user, password) != null) {
			success = true;
		}

		dao.disconnect();
		return success;
	}
}