package de.olivergeisel.kegelplay.gui;


import core.game.Game;
import core.game.Game120;
import core.match.Match;
import core.match.Match2Teams;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.TimerTask;

public class _2TeamsAgainstController<G extends Game> extends DisplayGameController<G> {

	private static final System.Logger LOGGER = System.getLogger(_2TeamsAgainstController.class.getName());

	@FXML
	Pane root;
	@FXML
	private VBox playerBoxes;
	@FXML
	private Pane table;
	@FXML
	private Pane pair1;
	@FXML
	private Pane pair2;

	public _2TeamsAgainstController(Match<G> match) {
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
					setByMatch((Match<Game120>) getMatch());

					var end = LocalDateTime.now();
					var difference = Duration.between(start, end);
					LOGGER.log(System.Logger.Level.INFO, STR."Update complete: Duration: \{difference.toMillis()} ms");
				});
			}
		}, 2_000, REFRESH_INTERVAL);
		// initialize the player boxes

		// relational binding
		root.heightProperty().addListener((observable, oldValue, newValue) -> {
			playerBoxes.setPrefHeight(newValue.doubleValue());
			table.setPrefHeight(newValue.doubleValue());
		});
	}

//region setter/getter

	/**
	 * Set the players for the lanes. The players must be in the correct order.
	 * The first player is for lane 1, the second for lane 2, and so on.
	 * This can be overridden in the subclass to change the order or the number of players
	 *
	 * @param match the current players on the lanes
	 */
	public void setPlayerPairs(Match<Game120> match) {
		var controller = getControllerFromPane(pair1);
		if (controller instanceof PlayerPairController playerPairController) {
			playerPairController.update(match, 0);
		}
		controller = getControllerFromPane(pair2);
		if (controller instanceof PlayerPairController playerPairController) {
			playerPairController.update(match, 2);
		}
	}

	private void setByMatch(Match<Game120> match) {
		setPlayerPairs(match);
		setTable(match);
		table.getScene().getWindow().setOnCloseRequest(event -> {
			getTimer().cancel();
		});
		if (match.getStatusInfo().isFinished()) {
			LOGGER.log(System.Logger.Level.INFO, STR."\{match.getName()} is finished");
			getTimer().cancel();
		}
	}

	private void setTable(Match<Game120> match) {
		var _2TeamsMatch = match instanceof Match2Teams ? (Match2Teams<Game120>) match : null;
		if (_2TeamsMatch == null) {
			throw new IllegalArgumentException("Match must be a 2 teams match");
		}
		var controller = getControllerFromPane(table);
		if (controller instanceof TeamTableOverview tableController) {
			tableController.update(_2TeamsMatch);
		}
	}
//endregion
}
