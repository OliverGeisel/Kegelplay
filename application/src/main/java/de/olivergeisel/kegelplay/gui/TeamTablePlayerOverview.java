package de.olivergeisel.kegelplay.gui;


import core.game.Game120;
import core.point_system.GamePointsPlayer;
import core.team_and_player.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class TeamTablePlayerOverview implements Initializable {

	@FXML
	private Pane  root;
	@FXML
	private Label name;
	@FXML
	private Label throwCount;
	@FXML
	private Label missThrowCount;

	@FXML
	private Label set1Score;
	@FXML
	private Label set1Points;
	@FXML
	private Label set2Score;
	@FXML
	private Label set2Points;
	@FXML
	private Label set3Score;
	@FXML
	private Label set3Points;
	@FXML
	private Label set4Score;
	@FXML
	private Label set4Points;


	@FXML
	private Label score;
	@FXML
	private Label pointSum;
	@FXML
	private Label teamPoints;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		root.getProperties().put(FXMLLoader.CONTROLLER_KEYWORD, this);
	}

	public void update(Player<Game120> player, GamePointsPlayer<Game120> gamePoints) {
		name.setText(player.getCompleteName());
		var game = player.getGame();
		throwCount.setText(String.valueOf(game.getNumberOfWurf()));
		missThrowCount.setText(String.valueOf(game.getTotalFehlwurf()));
		score.setText(String.valueOf(game.getTotalScore()));
		set1Score.setText(String.valueOf(game.getSets()[0].getScore()));
		set2Score.setText(String.valueOf(game.getSets()[1].getScore()));
		set3Score.setText(String.valueOf(game.getSets()[2].getScore()));
		set4Score.setText(String.valueOf(game.getSets()[3].getScore()));

		var points = gamePoints.getGameSetPoints();
		set1Points.setText(String.valueOf(points.get(0).getPoints()));
		set2Points.setText(String.valueOf(points.get(1).getPoints()));
		set3Points.setText(String.valueOf(points.get(2).getPoints()));
		set4Points.setText(String.valueOf(points.get(3).getPoints()));
		pointSum.setText(String.valueOf(gamePoints.getSumGameSetPoints()));
		teamPoints.setText(String.valueOf(gamePoints.getTeamPoints()));
		// color the points
	}
}
