package org.javafx.trismasterfx.controller;

import java.net.URL;
import java.util.ResourceBundle;
import org.javafx.trismasterfx.App;
import org.javafx.trismasterfx.connection.HttpConnection;
import org.javafx.trismasterfx.entity.Message;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class MessagePanelController implements Initializable {
	@FXML ListView<Message> container;
	@FXML Button refresh;
	
	private final String UNAUTHORIZED_USER_ERROR = "user.error";

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.container.setItems(ObjectAccessController.getMessages());
		
		this.container.setCellFactory(_ -> new ListCell<Message>() {
			@Override
			protected void updateItem(Message message, boolean empty) {
				super.updateItem(message, empty);
				
				if(empty || message == null)
					super.setText(null);
				else {
					if(message.getLabel().compareTo("0") == 0)
						super.setText("[FINISH]\t" + message.getBody() + "\t" + message.getTimestamp());
					else if(message.getLabel().compareTo("1") == 0)
						super.setText("[PROGRESS]\t" + message.getBody() + "\t" + message.getTimestamp());
					else if(message.getLabel().compareTo("2") == 0)
						super.setText("[WAIT]\t" + message.getBody() + "\t" + message.getTimestamp());
					else if(message.getLabel().compareTo("4") == 0)
						super.setText("[CREATE]\t" + message.getBody() + "\t" + message.getTimestamp());
				}
			}
		});
		
		this.refresh.setOnAction(_ -> {
			try {
				int status_code = HttpConnection.message_request();
				if(status_code == 401)
					throw new Exception(resources.getString(UNAUTHORIZED_USER_ERROR));
			} catch (Exception error) {
				error.printStackTrace();
				App.crt_dlg("error_dialog", new GenericDialogController(error.getMessage()));
			}
		});
		
		Platform.runLater(() -> this.refresh.fire());
	}
	
}
