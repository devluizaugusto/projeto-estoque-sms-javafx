package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listners.DataChangeListners;
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
import model.exceptions.ValidationException;
import model.services.ProductService;

public class ProductFormController implements Initializable {

	private Product entity;

	private ProductService service;

	private List<DataChangeListners> dataChangeListner = new ArrayList<>();
	
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
	private Label labelErrorName;
	
	@FXML
	private Label labelErrorQtdEntrada;
	
	@FXML
	private Label labelErrorQtdSaida;
	
	@FXML
	private Label labelErrorQtdTotal;

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
	
	public void subscribeDataChangeListner(DataChangeListners listner) {
		dataChangeListner.add(listner);
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
		notifyDataChangeListner();
		Utils.currentStage(event).close();
	}
		catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		}
		catch (DbException e) {
			Alerts.showAlerts("Erro salvando produto", null, e.getMessage(), AlertType.ERROR);
		}
	}
	private void notifyDataChangeListner() {
		for (DataChangeListners listner : dataChangeListner) {
			listner.onDataChanged();
		}
		
	}
	
	@FXML
	public void onBtCancelar(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	private Product getFormData() {
		Product obj = new Product();
		
		ValidationException exception = new ValidationException("ERRO NA VALIDAÇÃO");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if(txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			exception.addErrors("nome", "CAMPO DE TEXTO NAO PODE SER VAZIO");
		}
		obj.setNome(txtNome.getText());
		
		if(txtQtdEntrada.getText() == null || txtQtdEntrada.getText().trim().equals("")) {
			exception.addErrors("qtdEntrada", "CAMPO DE TEXTO NAO PODE SER VAZIO");
		}
		obj.setQtdEntrada(Utils.tryParseToInt(txtQtdEntrada.getText()));
		
		if(txtQtdSaida.getText() == null || txtQtdSaida.getText().trim().equals("")) {
			exception.addErrors("qtdSaida", "CAMPO DE TEXTO NAO PODE SER VAZIO");
		}
		obj.setQtdSaida(Utils.tryParseToInt(txtQtdSaida.getText()));
		
		if(txtQtdTotal.getText() == null || txtQtdTotal.getText().trim().equals("")) {
			exception.addErrors("qtdTotal", "CAMPO DE TEXTO NAO PODE SER VAZIO");
		}
		obj.setQtdTotal(Utils.tryParseToInt(txtQtdTotal.getText()));
		
		if(exception.getErrors().size() > 0) {
			throw exception;
		}
		
		return obj;
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
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		labelErrorName.setText(fields.contains("nome") ? errors.get("nome") : "");
		labelErrorQtdEntrada.setText(fields.contains("qtdEntrada") ? errors.get("qtdEntrada") : "");
		labelErrorQtdSaida.setText(fields.contains("qtdSaida") ? errors.get("qtdSaida") : "");
		labelErrorQtdTotal.setText(fields.contains("qtdTotal") ? errors.get("qtdTotal") : "");
	}
}
