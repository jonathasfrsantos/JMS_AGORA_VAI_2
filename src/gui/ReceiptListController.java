package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Customer;
import model.entities.Receipt;
import model.services.CustomerService;
import model.services.ReceiptService;

public class ReceiptListController implements Initializable, DataChangeListener {

	private ReceiptService service;

	@FXML
	private TableView<Receipt> tableViewReceipt;

	@FXML
	private TableColumn<Receipt, Integer> tableColumnCod;

	@FXML
	private TableColumn<Receipt, String> tableColumnDocumentType;

	@FXML
	private TableColumn<Receipt, Date> tableColumnIssueDate;
	
	@FXML
	private TableColumn<Receipt, Date> tableColumnDueDate;
	
	@FXML
	private TableColumn<Receipt, Double> tableColumnValue;
	
	@FXML
	private TableColumn<Receipt, String> tableColumnPaymentStatus;
	
	@FXML
	private TableColumn<Receipt, Date> tableColumnPayDate;
	
	@FXML
	private TableColumn<Receipt, String> tableColumnBank;
	
	@FXML
	private TableColumn<Customer, Customer> tableColumnCustomer;
	
	@FXML
	private TableColumn<Receipt, Receipt> tableColumnEDIT;

	@FXML
	private TableColumn<Receipt, Receipt> tableColumnREMOVE;
	


	@FXML
	private Button btNew;

	private ObservableList<Receipt> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Receipt obj = new Receipt();
		createDialogForm(obj, "/gui/ReceiptForm.fxml", parentStage);
	}

	public void setReceiptService(ReceiptService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnCod.setCellValueFactory(new PropertyValueFactory<>("codDocument"));
		tableColumnDocumentType.setCellValueFactory(new PropertyValueFactory<>("documentType"));
		tableColumnIssueDate.setCellValueFactory(new PropertyValueFactory<>("issueDate"));
		Utils.formatTableColumnDate(tableColumnIssueDate, "dd/MM/yyyy");
		tableColumnDueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
		Utils.formatTableColumnDate(tableColumnDueDate, "dd/MM/yyyy");
		tableColumnValue.setCellValueFactory(new PropertyValueFactory<>("value"));
		Utils.formatTableColumnDouble(tableColumnValue, 2);
		tableColumnPaymentStatus.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
		tableColumnPayDate.setCellValueFactory(new PropertyValueFactory<>("payDate"));
		//Utils.formatTableColumnDate(tableColumnPayDate, "dd/MM/yyyy"); // apos comentar essa linha funcionou, porque o campo de data não pode ser um null
		tableColumnBank.setCellValueFactory(new PropertyValueFactory<>("bank"));
		
		
		
		
	

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewReceipt.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Receipt> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewReceipt.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Receipt obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			ReceiptFormController controller = loader.getController();
			controller.setReceipt(obj);
			controller.setServices(new ReceiptService(), new CustomerService());
			controller.loadAssociatedObjects();
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Receipt data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Receipt, Receipt>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Receipt obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/ReceiptForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Receipt, Receipt>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Receipt obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Receipt obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");

		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				service.remove(obj);
				updateTableView();
			}
			catch (DbIntegrityException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
}
