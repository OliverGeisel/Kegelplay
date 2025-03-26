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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.stream.Stream;

/**
 * Controller für die Anzeige der Reihenfolge der Teams. Er zeigt die Spieler der Teams in absteigender Reihenfolge ihrer Punkte an.
 * <p>
 * Die View die dazu gehört ist <b>team-order.fxml</b>
 *
 * @author Oliver Geisel
 * @version 1.0.0
 * @since 1.0.0
 */
public class TeamOrderController implements Initializable {

	private static final Log log = LogFactory.getLog(TeamOrderController.class);
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

	private static Stream<PlayerScore> getPlayerScoreStream(Team team, IniFile iniFile) {
		// Sort the players by their score (current score + vorlauf)
		var mapping = Arrays.stream(team.getPlayers()).map(player -> {
			var game = player.getGame();
			int vorlauf;
			try {
				vorlauf = Integer.parseInt(iniFile.getRegion("Vorlauf").getValue(player.getCompleteName()));
			} catch (Exception e) {
				vorlauf = 0;
			}
			return new PlayerScore(player, game.getTotalScore() + vorlauf);
		});
		return mapping.sorted((a, b) -> b.score() - a.score());
	}

	public void update(Team team) {
		IniFile iniFile;
		try {
			iniFile = new IniFile("display-infos/additional-info.ini");
		} catch (IOException e) {
			log.error("Could not read additional-info.ini. Please create 'display-infos/additional-info.ini'", e);
			return;
		}
		// Clear the root node
		root.getChildren().clear();
		var teamLabel = new Label(team.getName());
		teamLabel.fontProperty().set(new Font(30));
		root.getChildren().add(teamLabel);
		var sorted = getPlayerScoreStream(team, iniFile);
		sorted.forEach(player-> {
			var playerLine = new HBox();
			var playerName = new Label(player.player().getCompleteName());
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
		});
	}

	/**
	 * Pair of a player and his score.
	 * @param player The player.
	 * @param score The score.
	 */
	private record PlayerScore(Player player, int score) {
	}
}
