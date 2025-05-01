package com.maven.trismaster.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import com.maven.trismaster.App;
import com.maven.trismaster.dao.SettingsDaoService;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public class ConnectionDialogController extends GenericDialogController implements Initializable {
	private Button remote = null, local = null;
	
	private SettingsDaoService settings_dao = new SettingsDaoService();
	
	private final String BTN_REMOTE_TXT = "remote.button", BTN_LOCAL_TXT = "local.button";
	
	public ConnectionDialogController(String content) {
		super(content);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		super.root.getButtonTypes().clear();
		ButtonType remote_btn = new ButtonType(resources.getString(BTN_REMOTE_TXT), ButtonBar.ButtonData.LEFT);
		ButtonType local_btn = new ButtonType(resources.getString(BTN_LOCAL_TXT), ButtonBar.ButtonData.RIGHT);
		super.root.getButtonTypes().addAll(remote_btn, local_btn);
		
		this.remote = (Button) super.root.lookupButton(remote_btn);
		this.local = (Button) super.root.lookupButton(local_btn);
		
		this.remote.setOnAction(_ -> {
			try {
				App.setConnectionType("Remote");
				this.settings_dao.write_and_store();
				App.close_dialog();
			} catch (IOException error) {
				error.printStackTrace();
				App.close_dialog();
				App.crt_dlg("error_dialog", new GenericDialogController(error.getMessage()));
			}
		});
		
		this.local.setOnAction(_ -> {
			try {
				App.setConnectionType("Local");
				this.settings_dao.write_and_store();
				App.close_dialog();
			} catch (IOException error) {
				error.printStackTrace();
				App.close_dialog();
				App.crt_dlg("error_dialog", new GenericDialogController(error.getMessage()));
			}
		});
		
		Platform.runLater(() -> {
			this.remote.setStyle("-fx-background-radius: 8; -fx-border-radius: 8;");
			this.remote.requestFocus();
			this.local.setStyle("-fx-background-radius: 8; -fx-border-radius: 8;");
		});
	}
}
