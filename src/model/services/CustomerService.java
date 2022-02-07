package model.services;

import java.util.List;

import model.dao.CustomerDao;
import model.dao.DaoFactory;
import model.entities.Customer;


public class CustomerService {

	private CustomerDao dao = DaoFactory.createCustomerDao();
	
	public List<Customer> findAll() {
		return dao.findAll();
	}
	
	public void insertOrUpdate(Customer obj) {
		dao.InsertOrUpdate(obj);
	}
	
	public void remove(Customer obj) {
		dao.deleteByCod(obj.getCodCustomer());
	}
}
