package de.olivergeisel.kegelplay.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.olivergeisel.kegelplay.core.game.Game120;
import de.olivergeisel.kegelplay.infrastructure.data_reader.KeglerheimGeneralReader;
import javafx.concurrent.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static java.lang.Math.round;

public class SelectGameController {

	private static final double ASPECT_RATIO = 16.0 / 10; // Gewünschtes Seitenverhältnis (z. B. 16:9)
	@FXML
	Label label;
	private Service<Void> service;
	private Path          datePath;
	@FXML
	private VBox          vBox;

	@FXML
	private DatePicker datePicker;


	@FXML
	public void loadDay(ActionEvent event) throws IOException {
		System.out.println("loadDay");
		var datepick = (DatePicker) event.getSource();
		LocalDate date = datepick.getValue();
		Path path = Path.of("./configs/settings.json");
		var parser = new ObjectMapper();
		var parsed = parser.readTree(path.toFile());
		var folder = parsed.get("dataFolder").asText();
		var formatted = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

		Path pathDay = Path.of(STR."\{folder}/\{formatted}");
		datePath = pathDay;
		var file = pathDay.toFile();
		if (!file.exists() || !file.isDirectory()) {
			throw new FileNotFoundException("There is no game at moment for today");
		}
		var matches = Arrays.stream(file.listFiles()).map(File::getName).toArray(String[]::new);
		showGames(matches);
	}

	@FXML
	public void loadToday(ActionEvent event) throws ParseException, IOException {
		System.out.println("loadGame");
		LocalDate today = LocalDate.now();
		Path path = Path.of("./configs/settings.json");
		var parser = new ObjectMapper();
		var parsed = parser.readTree(path.toFile());
		var folder = parsed.get("dataFolder").asText();
		var formatted = today.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

		Path pathToday = Path.of(STR."\{folder}/\{formatted}");
		var file = pathToday.toFile();
		if (!file.exists() || file.isDirectory()) {
			throw new FileNotFoundException("There is no game at moment for today");
		}
		var matches = file.listFiles();
		showGames(Arrays.stream(matches).map(File::getName).toArray(String[]::new));
		System.out.println("loaded");
	}

	private void showGames(String[] matches) {
		vBox.getChildren().clear();
		for (var game : matches) {
			var button = new Button(game);

			button.setOnAction(this::loadSelectedGame);

			vBox.getChildren().add(button);
		}
	}

	@FXML
	public void loadSelectedGame(ActionEvent event) {
		var button = (Button) event.getSource();
		var selectedGame = ((Button) event.getSource()).getText();
		var oldScene = button.getScene();
		var stage = (Stage) oldScene.getWindow();
		var fxmlLoader = new FXMLLoader(getClass().getResource("display-game.fxml"));

		Path path = Path.of("Blockstart 120 Wurf 6Sp 2M_000");
		//Path path = Path.of("C:\\Control Center Kegeln\\Daten\\01.04.2023\\Blockstart 120 Wurf 6Sp 2M_002");
		var dataReader = new KeglerheimGeneralReader(datePath.resolve(selectedGame), true);
		var match = dataReader.initNewMatch();
		for (var player : match.getTeams()[0].getPlayers()) {
			player.setGame(new Game120(player));
		}
		for (var player : match.getTeams()[1].getPlayers()) {
			player.setGame(new Game120(player));
		}
		DisplayGameController controller = new DisplayGameController<Game120>(match);
		fxmlLoader.setController(controller);
		Pane stackPane;
		try {
			stackPane = fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		var scene = new Scene(stackPane);
		scene.getStylesheets().add("file:css/bkv.css");

		// ChangeListener für die Breite und Höhe der Szene
		scene.widthProperty().addListener((observableValue, oldWidth, newWidth) -> {
			double newHeight = round(newWidth.doubleValue() / ASPECT_RATIO);
			stage.setHeight(newHeight);
		});

		stage.setTitle("4 gegeneinander Satzpunkte");
		stage.setHeight(stackPane.getPrefHeight());
		stage.setWidth(stackPane.getPrefWidth());
		stage.setScene(scene);
		stage.setMinHeight(stackPane.getMinHeight());
		stage.setMinWidth(stackPane.getMinWidth());
		stage.centerOnScreen();
		stage.show();
	}
}
