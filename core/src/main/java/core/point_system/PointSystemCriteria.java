package core.point_system;

/**
 * Enum to define the criteria for the point system.
 * This is the criteria for which the total Points in a match are calculated.
 */
public enum PointSystemCriteria {

	TEAM, // every team plays the other teams
	PLAYER, // every player plays against every other player. There is only one team but all players are rivals.
	SET, // every set is a match. The team with the most sets won is the winner.
}
