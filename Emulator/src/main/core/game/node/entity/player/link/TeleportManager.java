package core.game.node.entity.player.link;

import content.region.island.tutorial.plugin.TutorialStage;
import content.region.island.tutorial.plugin.*;
import core.ServerConstants;
import core.game.node.entity.Entity;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.player.Player;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.worker.ManagementEvents;
import org.rs.consts.Sounds;
import proto.management.JoinClanRequest;

import static core.api.ContentAPIKt.*;

/**
 * Handles the entity teleport.
 *
 * @author SonicForce41, Woah
 */
public class TeleportManager {

    /**
     * The constant wilderness teleports.
     */
    public static final int WILDERNESS_TELEPORT = 1 << 16 | 8;

    /**
     * The animations used in the home teleport.
     */
    private final static int[] HOME_ANIMATIONS = {1722, 1723, 1724, 1725, 2798, 2799, 2800, 3195, 4643, 4645, 4646, 4847, 4848, 4849, 4850, 4851, 4852, 65535};

    /**
     * The graphics used in the home teleport.
     */
    private final static int[] HOME_GRAPHICS = {775, 800, 801, 802, 803, 804, 1703, 1704, 1705, 1706, 1707, 1708, 1709, 1710, 1711, 1712, 1713, 65535};

    /**
     * The entity being handled.
     */
    private final Entity entity;

    /**
     * The last teleport of this entity.
     */
    private Pulse lastTeleport;

    /**
     * The current teleport of this entity.
     */
    private Pulse currentTeleport;

    /**
     * The current teleport type.
     */
    private int teleportType;

    /**
     * Instantiates a new Teleport manager.
     *
     * @param entity the entity
     */
    public TeleportManager(Entity entity) {
        this.entity = entity;
        lastTeleport = TeleportType.HOME.getPulse(entity, ServerConstants.HOME_LOCATION);
    }

    /**
     * Sends the teleport.
     *
     * @param location the Location.
     * @return {@code True} if the player successfully started teleporting.
     */
    public boolean send(Location location) {
        return send(location, entity instanceof Player ? getType((Player) entity) : TeleportType.NORMAL, 0);
    }

    /**
     * Sends the teleport.
     *
     * @param location the Location.
     * @param type     the NodeType.
     * @return {@code True} if the player successfully started teleporting.
     */
    public boolean send(Location location, TeleportType type) {
        return send(location, type, 0);
    }

    /**
     * Sends the teleport.
     *
     * @param location     the Location.
     * @param type         the NodeType.
     * @param teleportType The teleporting type. (0=spell, 1=item, 2=object, 3=npc -1= force)
     * @return {@code True} if the player successfully started teleporting.
     */
    public boolean send(Location location, TeleportType type, int teleportType) {
        if (teleportType == WILDERNESS_TELEPORT || type == TeleportType.OBELISK) {
            if (hasTimerActive(entity, "teleblock")) return false;
        } else {
            if (!entity.getZoneMonitor().teleport(teleportType, null)) {
                return false;
            }
            if (teleportType != -1 && entity.isTeleBlocked()) {
                if (entity.isPlayer())
                    entity.asPlayer().sendMessage("A magical force has stopped you from teleporting.");
                return false;
            }
        }
        if (teleportType != -1) {
            if (entity instanceof Player) {
                Player p = (Player) entity;
                p.getDialogueInterpreter().close();
            }
        }
        if (entity.getAttribute("tablet-spell", false)) {
            type = TeleportType.TELETABS;
        }
        this.teleportType = teleportType;
        entity.getWalkingQueue().reset();
        lastTeleport = currentTeleport;
        currentTeleport = type.getPulse(entity, location);
        entity.getPulseManager().clear();
        if (type == TeleportType.HOME) {
            entity.getPulseManager().run(type.getPulse(entity, location));
        } else {
            entity.lock(12);
            entity.getImpactHandler().setDisabledTicks(teleportType == -1 ? 5 : 12);
            GameWorld.getPulser().submit(currentTeleport);
        }
        if (entity instanceof Player) {
            ((Player) entity).getInterfaceManager().close();
        }
        return true;
    }

