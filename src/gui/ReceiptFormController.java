package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Customer;
import model.entities.Customer;
import model.entities.Receipt;
import model.exceptions.ValidationException;
import model.services.CustomerService;
import model.services.ReceiptService;

public class ReceiptFormController implements Initializable {

	private Receipt entity;

	private ReceiptService service;

	private CustomerService departmentService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtCod;

	@FXML
	private TextField txtDocumentType;

	@FXML
	private DatePicker dpIssueDate;

	@FXML
	private DatePicker dpDueDate;

	@FXML
	private TextField txtValue;

	@FXML
	private TextField txtPaymentStatus;

	@FXML
	private DatePicker dpPayDate;

	@FXML
	private TextField txtBank;

	@FXML
	private ComboBox<Customer> comboBoxCustomer;

	@FXML
	private Label labelErrorDocumentType;

	@FXML
	private Label labelErrorIssueDate;

	@FXML
	private Label labelErrorDueDate;

	@FXML
	private Label labelErrorValue;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	private ObservableList<Customer> obsList;

	public void setReceipt(Receipt entity) {
		this.entity = entity;
	}

	public void setServices(ReceiptService service, CustomerService departmentService) {
		this.service = service;
		this.departmentService = departmentService;
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
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		} catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	private Receipt getFormData() {
		Receipt obj = new Receipt();

		ValidationException exception = new ValidationException("Validation error");

		obj.setCodDocument(Utils.tryParseToInt(txtCod.getText()));

		if (txtDocumentType.getText() == null || txtDocumentType.getText().trim().equals("")) {
			exception.addError("documentType", "Field can't be empty");
		}
		obj.setDocumentType(txtDocumentType.getText());

		if (dpIssueDate.getValue() == null) {
			exception.addError("issueDate", "Field can't be empty");
		} else {
			Instant instant = Instant.from(dpIssueDate.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setIssueDate(Date.from(instant));
		}

		if (dpDueDate.getValue() == null) {
			exception.addError("dueDate", "Field can't be empty");
		} else {
			Instant instant = Instant.from(dpDueDate.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDueDate(Date.from(instant));
		}

		if (txtValue.getText() == null || txtValue.getText().trim().equals("")) {
			exception.addError("value", "Field can't be empty");
		}
		obj.setValue(Utils.tryParseToDouble(txtValue.getText()));
		
		obj.setPaymentStatus(txtPaymentStatus.getText());
		
		//Após comentar essas linhas funcionou
		//Instant instant = Instant.from(dpPayDate.getValue().atStartOfDay(ZoneId.systemDefault()));
		//obj.setPayDate(Date.from(instant));
		
		obj.setBank(txtBank.getText());

		obj.setCustomer(comboBoxCustomer.getValue());

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
		Constraints.setTextFieldMaxLength(txtDocumentType, 11);
		Utils.formatDatePicker(dpIssueDate, "dd/MM/yyyy");
		Utils.formatDatePicker(dpDueDate, "dd/MM/yyyy");
		Constraints.setTextFieldDouble(txtValue);
		Constraints.setTextFieldMaxLength(txtPaymentStatus, 11);
		Utils.formatDatePicker(dpPayDate, "dd/MM/yyyy");
		Constraints.setTextFieldMaxLength(txtBank, 11);
		initializeComboBoxCustomer();
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		Locale.setDefault(Locale.US);
		
		txtCod.setText(String.valueOf(entity.getCodDocument()));
		txtDocumentType.setText(entity.getDocumentType());
		if (entity.getIssueDate() != null) {
			dpIssueDate.setValue(LocalDate.ofInstant(entity.getIssueDate().toInstant(), ZoneId.systemDefault()));
		}
		
		if (entity.getDueDate() != null) {
			dpDueDate.setValue(LocalDate.ofInstant(entity.getDueDate().toInstant(), ZoneId.systemDefault()));
		}
		
		txtValue.setText(String.format("%.2f", entity.getValue()));
		
		txtPaymentStatus.setText(entity.getPaymentStatus());
		
		if (entity.getPayDate() != null) {
			dpPayDate.setValue(LocalDate.ofInstant(entity.getPayDate().toInstant(), ZoneId.systemDefault()));
		}
		
		txtBank.setText(entity.getBank());
		
		if (entity.getCustomer() == null) {
			comboBoxCustomer.getSelectionModel().selectFirst();
		} else {
			comboBoxCustomer.setValue(entity.getCustomer());
		}
		
		
	}

	public void loadAssociatedObjects() {
		if (departmentService == null) {
			throw new IllegalStateException("CustomerService was null");
		}
		List<Customer> list = departmentService.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxCustomer.setItems(obsList);
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		labelErrorDocumentType.setText((fields.contains("documentType") ? errors.get("documentType") : ""));
		labelErrorIssueDate.setText((fields.contains("issueDate") ? errors.get("issuDate") : ""));
		labelErrorDueDate.setText((fields.contains("dueDate") ? errors.get("dueDate") : ""));
		labelErrorValue.setText((fields.contains("value") ? errors.get("value") : ""));
	}

	private void initializeComboBoxCustomer() {
		Callback<ListView<Customer>, ListCell<Customer>> factory = lv -> new ListCell<Customer>() {
			@Override
			protected void updateItem(Customer item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxCustomer.setCellFactory(factory);
		comboBoxCustomer.setButtonCell(factory.call(null));
	}
}
