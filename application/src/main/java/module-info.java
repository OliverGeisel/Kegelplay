/**
 * Module to provide the application layer of the KegelPlay application.
 *
 * @author Oliver Geisel
 * @since 1.0.0
 */
module de.kegelplay.application {

	requires de.kegelplay.core;
	requires de.kegelplay.infrastructure;

	requires javafx.controls;
	requires javafx.fxml;
	requires org.controlsfx.controls;
	requires org.kordamp.ikonli.javafx;
	requires org.kordamp.bootstrapfx.core;
	requires com.opencsv;
	requires com.fasterxml.jackson.databind;

	opens de.olivergeisel.kegelplay.gui to javafx.fxml, javafx.graphics;
	opens de.olivergeisel.kegelplay to javafx.fxml, javafx.graphics;

	exports de.olivergeisel.kegelplay;

}