    /**
     * Fires a random event.
     *
     * @param entity   The entity teleporting.
     * @param location The destination lcoation.
     */
    public static void fireRandom(Entity entity, Location location) {
        if (entity instanceof Player && entity.getTeleporter().getTeleportType() == 0) {
            Player p = (Player) entity;
        }
    }

    /**
     * Get the home teleport audio based on tick count.
     *
     * @param count
     */
    private static int getAudio(int count) {
        switch (count) {
            case 0:
                return 193;
            case 4:
                return 194;
            case 11:
                return 195;
        }
        return -1;
    }

    /**
     * Gets entity.
     *
     * @return the entity
     */
    public final Entity getEntity() {
        return entity;
    }

    /**
     * Gets last teleport.
     *
     * @return the last teleport
     */
    public final Pulse getLastTeleport() {
        return lastTeleport;
    }

    /**
     * Gets current teleport.
     *
     * @return the current teleport
     */
    public final Pulse getCurrentTeleport() {
        return currentTeleport;
    }

    /**
     * Represents a NodeType for Teleporter.
     *
     * @author SonicForce41
     */
    public enum TeleportType {
        NORMAL(new TeleportSettings(8939, 8941, 1576, 1577)) {
            @Override
            public Pulse getPulse(final Entity entity, final Location location) {
                return new TeleportPulse(entity) {
                    int delay = 0;

                    @Override
                    public boolean pulse() {
                        if (delay == 0) {
                            playGlobalAudio(entity.getLocation(), Sounds.TP_ALL_200);
                            entity.getAnimator().forceAnimation(new Animation(getSettings().getStartEmote()));
                            entity.graphics(new Graphics(getSettings().getStartGfx()));
                        } else if (delay == 3) {
                            entity.getProperties().setTeleportLocation(Location.create(location));
                            fireRandom(entity, location);
                        } else if (delay == 4) {
                            playGlobalAudio(entity.getLocation(), Sounds.TP_REVERSE_201);
                            entity.getAnimator().forceAnimation(new Animation(getSettings().getEndEmote(), Priority.HIGH));
                            entity.graphics(new Graphics(getSettings().getEndGfx()));
                            return true;
                        }
                        delay++;
                        return false;
                    }

                    @Override
                    public void stop() {
                        super.stop();
                        entity.unlock();
                        entity.lock(4);
                    }
                };
            }
        }, ANCIENT(new TeleportSettings(1979, -1, 392, -1)) {
            @Override
            public Pulse getPulse(final Entity entity, final Location location) {
                return new TeleportPulse(entity) {
                    int delay = 0;

                    @Override
                    public boolean pulse() {
                        if (delay == 0) {
                            playGlobalAudio(entity.getLocation(), Sounds.BLOCK_TP_197, 0, 7);
                            entity.getAnimator().forceAnimation(new Animation(getSettings().getStartEmote()));
                            entity.graphics(new Graphics(getSettings().getStartGfx()));
                        } else if (delay == 4) {
                            entity.getProperties().setTeleportLocation(Location.create(location));
                            fireRandom(entity, location);
                        } else if (delay == 5) {
                            entity.getAnimator().forceAnimation(new Animation(getSettings().getEndEmote(), Priority.HIGH));
                            entity.graphics(new Graphics(getSettings().getEndGfx()));
                            return true;
                        }
                        delay++;
                        return false;
                    }

                    @Override
                    public void stop() {
                        super.stop();
                        entity.getAnimator().forceAnimation(new Animation(-1));
                        entity.graphics(new Graphics(-1));
                        entity.unlock();
                    }
                };
            }
        }, LUNAR(new TeleportSettings(1816, -1, 747, -1)) {
            @Override
            public Pulse getPulse(final Entity entity, final Location location) {
                return new TeleportPulse(entity) {
                    int delay = 0;

                    @Override
                    public boolean pulse() {
                        if (delay == 0) {
                            entity.graphics(new Graphics(getSettings().getStartGfx(), 120));
                            entity.getAnimator().forceAnimation(new Animation(getSettings().getStartEmote()));
                        } else if (delay == 3) {
                            entity.getProperties().setTeleportLocation(Location.create(location));
                            fireRandom(entity, location);
                        } else if (delay == 4) {
                            entity.getAnimator().forceAnimation(new Animation(getSettings().getEndEmote(), Priority.HIGH));
                            entity.graphics(new Graphics(getSettings().getEndGfx()));
                            return true;
                        }
                        delay++;
                        return false;
                    }

                    @Override
                    public void stop() {
                        super.stop();
                        entity.getAnimator().forceAnimation(new Animation(-1));
                        entity.graphics(new Graphics(-1));
                        entity.unlock();
                    }
                };
            }
        }, TELETABS(new TeleportSettings(4731, -1, 678, -1)) {
            @Override
            public Pulse getPulse(final Entity entity, final Location location) {
                return new TeleportPulse(entity) {
                    int delay = 0;

                    @Override
                    public boolean pulse() {
                        if (delay == 0) {
                            playGlobalAudio(entity.getLocation(), Sounds.POH_TABLET_BREAK_979);
                            entity.getAnimator().forceAnimation(new Animation(4069));
                        } else if (delay == 2) {
                            entity.getAnimator().forceAnimation(new Animation(getSettings().getStartEmote(), Priority.HIGH));
                            entity.graphics(new Graphics(getSettings().getStartGfx()));
                        } else if (delay == 3) {
                            entity.getProperties().setTeleportLocation(Location.create(location));
                        } else if (delay == 4) {
                            entity.getAnimator().forceAnimation(new Animation(getSettings().getEndEmote()));
                            entity.graphics(new Graphics(getSettings().getEndGfx()));
                            return true;
                        }
                        delay++;
                        return false;
                    }

                    @Override
                    public void stop() {
                        super.stop();
                        entity.getAnimator().forceAnimation(new Animation(-1));
                        entity.graphics(new Graphics(-1));
                        entity.unlock();
                        entity.lock(2);
                    }
                };
            }
        }, HOME(new TeleportSettings(4847, 4857, 800, 804)) {
            @Override
            public Pulse getPulse(final Entity entity, final Location location) {
                return new TeleportPulse(entity) {
                    int count;
                    Player player;

                    @Override
                    public boolean pulse() {
                        if (count == 18) {
                            player.getProperties().setTeleportLocation(location);
                            return true;
                        } else {
                            playGlobalAudio(entity.getLocation(), getAudio(count));
                            player.getPacketDispatch().sendGraphic(HOME_GRAPHICS[count]);
                            player.getPacketDispatch().sendAnimation(HOME_ANIMATIONS[count]);
                        }
                        count++;
                        return false;
                    }

                    @Override
                    public void start() {
                        player = ((Player) entity);
                        /*
                        if (player.getSavedData().globalData.getHomeTeleportDelay() > System.currentTimeMillis()) {
                            long milliseconds = player.getSavedData().globalData.getHomeTeleportDelay() - System.currentTimeMillis();
                            int minutes = Math.round((float) milliseconds / 120000);
                            if (minutes > 15) {
                                player.getSavedData().globalData.setHomeTeleportDelay(System.currentTimeMillis() + 1200000);
                                milliseconds = player.getSavedData().globalData.getHomeTeleportDelay() - System.currentTimeMillis();
                                minutes = Math.round((float) milliseconds / 120000);
                            }
                            if (minutes != 0) {
                                player.getPacketDispatch().sendMessage("You need to wait another " + minutes + " " + (minutes == 1 ? "minute" : "minutes") + " to cast this spell.");
                                stop();
                                return;
                            }
                        }
                        */
                        super.start();
                    }

                    @Override
                    public void stop() {
                        super.stop();
                        if (player.getAttribute(TutorialStage.TUTORIAL_STAGE, -1) == 72) {
                            Player p = entity.asPlayer();
                            completeTutorial(p);
                        }
                        entity.getAnimator().forceAnimation(new Animation(-1));
                        player.graphics(new Graphics(-1));
                    }
                };
            }
        }, OBELISK(new TeleportSettings(8939, 8941, 661, -1)) {
            @Override
            public Pulse getPulse(final Entity entity, final Location location) {
                return new TeleportPulse(entity) {
                    int delay = 0;

                    @Override
                    public boolean pulse() {
                        if (delay == 0) {
                            entity.lock();
                            entity.getAnimator().forceAnimation(new Animation(1816));
                        } else if (delay == 3) {
                            entity.getProperties().setTeleportLocation(Location.create(location));
                        } else if (delay == 4) {
                            entity.getAnimator().forceAnimation(Animation.RESET);
                            entity.unlock();
                            return true;
                        }
                        delay++;
                        return false;
                    }
                };
            }
        }, TELE_OTHER(new TeleportSettings(1816, -1, 342, -1)) {
            @Override
            public Pulse getPulse(final Entity entity, final Location location) {
                return new TeleportPulse(entity) {
                    int delay = 0;

                    @Override
                    public boolean pulse() {
                        if (delay == 0) {
                            playGlobalAudio(entity.getLocation(), Sounds.TELE_OTHER_CAST_199);
                            entity.getAnimator().forceAnimation(new Animation(getSettings().getStartEmote()));
                            entity.graphics(new Graphics(getSettings().getStartGfx()));
                        } else if (delay == 3) {
                            entity.getProperties().setTeleportLocation(Location.create(location));
                        } else if (delay == 4) {
                            entity.getAnimator().forceAnimation(new Animation(-1));
                            entity.unlock();
                            return true;
                        }
                        delay++;
                        return false;
                    }
                };
            }
        }, FAIRY_RING(new TeleportSettings(-1, -1, -1, -1)) {
            @Override
            public Pulse getPulse(final Entity entity, final Location location) {
                entity.graphics(Graphics.create(569));
                return new TeleportPulse(entity) {
                    int delay;

                    @Override
                    public boolean pulse() {
                        switch (++delay) {
                            case 2:
                                entity.animate(Animation.create(3265));
                                if (entity instanceof Player) {
                                    playAudio(entity.asPlayer(), Sounds.FT_FAIRY_TP_1098);
                                }
                                break;
                            case 4:
                                entity.animate(Animation.create(3266));
                                entity.getProperties().setTeleportLocation(location);
                                entity.unlock();
                                entity.lock(2);
                                return true;
                        }
                        return false;
                    }

                };
            }
        }, PURO_PURO(new TeleportSettings(6601, 1118, -1, -1)) {
            @Override
            public Pulse getPulse(final Entity entity, final Location location) {
                return new TeleportPulse(entity) {
                    int delay = 0;

                    @Override
                    public boolean pulse() {
                        if (delay == 0) {
                            entity.getAnimator().forceAnimation(new Animation(getSettings().getStartEmote()));
                            entity.graphics(new Graphics(1118));
                        } else if (delay == 9) {
                            entity.getProperties().setTeleportLocation(Location.create(location));
                            entity.getAnimator().forceAnimation(new Animation(-1));
                            entity.unlock();
                            return true;
                        }
                        delay++;
                        return false;
                    }
                };
            }
        }, ECTOPHIAL(new TeleportSettings(8939, 8941, 1587, 1588)) {
            @Override
            public Pulse getPulse(final Entity entity, final Location location) {
                return new TeleportPulse(entity) {
                    int delay = 0;

                    @Override
                    public boolean pulse() {
                        if (delay == 0) {
                            playGlobalAudio(entity.getLocation(), Sounds.TP_ALL_200);
                            entity.getAnimator().forceAnimation(new Animation(getSettings().getStartEmote()));
                            entity.graphics(new Graphics(getSettings().getStartGfx()));
                        } else if (delay == 3) {
                            entity.getProperties().setTeleportLocation(Location.create(location));
                            fireRandom(entity, location);
                        } else if (delay == 4) {
                            playGlobalAudio(entity.getLocation(), Sounds.TP_REVERSE_201);
                            entity.getAnimator().forceAnimation(new Animation(getSettings().getEndEmote(), Priority.HIGH));
                            entity.graphics(new Graphics(getSettings().getEndGfx()));
                            return true;
                        }
                        delay++;
                        return false;
                    }
                };
            }
        }, CHRISTMAS(new TeleportSettings(7534, -1, 1292, -1)) {
            @Override
            public Pulse getPulse(final Entity entity, final Location location) {
                return new TeleportPulse(entity) {
                    int delay = 0;

                    @Override
                    public boolean pulse() {
                        if (delay == 0) {
                            playGlobalAudio(entity.getLocation(), Sounds.TP_ALL_200);
                            entity.getAnimator().forceAnimation(new Animation(getSettings().getStartEmote()));
                            entity.graphics(new Graphics(getSettings().getStartGfx()));
                        } else if (delay == 3) {
                            entity.getProperties().setTeleportLocation(Location.create(location));
                            fireRandom(entity, location);
                        } else if (delay == 4) {
                            playGlobalAudio(entity.getLocation(), Sounds.TP_REVERSE_201);
                            entity.getAnimator().forceAnimation(new Animation(getSettings().getEndEmote(), Priority.HIGH));
                            entity.graphics(new Graphics(getSettings().getEndGfx()));
                            return true;
                        }
                        delay++;
                        return false;
                    }

                    @Override
                    public void stop() {
                        super.stop();
                        entity.unlock();
                        entity.lock(4);
                    }
                };
            }
        }, CABBAGE(new TeleportSettings(9984, 9986, 1731, 1732)) {
            @Override
            public Pulse getPulse(final Entity entity, final Location location) {
                return new TeleportPulse(entity) {
                    int delay = 0;

                    @Override
                    public boolean pulse() {
                        if (delay == 0) {
                            if (entity instanceof Player) {
                                playAudio(entity.asPlayer(), 5036);
                            }
                            entity.getAnimator().forceAnimation(new Animation(getSettings().getStartEmote()));
                            entity.graphics(new Graphics(getSettings().getStartGfx()));
                        } else if (delay == 5) {
                            entity.getProperties().setTeleportLocation(Location.create(location));
                            fireRandom(entity, location);
                            if (entity instanceof Player) {
                                playAudio(entity.asPlayer(), 5034);
                            }
                            entity.getAnimator().forceAnimation(new Animation(getSettings().getEndEmote(), Priority.HIGH));
                            entity.graphics(new Graphics(getSettings().getEndGfx()));
                            return true;
                        }
                        delay++;
                        return false;
                    }

                    @Override
                    public void stop() {
                        super.stop();
                        entity.unlock();
                        entity.lock(4);
                    }
                };
            }
        }, ENTRANA_MAGIC_DOOR(new TeleportSettings(10100, 9013, 1745, 1747)) { //

            @Override
            public Pulse getPulse(final Entity entity, final Location location) {
                return new TeleportPulse(entity) {
                    int delay = 0;

                    @Override
                    public boolean pulse() {
                        if (delay == 0) {
                            playGlobalAudio(entity.getLocation(), Sounds.TP_ALL_200);
                            entity.getAnimator().forceAnimation(new Animation(getSettings().getStartEmote()));
                            entity.graphics(new Graphics(getSettings().getStartGfx()));
                        } else if (delay == 3) {
                            entity.getProperties().setTeleportLocation(Location.create(location));
                            fireRandom(entity, location);
                        } else if (delay == 4) {
                            playGlobalAudio(entity.getLocation(), Sounds.TP_REVERSE_201);
                            entity.getAnimator().forceAnimation(new Animation(getSettings().getEndEmote(), Priority.HIGH));
                            entity.graphics(new Graphics(getSettings().getEndGfx()));
                            return true;
                        }
                        delay++;
                        return false;
                    }

                    @Override
                    public void stop() {
                        super.stop();
                        entity.unlock();
                        entity.lock(4);
                    }
                };
            }
        }, RANDOM_EVENT_OLD(new TeleportSettings(714, -1, -1, -1)) {
            @Override
            public Pulse getPulse(final Entity entity, final Location location) {
                return new TeleportPulse(entity) {
                    int delay = 0;
                    Player player;

                    @Override
                    public boolean pulse() {
                        if (delay == 0) {
                            entity.getAnimator().forceAnimation(new Animation(getSettings().getStartEmote()));
                            entity.graphics(new Graphics(308, 100, 50));
                            if (entity instanceof Player) {
                                playAudio(entity.asPlayer(), Sounds.TP_ALL_200);
                            }
                        } else if (delay == 4) {
                            entity.getProperties().setTeleportLocation(Location.create(location));
                            entity.getAnimator().forceAnimation(new Animation(-1));
                            entity.unlock();
                            return true;
                        }
                        delay++;
                        return false;
                    }

                    @Override
                    public void start() {

                        super.start();
                    }

                    @Override
                    public void stop() {
                        super.stop();
                        entity.getAnimator().forceAnimation(new Animation(-1));
                        entity.graphics(new Graphics(-1));
                    }
                };
            }
        }, MINIGAME(new TeleportSettings(6601, 1118, -1, -1)) {
            @Override
            public Pulse getPulse(final Entity entity, final Location location) {
                return new TeleportPulse(entity) {
                    int delay = 0;
                    Player player;

                    @Override
                    public boolean pulse() {
                        if (delay == 0) {
                            entity.getAnimator().forceAnimation(new Animation(getSettings().getStartEmote()));
                            entity.graphics(new Graphics(1118));
                        } else if (delay == 9) {
                            entity.getProperties().setTeleportLocation(Location.create(location));
                            entity.getAnimator().forceAnimation(new Animation(-1));
                            entity.unlock();
                            return true;
                        }
                        delay++;
                        return false;
                    }

                    @Override
                    public void start() {

                        super.start();
                    }

                    @Override
                    public void stop() {
                        super.stop();
                        entity.getAnimator().forceAnimation(new Animation(-1));
                        entity.graphics(new Graphics(-1));
                    }
                };
            }
        }, PHARAOH_SCEPTRE(new TeleportSettings(714, 715, -1, -1)) {
            @Override
            public Pulse getPulse(final Entity entity, final Location location) {
                return new TeleportPulse(entity) {
                    int delay = 0;
                    Player player;

                    @Override
                    public boolean pulse() {
                        if (delay == 0) {
                            entity.getAnimator().forceAnimation(new Animation(getSettings().getStartEmote()));
                            entity.graphics(new Graphics(715));
                        } else if (delay == 4) {
                            entity.getProperties().setTeleportLocation(Location.create(location));
                            entity.getAnimator().forceAnimation(new Animation(-1));
                            entity.unlock();
                            return true;
                        }
                        delay++;
                        return false;
                    }

                    @Override
                    public void start() {
                        super.start();
                    }

                    @Override
                    public void stop() {
                        super.stop();
                        entity.getAnimator().forceAnimation(new Animation(-1));
                        entity.graphics(new Graphics(-1));
                    }
                };
            }
        }, INSTANT(new TeleportSettings(-1, -1, -1, -1)) {
            @Override
            public Pulse getPulse(final Entity entity, final Location location) {
                return new TeleportPulse(entity) {
                    int delay = 0;

                    @Override
                    public boolean pulse() {
                        if (delay == 0) {
                            entity.lock();
                        } else if (delay == 3) {
                            entity.getProperties().setTeleportLocation(Location.create(location));
                        } else if (delay == 4) {
                            entity.getAnimator().forceAnimation(Animation.RESET);
                            entity.unlock();
                            return true;
                        }
                        delay++;
                        return false;
                    }
                };
            }
        };

