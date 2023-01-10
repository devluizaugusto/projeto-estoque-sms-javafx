package gui;

import java.net.URL;
import java.util.ResourceBundle;

import db.DbException;
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
import model.entities.Product;
import model.services.ProductService;

public class ProductFormController implements Initializable {

	private Product entity;

	private ProductService service;

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtNome;

	@FXML
	private TextField txtQtdEntrada;

	@FXML
	private TextField txtQtdSaida;

	@FXML
	private TextField txtQtdTotal;

	@FXML
	private Label labelError;

	@FXML
	private Button btSalvar;

	@FXML
	private Button btCancelar;
	
	public void setProduct(Product entity) {
		this.entity = entity;
	}

	public void setProductService(ProductService service) {
		this.service = service;
	}

	@FXML
	public void onBtSalvar(ActionEvent event) {
		if(entity == null) {
			throw new IllegalStateException("Entidade estava nula!");
		}
		if(service == null) {
			throw new IllegalStateException("Serviço estava nulo!");
		}
		try {
		entity = getFormData();
		service.saveOrUpdate(entity);
		Utils.currentStage(event).close();
	}
		catch (DbException e) {
			Alerts.showAlerts("Erro salvando produto", null, e.getMessage(), AlertType.ERROR);
		}
	}
	private Product getFormData() {
		Product obj = new Product();
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		obj.setNome(txtNome.getText());
		obj.setQtdEntrada(Utils.tryParseToInt(txtQtdEntrada.getText()));
		obj.setQtdSaida(Utils.tryParseToInt(txtQtdSaida.getText()));
		obj.setQtdTotal(Utils.tryParseToInt(txtQtdTotal.getText()));
		
		return obj;
	}

	@FXML
	public void onBtCancelar() {
		System.out.println("onBtCancelar");
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLenght(txtNome, 30);
		Constraints.setTextFieldInteger(txtQtdEntrada);
		Constraints.setTextFieldInteger(txtQtdSaida);
		Constraints.setTextFieldInteger(txtQtdTotal);
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entidade estava nula!");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtNome.setText(entity.getNome());
		txtQtdEntrada.setText(String.valueOf(entity.getQtdEntrada()));
		txtQtdSaida.setText(String.valueOf(entity.getQtdSaida()));
		txtQtdTotal.setText(String.valueOf(entity.getQtdTotal()));

	}

}
