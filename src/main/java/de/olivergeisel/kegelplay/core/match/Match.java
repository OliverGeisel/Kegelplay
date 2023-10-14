package de.olivergeisel.kegelplay.core.match;

import de.olivergeisel.kegelplay.core.team_and_player.Team;

public abstract class Match {

	private final MatchConfig config;
	private       MatchState  state;


	protected Match(MatchConfig config) {
		this.config = config;
		state = new NotStarted();
	}

	private void init() {
		state = new Preparing();
	}

	//region setter/getter
	public MatchConfig getConfig() {
		return config;
	}

	public abstract Team[] getTeams();

	private String getState() {
		return state.name;
	}
//endregion


	private class MatchState {

		protected final String name;


		private MatchState(String name) {this.name = name;}
	}

	private class NotStarted extends MatchState {


		private NotStarted() {
			super("Not started");
		}
	}

	private class Preparing extends MatchState {


		private Preparing() {
			super("Preparing");
		}
	}

	private class Changing extends MatchState {

		private Changing() {
			super("Changing");
		}
	}

	private class Running extends MatchState {


		private Running() {
			super("Running");
		}
	}

	private class Finished extends MatchState {
		private Finished() {
			super("Finished");
		}
	}
}
