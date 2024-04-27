package de.olivergeisel.kegelplay.gui;

import de.olivergeisel.kegelplay.core.game.Game120;
import de.olivergeisel.kegelplay.core.team_and_player.Team;
import de.olivergeisel.kegelplay.infrastructure.ini.IniFile;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class TeamTableController implements Initializable {

	@FXML
	private GridPane teamTable;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		teamTable.getProperties().put(FXMLLoader.CONTROLLER_KEYWORD, this);
	}

	public void update(List<Team<Game120>> teams) {
		var children = teamTable.getChildren();
		var vorlaufMap = new HashMap<String, Integer>();
		for (var team : teams) {
			vorlaufMap.put(team.getName(), getVorlauf(team.getName()));
		}
		var teamsSort = teams.stream()
							 .sorted(Comparator.comparingInt(t -> (t.getTeamScore() + vorlaufMap.get(t.getName()))))
							 .toList();
		for (var child : children) {
			if (child instanceof Label label) {
				var rowIndex = GridPane.getRowIndex(label);
				var columnIndex = GridPane.getColumnIndex(label);
				if (rowIndex == 0) { // header
					continue;
				}
				Team<Game120> team = teamsSort.get(rowIndex - 1);
				int vorlauf = vorlaufMap.get(team.getName());
				switch (columnIndex) {
					case 0 -> label.setText(team.getName()); // team name
					case 1 -> label.setText(Integer.toString(team.getTeamTotalThrows()));
					case 2 -> label.setText(String.valueOf(team.getTeamTotalVolle()));
					case 3 -> label.setText(String.valueOf(team.getTeamTotalAbraeumen()));
					case 4 -> label.setText(String.valueOf(team.getTeamTotalMissThrow()));
					case 5 ->
						// set the vorlauf value
							label.setText(Integer.toString(vorlauf));
					case 6 -> // set total score
							label.setText(STR."\{vorlauf + team.getTeamScore()}");
					case 7 -> { // difference to the next team
						if (rowIndex == 2) {
							label.setText("0"); // Todo not needed
						}
						var sum = team.getTeamScore() + vorlauf;
						var prefTeam = teamsSort.get(rowIndex - 2);
						var vorlaufPrevious = vorlaufMap.get(prefTeam.getName());
						var sumPrevious = prefTeam.getTeamScore() + vorlaufPrevious;
						var diff = sum - sumPrevious;
						label.setText(String.valueOf(diff));
					}
				}

			}
		}
	}

	private int getVorlauf(String teamName) {
		final var iniFilePath = Path.of("teams.ini");
		try {
			var iniFile = new IniFile(iniFilePath);
			var teamRegion = iniFile.getRegion(teamName);
			var value = teamRegion.getValue("Vorlauf");
			return Integer.parseInt(value);
		} catch (Exception e) {
			return 0;
		}
	}


}
