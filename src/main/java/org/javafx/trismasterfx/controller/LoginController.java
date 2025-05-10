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

public class LoginController implements Initializable {
	@FXML private TextField username;
	@FXML private PasswordField password;
	@FXML private Button login;
	@FXML private Label signup;
	@FXML private ImageView welcome;
	
	/* Permette di non mostrare più il dialog di richiesta tipologia connessione (Remote o Local) fino al prossimo riavvio dell'applicazione */
	private static boolean isShowedConnection = false;
	private final String LOGIN_ERROR = "login.error.aut", CONNECTION_CHOICE_MESSAGE = "connection.choice.message";

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.login.setOnAction(_ -> {
			try {
				User.crt_usr_inst(this.username.getText(), this.password.getText());
				int status_code = HttpConnection.login_request();
				if(status_code == 200)
					App.load_window("home", null, null);
				else if(status_code == 404)
					throw new Exception(resources.getString(LOGIN_ERROR));
			} catch(Exception error) {
				error.printStackTrace();
				App.crt_dlg("error_dialog", new GenericDialogController(error.getMessage()));
			}
		});
		
		this.signup.setOnMouseClicked(_ -> App.load_window("submit", null, null));
		
		Platform.runLater(() -> {
			this.welcome.fitWidthProperty().bind(App.get_stage_scene().widthProperty().multiply(0.65));
			this.welcome.fitHeightProperty().bind(App.get_stage_scene().heightProperty().multiply(1.0));
			this.login.requestFocus();
			if(!isShowedConnection) {
				App.crt_dlg("connection_dialog", new ConnectionDialogController(resources.getString(CONNECTION_CHOICE_MESSAGE)));
				isShowedConnection = true;
			}
		});
	}
}
