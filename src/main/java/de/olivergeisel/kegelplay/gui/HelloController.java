package de.olivergeisel.kegelplay.gui;

import de.olivergeisel.kegelplay.core.game.GameOverview;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.round;

public class HelloController {
	private static final double ASPECT_RATIO = 16.0 / 10; // Gewünschtes Seitenverhältnis (z. B. 16:9)


	@FXML
	private Label welcomeText;

	protected void newDisplay() throws IOException {
		var stage = new Stage();
		stage.setTitle("Neue Anzeige");
		var loader = new FXMLLoader(getClass().getResource("displays.fxml"));
		var scene = new Scene(loader.load(), 320, 240);
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	public void onDebugButtonClick(ActionEvent actionEvent) throws IOException {
		var oldScene = welcomeText.getScene();
		var stage = (Stage) oldScene.getWindow();
		var fxmlLoader = new FXMLLoader(getClass().getResource("debug.fxml"));
		Pane stackPane = fxmlLoader.load();
		var scene = new Scene(stackPane);
		scene.getStylesheets().add("file:css/bkv.css");

		// Fügen Sie einen ChangeListener für die Breite und Höhe der Szene hinzu
		scene.widthProperty().addListener((observableValue, oldWidth, newWidth) -> {
			// Passen Sie die Höhe basierend auf dem gewünschten Seitenverhältnis an
			double newHeight = round(newWidth.doubleValue() / ASPECT_RATIO);
			stage.setHeight(newHeight);
		});

		stage.setTitle("Debug");
		stage.setHeight(stackPane.getPrefHeight());
		stage.setWidth(stackPane.getPrefWidth());
		stage.setScene(scene);
		stage.setMinHeight(stackPane.getMinHeight());
		stage.setMinWidth(stackPane.getMinWidth());
		stage.centerOnScreen();
		DisplayGameController controller = fxmlLoader.getController();
		stage.show();
	}

	@FXML
	protected void onHelloButtonClick() {
		welcomeText.setText("Welcome to JavaFX Application!");
	}

	@FXML
	public void onNeuFensterClick(ActionEvent actionEvent) throws IOException {
		var oldScene = welcomeText.getScene();
		var stage = (Stage) oldScene.getWindow();
		var fxmlLoader = new FXMLLoader(getClass().getResource("selectgame.fxml"));
		Pane stackPane = fxmlLoader.load();
		var scene = new Scene(stackPane);
		scene.getStylesheets().add("file:css/bkv.css");
		// ChangeListener für die Breite und Höhe der Szene
		scene.widthProperty().addListener((observableValue, oldWidth, newWidth) -> {
			double newHeight = round(newWidth.doubleValue() / ASPECT_RATIO);
			stage.setHeight(newHeight);
		});
		stage.setTitle("Wahl Spiel");
		stage.setHeight(stackPane.getPrefHeight());
		stage.setWidth(stackPane.getPrefWidth());
		stage.setScene(scene);
		stage.setMinHeight(stackPane.getMinHeight());
		stage.setMinWidth(stackPane.getMinWidth());
		stage.centerOnScreen();
		stage.show();
	}



	void selectGame() throws IOException {
		var today = LocalDateTime.now();
		var pathString = String.format("%s\\Daten\\%2d.%2d.%4d", "No_PATH",
				today.getDayOfMonth(), today.getMonthValue(), today.getYear());
		File directory = new File(pathString);
		if (!directory.exists()) {
			throw new NoSuchFileException("Verzeichnis existiert nicht");
		}
		List<GameOverview> gameOverviews = new ArrayList<>();
		var games = directory.listFiles();
		assert games != null;
		for (var game : games) {
			if (!game.isDirectory()) {
				continue;
			}
			var teams = Arrays.stream(Objects.requireNonNull(game.listFiles())).filter(File::isDirectory)
					.map(File::getName).toArray(String[]::new);
			var overview = new GameOverview(LocalDateTime.now(), game.getName(), teams);
			gameOverviews.add(overview);
		}
		var stage = new Stage();
		stage.setTitle("Für Spiel");
		var loader = new FXMLLoader(getClass().getResource("spielwahl.fxml"));
		var scene = new Scene(loader.load(), 320, 240);
		stage.setScene(scene);
		stage.show();

	}
}