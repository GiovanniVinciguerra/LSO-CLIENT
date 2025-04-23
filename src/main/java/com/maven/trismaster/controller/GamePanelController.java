package com.maven.trismaster.controller;

import java.net.URL;
import java.util.ResourceBundle;
import com.maven.trismaster.entity.Match;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

public class GamePanelController implements Initializable {
	@FXML ListView<Match> container;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.container.setItems(ObjectAccessController.getMatches());
		this.container.setCellFactory(_ -> new GameListCellController());
	}
}
