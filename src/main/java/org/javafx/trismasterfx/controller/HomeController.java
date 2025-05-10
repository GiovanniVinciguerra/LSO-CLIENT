package org.javafx.trismasterfx.controller;

import java.net.URL;
import java.util.ResourceBundle;
import org.javafx.trismasterfx.App;
import org.javafx.trismasterfx.connection.HttpConnection;
import org.javafx.trismasterfx.entity.Match;
import org.javafx.trismasterfx.entity.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class HomeController implements Initializable {
	@FXML Label session_id;
	
	private final String UNAUTHORIZED_USER_ERROR = "user.error";

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			int status_code = HttpConnection.matches_request();
			if(status_code == 401)
				throw new Exception(resources.getString(UNAUTHORIZED_USER_ERROR));
			Match match = ObjectAccessController.getProgressMatch();
			if(match != null) {
				status_code = HttpConnection.update_request(match);
				if(status_code == 401)
					throw new Exception(resources.getString(UNAUTHORIZED_USER_ERROR));
			}
		} catch (Exception error) {
			error.printStackTrace();
			App.crt_dlg("error_dialog", new GenericDialogController(error.getMessage()));
		}
		
		Platform.runLater(() -> this.session_id.setText(String.valueOf(User.get_usr_inst().getSessionId())));
	}
}
