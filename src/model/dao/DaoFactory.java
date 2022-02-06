package model.dao;

import db.DB;
import model.dao.impl.CustomerDaoJDBC;
import model.dao.impl.ReceiptDaoJDBC;

public class DaoFactory {
	
	
	public static CustomerDao createCustomerDao() {
		return new CustomerDaoJDBC(DB.getConnection());
	}
	
	
	public static ReceiptDao createReceiptDao() {
		return new ReceiptDaoJDBC(DB.getConnection());
	}
	
	
	
	
}