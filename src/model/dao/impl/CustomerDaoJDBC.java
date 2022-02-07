package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.CustomerDao;
import model.entities.Customer;

public class CustomerDaoJDBC implements CustomerDao {

	private Connection conn;

	public CustomerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Customer obj) {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement(
				
			// O id não é passado no INSERT, pois é um auto increment
					
					"INSERT INTO tb_customer (cod_customer, name, feesValue, email, email2) "
							+ "VALUES (?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setInt(1, obj.getCodCustomer());
			st.setString(2, obj.getName());
			st.setDouble(3, obj.getFeesValue());
			st.setString(4, obj.getEmail());
			st.setString(5, obj.getEmail2());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
			} else {
				throw new DbException("Unexpected error! No rows affected!");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public List<Customer> findAll() {

		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM tb_customer");

			rs = st.executeQuery();

			List<Customer> list = new ArrayList<>();

			while (rs.next()) {
				Customer obj = new Customer();
				obj.setId(rs.getInt("id_customer"));
				obj.setCodCustomer(rs.getInt("cod_customer"));
				obj.setName(rs.getString("name"));
				obj.setFeesValue(rs.getDouble("feesValue"));
				obj.setEmail(rs.getString("email"));
				obj.setEmail2(rs.getString("email2"));
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	public Customer findByCod(Integer cod) {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("SELECT * FROM tb_customer WHERE cod_customer = ?");
			st.setInt(1, cod);

			rs = st.executeQuery();
			if (rs.next()) {
				Customer obj = new Customer();
				obj.setId(rs.getInt("id_customer"));
				obj.setCodCustomer(rs.getInt("cod_customer"));
				obj.setName(rs.getString("name"));
				obj.setFeesValue(rs.getDouble("feesValue"));
				return obj;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Customer obj) {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("UPDATE tb_customer SET " 
										+ "( name = ?, feesValue = ?, email = ?, email2 = ?) " 
										+ " WHERE cod_customer = ?");

			st.setString(1, obj.getName());
			st.setDouble(2, obj.getFeesValue());
			st.setString(3, obj.getEmail());
			st.setString(4, obj.getEmail2());
			st.setInt(5, obj.getCodCustomer());

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void deleteByCod(Integer cod) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM tb_customer WHERE cod_customer = ?");

			st.setInt(1, cod);
			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());

		} finally {
			DB.closeStatement(st);
		}

	}
	
	@Override
	public void InsertOrUpdate(Customer obj) {
		PreparedStatement st = null;
		
		try {
			st  = conn.prepareStatement(
				"INSERT INTO tb_customer (cod_customer, name, feesValue, email, email2) "
				+ "VALUES(?, ?, ?, ?, ?) "
				+ "ON DUPLICATE KEY UPDATE name = values(name), feesValue = values(feesValue), email = values(email), email2 = values(email2) ");
			
				st.setInt(1, obj.getCodCustomer());
				st.setString(2, obj.getName());
				st.setDouble(3, obj.getFeesValue());
				st.setString(4, obj.getEmail());
				st.setString(5, obj.getEmail2());
				
				st.executeUpdate();
					
		}catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}
	
	
	

}
