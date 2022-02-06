package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Customer;
import model.exceptions.ValidationException;
import model.services.CustomerService;

public class CustomerFormController implements Initializable {

	private Customer entity;
	
	private CustomerService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtCod;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private TextField txtFeesValue;
	
	@FXML
	private TextField txtEmail;
	
	@FXML
	private TextField txtEmail2;
	
	@FXML 
	private Label labelErrorCod;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Label labelErrorFeesValue;
	
	
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	public void setCustomer(Customer entity) {
		this.entity = entity;
	}
	
	public void setCustomerService(CustomerService service) {
		this.service = service;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		}
		catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		}
		catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	private Customer getFormData() {
		Customer obj = new Customer();
		
		ValidationException exception = new ValidationException("Validation error");
		
		obj.setCodCustomer(Utils.tryParseToInt(txtCod.getText()));
		
		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "Field can't be empty");
		}
		obj.setName(txtName.getText());
		
		if (txtFeesValue.getText() == null || txtFeesValue.getText().trim().equals("")){
			exception.addError("feesValue", "Field can't be empty");
		}
		obj.setFeesValue(Utils.tryParseToDouble(txtFeesValue.getText()));
		
		obj.setEmail(txtEmail.getText());
		
		obj.setEmail2(txtEmail2.getText());
				
		
		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		
		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtCod);
		Constraints.setTextFieldMaxLength(txtName, 200);
		Constraints.setTextFieldDouble(txtFeesValue);
		Constraints.setTextFieldMaxLength(txtEmail, 100);
		Constraints.setTextFieldMaxLength(txtEmail2, 100);
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtCod.setText(String.valueOf(entity.getCodCustomer()));
		txtName.setText(entity.getName());
		//Locale.setDefault(Locale.US);
		txtFeesValue.setText(String.format("%.2f", entity.getFeesValue()));
		txtEmail.setText(entity.getEmail());
		txtEmail2.setText(entity.getEmail2());
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		labelErrorCod.setText((fields.contains("codCustomer") ? errors.get("codCustomer") : ""));
		labelErrorName.setText((fields.contains("name") ? errors.get("name") : ""));
		labelErrorFeesValue.setText((fields.contains("feesValue") ? errors.get("feesValue") : ""));
	}
}
