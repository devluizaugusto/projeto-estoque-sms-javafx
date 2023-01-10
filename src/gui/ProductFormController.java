package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Product;

public class ProductFormController implements Initializable {
	
	private Product entity;
	
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
	
	@FXML
	public void onBtSalvar() {
		System.out.println("onBtSalvar");
	}
	
	@FXML
	public void onBtCancelar() {
		System.out.println("onBtCancelar");
	}
	
	public void setProduct(Product entity) {
		this.entity = entity;
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
		if(entity == null) {
			throw new IllegalStateException("Entidade estava nula!");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtNome.setText(entity.getNome());
		txtQtdEntrada.setText(String.valueOf(entity.getQtdEntrada()));
		txtQtdSaida.setText(String.valueOf(entity.getQtdSaida()));
		txtQtdTotal.setText(String.valueOf(entity.getQtdTotal()));
		
	}

}
