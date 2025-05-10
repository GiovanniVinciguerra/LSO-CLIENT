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
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public class GameDialogController extends GenericDialogController implements Initializable {
	@FXML Canvas table;
	
	private Match match = ObjectAccessController.getProgressMatch();
	private final String UNAUTHORIZED_USER_ERROR = "user.error";
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		this.table.getGraphicsContext2D().setLineWidth(2);
		for(int i = 1; i < 3; i++) {
			this.table.getGraphicsContext2D().strokeLine(i * 100, 0, i * 100, 300); /* Linee verticali */
			this.table.getGraphicsContext2D().strokeLine(0, i * 100, 300, i * 100); /* Linee orizzontali */
		}
		
		this.table.setOnMouseClicked(event -> {
			int col = (int) (event.getX() / 100);
			int row = (int) (event.getY() / 100);
			
			if(this.match.getStep(row, col) == '\0') {
				if(this.match.isYourturn())
					try {
						int status_code = HttpConnection.step_request(this.match.getMatch_id(), this.match.getStepAsText(row, col, this.match.getSeed().charAt(0)));
						if(status_code == 401)
							throw new Exception(resources.getString(UNAUTHORIZED_USER_ERROR));
						this.drawSymbol(row, col, this.match.getSeed().charAt(0));
						this.match.setTurn(false);
						if(this.match.isWinner()) {
							this.drawLine();
							if(this.match.getPlayer_1().compareTo(User.get_usr_inst().getUsername()) == 0)
								this.match.setResult("1");
							else
								this.match.setResult("2");
							
							this.match.setStatus("0");
						} else if(this.match.getStepSize() == 9) {
							this.match.setResult("0");
							this.match.setStatus("0");
						}
					} catch (Exception error) {
						error.printStackTrace();
						App.crt_dlg("error_dialog", new GenericDialogController(error.getMessage()));
					}
			}
		});
		
		Platform.runLater(() -> {
			/* Controlla se è il turno del player1 o del player2 */
			if((this.match.getStepSize() % 2) == 0 && this.match.getPlayer_1().compareTo(User.get_usr_inst().getUsername()) == 0)
				this.match.setTurn(true);
			else if((this.match.getStepSize() % 2) != 0 && this.match.getPlayer_2().compareTo(User.get_usr_inst().getUsername()) == 0)
				this.match.setTurn(true);
			
			for(int i = 0; i < 3; i++) {
				for(int j = 0; j < 3; j++) {
					char value = this.match.getStep(i, j);
					this.drawSymbol(i, j, value);
				}
			}
		});
	}
	
	private void drawSymbol(int row, int col, char value) {
	    if (value == 'X') {
	        /* Disegna la 'X' */
	        this.table.getGraphicsContext2D().strokeLine(col * 100 + 20, row * 100 + 20, (col + 1) * 100 - 20, (row + 1) * 100 - 20);
	        this.table.getGraphicsContext2D().strokeLine(col * 100 + 20, (row + 1) * 100 - 20, (col + 1) * 100 - 20, row * 100 + 20);
	    } else if(value == 'O')
	        /* Disegna il 'O' */
	    	this.table.getGraphicsContext2D().strokeOval(col * 100 + 20, row * 100 + 20, 100 - 40, 100 - 40);
	}
	
	private void drawLine() {
		this.table.getGraphicsContext2D().setLineWidth(5);
		this.table.getGraphicsContext2D().setStroke(Color.RED);
		this.table.getGraphicsContext2D().strokeLine(this.match.getStartX(), this.match.getStartY(), this.match.getEndX(), this.match.getEndY());
	}
}
