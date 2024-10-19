package de.olivergeisel.kegelplay.gui;

import core.game.Game120;
import core.point_system.GamePointsPlayer;
import core.team_and_player.Team;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TeamTableTeamOverview implements Initializable {

	@FXML
	private Pane  root;
	@FXML
	private VBox  players;
	@FXML
	private Label teamScore;
	@FXML
	private Label teamPoints;
	@FXML
	private Label gameSetPoints;
	@FXML
	private Label teamThrowCount;
	@FXML
	private Label teamMissThrowCount;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		root.getProperties().put(FXMLLoader.CONTROLLER_KEYWORD, this);
	}

	public void update(Team<Game120> team, List<GamePointsPlayer<Game120>> gamePoints, double teamPoints) {
		int i = 0;
		for (var player : players.getChildren()) {
			var controller = ((TeamTablePlayerOverview) player.getProperties().get(FXMLLoader.CONTROLLER_KEYWORD));
			controller.update(team.getPlayer(i), gamePoints.get(i));
			i++;
		}
		var gameSetpointsValue = gamePoints.stream().mapToDouble(it->it.getPoints()).sum();
		gameSetPoints.setText(String.valueOf(gameSetpointsValue));
		teamScore.setText(String.valueOf(team.getTeamScore()));
		this.teamPoints.setText(String.valueOf(teamPoints));
		this.teamThrowCount.setText(String.valueOf(team.getTeamTotalThrows()));
		this.teamMissThrowCount.setText(String.valueOf(team.getTeamTotalMissThrow()));
	}
}
