package de.olivergeisel.kegelplay.gui;


import core.team_and_player.Player;
import core.team_and_player.Team;
import de.kegelplay.infrastructure.ini.IniFile;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class TeamOrderController implements Initializable {

	@FXML
	private VBox root;

	/**
	 * Called to initialize a controller after its root element has been
	 * completely processed.
	 *
	 * @param location  The location used to resolve relative paths for the root object, or
	 *                  {@code null} if the location is not known.
	 * @param resources The resources used to localize the root object, or {@code null} if
	 *                  the root object was not localized.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		root.getProperties().put(FXMLLoader.CONTROLLER_KEYWORD, this);
	}

	public void update(Team team) {
		IniFile iniFile = null;
		try {

			iniFile = new IniFile("display-infos/additional-info.ini");
		} catch (IOException e) {
			return;
		}

		root.getChildren().clear();
		var teamLabel = new Label(team.getName());
		teamLabel.fontProperty().set(new Font(30));
		root.getChildren().add(teamLabel);
		IniFile finalIniFile = iniFile;
		var mapping = Arrays.stream(team.getPlayers()).map(player -> {
			var game = player.getGame();
			int vorlauf;
			try {
				vorlauf = Integer.parseInt(finalIniFile.getRegion("Vorlauf").getValue(player.getCompleteName()));
			} catch (Exception e) {
				vorlauf = 0;
			}
			return new PlayerScore(player, game.getTotalScore() + vorlauf);
		});
		var sorted = mapping.sorted((a, b) -> b.score() - a.score()).toList();
		for (var player : sorted) {
			var playerLine = new HBox();
			var playerName = new Label(STR."\{player.player().getVorname()} \{player.player().getNachname()}");
			playerName.paddingProperty().set(new Insets(0, 10, 0, 0));
			playerName.fontProperty().set(new Font(20));
			playerName.prefWidthProperty().set(200);
			var score = new Label(STR."\{player.score()}");
			score.paddingProperty().set(new Insets(0, 0, 0, 10));
			score.prefWidthProperty().set(100);
			score.fontProperty().set(new Font(20));
			var verein = new Label(STR."\{player.player().getClub()}");
			verein.paddingProperty().set(new Insets(0, 10, 0, 0));
			verein.fontProperty().set(new Font(20));
			verein.prefWidthProperty().set(200);
			playerLine.getChildren().addAll(playerName, verein, score);
			root.getChildren().add(playerLine);
		}
	}

	private record PlayerScore(Player player, int score) {
	}
}
