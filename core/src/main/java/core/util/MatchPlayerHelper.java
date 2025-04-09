package core.util;

import core.game.Game;
import core.match.Match;
import core.team_and_player.Player;

import java.util.LinkedList;
import java.util.List;

public class MatchPlayerHelper {

	public static <G extends Game> List<Player<G>> getPlayersOrderedByStartPosition(Match<G> match) {
		var wks = match.getConfig().getSchema();
		var back = new LinkedList<Player<G>>();
		var teams = match.getTeams();
		var pos = wks.getStartPositions();
		for (var pair : pos) {
			var team = teams[pair.getFirst()];
			var players = team.getPlayers();
			back.add(players[pair.getSecond()]);
		}
		return back;
	}
}
