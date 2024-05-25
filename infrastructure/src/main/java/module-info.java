/**
 * Module for infrastructure in Kegelplay.
 */
module de.kegelplay.infrastructure {
	requires de.kegelplay.core;
	requires com.opencsv;
	requires com.fasterxml.jackson.databind;

	exports de.kegelplay.infrastructure.data_reader;
	exports de.kegelplay.infrastructure.csv;
	exports de.kegelplay.infrastructure.ini;
	exports de.kegelplay.infrastructure.update;

}