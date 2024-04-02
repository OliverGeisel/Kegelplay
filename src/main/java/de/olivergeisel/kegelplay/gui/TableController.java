package de.olivergeisel.kegelplay.gui;

import de.olivergeisel.kegelplay.core.game.Game;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class TableController implements Initializable {

	@FXML
	private GridPane grid;

	private Game game;

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
		for (var lane : grid.getChildren()) {
			if (lane instanceof Label label) {
				if (GridPane.getRowIndex(label) == 0) {
					continue;
				}
				label.setText("0");
			}
		}
		grid.getProperties().put(FXMLLoader.CONTROLLER_KEYWORD, this);
	}

	private void updateTable() {
		var rowcount = grid.getRowCount();
		for (var lane : grid.getChildren()) {
			if (lane instanceof Label label) {
				var row = GridPane.getRowIndex(label);
				var col = GridPane.getColumnIndex(label);
				if (row == 0) { // skip first row
					continue;
				}
				if (GridPane.getRowIndex(label) == rowcount - 1) { // last row
					switch (col) {
						case 0 -> label.setText(Integer.toString(game.getNumberOfWurf()));
						case 1 -> label.setText(Integer.toString(game.getTotalVolle()));
						case 2 -> label.setText(Integer.toString(game.getTotalAbraeumen()));
						case 3 -> label.setText(Integer.toString(game.getTotalScore()));
						case 4 -> label.setText(Integer.toString(game.getTotalFehlwurf()));
						case 5 -> label.setText("0");
						default -> throw new IllegalStateException(STR."Unexpected value: \{col}");
					}
					continue;
				}
				var durchgang = game.getDurchgang(row - 1);
				switch (col) {
					case 0 -> label.setText(Integer.toString(durchgang.getAnzahlGespielteWuerfe()));
					case 1 -> label.setText(Integer.toString(durchgang.getAnzahlVolle()));
					case 2 -> label.setText(Integer.toString(durchgang.getAnzahlAbraeumen()));
					case 3 -> label.setText(Integer.toString(durchgang.getScore()));
					case 4 -> label.setText(Integer.toString(durchgang.getAnzahlFehler()));
					case 5 -> label.setText("0");
					default -> throw new IllegalStateException(STR."Unexpected value: \{col}");
				}
			}
		}
	}

//region setter/getter
	public void setGame(Game game) {
		this.game = game;
		updateTable();
	}
//endregion
}
