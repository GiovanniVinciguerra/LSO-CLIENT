package org.javafx.trismasterfx;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import org.javafx.trismasterfx.connection.HttpConnection;
import org.javafx.trismasterfx.controller.GameDialogController;
import org.javafx.trismasterfx.controller.GenericDialogController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application {	
	public static final double DEFAULT_WIDTH = 1366.0, DEFAULT_HEIGTH = 768.0;
	public static final double DEFAULT_MIN_WIDTH = 960.0, DEFAULT_MIN_HEIGHT = 540.0;
	private final String DEFAULT_LOCALE_PATH = "org.javafx.trismasterfx.locales.strings";
	private static ResourceBundle resource = null;
	private static Stage stage = null, dialog_stage = null, game_dialog = null;
	private static String connectionType = new String("Remote");
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// English United Kingdom
		resource = ResourceBundle.getBundle(DEFAULT_LOCALE_PATH, Locale.UK);

		Parent parent = FXMLLoader.load(getClass().getResource("fxml/login.fxml"), resource);
		
		stage = new Stage();
		
		stage.setTitle("TrisMaster");
		stage.initStyle(StageStyle.DECORATED);
		stage.setScene(new Scene(parent, Color.TRANSPARENT));
		stage.setMinWidth(DEFAULT_MIN_WIDTH);
		stage.setMinHeight(DEFAULT_MIN_HEIGHT);
		stage.setResizable(true);
		stage.getIcons().add(new Image(App.class.getResourceAsStream("/image/trismaster.png")));
		
		stage.show();
	}
	
	public static String get_resource_key(String key) {
		return resource.getString(key);
	}
	
	public static void load_window(String window, Double width, Double heigth) {
		try {
			Parent parent = FXMLLoader.load(App.class.getResource("fxml/" + window + ".fxml"), resource);
			
			if(width == null || heigth == null) {
				double scene_width = stage.getScene().getWidth();
				double scene_heigth = stage.getScene().getHeight();
				stage.setScene(new Scene(parent, scene_width, scene_heigth, Color.TRANSPARENT));
			} else
				stage.setScene(new Scene(parent, width, heigth, Color.TRANSPARENT));
		} catch (IOException error) {
			error.printStackTrace();
		}
	}
	
	public static void crt_dlg(String dialog, GenericDialogController ctrl) {
		try {
			GenericDialogController controller = ctrl;
			load_dialog(dialog, controller);
		} catch (IOException error) {
			error.printStackTrace();
		}
	}
	
	private static void load_dialog(String dialog, GenericDialogController controller) throws IOException {
		FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/" + dialog + ".fxml"), resource);
		loader.setController(controller);
		
		Parent parent = loader.load();
		
		if(controller instanceof GameDialogController) {
			game_dialog = new Stage();
			
			game_dialog.setScene(new Scene(parent, Color.TRANSPARENT));
			game_dialog.initModality(Modality.WINDOW_MODAL);
			game_dialog.initStyle(StageStyle.TRANSPARENT);
			game_dialog.initOwner(stage);
			Platform.runLater(() -> game_dialog.showAndWait());
		} else {
			dialog_stage = new Stage();
			
			dialog_stage.setScene(new Scene(parent, Color.TRANSPARENT));
			dialog_stage.initModality(Modality.WINDOW_MODAL);
			dialog_stage.initStyle(StageStyle.TRANSPARENT);
			dialog_stage.initOwner(stage);
			Platform.runLater(() -> dialog_stage.showAndWait());
		}
	}
	
	public static void close_dialog() {
		if(dialog_stage != null)
			dialog_stage.close();
		
		dialog_stage = null;
	}
	
	public static void close_game_dialog() {
		if(game_dialog != null)
			game_dialog.close();
		
		game_dialog = null;
	}
	
	public static Node load_cell(String cell, Object controller) throws IOException {
		Node node = null;
		
		FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/" + cell + ".fxml"), resource);
		loader.setController(controller);
		node = loader.load();
		
		return node;
	}
	
	public static Scene get_stage_scene() {
		return stage.getScene();
	}
	
	public static boolean isRemote() {
		return connectionType.compareTo("Remote") == 0;
	}
	
	public static String getConnectionType() {
		return connectionType;
	}
	
	public static void setConnectionType(String type) {
		connectionType = type;
		HttpConnection.set_url();
	}
}
