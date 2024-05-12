package de.olivergeisel.kegelplay.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.olivergeisel.kegelplay.core.game.Game120;
import de.olivergeisel.kegelplay.core.match.Match;
import de.olivergeisel.kegelplay.core.team_and_player.Player;
import de.olivergeisel.kegelplay.infrastructure.update.MatchUpdater;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class VorlaufEndlaufController implements Initializable, GameController<Game120> {

	private static final long          REFRESH_INTERVAL;
	private static final System.Logger LOGGER = System.getLogger(DisplayGameController.class.getName());

	static {
		var mapper = new ObjectMapper();
		var path = Path.of("./configs/settings.json");
		long selectedInterval = 10 * 1_000; // 10 seconds
		try {
			var parsed = mapper.readTree(path.toFile());
			selectedInterval = parsed.get("refreshRate").asLong();
		} catch (Exception _) {
		}
		REFRESH_INTERVAL = selectedInterval;
	}

	private final MatchUpdater<Game120> matchUpdater;
	private final Match<Game120>        match;
	private final Timer                 timer;


	@FXML
	private VBox team1;
	@FXML
	private VBox team2;
	@FXML
	private VBox team3;

	@FXML private Label showLabel;
	@FXML private VBox  lane1;
	@FXML private VBox  lane2;
	@FXML private VBox  lane3;
	@FXML private VBox  lane4;

	public VorlaufEndlaufController(Match match) {
		this.match = match;
		matchUpdater = new MatchUpdater<>(match);
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(() -> {
					matchUpdater.updateMatch();
					update();
				});
			}
		}, 5_000, REFRESH_INTERVAL);
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	public void update() {
		var start = LocalDateTime.now();
		LOGGER.log(System.Logger.Level.INFO, STR."Update start: \{start}");
		matchUpdater.updateMatch();
		var players = match.getCurrentPlayers();
		setPlayers(players);
		var end = LocalDateTime.now();
		var difference = Duration.between(start, end);
		LOGGER.log(System.Logger.Level.INFO, STR."Update complete: Duration: \{difference.toMillis()} ms");
		int i = 0;
		var teamBoxes = List.of(team1, team2, team3);
		for (var team : match.getTeams()) {
			var box = teamBoxes.get(i++);
			var controller = (TeamOrderController) box.getProperties().get(FXMLLoader.CONTROLLER_KEYWORD);
			controller.update(team);
		}
	}

	private void setLane(Player<Game120> player, VBox lane) {
		var lanes = match.getConfig().getLaneNames();
		var parent = (Pane) lane.getParent();
		var index = parent.getChildren().indexOf(lane);
		var header = (GridPane) lane.getChildren().get(0);
		var headerController = (HeaderController) header.getProperties().get(FXMLLoader.CONTROLLER_KEYWORD);
		if (!player.equals(headerController.getPlayer())) {
			headerController.setPlayer(player);
		}
		var tabelle = lane.getChildren().get(1);
		var tabellenController = (TableController) tabelle.getProperties().get(FXMLLoader.CONTROLLER_KEYWORD);
		tabellenController.setGame(player.getGame(), match);
	}

//region setter/getter
	public void setPlayers(List<Player<Game120>> players) {
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
