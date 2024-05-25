module kegelplay {

	requires de.kegelplay.core;
	requires de.kegelplay.infrastructure;

	requires javafx.controls;
	requires javafx.fxml;
	requires org.controlsfx.controls;
	requires org.kordamp.ikonli.javafx;
	requires org.kordamp.bootstrapfx.core;
	requires com.opencsv;
	requires com.fasterxml.jackson.databind;


	exports de.olivergeisel.kegelplay.gui;
	opens de.olivergeisel.kegelplay to javafx.fxml, javafx.graphics;
	opens de.olivergeisel.kegelplay.gui to javafx.fxml;
}