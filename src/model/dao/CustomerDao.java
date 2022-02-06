package model.dao;

import java.util.List;

import model.entities.Customer;

public interface CustomerDao {
	
	void insert(Customer obj);
	void update(Customer obj);  
	void deleteByCod(Integer cod); 
	Customer findByCod(Integer cod); 
	List<Customer> findAll();
	
	

	
	
}