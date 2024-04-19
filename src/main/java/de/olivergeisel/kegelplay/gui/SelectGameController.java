package de.olivergeisel.kegelplay.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.olivergeisel.kegelplay.core.game.Game;
import de.olivergeisel.kegelplay.core.game.GameKind;
import de.olivergeisel.kegelplay.core.match.Match;
import de.olivergeisel.kegelplay.core.match.Match1Team;
import de.olivergeisel.kegelplay.core.point_system.AllAgainstAll120_4PlayerPointSystem;
import de.olivergeisel.kegelplay.core.point_system.PointSystem;
import de.olivergeisel.kegelplay.core.point_system._2Teams120PointSystem;
import de.olivergeisel.kegelplay.infrastructure.data_reader.KeglerheimGeneralReader;
import de.olivergeisel.kegelplay.infrastructure.data_reader.UnsupportedMatchSchema;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static java.lang.Math.round;

public class SelectGameController implements Initializable {

	private static final double        ASPECT_RATIO = 16.0 / 9; // Gewünschtes Seitenverhältnis (z. B. 16:9)
	private static final System.Logger LOGGER       = System.getLogger(SelectGameController.class.getName());


	private Path datePath;

	@FXML
	private VBox                vBox;
	@FXML
	private DatePicker          datePicker;
	@FXML
	private ChoiceBox<GameKind> gameKind;
	@FXML
	private ChoiceBox<String>   view;
	@FXML
	private ChoiceBox<String> pointSystemField;
	@FXML
	private CheckBox          frameless;
	@FXML
	private CheckBox          onTop;


	@FXML
	public void loadDay(ActionEvent event) {
		var datepick = (DatePicker) event.getSource();
		LocalDate date = datepick.getValue();
		Path path = Path.of("./configs/settings.json");
		try {
			loadGames(path, date);
		} catch (FileNotFoundException e) {
			vBox.getChildren()
				.add(new Label(STR."No games for the \{date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))}"));
		} catch (IOException e) {
			LOGGER.log(System.Logger.Level.ERROR, STR."Error loading games: \{e.getMessage()}");
		}
	}

	private void loadGames(Path path, LocalDate date) throws FileNotFoundException, IOException {
		System.out.println("loadDay");
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
		var matches = Arrays.stream(Objects.requireNonNull(file.listFiles())).map(File::getName).toArray(String[]::new);
		showGames(matches);
		System.out.println("loaded");
	}

	@FXML
	public void loadToday(ActionEvent event) throws IOException {
		System.out.println("loadGame");
		LocalDate today = LocalDate.now();
		Path path = Path.of("./configs/settings.json");
		try {
			loadGames(path, today);
		} catch (FileNotFoundException e) {
			vBox.getChildren().add(new Label("No games for today"));
		}
		datePicker.setValue(today);
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
		var selectedGame = button.getText();
		var oldScene = button.getScene();
		var stage = (Stage) oldScene.getWindow();
		var fxmlLoader = new FXMLLoader(getClass().getResource("display-game.fxml"));
		var datePath = this.datePath;
		var dataReader = new KeglerheimGeneralReader(datePath.resolve(selectedGame), true);
		var kind = gameKind.getValue();
		var selectedView = view.getValue();
		var systemSelect = pointSystemField.getValue();
		var title = "";
		Match<?> match;
		try {
			match = dataReader.initNewMatch();
		} catch (UnsupportedMatchSchema e) {
			e.printStackTrace();
			return;
		}
		PointSystem pointSystem = switch (systemSelect) {
			case "4 Spieler gegeneinander" -> {
				title = "4 gegeneinander Satzpunkte";
				yield new AllAgainstAll120_4PlayerPointSystem();
			}
			case "2 Teams paarweise" -> {
				title = "2 Mannschaften mit Satzpunkten";
				yield new _2Teams120PointSystem();
			}
			case "Teams summe", "Paarweise gegeneinander" -> null;
			default -> throw new IllegalStateException(STR."Unexpected value: \{systemSelect}");
		};
		match.setPointSystem(pointSystem);
		DisplayGameController<? extends Game> controller = switch (selectedView) {
			case "4 gegeneinander" -> {
				if (match instanceof Match1Team<?> match1Team) {
					yield new _4PlayersAllAgainstController<>(match1Team);
				} else {
					throw new IllegalStateException(STR."Unexpected value: \{match}");
				}
			}
			case "2 Teams" -> new _2TeamsAgainstController<>(match);
			case "N Teams" -> null;
			default -> throw new IllegalStateException(STR."Unexpected value: \{selectedView}");
		};
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
		stage.setTitle(title);
		stage.setHeight(stackPane.getPrefHeight());
		stage.setWidth(stackPane.getPrefWidth());
		stage.setMinHeight(stackPane.getMinHeight());
		stage.setMinWidth(stackPane.getMinWidth());
		stage.centerOnScreen();
		stage.setScene(scene);
		stage.show();
		if (frameless.isSelected()) {
			stage.setFullScreen(true);
		} else {
			scene.widthProperty().addListener((observableValue, oldWidth, newWidth) -> {
				double newHeight = round(newWidth.doubleValue() / ASPECT_RATIO);
				stage.setHeight(newHeight);
			});
		}
		stage.setAlwaysOnTop(onTop.isSelected());
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
		gameKind.getItems().addAll(GameKind.values());
		gameKind.setValue(GameKind.GAME_120);
		// view
		var views = List.of("4 gegeneinander", "2 Teams", "N Teams");
		view.getItems().addAll(views);
		// pointSystem
		var pointSystems =
				List.of("4 Spieler gegeneinander", "2 Teams paarweise", "Teams summe", "Paarweise gegeneinander");
		pointSystemField.getItems().addAll(pointSystems);
	}
}
