package de.olivergeisel.kegelplay.core.match;

public class MatchConfig120AllAgainst extends MatchConfig {

	private static final int          MAX_PLAYERS   = 4;
	private static final int          MAX_TEAMS     = 1;
	private static final ChangeSchema CHANGE_SCHEMA = ChangeSchema.ALL_AGAINST;

	private static final int NUM_WURF    = 120;
	private static final int NUM_SAETZE  = 4;
	private static final int NUM_VOLLE   = 15;
	private static final int NUM_RAEUMER = 15;

	private static final Pointsystem POINTSYSTEM = new Pointsystem();
}
