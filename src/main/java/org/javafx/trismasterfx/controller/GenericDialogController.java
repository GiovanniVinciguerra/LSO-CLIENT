package org.javafx.trismasterfx.controller;

import java.net.URL;
import java.util.ResourceBundle;
import org.javafx.trismasterfx.App;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;

public class GenericDialogController implements Initializable {
	@FXML protected DialogPane root;
	@FXML protected Label cnt_txt;
	private Button close = null;
	
	private String content = null;
	
	private final String BTN_CLOSE_TXT = "close.button";
	
	public GenericDialogController() {}
	
	public GenericDialogController(String content) {
		this.content = content;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ButtonType cls_btn = new ButtonType(resources.getString(BTN_CLOSE_TXT), ButtonBar.ButtonData.CANCEL_CLOSE);
		this.root.getButtonTypes().add(cls_btn);
		
		this.close = (Button) this.root.lookupButton(cls_btn);
		this.close.setOnAction(_ -> {
			if(this instanceof GameDialogController)
				App.close_game_dialog();
			else
				App.close_dialog();
		});
		
		if(this.content != null)
			this.cnt_txt.setText(this.content);
		
		Platform.runLater(() -> {
			this.close.setStyle("-fx-background-radius: 8; -fx-border-radius: 8;");
			this.close.requestFocus();
		});
	}
}
