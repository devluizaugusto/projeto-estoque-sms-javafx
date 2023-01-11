package gui;

import java.io.IOException;
import java.net.URL;
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

public class ProductListController implements Initializable, DataChangeListners {

	private ProductService service;

	@FXML
	private TableView<Product> tableViewProduto;

	@FXML
	private TableColumn<Product, Integer> tableColumnId;

	@FXML
	private TableColumn<Product, String> tableColumnNome;

	@FXML
	private TableColumn<Product, Integer> tableColumnQtdEntrada;

	@FXML
	private TableColumn<Product, Integer> tableColumnQtdSaida;

	@FXML
	private TableColumn<Product, Integer> tableColumnQtdTotal;

	@FXML
	private TableColumn<Product, Product> tableColumnEditar;

	@FXML
	private TableColumn<Product, Product> tableColumnRemover;

	@FXML
	private Button btNovo;

	private ObservableList<Product> obsList;

	@FXML
	public void onBtNovoAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Product obj = new Product();
		createDialogForm(obj, "/gui/ProductForm.fxml", parentStage);
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
		tableColumnQtdEntrada.setCellValueFactory(new PropertyValueFactory<>("qtdEntrada"));
		tableColumnQtdSaida.setCellValueFactory(new PropertyValueFactory<>("qtdSaida"));
		tableColumnQtdTotal.setCellValueFactory(new PropertyValueFactory<>("qtdTotal"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewProduto.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Serviço estava nulo!");
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

			ProductFormController controller = loader.getController();
			controller.setProduct(obj);
			controller.setProductService(new ProductService());
			controller.subscribeDataChangeListner(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Entre com os dados do Produto");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			Alerts.showAlerts("IO Exception", "Erro carregando a janela", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	private void initEditButtons() {
		tableColumnEditar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEditar.setCellFactory(param -> new TableCell<Product, Product>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Product obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, "/gui/ProductForm.fxml", Utils.currentStage(event)));
			}
		});

	}

	private void initRemoveButtons() {
		tableColumnRemover.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemover.setCellFactory(param -> new TableCell<Product, Product>() {
			private final Button button = new Button("Remover");

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
		Optional<ButtonType> result = Alerts.showConfirmation("CONFIRMAÇÃO", "TEM CERTEZA QUE DESEJA REMOVER?");
		
		if(result.get() == ButtonType.OK) {
			if(service == null) {
				throw new IllegalStateException("SERVIÇO ESTAVA NULO");
			}
			try {
				service.remove(obj);
				updateTableView();
			}
			catch (DbIntegretyException e) {
				Alerts.showAlerts("ERRO NA REMOÇÃO DO PRODUTO", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
}