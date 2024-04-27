package de.olivergeisel.kegelplay.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.olivergeisel.kegelplay.core.game.Game120;
import de.olivergeisel.kegelplay.core.match.Match;
import de.olivergeisel.kegelplay.infrastructure.data_reader.KeglerheimGeneralReader;
import de.olivergeisel.kegelplay.infrastructure.data_reader.UnsupportedMatchSchema;
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
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

public class SelectGameSpecialController implements Initializable {

	private static final double        ASPECT_RATIO = 16.0 / 9; // Gewünschtes Seitenverhältnis (z. B. 16:9)
	private static final System.Logger LOGGER       = System.getLogger(SelectGameSpecialController.class.getName());


	private Path datePath;

	@FXML
	private VBox              vBox;
	@FXML
	private DatePicker        datePicker;
	@FXML
	private ChoiceBox<String> screenSelect;
	@FXML
	private ChoiceBox<String> cssFile;
	@FXML
	private CheckBox          frameless;
	@FXML
	private CheckBox          onTop;
	@FXML
	private ChoiceBox<String> match1Select;
	@FXML
	private ChoiceBox<String> match2Select;
	@FXML
	private Button            loadButton;


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
		loadButton.setDisable(true);
	}

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

	@FXML
	public void updateDisable() {
		var val1 = match1Select.getValue();
		var val2 = match2Select.getValue();
		var res = val1 == null || val2 == null
				  || val1.isBlank() || val2.isBlank() || val1.equals(val2);
		loadButton.setDisable(res);
	}

	private void showGames(String[] matches) {
		vBox.getChildren().clear();
		var list1 = match1Select.getItems();
		var list2 = match2Select.getItems();
		list1.clear();
		list2.clear();
		for (var game : matches) {
			var button = new Button(game);
			vBox.getChildren().add(button);
			list1.add(game);
			list2.add(game);
		}
	}

	@FXML
	public void loadSelectedGames(ActionEvent event) {
		if (match1Select.getValue().isBlank() || match2Select.getValue().isBlank()) {
			return;
		}
		var selectedGame1 = match1Select.getValue();
		var selectedGame2 = match2Select.getValue();
		var oldScene = match1Select.getScene();
		var stage = (Stage) oldScene.getWindow();
		//stage.close();
		stage = new Stage();
		var fxmlLoader = new FXMLLoader(getClass().getResource("team-overview-ranking.fxml"));
		var datePath = this.datePath;
		var dataReader1 = new KeglerheimGeneralReader(datePath.resolve(selectedGame1), true);
		var dataReader2 = new KeglerheimGeneralReader(datePath.resolve(selectedGame2), true);
		var title = "";
		Match<Game120> match1;
		try {
			match1 = dataReader1.initNewMatch();
		} catch (UnsupportedMatchSchema e) {
			e.printStackTrace();
			return;
		}
		Match<Game120> match2;
		try {
			match2 = dataReader2.initNewMatch();
		} catch (UnsupportedMatchSchema e) {
			e.printStackTrace();
			return;
		}
		TeamOverviewRankingController controller = new TeamOverviewRankingController(match1, match2);
		fxmlLoader.setController(controller);
		Pane stackPane;
		try {
			stackPane = fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		var scene = new Scene(stackPane);
		scene.getStylesheets().add(STR."file:css/\{cssFile.getValue()}");
		stage.setTitle(title);
		stage.setResizable(true);
		var image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/kegeln.png")));
		stage.getIcons().add(image);
		stage.setHeight(stackPane.getPrefHeight());
		stage.setWidth(stackPane.getPrefWidth());
		stage.setMinHeight(stackPane.getMinHeight());
		stage.setMinWidth(stackPane.getMinWidth());
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


		stage.setMaxWidth(selectedScreen.getBounds().getWidth() + 20);
		stage.setMaxHeight(selectedScreen.getBounds().getHeight());
		stage.show();
		if (frameless.isSelected()) {
			stage.setFullScreen(true);
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
		var screens = Screen.getScreens();
		for (int i = 0; i < screens.size(); i++) {
			screenSelect.getItems().add(STR."Screen \{i + 1}");
		}
		screenSelect.setValue("Screen 1");
		File cssFolder = new File("css");
		var cssFiles = cssFolder.listFiles();
		if (cssFiles == null) {
			return;
		}
		for (var css : cssFiles) {
			cssFile.getItems().add(css.getName());
		}
		cssFile.setValue(cssFiles[0].getName());
		match1Select.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			updateDisable();
		});
		match2Select.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			updateDisable();
		});
	}
}
