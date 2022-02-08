package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.ReceiptDao;
import model.entities.Receipt;

public class ReceiptService {

	private ReceiptDao dao = DaoFactory.createReceiptDao();
	
	public List<Receipt> findAll() {
		return dao.findAll();
	}
	
	public void saveOrUpdate(Receipt obj) {
		dao.insertOrUpdate(obj);
	}
	
	public void remove(Receipt obj) {
		dao.deleteByCod(obj.getCodDocument());
	}
}
