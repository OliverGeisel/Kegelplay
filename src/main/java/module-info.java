module de.olivergeisel.kegelplay {
	requires javafx.controls;
	requires javafx.fxml;
	requires org.controlsfx.controls;
	requires org.kordamp.ikonli.javafx;
	requires org.kordamp.bootstrapfx.core;
	requires com.opencsv;
	requires com.fasterxml.jackson.databind;

	exports de.olivergeisel.kegelplay;
	exports de.olivergeisel.kegelplay.infrastructure.csv;
	exports de.olivergeisel.kegelplay.gui;
	exports de.olivergeisel.kegelplay.core.match;
	opens de.olivergeisel.kegelplay.infrastructure.csv to javafx.fxml;
	opens de.olivergeisel.kegelplay to javafx.fxml;
	opens de.olivergeisel.kegelplay.gui to javafx.fxml;
	exports de.olivergeisel.kegelplay.core.game;
}