        /**
         * The NodeSettings
         */
        private TeleportSettings settings;

        /**
         * Gets pulse.
         *
         * @param entity   the entity
         * @param location the location
         * @return the pulse
         */
        public abstract Pulse getPulse(final Entity entity, final Location location);

        TeleportType(TeleportSettings settings) {
            this.settings = settings;
        }

        /**
         * Gets settings.
         *
         * @return the settings
         */
        public final TeleportSettings getSettings() {
            return settings;
        }
    }

    /**
     * Represents teleport node settings
     *
     * @author SonicForce41
     */
    static class TeleportSettings {
        /**
         * The start animation.
         */
        private int startAnim;

        /**
         * The end animation.
         */
        private int endAnim;

        /**
         * The start graphics.
         */
        private int startGFX;

        /**
         * The end gfx.
         */
        private int endGFX;

        /**
         * Constructs a new {@code Teleporter.java} {@code Object}.
         *
         * @param startAnim the start animation.
         * @param endAnim   the end animation.
         * @param startGfx  the start graphics.
         * @param endGfx    the end graphiics.
         */
        public TeleportSettings(int startAnim, int endAnim, int startGfx, int endGfx) {
            this.startAnim = startAnim;
            this.endAnim = endAnim;
            this.startGFX = startGfx;
            this.endGFX = endGfx;
        }

