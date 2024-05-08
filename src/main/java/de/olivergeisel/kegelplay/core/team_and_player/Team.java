package de.olivergeisel.kegelplay.core.team_and_player;

import de.olivergeisel.kegelplay.core.game.Game;
import de.olivergeisel.kegelplay.core.match.Match;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A team in a {@link Match}. This is the basic class for a team in a {@link Match}.
 * A team consists of active {@link Player}s and the substitutes ({@link Player} too). Every general information about
 * the team is stored in a {@link GeneralTeamInfo} object. It can be accessed by the {@link #getGeneralTeamInfo()}.
 *
 * @param <G> the type of the {@link Game}
 * @see Player
 * @see Game
 * @see GeneralTeamInfo
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Oliver Geisel
 */
public abstract class Team<G extends Game> {

	private final Map<Player<G>, Player<G>> substitutesMap = new HashMap<>();
	private       String                    name;
	private       Player<G>[]               players;
	private       Player<G>[]               substitutes;
	private       GeneralTeamInfo           generalTeamInfo;

	protected Team(String name, GeneralTeamInfo teamInfo, Player<G>[] players, Player<G>[] substitutes) {
		this.name = name;
		this.players = players;
		this.substitutes = substitutes;
		this.generalTeamInfo = teamInfo;

	}

	protected Team(String name, Player<G>[] players, Player<G>[] substitutes, GeneralTeamInfo teamInfo,
			int numberOfPlayers, int numberOfSubstitutes) {
		this.name = name;
		this.players = players;
		this.substitutes = substitutes;
		this.generalTeamInfo = teamInfo;
		if (players.length != numberOfPlayers) {
			throw new IllegalArgumentException(
					STR."A team with \{numberOfPlayers} players must have \{numberOfPlayers} players.");
		}
		if (substitutes.length != numberOfSubstitutes) {
			throw new IllegalArgumentException(
					STR."A team with \{numberOfSubstitutes} substitutes must have \{numberOfSubstitutes} substitutes.");
		}
	}

	/**
	 * Returns the player with the given index.
	 *
	 * @param index the index of the player
	 * @return the player with the given index
	 * @throws IllegalArgumentException if the index is out of bounds
	 */
	public Player<G> getPlayer(int index) throws IllegalArgumentException {
		try {
			return players[index];
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException(STR."Index out of bounds: \{index}", e);
		}
	}

	/**
	 * Returns the player with the given name.
	 *
	 * @param name the name of the player. The name must be the complete name with commata.
	 * @return the player with the given name
	 * @throws NoPlayerFoundException if no player with the given name is found
	 */
	public Player<G> getPlayer(String name) throws NoPlayerFoundException {
		for (Player<G> player : players) {
			if (player == null) {
				continue; // todo maybe throw exception or assure not possible
			}
			if (player.getCompleteNameWithCommata().equals(name)) {
				return player;
			}
		}
		throw new NoPlayerFoundException();
	}

	/**
	 * Returns the substitute with the given index.
	 *
	 * @param index the index of the substitute
	 * @return the substitute with the given index
	 * @throws IllegalArgumentException if the index is out of bounds
	 */
	public Player<G> getSubstitute(int index) throws IllegalArgumentException {
		if (index < 0 || index >= substitutes.length) {
			throw new IllegalArgumentException(STR."Index out of bounds: \{index}");
		}
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

	/**
	 * Returns all information in the form of a {@link GeneralTeamInfo} for the team.
	 * @return the {@link GeneralTeamInfo} of the team
	 */
	public GeneralTeamInfo getGeneralTeamInfo() {
		return generalTeamInfo;
	}

	public int getTeamScore() {
		int score = 0;
		for (Player<G> player : players) {
			score += player.getGame().getTotalScore();
		}
		return score;
	}

	public int getTeamTotalMissThrow() {
		int score = 0;
		for (Player<G> player : players) {
			score += player.getGame().getTotalFehlwurf();
		}
		return score;
	}

	public int getTeamTotalVolle() {
		int score = 0;
		for (Player<G> player : players) {
			score += player.getGame().getTotalVolle();
		}
		return score;
	}

	public int getTeamTotalAbraeumen() {
		int score = 0;
		for (Player<G> player : players) {
			score += player.getGame().getTotalAbraeumen();
		}
		return score;
	}


	public int getTeamTotalThrows() {
		int score = 0;
		for (Player<G> player : players) {
			score += player.getGame().getNumberOfWurf();
		}
		return score;
	}


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
