package de.olivergeisel.kegelplay.gui;

import core.game.Game120;
import core.match.Match2Teams;
import core.point_system.GamePointsEvaluator_2Players;
import core.point_system.GamePointsPlayer;
import core.util.Pair;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import static de.olivergeisel.kegelplay.gui.DisplayGameController.getControllerFromPane;

public class RoundController implements Initializable {

	@FXML
	private Pane root;
	@FXML
	private Pane pair1;
	@FXML
	private Pane pair2;

	public void update(Match2Teams<Game120> match, int startRound) {
		var p1 = match.getPairByPosition(startRound);
		var p2 = match.getPairByPosition(startRound + 1);
		var controller = getControllerFromPane(pair1);
		var points = evaluateMatch(match);
		if (controller instanceof PairOverviewController pairController) {
			var pairPoints = new Pair<>(points.getFirst().get(startRound), points.getSecond().get(startRound));
			pairController.update(p1, pairPoints);
		}
		controller = getControllerFromPane(pair2);
		if (controller instanceof PairOverviewController pairController) {
			var pairPoints = new Pair<>(points.getFirst().get(startRound + 1), points.getSecond().get(startRound + 1));
			pairController.update(p2, pairPoints);
		}
	}

	private Pair<List<GamePointsPlayer<Game120>>, List<GamePointsPlayer<Game120>>> evaluateMatch(
			Match2Teams<Game120> match) {
		var home = match.getHome();
		var guest = match.getGuest();
		var homePoints = new LinkedList<GamePointsPlayer<Game120>>();
		var guestPoints = new LinkedList<GamePointsPlayer<Game120>>();
		for (int i = 0; i < home.getPlayers().length; i++) {
			var pair = GamePointsEvaluator_2Players.evaluate(home.getPlayers()[i],
					guest.getPlayers()[i]);
			homePoints.add(pair.getFirst());
			guestPoints.add(pair.getSecond());
		}
		return new Pair<>(homePoints, guestPoints);
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		root.getProperties().put(FXMLLoader.CONTROLLER_KEYWORD, this);
	}
}
