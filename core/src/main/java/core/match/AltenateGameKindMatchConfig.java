package core.match;

import core.game.GameKind;

import java.util.Collections;
import java.util.List;

/**
 * Configuration of a match. this is a special configuration for a match with multiple game kinds.
 *
 * @author Oliver Geisel
 * @version 1.0.0
 * @see MatchConfig
 * @see Match
 * @since 1.0.0
 */
public class AltenateGameKindMatchConfig extends MatchConfig {

	private List<GameKind> gameKinds;

	/**
	 * Creates a new match configuration. This constructor is only used for serialization.
	 *
	 * @param schema    The schema of the match.
	 * @param gameKinds The game kinds of the match.
	 */
	public AltenateGameKindMatchConfig(MatchSchema schema, List<GameKind> gameKinds) {
		super(schema, gameKinds.getFirst());
		this.gameKinds = gameKinds;
	}

//region setter/getter

	/**
	 * Get the GameKinds of the match in the correct order.
	 *
	 * @return
	 */
	public List<GameKind> getGameKinds() {
		return Collections.unmodifiableList(gameKinds);
	}
//endregion
}
