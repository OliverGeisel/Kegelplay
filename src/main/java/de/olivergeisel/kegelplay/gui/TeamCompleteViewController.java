package de.olivergeisel.kegelplay.gui;

import de.olivergeisel.kegelplay.core.team_and_player.Team;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class TeamCompleteViewController implements Initializable {


	@FXML
	private VBox  teamBox;
	@FXML
	private Label teamLabel;

	@FXML
	private VBox players;


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
		teamBox.getProperties().put(FXMLLoader.CONTROLLER_KEYWORD, this);
	}

	public void update(Team team) {
		teamLabel.setText(team.getName());
		var playerBoxes = players.getChildren();
		int i = 0;
		for (var player : team.getPlayers()) {
			var playerBox = playerBoxes.get(i++);
			var controller =
					(PlayerTeamOverviewController) playerBox.getProperties().get(FXMLLoader.CONTROLLER_KEYWORD);
			controller.update(player.getGame());
		}
	}


}
