package org.javafx.trismasterfx.controller;

import java.net.URL;
import java.util.ResourceBundle;
import org.javafx.trismasterfx.entity.Match;
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
