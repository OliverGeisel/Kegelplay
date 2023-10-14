package de.olivergeisel.kegelplay.gui;

import de.olivergeisel.kegelplay.core.match.Match;
import de.olivergeisel.kegelplay.core.match.Match1Team;
import de.olivergeisel.kegelplay.infrastructure.csv.CsvService;
import de.olivergeisel.kegelplay.infrastructure.data_reader.KeglerheimGeneralReader;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Timer;
import java.util.TimerTask;

public class DisplayGameController {

	private static final long                    REFRESH_INTERVAL = 2 * 5 * 1_000L; // 10 Sekunden in Millisekunden
	private              Stage                   stage;
	private              Match                   match;
	private              KeglerheimGeneralReader dataReader;
	private              int                     count            = 0;
	private              CsvService              csvService;

	@FXML
	private Label showLabel;


	public DisplayGameController() {
		this.csvService = new CsvService();
	}

	private void initMatch() {
		match = new Match1Team();
		var today = LocalDate.now();
		//var dateString = today.format(DateTimeFormatter.ofPattern(KeglerheimGeneralReader.TODAY_FORMAT));
		var path = Paths.get("Blockstart 120 Wurf 6Sp 2M_000");
		dataReader = new KeglerheimGeneralReader(path, true);
		dataReader.updateMatch(match);
	}


	public void initialize() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(() -> {
					//showLabel.setText("Count: " + count);
					count++;
					csvService.update();
				});
			}
		}, 0, REFRESH_INTERVAL);
	}

	@FXML
	protected void onCloseInMenu(ActionEvent actionEvent) {
		var oldScene = showLabel.getScene();
		var stage = (Stage) oldScene.getWindow();
		stage.close();
	}

	@FXML
	public void onAbout(ActionEvent actionEvent) {}

	//region setter/getter
	public void setStage(Stage stage) {
		this.stage = stage;
		/*
		try {
			stage.getScene().heightProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observableValue, Number oldHeight,
						Number newHeight) {
					// Ändern Sie die Schriftgröße aller Textelemente basierend auf der Fenstergröße
					double fontSize = newHeight.doubleValue() / 20.0; // Sie können den Faktor entsprechend anpassen
					Font newFont = new Font(fontSize);
					showLabel.setFont(newFont);
					/*label.setFont(newFont);
					button.setFont(newFont);
					textArea.setFont(newFont);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}*/

	}
//endregion
}
