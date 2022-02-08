package application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import model.dao.CustomerDao;
import model.dao.DaoFactory;
import model.dao.ReceiptDao;
import model.entities.Customer;
import model.entities.Receipt;

public class Program {
	
	

	public static void main(String[] args) throws ParseException {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
		
		CustomerDao customerDao = DaoFactory.createCustomerDao();
		ReceiptDao receiptDao = DaoFactory.createReceiptDao();	
		Customer customer = customerDao.findByCod(9999);
		
		Receipt receipt = new Receipt(null, 7777, "teste", sdf.parse("01/01/2022"), sdf.parse("31/01/2022"), 900.0, customer);
		receiptDao.insertOrUpdate(receipt);
	
		System.out.println(receipt);
		
		
		
		
	}

}
