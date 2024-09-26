package de.olivergeisel.kegelplay.gui;

import core.game.Wurfbild;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Controller for displaying the fall image(Fallbild) of a throw in the 9-Pin bowling game.
 */
public class FallbildController implements InvalidationListener, Initializable {

	private static final double PIN_WIDTH_RATIO = 8.5 / 100.;

	private static final double SQRT_2 = Math.sqrt(2);

	private WurfbildSubscribe wurfbild;

	@FXML
	private AnchorPane anchorPane;
	@FXML
	private Rectangle  square;
	@FXML
	private Circle     pin1;
	@FXML
	private Circle     pin2;
	@FXML
	private Circle     pin3;
	@FXML
	private Circle     pin4;
	@FXML
	private Circle     pin5;
	@FXML
	private Circle     pin6;
	@FXML
	private Circle     pin7;
	@FXML
	private Circle     pin8;
	@FXML
	private Circle     pin9;

	private void updatePinToHit(Circle pin) {
		updatePinTo(pin, "pin-hit");
	}

	private void updatePinToMiss(Circle pin) {
		updatePinTo(pin, "pin-stand");
	}

	private void updatePinTo(Circle pin, String className) {
		pin.getStyleClass().clear();
		pin.getStyleClass().add(className);
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
		setWurfbild(new Wurfbild(0));
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

		pin1.getProperties().put(FXMLLoader.CONTROLLER_KEYWORD, this);
		pin2.getProperties().put(FXMLLoader.CONTROLLER_KEYWORD, this);
		pin3.getProperties().put(FXMLLoader.CONTROLLER_KEYWORD, this);
		pin4.getProperties().put(FXMLLoader.CONTROLLER_KEYWORD, this);
		pin5.getProperties().put(FXMLLoader.CONTROLLER_KEYWORD, this);
		pin6.getProperties().put(FXMLLoader.CONTROLLER_KEYWORD, this);
		pin7.getProperties().put(FXMLLoader.CONTROLLER_KEYWORD, this);
		pin8.getProperties().put(FXMLLoader.CONTROLLER_KEYWORD, this);
		pin9.getProperties().put(FXMLLoader.CONTROLLER_KEYWORD, this);
		anchorPane.getProperties().put(FXMLLoader.CONTROLLER_KEYWORD, this);

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

	//region setter/getter
	public void setWurfbild(WurfbildSubscribe wurfbild) {
		this.wurfbild = wurfbild;
		this.wurfbild.one.addListener((od, n, newValue) -> {
			if (newValue) updatePinToHit(pin1);
			else updatePinToMiss(pin1);
		});
		this.wurfbild.two.addListener(this);
		this.wurfbild.three.addListener(this);
		this.wurfbild.four.addListener(this);
		this.wurfbild.five.addListener(this);
		this.wurfbild.six.addListener(this);
		this.wurfbild.seven.addListener(this);
		this.wurfbild.eight.addListener(this);
		this.wurfbild.nine.addListener(this);
	}

	public void setWurfbild(Wurfbild wurfbild) {
		if (wurfbild.get(1)) {
			updatePinToHit(pin1);
		} else {
			updatePinToMiss(pin1);
		}
		if (wurfbild.get(2)) {
			updatePinToHit(pin2);
		} else {
			updatePinToMiss(pin2);
		}
		if (wurfbild.get(3)) {
			updatePinToHit(pin3);
		} else {
			updatePinToMiss(pin3);
		}
		if (wurfbild.get(4)) {
			updatePinToHit(pin4);
		} else {
			updatePinToMiss(pin4);
		}
		if (wurfbild.get(5)) {
			updatePinToHit(pin5);
		} else {
			updatePinToMiss(pin5);
		}
		if (wurfbild.get(6)) {
			updatePinToHit(pin6);
		} else {
			updatePinToMiss(pin6);
		}
		if (wurfbild.get(7)) {
			updatePinToHit(pin7);
		} else {
			updatePinToMiss(pin7);
		}
		if (wurfbild.get(8)) {
			updatePinToHit(pin8);
		} else {
			updatePinToMiss(pin8);
		}
		if (wurfbild.get(9)) {
			updatePinToHit(pin9);
		} else {
			updatePinToMiss(pin9);
		}
	}
//endregion
}

class WurfbildSubscribe implements InvalidationListener {
	BooleanProperty one;
	BooleanProperty two;
	BooleanProperty three;
	BooleanProperty four;
	BooleanProperty five;
	BooleanProperty six;
	BooleanProperty seven;
	BooleanProperty eight;
	BooleanProperty nine;

	public WurfbildSubscribe() {
		this.one = new SimpleBooleanProperty();
		this.two = new SimpleBooleanProperty();
		this.three = new SimpleBooleanProperty();
		this.four = new SimpleBooleanProperty();
		this.five = new SimpleBooleanProperty();
		this.six = new SimpleBooleanProperty();
		this.seven = new SimpleBooleanProperty();
		this.eight = new SimpleBooleanProperty();
		this.nine = new SimpleBooleanProperty();
	}

	public void update(Wurfbild wurfbild) {
		this.one.set(wurfbild.get(1));
		this.two.set(wurfbild.get(2));
		this.three.set(wurfbild.get(3));
		this.four.set(wurfbild.get(4));
		this.five.set(wurfbild.get(5));
		this.six.set(wurfbild.get(6));
		this.seven.set(wurfbild.get(7));
		this.eight.set(wurfbild.get(8));
		this.nine.set(wurfbild.get(9));
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
