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
import gui.listners.DataChangeListners;
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
import model.entities.Product;
import model.entities.Setores;
import model.exceptions.ValidationException;
import model.services.ProductService;
import model.services.SetoresService;

public class ProdutoFormController implements Initializable {

	private Product entity;

	private ProductService service;

	private SetoresService setorService;

	private List<DataChangeListners> dataChangeListner = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtNome;

	@FXML
	private DatePicker dpDataEntrada;

	@FXML
	private TextField txtQtdEntrada;

	@FXML
	private TextField txtQtdSaida;
	
	@FXML
	private TextField txtQtdTotal;
	
	@FXML
	private DatePicker dpDataSaida;

	@FXML
	ComboBox<Setores> comboBoxSetor;

	@FXML
	private Label labelErrorName;

	@FXML
	private Label labelErrorQtdEntrada;

	@FXML
	private Label labelErrorQtdSaida;

	@FXML
	private Label labelErrorQtdTotal;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	ObservableList<Setores> obsList;

	public void setProduct(Product entity) {
		this.entity = entity;
	}

	public void setServices(ProductService service, SetoresService setorService) {
		this.service = service;
		this.setorService = setorService;
	}

	public void subscribeDataChangeListner(DataChangeListners listner) {
		dataChangeListner.add(listner);
	}

	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("ENTITY WAS NULL");
		}
		if (service == null) {
			throw new IllegalStateException("SERVICE WAS NULL");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListners();
			Utils.currentStage(event).close();
		} 
		catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		} 
		catch (DbException e) {
			Alerts.showAlerts("ERROR SAVING OBJECT", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataChangeListners() {
		for (DataChangeListners listner : dataChangeListner) {
			listner.onDataChanged();
		}

	}

	private Product getFormData() {
		Product obj = new Product();

		ValidationException exception = new ValidationException("VALIDATION ERROR");

		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			exception.addErrors("nome", "CAMPO DE TEXTO NAO PODE SER VAZIO!");
		}
		obj.setNome(txtNome.getText());
		
		if (dpDataEntrada.getValue() == null) {
			exception.addErrors("dataEntrada", "CAMPO DE TEXTO NAO PODE SER VAZIO!");
		} else {
			Instant instant = Instant.from(dpDataEntrada.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataEntrada(Date.from(instant));
		}
		
		if(txtQtdEntrada.getText() == null || txtQtdEntrada.getText().trim().equals("")) {
			exception.addErrors("qtdEntrada", "CAMPO DE TEXTO NAO PODE SER VAZIO!");
		}
		obj.setQtdEntrada(Utils.tryParseToInt(txtQtdEntrada.getText()));
		
		if(txtQtdSaida.getText() == null || txtQtdSaida.getText().trim().equals("")) {
			exception.addErrors("qtdSaida", "CAMPO DE TEXTO NAO PODE SER VAZIO!");
		}
		obj.setQtdSaida(Utils.tryParseToInt(txtQtdSaida.getText()));

		if(txtQtdTotal.getText() == null || txtQtdTotal.getText().trim().equals("")) {
			exception.addErrors("qtdTotal", "CAMPO DE TEXTO NAO PODE SER VAZIO!");
		}
		obj.setQtdTotal(Utils.tryParseToInt(txtQtdTotal.getText()));
		
		if (dpDataSaida.getValue() == null) {
			exception.addErrors("dataSaida", "CAMPO DE TEXTO NAO PODE SER VAZIO!");
		} else {
			Instant instant = Instant.from(dpDataSaida.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataSaida(Date.from(instant));
		}
		
		obj.setSetor(comboBoxSetor.getValue());

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
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNome, 70);
		Utils.formatDatePicker(dpDataEntrada, "dd/MM/yyyy");
		Constraints.setTextFieldInteger(txtQtdEntrada);
		Constraints.setTextFieldInteger(txtQtdSaida);
		Constraints.setTextFieldInteger(txtQtdTotal);
		Utils.formatDatePicker(dpDataSaida, "dd/MM/yyyy");
		initializeComboBoxSetor();
	}

	public void updateFormData() {
		Locale.setDefault(Locale.US);
		if (entity == null) {
			throw new IllegalStateException("ENTITY WAS NULL");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtNome.setText(entity.getNome());
		if (entity.getDataEntrada() != null) {
			dpDataEntrada.setValue(LocalDate.ofInstant(entity.getDataEntrada().toInstant(), ZoneId.systemDefault()));
		}
		txtQtdEntrada.setText(String.valueOf(entity.getQtdEntrada()));
		txtQtdSaida.setText(String.valueOf(entity.getQtdSaida()));
		txtQtdTotal.setText(String.valueOf(entity.getQtdTotal()));
		if (entity.getDataSaida() != null) {
			dpDataSaida.setValue(LocalDate.ofInstant(entity.getDataSaida().toInstant(), ZoneId.systemDefault()));
		}
			
		if (entity.getSetor() == null) {
			comboBoxSetor.getSelectionModel().selectFirst();
		} else {
			comboBoxSetor.setValue(entity.getSetor());
		}
	}

	public void loadAssociateObjects() {
		if (setorService == null) {
			throw new IllegalStateException("SERVIÇO DO SETOR ESTA NULO!");
		}
		List<Setores> list = setorService.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxSetor.setItems(obsList);
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		labelErrorName.setText((fields.contains("nome") ? errors.get("nome") : ""));
		
		labelErrorQtdEntrada.setText((fields.contains("qtdEntrada") ? errors.get("qtdEntrada") : ""));
		
		labelErrorQtdSaida.setText((fields.contains("qtdSaida") ? errors.get("qtdSaida") : ""));
		
		labelErrorQtdTotal.setText((fields.contains("qtdTotal") ? errors.get("qtdTotal") : ""));
	}

	private void initializeComboBoxSetor() {
		Callback<ListView<Setores>, ListCell<Setores>> factory = lv -> new ListCell<Setores>() {
			@Override
			protected void updateItem(Setores item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNome());
			}
		};
		comboBoxSetor.setCellFactory(factory);
		comboBoxSetor.setButtonCell(factory.call(null));
	}
}

