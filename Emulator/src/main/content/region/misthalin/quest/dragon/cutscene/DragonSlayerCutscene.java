package content.region.misthalin.quest.dragon.cutscene;

import content.global.travel.charter.Charter;
import core.game.activity.ActivityManager;
import core.game.activity.ActivityPlugin;
import core.game.activity.CutscenePlugin;
import core.game.component.Component;
import core.game.dialogue.Dialogue;
import core.game.node.entity.combat.ImpactHandler.HitsplatType;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.quest.Quest;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.map.build.DynamicRegion;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.net.packet.PacketRepository;
import core.net.packet.context.CameraContext;
import core.net.packet.context.CameraContext.CameraType;
import core.net.packet.context.MinimapStateContext;
import core.net.packet.out.CameraViewPacket;
import core.net.packet.out.MinimapState;
import org.rs.consts.Animations;
import org.rs.consts.Quests;

import java.util.ArrayList;
import java.util.List;

import static core.api.ContentAPIKt.setAttribute;
import static core.api.ContentAPIKt.setVarp;

/**
 * The type Dragon slayer cutscene.
 */
public final class DragonSlayerCutscene extends CutscenePlugin {

    private static final Animation ANIMATION = new Animation(Animations.GET_UP_FROM_BEING_FACE_DOWN_ON_FLOOR_DRAGON_SLAYER_4191);

    private final BobingPulse bobinPulse = new BobingPulse();

    /**
     * Instantiates a new Dragon slayer cutscene.
     */
    public DragonSlayerCutscene() {
        this(null);
    }

    /**
     * Instantiates a new Dragon slayer cutscene.
     *
     * @param p the p
     */
    public DragonSlayerCutscene(final Player p) {
        super(Quests.DRAGON_SLAYER, true);
        this.player = p;
    }

    @Override
    public ActivityPlugin newInstance(Player p) throws Throwable {
        return new DragonSlayerCutscene(p);
    }

    @Override
    public boolean start(final Player player, final boolean login, Object... args) {
        npcs.add(NPC.create(918, base.transform(39, 6, 1)));
        npcs.add(NPC.create(6085, base.transform(39, 7, 1)));
        for (NPC n : npcs) {
            n.setWalks(false);
            n.init();
        }
        player.lock();
        return super.start(player, login, args);
    }

    @Override
    public void register() {
        new DSNedDialogue().init();
    }

