package de.olivergeisel.kegelplay.gui;

import core.game.Game120;
import core.match.Match;
import core.team_and_player.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

import static de.olivergeisel.kegelplay.gui.DisplayGameController.getControllerFromPane;

public class PlayerPairController implements Initializable {

	@FXML
	private Pane root;
	@FXML
	private Pane lane1;
	@FXML
	private Pane lane2;
	@FXML
	private Pane header1;
	@FXML
	private Pane header2;
	@FXML
	private Pane table1;
	@FXML
	private Pane table2;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		root.getProperties().put(FXMLLoader.CONTROLLER_KEYWORD, this);
		root.heightProperty().addListener((observableValue, oldHeight, newHeight) -> {
			double subFrameHeight = newHeight.doubleValue() * .3;
			header1.setPrefHeight(subFrameHeight);
			header2.setPrefHeight(subFrameHeight);
			table1.setPrefHeight(newHeight.doubleValue() * .4);
			table2.setPrefHeight(newHeight.doubleValue() * .4);
		});
	}

	public void update(Match<Game120> match, int firstLane) {
		setLane(firstLane, match, lane1);
		setLane(firstLane + 1, match, lane2);
	}

	private void setLane(int laneNumber, Match<Game120> match, Pane lane) {
		var lanes = match.getConfig().getLaneNames();
		var bahn = (Label) lane.getChildren().getFirst();
		bahn.setText(lanes.get(laneNumber));
		bahn.setText(lanes.get(laneNumber));
		var header = (GridPane) lane.getChildren().get(1);
		var headerController = (HeaderController) getControllerFromPane(header);
		Player<Game120> player = match.getCurrentPlayers().get(laneNumber);
		if (!player.equals(headerController.getPlayer())) {
			headerController.setPlayer(player);
		}
		var tabelle = lane.getChildren().get(2);
		var tabellenController = (TableController) getControllerFromPane((Pane) tabelle);
		tabellenController.setGame(player.getGame(), match);
	}

	//region setter/getter
	public Pane getHeader1() {
		return header1;
	}

	public Pane getHeader2() {
		return header2;
	}

	public Pane getTable1() {
		return table1;
	}

	public Pane getTable2() {
		return table2;
	}
//endregion
}
