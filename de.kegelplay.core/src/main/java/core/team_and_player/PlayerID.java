package core.team_and_player;

import static java.util.FormatProcessor.FMT;


/**
 * Represents the ID of a Player. This is the Number of the Player that stand in his player pass.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Oliver Geisel
 */
public record PlayerID (String countryCode, int number){

	public PlayerID {
		if (number > 999_999) {
			throw new IllegalArgumentException("Number too big");
		}
		if (countryCode == null) {
			countryCode = "";
		}
	}

	public String toString(){
		return FMT."\{countryCode}%06d\{number}";
	}
}
