package de.olivergeisel.kegelplay.core.game;

import java.util.Objects;

public class Durchgang {

	private final int     anzahllWuerfe;
	private final int     anzahlVolle;
	private final int     anzahlAbraeumen;
	private final Wurf[]  wuerfe;
	private       boolean completed = false;

	public Durchgang(int anzahlWuerfe, int anzahlVolle, int anzahlAbraeumen) {
		if (anzahlWuerfe < 1) throw new IllegalArgumentException("Anzahl Würfe muss mindestens 1 sein");
		if (anzahlVolle + anzahlAbraeumen != anzahlWuerfe) {
			throw new IllegalArgumentException("Anzahl Volle und "
											   + "Abräumen muss gleich Anzahl Würfe sein");
		}
		this.anzahllWuerfe = anzahlWuerfe;
		this.wuerfe = new Wurf[anzahlWuerfe];
		this.anzahlVolle = anzahlVolle;
		this.anzahlAbraeumen = anzahlAbraeumen;
	}

	public void set(int wurf, Wurf w) {
		wuerfe[wurf] = w;
	}

	public Wurf get(int wurf) {
		if (wurf >= anzahllWuerfe) throw new IllegalArgumentException("Wurf " + wurf + " existiert nicht");
		return wuerfe[wurf];
	}

	public void complete(boolean completed) {
		this.completed = completed;
	}

	//region setter/getter
	public boolean isCompleted() {
		return completed;
	}

	public int getScore() {
		int score = 0;
		for (int i = 0; i < anzahllWuerfe; i++) {
			if (wuerfe[i] != null) score += wuerfe[i].getScore();
		}
		return score;
	}

	public int getAnzahlGespielteWuerfe() {
		return (int) java.util.Arrays.stream(wuerfe).filter(Objects::nonNull).count();
	}

	public Wurf getLastPlayed() {
		for (int i = anzahllWuerfe - 1; i >= 0; i--) {
			if (wuerfe[i] != null) return wuerfe[i];
		}
		return null;
	}

	public int getAnzahllWuerfe() {
		return anzahllWuerfe;
	}
//endregion

}
