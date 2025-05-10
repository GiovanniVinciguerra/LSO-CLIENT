package org.javafx.trismasterfx.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import org.javafx.trismasterfx.App;
import org.javafx.trismasterfx.connection.HttpConnection;
import org.javafx.trismasterfx.entity.Match;
import org.javafx.trismasterfx.entity.Stat;
import org.javafx.trismasterfx.entity.User;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class GameListCellController extends ListCell<Match> implements Initializable {
	@FXML Pane tag, notice;
	@FXML Text player_1, player_2, status, seed, match_id;
	@FXML Button refresh, play;
	
	private InvalidationListener statusPropertyListener = null;
	private ChangeListener<? super Boolean> cellPropertyListener = null;
	
	private final String UNAUTHORIZED_USER_ERROR = "user.error";
	private final String INFO_NEW_CREATION = "info.creation", INFO_VALIDATION = "info.validation", INFO_WAITING = "info.waiting", INFO_PROGRESS = "info.progress";
	private final String WINNER = "game.winner", LOSER = "game.loser", TIE = "game.tie", VALIDATION = "game.validation";
	private final String REFUSE_MATCH = "refuse.match";
	
	private ResourceBundle resources = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.resources = resources;
		
		this.statusPropertyListener = _ -> {
			/* Imposta la notifica visibile perchè c'è stato un cambiamento */
			this.notice.setVisible(true);
			
			/* Mostra o nasconde il bottone di refresh */
			if(super.getItem().getStatus().compareTo("4") == 0 || super.getItem().getStatus().compareTo("0") == 0)
				this.refresh.setVisible(false);
			else
				this.refresh.setVisible(true);
			
			if(super.getItem().getStatus().compareTo("1") == 0)
				this.play.setVisible(true);
			else
				this.play.setVisible(false);
			
			/* Set tag style */
			if(super.getItem().getStatus().compareTo("0") == 0) {
				this.tag.setStyle("-fx-background-color: #FFFFFF;-fx-background-radius: 7 0 0 7;-fx-border-radius: 7 0 0 7;-fx-border-color: none;");
				
				/* Gestisce la vittoria la sconfitta e il pareggio */
				if((super.getItem().getResult().compareTo("1") == 0 && super.getItem().getPlayer_1().compareTo(User.get_usr_inst().getUsername()) == 0) || (super.getItem().getResult().compareTo("2") == 0 && super.getItem().getPlayer_2().compareTo(User.get_usr_inst().getUsername()) == 0)) {
					try {
						int status_code = HttpConnection.winner_request(super.getItem().getMatch_id());
						if(status_code == 401)
							throw new Exception(resources.getString(UNAUTHORIZED_USER_ERROR));
					} catch (Exception error) {
						error.printStackTrace();
						App.crt_dlg("error_dialog", new GenericDialogController(error.getMessage()));
					}
					
					App.crt_dlg("result_dialog", new GenericDialogController(resources.getString(WINNER)));
				} else if(super.getItem().getResult().compareTo("0") == 0) {
					if(super.getItem().getPlayer_1().compareTo(User.get_usr_inst().getUsername()) == 0) {
						try {
							int status_code = HttpConnection.tie_request(super.getItem().getMatch_id());
							if(status_code == 401)
								throw new Exception(resources.getString(UNAUTHORIZED_USER_ERROR));
						} catch (Exception error) {
							error.printStackTrace();
							App.crt_dlg("error_dialog", new GenericDialogController(error.getMessage()));
						}
					}
					
					App.crt_dlg("result_dialog", new GenericDialogController(resources.getString(TIE)));
				} else
					App.crt_dlg("result_dialog", new GenericDialogController(resources.getString(LOSER)));
				
				Stat stat = new Stat(super.getItem().getPlayer_1(), super.getItem().getPlayer_2(), super.getItem().getResult());
				ObjectAccessController.getStats().add(stat);
				
				/* Seleziona un nuovo match, se  esiste, da poter mettere in attesa */
				select_new_match();
				
				Platform.runLater(() -> {
					ObjectAccessController.getMatches().remove(ObjectAccessController.getFinishMatch());
					super.getListView().refresh();
				});
			} else if(super.getItem().getStatus().compareTo("1") == 0)
				this.tag.setStyle("-fx-background-color: #32CD32;-fx-background-radius: 7 0 0 7;-fx-border-radius: 7 0 0 7;-fx-border-color: none;");
			else if(super.getItem().getStatus().compareTo("2") == 0) {
				this.tag.setStyle("-fx-background-color: #FFA500;-fx-background-radius: 7 0 0 7;-fx-border-radius: 7 0 0 7;-fx-border-color: none;");
				if(super.getItem().getPlayer_1().compareTo(User.get_usr_inst().getUsername()) != 0 && super.getItem().isPlayer2Null()) {
					App.crt_dlg("info_dialog", new GenericDialogController(resources.getString(REFUSE_MATCH)));
					
					select_new_match();
					
					Platform.runLater(() -> {
						ObjectAccessController.getMatches().remove(super.getItem());
						super.getListView().refresh();
					});
				} else if(super.getItem().getPlayer_1().compareTo(User.get_usr_inst().getUsername()) == 0 && !super.getItem().isPlayer2Null())
					super.getItem().setPlayer_2(null);
			} else if(super.getItem().getStatus().compareTo("3") == 0) {
				this.tag.setStyle("-fx-background-color: #000000;-fx-background-radius: 7 0 0 7;-fx-border-radius: 7 0 0 7;-fx-border-color: none;");
				/* Fa apparire al player_1 il dialog di conferma per giocare o meno la partita */
				if(super.getItem().getPlayer_1().compareTo(User.get_usr_inst().getUsername()) == 0)
					App.crt_dlg("validation_dialog", new ValidationDialogController(resources.getString(VALIDATION) + " " + this.player_2.getText() + "?"));
			} else if(super.getItem().getStatus().compareTo("4") == 0)
				this.tag.setStyle("-fx-background-color: #1E90FF;-fx-background-radius: 7 0 0 7;-fx-border-radius: 7 0 0 7;-fx-border-color: none;");
		};
		this.status.textProperty().addListener(this.statusPropertyListener);
		
		this.cellPropertyListener = (_, _, isSelected) -> {
			if(isSelected) {					
				boolean wasVisible = this.notice.isVisible();
				
				if(super.getItem().getStatus().compareTo("4") == 0) {
					if(wasVisible)
						App.crt_dlg("info_dialog", new GenericDialogController(resources.getString(INFO_NEW_CREATION)));
				} else if(super.getItem().getStatus().compareTo("3") == 0) {
					if(wasVisible)
						App.crt_dlg("info_dialog", new GenericDialogController(resources.getString(INFO_VALIDATION)));
				} else if(super.getItem().getStatus().compareTo("2") == 0) {
					if(wasVisible)
						App.crt_dlg("info_dialog", new GenericDialogController(resources.getString(INFO_WAITING)));
				} else if(super.getItem().getStatus().compareTo("1") == 0) {
					if(wasVisible)
						App.crt_dlg("info_dialog", new GenericDialogController(resources.getString(INFO_PROGRESS)));
				}
				
				this.notice.setVisible(false);
			}
		};
		super.selectedProperty().addListener(this.cellPropertyListener);
		
		this.refresh.setOnAction(_ -> {
			try {
				int status_code = HttpConnection.update_request(super.getItem());
				if(status_code == 401)
					throw new Exception(resources.getString(UNAUTHORIZED_USER_ERROR));
			} catch (Exception error) {
				error.printStackTrace();
				App.crt_dlg("error_dialog", new GenericDialogController(error.getMessage()));
			}
		});
		
		this.play.setOnAction(_ -> App.crt_dlg("game_dialog", new GameDialogController()));
	}
	
	private void select_new_match() {
		Match match = ObjectAccessController.getNewCreationMatch();
		if(match != null) {
			try {
				int status_code = HttpConnection.waiting_request(match);
				if(status_code == 200)
					match.setStatus("2");
				else if(status_code == 401)
					throw new Exception(this.resources.getString(UNAUTHORIZED_USER_ERROR));
			} catch (Exception error) {
				error.printStackTrace();
				App.crt_dlg("error_dialog", new GenericDialogController(error.getMessage()));
			}
		}
	}
	
	@Override
	protected void updateItem(Match match, boolean empty) {
		super.updateItem(match, empty);
		
		try {
			if(match == null || empty) {
				if(this.player_1 != null)
					this.player_1.textProperty().unbind();
				if(this.player_2 != null)
					this.player_2.textProperty().unbind();
				if(this.status != null)
					this.status.textProperty().unbind();
				if(this.seed != null)
					this.seed.textProperty().unbind();
				if(this.statusPropertyListener != null)
					this.status.textProperty().removeListener(this.statusPropertyListener);
				if(this.cellPropertyListener != null)
					super.selectedProperty().removeListener(this.cellPropertyListener);
				
				super.setGraphic(null);
			} else if(super.getGraphic() == null) {
				super.setGraphic(App.load_cell("game_list_cell", this));
				
				this.match_id.setText(Integer.toString(match.getMatch_id()));
				this.player_1.textProperty().bind(match.getPlayer_1Property());
				this.player_2.textProperty().bind(match.getPlayer_2Property());
				this.status.textProperty().bind(match.getLiteralStatusProperty());
				this.seed.textProperty().bind(match.getSeedProperty());
				
				if(match.getPlayer_1().compareTo(User.get_usr_inst().getUsername()) == 0)
					match.setTurn(true);
			}
		} catch(IOException error) {
			error.printStackTrace();
			super.setGraphic(null);
			App.crt_dlg("error_dialog", new GenericDialogController(error.getMessage()));
		}
	}
}
