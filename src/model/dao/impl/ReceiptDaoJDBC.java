package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.ReceiptDao;
import model.entities.Customer;
import model.entities.Receipt;

public class ReceiptDaoJDBC implements ReceiptDao {

	private Connection conn;

	public ReceiptDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Receipt obj) {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("INSERT INTO tb_document "
					+ "(cod_document, documentType, issueDate, dueDate, value, cod_customer) " + "VALUES "
					+ "(?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			st.setInt(1, obj.getCodDocument());
			st.setString(2, obj.getDocumentType());
			st.setDate(3, new java.sql.Date(obj.getIssueDate().getTime()));
			st.setDate(4, new java.sql.Date(obj.getDueDate().getTime()));
			st.setDouble(5, obj.getValue());
			st.setInt(6, obj.getCustomer().getCodCustomer());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int cod = rs.getInt(1);
					obj.setId(cod);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Unexpected Error! no rows affected");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void update(Receipt obj) {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement(
					"UPDATE tb_document " + "SET paymentStatus = ?, bank = ?, payDate = ? " + "WHERE cod_document = ?");

			st.setString(1, obj.getPaymentStatus());
			st.setString(2, obj.getBank());
			st.setDate(3, new java.sql.Date(obj.getPayDate().getTime()));
			st.setInt(4, obj.getCodDocument());

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
			st = conn.prepareStatement("DELETE FROM tb_document WHERE cod_document = ?");

			st.setInt(1, cod);

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public Receipt findByCod(Integer cod) { // ok
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"SELECT tb_document.*, B.name " 
					+ "FROM tb_document " 
					+ "INNER JOIN tb_customer B "
					+ "ON tb_document.cod_customer = B.cod_customer " 
					+ "WHERE tb_document.cod_document = ? ");

			st.setInt(1, cod);
			rs = st.executeQuery();
			if (rs.next()) {
				Customer customer = instantiateCustomer(rs);
				Receipt receipt = instantiateReceipt(rs, customer);
				return receipt;

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
	public List<Receipt> findAll() { // ok
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("SELECT tb_document.*, B.name " 
										+ "FROM tb_document "
										+ "INNER JOIN tb_customer B " 
										+ "ON tb_document.cod_customer = B.cod_customer " 
										+ "ORDER BY name");
			rs = st.executeQuery();
			List<Receipt> list = new ArrayList<>();
			Map<Integer, Customer> map = new HashMap<>();

			while (rs.next()) {
				Customer customer = map.get(rs.getInt("cod_customer"));

				if (customer == null) {
					customer = instantiateCustomer(rs);
					map.put(rs.getInt("cod_customer"), customer);
				}

				Receipt obj = instantiateReceipt(rs, customer);
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

	public List<Receipt> findByCustomer(Customer customer) {

		// TODO Auto-generated method stub
		return null;
	}

	private Receipt instantiateReceipt(ResultSet rs, Customer customer) throws SQLException {
		Receipt obj = new Receipt();
		obj.setId(rs.getInt("id_document"));
		obj.setCodDocument(rs.getInt("cod_document"));
		obj.setDocumentType(rs.getNString("documentType"));
		obj.setIssueDate(rs.getDate("issueDate"));
		obj.setDueDate(rs.getDate("dueDate"));
		obj.setValue(rs.getDouble("value"));
		obj.setPaymentStatus(rs.getString("paymentStatus"));
		obj.setBank(rs.getString("bank"));
		obj.setPayDate(rs.getDate("payDate"));
		obj.setCustomer(customer);

		return obj;

	}

	private Customer instantiateCustomer(ResultSet rs) throws SQLException {
		Customer customer = new Customer();
		customer.setId(rs.getInt("cod_customer"));
		customer.setName(rs.getString("name"));
		customer.setFeesValue(rs.getDouble("valor"));
		customer.setEmail(rs.getString("email"));
		customer.setEmail2(rs.getString("email2"));// apos  comentar essa linha o
		// programa funcionou
		return customer;

	}


}