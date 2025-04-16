package com.maven.trismaster.controller;

import java.net.URL;
import java.util.ResourceBundle;
import com.maven.trismaster.App;
import com.maven.trismaster.connection.HttpConnection;
import javafx.fxml.Initializable;

public class HomeController implements Initializable {
	private final String UNAUTHORIZED_USER_ERROR = "user.error";

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			int status_code = HttpConnection.matches_request();
			if(status_code == 401)
				throw new Exception(resources.getString(UNAUTHORIZED_USER_ERROR));
		} catch (Exception error) {
			error.printStackTrace();
			App.crt_dlg("error_dialog", new GenericDialogController(error.getMessage()));
		}
	}
}
