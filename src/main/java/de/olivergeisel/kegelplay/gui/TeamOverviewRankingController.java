package de.olivergeisel.kegelplay.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.olivergeisel.kegelplay.core.game.Game120;
import de.olivergeisel.kegelplay.core.match.Match;
import de.olivergeisel.kegelplay.core.team_and_player.Team;
import de.olivergeisel.kegelplay.infrastructure.update.MatchUpdater;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class TeamOverviewRankingController implements Initializable {


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

	private final MatchUpdater<Game120> matchUpdater1;
	private final MatchUpdater<Game120> matchUpdater2;
	private final Timer                 timer;
	private       Match<Game120>        match1;
	private       Match<Game120>        match2;


	@FXML
	private VBox     team1;
	@FXML
	private VBox     team2;
	@FXML
	private VBox     team3;
	@FXML
	private VBox     team4;
	@FXML
	private GridPane teamOverview;

	public TeamOverviewRankingController(Match<Game120> match1, Match<Game120> match2) {
		this.match1 = match1;
		this.match2 = match2;
		matchUpdater1 = new MatchUpdater<>(match1);
		matchUpdater2 = new MatchUpdater<>(match2);
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
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(() -> {
					// update
					var start = LocalDateTime.now();
					LOGGER.log(System.Logger.Level.INFO, STR."Update start: \{start}");
					matchUpdater1.updateMatch();
					var teams = new LinkedList<>(Arrays.stream(match1.getTeams()).toList());
					matchUpdater2.updateMatch();
					teams.addAll(Arrays.stream(match2.getTeams()).toList());
					// update gui
					update(teams);
					// end update
					var end = LocalDateTime.now();
					var difference = Duration.between(start, end);
					LOGGER.log(System.Logger.Level.INFO, STR."Update complete: Duration: \{difference.toMillis()} ms");
				});
			}
		}, 0, REFRESH_INTERVAL);
	}

	public void update(List<Team<Game120>> teams) {
		if (teams.size() < 4) {
			return;
		}
		var controller = (TeamCompleteViewController) team1.getProperties().get(FXMLLoader.CONTROLLER_KEYWORD);
		controller.update(teams.getFirst());
		controller = (TeamCompleteViewController) team2.getProperties().get(FXMLLoader.CONTROLLER_KEYWORD);
		controller.update(teams.get(1));
		controller = (TeamCompleteViewController) team3.getProperties().get(FXMLLoader.CONTROLLER_KEYWORD);
		controller.update(teams.get(2));
		controller = (TeamCompleteViewController) team4.getProperties().get(FXMLLoader.CONTROLLER_KEYWORD);
		controller.update(teams.get(3));
		var tableController = (TeamTableController) teamOverview.getProperties().get(FXMLLoader.CONTROLLER_KEYWORD);
		tableController.update(teams);
	}

	//region setter/getter
	public void setMatch1(Match<Game120> match1) {
		this.match1 = match1;
	}

	public void setMatch2(Match<Game120> match2) {
		this.match2 = match2;
	}
//endregion

}
