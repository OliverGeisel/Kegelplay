package de.olivergeisel.kegelplay.gui;

import de.olivergeisel.kegelplay.core.game.Game;
import de.olivergeisel.kegelplay.core.match.Match;
import de.olivergeisel.kegelplay.core.match.Match1Team;
import de.olivergeisel.kegelplay.core.team_and_player.Player;
import de.olivergeisel.kegelplay.infrastructure.csv.CsvService;
import de.olivergeisel.kegelplay.infrastructure.data_reader.KeglerheimGeneralReader;
import de.olivergeisel.kegelplay.infrastructure.update.MatchUpdater;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class DisplayGameController<G extends Game> implements Initializable {

	private static final long          REFRESH_INTERVAL = 2 * 5 * 1_000L; // 10 Sekunden in Millisekunden
	private static final System.Logger LOGGER           = System.getLogger(DisplayGameController.class.getName());

	private Match<G>                match;
	private KeglerheimGeneralReader dataReader;
	private CsvService              csvService;
	private MatchUpdater<G>         matchUpdater;

	@FXML
	private Label showLabel;
	@FXML
	private VBox lane1;
	@FXML
	private VBox lane2;
	@FXML
	private VBox lane3;
	@FXML
	private VBox lane4;

	public DisplayGameController(Match<G> match) {
		this.csvService = new CsvService();
		this.match = match;
		matchUpdater = new MatchUpdater<>(match);
	}

	private void initMatch() {
		var today = LocalDate.now();
		//var dateString = today.format(DateTimeFormatter.ofPattern(KeglerheimGeneralReader.TODAY_FORMAT));
		var path = Paths.get("Blockstart 120 Wurf 6Sp 2M_000");
		match = new Match1Team<>(path);
		dataReader = new KeglerheimGeneralReader(path, true);
		dataReader.updateMatch(match);
		// 1 Team 4 Spieler
		setPlayers(Arrays.stream(match.getTeams()[0].getPlayers()).toList());
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
		init();
	}

	private void init() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(() -> {
					// update
					LOGGER.log(System.Logger.Level.INFO, STR."Update start: \{LocalDateTime.now()}");
					matchUpdater.updateMatch();
					var players = match.getCurrentPlayers();
					setPlayers(players);
					LOGGER.log(System.Logger.Level.INFO, STR."Update complete: \{LocalDateTime.now()}");
				});
			}
		}, 0, REFRESH_INTERVAL);
	}


	private void setLane(Player<G> player, VBox lane) {
		var header = (GridPane) lane.getChildren().getFirst();
		var headerController = (HeaderController) header.getProperties().get(FXMLLoader.CONTROLLER_KEYWORD);
		headerController.setPlayer(player);
		var tabelle = lane.getChildren().get(1);
		var tabellenController = (TableController) tabelle.getProperties().get(FXMLLoader.CONTROLLER_KEYWORD);
		tabellenController.setGame(player.getGame());
		var fallbild = lane.getChildren().get(2);
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
	 * Call only once
	 */
	public void update() {
		csvService.update();
	}

	//region setter/getter
	public void setPlayers(List<Player<G>> players) {
		if (players.size() != 4) {
			throw new IllegalArgumentException("Es müssen 4 Spieler übergeben werden");
		}
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
