package de.olivergeisel.kegelplay.core.team_and_player;

import de.olivergeisel.kegelplay.core.game.Game;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A team in a match.
 * A team consists of players and substitutes.
 */
public abstract class Team<G extends Game> {

	private final Map<Player<G>, Player<G>> substitutesMap = new HashMap<>();
	private       String                    name;
	private       Player<G>[]               players;
	private       Player<G>[]               substitutes;
	private       int                       numberOfPlayers;
	private       int                       numberOfSubstitutes;
	private       GeneralTeamInfo           generalTeamInfo;

	protected Team(String name, Player<G>[] players, Player<G>[] substitutes) {
		this.name = name;
		this.players = players;
		this.substitutes = substitutes;

	}

	protected Team(String name, Player<G>[] players, Player<G>[] substitutes, int numberOfPlayers,
			int numberOfSubstitutes) {
		this.name = name;
		this.players = players;
		this.substitutes = substitutes;
		this.numberOfPlayers = numberOfPlayers;
		this.numberOfSubstitutes = numberOfSubstitutes;
		if (players.length != numberOfPlayers) {
			throw new IllegalArgumentException(
					STR."A team with \{numberOfPlayers} players must have \{numberOfPlayers} players.");
		}
		if (substitutes.length != numberOfSubstitutes) {
			throw new IllegalArgumentException(
					STR."A team with \{numberOfSubstitutes} substitutes must have \{numberOfSubstitutes} substitutes.");
		}
	}

	public Player<G> getPlayer(int index) throws IllegalArgumentException {
		try {
			return players[index];
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException(STR."Index out of bounds: \{index}", e);
		}
	}

	public Player<G> getPlayer(String name) throws NoPlayerFoundException {
		for (Player<G> player : players) {
			if (player.getCompleteName().equals(name)) {
				return player;
			}
		}
		throw new NoPlayerFoundException();
	}

	public Player<G> getSubstitute(int index) {
		return substitutes[index];
	}

	public void setPlayer(int index, Player<G> player) {
		players[index] = player;
	}

	public void setSubstitute(int index, Player<G> substitute) {
		substitutes[index] = substitute;
	}

	public void substitute(int playerIndex, int substituteIndex) throws IllegalArgumentException {
		if (playerIndex < 0 || playerIndex >= players.length) {
			throw new IllegalArgumentException(STR."playerIndex out of bounds: \{playerIndex}");
		}
		if (substituteIndex < 0 || substituteIndex >= substitutes.length) {
			throw new IllegalArgumentException(STR."substituteIndex out of bounds: \{substituteIndex}");
		}
		Player<G> player = players[playerIndex];
		Player<G> substitute = substitutes[substituteIndex];
		players[playerIndex] = substitute;
		substitutes[substituteIndex] = player;
		substitutesMap.put(player, substitute);
	}

	//region setter/getter
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Player<G>[] getPlayers() {
		return players;
	}

	public void setPlayers(Player<G>[] players) {
		this.players = players;
	}

	public Player<G>[] getSubstitutes() {
		return substitutes;
	}

	public void setSubstitutes(Player<G>[] substitutes) {
		this.substitutes = substitutes;
	}

	public Map<Player<G>, Player<G>> getSubstitutesMap() {
		return Collections.unmodifiableMap(substitutesMap);
	}

	public int getNumberOfPlayers() {
		return players.length;
	}

	public int getNumberOfSubstitutes() {
		return substitutes.length;
	}
//endregion


	record Substitute(Player player, Player substitute, int positionTeam, int durchgangNum, int wurfNum) {
		public Substitute(Player player, Player substitute, int positionTeam, int durchgangNum) {
			this(player, substitute, positionTeam, durchgangNum, 0);
		}

	}

}
