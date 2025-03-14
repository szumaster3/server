package core.game.node.scenery;

import core.game.node.item.GroundItem;
import core.game.node.item.GroundItemManager;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.map.build.LandscapeParser;
import core.game.world.update.flag.chunk.SceneryUpdateFlag;

/**
 * The type Scenery builder.
 */
public final class SceneryBuilder {

    /**
     * Replace boolean.
     *
     * @param remove    the remove
     * @param construct the construct
     * @return the boolean
     */
    public static boolean replace(Scenery remove, Scenery construct) {
        return replace(remove, construct, true, false);
    }

    /**
     * Replace boolean.
     *
     * @param remove    the remove
     * @param construct the construct
     * @param clip      the clip
     * @param permanent the permanent
     * @return the boolean
     */
    public static boolean replace(Scenery remove, Scenery construct, boolean clip, boolean permanent) {
        if (!clip) {
            return replaceClientSide(remove, construct, -1);
        }
        remove = remove.getWrapper();
        Scenery current = LandscapeParser.removeScenery(remove);
        if (current == null) {
            return false;
        }
        if (current.getRestorePulse() != null) {
            current.getRestorePulse().stop();
            current.setRestorePulse(null);
        }
        if (current instanceof Constructed) {
            Scenery previous = ((Constructed) current).getReplaced();
            if (previous != null && previous.equals(construct)) {
                LandscapeParser.addScenery(previous);
                update(current, previous);
                return true;
            }
        }
        Constructed constructed = construct.asConstructed();
        if (!permanent) {
            constructed.setReplaced(current);
        }
        LandscapeParser.addScenery(constructed);
        update(current, constructed);
        return true;
    }

    private static boolean replaceClientSide(final Scenery remove, final Scenery construct, int restoreTicks) {
        RegionManager.getRegionChunk(remove.getLocation()).flag(new SceneryUpdateFlag(remove, true));
        RegionManager.getRegionChunk(construct.getLocation()).flag(new SceneryUpdateFlag(construct, false));
        if (restoreTicks > 0) {
            GameWorld.getPulser().submit(new Pulse(restoreTicks) {
                @Override
                public boolean pulse() {
                    return replaceClientSide(construct, remove, -1);
                }
            });
        }
        return true;
    }

    /**
     * Replace boolean.
     *
     * @param remove       the remove
     * @param construct    the construct
     * @param restoreTicks the restore ticks
     * @return the boolean
     */
    public static boolean replace(Scenery remove, Scenery construct, int restoreTicks) {
        return replace(remove, construct, restoreTicks, true);
    }

    /**
     * Replace boolean.
     *
     * @param remove       the remove
     * @param construct    the construct
     * @param restoreTicks the restore ticks
     * @param clip         the clip
     * @return the boolean
     */
    public static boolean replace(Scenery remove, Scenery construct, int restoreTicks, final boolean clip) {
        if (!clip) {
            return replaceClientSide(remove, construct, restoreTicks);
        }
        remove = remove.getWrapper();
        Scenery current = LandscapeParser.removeScenery(remove);
        if (current == null) {
            return false;
        }
        if (current.getRestorePulse() != null) {
            current.getRestorePulse().stop();
            current.setRestorePulse(null);
        }
        if (current instanceof Constructed) {
            Scenery previous = ((Constructed) current).getReplaced();
            if (previous != null && previous.equals(construct)) {
                throw new IllegalStateException("Can't temporarily replace an already temporary object!");
            }
        }
        final Constructed constructed = construct.asConstructed();
        constructed.setReplaced(current);
        LandscapeParser.addScenery(constructed);
        update(current, constructed);
        if (restoreTicks < 0) {
            return true;
        }
        constructed.setRestorePulse(new Pulse(restoreTicks) {
            @Override
            public boolean pulse() {
                replace(constructed, constructed.getReplaced());
                return true;
            }
        });
        GameWorld.getPulser().submit(constructed.getRestorePulse());
        return true;
    }

