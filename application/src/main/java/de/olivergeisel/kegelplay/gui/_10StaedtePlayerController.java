package de.olivergeisel.kegelplay.gui;

import core.match.Match;
import core.team_and_player.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class _10StaedtePlayerController implements Initializable {

	@FXML
	private HBox root;

	@FXML
	private Label name;

	@FXML
	private Label throwCount, throwCount2;

	@FXML
	private Label volle, volle2;

	@FXML
	private Label raeumer, raeumer2;

	@FXML
	private Label sum, sum2, total;

	public void update(Player player, Match match) {
		root.setStyle("-fx-border-radius: 1; -fx-border-width: 2; -fx-border-color: #000000;");
		if (match.getCurrentPlayers().contains(player))
			root.setStyle("-fx-background-color: rgba(255,221,0,0.8)");
		name.setText(player.getCompleteName());
		var game = player.getGame();
		var set1 = game.getGameSet(0);
		var set2 = game.getGameSet(1);
		throwCount.setText(String.valueOf(set1.getAnzahlGespielteWuerfe()));
		volle.setText(String.valueOf(set1.getVolleScore()));
		raeumer.setText(String.valueOf(set1.getAbraeumenScore()));
		sum.setText(String.valueOf(set1.getScore()));
		throwCount2.setText(String.valueOf(set2.getAnzahlGespielteWuerfe()));
		volle2.setText(String.valueOf(set2.getVolleScore()));
		raeumer2.setText(String.valueOf(set2.getAbraeumenScore()));
		sum2.setText(String.valueOf(set2.getScore()));
		total.setText(String.valueOf(game.getTotalScore()));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		root.getProperties().put(FXMLLoader.CONTROLLER_KEYWORD, this);
	}
}
