package de.olivergeisel.kegelplay.core.match;

import de.olivergeisel.kegelplay.infrastructure.ini.IniFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Status of a match. This will change during the match.
 */
public class MatchStatusInfo {

	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private boolean       aborted;
	private boolean       finished;
	// status all
	private boolean       laneOk;
	private boolean       passOk;
	private boolean       injury;  // verletzung
	private boolean       warnings;
	private boolean       protests;
	private boolean       refereeHome;
	private boolean       refereeGuest;
	// anzahl
	private int           currentSet;

	public MatchStatusInfo(IniFile iniFile, int durchgang) {
		var region = iniFile.getRegion("Allgemein");
		var format = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
		startTime = LocalDateTime.parse(region.getValue("Anfang"), format);
		try {
			endTime = LocalDateTime.parse(region.getValue("Ende"), format);
		} catch (Exception e) {
			endTime = null;
		}
		aborted = region.getValue("Abgebrochen").equals("1");
		finished = region.getValue("Abgeschlossen").equals("1");
		// status all
		region = iniFile.getRegion("Status");
		laneOk = region.getValue("Bahn iO").equals("1");
		passOk = region.getValue("Passe iO").equals("1");
		injury = region.getValue("Verletzung").equals("1");
		// übersetze Verletzung ins englische
		warnings = region.getValue("Verwarnung").equals("1");
		protests = region.getValue("Protest").equals("1");
		refereeHome = region.getValue("SchiriHeim").equals("1");
		refereeHome = region.getValue("SchiriGast").equals("1");
		//
		currentSet = durchgang;

	}

	//region setter/getter
	public LocalDateTime getStartTime() {
		return startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public boolean isAborted() {
		return aborted;
	}

	public boolean isFinished() {
		return finished;
	}

	public boolean isLaneOk() {
		return laneOk;
	}

	public boolean isPassOk() {
		return passOk;
	}

	public boolean isInjury() {
		return injury;
	}

	public boolean isWarnings() {
		return warnings;
	}

	public boolean isProtests() {
		return protests;
	}

	public boolean isRefereeHome() {
		return refereeHome;
	}
	// teams

	public boolean isRefereeGuest() {
		return refereeGuest;
	}

	public int getCurrentSet() {
		return currentSet;
	}
//endregion
}
