module de.olivergeisel.kegelplay {
	requires javafx.controls;
	requires javafx.fxml;

	requires org.controlsfx.controls;
	requires org.kordamp.ikonli.javafx;
	requires org.kordamp.bootstrapfx.core;
	requires com.opencsv;

	opens de.olivergeisel.kegelplay to javafx.fxml;
	exports de.olivergeisel.kegelplay;
	exports de.olivergeisel.kegelplay.infrastructure.csv;
	opens de.olivergeisel.kegelplay.infrastructure.csv to javafx.fxml;
	exports de.olivergeisel.kegelplay.gui;
	opens de.olivergeisel.kegelplay.gui to javafx.fxml;
}