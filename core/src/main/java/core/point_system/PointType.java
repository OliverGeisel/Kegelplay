package core.point_system;

/**
 * Enum to define the type of points to be calculated.
 * when calculating points per Durchgang, every set is important for the total endpoints.
 * When calculating points per Game, each game decides if there is a total point or not.
 *
 * @author Oliver Geisel
 * @version 1.0.0
 * @since 1.0.0
 */
public enum PointType {

	/**
	 * Points are calculated per Durchgang.
	 */
	PER_DURCHGANG,

	/**
	 * Points are calculated per Game.
	 */
	PER_GAME,
}
