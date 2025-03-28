package de.olivergeisel.kegelplay.gui;


import core.team_and_player.Team;
import de.kegelplay.infrastructure.ini.IniFile;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.IOException;

public class TeamOrderExtendedController extends TeamOrderController {

	@FXML
	private VBox root;

	@Override
	public void update(Team team) {
		IniFile iniFile;
		try {
			iniFile = new IniFile("display-infos/additional-info.ini");
		} catch (IOException e) {
			log.log(System.Logger.Level.ERROR,
					"Could not read additional-info.ini. Please create 'display-infos/additional-info.ini'", e);
			return;
		}
		// Clear the root node
		root.getChildren().clear();
		var teamLabel = new Label(team.getName());
		teamLabel.fontProperty().set(new Font(30));
		root.getChildren().add(teamLabel);
		var header = new HBox();
		header.paddingProperty().set(new Insets(10, 0, 2, 0));
		var playerNameHeader = new Label(STR."Spieler");
		playerNameHeader.fontProperty().set(new Font(22));
		playerNameHeader.prefWidthProperty().set(200);
		var vereinHeader = new Label(STR."Verein");
		vereinHeader.fontProperty().set(new Font(22));
		vereinHeader.prefWidthProperty().set(200);
		var scoreVorlaufHeader = new Label(STR."Vorlauf");
		scoreVorlaufHeader.fontProperty().set(new Font(22));
		scoreVorlaufHeader.prefWidthProperty().set(100);
		var scoreEndlaufHeader = new Label(STR."Endlauf");
		scoreEndlaufHeader.fontProperty().set(new Font(22));
		scoreEndlaufHeader.prefWidthProperty().set(100);
		var totalScoreHeader = new Label(STR."Gesamt");
		totalScoreHeader.fontProperty().set(new Font(22));
		totalScoreHeader.prefWidthProperty().set(100);
		header.getChildren()
			  .addAll(playerNameHeader, vereinHeader, scoreVorlaufHeader, scoreEndlaufHeader, totalScoreHeader);
		root.getChildren().add(header);
		var sorted = getPlayerScoreStream(team, iniFile);
		sorted.forEach(playerAndScore -> {
			var playerLine = new HBox();
			var playerName = new Label(playerAndScore.player().getCompleteName());
			playerName.paddingProperty().set(new Insets(0, 10, 0, 0));
			playerName.fontProperty().set(new Font(20));
			playerName.prefWidthProperty().set(200);
			var totalScore = new Label(STR."\{playerAndScore.score()}");
			totalScore.paddingProperty().set(new Insets(0, 0, 0, 10));
			totalScore.prefWidthProperty().set(100);
			totalScore.fontProperty().set(new Font(20));
			var endlaufScoreValue = playerAndScore.player().getGame().getTotalScore();
			var vorlaufScoreValue = playerAndScore.score() - endlaufScoreValue;
			if (endlaufScoreValue == 0) {
				vorlaufScoreValue = 0;
			}
			var scoreVorlauf = new Label(STR."\{vorlaufScoreValue}");
			scoreVorlauf.fontProperty().set(new Font(20));
			scoreVorlauf.prefWidthProperty().set(100);
			var scoreEndlauf = new Label(STR."\{endlaufScoreValue}");
			scoreEndlauf.fontProperty().set(new Font(20));
			scoreEndlauf.prefWidthProperty().set(100);
			var verein = new Label(STR."\{playerAndScore.player().getClub()}");
			verein.paddingProperty().set(new Insets(0, 10, 0, 0));
			verein.fontProperty().set(new Font(20));
			verein.prefWidthProperty().set(200);
			playerLine.getChildren().addAll(playerName, verein, scoreVorlauf, scoreEndlauf, totalScore);
			root.getChildren().add(playerLine);
		});
	}

	//region setter/getter
	public void setRoot(VBox root) {
		this.root = root;
	}
//endregion

}
