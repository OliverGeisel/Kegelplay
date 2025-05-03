package de.olivergeisel.kegelplay.gui;

import core.game.Game;
import core.match.Match;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.TimerTask;

public class _10StaedteController<G extends Game> extends DisplayGameController<G> {

	private static final System.Logger LOGGER = System.getLogger(_10StaedteController.class.getName());


	@FXML VBox root;
	@FXML HBox row1;
	@FXML HBox row2;
	@FXML HBox row3;

	@FXML VBox team1, team2,
			team3, team4,
			team5, team6,
			team7, team8,
			team9, team10;
	@FXML VBox summary;

	public _10StaedteController(Match<G> match) {
		super(match);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		getTimer().schedule(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(() -> {
					// update
					var start = LocalDateTime.now();
					LOGGER.log(System.Logger.Level.INFO, STR."Update start: \{start}");
					getMatchUpdater().updateMatch();
					update(getMatch());

					var end = LocalDateTime.now();
					var difference = Duration.between(start, end);
					LOGGER.log(System.Logger.Level.INFO, STR."Update complete: Duration: \{difference.toMillis()} ms");
				});
			}
		}, 2_000, REFRESH_INTERVAL);
	}

	@FXML
	private void onClose() {
		getTimer().cancel();
	}

	private void update(Match match) {
		var teams = match.getTeams();
		team1.getScene().getWindow().setOnCloseRequest(event -> {
			getTimer().cancel();
		});
		var controller = (_10StaedteTeamController) team1.getProperties().get(FXMLLoader.CONTROLLER_KEYWORD);
		controller.update(teams[0], match);
		controller = (_10StaedteTeamController) team2.getProperties().get(FXMLLoader.CONTROLLER_KEYWORD);
		controller.update(teams[1], match);
		controller = (_10StaedteTeamController) team3.getProperties().get(FXMLLoader.CONTROLLER_KEYWORD);
		controller.update(teams[2], match);
		controller = (_10StaedteTeamController) team4.getProperties().get(FXMLLoader.CONTROLLER_KEYWORD);
		controller.update(teams[3], match);
		controller = (_10StaedteTeamController) team5.getProperties().get(FXMLLoader.CONTROLLER_KEYWORD);
		controller.update(teams[4], match);
		controller = (_10StaedteTeamController) team6.getProperties().get(FXMLLoader.CONTROLLER_KEYWORD);
		controller.update(teams[5], match);
		controller = (_10StaedteTeamController) team7.getProperties().get(FXMLLoader.CONTROLLER_KEYWORD);
		controller.update(teams[6], match);
		controller = (_10StaedteTeamController) team8.getProperties().get(FXMLLoader.CONTROLLER_KEYWORD);
		controller.update(teams[7], match);
		controller = (_10StaedteTeamController) team9.getProperties().get(FXMLLoader.CONTROLLER_KEYWORD);
		controller.update(teams[8], match);
		controller = (_10StaedteTeamController) team10.getProperties().get(FXMLLoader.CONTROLLER_KEYWORD);
		controller.update(teams[9], match);
		var sumController = (_10StaedteSummaryController) summary.getProperties().get(FXMLLoader.CONTROLLER_KEYWORD);
		sumController.update(match.getTeams(), match.getCurrentPlayers());
	}


}
