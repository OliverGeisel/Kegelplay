package core.point_system;

/**
 * The way to decide the points of a point system.
 *
 * @version 1.0.0
 * @since 1.0.0
 * author Oliver Geisel
 */
public enum PointDecide {

	/**
	 * Only the total score of the teams is used to decide the points.
	 */
	SCORE,

	/**
	 * Only {@link GameSetPoints} are used to decide the points.
	 */
	SET_POINTS,

	/**
	 * Only the points of the players are used to decide the points.
	 */
	TEAM_POINTS,
}
