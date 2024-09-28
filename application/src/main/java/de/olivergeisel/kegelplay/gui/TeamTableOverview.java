package de.olivergeisel.kegelplay.gui;

import core.game.Game120;
import core.match.Match2Teams;
import core.point_system.GamePointsPlayer;
import core.point_system._2Teams120PointSystem;
import core.point_system._2TeamsMatchPoints;
import core.util.Pair;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class TeamTableOverview implements Initializable {

	@FXML
	private Pane  root;
	@FXML
	private Pane  team1;
	@FXML
	private Pane  team2;
	@FXML
	private Label team1Name;
	@FXML
	private Label team2Name;
	@FXML
	private Label sum1;
	@FXML
	private Label sum2;
	@FXML
	private Label diff;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		root.getProperties().put(FXMLLoader.CONTROLLER_KEYWORD, this);
	}

	public void update(Match2Teams<Game120> match) {
		team1Name.setText(match.getHome().getName());
		team2Name.setText(match.getGuest().getName());
		var playerPoints = evaluateMatch(match);
		var team1Controller = ((TeamTableTeamOverview) team1.getProperties().get(FXMLLoader.CONTROLLER_KEYWORD));
		var team2Controller = ((TeamTableTeamOverview) team2.getProperties().get(FXMLLoader.CONTROLLER_KEYWORD));
		var pointSystem = (_2Teams120PointSystem) match.getPointSystem();
		var points = (_2TeamsMatchPoints<Game120>) pointSystem.getMatchPoints(match);
		var team1Points = points.totalPointsTeam1();
		var team2Points = points.totalPointsTeam2();
		team1Controller.update(match.getHome(), playerPoints.getFirst(), team1Points);
		team2Controller.update(match.getGuest(), playerPoints.getSecond(), team2Points);
		var sum1 = match.getHome().getTeamScore();
		var sum2 = match.getGuest().getTeamScore();
		this.sum1.setText(String.valueOf(sum1));
		this.sum2.setText(String.valueOf(sum2));
		var diff = sum1 - sum2;
		if (diff > 0) {
			this.diff.setStyle("-fx-background-color: green");
		} else if (diff < 0) {
			this.diff.setStyle("-fx-background-color: red");
		} else {
			this.diff.setStyle("-fx-background-color: yellow");
		}
		this.diff.setText(String.valueOf(sum1 - sum2));

	}

	private Pair<List<GamePointsPlayer<Game120>>, List<GamePointsPlayer<Game120>>> evaluateMatch(
			Match2Teams<Game120> match) {
		var home = match.getHome();
		var guest = match.getGuest();
		var homePoints = new LinkedList<GamePointsPlayer<Game120>>();
		var guestPoints = new LinkedList<GamePointsPlayer<Game120>>();
		for (int i = 0; i < home.getPlayers().length; i++) {
			var pair = core.point_system.GamePointsEvaluator_2Teams.evaluate(home.getPlayers()[i],
					guest.getPlayers()[i]);
			homePoints.add(pair.getFirst());
			guestPoints.add(pair.getSecond());
		}
		return new Pair<>(homePoints, guestPoints);
	}
}
