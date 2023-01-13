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
import model.entities.Setores;
import model.services.SetoresService;

public class SetorListController implements Initializable, DataChangeListners {

	private SetoresService service;

	@FXML
	private TableView<Setores> tableViewSetor;

	@FXML
	private TableColumn<Setores, Integer> tableColumnId;

	@FXML
	private TableColumn<Setores, String> tableColumnNome;

	@FXML
	private TableColumn<Setores, Setores> tableColumnEditar;

	@FXML
	private TableColumn<Setores, Setores> tableColumnRemover;

	@FXML
	private Button btNovo;

	private ObservableList<Setores> obsList;

	@FXML
	public void onBtNovoAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Setores obj = new Setores();
		createDialogForm(obj, "/gui/SetorForm.fxml", parentStage);
	}

	public void setSetoresService(SetoresService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewSetor.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Serviço estava nulo!");
		}
		List<Setores> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewSetor.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Setores obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			SetorFormController controller = loader.getController();
			controller.setSetores(obj);
			controller.setSetoresService(new SetoresService());
			controller.subscribeDataChangeListner(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("ENTRE COM OS DADOS DO SETOR");
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
		tableColumnEditar.setCellFactory(param -> new TableCell<Setores, Setores>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Setores obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, "/gui/SetorForm.fxml", Utils.currentStage(event)));
			}
		});

	}

	private void initRemoveButtons() {
		tableColumnRemover.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemover.setCellFactory(param -> new TableCell<Setores, Setores>() {
			private final Button button = new Button("Remover");

			@Override
			protected void updateItem(Setores obj, boolean empty) {
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
	
	private void removeEntity(Setores obj) {
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
				Alerts.showAlerts("ERRO NA REMOÇÃO DO SETOR", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
}
