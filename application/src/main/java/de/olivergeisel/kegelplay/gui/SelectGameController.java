package de.olivergeisel.kegelplay.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.game.Game;
import core.game.GameKind;
import core.match.Match;
import core.match.Match1Team;
import core.point_system.AllAgainstAll120_4PlayerPointSystem;
import core.point_system.PairPlayerAgainstPointSystem;
import core.point_system.PointSystem;
import core.point_system._2Teams120PointSystem;
import de.kegelplay.infrastructure.data_reader.KeglerheimGeneralReader;
import de.kegelplay.infrastructure.data_reader.UnsupportedMatchSchema;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
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
import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.round;

/**
 * Controller for selecting a match to display.
 * <p>
 * Select the match to display and the view to display it.
 * The default Aspect ratio is 16:9 for all Match-views.
 * </p>
 *
 * @author Oliver Geisel
 * @version 1.0.0
 * @since 1.0.0
 */
public class SelectGameController implements Initializable {

	private static final double        ASPECT_RATIO  = 16.0 / 9; // Gewünschtes Seitenverhältnis (z. B. 16:9)
	private static final System.Logger LOGGER        = System.getLogger(SelectGameController.class.getName());
	private static final List<String>  VIEWS         = List.of("4 gegeneinander", "2 Teams", "N Teams");
	private static final List<String>  POINT_SYSTEMS =
			List.of("4 Spieler gegeneinander", "2 Teams paarweise", "Teams summe", "Paarweise gegeneinander");

	private static final Map<String, String> VIEW_INFO = Map.of(
			"4 gegeneinander", "4 Spieler in einem Team, die alle gegeneinander spielen",
			"2 Teams", "2 Teams spielen gegeneinander in Paaren",
			"N Teams", "N Teams spielen gegeneinander. Die Punkte werden hier maßgeblich durch das Punktesystem "
					   + "bestimmt",
			"Halbfinale", "Hier spielen die Spieler aus einer Mannschaft paarweise gegeneinander"
	);

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

	/**
	 * Loads the games for today.
	 *
	 * @param event the event (Button etc.) that triggered the action
	 * @throws IOException if the games could not be loaded
	 */
	@FXML
	public void loadToday(ActionEvent event) throws IOException {
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

	/**
	 * Load the selected game. This starts the concrete view of the {@link Match}.
	 *
	 * @param event the event (Button etc.) that triggered the action
	 */
	@FXML
	public void loadSelectedGame(ActionEvent event) {
		var button = (Button) event.getSource();
		var selectedGame = button.getText();
		var oldScene = button.getScene();
		var stage = (Stage) oldScene.getWindow();
		// stage.close();
		stage = new Stage();
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
		GameController<? extends Game> controller = switch (selectedView) {
			case "4 gegeneinander" -> {
				if (match instanceof Match1Team match1Team) {
					fxmlLoader = new FXMLLoader(getClass().getResource("display-game.fxml"));
					yield new _4PlayersAllAgainstController<>(match1Team);
				} else {
					throw new IllegalStateException(STR."Unexpected value: \{match}");
				}
			}
			case "2 Teams" -> {
				fxmlLoader = new FXMLLoader(getClass().getResource("display-game.fxml"));
				yield new _2TeamsAgainstController<>(match);
			}
			case "Vorlauf-Endlauf" -> {
				if (match instanceof Match match1Team) {
					fxmlLoader = new FXMLLoader(getClass().getResource("vorlauf-endlauf.fxml"));
					yield new VorlaufEndlaufController(match1Team);
				} else {
					throw new IllegalStateException(STR."Unexpected value: \{match}");
				}
			}
			case "Halbfinale" -> {
				if (match instanceof Match1Team match1Team) {
					fxmlLoader = new FXMLLoader(getClass().getResource("semi-final.fxml"));
					yield new SemiFinalController(match1Team);
				} else {
					throw new IllegalStateException(STR."Unexpected value: \{match}");
				}
			}
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
		//  Todo check if view is not wrong for the match
		var scene = new Scene(stackPane);
		scene.getStylesheets().add(STR."file:css/\{cssFile.getValue()}");
		stage.setTitle(title);
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
		var screens = Screen.getScreens();
		var screenIndex = Integer.parseInt(screenSelect.getValue().split(" ")[1]) - 1;
		var selectedScreen = screens.get(screenIndex);
		Rectangle2D bounds = selectedScreen.getBounds();

		// Setzen der Position und Größe des neuen Fensters basierend auf dem zweiten Bildschirm
		stage.setX(bounds.getMinX() + (bounds.getWidth() - 400) / 2); // 400 ist die Breite des neuen Fensters
		stage.setY(bounds.getMinY() + (bounds.getHeight() - 300) / 2); // 300 ist die Höhe des neuen Fensters


		stage.setMaxWidth(max(selectedScreen.getBounds().getWidth() + 20, 20+1920));
		stage.setMaxHeight(max(selectedScreen.getBounds().getHeight(), 1080+20));
		stage.show();
		if (frameless.isSelected()) {
			stage.setFullScreen(true);
		} else {
			Stage finalStage = stage;
			scene.widthProperty().addListener((observableValue, oldWidth, newWidth) -> {
				double newHeight = round(newWidth.doubleValue() / ASPECT_RATIO);
				finalStage.setHeight(newHeight);
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
		view.getItems().addAll(VIEWS);
		// pointSystem
		pointSystemField.getItems().addAll(POINT_SYSTEMS);
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
}
