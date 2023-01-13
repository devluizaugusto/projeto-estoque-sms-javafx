package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.ProductService;
import model.services.SetoresService;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemProduto;
	
	@FXML
	private MenuItem menuItemSetor;

	@FXML
	private MenuItem menuItemSobre;

	@FXML
	public void onMenuItemProdutoAction() {
		loadView("/gui/ProductList.fxml", (ProductListController controller) -> {
		controller.setProductService(new ProductService());
		controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemSetorAction() {
		loadView("/gui/SetorList.fxml", (SetorListController controller) -> {
		controller.setSetoresService(new SetoresService());
		controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemSobreAction() {
		loadView("/gui/About.fxml", x -> {});
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}
	
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVbBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVbox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVbox.getChildren().get(0);
			mainVbox.getChildren().clear();
			mainVbox.getChildren().add(mainMenu);
			mainVbox.getChildren().addAll(newVbBox.getChildren());
			
			T controller = loader.getController();
			initializingAction.accept(controller);
			
		} catch (IOException e) {
			Alerts.showAlerts("IO Exception", "Error ", e.getMessage(), AlertType.ERROR);
		}

	}
}
