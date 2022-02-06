package application;

import java.util.List;

import model.dao.CustomerDao;
import model.dao.DaoFactory;
import model.entities.Customer;

public class Program {
	
	

	public static void main(String[] args) {
		
		CustomerDao customerDao = DaoFactory.createCustomerDao();
		
		
		Customer customer = new Customer(null, 9999,"Lixo LTDA", 999.99 , "void@gmail.com", null);
		customerDao.insert(customer);
		
		System.out.println(customer);
		
		
		
		
	}

}
