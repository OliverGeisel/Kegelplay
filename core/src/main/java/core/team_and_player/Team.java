package core.team_and_player;

import core.game.Game;
import core.match.Match;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A team in a {@link Match}. This is the basic class for a team in a {@link Match}.
 * A team consists of active {@link Player}s and the substitutes ({@link Player} too). Every general information about
 * the team is stored in a {@link GeneralTeamInfo} object. It can be accessed by the {@link #getGeneralTeamInfo()}.
 *
 * @param <G> the type of the {@link Game}
 * @author Oliver Geisel
 * @version 1.0.0
 * @see Player
 * @see Game
 * @see GeneralTeamInfo
 * @since 1.0.0
 */
public abstract class Team<G extends Game> {

	private final Map<Player<G>, Player<G>> substitutesMap = new HashMap<>();
	private       String                    name;
	private       Player<G>[]               players;
	private       Player<G>[]               substitutes;
	private       GeneralTeamInfo           generalTeamInfo;

	/**
	 * Creates a new team.
	 *
	 * @param name        The name of the team.
	 * @param teamInfo    The general information about the team.
	 * @param players     The players of the team.
	 * @param substitutes The substitutes of the team.
	 */
	protected Team(String name, GeneralTeamInfo teamInfo, Player<G>[] players, Player<G>[] substitutes) {
		this.name = name;
		this.players = players;
		this.substitutes = substitutes;
		this.generalTeamInfo = teamInfo;

	}

	/**
	 * Creates a new team.
	 *
	 * @param name                The name of the team.
	 * @param players             The players of the team.
	 * @param substitutes         The substitutes of the team.
	 * @param teamInfo            The general information about the team.
	 * @param numberOfPlayers     The number of players in the team.
	 * @param numberOfSubstitutes The number of substitutes in the team.
	 */
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
	 *
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
	 *
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
	 *
	 * @throws IllegalArgumentException if the index is out of bounds
	 */
	public Player<G> getSubstitute(int index) throws IllegalArgumentException {
		if (index < 0 || index >= substitutes.length) {
			throw new IllegalArgumentException(STR."Index out of bounds: \{index}");
		}
		return substitutes[index];
	}

	/**
	 * Sets the player with the given index.
	 *
	 * @param index  the index of the player
	 * @param player the player to set
	 */
	public void setPlayer(int index, Player<G> player) {
		players[index] = player;
	}

	/**
	 * Sets the substitute with the given index.
	 *
	 * @param index      the index of the substitute
	 * @param substitute the substitute to set
	 */
	public void setSubstitute(int index, Player<G> substitute) {
		substitutes[index] = substitute;
	}

	/**
	 * Substitutes the player with the given index with the substitute with the given index.
	 *
	 * @param playerIndex     the index of the player
	 * @param substituteIndex the index of the substitute
	 *
	 * @throws IllegalArgumentException if the playerIndex or substituteIndex is out of bounds
	 */
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
	 *
	 * @return the {@link GeneralTeamInfo} of the team
	 */
	public GeneralTeamInfo getGeneralTeamInfo() {
		return generalTeamInfo;
	}

	/**
	 * Get the total score of the team. Is the sum of the total score of all players.
	 * @return total score of the team
	 */
	public int getTeamScore() {
		int score = 0;
		for (Player<G> player : players) {
			score += player.getGame().getTotalScore();
		}
		return score;
	}

	/**
	 * Get the total number of misses of the team. Is the sum of the total misses of all players.
	 * @return total number of misses of the team
	 */
	public int getTeamTotalMissThrow() {
		int score = 0;
		for (Player<G> player : players) {
			score += player.getGame().getTotalFehlwurf();
		}
		return score;
	}

	/**
	 * Get the total number of volle of the team. Is the sum of the total volle of all players.
	 * @return total number of volle of the team
	 */
	public int getTeamTotalVolle() {
		int score = 0;
		for (Player<G> player : players) {
			score += player.getGame().getTotalVolle();
		}
		return score;
	}

	/**
	 * Get the total number of abraeumen of the team. Is the sum of the total abraeumen of all players.
	 * @return total number of abraeumen of the team
	 */
	public int getTeamTotalAbraeumen() {
		int score = 0;
		for (Player<G> player : players) {
			score += player.getGame().getTotalAbraeumen();
		}
		return score;
	}

	/**
	 * Get the total number of throws of the team. Is the sum of the total throws of all players.
	 * @return total number of throws of the team
	 */
	public int getTeamTotalThrows() {
		int score = 0;
		for (Player<G> player : players) {
			score += player.getGame().getNumberOfWurf();
		}
		return score;
	}

	/**
	 * Get the name of the team.
	 * @return the name of the team
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the team.
	 * @param name the name of the team
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the players of the team.
	 * @return the players of the team
	 */
	public Player<G>[] getPlayers() {
		return players;
	}

	/**
	 * Set the players of the team.
	 * @param players the players of the team
	 */
	public void setPlayers(Player<G>[] players) {
		this.players = players;
	}

	/**
	 * Get the substitutes of the team.
	 * @return the substitutes of the team
	 */
	public Player<G>[] getSubstitutes() {
		return substitutes;
	}

	/**
	 * Set the substitutes of the team.
	 * @param substitutes the substitutes of the team
	 */
	public void setSubstitutes(Player<G>[] substitutes) {
		this.substitutes = substitutes;
	}

	/**
	 * Get the mapping of the substitutes. The key is the player and the value is the substitute.
	 * @return the mapping of the substitutes
	 */
	public Map<Player<G>, Player<G>> getSubstitutesMap() {
		return Collections.unmodifiableMap(substitutesMap);
	}

	/**
	 * Get the number of players in the team.
	 * @return the number of players in the team
	 */
	public int getNumberOfPlayers() {
		return players.length;
	}

	/**
	 * Get the number of substitutes in the team.
	 * @return the number of substitutes in the team
	 */
	public int getNumberOfSubstitutes() {
		return substitutes.length;
	}
//endregion


	/**
	 * A substitute of a player in a team. The substitute is a player too.
	 * @param player The player to substitute.
	 * @param substitute The substitute player.
	 * @param positionTeam The position of the team.
	 * @param durchgangNum The number of the Durchgang at which the substitution is made.
	 * @param wurfNum The number of the Wurf at which the substitution is made.
	 *
	 * @since 1.0.0
	 * @version 1.0.0
	 * @see Player
	 * @author Oliver Geisel
	 */
	record Substitute(Player player, Player substitute, int positionTeam, int durchgangNum, int wurfNum) {
		public Substitute(Player player, Player substitute, int positionTeam, int durchgangNum) {
			this(player, substitute, positionTeam, durchgangNum, 0);
		}

	}

}
