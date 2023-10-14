package de.olivergeisel.kegelplay.gui;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class FallbildController implements InvalidationListener {

	private static final double PIN_WIDTH_RATIO = 8.5 / 100.;

	private static final double     SQRT_2 = Math.sqrt(2);
	@FXML
	private              AnchorPane anchorPane;

	@FXML
	private Rectangle square;
	@FXML
	private Circle    pin1;
	@FXML
	private Circle    pin2;
	@FXML
	private Circle    pin3;
	@FXML
	private Circle    pin4;
	@FXML
	private Circle    pin5;
	@FXML
	private Circle    pin6;
	@FXML
	private Circle    pin7;
	@FXML
	private Circle    pin8;
	@FXML
	private Circle    pin9;

	public void initialize() {
		pin1.radiusProperty().bind(square.widthProperty().multiply(PIN_WIDTH_RATIO));
		pin2.radiusProperty().bind(square.widthProperty().multiply(PIN_WIDTH_RATIO));
		pin3.radiusProperty().bind(square.widthProperty().multiply(PIN_WIDTH_RATIO));
		pin4.radiusProperty().bind(square.widthProperty().multiply(PIN_WIDTH_RATIO));
		pin5.radiusProperty().bind(square.widthProperty().multiply(PIN_WIDTH_RATIO));
		pin6.radiusProperty().bind(square.widthProperty().multiply(PIN_WIDTH_RATIO));
		pin7.radiusProperty().bind(square.widthProperty().multiply(PIN_WIDTH_RATIO));
		pin8.radiusProperty().bind(square.widthProperty().multiply(PIN_WIDTH_RATIO));
		pin9.radiusProperty().bind(square.widthProperty().multiply(PIN_WIDTH_RATIO));

		pin1.centerXProperty().bind(anchorPane.widthProperty().divide(2));
		pin1.centerYProperty().bind(anchorPane.widthProperty().divide(2));


		anchorPane.widthProperty().addListener((obs, oldWidth, newWidth) -> {
			double size = Math.min(newWidth.doubleValue(), anchorPane.getWidth()); // Quadratgröße anpassen
			var realSize = Math.sqrt(size * size / 2);

			square.setWidth(realSize);
			square.setHeight(realSize);
			// Zentrieren Sie das Quadrat im AnchorPane
			AnchorPane.setLeftAnchor(square, anchorPane.getWidth() / 2 - square.getWidth() / 2);
			AnchorPane.setTopAnchor(square, (anchorPane.getWidth() - square.getWidth()) / 2);

			var pinSize = square.getWidth() * PIN_WIDTH_RATIO * 2;
			AnchorPane.setLeftAnchor(pin1, square.getWidth() / 2 + pinSize * 0.75);
			AnchorPane.setTopAnchor(pin1, anchorPane.getWidth() - pinSize * 2);

			AnchorPane.setLeftAnchor(pin2, 2.5 * pinSize);
			AnchorPane.setTopAnchor(pin2, anchorPane.getWidth() - 3.25 * pinSize);
			AnchorPane.setLeftAnchor(pin3, anchorPane.getWidth() - 3.1 * pinSize);
			AnchorPane.setTopAnchor(pin3, anchorPane.getWidth() - 3.25 * pinSize);

			AnchorPane.setLeftAnchor(pin4, pinSize);
			AnchorPane.setTopAnchor(pin4, anchorPane.getWidth() / 2 - pinSize * .5);
			AnchorPane.setLeftAnchor(pin5, square.getWidth() / 2 + pinSize * .75);
			AnchorPane.setTopAnchor(pin5, anchorPane.getWidth() / 2 - pinSize * .5);
			AnchorPane.setLeftAnchor(pin6, anchorPane.getWidth() - pinSize * 2);
			AnchorPane.setTopAnchor(pin6, anchorPane.getWidth() / 2 - pinSize * .5);

			AnchorPane.setLeftAnchor(pin7, 2.5 * pinSize);
			AnchorPane.setTopAnchor(pin7, 2.25 * pinSize);
			AnchorPane.setLeftAnchor(pin8, anchorPane.getWidth() - 3.1 * pinSize);
			AnchorPane.setTopAnchor(pin8, 2.25 * pinSize);

			AnchorPane.setLeftAnchor(pin9, square.getWidth() / 2 + pinSize * .75);
			AnchorPane.setTopAnchor(pin9, pinSize);
		});


	}

	/**
	 * This method needs to be provided by an implementation of
	 * {@code InvalidationListener}. It is called if an {@link Observable}
	 * becomes invalid.
	 * <p>
	 * In general, it is considered bad practice to modify the observed value in
	 * this method.
	 *
	 * @param observable The {@code Observable} that became invalid
	 */
	@Override
	public void invalidated(Observable observable) {

	}
}
