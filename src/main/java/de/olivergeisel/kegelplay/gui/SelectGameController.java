package de.olivergeisel.kegelplay.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.olivergeisel.kegelplay.core.game.Game;
import de.olivergeisel.kegelplay.core.game.GameKind;
import de.olivergeisel.kegelplay.core.match.Match;
import de.olivergeisel.kegelplay.core.match.Match1Team;
import de.olivergeisel.kegelplay.core.point_system.AllAgainstAll120_4PlayerPointSystem;
import de.olivergeisel.kegelplay.core.point_system.PairPlayerAgainstPointSystem;
import de.olivergeisel.kegelplay.core.point_system.PointSystem;
import de.olivergeisel.kegelplay.core.point_system._2Teams120PointSystem;
import de.olivergeisel.kegelplay.core.team_and_player.Player;
import de.olivergeisel.kegelplay.core.team_and_player.Team;
import de.olivergeisel.kegelplay.infrastructure.data_reader.GeneralReader;
import de.olivergeisel.kegelplay.infrastructure.data_reader.KeglerheimGeneralReader;
import de.olivergeisel.kegelplay.infrastructure.data_reader.MatchNTeams;
import de.olivergeisel.kegelplay.infrastructure.data_reader.UnsupportedMatchSchema;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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

import static java.lang.Math.max;
import static java.lang.Math.round;


