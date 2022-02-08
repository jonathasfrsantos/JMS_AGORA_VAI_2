package model.entities;

import java.io.Serializable;
import java.util.Objects;

public class Customer implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer codCustomer;
	private String name;
	private Double feesValue;
	private String email;
	private String email2;


	public Customer() {
	}

	public Customer(Integer id, Integer codCustomer, String name, Double feesValue, String email, String email2) {

		this.id = id;
		this.codCustomer = codCustomer;
		this.name = name;
		this.feesValue = feesValue;
		this.email = email;
		this.email2 = email2;
	}

	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCodCustomer() {
		return codCustomer;
	}

	public void setCodCustomer(Integer codCustomer) {
		this.codCustomer = codCustomer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getFeesValue() {
		return feesValue;
	}

	public void setFeesValue(Double feesValue) {
		this.feesValue = feesValue;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail2() {
		return email2;
	}

	public void setEmail2(String email2) {
		this.email2 = email2;
	}

	@Override
	public String toString() {
		return name;
	}

	
	

	
	
	
	 
	


	}