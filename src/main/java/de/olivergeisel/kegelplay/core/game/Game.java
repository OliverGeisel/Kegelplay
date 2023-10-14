package de.olivergeisel.kegelplay.core.game;

import de.olivergeisel.kegelplay.core.team_and_player.Player;
import javafx.beans.Observable;
import javafx.util.Callback;

import java.time.LocalDateTime;
import java.util.Observer;

public abstract class Game implements Observable {

	private Player        player;
	private LocalDateTime date;

	protected Game(Player player, LocalDateTime date) {
		this.player = player;
		player.setGame(this);
		this.date = date;
	}

	public abstract void start();

	public abstract int getNumberOfDurchgaenge();

	public abstract int getNumberOfWurf();

	public abstract Durchgang[] getDurchgaenge();

	public Player getPlayer() {
		return player;
	}

	public abstract Durchgang getDurchgang(int durchgang);
}
