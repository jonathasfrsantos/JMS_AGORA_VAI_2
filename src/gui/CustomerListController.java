package gui;

import java.io.IOException;
import java.net.URL;
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
import model.services.CustomerService;

public class CustomerListController implements Initializable, DataChangeListener {

	private CustomerService service;

	@FXML
	private TableView<Customer> tableViewCustomer;

	@FXML
	private TableColumn<Customer, Integer> tableColumnCod;

	@FXML
	private TableColumn<Customer, String> tableColumnName;
	
	@FXML
	private TableColumn<Customer, Double> tableColumnFeesValue;
	
	@FXML
	private TableColumn<Customer, String> tableColumnEmail;
	
	@FXML
	private TableColumn<Customer, String> tableColumnEmail2;
	
	@FXML
	private TableColumn<Customer, Customer> tableColumnEDIT;

	@FXML
	private TableColumn<Customer, Customer> tableColumnREMOVE;

	@FXML
	private Button btNew;

	private ObservableList<Customer> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Customer obj = new Customer();
		createDialogForm(obj, "/gui/CustomerForm.fxml", parentStage);
	}

	public void setCustomerService(CustomerService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnCod.setCellValueFactory(new PropertyValueFactory<>("codCustomer"));   // os parâmetros entre aspas precisam ser iguais aos nomes dos atributos na entidade
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnFeesValue.setCellValueFactory(new PropertyValueFactory<>("feesValue"));
		Utils.formatTableColumnDouble(tableColumnFeesValue, 2);
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnEmail2.setCellValueFactory(new PropertyValueFactory<>("email2"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewCustomer.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Customer> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewCustomer.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Customer obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			CustomerFormController controller = loader.getController();
			controller.setCustomer(obj);
			controller.setCustomerService(new CustomerService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Customer data");
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
		tableColumnEDIT.setCellFactory(param -> new TableCell<Customer, Customer>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Customer obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/CustomerForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Customer, Customer>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Customer obj, boolean empty) {
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

	private void removeEntity(Customer obj) {
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
