package org.javafx.trismasterfx.controller;

import java.net.URL;
import java.util.ResourceBundle;
import org.javafx.trismasterfx.App;
import org.javafx.trismasterfx.connection.HttpConnection;
import org.javafx.trismasterfx.entity.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class SubmitController implements Initializable {
	@FXML TextField name, surname, mail, username;
	@FXML PasswordField password;
	@FXML Label signin;
	@FXML Button submit;
	@FXML ImageView welcome;
	
	private final String SUBMIT_ERROR_CONFLICT = "submit.conflict";
	private final String SUBMIT_ERROR_INTERNAL = "submit.internal";
	private final String SUBMIT_OK = "submit.ok";

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.submit.setOnAction(_ -> {
			try {
				User.crt_usr_inst(this.name.getText(), this.surname.getText(), this.mail.getText(), this.username.getText(), this.password.getText());
				int status_code = HttpConnection.submit_request();
				if(status_code == 200)
					App.crt_dlg("success_dialog", new GenericDialogController(resources.getString(SUBMIT_OK)));
				else if(status_code == 409)
					throw new Exception(SUBMIT_ERROR_CONFLICT);
				else if(status_code == 500)
					throw new Exception(SUBMIT_ERROR_INTERNAL);
			} catch(Exception error) {
				error.printStackTrace();
				App.crt_dlg("error_dialog", new GenericDialogController(error.getMessage()));
			}
		});
		
		this.signin.setOnMouseClicked(_ -> App.load_window("login", null, null));
		
		Platform.runLater(() -> {
			this.welcome.fitWidthProperty().bind(App.get_stage_scene().widthProperty().multiply(0.65));
			this.welcome.fitHeightProperty().bind(App.get_stage_scene().heightProperty().multiply(1.0));
			this.submit.requestFocus();
		});
	}

}
