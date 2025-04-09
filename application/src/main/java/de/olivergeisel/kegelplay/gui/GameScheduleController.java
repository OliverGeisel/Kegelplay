package de.olivergeisel.kegelplay.gui;

import core.game.Game120;
import core.match.Match;
import core.team_and_player.Player;
import core.util.MatchPlayerHelper;
import de.kegelplay.infrastructure.ini.IniFile;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * A Schedule for games that comes up next.
 * <p>
 * It's a list (table) of player when they play next and what lane they will start.
 * </p>
 *
 * @author Oliver Geisel
 * @version 1.0.0
 * @since 1.0.0
 */
public class GameScheduleController implements Initializable {

	private static final System.Logger LOGGER = System.getLogger(GameScheduleController.class.getName());

	@FXML
	private VBox gameSchedule;
	@FXML
	private VBox root;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		root.getProperties().put(FXMLLoader.CONTROLLER_KEYWORD, this);
		gameSchedule.getProperties().put(FXMLLoader.CONTROLLER_KEYWORD, this);
	}


	public void update(Match match, int gameRound) {
		final var playerPerGameSet = 4;
		// get the players to play
		List<Player> orderedPlayers = MatchPlayerHelper.<Game120>getPlayersOrderedByStartPosition(match);
		var remainingPlayers = orderedPlayers.stream().skip((long) gameRound * playerPerGameSet).toList();
		// load times from ini
		var times = new LinkedList<String>();
		try {
			var iniFile = new IniFile("display-infos/additional-info.ini");
			var gameTimes = iniFile.getRegion("Zeiten");
			var timeSlots = gameTimes.getKeys().stream().filter(it -> it.matches("Durchgangszeit\\d+")).count();
			for (int j = 1; j <= timeSlots; j++) {
				times.add(gameTimes.getValue(STR."Durchgangszeit\{j}"));
			}
		} catch (IOException e) {
			LOGGER.log(System.Logger.Level.ERROR, "could not read ini file", e);
			return;
		}
		gameSchedule.getChildren().clear();
		outer:
		for (int i = 0; i < times.size(); i++) {
			// create new line
			var line = new HBox();
			var time = times.get(i + gameRound);
			var children = line.getChildren();
			line.setAlignment(Pos.CENTER);
			var timeLabel = new Label(STR."\{time} Uhr");
			timeLabel.paddingProperty().setValue(new Insets(0, 10, 0, 10));
			timeLabel.minWidthProperty().setValue(100);
			timeLabel.setFont(new Font(14));
			children.add(timeLabel);
			// get next 4 players
			for (int k = 0; k < playerPerGameSet; k++) {
				try {
					var player = remainingPlayers.get(k + i * playerPerGameSet);
					var playerLabel = new Label(STR."\{player.getVorname()} \{player.getNachname()}");
					playerLabel.paddingProperty().setValue(new Insets(0, 10, 0, 10));
					playerLabel.minWidthProperty().setValue(150);
					playerLabel.setFont(new Font(14));
					children.add(playerLabel);
				} catch (IndexOutOfBoundsException e) {
					break outer;
				}
			}
			gameSchedule.getChildren().add(line);
		}

	}
}
