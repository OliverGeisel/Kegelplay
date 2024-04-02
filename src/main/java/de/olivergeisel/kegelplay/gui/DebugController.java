package de.olivergeisel.kegelplay.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.olivergeisel.kegelplay.infrastructure.data_reader.KeglerheimGeneralReader;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static java.util.FormatProcessor.FMT;

public class DebugController {

	@FXML
	Label label;
	private Service<Void> service;
	private Path          datePath;
	@FXML
	private VBox vBox;

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
	public void stop(ActionEvent event) {
		service.cancel();
		System.out.println(STR."stopped \{LocalDateTime.now()}");

	}

	@FXML
	public void loadSelectedGame(ActionEvent event) {
		var selectedGame = ((Button) event.getSource()).getText();
		var dataReader = new KeglerheimGeneralReader(datePath.resolve(selectedGame), true);
		service = new Service<>() {
			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						int i = 0;
						while (!isCancelled()) {
							try {
								System.out.println(FMT."load: \{LocalDateTime.now()} \t %05d\{i++}");
								var match = dataReader.initNewMatch();
							} catch (Exception e) {
								Thread.sleep(1000);
							}
							Thread.sleep(100);
						}
						return null;
					}
				};
			}
		};
		service.start();
	}

}
