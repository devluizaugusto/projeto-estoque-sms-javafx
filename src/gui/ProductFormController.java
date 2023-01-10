package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ProductFormController implements Initializable {

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

}
