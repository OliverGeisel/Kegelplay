package de.olivergeisel.kegelplay.gui;

import core.match.Match;
import de.kegelplay.infrastructure.ini.IniFile;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
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


	public void update(Match match, int gameSet) {
		final var playerPerGameSet = 4;
		// get the players to play
		var allPlayers = match.getTeams()[0].getPlayers();
		var remainingPlayers = Arrays.stream(allPlayers).skip(gameSet * playerPerGameSet).toList();
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
		for (int i = gameSet; i < times.size(); i++) {
			// create new line
			var line = new HBox();
			var time = times.get(i+1);
			var children = line.getChildren();
			var timeLabel = new Label(STR."\{time} Uhr");
			timeLabel.paddingProperty().setValue(new Insets(0, 10, 0, 10));
			timeLabel.minWidthProperty().setValue(100);
			children.add(timeLabel);
			// get next 4 players
			for (int k = 0; k < playerPerGameSet; k++) {
				try {
					var player = remainingPlayers.get(k + i * playerPerGameSet);
					var playerLabel = new Label(STR."\{player.getVorname()} \{player.getNachname()}");
					playerLabel.paddingProperty().setValue(new Insets(0, 10, 0, 10));
					playerLabel.minWidthProperty().setValue(150);
					children.add(playerLabel);
				} catch (IndexOutOfBoundsException e) {
					break outer;
				}
			}
			gameSchedule.getChildren().add(line);
		}

	}
}
