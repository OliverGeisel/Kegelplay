package de.olivergeisel.kegelplay.gui;

import de.olivergeisel.kegelplay.core.team_and_player.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class HeaderController {

	private Player    player;
	@FXML
	private Label     vorname;
	@FXML
	private Label     name;
	@FXML
	private Label     verein;
	@FXML
	private ImageView image;

	public HeaderController() {
	}

	public HeaderController(Player player) {
		this.player = player;
		vorname.setText(player.getVorname());
		name.setText(player.getNachname());
		verein.setText(player.getClub());
	}

	public void initialize() {
		vorname.setText("Vorname");
		name.setText("Name");
		verein.setText("Verein");
	}

}
