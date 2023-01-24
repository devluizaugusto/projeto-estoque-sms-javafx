package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegretyException;
import gui.listners.DataChangeListners;
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
import model.entities.Product;
import model.services.ProductService;
import model.services.SetoresService;

public class ProdutoListController implements Initializable, DataChangeListners {

	private ProductService service;
	
	@FXML
	private TableView<Product> tableViewProduto;

	@FXML
	private TableColumn<Product, Integer> tableColumnId;

	@FXML
	private TableColumn<Product, String> tableColumnNome;
	
	@FXML
	private TableColumn<Product, Date> tableColumnDataEntrada;
	
	@FXML
	private TableColumn<Product, Integer> tableColumnQtdEntrada;
	
	@FXML
	private TableColumn<Product, Integer> tableColumnQtdSaida;
	
	@FXML
	private TableColumn<Product, Integer> tableColumnQtdTotal;

	@FXML
	private TableColumn<Product, Date> tableColumnDataSaida;
	
	@FXML
	private TableColumn<Product, String> tableColumnNomeSetor;
	
	@FXML
	private TableColumn<Product, Product> tableColumnEditar;

	@FXML
	private TableColumn<Product, Product> tableColumnRemover;
	
	@FXML
	private Button btNovo;

	private ObservableList<Product> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Product obj = new Product();
		createDialogForm(obj, "/gui/ProdutoForm.fxml", parentStage);
	}

	public void setProductService(ProductService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnDataEntrada.setCellValueFactory(new PropertyValueFactory<>("dataEntrada"));
		Utils.formatTableColumnDate(tableColumnDataEntrada, "dd/MM/yyyy");
		tableColumnQtdEntrada.setCellValueFactory(new PropertyValueFactory<>("qtdEntrada"));
		tableColumnQtdSaida.setCellValueFactory(new PropertyValueFactory<>("qtdSaida"));
		tableColumnQtdTotal.setCellValueFactory(new PropertyValueFactory<>("qtdTotal"));
		tableColumnDataSaida.setCellValueFactory(new PropertyValueFactory<>("dataSaida"));
		Utils.formatTableColumnDate(tableColumnDataSaida, "dd/MM/yyyy");
		tableColumnNomeSetor.setCellValueFactory(new PropertyValueFactory<>("setor"));
	
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewProduto.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Product> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewProduto.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}
	
	private void createDialogForm(Product obj, String absoluteName, Stage parentStage) {
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			ProdutoFormController controller = loader.getController();
			controller.setProduct(obj);
			controller.setServices(new ProductService(), new SetoresService());
			controller.loadAssociateObjects();
			controller.subscribeDataChangeListner(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("ENTER PRODUCT DATA: ");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlerts("IO Exception", "Error Load View", e.getMessage(), AlertType.ERROR);
		}
		
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	private void initEditButtons() {
		tableColumnEditar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEditar.setCellFactory(param -> new TableCell<Product, Product>() {
			private final Button button = new Button("EDITAR");

			@Override
			protected void updateItem(Product obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/ProdutoForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnRemover.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemover.setCellFactory(param -> new TableCell<Product, Product>() {
			private final Button button = new Button("REMOVER");

			@Override
			protected void updateItem(Product obj, boolean empty) {
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

	private void removeEntity(Product obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("CONFIRMATION", "ARE YOU SURE TO DELETE?");

		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("SERVICE WAS NULL");
			}
			try {
				service.remove(obj);
				updateTableView();
			} 
			catch (DbIntegretyException e) {
				Alerts.showAlerts("ERROR REMOVING OBJECT", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
}
