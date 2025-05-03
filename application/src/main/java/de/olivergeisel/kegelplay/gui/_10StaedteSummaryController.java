package de.olivergeisel.kegelplay.gui;

import core.team_and_player.Player;
import core.team_and_player.Team;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class _10StaedteSummaryController implements Initializable {

	@FXML
	VBox root;


	public void update(Team[] teams, List<Player> currentPlayers) {
		root.getChildren().clear();
		var sortedTeams = new LinkedList<>(Arrays.asList(teams));
		sortedTeams.sort((team1, team2) -> {
			var score1 = team1.getTeamScore();
			var score2 = team2.getTeamScore();
			return Integer.compare(score2, score1);
		});
		int i = 1;
		for (var team : sortedTeams) {
			var teamLine = new HBox();
			var teamRank = new Label(String.valueOf(i++));
			teamRank.setStyle("-fx-font-size: 20px;");
			teamRank.setMinWidth(20);
			var name = new Label(team.getName());
			name.setPadding(new Insets(0, 10, 0, 10));
			name.setStyle("-fx-font-size: 20px;");
			name.setMinWidth(350);
			name.setAlignment(javafx.geometry.Pos.CENTER);
			var score = new Label(String.valueOf(team.getTeamScore()));
			score.setMinWidth(50);
			score.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
			teamLine.setAlignment(Pos.CENTER_LEFT);
			teamLine.setSpacing(10);
			teamLine.setPadding(new Insets(5, 0, 5, 10));
			var bahn = new Label(find_Bahn(team, currentPlayers));
			bahn.setStyle("-fx-font-size: 20px;");
			bahn.setMinWidth(50);
			var volle = new Label(team.getTeamTotalVolle() + "");
			volle.setStyle("-fx-font-size: 20px;");
			volle.setMinWidth(50);
			var raeumer = new Label(team.getTeamTotalAbraeumen() + "");
			raeumer.setStyle("-fx-font-size: 20px;");
			raeumer.setMinWidth(50);
			var fehler = new Label(team.getTeamTotalMissThrow() + "");
			fehler.setStyle("-fx-font-size: 20px;");
			fehler.setMinWidth(50);
			bahn.setAlignment(Pos.CENTER);
			bahn.setPadding(new Insets(0, 10, 0, 10));
			teamLine.setStyle("-fx-background-color: rgba(65,245,45,0.1);");
			teamLine.getChildren().add(teamRank);
			teamLine.getChildren().add(name);
			teamLine.getChildren().add(score);
			teamLine.getChildren().add(volle);
			teamLine.getChildren().add(raeumer);
			teamLine.getChildren().add(fehler);
			teamLine.getChildren().add(bahn);

			root.getChildren().add(teamLine);
		}
	}

	private String find_Bahn(Team team, List<Player> currentPlayers) {
		int i = 1;
		for (var player : currentPlayers) {
			if (Arrays.stream(team.getPlayers()).toList().contains(player)) {
				if (i > 4) {
					return STR."Bahn \{i + 4}";
				}
				return STR."Bahn \{i}";
			}
			i++;
		}
		return "";
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		root.getProperties().put(FXMLLoader.CONTROLLER_KEYWORD, this);
	}
}
