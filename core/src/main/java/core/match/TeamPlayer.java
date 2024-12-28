package core.match;

import core.team_and_player.Player;
import core.team_and_player.Team;

/**
 * A {@link Team} with an associated {@link Player}.
 *
 * @param team   The team
 * @param player The player in the team
 * @author Oliver Geisel
 * @version 1.0.0
 * @see Team
 * @see Player
 * @since 1.0.0
 */
public record TeamPlayer(Team team, Player player) {
}
