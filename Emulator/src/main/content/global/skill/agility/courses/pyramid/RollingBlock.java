package content.global.skill.agility.courses.pyramid;

import content.global.skill.agility.AgilityHandler;
import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.system.task.MovementHook;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Animation;
import kotlin.Unit;
import org.rs.consts.Sounds;

import static core.api.ContentAPIKt.playAudio;

/**
 * The type Rolling block.
 */
public final class RollingBlock implements MovementHook {

    @Override
    public boolean handle(final Entity e, final Location dest) {
        final Player player = (Player) e;
        final BlockSets set = BlockSets.forLocation(dest);
        final Scenery stone = RegionManager.getObject(set.getBlockLocation());
        final boolean backwards = isBackwards(stone.getDirection(), player.getDirection(), stone.getRotation());
        final boolean fail = backwards || AgilityHandler.hasFailed(player, 2, 0.3);
        player.lock(5);
        AgilityPyramidCourse.addConfig(player, stone, 1, false);
        playAudio(player, Sounds.PYRAMID_SCRAPE_1396);
        playAudio(player, Sounds.LAND_FLAT_2469, 40);

        if (e.isPlayer()) {
            ((Player) e).logoutListeners.put("rolling-block", p -> {
                p.setLocation(e.getLocation().transform(0, 0, 0));
                return Unit.INSTANCE;
            });
        }
        if (fail) {
            GameWorld.getPulser().submit(new Pulse(1, player) {
                int counter;

                @Override
                public boolean pulse() {
                    switch (++counter) {
                        case 1:
                            final Location end = player.getLocation().transform(stone.getDirection(), -2);
                            final int hit = backwards ? 1 : 6;
                            if (set == BlockSets.FOUR) {
                                AgilityHandler.failWalk(player, 3, dest, end, AgilityPyramidCourse.transformLevel(end), Animation.create(1116), 10, hit, null);
                            } else {
                                AgilityHandler.failWalk(player, -1, dest, AgilityPyramidCourse.transformLevel(end), AgilityPyramidCourse.transformLevel(end), Animation.create(1116), 10, hit, null);
                            }
                            break;
                        case 3:
                            AgilityPyramidCourse.addConfig(player, stone, 0, true);
                            player.logoutListeners.remove("rolling-block");
                            return true;
                    }
                    return false;
                }
            });
            return false;
        }
        AgilityHandler.forceWalk(player, -1, dest, dest.transform(player.getDirection(), 2), Animation.create(1115), 20, 12, null);
        GameWorld.getPulser().submit(new Pulse(3, player) {
            @Override
            public boolean pulse() {
                AgilityPyramidCourse.addConfig(player, stone, 0, true);
                return true;
            }
        });
        return false;
    }

    /**
     * Is backwards boolean.
     *
     * @param direction the direction
     * @param dir       the dir
     * @param rot       the rot
     * @return the boolean
     */
    public boolean isBackwards(Direction direction, Direction dir, int rot) {
        switch (direction) {
            case NORTH:
                break;
            case SOUTH:
                return rot == 2 & dir == Direction.WEST;
            case EAST:
                return rot == 1 && dir == Direction.SOUTH;
            case WEST:
                return rot == 3 && dir == Direction.NORTH;
            default:
                break;
        }
        return false;
    }

    /**
     * The enum Block sets.
     */
    public enum BlockSets {

        /**
         * One block sets.
         */
        ONE(Location.create(3354, 2841, 0), Location.create(3354, 2842, 1), Location.create(3355, 2842, 1), Location.create(3355, 2841, 1), Location.create(3354, 2841, 1)),

        /**
         * Two block sets.
         */
        TWO(Location.create(3374, 2835, 0), Location.create(3374, 2835, 1), Location.create(3374, 2836, 1), Location.create(3375, 2836, 1), Location.create(3375, 2835, 1)),

        /**
         * Three block sets.
         */
        THREE(Location.create(3368, 2849, 1), Location.create(3368, 2849, 2), Location.create(3369, 2849, 2), Location.create(3369, 2850, 2), Location.create(3368, 2850, 2)),

        /**
         * Four block sets.
         */
        FOUR(Location.create(3048, 4699, 1), Location.create(3048, 4700, 2), Location.create(3049, 4700, 2), Location.create(3049, 4699, 2), Location.create(3048, 4699, 2)),

        /**
         * Five block sets.
         */
        FIVE(Location.create(3044, 4699, 2), Location.create(3044, 4700, 3), Location.create(3045, 4700, 3), Location.create(3045, 4699, 3), Location.create(3044, 4699, 3));

        private final Location blockLocation;

        private final Location[] tiles;

        BlockSets(Location blockLocation, Location... tiles) {
            this.blockLocation = blockLocation;
            this.tiles = tiles;
        }

        /**
         * For location block sets.
         *
         * @param location the location
         * @return the block sets
         */
        public static BlockSets forLocation(Location location) {
            for (BlockSets set : values()) {
                for (Location loc : set.getTiles()) {
                    if (loc.equals(location)) {
                        return set;
                    }
                }
            }
            return null;
        }

        /**
         * Gets block location.
         *
         * @return the block location
         */
        public Location getBlockLocation() {
            return blockLocation;
        }

        /**
         * Get tiles location [ ].
         *
         * @return the location [ ]
         */
        public Location[] getTiles() {
            return tiles;
        }

        static {
            final RollingBlock block = new RollingBlock();
            for (BlockSets set : values()) {
                for (Location l : set.getTiles()) {
                    AgilityPyramidZone.getLOCATION_TRAPS().put(l, block);
                }
            }
        }
    }
}
