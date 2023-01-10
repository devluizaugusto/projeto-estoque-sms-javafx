package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Product;
import model.services.ProductService;

public class ProductListController implements Initializable{

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
	private Button btNovo;
	
	private ObservableList<Product> obsList;
	
	@FXML
	public void onBtNovoAction(ActionEvent event ) {
		System.out.println("onBtNovoAction");
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
		if(service == null) {
			throw new IllegalStateException("Serviço estava nulo!");
		}
		List<Product> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewProduto.setItems(obsList);
	}
	
}
