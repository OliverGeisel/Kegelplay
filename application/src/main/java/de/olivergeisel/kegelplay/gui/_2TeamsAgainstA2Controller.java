package de.olivergeisel.kegelplay.gui;


import core.game.Game;
import core.game.Game120;
import core.match.Match;
import core.match.Match2Teams;
import core.point_system.ScoreEvaluator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TimerTask;

import static core.point_system.Evaluate2TeamsPair.evaluateMatch;

public class _2TeamsAgainstA2Controller<G extends Game> extends DisplayGameController<G> {

	private static final System.Logger LOGGER = System.getLogger(_2TeamsAgainstA2Controller.class.getName());

	@FXML
	Pane root;
	@FXML
	private HBox  playerBoxes;
	@FXML
	private Pane  pair1;
	@FXML
	private Pane  pair2;
	@FXML
	private Label difference;
	@FXML
	private Pane  round1;
	@FXML
	private Pane  round2;
	@FXML
	private Pane  round3;
	@FXML
	private Pane  sumHome;
	@FXML
	private Pane  sumGuest;
	@FXML
	private VBox  rounds;

	public _2TeamsAgainstA2Controller(Match<G> match) {
		super(match);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		updateViewAfterLoad();
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
			playerBoxes.setPrefHeight(newValue.doubleValue() * 0.5);
		});
	}


	@Override
	public void updateViewAfterLoad() throws IllegalStateException, UnsupportedOperationException {
		var match = getMatch();
		var numPlayers = match.getTeams()[0].getNumberOfPlayers();
		if (numPlayers == 0 || numPlayers % 2 != 0 || numPlayers > 6) {
			throw new IllegalStateException("Number of players must be even and greater than 0 and less than 7");
		}
		// remove the unnecessary player boxes/rounds
		while (numPlayers < rounds.getChildren().size() * 2) {
			rounds.getChildren().removeLast();
		}
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
		var _2TeamsMatch = match instanceof Match2Teams ? (Match2Teams<Game120>) match : null;
		if (_2TeamsMatch == null) {
			throw new IllegalArgumentException("Match must be a 2 teams match");
		}
		setPlayerPairs(_2TeamsMatch);
		setRounds(_2TeamsMatch);
		setTeamOverview(_2TeamsMatch);
		difference.getScene().getWindow().setOnCloseRequest(event -> {
			getTimer().cancel();
		});
		if (match.getStatusInfo().isFinished()) {
			LOGGER.log(System.Logger.Level.INFO, STR."\{match.getName()} is finished");
			getTimer().cancel();
		}
	}

	private void setRounds(Match2Teams<Game120> match) {

		final var rounds = List.of(round1, round2, round3);
		for (int i = 0; i < match.getTeams()[0].getNumberOfPlayers(); i += 2) {
			var controller = getControllerFromPane(rounds.get(i / 2));
			if (controller instanceof RoundController roundController) {
				roundController.update(match, i);
			}
		}
	}

	private void setTeamOverview(Match2Teams<Game120> _2TeamsMatch) {
		var team1 = _2TeamsMatch.getHome();
		var team2 = _2TeamsMatch.getGuest();
		var diff = team1.getTeamScore() - team2.getTeamScore();
		difference.setText(Integer.toString(diff));
		var controller = getControllerFromPane(sumHome);
		var points = evaluateMatch(_2TeamsMatch);
		var scorePoints = ScoreEvaluator.evaluate(_2TeamsMatch);
		if (controller instanceof TeamSummaryController teamSummaryController) {
			teamSummaryController.update(team1, points.getFirst(), scorePoints.getFirst());
		}
		controller = getControllerFromPane(sumGuest);
		if (controller instanceof TeamSummaryController teamSummaryController) {
			teamSummaryController.update(team2, points.getSecond(), scorePoints.getSecond());
		}
		var classes = difference.getStyleClass();
		classes.clear();
		if (diff > 0) {
			classes.add("positive");
		} else if (diff < 0) {
			classes.add("negative");
		} else {
			classes.add("neutral");
		}
	}
	//endregion

}
