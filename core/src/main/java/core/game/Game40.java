package core.game;

import core.team_and_player.Player;

import java.time.LocalDateTime;
import java.util.List;

public class Game40 extends Game {

	GameSet[] sets = new GameSet[2];

	public Game40(Player player) {
		super(player);
	}

	public Game40(Player player, LocalDateTime date) {
		super(player, date);
	}

	public Game40(Player player, Player substitution1, Player substitution2, LocalDateTime date) {
		super(player, substitution1, substitution2, date);
	}

	@Override
	public void start() {

	}

	@Override
	public GameSet getDurchgang(int durchgang) throws IllegalArgumentException {
		return sets[durchgang];
	}

//region setter/getter
	@Override
	public GameInfo getGameInfo() {
		return new GameInfo(2, 10, 10, 8, true);
	}

	@Override
	public GameKind getGameKind() {
		return GameKind.GAME_40;
	}

	@Override
	public int getNumberOfDurchgaenge() {
		return 2;
	}

	public int getNumberOfWurf() {
		var back = 0;
		for (GameSet set : sets) {
			if (set.getState() == SetState.FINISHED) {
				back += set.getThrowCount();
			} else {
				back += set.getAnzahlGespielteWuerfe();
			}
		}
		return back;
	}

	@Override
	public int getTotalFehlwurf() {
		var back = 0;
		for (GameSet set : sets) {
			back += set.getAnzahlFehler();
		}
		return back;
	}

	@Override
	public int getTotalScore() {
		var back = 0;
		for (GameSet set : sets) {
			back += set.getScore();
		}
		return back;
	}

	@Override
	public int getTotalVolle() {
		var back = 0;
		for (GameSet set : sets) {
			back += set.getVolleScore();
		}
		return back;
	}

	@Override
	public int getTotalAbraeumen() {
		var back = 0;
		for (GameSet set : sets) {
			back += set.getAbraeumenScore();
		}
		return back;
	}

	@Override
	public GameSet[] getSets() {
		return sets;
	}

	@Override
	public void setDurchgaenge(List<GameSet> durgaenge) throws IllegalArgumentException {
		if (durgaenge.size() != 2) {
			throw new IllegalArgumentException(
					STR."Anzahl der Durchgänge stimmt nicht. Erwarte 2; waren aber \{durgaenge.size()}");
		}
		for (int i = 0; i < 2; i++) {
			sets[i] = durgaenge.get(i);
		}
		checkState();
	}
//endregion
}
