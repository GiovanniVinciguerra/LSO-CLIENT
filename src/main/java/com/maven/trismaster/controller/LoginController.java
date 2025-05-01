package com.maven.trismaster.controller;

import java.net.URL;
import java.util.ResourceBundle;
import com.maven.trismaster.App;
import com.maven.trismaster.connection.HttpConnection;
import com.maven.trismaster.dao.UserDaoService;
import com.maven.trismaster.entity.User;
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
	private UserDaoService user_dao = new UserDaoService();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.login.setOnAction(_ -> {
			try {
				User.crt_usr_inst(this.username.getText(), this.password.getText());
				int status_code = HttpConnection.login_request();
				if(status_code == 200) {
					user_dao.write_and_store();
					App.load_window("home", null, null);
				}
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
