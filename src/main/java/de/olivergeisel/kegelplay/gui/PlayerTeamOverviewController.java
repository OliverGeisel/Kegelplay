package de.olivergeisel.kegelplay.gui;

import java.net.URL;
import java.util.ResourceBundle;

import de.olivergeisel.kegelplay.core.game.Game;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PlayerTeamOverviewController implements Initializable {


	@FXML
	private VBox  playerBox;
	@FXML
	private Label playerLabel;
	@FXML
	private Label bahnLabel;
	@FXML
	private Label throwLabel;
	@FXML
	private Label volleLabel;
	@FXML
	private Label abraeumenLabel;
	@FXML
	private Label missLabel;
	@FXML
	private Label sumLabel;


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
		playerBox.getProperties().put(FXMLLoader.CONTROLLER_KEYWORD, this);
	}


	public void update(Game game, String bahn) {
		playerLabel.setText(STR."\{game.getPlayer().getVorname()} \{game.getPlayer().getNachname()}");
		bahnLabel.setText(bahn);
		throwLabel.setText(String.valueOf(game.getNumberOfWurf()));
		volleLabel.setText(String.valueOf(game.getTotalVolle()));
		abraeumenLabel.setText(String.valueOf(game.getTotalAbraeumen()));
		missLabel.setText(String.valueOf(game.getTotalFehlwurf()));
		sumLabel.setText(String.valueOf(game.getTotalScore()));
	}
}
