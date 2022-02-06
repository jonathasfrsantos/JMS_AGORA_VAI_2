package model.dao;

import java.util.List;

import model.entities.Customer;
import model.entities.Receipt;

public interface ReceiptDao {

	
	  void insert(Receipt obj); 
	  void update(Receipt obj);
	  void deleteByCod(Integer cod); 
	  Receipt findByCod(Integer cod);
	  List<Receipt> findAll();
	  List<Receipt> findByCustomer(Customer customer);
	
	

	 }