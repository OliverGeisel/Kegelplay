package de.olivergeisel.kegelplay.gui;


import core.game.Game120;
import core.match.Match;
import core.match.Match1Team;
import core.point_system.Winner;
import core.team_and_player.Player;
import de.kegelplay.infrastructure.update.MatchUpdater;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Controller for the semi-final view.
 * <p>
 * in the SemiFinalController the current game is displayed and updated every few seconds. The
 * {@link #REFRESH_INTERVAL} is the time between the updates.
 * The Semi-final shows 4 lanes with 4 players. The lanes 1 and 2 plays against each other and the lanes 3 and 4 plays against each other.
 * </p>
 *
 * @author Oliver Geisel
 * @version 1.0.0
 * @see GameController
 * @since 1.0.0
 */
public class SemiFinalController implements GameController<Game120>, Initializable {


	private static final System.Logger LOGGER           = System.getLogger(SemiFinalController.class.getName());
	private static final long          REFRESH_INTERVAL = 5 * 1000; // 5 seconds

	private final Timer                 timer          = new Timer();
	private final Match<Game120>        match;
	private final MatchUpdater<Game120> matchUpdater;
	private       int                   currentGameSet = -1;

	@FXML
	private VBox semiFinalList;
	@FXML
	private VBox gameSchedule;
	@FXML
	private VBox lane1;
	@FXML
	private VBox lane2;
	@FXML
	private VBox lane3;
	@FXML
	private VBox lane4;


	public SemiFinalController(Match1Team<Game120> match) {
		this.match = match;
		matchUpdater = new MatchUpdater<>(match);
	}

	public void update() {
		// update current game
		updateCurrentGame(match);
		lane1.getScene().getWindow().setOnCloseRequest(event -> {
			timer.cancel();
		});
		// update finalist list
		updateFinalistList(match);
		// update game schedule
		updateGameSchedule(match);
	}


	private void updateCurrentGame(Match match) {
		// update current game
		List<Player> players = match.getCurrentPlayers();
		var lanes = List.of(lane1, lane2, lane3, lane4);
		for (int i = 0; i < players.size(); i++) {
			var lane = lanes.get(i);
			var player = players.get(i);
			setLane(player, lane, match);
		}
	}

	private void setLane(Player player, VBox lane, Match match) {
		var lanes = match.getConfig().getLaneNames();
		var parent = (Pane) lane.getParent();
		var index = parent.getChildren().indexOf(lane);
		var header = (GridPane) lane.getChildren().getFirst();
		var headerController = (HeaderController) header.getProperties().get(FXMLLoader.CONTROLLER_KEYWORD);
		if (!player.equals(headerController.getPlayer())) {
			headerController.setPlayer(player);
		}
		var tabelle = lane.getChildren().get(1);
		var tabellenController = (TableController) tabelle.getProperties().get(FXMLLoader.CONTROLLER_KEYWORD);
		tabellenController.setGame(player.getGame(), match);
	}

	private void updateFinalistList(Match match) {
		// update finalist list
		var gameSet = (match.getCurrentSet()) / 4;
		if (gameSet == currentGameSet) {
			return;
		}
		// get all current winners
		List<Winner> winners = match.getPointSystem().getWinner(match);
		// add all winners to the list
		var controller = (FinalistListController) semiFinalList.getProperties().get(FXMLLoader.CONTROLLER_KEYWORD);
		for (var winner : winners) {
			var nameSplit = winner.getName().split(" ");
			controller.addFinalist(nameSplit[0], nameSplit[1], winner.getScore());
		}
	}

	private void updateGameSchedule(Match match) {
		// update game schedule
		var gameSet = (match.getCurrentSet()) / 4;
		if (gameSet == currentGameSet) {
			return;
		}
		currentGameSet = gameSet;
		var controller = (GameScheduleController) gameSchedule.getProperties().get(FXMLLoader.CONTROLLER_KEYWORD);
		controller.update(match, gameSet);
	}


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
		int i = 1;
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(() -> {
					// update
					var start = LocalDateTime.now();
					LOGGER.log(System.Logger.Level.INFO, STR."Update start: \{start}");
					matchUpdater.updateMatch();
					update();
					var end = LocalDateTime.now();
					var difference = Duration.between(start, end);
					LOGGER.log(System.Logger.Level.INFO, STR."Update complete: Duration: \{difference.toMillis()} ms");
				});
			}
		}, 5_000, REFRESH_INTERVAL);
		for (var lane : List.of(lane1, lane2, lane3, lane4)) {
			final Pane finalTabelle = (Pane) lane.getChildren().get(0);
			final Pane finalHeader = (Pane) lane.getChildren().get(1);
			lane.heightProperty().addListener((observableValue, oldHeight, newHeight) -> {
				double subFrameHeight2 = newHeight.doubleValue() * 0.25;
				finalTabelle.setPrefHeight(subFrameHeight2);
				finalHeader.setPrefHeight(subFrameHeight2);
			});
		}
	}
}
