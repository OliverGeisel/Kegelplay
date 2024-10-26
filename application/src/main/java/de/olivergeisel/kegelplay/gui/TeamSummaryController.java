package de.olivergeisel.kegelplay.gui;

import core.game.Game120;
import core.point_system.GamePointsPlayer;
import core.team_and_player.Team;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TeamSummaryController implements Initializable {

	@FXML
	private Pane  root;
	@FXML
	private Label totalPins;
	@FXML
	private Label totalTeamPoints;
	@FXML
	private Label totalSetPoints;
	@FXML
	private Label teamName;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		root.getProperties().put(FXMLLoader.CONTROLLER_KEYWORD, this);
	}

	public void update(Team<Game120> team, List<GamePointsPlayer<Game120>> points, double scorePoints) {
		this.totalPins.setText(String.valueOf(team.getTeamScore()));
		var sum = points.stream().mapToDouble(GamePointsPlayer::getTeamPoints).sum();
		this.totalTeamPoints.setText(String.valueOf(sum + scorePoints));
		this.totalSetPoints.setText(
				String.valueOf(points.stream().mapToDouble(GamePointsPlayer::getSumGameSetPoints).sum()));
		teamName.setText(team.getName());
	}
}