        /**
         * Gets start emote.
         *
         * @return the start emote
         */
        public final int getStartEmote() {
            return startAnim;
        }

        /**
         * Gets end emote.
         *
         * @return the end emote
         */
        public final int getEndEmote() {
            return endAnim;
        }

        /**
         * Gets start gfx.
         *
         * @return the start gfx
         */
        public final int getStartGfx() {
            return startGFX;
        }

        /**
         * Gets end gfx.
         *
         * @return the end gfx
         */
        public final int getEndGfx() {
            return endGFX;
        }
    }

    /**
     * Gets the teleporting type for the player depending on spellbook.
     *
     * @param player The player.
     * @return The teleport type.
     */
    public static TeleportType getType(Player player) {
        switch (player.getSpellBookManager().getSpellBook()) {
            case 193:
                return TeleportType.ANCIENT;
            case 430:
                return TeleportType.LUNAR;
        }
        return TeleportType.NORMAL;
    }

    /**
     * Gets teleport type.
     *
     * @return the teleport type
     */
    public int getTeleportType() {
        return teleportType;
    }

    /**
     * Sets teleport type.
     *
     * @param teleportType the teleport type to set.
     */
    public void setTeleportType(int teleportType) {
        this.teleportType = teleportType;
    }

    /**
     * Completes the tutorial.
     *
     * @param player the player for whom the tutorial complete.
     */
    public static void completeTutorial(Player player) {
        setVarbit(player, TutorialStage.FLASHING_ICON, 0);
        setVarp(player, 281, 1000, true);
        setAttribute(player, "/save:tutorial:complete", true);
        setAttribute(player, "/save:tutorial:stage", 73);

        player.unhook(TutorialCastReceiver.INSTANCE);
        player.unhook(TutorialKillReceiver.INSTANCE);
        player.unhook(TutorialFireReceiver.INSTANCE);
        player.unhook(TutorialResourceReceiver.INSTANCE);
        player.unhook(TutorialUseWithReceiver.INSTANCE);
        player.unhook(TutorialInteractionReceiver.INSTANCE);
        player.unhook(TutorialButtonReceiver.INSTANCE);

        if (GameWorld.getSettings() != null && GameWorld.getSettings().getEnable_default_clan()) {
            player.getCommunication().setCurrentClan(ServerConstants.SERVER_NAME);
            JoinClanRequest.Builder clanJoin = JoinClanRequest.newBuilder();
            clanJoin.setClanName(ServerConstants.SERVER_NAME);
            clanJoin.setUsername(player.getName());
            ManagementEvents.publish(clanJoin.build());
        }

        closeOverlay(player);
        player.getInventory().clear();
        player.getBank().clear();
        player.getEquipment().clear();
        player.getInventory().add(TutorialStage.STARTER_PACK);
        player.getBank().add(TutorialStage.STARTER_BANK);
        player.getInterfaceManager().restoreTabs();
        player.getInterfaceManager().setViewedTab(3);
        player.getInterfaceManager().openDefaultTabs();
        player.getDialogueInterpreter().sendDialogues(
                "Welcome to Lumbridge! To get more help, simply click on the",
                "Lumbridge Guide or one of the Tutors - these can be found by",
                "looking for the question mark icon on your minimap. If you find you",
                "are lost at any time, look for a signpost or use the Lumbridge Home",
                "Teleport spell."
        );
        setAttribute(player, "close_c_", true);
    }
}

