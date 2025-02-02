package core.match;


import core.util.KeyValueRegion;
import core.util.KeyValueRegionCollection;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Status of a {@link Match}.
 * This will change during the match.
 * <br>
 * It contains the start and end time, if the match is aborted or finished and the status of the match.
 * <p>
 * It is important to know the status of a match. This class contains the status of a match.
 * At the end of the match, the status will be saved in a file.
 * </p>
 *
 * @author Oliver Geisel
 * @version 1.0.0
 * @see Match
 * @since 1.0.0
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

	/**
	 * Create a new MatchStatusInfo with the given values.
	 *
	 * @param iniFile        A KeyValueRegionCollection with the values of the match
	 * @param currentGameSet The current set the match is in
	 */
	public MatchStatusInfo(KeyValueRegionCollection<String, String, KeyValueRegion<String, String>> iniFile,
			int currentGameSet) {
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
		// Older Versions don't have this
		var schiriHeim = region.getValue("SchiriHeim");
		if (schiriHeim != null) {
			refereeHome = schiriHeim.equals("1");
			refereeGuest = region.getValue("SchiriGast").equals("1");
		} else {
			refereeHome = true;
			refereeGuest = true;
		}
		//
		currentSet = currentGameSet;

	}

	//region setter/getter

	/**
	 * Returns the start time of the match.
	 *
	 * @return The start time of the match.
	 */
	public LocalDateTime getStartTime() {
		return startTime;
	}

	/**
	 * Returns the end time of the match.
	 *
	 * @return The end time of the match.
	 */
	public LocalDateTime getEndTime() {
		return endTime;
	}

	/**
	 * Returns if the match was aborted.
	 *
	 * @return <b>true</b> if the match was aborted.
	 */
	public boolean isAborted() {
		return aborted;
	}

	/**
	 * Returns if the match was finished.
	 *
	 * @return <b>true</b> if the match was finished.
	 */
	public boolean isFinished() {
		return finished;
	}

	/**
	 * Returns if the lane is ok. Or generally the status of the lane/location.
	 *
	 * @return <b>true</b> if the lane is ok.
	 */
	public boolean isLaneOk() {
		return laneOk;
	}

	/**
	 * Returns if all ids are ok.
	 *
	 * @return <b>true</b> if all ids are ok.
	 */
	public boolean isPassOk() {
		return passOk;
	}

	/**
	 * Returns if there is an injury.
	 *
	 * @return <b>true</b> if there is an injury.
	 */
	public boolean isInjury() {
		return injury;
	}

	/**
	 * Returns if there are warnings.
	 *
	 * @return <b>true</b> if there are warnings.
	 */
	public boolean isWarnings() {
		return warnings;
	}

	/**
	 * Returns if there are protests.
	 *
	 * @return <b>true</b> if there are protests.
	 */
	public boolean isProtests() {
		return protests;
	}

	/**
	 * Returns if the referee from home is okay.
	 *
	 * @return <b>true</b> if the referee is okay.
	 */
	public boolean isRefereeHome() {
		return refereeHome;
	}
	// teams

	/**
	 * Returns if the referee from the guest is okay.
	 *
	 * @return <b>true</b> if the referee is okay.
	 */
	public boolean isRefereeGuest() {
		return refereeGuest;
	}

	/**
	 * Returns the current set of the match.
	 *
	 * @return The current set of the match.
	 */
	public int getCurrentSet() {
		return currentSet;
	}
//endregion
}
