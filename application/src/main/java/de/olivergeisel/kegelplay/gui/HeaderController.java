package de.olivergeisel.kegelplay.gui;

import core.team_and_player.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class HeaderController implements Initializable {

	private static final String DEFAULT_IMAGE = "file:./images/kegeln.png";


	private Player player;
	@FXML
	private Label  vorname;
	@FXML
	private Label     name;
	@FXML
	private Label     verein;
	@FXML
	private ImageView image;
	@FXML
	private GridPane grid;


	public HeaderController() {
	}

	private static String getImagePathPNG(Player player) {
		return STR."./images/\{player.getClub()}/\{player.getCompleteNameWithUnderscore()}.png";
	}

	private static String getImagePathJPG(Player player) {
		return STR."./images/\{player.getClub()}/\{player.getCompleteNameWithUnderscore()}.jpg";
	}

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
		vorname.setText("Vorname");
		name.setText("Name");
		verein.setText("Verein");
		vorname.setWrapText(true);
		name.setWrapText(true);
		verein.setWrapText(true);
		image.setImage(new Image(DEFAULT_IMAGE));

		vorname.getProperties().put(FXMLLoader.CONTROLLER_KEYWORD, this);
		name.getProperties().put(FXMLLoader.CONTROLLER_KEYWORD, this);
		verein.getProperties().put(FXMLLoader.CONTROLLER_KEYWORD, this);
		image.getProperties().put(FXMLLoader.CONTROLLER_KEYWORD, this);
		grid.getProperties().put(FXMLLoader.CONTROLLER_KEYWORD, this);
		// Todo check if can be swapped with only set instead bind
		grid.widthProperty().addListener((observableValue, old, newValue) -> {
			var width = newValue.doubleValue();
			var height = grid.getHeight();
			var imageWidth = image.getImage().getWidth();
			var imageHeight = image.getImage().getHeight();
			var imageRatio = imageWidth / imageHeight;
			var newHeight = 0.38 * width / imageRatio;
			if (newHeight > 0.95 * height) {
				image.fitHeightProperty().bind(grid.heightProperty().multiply(0.95));
			} else {
				image.fitWidthProperty().bind(grid.widthProperty().multiply(0.38));
			}
		});
	}

	//region setter/getter


	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
		vorname.setText(player.getVorname());
		name.setText(player.getNachname());
		verein.setText(player.getClub());
		var imagePath = getImagePathPNG(player);
		var imageFile = new File(imagePath);
		if (imageFile.exists()) {
			this.image.setImage(new Image(STR."file:\{imagePath}"));
		} else { // Todo refactor
			imagePath = getImagePathJPG(player);
			if (new File(imagePath).exists()) {
				this.image.setImage(new Image(STR."file:\{imagePath}"));
			} else {
				var fallBack = STR."images/\{player.getClub()}/logo.png";
				if (new File(fallBack).exists()) {
					this.image.setImage(new Image(STR."file:\{fallBack}"));
				} else {
					this.image.setImage(new Image(DEFAULT_IMAGE));
				}
			}
		}
	}
//endregion
}