    /**
     * Replace with temp before new boolean.
     *
     * @param remove       the remove
     * @param temporary    the temporary
     * @param construct    the construct
     * @param restoreTicks the restore ticks
     * @param clip         the clip
     * @return the boolean
     */
    public static boolean replaceWithTempBeforeNew(Scenery remove, Scenery temporary, Scenery construct, int restoreTicks, final boolean clip) {
        if (!clip) {
            return replaceClientSide(remove, temporary, restoreTicks);
        }
        remove = remove.getWrapper();
        Scenery current = LandscapeParser.removeScenery(remove);
        if (current == null) {
            return false;
        }
        if (current.getRestorePulse() != null) {
            current.getRestorePulse().stop();
            current.setRestorePulse(null);
        }
        if (current instanceof Constructed) {
            Scenery previous = ((Constructed) current).getReplaced();
            if (previous != null && previous.equals(temporary)) {
                throw new IllegalStateException("Can't temporarily replace an already temporary object!");
            }
        }
        final Constructed constructed = temporary.asConstructed();
        constructed.setReplaced(current);
        LandscapeParser.addScenery(constructed);
        update(current, constructed);
        if (restoreTicks < 0) {
            return true;
        }
        constructed.setRestorePulse(new Pulse(restoreTicks) {
            @Override
            public boolean pulse() {
                replace(constructed, construct);
                return true;
            }
        });
        GameWorld.getPulser().submit(constructed.getRestorePulse());
        return true;
    }

    /**
     * Add constructed.
     *
     * @param scenery the scenery
     * @return the constructed
     */
    public static Constructed add(Scenery scenery) {
        return add(scenery, -1);
    }

    /**
     * Add constructed.
     *
     * @param scenery the scenery
     * @param ticks   the ticks
     * @param items   the items
     * @return the constructed
     */
    public static Constructed add(Scenery scenery, int ticks, final GroundItem... items) {
        scenery = scenery.getWrapper();
        final Constructed constructed = scenery.asConstructed();
        LandscapeParser.addScenery(constructed);
        update(constructed);
        if (ticks > -1) {
            GameWorld.getPulser().submit(new Pulse(ticks, scenery) {
                @Override
                public boolean pulse() {
                    remove(constructed);
                    if (items != null) {
                        for (int i = 0; i < items.length; i++) {
                            GroundItemManager.create(items[i]);
                        }
                    }
                    return true;
                }
            });
        }
        return constructed;
    }

    /**
     * Remove all boolean.
     *
     * @param objectId  the object id
     * @param southWest the south west
     * @param northEast the north east
     * @return the boolean
     */
    public static boolean removeAll(int objectId, Location southWest, Location northEast) {
        if (southWest.getX() > northEast.getX() || southWest.getY() > northEast.getY())
            return false;

        int differenceX = northEast.getX() - southWest.getX();
        int differenceY = northEast.getY() - southWest.getY();

        for (int x = 0; x <= differenceX; x++) {
            for (int y = 0; y <= differenceY; y++) {
                Scenery scenery = new Scenery(objectId, Location.create(southWest.getX() + x, southWest.getY() + y, southWest.getZ()));
                remove(scenery);
            }
        }
        return true;
    }

    /**
     * Remove boolean.
     *
     * @param scenery the scenery
     * @return the boolean
     */
    public static boolean remove(Scenery scenery) {
        if (scenery == null) {
            return false;
        }
        scenery = scenery.getWrapper();
        Scenery current = LandscapeParser.removeScenery(scenery);
        if (current == null) {
            return false;
        }
        update(current);
        return true;
    }

    /**
     * Remove boolean.
     *
     * @param scenery      the scenery
     * @param respawnTicks the respawn ticks
     * @return the boolean
     */
    public static boolean remove(final Scenery scenery, int respawnTicks) {
        if (remove(scenery)) {
            GameWorld.getPulser().submit(new Pulse(respawnTicks) {
                @Override
                public boolean pulse() {
                    add(scenery);
                    return true;
                }
            });
            return true;
        }
        return false;
    }

    /**
     * Update.
     *
     * @param sceneries the sceneries
     */
    public static void update(Scenery... sceneries) {
        for (Scenery o : sceneries) {
            if (o == null) {
                continue;
            }
            RegionManager.getRegionChunk(o.getLocation()).flag(new SceneryUpdateFlag(o, !o.isActive()));
        }
    }
}
