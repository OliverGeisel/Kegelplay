package core.team_and_player;

import static java.util.FormatProcessor.FMT;


/**
 * Represents the ID of a Player. This is the Number of the Player that stand in his player pass.
 *
 * @param countryCode The country code of the player (D, A, F, ...)
 * @param number      The number of the player (0-999999)
 * @author Oliver Geisel
 * @version 1.0.0
 * @since 1.0.0
 */
public record PlayerID(String countryCode, int number) {

	/**
	 * Creates a new PlayerID.
	 *
	 * @param countryCode The country code of the player. must be a letter.
	 * @param number      The number of the player. Must be a number between 0 and 999_999.
	 */
	public PlayerID {
		if (number > 999_999) {
			throw new IllegalArgumentException("Number too big");
		}
		if (countryCode == null) {
			countryCode = "";
		}
	}

	@Override
	public String toString() {
		return FMT."\{countryCode}%06d\{number}";
	}
}
