package content.region.misthalin.varrock.quest.surok.plugin;

import core.game.activity.ActivityPlugin;
import core.game.activity.CutscenePlugin;
import core.game.dialogue.FaceAnim;
import core.game.interaction.Option;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.map.build.DynamicRegion;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.net.packet.PacketRepository;
import core.net.packet.context.CameraContext;
import core.net.packet.out.CameraViewPacket;
import shared.consts.Music;
import shared.consts.Quests;

import static core.api.ContentAPIKt.removeAttribute;
import static core.api.ContentAPIKt.setAttribute;

/**
 * The type What lies below cutscene.
 */
public class WhatLiesBelowCutscene extends CutscenePlugin {

    private NPC SUROK_NPC;

    private NPC KING;

    private NPC ZAFF;

    /**
     * Instantiates a new What lies below cutscene.
     */
    public WhatLiesBelowCutscene() {
        super(Quests.WHAT_LIES_BELOW);
    }

    /**
     * Instantiates a new What lies below cutscene.
     *
     * @param player the player
     */
    public WhatLiesBelowCutscene(Player player) {
        this();
        this.player = player;
        this.setUid(getName().hashCode());
    }

    @Override
    public boolean start(final Player player, boolean login, java.lang.Object... args) {
        player.lock();
        setAttribute(player, "cutscene", this);
        Scenery table = RegionManager.getObject(base.transform(9, 37, 0));
        if (table != null) {
            SceneryBuilder.remove(table);
        }
        player.getMusicPlayer().unlock(Music.SUROKS_THEME_250);
        player.getMusicPlayer().unlock(Music.COMPLICATION_142);
        SUROK_NPC = NPC.create(5835, base.transform(8, 39, 0));
        SUROK_NPC.init();
        KING = KingRoaldNPC.create(5838, base.transform(10, 34, 0));
        KING.lock();
        ((KingRoaldNPC) KING).setCutscene(this);
        KING.init();
        KING.face(player);
        SUROK_NPC.face(KING);
        player.face(SUROK_NPC);
        player.getDialogueInterpreter().open(5834, SUROK_NPC, this);
        return super.start(player, login, args);
    }

    @Override
    public void open() {
        player.lock();
        sendCamera(0, -6, 0, 0, 500, 100);
        SUROK_NPC.animate(Animation.create(1084));
        SUROK_NPC.graphics(Graphics.create(108));
        SUROK_NPC.sendChat("Annach Narh Hin Dei!");
        sendCamera(0, 3, 0, 0, 450, 95, 5);
        KING.animate(Animation.create(860), 7);
        KING.sendChat("What's going on?", 6);
        KING.sendChat("I...must...kill..." + player.getUsername() + "!!", 9);
        player.sendChat("Uh oh! King Roald looks evil!", 13);
        GameWorld.getPulser().submit(new Pulse(13) {
            @Override
            public boolean pulse() {
                reset();
                player.face(KING);
                player.getDialogueInterpreter().sendDialogues(player, FaceAnim.ANNOYED, "Uh oh! King Roald looks evil!");
                return true;
            }
        });
        super.open();
    }

    @Override
    public void end() {
        super.end();
        removeAttribute(player, "cutscene");
        player.getDialogueInterpreter().close();
        if (player.getDialogueInterpreter().getDialogue() != null) {
            player.getDialogueInterpreter().getDialogue().end();
        }
    }

