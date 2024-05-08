package de.olivergeisel.kegelplay.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

import static java.lang.Math.round;

public class HelloController {
	private static final double ASPECT_RATIO = 16.0 / 9; // Gewünschtes Seitenverhältnis (z. B. 16:9)


	@FXML
	public void onDebugButtonClick(ActionEvent actionEvent) throws IOException {
		var oldScene = ((Button) actionEvent.getSource()).getScene();
		var stage = (Stage) oldScene.getWindow();
		var fxmlLoader = new FXMLLoader(getClass().getResource("debug.fxml"));
		Pane stackPane = fxmlLoader.load();
		var scene = new Scene(stackPane);
		scene.getStylesheets().add("file:css/bkv.css");

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
	public void onNeuFensterClick(ActionEvent actionEvent) throws IOException {
		var oldScene = ((Button) actionEvent.getSource()).getScene();
		var stage = (Stage) oldScene.getWindow();
		var fxmlLoader = new FXMLLoader(getClass().getResource("selectgame.fxml"));
		Pane stackPane = fxmlLoader.load();
		var scene = new Scene(stackPane);
		stage.setTitle("Wahl Spiel");
		stage.setHeight(stackPane.getPrefHeight());
		stage.setWidth(stackPane.getPrefWidth());
		stage.setScene(scene);
		stage.setMinHeight(stackPane.getMinHeight());
		stage.setMinWidth(stackPane.getMinWidth());
		stage.centerOnScreen();
		stage.show();
	}
}