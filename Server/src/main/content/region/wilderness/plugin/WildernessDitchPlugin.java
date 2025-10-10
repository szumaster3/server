package content.region.wilderness.plugin;

import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.DestinationFlag;
import core.game.interaction.MovementPulse;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.impl.PulseType;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.WarningActions;
import core.game.node.entity.player.link.WarningManager;
import core.game.node.entity.player.link.Warnings;
import core.game.node.scenery.Scenery;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.plugin.Plugin;
import kotlin.Pair;
import shared.consts.Animations;

import static core.api.ContentAPIKt.forceMove;

/**
 * Represents the plugin to handle the crossing.
 *
 * @author Vexia
 */
@Initializable
public final class WildernessDitchPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        SceneryDefinition.forId(23271).getHandlers().put("option:cross", this);
        return this;
    }

    @Override
    public boolean handle(final Player player, final Node node, String option) {
        if (player.isArtificial()) {
            Pair<Location, Location> locations = WarningActions.getDitchLocations(player.getLocation(), node.getLocation(), 0);
            Location start = locations.getFirst();
            Location end = locations.getSecond();

            if (!player.getLocation().equals(start)) {
                player.getPulseManager().run(new MovementPulse(player, start, DestinationFlag.LOCATION) {
                    @Override
                    public boolean pulse() {
                        return true;
                    }
                });
                return true;
            }

            forceMove(player, start, end, 0, 60, null, Animations.JUMP_OVER_OBSTACLE_6132,
                    () -> {
                        return kotlin.Unit.INSTANCE;
                    }
            );

            return true;
        }

        if (player.getLocation().getDistance(node.getLocation()) < 3) {
            handleDitch(player, node);
        } else {
            player.getPulseManager().run(new MovementPulse(player, node) {
                @Override
                public boolean pulse() {
                    handleDitch(player, node);
                    return true;
                }
            }, PulseType.STANDARD);
        }

        return true;
    }

    /**
     * Handles the wilderness ditch jumping.
     *
     * @param player The player.
     * @param node   The ditch object.
     */
    public void handleDitch(final Player player, Node node) {
        player.faceLocation(node.getLocation());
        Scenery ditch = (Scenery) node;
        player.setAttribute("wildy_ditch", ditch);

        if (!player.isArtificial()) {
            boolean crossInto = false;

            if (ditch.getRotation() % 2 == 0) {
                crossInto = player.getLocation().getY() <= node.getLocation().getY();
            } else {
                crossInto = player.getLocation().getX() <= node.getLocation().getX();
            }

            if (crossInto && !WarningManager.isWarningDisabled(player, Warnings.WILDERNESS_DITCH)) {
                WarningManager.openWarningInterface(player, Warnings.WILDERNESS_DITCH);
                return;
            }
        }

        WarningActions.handleWildernessJump(player);
    }

    @Override
    public boolean isWalk() {
        return true;
    }
}
