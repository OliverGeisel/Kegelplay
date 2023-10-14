package de.olivergeisel.kegelplay.core.team_and_player;

import static java.util.FormatProcessor.FMT;

public record PlayerID (String countryCode, int number){

	public String toString(){
		return FMT."\{countryCode}%06d\{number}";
	}
}
