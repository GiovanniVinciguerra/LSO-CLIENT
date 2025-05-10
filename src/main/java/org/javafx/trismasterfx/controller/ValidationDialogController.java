package org.javafx.trismasterfx.controller;

import java.net.URL;
import java.util.ResourceBundle;
import org.javafx.trismasterfx.App;
import org.javafx.trismasterfx.connection.HttpConnection;
import org.javafx.trismasterfx.entity.Match;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public class ValidationDialogController extends GenericDialogController implements Initializable {
	private Button yes = null, no = null;
	
	private final String BTN_YES_TXT = "yes.button", BTN_NO_TXT = "no.button";
	private final String UNAUTHORIZED_USER_ERROR = "user.error";

	public ValidationDialogController(String content) {
		super(content);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		super.root.getButtonTypes().clear();
		ButtonType yes_btn = new ButtonType(resources.getString(BTN_YES_TXT), ButtonBar.ButtonData.YES);
		ButtonType no_btn = new ButtonType(resources.getString(BTN_NO_TXT), ButtonBar.ButtonData.NO);
		super.root.getButtonTypes().addAll(yes_btn, no_btn);
		
		this.yes = (Button) super.root.lookupButton(yes_btn);
		this.no = (Button) super.root.lookupButton(no_btn);
		
		this.yes.setOnAction(_ -> {
			Match match = ObjectAccessController.getValidationMatch();
			if(match != null) {
				try {
					int status_code = HttpConnection.progress_request(match.getMatch_id());
					if(status_code == 200)
						match.setStatus("1");
					else if(status_code == 401)
						throw new Exception(resources.getString(UNAUTHORIZED_USER_ERROR));
					App.close_dialog();
				} catch (Exception error) {
					error.printStackTrace();
					App.close_dialog();
					App.crt_dlg("error_dialog", new GenericDialogController(error.getMessage()));
				}
			}
		});
		
		this.no.setOnAction(_ -> {
			Match match = ObjectAccessController.getValidationMatch();
			if(match != null) {
				try {
					int status_code = HttpConnection.waiting_request(match);
					if(status_code == 200)
						match.setStatus("2");
					else if(status_code == 401)
						throw new Exception(resources.getString(UNAUTHORIZED_USER_ERROR));
					App.close_dialog();
				} catch (Exception error) {
					error.printStackTrace();
					App.close_dialog();
					App.crt_dlg("error_dialog", new GenericDialogController(error.getMessage()));
				}
			}
		});
		
		Platform.runLater(() -> {
			this.yes.setStyle("-fx-background-radius: 8; -fx-border-radius: 8;");
			this.yes.requestFocus();
			this.no.setStyle("-fx-background-radius: 8; -fx-border-radius: 8;");
		});
	}

}
