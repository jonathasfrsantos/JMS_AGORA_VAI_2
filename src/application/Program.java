package application;

import model.dao.CustomerDao;
import model.dao.DaoFactory;
import model.entities.Customer;

public class Program {
	
	

	public static void main(String[] args) {
		
		CustomerDao customerDao = DaoFactory.createCustomerDao();
		
		
		Customer customer = new Customer(null, 7777, "Lixo S/A", 5000.0, "void@gmail.com", "void2@gmail.com");
		
		//Customer customer = new Customer(null, 7777,"Lixo LTDA", 999.99 , "void@gmail.com", null);
		customerDao.InsertOrUpdate(customer);
		
		System.out.println(customer);
		
		
		
		
	}

}
