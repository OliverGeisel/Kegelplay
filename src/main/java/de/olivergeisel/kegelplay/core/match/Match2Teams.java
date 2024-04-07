package de.olivergeisel.kegelplay.core.match;

import de.olivergeisel.kegelplay.core.game.Game;
import de.olivergeisel.kegelplay.core.point_system.PointSystem;
import de.olivergeisel.kegelplay.core.team_and_player.Player;
import de.olivergeisel.kegelplay.core.team_and_player.Team;

import java.nio.file.Path;

public class Match2Teams<G extends Game> extends Match<G> {

	private final Team<G> home;
	private final Team<G> guest;


	public Match2Teams(MatchConfig config, GeneralMatchInfo generalMatchInfo, MatchStatusInfo statusInfo,
			PointSystem<G> pointSystem, Team<G> home,
			Team<G> guest, Path path) {
		super(config, generalMatchInfo, statusInfo, pointSystem, path);
		this.home = home;
		this.guest = guest;
	}

	public Player<G> getHomePlayerByName(String name) {
		return home.getPlayer(name);
	}

	public Player<G> getGuestPlayerByName(String name) {
		return guest.getPlayer(name);
	}

	public Player<G> getHomePlayerByPosition(int position) {
		return home.getPlayer(position);
	}

	public Player<G> getGuestPlayerByPosition(int position) {
		return guest.getPlayer(position);
	}

	//region setter/getter
	public Team<G> getGuest() {
		return guest;
	}

	public Team<G> getHome() {
		return home;
	}

	@Override
	public Team<G>[] getTeams() {
		return new Team[]{home, guest};
	}
//endregion
}
