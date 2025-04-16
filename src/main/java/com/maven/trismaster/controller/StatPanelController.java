package com.maven.trismaster.controller;

import java.net.URL;
import java.util.ResourceBundle;
import com.maven.trismaster.entity.Stat;
import com.maven.trismaster.App;
import com.maven.trismaster.connection.HttpConnection;
import com.maven.trismaster.entity.User;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class StatPanelController implements Initializable {
	@FXML private Label title, total, stat;
	
	private final String UNAUTHORIZED_USER_ERROR = "user.error";
	private final String STAT_WIN = "stat.win";
	private final String STAT_LOSE = "stat.lose";

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ObjectAccessController.getStats().addListener((ListChangeListener<Stat>) change -> {
			change.next();
			if(change.wasAdded()) {
				int tot_match = ObjectAccessController.getStats().size();
				this.total.setText(String.valueOf(tot_match));
				
				float num_win = 0, num_lose = 0;
				for (Stat item : ObjectAccessController.getStats()) {
					if(item.getResult().compareTo("1") == 0)
						if(item.getPlayer_1().compareTo(User.get_usr_inst().getUsername()) == 0)
							num_win++;
						else
							num_lose++;
					else if(item.getResult().compareTo("2") == 0)
						if(item.getPlayer_2().compareTo(User.get_usr_inst().getUsername()) == 0)
							num_win++;
						else
							num_lose++;
					else // Il risultato è parità e viene considerata come sconfitta
						num_lose++;
				}
				this.stat.setText(
					resources.getString(STAT_WIN) +
					"\t" +
					String.format("%.2f", (num_win / tot_match) * 100) +
					"%\t\t" +
					resources.getString(STAT_LOSE) +
					"\t" +
					String.format("%.2f", (num_lose / tot_match) * 100) +
					"%"
				);
			}
		});
		
		try {
			int status_code = HttpConnection.stat_request();
			if(status_code == 401)
				throw new Exception(resources.getString(UNAUTHORIZED_USER_ERROR));
		} catch(Exception error) {
			error.printStackTrace();
			App.crt_dlg("error_dialog", new GenericDialogController(error.getMessage()));
		}
		
		Platform.runLater(() -> {
			String complete_title = User.get_usr_inst().getName() + this.title.getText();
			title.setText(complete_title);
		});
	}

}
