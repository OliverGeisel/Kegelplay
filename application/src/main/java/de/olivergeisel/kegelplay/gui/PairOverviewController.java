package de.olivergeisel.kegelplay.gui;

import core.game.Game120;
import core.point_system.GamePointsPlayer;
import core.team_and_player.Player;
import core.util.Pair;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

import static de.olivergeisel.kegelplay.gui.DisplayGameController.getControllerFromPane;

public class PairOverviewController implements Initializable {


	@FXML
	private Pane root;
	@FXML
	private Pane homePlayer;
	@FXML
	private Pane guestPlayer;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		root.getProperties().put(FXMLLoader.CONTROLLER_KEYWORD, this);
	}

	public void update(Pair<Player<Game120>, Player<Game120>> pair,
			Pair<GamePointsPlayer<Game120>, GamePointsPlayer<Game120>> pair2) {
		var controller = getControllerFromPane(homePlayer);
		if (controller instanceof TeamTablePlayerOverview playerController) {
			playerController.update(pair.getFirst(), pair2.getFirst());
		}
		controller = getControllerFromPane(guestPlayer);
		if (controller instanceof TeamTablePlayerOverview playerController2) {
			playerController2.update(pair.getSecond(), pair2.getSecond());
		}


	}

}
