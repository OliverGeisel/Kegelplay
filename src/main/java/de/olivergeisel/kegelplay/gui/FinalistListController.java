package de.olivergeisel.kegelplay.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class FinalistListController implements Initializable {

	@FXML
	private VBox finalistList;
	@FXML
	private VBox root;


	public void clearFinalist(){
		finalistList.getChildren().clear();
	}

	public void addFinalist(String fornname, String surname, int score) {
		var displayString = STR."\{fornname} \{surname} - \{score}";
		if (finalistList.getChildren().stream().noneMatch(node -> {
			if (node instanceof HBox box) {
				var children = box.getChildren();
				if (!children.isEmpty()) {
					var firstChild = children.getFirst();
					if (firstChild instanceof Label label) {
						return label.getText().equals(displayString);
					}
				}
			}
			return false;
		})) {
			HBox playerLine = new HBox();
			var children = playerLine.getChildren();
			children.add(new Label(STR."\{fornname} \{surname}"));
			finalistList.getChildren().add(playerLine);
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
		root.getProperties().put(FXMLLoader.CONTROLLER_KEYWORD, this);
		finalistList.getProperties().put(FXMLLoader.CONTROLLER_KEYWORD, this);
	}
}
