package de.olivergeisel.kegelplay.core.team_and_player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class Team {

	private final Map<Player, Player> substitutesMap = new HashMap<>();
	private       String              name;
	private       Player[]            players;
	private       Player[]            substitutes;

	protected Team(String name, Player[] players, Player[] substitutes) {
		this.name = name;
		this.players = players;
		this.substitutes = substitutes;

	}

	public Player getPlayer(int index) throws IllegalArgumentException{
		try {
			return players[index];
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Index out of bounds: " + index,e);
		}
	}

	public Player getPlayer(String name) throws NoPlayerFoundException {
		for (Player player : players) {
			if (player.getCompleteName().equals(name)) {
				return player;
			}
		}
		throw new NoPlayerFoundException();
	}

	public Player getSubstitute(int index) {
		return substitutes[index];
	}

	public void setPlayer(int index, Player player) {
		players[index] = player;
	}

	public void setSubstitute(int index, Player substitute) {
		substitutes[index] = substitute;
	}

	public void substitute(int playerIndex, int substituteIndex) throws IllegalArgumentException{
		if (playerIndex < 0 || playerIndex >= players.length) {
			throw new IllegalArgumentException("playerIndex out of bounds: " + playerIndex);
		}
		if (substituteIndex < 0 || substituteIndex >= substitutes.length) {
			throw new IllegalArgumentException("substituteIndex out of bounds: " + substituteIndex);
		}
		Player player = players[playerIndex];
		Player substitute = substitutes[substituteIndex];
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

	public Player[] getPlayers() {
		return players;
	}

	public void setPlayers(Player[] players) {
		this.players = players;
	}

	public Player[] getSubstitutes() {
		return substitutes;
	}

	public void setSubstitutes(Player[] substitutes) {
		this.substitutes = substitutes;
	}

	public Map<Player, Player> getSubstitutesMap() {
		return Collections.unmodifiableMap(substitutesMap);
	}

//endregion


	record Substitute(Player player, Player substitute, int positionTeam, int durchgangNum, int wurfNum) {
		public Substitute(Player player, Player substitute, int positionTeam, int durchgangNum) {
			this(player, substitute, positionTeam, durchgangNum, 0);
		}

	}



}
