package de.olivergeisel.kegelplay.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.game.Game;
import core.match.Match;
import core.team_and_player.Player;
import de.kegelplay.infrastructure.data_reader.KeglerheimGeneralReader;
import de.kegelplay.infrastructure.update.MatchUpdater;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

/**
 * An abstract class for displaying the {@link Match}. Every implementation of this class should be able to display a
 * complete {@link Match} and all its subparts.
 *
 * <p>
 * The {@link DisplayGameController} is responsible for updating the {@link Match} and displaying it in a GUI.
 * The default refresh rate is 10 seconds, but can be changed in the settings.json with "refreshRate" set in milliseconds.
 * file.
 * </p>
 *
 * @param <G> the type of the {@link Game} that is played in the match
 * @author Oliver Geisel
 * @version 1.0.0
 * @see Match
 * @see Game
 * @since 1.0.0
 */
public abstract class DisplayGameController<G extends Game> implements GameController<G>, Initializable {

	private static final long          REFRESH_INTERVAL;
	private static final System.Logger LOGGER = System.getLogger(DisplayGameController.class.getName());

	static {
		var mapper = new ObjectMapper();
		var path = Path.of("./configs/settings.json");
		long selectedInterval = 10 * 1000; // 10 seconds
		try {
			var parsed = mapper.readTree(path.toFile());
			selectedInterval = parsed.get("refreshRate").asLong();
		} catch (Exception _) {
		}
		REFRESH_INTERVAL = selectedInterval;
	}

	private final MatchUpdater<G> matchUpdater;
	private final Match<G>        match;
	private final Timer           timer;
	private       KeglerheimGeneralReader dataReader;


	@FXML private Label showLabel;
	@FXML private VBox  lane1;
	@FXML private VBox  lane2;
	@FXML private VBox  lane3;
	@FXML private VBox  lane4;

	public DisplayGameController(Match<G> match) {
		this.match = match;
		matchUpdater = new MatchUpdater<>(match);
		timer = new Timer();
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
					var players = match.getCurrentPlayers();
					setPlayers(players);
					var end = LocalDateTime.now();
					var difference = Duration.between(start, end);
					LOGGER.log(System.Logger.Level.INFO, STR."Update complete: Duration: \{difference.toMillis()} ms");
				});
			}
		}, 0, REFRESH_INTERVAL);
		for (var lane : List.of(lane1, lane2, lane3, lane4)) {
			final Label bahnLabel = (Label) lane.getChildren().getFirst();
			bahnLabel.setText(STR."Bahn \{i++}");
			final Pane finalTabelle = (Pane) lane.getChildren().get(1);
			final Pane finalHeader = (Pane) lane.getChildren().get(2);
			final Pane finalFallbild = (Pane) lane.getChildren().get(3);
			lane.heightProperty().addListener((observableValue, oldHeight, newHeight) -> {
				double subFrameHeight2 = newHeight.doubleValue() * 0.25;
				finalTabelle.setPrefHeight(subFrameHeight2);
				finalHeader.setPrefHeight(subFrameHeight2);
				finalFallbild.setPrefHeight(newHeight.doubleValue() * .5);
			});
		}
	}

	@FXML
	private void onClose() {
		timer.cancel();
	}


	/**
	 * Set the players for the lanes. The players must be in the correct order.
	 *
	 * @param player the players to set
	 * @param lane   the lane to set the players to
	 */
	private void setLane(Player<G> player, VBox lane) {
		var lanes = match.getConfig().getLaneNames();
		var parent = (Pane) lane.getParent();
		var index = parent.getChildren().indexOf(lane);
		var bahn = (Label) lane.getChildren().getFirst();
		bahn.setText(lanes.get(index));
		bahn.setText(lanes.get(index));
		var header = (GridPane) lane.getChildren().get(1);
		var headerController = (HeaderController) header.getProperties().get(FXMLLoader.CONTROLLER_KEYWORD);
		if (!player.equals(headerController.getPlayer())) {
			headerController.setPlayer(player);
		}
		var tabelle = lane.getChildren().get(2);
		var tabellenController = (TableController) tabelle.getProperties().get(FXMLLoader.CONTROLLER_KEYWORD);
		tabellenController.setGame(player.getGame(), match);
		var fallbild = lane.getChildren().get(3);
		var fallbildController = (FallbildController) fallbild.getProperties().get(FXMLLoader.CONTROLLER_KEYWORD);
		fallbildController.setWurfbild(player.getGame().getLastWurf().bild());
	}

	@FXML
	protected void onCloseInMenu(ActionEvent actionEvent) {
		var oldScene = showLabel.getScene();
		var stage = (Stage) oldScene.getWindow();
		stage.close();
	}

	@FXML
	public void onAbout(ActionEvent actionEvent) {}

	/**
	 * Set the players for the lanes. The players must be in the correct order.
	 * The first player is for lane 1, the second for lane 2, and so on.
	 * This can be overridden in the subclass to change the order or the number of players
	 * @param players
	 */
	//region setter/getter
	public void setPlayers(List<Player<G>> players) {
		if (players.size() != 4) {
			throw new IllegalArgumentException("Es müssen 4 Spieler übergeben werden");
		}
		lane1.getScene().getWindow().setOnCloseRequest(event -> {
			timer.cancel();
		});
		var player = players.getFirst();
		setLane(player, lane1);
		player = players.get(1);
		setLane(player, lane2);
		player = players.get(2);
		setLane(player, lane3);
		player = players.get(3);
		setLane(player, lane4);
	}


//endregion
}
