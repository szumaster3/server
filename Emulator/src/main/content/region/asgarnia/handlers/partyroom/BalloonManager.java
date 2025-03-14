package content.region.asgarnia.handlers.partyroom;

import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.GroundItem;
import core.game.node.item.GroundItemManager;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Animation;
import core.plugin.Plugin;
import core.tools.RandomFunction;

import java.util.ArrayList;
import java.util.List;

import static core.api.ContentAPIKt.setVarp;

/**
 * The type Balloon manager.
 */
public final class BalloonManager extends OptionHandler {
    private static final List<Scenery> balloons = new ArrayList<>(20);
    private int countdown;

    /**
     * Instantiates a new Balloon manager.
     */
    public BalloonManager() {
    }

    @Override
    public Plugin<java.lang.Object> newInstance(java.lang.Object arg) throws Throwable {
        for (PartyBalloon balloon : PartyBalloon.values()) {
            SceneryDefinition.forId(balloon.getBalloonId()).getHandlers().put("option:burst", this);
        }
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        switch (option) {
            case "burst":
                PartyBalloon.forId(node.getId()).burst(player, node.asScenery());
                return true;
        }
        return true;
    }

    @Override
    public Location getDestination(Node node, Node n) {
        return n.getLocation();
    }

    /**
     * Start.
     */
    public void start() {
        if (isCountingDown()) {
            return;
        }
        countdown = GameWorld.getTicks() + getDropDelay();
        final NPC partyPete = RegionManager.getNpc(new Location(3052, 3373, 0), 659, 1);
        GameWorld.getPulser().submit(new Pulse(1) {
            @Override
            public boolean pulse() {
                int realCount = --countdown - GameWorld.getTicks();
                for (ChestViewer viewer : PartyRoomOptionHandler.getViewers().values()) {
                    setVarp(viewer.getPlayer(), 1135, realCount);
                }
                if (--realCount - GameWorld.getTicks() <= 0) {
                    drop();
                    return true;
                }
                partyPete.sendChat("" + realCount);
                return realCount < 0;
            }
        });
    }

    private void drop() {
        countdown = 0;
        balloons.clear();
        PartyRoomOptionHandler.partyChest.addAll(PartyRoomOptionHandler.chestQueue);
        PartyRoomOptionHandler.chestQueue.clear();
        PartyRoomOptionHandler.update();
        GameWorld.getPulser().submit(new Pulse(1) {
            int waves;

            @Override
            public boolean pulse() {
                if (waves == 0 || waves == 3 || waves == 5 || waves == 8 || waves == 10 || waves == 12 || waves == 15 || waves == 18 || waves == 20) {
                    for (int i = 0; i < 30; i++) {
                        Scenery balloon = getBalloon();
                        if (balloon != null) {
                            balloons.add(balloon);
                            SceneryBuilder.add(balloon, RandomFunction.random(200, 300));
                        }
                    }
                }
                return ++waves > 20;
            }

        });
    }

    private Scenery getBalloon() {
        final Location location = new Location(3045 + RandomFunction.randomSign(RandomFunction.getRandom(8)), 3378 + RandomFunction.randomSign(RandomFunction.getRandom(6)), 0);
        if (!RegionManager.isTeleportPermitted(location) || RegionManager.getObject(location) != null) {
            return null;
        }
        return new Scenery(PartyBalloon.values()[RandomFunction.random(PartyBalloon.values().length)].getBalloonId(), location);
    }

