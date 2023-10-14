package de.olivergeisel.kegelplay.core.match;

import de.olivergeisel.kegelplay.core.team_and_player.Player;
import de.olivergeisel.kegelplay.core.team_and_player.Team;

public class Match2Teams extends Match {

	private final Team home;
	private final Team guest;


	public Match2Teams(MatchConfig config, Team home, Team guest) {
		super(config);
		this.home = home;
		this.guest = guest;
	}

	public Player getHomePlayerByName(String name) {
		return home.getPlayer(name);
	}

	public Player getGuestPlayerByName(String name) {
		return guest.getPlayer(name);
	}

	public Player getHomePlayerByPosition(int position) {
		return home.getPlayer(position);
	}

	public Player getGuestPlayerByPosition(int position) {
		return guest.getPlayer(position);
	}

	//region setter/getter
	public Team getGuest() {
		return guest;
	}

	public Team getHome() {
		return home;
	}

	@Override
	public Team[] getTeams() {
		return new Team[]{home, guest};
	}
//endregion
}