    @Override
    public boolean interact(Entity entity, Node target, Option option) {
        if (entity instanceof Player) {
            switch (target.getId()) {
                case 5835:
                    if (player.getAttribute("can-arrest", false)) {
                        Location loc = base.transform(9, 34, 0);
                        PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraContext.CameraType.POSITION, loc.getX(), loc.getY(), 450, 1, 100));
                        PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraContext.CameraType.ROTATION, loc.getX(), loc.getY(), 450, 1, 100));
                        ZAFF.face(SUROK_NPC);
                        SUROK_NPC.face(ZAFF);
                        player.lock();
                        SUROK_NPC.animate(Animation.create(1084));
                        SUROK_NPC.sendChat("Mirra din namus!!", 1);
                        player.sendMessage("Surok looks like he's trying to teleport away!");
                        ZAFF.sendChat("Stop!!", 3);
                        SUROK_NPC.sendChat("Nooooooooooooo!", 6);
                        GameWorld.getPulser().submit(new Pulse(3, player) {

                            @Override
                            public boolean pulse() {
                                SUROK_NPC.animate(Animation.create(6098));
                                ZAFF.animate(Animation.create(1819));
                                Projectile.magic(ZAFF, SUROK_NPC, 109, 60, 36, 36, 10).send();
                                return true;
                            }

                        });
                        GameWorld.getPulser().submit(new Pulse(9, player) {

                            @Override
                            public boolean pulse() {
                                player.getDialogueInterpreter().open(5834, SUROK_NPC, this, true);
                                return true;
                            }

                        });
                        return true;
                    }
                    return true;
                case 11014:
                    if (option.name.equalsIgnoreCase("operate") || option.name.equalsIgnoreCase("summon")) {
                        if (KING.getAttribute("message", false)) {
                            summonZaff();
                            return true;
                        }
                    }
                    break;
                case 15536:
                    return true;
            }
        }
        return super.interact(entity, target, option);
    }

    @Override
    public boolean teleport(Entity e, int type, Node node) {
        if (type != -1) {
            return false;
        }
        return super.teleport(e, type, node);
    }

    /**
     * Summon zaff.
     */
    public void summonZaff() {
        if (!KING.getAttribute("message", false)) {
            player.sendMessage("Zaff isn't ready to be summoned.");
            return;
        }
        player.lock();
        KING.lock();
        KING.getProperties().getCombatPulse().stop();
        player.getProperties().getCombatPulse().stop();
        player.getInterfaceManager().removeTabs(getRemovedTabs());
        ZAFF = NPC.create(5836, player.getLocation());
        Location loc = RegionManager.getSpawnLocation(player, ZAFF);
        if (loc != null) {
            ZAFF.setLocation(loc);
        }
        ZAFF.init();
        ZAFF.graphics(Graphics.create(110));
        if (ZAFF.getLocation().equals(KING.getLocation()) || ZAFF.getLocation().equals(SUROK_NPC.getLocation())) {
            ZAFF.moveStep();
        }
        ZAFF.face(KING);
        KING.face(ZAFF);
        ZAFF.animate(Animation.create(5633));
        ZAFF.sendChat("Sin danna nim borha!!", 2);
        KING.animate(Animation.create(6099), 5);
        ZAFF.graphics(Graphics.create(108), 3);
        KING.graphics(Graphics.create(110), 8);
        KING.sendChat("Wh...!", 7);
        GameWorld.getPulser().submit(new Pulse(9, player) {

            @Override
            public boolean pulse() {
                KING.clear();
                player.unlock();
                setAttribute(player, "can-arrest", true);
                player.getDialogueInterpreter().sendDialogues(ZAFF, null, "The king's mind has been restored to him and he has", "been teleported away to safety. Now, to deal with", "Surok!");
                return true;
            }

        });
    }

    /**
     * Send camera.
     *
     * @param x1     the x 1
     * @param y1     the y 1
     * @param x2     the x 2
     * @param y2     the y 2
     * @param height the height
     * @param speed  the speed
     * @param ticks  the ticks
     */
    public void sendCamera(final int x1, final int y1, final int x2, final int y2, final int height, final int speed, int ticks) {
        GameWorld.getPulser().submit(new Pulse(ticks, player) {

            @Override
            public boolean pulse() {
                sendCamera(x1, y1, x2, y2, height, speed);
                return true;
            }

        });
    }

    /**
     * Send camera.
     *
     * @param x1     the x 1
     * @param y1     the y 1
     * @param x2     the x 2
     * @param y2     the y 2
     * @param height the height
     * @param speed  the speed
     */
    public void sendCamera(int x1, int y1, int x2, int y2, int height, int speed) {
        PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraContext.CameraType.POSITION, player.getLocation().getX() + x1, player.getLocation().getY() + y1, height, 1, speed));
        PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraContext.CameraType.ROTATION, player.getLocation().getX() + x2, player.getLocation().getY() + y2, height, 1, speed));
    }

    /**
     * Reset.
     *
     * @param ticks the ticks
     */
    public void reset(int ticks) {
        GameWorld.getPulser().submit(new Pulse(1, player) {

            @Override
            public boolean pulse() {
                reset();
                return true;
            }

        });
    }

    /**
     * Reset.
     */
    public void reset() {
        PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraContext.CameraType.RESET, 0, 0, 0, 0, 0));
    }

    @Override
    public ActivityPlugin newInstance(Player p) throws Throwable {
        return new WhatLiesBelowCutscene(p);
    }

    @Override
    public Location getStartLocation() {
        return base.transform(10, 38, 0);
    }

    @Override
    public Location getSpawnLocation() {
        return null;
    }

    @Override
    public void configure() {
        region = DynamicRegion.create(12854);
        setRegionBase();
        registerRegion(region.getId());
    }

    /**
     * Gets surok.
     *
     * @return the surok
     */
    public NPC getSUROK_NPC() {
        return SUROK_NPC;
    }

    /**
     * Sets surok.
     *
     * @param SUROK_NPC the surok
     */
    public void setSUROK_NPC(NPC SUROK_NPC) {
        this.SUROK_NPC = SUROK_NPC;
    }

    /**
     * Gets king.
     *
     * @return the king
     */
    public NPC getKING() {
        return KING;
    }

    /**
     * Sets king.
     *
     * @param KING the king
     */
    public void setKING(NPC KING) {
        this.KING = KING;
    }

    /**
     * Gets zaff.
     *
     * @return the zaff
     */
    public NPC getZAFF() {
        return ZAFF;
    }

    /**
     * Sets zaff.
     *
     * @param ZAFF the zaff
     */
    public void setZAFF(NPC ZAFF) {
        this.ZAFF = ZAFF;
    }

}
