package com.maven.trismaster.controller;

import java.net.URL;
import java.security.InvalidParameterException;
import java.util.ResourceBundle;
import com.maven.trismaster.App;
import com.maven.trismaster.connection.HttpConnection;
import com.maven.trismaster.dao.UserDaoService;
import com.maven.trismaster.entity.Match;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class ButtonBarController implements Initializable {
	@FXML private Button logout, new_game, about;
	
	private UserDaoService user_dao = new UserDaoService();
	
	private final String UNAUTHORIZED_USER_ERROR = "user.error";

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.logout.setOnAction(_ -> {
			try {
				int status_code = HttpConnection.logout_request();
				if(status_code == 200) {
					user_dao.delete();
					App.load_window("login", null, null);
				} else if(status_code == 401)
					throw new Exception(resources.getString(UNAUTHORIZED_USER_ERROR));
			} catch(Exception error) {
				error.printStackTrace();
				App.crt_dlg("error_dialog", new GenericDialogController(error.getMessage()));
			}
		});
		
		this.new_game.setOnAction(_ -> {
			try {
				int status_code = HttpConnection.new_game_request();
				if(status_code == 200) {
					Match tmp = null;
					for(Match item : ObjectAccessController.getMatches())
						if(item.getStatus().compareTo("3") == 0 || item.getStatus().compareTo("2") == 0 || item.getStatus().compareTo("1") == 0)
							tmp = item;
					if(tmp == null) {
						tmp = ObjectAccessController.getMatches().getFirst();
						status_code = HttpConnection.waiting_request(tmp);
						if(status_code == 200)
							tmp.setStatus("2");
						else if(status_code == 401)
							throw new InvalidParameterException(resources.getString(UNAUTHORIZED_USER_ERROR));
					}
				} else if(status_code == 401)
					throw new InvalidParameterException(resources.getString(UNAUTHORIZED_USER_ERROR));
			} catch(InvalidParameterException error) {
				error.printStackTrace();
				App.crt_dlg("error_dialog", new GenericDialogController(error.getMessage()));
			} catch(Exception error) {
				error.printStackTrace();
				ObjectAccessController.getMatches().removeLast();
				App.crt_dlg("error_dialog", new GenericDialogController(error.getMessage()));
			}
		});
		
		this.about.setOnAction(_ -> App.crt_dlg("about_dialog", new GenericDialogController(null)));
		
		Platform.runLater(() -> this.new_game.requestFocus());
	}

}
