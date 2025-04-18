package com.maven.trismaster.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import com.maven.trismaster.App;
import com.maven.trismaster.connection.HttpConnection;
import com.maven.trismaster.entity.Match;
import com.maven.trismaster.entity.Match.GameValueTable;
import com.maven.trismaster.entity.Step;
import com.maven.trismaster.entity.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class GamePanelController implements Initializable {
	@FXML ListView<Match> container;
	@FXML VBox sq1, sq2, sq3, sq4, sq5, sq6, sq7, sq8, sq9;
	
	private Circle circle = new Circle(25.0);
	private Line line1 = new Line(50.0, 50.0, 50.0 + 17.68, 50.0 + 17.68);
	private Line line2 = new Line(50.0 + 17.68, 50.0, 50.0, 50.0 + 17.68);
	
	private final String UNAUTHORIZED_USER_ERROR = "user.error";
	private ResourceBundle resources = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.resources = resources;
		
		this.container.setItems(ObjectAccessController.getMatches());
		this.container.setCellFactory(_ -> new GameListCellController());
		this.container.getSelectionModel().selectedItemProperty().addListener((_, _, selectedItem) -> {
			if(selectedItem != null && selectedItem.getStatus().compareTo("1") == 0)
				this.enableGameTable();
			else
				this.disableGameTable();
		});
		
		this.disableGameTable();
		this.prepareGameTable();
		
		this.circle.setStyle("-fx-fill: #F4F4F4;-fx-stroke: #000000;-fx-stroke-width: 2;");
		this.line1.setStyle("-fx-stroke: #000000;-fx-stroke-width: 2;");
		this.line2.setStyle("-fx-stroke: #000000;-fx-stroke-width: 2;");
	}
	
	private void enableGameTable() {
		Match match = ObjectAccessController.getCurrMatch();
		for(Step step : match.getSteps()) {
			if(step.getIndex() == 1) {
				if(step.getSeed() == 'O')
					this.sq1.getChildren().addAll(this.circle);
				else
					this.sq1.getChildren().addAll(this.line1, this.line2);
			} else if(step.getIndex() == 2) {
				if(step.getSeed() == 'O')
					this.sq2.getChildren().addAll(this.circle);
				else
					this.sq2.getChildren().addAll(this.line1, this.line2);
			} else if(step.getIndex() == 3) {
				if(step.getSeed() == 'O')
					this.sq3.getChildren().addAll(this.circle);
				else
					this.sq3.getChildren().addAll(this.line1, this.line2);
			} else if(step.getIndex() == 4) {
				if(step.getSeed() == 'O')
					this.sq4.getChildren().addAll(this.circle);
				else
					this.sq4.getChildren().addAll(this.line1, this.line2);
			} else if(step.getIndex() == 5) {
				if(step.getSeed() == 'O')
					this.sq5.getChildren().addAll(this.circle);
				else
					this.sq5.getChildren().addAll(this.line1, this.line2);
			} else if(step.getIndex() == 6) {
				if(step.getSeed() == 'O')
					this.sq6.getChildren().addAll(this.circle);
				else
					this.sq6.getChildren().addAll(this.line1, this.line2);
			} else if(step.getIndex() == 7) {
				if(step.getSeed() == 'O')
					this.sq7.getChildren().addAll(this.circle);
				else
					this.sq7.getChildren().addAll(this.line1, this.line2);
			} else if(step.getIndex() == 8) {
				if(step.getSeed() == 'O')
					this.sq8.getChildren().addAll(this.circle);
				else
					this.sq8.getChildren().addAll(this.line1, this.line2);
			} else if(step.getIndex() == 9) {
				if(step.getSeed() == 'O')
					this.sq9.getChildren().addAll(this.circle);
				else
					this.sq9.getChildren().addAll(this.line1, this.line2);
			}
		}
		
		if(match.getSteps().size() % 2 == 0 && User.get_usr_inst().getUsername().compareTo(match.getPlayer_1()) == 0) {
			this.checkEnable();
		} else if(match.getSteps().size() % 2 != 0 && User.get_usr_inst().getUsername().compareTo(match.getPlayer_2()) == 0) {
			this.checkEnable();
		}
	}
	
	private void checkEnable() {
		if(this.sq1.getChildren().size() == 0)
			this.sq1.setDisable(false);
		if(this.sq2.getChildren().size() == 0)
			this.sq2.setDisable(false);
		if(this.sq3.getChildren().size() == 0)
			this.sq3.setDisable(false);
		if(this.sq4.getChildren().size() == 0)
			this.sq4.setDisable(false);
		if(this.sq5.getChildren().size() == 0)
			this.sq5.setDisable(false);
		if(this.sq6.getChildren().size() == 0)
			this.sq6.setDisable(false);
		if(this.sq7.getChildren().size() == 0)
			this.sq7.setDisable(false);
		if(this.sq8.getChildren().size() == 0)
			this.sq8.setDisable(false);
		if(this.sq9.getChildren().size() == 0)
			this.sq9.setDisable(false);
	}
	
	private void calculate() {
		Match match = ObjectAccessController.getCurrMatch();
		GameValueTable game = new GameValueTable(new ArrayList<Step>(match.getSteps()));
		if(game.isWinner()) {
			if(match.getPlayer_1().compareTo(User.get_usr_inst().getUsername()) == 0)
				match.setResult("1");
			else
				match.setResult("2");
			match.setStatus("0");
		} else if(match.getSteps().size() == 9) {
			match.setResult("0");
			match.setStatus("0");
		}
	}
	
	private void prepareGameTable() {
		this.sq1.setOnMouseClicked(_ -> {
			Match match = ObjectAccessController.getCurrMatch();
			try {
				if(match.getSeed().compareTo("O") == 0)
					this.sq1.getChildren().addAll(this.circle);
				else
					this.sq1.getChildren().addAll(this.line1, this.line2);
				Step step = new Step(1, match.getSeed().charAt(0));
				this.doStepRequest(step);
				this.disableGameTable();
				this.calculate();
			} catch (Exception error) {
				error.printStackTrace();
				this.sq1.getChildren().clear();
				App.crt_dlg("error_dialog", new GenericDialogController(error.getMessage()));
			}
		});
		
		this.sq2.setOnMouseClicked(_ -> {
			Match match = ObjectAccessController.getCurrMatch();
			try {
				if(match.getSeed().compareTo("O") == 0)
					this.sq2.getChildren().addAll(this.circle);
				else
					this.sq2.getChildren().addAll(this.line1, this.line2);
				Step step = new Step(2, match.getSeed().charAt(0));
				this.doStepRequest(step);
				this.disableGameTable();
				this.calculate();
			} catch (Exception error) {
				error.printStackTrace();
				this.sq2.getChildren().clear();
				App.crt_dlg("error_dialog", new GenericDialogController(error.getMessage()));
			}
		});
		
		this.sq3.setOnMouseClicked(_ -> {
			Match match = ObjectAccessController.getCurrMatch();
			try {
				if(match.getSeed().compareTo("O") == 0)
					this.sq3.getChildren().addAll(this.circle);
				else
					this.sq3.getChildren().addAll(this.line1, this.line2);
				Step step = new Step(3, match.getSeed().charAt(0));
				this.doStepRequest(step);
				this.disableGameTable();
				this.calculate();
			} catch (Exception error) {
				error.printStackTrace();
				this.sq3.getChildren().clear();
				App.crt_dlg("error_dialog", new GenericDialogController(error.getMessage()));
			}
		});
		
		this.sq4.setOnMouseClicked(_ -> {
			Match match = ObjectAccessController.getCurrMatch();
			try {
				if(match.getSeed().compareTo("O") == 0)
					this.sq4.getChildren().addAll(this.circle);
				else
					this.sq4.getChildren().addAll(this.line1, this.line2);
				Step step = new Step(4, match.getSeed().charAt(0));
				this.doStepRequest(step);
				this.disableGameTable();
				this.calculate();
			} catch (Exception error) {
				error.printStackTrace();
				this.sq4.getChildren().clear();
				App.crt_dlg("error_dialog", new GenericDialogController(error.getMessage()));
			}
		});
		
		this.sq5.setOnMouseClicked(_ -> {
			Match match = ObjectAccessController.getCurrMatch();
			try {
				if(match.getSeed().compareTo("O") == 0)
					this.sq5.getChildren().addAll(this.circle);
				else
					this.sq5.getChildren().addAll(this.line1, this.line2);
				Step step = new Step(5, match.getSeed().charAt(0));
				this.doStepRequest(step);
				this.disableGameTable();
				this.calculate();
			} catch (Exception error) {
				error.printStackTrace();
				this.sq5.getChildren().clear();
				App.crt_dlg("error_dialog", new GenericDialogController(error.getMessage()));
			}
		});
		
		this.sq6.setOnMouseClicked(_ -> {
			Match match = ObjectAccessController.getCurrMatch();
			try {
				if(match.getSeed().compareTo("O") == 0)
					this.sq6.getChildren().addAll(this.circle);
				else
					this.sq6.getChildren().addAll(this.line1, this.line2);
				Step step = new Step(6, match.getSeed().charAt(0));
				this.doStepRequest(step);
				this.disableGameTable();
				this.calculate();
			} catch (Exception error) {
				error.printStackTrace();
				this.sq6.getChildren().clear();
				App.crt_dlg("error_dialog", new GenericDialogController(error.getMessage()));
			}
		});
		
		this.sq7.setOnMouseClicked(_ -> {
			Match match = ObjectAccessController.getCurrMatch();
			try {
				if(match.getSeed().compareTo("O") == 0)
					this.sq7.getChildren().addAll(this.circle);
				else
					this.sq7.getChildren().addAll(this.line1, this.line2);
				Step step = new Step(7, match.getSeed().charAt(0));
				this.doStepRequest(step);
				this.disableGameTable();
				this.calculate();
			} catch (Exception error) {
				error.printStackTrace();
				this.sq7.getChildren().clear();
				App.crt_dlg("error_dialog", new GenericDialogController(error.getMessage()));
			}
		});
		
		this.sq8.setOnMouseClicked(_ -> {
			Match match = ObjectAccessController.getCurrMatch();
			try {
				if(match.getSeed().compareTo("O") == 0)
					this.sq8.getChildren().addAll(this.circle);
				else
					this.sq8.getChildren().addAll(this.line1, this.line2);
				Step step = new Step(8, match.getSeed().charAt(0));
				this.doStepRequest(step);
				this.disableGameTable();
				this.calculate();
			} catch (Exception error) {
				error.printStackTrace();
				this.sq8.getChildren().clear();
				App.crt_dlg("error_dialog", new GenericDialogController(error.getMessage()));
			}
		});
		
		this.sq9.setOnMouseClicked(_ -> {
			Match match = ObjectAccessController.getCurrMatch();
			try {
				if(match.getSeed().compareTo("O") == 0)
					this.sq9.getChildren().addAll(this.circle);
				else
					this.sq9.getChildren().addAll(this.line1, this.line2);
				Step step = new Step(9, match.getSeed().charAt(0));
				this.doStepRequest(step);
				this.disableGameTable();
				this.calculate();
			} catch (Exception error) {
				error.printStackTrace();
				this.sq9.getChildren().clear();
				App.crt_dlg("error_dialog", new GenericDialogController(error.getMessage()));
			}
		});
	}
	
	private void doStepRequest(Step step) throws Exception {
		Match match = ObjectAccessController.getCurrMatch();
		int status_code = HttpConnection.step_request(match.getMatch_id(), step);
		if(status_code == 401)
			throw new Exception(resources.getString(UNAUTHORIZED_USER_ERROR));
	}
	
	private void disableGameTable() {
		this.sq1.setDisable(true);
		this.sq1.getChildren().clear();
		this.sq2.setDisable(true);
		this.sq2.getChildren().clear();
		this.sq3.setDisable(true);
		this.sq3.getChildren().clear();
		this.sq4.setDisable(true);
		this.sq4.getChildren().clear();
		this.sq5.setDisable(true);
		this.sq5.getChildren().clear();
		this.sq6.setDisable(true);
		this.sq6.getChildren().clear();
		this.sq7.setDisable(true);
		this.sq7.getChildren().clear();
		this.sq8.setDisable(true);
		this.sq8.getChildren().clear();
		this.sq9.setDisable(true);
		this.sq9.getChildren().clear();
	}

}
