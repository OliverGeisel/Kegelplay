package de.olivergeisel.kegelplay.gui;

import core.match.Match;
import core.team_and_player.Team;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class _10StaedteTeamController implements Initializable {

	private static final String DEFAULT_IMAGE = "./images/kegeln.png";
	@FXML
	HBox player1, player2, player3, player4, player5, player6;
	@FXML
	Label teamName;
	@FXML ImageView image;
	@FXML
	private VBox root;

	private static String getClubLogo(String teamName) {
		var defaultImage = STR."./images/\{teamName}/logo.png";
		if (new File(defaultImage).exists()) {
			return defaultImage;
		}
		// JPG
		var imagePath = STR."./images/\{teamName}/logo.jpg";
		if (new File(imagePath).exists()) {
			return imagePath;
		}
		/** TODO IMPROVE
		 // SVG
		 imagePath = STR."./images/\{player.getClub()}/logo.svg";
		 if (new File(imagePath).exists()) {
		 return imagePath;
		 }**/
		return DEFAULT_IMAGE;
	}

	public void update(Team team, Match match) {
		teamName.setText(team.getName());
		var players = team.getPlayers();
		var controller = (_10StaedtePlayerController) player1.getProperties().get(FXMLLoader.CONTROLLER_KEYWORD);
		controller.update(players[0], match);
		controller = (_10StaedtePlayerController) player2.getProperties().get(FXMLLoader.CONTROLLER_KEYWORD);
		controller.update(players[1], match);
		controller = (_10StaedtePlayerController) player3.getProperties().get(FXMLLoader.CONTROLLER_KEYWORD);
		controller.update(players[2], match);
		controller = (_10StaedtePlayerController) player4.getProperties().get(FXMLLoader.CONTROLLER_KEYWORD);
		controller.update(players[3], match);
		controller = (_10StaedtePlayerController) player5.getProperties().get(FXMLLoader.CONTROLLER_KEYWORD);
		controller.update(players[4], match);
		controller = (_10StaedtePlayerController) player6.getProperties().get(FXMLLoader.CONTROLLER_KEYWORD);
		controller.update(players[5], match);
		image.setImage(new Image(STR."file:\{getClubLogo(team.getName())}"));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		root.getProperties().put(FXMLLoader.CONTROLLER_KEYWORD, this);
	}

}