    /**
     * Is cluttered boolean.
     *
     * @return the boolean
     */
    public boolean isCluttered() {
        for (Scenery scenery : balloons) {
            if (RegionManager.getObject(scenery.getLocation()) != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets drop delay.
     *
     * @return the drop delay
     */
    public int getDropDelay() {
        int wealth = getWealth();
        if (wealth <= 50000) {
            return 10;
        } else if (wealth >= 50000 && wealth <= 150000) {
            return 100;
        } else if (wealth >= 150000 && wealth <= 1000000) {
            return 500;
        } else if (wealth > 1000000) {
            return 1000;
        }
        return 0;
    }

    /**
     * Gets wealth.
     *
     * @return the wealth
     */
    public int getWealth() {
        return PartyRoomOptionHandler.chestQueue.getWealth() + PartyRoomOptionHandler.partyChest.getWealth();
    }

    /**
     * Is counting down boolean.
     *
     * @return the boolean
     */
    public boolean isCountingDown() {
        return countdown > GameWorld.getTicks();
    }

    /**
     * Gets balloons.
     *
     * @return the balloons
     */
    public static List<Scenery> getBalloons() {
        return balloons;
    }

    /**
     * Gets countdown.
     *
     * @return the countdown
     */
    public int getCountdown() {
        return countdown;
    }

    /**
     * The enum Party balloon.
     */
    enum PartyBalloon {

        /**
         * Yellow party balloon.
         */
        YELLOW(115, 123),

        /**
         * Red party balloon.
         */
        RED(116, 124),
        /**
         * Blue party balloon.
         */
        BLUE(117, 125),
        /**
         * Green party balloon.
         */
        GREEN(118, 126),
        /**
         * Purple party balloon.
         */
        PURPLE(119, 127),
        /**
         * White party balloon.
         */
        WHITE(120, 128),
        /**
         * Green blue party balloon.
         */
        GREEN_BLUE(121, 129),
        /**
         * Tri party balloon.
         */
        TRI(122, 130);

        private final int balloonId;
        private final int popId;

        PartyBalloon(int balloonId, int popId) {
            this.balloonId = balloonId;
            this.popId = popId;
        }

        /**
         * Burst.
         *
         * @param player  the player
         * @param scenery the scenery
         */
        public void burst(final Player player, final Scenery scenery) {
            final Scenery popped = scenery.transform(popId);
            if (!getBalloons().contains(scenery)) {
                player.sendMessage("Error! Balloon not registered.");
                return;
            }
            player.lock(2);
            SceneryBuilder.remove(scenery);
            SceneryBuilder.add(popped);
            getBalloons().remove(scenery);
            player.animate(Animation.create(10017));

            GameWorld.getPulser().submit(new Pulse(1) {
                int counter;

                @Override
                public boolean pulse() {
                    switch (++counter) {
                        case 1:
                            SceneryBuilder.remove(popped);
                            if (!player.getIronmanManager().isIronman() && RandomFunction.random(3) == 1) {
                                GroundItem ground = getGround(scenery.getLocation(), player);
                                if (ground != null) {
                                    GroundItemManager.create(ground);
                                    PartyRoomOptionHandler.partyChest.shift();
                                    PartyRoomOptionHandler.update();
                                }
                            }
                            return true;
                    }
                    return false;
                }
            });
        }

        private GroundItem getGround(Location location, Player player) {
            final Item item = PartyRoomOptionHandler.partyChest.toArray()[RandomFunction.random(PartyRoomOptionHandler.partyChest.itemCount())];
            if (item == null) {
                return null;
            }
            if (PartyRoomOptionHandler.partyChest.remove(item)) {
                final Item dropItem;
                int newamt;
                if (item.getAmount() > 1) {
                    newamt = RandomFunction.random(1, item.getAmount());
                    if (item.getAmount() - newamt > 0) {
                        Item newItem = new Item(item.getId(), item.getAmount() - newamt);
                        PartyRoomOptionHandler.partyChest.add(newItem);
                    }
                    dropItem = new Item(item.getId(), newamt);
                } else {
                    dropItem = item;
                }
                return new GroundItem(dropItem, location, player);
            }
            return null;
        }

        /**
         * For id party balloon.
         *
         * @param id the id
         * @return the party balloon
         */
        public static PartyBalloon forId(int id) {
            for (PartyBalloon balloon : values()) {
                if (balloon.getBalloonId() == id) {
                    return balloon;
                }
            }
            return null;
        }

        /**
         * Gets balloon id.
         *
         * @return the balloon id
         */
        public int getBalloonId() {
            return balloonId;
        }

        /**
         * Gets pop id.
         *
         * @return the pop id
         */
        public int getPopId() {
            return popId;
        }
    }

}