    @Override
    public void open() {
        player.lock();
        player.getDialogueInterpreter().open(918, npcs.get(0), this);
        PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraType.POSITION, player.getLocation().getX() + 11, player.getLocation().getY() + 1, 190, 1, 100));
        PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraType.ROTATION, player.getLocation().getX() + 3, player.getLocation().getY(), 190, 1, 100));
        bob(new CameraContext(player, CameraType.POSITION, player.getLocation().getX() + 7, player.getLocation().getY() - 3, 250, 1, 1), new CameraContext(player, CameraType.ROTATION, player.getLocation().getX() + 4, player.getLocation().getY(), 210, 1, 1));
    }

    @Override
    public void end() {
        super.end();
        this.getBobinPulse().stop();
        player.getProperties().setTeleportLocation(Charter.PORT_SARIM_TO_CRANDOR.getLocation());
        player.getInterfaceManager().close();
        player.animate(ANIMATION);
        player.getDialogueInterpreter().close();
        player.getDialogueInterpreter().sendDialogue("You are knocked unconscious and later awake on an ash-strewn", "beach.");
        player.getQuestRepository().getQuest(Quests.DRAGON_SLAYER).setStage(player, 40);
        player.getSavedData().questData.setDragonSlayerAttribute("repaired", false);
        setVarp(player, 177, 8257540);
        setVarp(player, 176, 8);
        player.getSavedData().questData.setDragonSlayerPlanks(0);
        PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 0));
        PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraType.RESET, player.getLocation().getX() + 13, player.getLocation().getY() - 3, 250, 1, 100));
        player.lock(3);
    }

    @Override
    public Location getStartLocation() {
        return base.transform(38, 5, 1);
    }

    /**
     * Bob.
     *
     * @param position the position
     * @param rotation the rotation
     */
    public void bob(final CameraContext position, final CameraContext rotation) {
        PacketRepository.send(CameraViewPacket.class, position);
        PacketRepository.send(CameraViewPacket.class, rotation);
        if (bobinPulse.position == null || bobinPulse.rotation == null) {
            GameWorld.getPulser().submit(bobinPulse);
        }
        bobinPulse.position = position;
        bobinPulse.rotation = rotation;
    }

    /**
     * The type Bobing pulse.
     */
    public class BobingPulse extends Pulse {

        /**
         * The Alternate.
         */
        boolean alternate = false;

        private CameraContext position = null;

        private CameraContext rotation = null;

        private int counter = 0;

        /**
         * Instantiates a new Bobing pulse.
         */
        public BobingPulse() {
            super(1);
        }

        @Override
        public boolean pulse() {
            if (counter == 3) {
                if (alternate) {
                    PacketRepository.send(CameraViewPacket.class, position);
                    PacketRepository.send(CameraViewPacket.class, rotation);
                    alternate = false;
                } else {
                    PacketRepository.send(CameraViewPacket.class, position);
                    alternate = true;
                }
                counter = 0;
            }
            counter++;
            return false;
        }
    }

    @Override
    public Location getSpawnLocation() {
        return Location.create(3047, 3203, 0);
    }

    @Override
    public void configure() {
        region = DynamicRegion.create(7500);
        setRegionBase();
        registerRegion(region.getId());
    }

    @Override
    public Pulse getStartPulse() {
        PacketRepository.send(MinimapState.class, new MinimapStateContext(player, getMapState()));
        player.getInterfaceManager().removeTabs(getRemovedTabs());
        return new Pulse(1) {
            int counter = 0;

            @Override
            public boolean pulse() {
                switch (counter++) {
                    case 1:
                        player.getProperties().setTeleportLocation(getStartLocation());
                        break;
                    case 2:
                        player.getInterfaceManager().close();
                        player.unlock();
                        player.getWalkingQueue().reset();
                        player.getLocks().lockMovement(1000000);
                        open();
                        return true;
                }
                return false;
            }
        };
    }

    /**
     * Gets bobin pulse.
     *
     * @return the bobin pulse
     */
    public BobingPulse getBobinPulse() {
        return bobinPulse;
    }

    /**
     * The type Ds ned dialogue.
     */
    public static final class DSNedDialogue extends Dialogue {

        private static final Animation[] ANIMATIONS = new Animation[]{new Animation(2105), new Animation(4280), new Animation(6649)};

        private static final Graphics FIRE = new Graphics(453);

        private Quest quest;

        private CutscenePlugin cutscene;

        private final List<Location> fires = new ArrayList<>(20);

        private boolean finished = false;

        /**
         * Instantiates a new Ds ned dialogue.
         */
        public DSNedDialogue() {

        }

        /**
         * Instantiates a new Ds ned dialogue.
         *
         * @param player the player
         */
        public DSNedDialogue(Player player) {

            super(player);
        }

        @Override
        public Dialogue newInstance(Player player) {
            return new DSNedDialogue(player);
        }

        @Override
        public boolean open(Object... args) {
            npc = (NPC) args[0];
            quest = player.getQuestRepository().getQuest(Quests.DRAGON_SLAYER);
            if (args.length > 1) {
                cutscene = ((DragonSlayerCutscene) args[1]);
                npc("Ah it's good to feel that salt spray on my face once", "again!");
                stage = 500;
                return true;
            }
            switch (quest.getStage(player)) {
                case 40:
                    if (!player.getSavedData().questData.getDragonSlayerAttribute("repaired")) {
                        npc("The ship's in a sorry state. You'd better fix up the hole", "in the hull before we can go anywhere.");
                        stage = 100;
                    } else {
                        npc("Ah, it's good to be on board a ship again! No matter", "how long I live on land, a ship will always seem better.", "Are you ready to depart?");
                        stage = 0;
                    }
                    break;
                case 30:
                    npc("Ah, it's good to be on board a ship again! No matter", "how long I live on land, a ship will always seem better.", "Are you ready to depart?");
                    stage = 0;
                    break;
                default:
                    break;
            }
            return true;
        }

        @Override
        public boolean handle(int interfaceId, int buttonId) {
            switch (quest.getStage(player)) {
                case 40:
                case 30:
                    switch (stage) {
                        case 500:
                            ((DragonSlayerCutscene) cutscene).bob(new CameraContext(player, CameraType.POSITION, player.getLocation().getX() + 6, player.getLocation().getY() - 3, 350, 1, 1), new CameraContext(player, CameraType.ROTATION, player.getLocation().getX() + 2, player.getLocation().getY(), 190, 1, 1));
                            npc("And this is a mighty fine ship. She don't look much, but", "she handles like a dream.");
                            stage = 501;
                            break;
                        case 501:
                            ((DragonSlayerCutscene) cutscene).bob(new CameraContext(player, CameraType.POSITION, player.getLocation().getX() + 5, player.getLocation().getY() - 3, 350, 1, 1), new CameraContext(player, CameraType.ROTATION, player.getLocation().getX() + 1, player.getLocation().getY(), 350, 1, 1));
                            player.face(npc);
                            npc.face(player);
                            player("How much longer until we reach Crandor?");
                            stage = 502;
                            break;
                        case 502:
                            npc("Not long now! According to the chart, we'd be able to", "see Crandor if it wasn't for those clouds on the horizon.");
                            stage = 503;
                            break;
                        case 503:
                            player.getInterfaceManager().open(new Component(543));
                            interpreter.sendPlainMessage(true, "Clouds surround the ship.");
                            GameWorld.getPulser().submit(new Pulse(6, player) {
                                @Override
                                public boolean pulse() {
                                    player.getInterfaceManager().close();
                                    interpreter.sendDialogues(6085, null, "Looks like theres a storm coming up, cap'n. Soon we", "won't be able to see anything!");
                                    stage = 504;
                                    PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraType.POSITION, player.getLocation().getX() + 8, player.getLocation().getY() + 15, 1200, 1, 100));
                                    PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraType.ROTATION, player.getLocation().getX(), player.getLocation().getY(), 1200, 1, 100));
                                    ((DragonSlayerCutscene) cutscene).bob(new CameraContext(player, CameraType.POSITION, player.getLocation().getX() + 5, player.getLocation().getY() + 18, 1200, 1, 1), new CameraContext(player, CameraType.ROTATION, player.getLocation().getX() - 2, player.getLocation().getY(), 1200, 1, 1));
                                    return true;
                                }
                            });
                            break;
                        case 504:
                            ((DragonSlayerCutscene) cutscene).bob(new CameraContext(player, CameraType.POSITION, player.getLocation().getX() - 2, player.getLocation().getY() + 14, 1000, 1, 1), new CameraContext(player, CameraType.ROTATION, player.getLocation().getX() - 2, player.getLocation().getY() - 50, 1000, 1, 1));
                            npc("Oh, well. The weather has been so good up until now.");
                            stage = 505;
                            break;
                        case 505:
                            player.getInterfaceManager().open(new Component(545));
                            GameWorld.getPulser().submit(new Pulse(2, player) {
                                @Override
                                public boolean pulse() {
                                    player("Did you see that?");
                                    stage = 506;
                                    player.getInterfaceManager().close();
                                    return true;
                                }
                            });
                            break;
                        case 506:
                            npc("See what?");
                            stage = 507;
                            break;
                        case 507:
                            player("I thought I saw something up above us.");
                            stage = 508;
                            break;
                        case 508:
                            PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraType.POSITION, player.getLocation().getX() + 7, player.getLocation().getY() + 2, 400, 1, 100));
                            PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraType.ROTATION, player.getLocation().getX() - 3, player.getLocation().getY(), 400, 1, 100));
                            ((DragonSlayerCutscene) cutscene).bob(new CameraContext(player, CameraType.POSITION, player.getLocation().getX() + 8, player.getLocation().getY() + 5, 400, 1, 1), new CameraContext(player, CameraType.ROTATION, player.getLocation().getX() - 3, player.getLocation().getY(), 400, 1, 1));
                            close();
                            player.getPacketDispatch().sendPositionedGraphic(446, 70, 1, cutscene.getBase().transform(37, 5, 1));
                            player.getPacketDispatch().sendPositionedGraphic(446, 70, 2, cutscene.getBase().transform(36, 5, 1));
                            player.getPacketDispatch().sendPositionedGraphic(446, 70, 3, cutscene.getBase().transform(35, 5, 1));
                            GameWorld.getPulser().submit(new Pulse(1, player) {

                                @Override
                                public boolean pulse() {

                                    fires.add(Location.create(37, 5, 1));
                                    fires.add(Location.create(36, 5, 1));
                                    fires.add(Location.create(35, 5, 1));
                                    return true;
                                }

                            });
                            startFires();
                            npc.animate(ANIMATIONS[0]);
                            cutscene.getNPCS().get(1).animate(ANIMATIONS[1]);
                            npc("It's the dragon!");
                            npc.face(player);
                            player.face(npc);
                            stage = 509;
                            break;
                        case 509:
                            close();
                            player.getInterfaceManager().open(new Component(546));
                            GameWorld.getPulser().submit(new Pulse(1, player) {
                                int counter = 0;

                                @Override
                                public boolean pulse() {
                                    switch (counter++) {
                                        case 2:
                                            PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraType.POSITION, player.getLocation().getX() + 5, player.getLocation().getY() + 2, 400, 1, 100));
                                            PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraType.ROTATION, player.getLocation().getX() - 3, player.getLocation().getY(), 400, 1, 100));
                                            ((DragonSlayerCutscene) cutscene).bob(new CameraContext(player, CameraType.POSITION, player.getLocation().getX() + 7, player.getLocation().getY() + 2, 400, 1, 1), new CameraContext(player, CameraType.ROTATION, player.getLocation().getX() - 3, player.getLocation().getY(), 400, 1, 1));

                                            player.getPacketDispatch().sendPositionedGraphic(446, 20, 2, cutscene.getBase().transform(38, 7, 1));
                                            player.getPacketDispatch().sendPositionedGraphic(446, 20, 1, cutscene.getBase().transform(37, 7, 1));
                                            player.getPacketDispatch().sendPositionedGraphic(446, 20, 2, cutscene.getBase().transform(38, 6, 1));
                                            player.getPacketDispatch().sendPositionedGraphic(446, 20, 1, cutscene.getBase().transform(36, 6, 1));
                                            fires.add(cutscene.getBase().transform(39, 7, 1));
                                            fires.add(cutscene.getBase().transform(38, 7, 1));
                                            fires.add(cutscene.getBase().transform(37, 7, 1));
                                            fires.add(cutscene.getBase().transform(38, 6, 1));
                                            fires.add(cutscene.getBase().transform(36, 6, 1));
                                            player.getInterfaceManager().close();
                                            npc.animate(ANIMATIONS[1]);
                                            cutscene.getNPCS().get(1).animate(ANIMATIONS[0]);
                                            break;
                                        case 4:
                                            cutscene.getNPCS().get(1).animate(ANIMATIONS[2]);
                                            break;
                                        case 5:
                                            player.getPacketDispatch().sendPositionedGraphic(446, 20, 1, npc.getLocation());
                                            break;
                                        case 6:
                                            player.getPacketDispatch().sendPositionedGraphics(FIRE, cutscene.getNPCS().get(1).getLocation());
                                            cutscene.getNPCS().get(1).animate(new Animation(836));
                                            cutscene.getNPCS().get(1).getImpactHandler().manualHit(player, 10, HitsplatType.NORMAL);
                                            break;
                                        case 10:
                                            PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraType.POSITION, player.getLocation().getX() + 13, player.getLocation().getY() + 2, 400, 1, 40));
                                            PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraType.ROTATION, player.getLocation().getX() - 3, player.getLocation().getY(), 400, 1, 40));
                                            ((DragonSlayerCutscene) cutscene).bob(new CameraContext(player, CameraType.POSITION, player.getLocation().getX() + 2, player.getLocation().getY() + 2, 400, 1, 1), new CameraContext(player, CameraType.ROTATION, player.getLocation().getX() - 4, player.getLocation().getY(), 400, 1, 1));
                                            npc("We're going to sink!");
                                            stage = 510;
                                            return true;
                                    }
                                    return false;
                                }
                            });
                            break;
                        case 510:
                            player("Look! Land ahead!");
                            ((DragonSlayerCutscene) cutscene).bob(new CameraContext(player, CameraType.POSITION, player.getLocation().getX() + 2, player.getLocation().getY() + 3, 350, 1, 1), new CameraContext(player, CameraType.ROTATION, player.getLocation().getX() + 70, player.getLocation().getY() - 30, 350, 1, 1));
                            player.face(npc);
                            stage = 511;
                            break;
                        case 511:
                            npc.faceLocation(npc.getLocation().transform(-1, 0, 0));
                            player.faceLocation(player.getLocation().transform(-1, 0, 0));
                            npc("We're going to crash!");
                            stage = 512;
                            break;
                        case 512:
                            setAttribute(player, "real-end", Charter.PORT_SARIM_TO_CRANDOR.getLocation());
                            setAttribute(player, "cutscene:original-loc", Charter.PORT_SARIM_TO_CRANDOR.getLocation());
                            PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraType.SHAKE, 4, 4, 1200, 4, 4));
                            player.getDialogueInterpreter().sendPlainMessage(true, "<col=8A0808>CRASH!");
                            cutscene.stop(true);
                            finished = true;
                            break;
                        case 0:
                            options("Yes, let's go!", "No, I'm not quite ready yet.");
                            stage = 1;
                            break;
                        case 1:
                            switch (buttonId) {
                                case 1:
                                    player("Yes, let's go!");
                                    stage = 10;
                                    break;
                                case 2:
                                    player("No, I'm not quite ready yet.");
                                    stage = 30;
                                    break;
                            }
                            break;
                        case 30:
                            end();
                            break;
                        case 10:
                            end();
                            Charter.PORT_SARIM_TO_CRANDOR.sail(player);
                            GameWorld.getPulser().submit(new Pulse(17) {
                                @Override
                                public boolean pulse() {
                                    player.getInterfaceManager().open(new Component(317));
                                    ActivityManager.start(player, Quests.DRAGON_SLAYER, false);
                                    player.getInterfaceManager().closeOverlay();
                                    player.getInterfaceManager().open(new Component(317));
                                    return true;
                                }
                            });
                            break;
                        case 100:
                            end();
                            break;
                        case 20:
                            end();
                            break;
                    }
                    break;
                default:
                    break;
            }
            return true;
        }

        /**
         * Start fires.
         */
        public void startFires() {
            for (Location fire : fires) {
                player.getPacketDispatch().sendPositionedGraphic(453, 0, 1, fire);
            }
            GameWorld.getPulser().submit(new Pulse(2, player) {
                @Override
                public boolean pulse() {
                    for (Location fire : fires) {
                        player.getPacketDispatch().sendPositionedGraphic(453, 0, 1, fire);
                    }
                    return finished;
                }

            });
        }

        @Override
        public int[] getIds() {
            return new int[]{918};
        }
    }
}
