package core.match;

import core.game.Game;
import core.point_system.PointSystem;
import core.team_and_player.Player;
import core.team_and_player.Team;
import core.util.Pair;

import java.nio.file.Path;

/**
 * Represents a match between two teams.
 *
 * @param <G> The type of the {@link Game} that is played in this match.
 * @author Oliver Geisel
 * @version 1.0.0
 * @see Game
 * @since 1.0.0
 */
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

	public Pair<Player<G>, Player<G>> getPairByPosition(int position) {
		return new Pair<>(home.getPlayer(position), guest.getPlayer(position));
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
