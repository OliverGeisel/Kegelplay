package core.point_system;

/**
 * Enum to define the type of points to be calculated.
 * when calculating points per Durchgang, every set is important for the total endpoints.
 * When calculating points per Game, each game decides if there is a total point or not.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Oliver Geisel
 */
public enum PointType {

	PER_DURCHGANG,
	PER_GAME,
}
