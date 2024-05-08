package de.olivergeisel.kegelplay;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class KegelplayApplication extends Application {
	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(KegelplayApplication.class.getResource("gui/hello-view.fxml"));
		Scene scene = new Scene(fxmlLoader.load(), 320, 240);
		stage.setTitle("Kegelplay");
		var visualBounds = Screen.getPrimary().getVisualBounds();
		stage.setMaxWidth(visualBounds.getWidth());
		stage.setMaxHeight(visualBounds.getHeight());
		stage.setOpacity(0.99);
		stage.setScene(scene);

		var image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/kegeln.png")));
		stage.getIcons().add(image);

		stage.show();
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		System.exit(0);
	}



	public static void main(String[] args) {
		launch();
	}
}