/**
 * Controller for the selection of a game.
 * This controller is used to select a game from a list of games.
 * each {@link Match} will be displayed in a special view. By selecting a {@link PointSystem} the view show the
 * points for each {@link Player} or {@link Team} in the {@link Match}.
 *
 * @see Match
 * @see GameController
 * @see PointSystem
 *
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Oliver Geisel
 *
 */
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
	private ChoiceBox<String> screenSelect;
	@FXML
	private ChoiceBox<String> cssFile;
	@FXML
	private CheckBox          frameless;
	@FXML
	private CheckBox          onTop;
	@FXML
	private CheckBox          debugView;


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
		Stage stage;
		//stage.close();
		FXMLLoader fxmlLoader;
		var datePath = this.datePath;
		var dataReader = new KeglerheimGeneralReader(datePath.resolve(selectedGame), true);
		var kind = gameKind.getValue();
		var selectedView = view.getValue();
		var systemSelect = pointSystemField.getValue();
		Match<?> match = loadMatch(dataReader);
		var title = loadPointSystemToMatch(systemSelect, match);
		GameController controller;
		var result = selectController(selectedView, match);
		controller = result.controller;
		fxmlLoader = result.loader;
		if (debugView.isSelected()) {
			stage = loadDebugScene(title, fxmlLoader, controller);
		} else {
			stage = loadScene(fxmlLoader, controller);
			stage.setTitle(title);
		}
		//  Todo check if view is not wrong for the match
		var scene = stage.getScene();
		var stackPane = (Pane) scene.getRoot();
		scene.getStylesheets().add(STR."file:css/\{cssFile.getValue()}");
		stage.setResizable(true);
		var image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/kegeln.png")));
		stage.getIcons().add(image);
		stage.setHeight(max(stackPane.getPrefHeight(), 300));
		stage.setWidth(max(stackPane.getPrefWidth(), 300));
		stage.setMinHeight(max(stackPane.getMinHeight(), 100));
		stage.setMinWidth(max(stackPane.getMinWidth(), 100));
		stage.initStyle(StageStyle.DECORATED);
		stage.centerOnScreen();
		stage.setScene(scene);
		// set screen for the new window
		var screens = Screen.getScreens();
		var screenIndex = Integer.parseInt(screenSelect.getValue().split(" ")[1]) - 1;
		var selectedScreen = screens.get(screenIndex);
		Rectangle2D bounds = selectedScreen.getBounds();
		// Setzen der Position und Größe des neuen Fensters basierend auf dem zweiten Bildschirm
		stage.setX(bounds.getMinX() + (bounds.getWidth() - 400) / 2); // 400 ist die Breite des neuen Fensters
		stage.setY(bounds.getMinY() + (bounds.getHeight() - 300) / 2); // 300 ist die Höhe des neuen Fensters
		// Max values depending on Screen
		stage.setMaxWidth(max(selectedScreen.getBounds().getWidth() + 20, 500));
		stage.setMaxHeight(max(selectedScreen.getBounds().getHeight(), 500));
		if (frameless.isSelected()) {
			stage.setFullScreen(true);
		} else {
			scene.widthProperty().addListener((observableValue, oldWidth, newWidth) -> {
				double newHeight = round(newWidth.doubleValue() / ASPECT_RATIO);
				stage.setHeight(newHeight);
			});
		}
		stage.setAlwaysOnTop(onTop.isSelected());
		stage.show();
	}

	private Match<?> loadMatch(GeneralReader dataReader) {
		try {
			return dataReader.initNewMatch();
		} catch (UnsupportedMatchSchema e) {
			LOGGER.log(System.Logger.Level.ERROR, e.getMessage());
			return null;
		}
	}

	private String loadPointSystemToMatch(String systemSelect, Match<?> match) {
		var title = "";
		PointSystem pointSystem = switch (systemSelect) {
			case "4 Spieler gegeneinander" -> {
				title = "4 gegeneinander Satzpunkte";
				yield new AllAgainstAll120_4PlayerPointSystem();
			}
			case "2 Teams paarweise" -> {
				title = "2 Mannschaften mit Satzpunkten";
				yield new _2Teams120PointSystem();
			}
			case "Paarweise gegeneinander" -> {
				title = "Paarweise gegeneinander";
				yield new PairPlayerAgainstPointSystem();
			}
			case "Teams summe" -> {
				title = "Teams summe";
				yield new PairPlayerAgainstPointSystem();
			}
			default -> throw new IllegalStateException(STR."Unexpected value: \{systemSelect}");
		};
		match.setPointSystem(pointSystem);
		return title;
	}

	private Stage loadScene(FXMLLoader fxmlLoader, GameController<? extends Game> controller) {
		fxmlLoader.setController(controller);
		Pane stackPane;
		try {
			stackPane = fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		var scene = new Scene(stackPane);
		var stage = new Stage();
		stage.setScene(scene);
		return stage;
	}

	private ControllerAndLoader selectController(String selectedView, Match match) {
		FXMLLoader loader = null;
		var controller = switch (selectedView) {
			case "4 gegeneinander" -> {
				if (match instanceof Match1Team<?> match1Team) {
					loader = new FXMLLoader(getClass().getResource("display-game.fxml"));
					yield new _4PlayersAllAgainstController<>(match1Team);
				} else {
					throw new IllegalStateException(
							STR."Unexpected value: \{match} is not matching Matchtpe for this view");
				}
			}
			case "2 Teams" -> {
				loader = new FXMLLoader(getClass().getResource("display-game.fxml"));
				yield new _2TeamsAgainstController<>(match);
			}
			case "Vorlauf-Endlauf" -> {
				if (match instanceof MatchNTeams match1Team) {
					loader = new FXMLLoader(getClass().getResource("vorlauf-endlauf.fxml"));
					yield new VorlaufEndlaufController(match1Team);
				} else {
					throw new IllegalStateException(
							STR."Unexpected value: \{match} is not matching Matchtpe for this view");
				}
			}
			case "Halbfinale" -> {
				if (match instanceof Match1Team match1Team) {
					loader = new FXMLLoader(getClass().getResource("semi-final.fxml"));
					yield new SemiFinalController(match1Team);
				} else {
					throw new IllegalStateException(
							STR."Unexpected value: \{match} is not matching Matchtpe for this view");
				}
			}
			case "N Teams" -> null;
			default -> throw new IllegalStateException(STR."Unexpected value: \{selectedView} is not a valid view");
		};
		return new ControllerAndLoader(controller, loader);
	}

	private Stage loadDebugScene(String title, FXMLLoader fxmlLoader, GameController controller) {
		final var stage = new Stage();
		stage.setTitle(STR."\{title}-Debug");
		stage.setResizable(true);
		fxmlLoader.setController(controller);
		Pane stackPane = new Pane();
		stackPane.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if (event.isControlDown() && event.getCode().equals(KeyCode.R)) {
				reload(stage, fxmlLoader);
			}
		});
		try {
			stackPane.getChildren().add(fxmlLoader.load());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		var scene = new Scene(stackPane);
		stage.setScene(scene);
		stage.show();
		return stage;
	}

	private void reload(Stage stage, FXMLLoader fxmlLoader) {
		var oldScene = stage.getScene();
		try {
			var newPane = new Pane(fxmlLoader.load());
			newPane.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
				if (event.isControlDown() && event.getCode().equals(KeyCode.R)) {
					reload(stage, fxmlLoader);
				}
			});
			var newScene = new Scene(newPane);
			newScene.getStylesheets().addAll(oldScene.getStylesheets());
			stage.setScene(newScene);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
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
		var views = List.of("4 gegeneinander", "Vorlauf-Endlauf", "Halbfinale", "2 Teams", "N Teams");
		view.getItems().addAll(views);
		// pointSystem
		var pointSystems =
				List.of("4 Spieler gegeneinander", "2 Teams paarweise", "Teams summe", "Paarweise gegeneinander");
		pointSystemField.getItems().addAll(pointSystems);
		var screens = Screen.getScreens();
		for (int i = 0; i < screens.size(); i++) {
			screenSelect.getItems().add(STR."Screen \{i + 1}");
		}
		screenSelect.setValue(STR."Screen 1");
		File cssFolder = new File("css");
		var cssFiles = cssFolder.listFiles();
		for (var css : cssFiles) {
			cssFile.getItems().add(css.getName());
		}
		cssFile.setValue(cssFiles[0].getName());

	}

	private record ControllerAndLoader(GameController controller, FXMLLoader loader) {
	}
}
