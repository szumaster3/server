package content.region.misthalin.varrock.quest.dslay.cutscene;

import content.region.misthalin.varrock.quest.dslay.plugin.DemonSlayerUtils;
import core.game.activity.ActivityManager;
import core.game.activity.ActivityPlugin;
import core.game.activity.CutscenePlugin;
import core.game.dialogue.Dialogue;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.spell.CombatSpell;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.SpellBookManager.SpellBook;
import core.game.node.entity.player.link.quest.Quest;
import core.game.node.entity.skill.Skills;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.map.build.DynamicRegion;
import core.game.world.map.path.Path;
import core.game.world.map.path.Pathfinder;
import core.game.world.update.flag.context.Animation;
import core.net.packet.OutgoingContext;
import core.net.packet.PacketRepository;
import core.net.packet.OutgoingContext.CameraType;
import core.net.packet.out.CameraViewPacket;
import core.net.packet.out.MinimapState;
import shared.consts.*;

import static core.api.ContentAPIKt.*;

/**
 * The type Demon slayer cutscene plugin.
 */
public final class DemonSlayerCutscenePlugin extends CutscenePlugin {

    /**
     * The delrith.
     */
    public static final int DELRITH = NPCs.DELRITH_879;

    /**
     * The weakened delrith.
     */
    public static final int WEAKENED_DELRITH = NPCs.WEAKENED_DELRITH_880;

    /**
     * The wizard animation.
     */
    private static final Animation WIZARD_ANIM = new Animation(4617);

    /**
     * The summoning demon sound effect.
     */
    public static final int SUMMON_DEMON = Sounds.DS_SUMMON_DEMON_2987;

    /**
     * The appearing demon sound effect.
     */
    public static final int DELRITH_APPEAR = Sounds.DS_DELRITH_APPEAR_2980;

    /**
     * The banished demon sound effect.
     */
    public static final int DELRITH_BANISHED = Sounds.DS_DELRITH_BANISHED_2981;

    /**
     * The Stone table.
     */
    public Scenery stoneTable;

    /**
     * The Delrith.
     */
    public NPC delrith;

    /**
     * Instantiates a new Demon slayer cutscene plugin.
     */
    public DemonSlayerCutscenePlugin() {
        super("Demon Slayer Cutscene");
    }

    /**
     * Instantiates a new Demon slayer cutscene plugin.
     *
     * @param player the player
     */
    public DemonSlayerCutscenePlugin(final Player player) {
        super("Demon Slayer Cutscene");
        this.player = player;
    }

    @Override
    public ActivityPlugin newInstance(Player p) throws Throwable {
        return new DemonSlayerCutscenePlugin(p);
    }

    @Override
    public boolean start(final Player player, final boolean login, java.lang.Object... args) {
        final NPC[] npcs = new NPC[]{NPC.create(NPCs.DARK_WIZARD_4658, base.transform(29, 43, 0)), NPC.create(4659, base.transform(29, 40, 0)), NPC.create(4662, base.transform(26, 40, 0)), NPC.create(4660, base.transform(26, 43, 0))};
        for (NPC n : npcs) {
            playAudio(player, DELRITH_APPEAR);
            n.init();
            n.faceLocation(base.transform(27, 42, 0));
            this.npcs.add(n);
        }
        player.lock();
        return super.start(player, login, args);
    }

    @Override
    public void open() {
        setAttribute(player, "cutscene:original-loc", Location.create(3221, 3373, 0));
        setAttribute(player, "real-end", Location.create(3221, 3373, 0));
        for (NPC n : npcs) {
            n.animate(WIZARD_ANIM);
        }
        PacketRepository.send(CameraViewPacket.class, new OutgoingContext.Camera(player, OutgoingContext.CameraType.POSITION, player.getLocation().getX(), player.getLocation().getY(), 450, 1, 100));
        PacketRepository.send(CameraViewPacket.class, new OutgoingContext.Camera(player, OutgoingContext.CameraType.ROTATION, player.getLocation().getX() - 15, player.getLocation().getY() + -55, 450, 1, 100));
        player.lock();
        player.getDialogueInterpreter().open(NPCs.DENATH_4662, npcs.get(3), this);
    }

    @Override
    public void end() {
        super.end();
        player.unlock();
    }

    @Override
    public boolean enter(final Entity entity) {
        if (!(entity instanceof Player)) {
            return true;
        }
        final Player player = ((Player) entity);
        final Quest quest = player.getQuestRepository().getQuest(Quests.DEMON_SLAYER);
        boolean in = player.getAttribute("demon-slayer:cutscene", false);
        if (quest.getStage(player) == 30 && !in && (player.getEquipment().containsItem(DemonSlayerUtils.SILVERLIGHT) || player.getInventory().containsItem(DemonSlayerUtils.SILVERLIGHT))) {
            ActivityManager.start(player, "Demon Slayer Cutscene", false);
            setAttribute(player, "demon-slayer:cutscene", true);
            return super.enter(entity);
        }
        return super.enter(entity);
    }

    @Override
    public void locationUpdate(Entity entity, Location last) {
        if (!(entity instanceof Player)) {
            super.locationUpdate(entity, last);
            return;
        }
        Player player = ((Player) entity);
        if (base != null && player.getAttribute("demon-slayer:cutscene", false) && player.getLocation().getDistance(base.transform(27, 42, 0)) > 8) {
            stop(true);
        }
        super.locationUpdate(entity, last);
    }

    @Override
    public boolean leave(final Entity e, final boolean logout) {
        if (!(e instanceof Player)) {
            return true;
        }
        final Player player = ((Player) e);
        if (player.getAttribute("demon-slayer:cutscene", false)) {
            if (base != null && player.getLocation().getDistance(base.transform(27, 42, 0)) > 13) {
                removeAttribute(player, "demon-slayer:cutscene");
            }
            return super.leave(player, logout);
        }
        return super.leave(player, logout);
    }

    @Override
    public Location getStartLocation() {
        return base.transform(29, 47, 0);
    }

    @Override
    public Location getSpawnLocation() {
        return null;
    }

    @Override
    public void configure() {
        region = DynamicRegion.create(12852);
        setRegionBase();
        registerRegion(region.getId());
    }

    @Override
    public void register() {

        try {
            new DarkWizardNPC().newInstance(null);
            new DelrithNPC().newInstance(null);
            new DelrithDialogue().init();
            new DenathDialogue().init();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * The type Dark wizard npc.
     */
    public static class DarkWizardNPC extends AbstractNPC {

        private static final int[] IDS = {4658};

        /**
         * Instantiates a new Dark wizard npc.
         */
        public DarkWizardNPC() {
            super(0, null);
        }

        private DarkWizardNPC(int id, Location location) {
            super(id, location, false);
        }

        @Override
        public AbstractNPC construct(int id, Location location, java.lang.Object... objects) {
            return new DarkWizardNPC(id, location);
        }

        @Override
        public void init() {
            super.init();
            this.getSkills().setLevel(Skills.MAGIC, 11);
            this.setRespawn(false);
        }

        @Override
        public void tick() {
            super.tick();
            getProperties().setAutocastSpell((CombatSpell) SpellBook.MODERN.getSpell(4));
            getProperties().getCombatPulse().setStyle(CombatStyle.MAGIC);
        }

        @Override
        public int[] getIds() {
            return IDS;
        }

    }

    /**
     * The type Delrith npc.
     */
    public class DelrithNPC extends AbstractNPC {

        private final int[] IDS = {WEAKENED_DELRITH, DELRITH};

        /**
         * Instantiates a new Delrith npc.
         */
        public DelrithNPC() {
            super(0, null);
        }

        private DelrithNPC(int id, Location location) {
            super(id, location, false);
        }

        @Override
        public AbstractNPC construct(int id, Location location, java.lang.Object... objects) {
            return new DelrithNPC(id, location);
        }

        @Override
        public void init() {
            super.init();
        }

        @Override
        public void finalizeDeath(final Entity killer) {
            if (getId() == DELRITH) {
                setAttribute("disable:drop", true);
                super.finalizeDeath(killer);
                super.setRespawnTick(-1);
                super.getWalkingQueue().reset();
                super.getLocks().lockMovement(10);
                transform(WEAKENED_DELRITH);
                getProperties().setTeleportLocation(null);
                lock();
            } else {
                super.finalizeDeath(killer);
            }
        }

        @Override
        public void tick() {
            super.tick();
        }

        @Override
        public int[] getIds() {
            return IDS;
        }

        @Override
        public int getWalkRadius() {
            return 3;
        }
    }

    /**
     * The type Delrith dialogue.
     */
    public static final class DelrithDialogue extends Dialogue {
        private static final String[] WORDS = new String[]{"Carlem", "Aber", "Camerinthum", "Purchai", "Gabindo"};
        private int counter = 0;
        private DemonSlayerCutscenePlugin cutscene;
        private final StringBuilder incantation = new StringBuilder();

        /**
         * Instantiates a new Delrith dialogue.
         */
        public DelrithDialogue() {

        }

        /**
         * Instantiates a new Delrith dialogue.
         *
         * @param player the player
         */
        public DelrithDialogue(Player player) {
            super(player);
        }

        @Override
        public Dialogue newInstance(Player player) {
            return new DelrithDialogue(player);
        }

        @Override
        public boolean open(java.lang.Object... args) {
            cutscene = player.getAttribute("cutscene", null);
            player("Now what was that incantation again?");
            stage = 0;
            return true;
        }

        @Override
        public boolean handle(int interfaceId, int buttonId) {
            switch (stage) {
                case 0:
                    interpreter.sendOptions("Select a word", WORDS);
                    stage = 1;
                    break;
                case 1:
                    switch (buttonId) {
                        default:
                            String text = WORDS[buttonId - 1] + (counter != 4 ? "..." : "!");
                            if (text.contains("!")) {
                                incantation.append(text.replace("!", ""));
                            } else {
                                incantation.append(text + " ");
                            }
                            player.sendChat(text);
                            player(text);
                            if (counter == 4) {
                                if (incantation.toString().equals(DemonSlayerUtils.getIncantation(player).trim())) {
                                    stage = 4;
                                } else {
                                    stage = 2;
                                }
                            } else {
                                stage = 0;
                            }
                            break;
                    }
                    counter++;
                    break;
                case 2:
                    cutscene.delrith.reTransform();
                    cutscene.delrith.animate(new Animation(4620));
                    cutscene.delrith.unlock();
                    interpreter.sendDialogue("The vortex collapses. That was the wrong incantation.");
                    stage = 3;
                    break;
                case 3:
                    end();
                    break;
                case 4:
                    interpreter.sendPlainMessage(true, "Delrith is sucked into the vortex...");
                    cutscene.delrith.animate(new Animation(4624));
                    player.lock();
                    GameWorld.getPulser().submit(new Pulse(10) {
                        @Override
                        public boolean pulse() {
                            cutscene.delrith.clear();
                            interpreter.sendDialogue("...back into the dark dimension from which he came.");
                            player.unlock();
                            cutscene.end();
                            cutscene.delrith.clear();
                            setVarp(player, 222, 5653570, true);
                            player.getQuestRepository().getQuest(Quests.DEMON_SLAYER).finish(player);
                            end();
                            return true;
                        }

                    });
                    break;
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{8427322};
        }
    }

    /**
     * The type Denath dialogue.
     */
    public static final class DenathDialogue extends Dialogue {
        private static final Animation ANIMATION = new Animation(4623);

        /**
         * The Cutscene.
         */
        public DemonSlayerCutscenePlugin cutscene;

        /**
         * Instantiates a new Denath dialogue.
         */
        public DenathDialogue() {

        }

        /**
         * Instantiates a new Denath dialogue.
         *
         * @param player the player
         */
        public DenathDialogue(Player player) {
            super(player);
        }

        @Override
        public Dialogue newInstance(Player player) {
            return new DenathDialogue(player);
        }

        @Override
        public boolean open(java.lang.Object... args) {
            npc = ((NPC) args[0]);
            cutscene = ((DemonSlayerCutscenePlugin) args[1]);
            npc("Arise, O mighty Delrith! Bring destruction to this soft,", "weak city!");
            stage = 0;
            return true;
        }

        @Override
        public boolean handle(int interfaceId, int buttonId) {
            switch (stage) {
                case 0:
                    interpreter.sendDialogues(cutscene.getNPCS().get(0), null, "Arise, Delrith!");
                    for (NPC n : cutscene.getNPCS()) {
                        n.sendChat("Arise, Delrith!");

                    }
                    stage = 1;
                    break;
                case 1:
                    player.getProperties().setTeleportLocation(cutscene.getBase().transform(28, 35, 0));
                    cutscene.stoneTable = new Scenery(17437, cutscene.getBase().transform(27, 41, 0));
                    playAudio(player, Sounds.DS_REPLACE_TABLE_2986);
                    SceneryBuilder.add(cutscene.stoneTable);
                    PacketRepository.send(CameraViewPacket.class, new OutgoingContext.Camera(player, OutgoingContext.CameraType.POSITION, player.getLocation().getX() - 1, player.getLocation().getY() + 2, 380, 1, 98));
                    PacketRepository.send(CameraViewPacket.class, new OutgoingContext.Camera(player, OutgoingContext.CameraType.ROTATION, player.getLocation().getX() + 19, player.getLocation().getY() - 55, 380, 1, 98));
                    interpreter.sendPlainMessage(true, "The wizards cast an evil spell...");
                    GameWorld.getPulser().submit(new Pulse(1, player) {
                        int counter = 0;

                        @SuppressWarnings("deprecation")
                        @Override
                        public boolean pulse() {
                            switch (counter++) {
                                case 5:
                                    playJingle(player, Music.DELRITH_SUMMON_195);
                                    cutscene.delrith = NPC.create(DemonSlayerCutscenePlugin.DELRITH, cutscene.getBase().transform(27, 40, 0));
                                    cutscene.delrith.init();
                                    cutscene.delrith.animate(ANIMATION);
                                    player.getProperties().setTeleportLocation(cutscene.getBase().transform(26, 48, 0));
                                    player.setLocation(cutscene.getBase().transform(26, 48, 0));
                                    PacketRepository.send(CameraViewPacket.class, new OutgoingContext.Camera(player, CameraType.POSITION, player.getLocation().getX() - 1, player.getLocation().getY() - 3, 390, 1, 100));
                                    PacketRepository.send(CameraViewPacket.class, new OutgoingContext.Camera(player, CameraType.ROTATION, player.getLocation().getX() + 30, player.getLocation().getY() - 55, 390, 1, 100));
                                    setVarp(player, 222, 14194946, true);
                                    playAudio(player, Sounds.DS_TABLE_EXPLOSION_2988, 1);
                                    SceneryBuilder.replace(cutscene.stoneTable, cutscene.stoneTable.transform(17438));
                                    npc("Ha ha ha! At last you are free, my demonic brother!", "Rest now, and then have your revenge on this pitiful", "city!");
                                    stage = 2;
                                    return true;
                            }
                            return false;
                        }
                    });
                    break;
                case 2:
                    interpreter.sendDialogues(cutscene.getNPCS().get(0), null, "Who's that?");
                    for (NPC n : cutscene.getNPCS()) {
                        n.animate(new Animation(-1));
                        n.face(player);
                    }
                    stage = 3;
                    break;
                case 3:
                    npc = cutscene.getNPCS().get(2);
                    npc("Noo! Not Silverlight! Delrith is not ready yet!");
                    stage = 4;
                    break;
                case 4:
                    npc("I've got to get out of here...");
                    npc.faceLocation(null);
                    npc.face(null);
                    Path path = Pathfinder.find(npc, cutscene.getBase().transform(18, 40, 0));
                    path.walk(npc);
                    stage = 5;
                    break;
                case 5:
                    player.getProperties().setTeleportLocation(cutscene.getBase().transform(24, 42, 0));
                    npc.clear();
                    cutscene.getNPCS().remove(npc);
                    end();
                    cutscene.delrith.moveStep();
                    cutscene.delrith.setWalks(true);
                    player.unlock();
                    player.getInterfaceManager().restoreTabs();
                    player.getInterfaceManager().close();
                    PacketRepository.send(MinimapState.class, new OutgoingContext.MinimapState(player, 0));
                    PacketRepository.send(CameraViewPacket.class, new OutgoingContext.Camera(player, CameraType.RESET, player.getLocation().getX(), player.getLocation().getY(), 390, 1, 100));
                    for (NPC n : cutscene.getNPCS()) {
                        n.clear();
                    }
                    cutscene.getNPCS().clear();
                    final Location base = cutscene.getBase();
                    cutscene.getNPCS().add(NPC.create(4658, base.transform(29, 40, 0)));
                    cutscene.getNPCS().add(NPC.create(4658, base.transform(26, 40, 0)));
                    cutscene.getNPCS().add(NPC.create(4658, base.transform(26, 43, 0)));
                    for (NPC n : cutscene.getNPCS()) {
                        n.init();
                        n.setWalks(true);
                        n.unlock();
                    }
                    PacketRepository.send(CameraViewPacket.class, new OutgoingContext.Camera(player, CameraType.RESET, 0, 0, 0, 0, 0));
                    cutscene.getNPCS().get(0).getProperties().getCombatPulse().attack(player);
                    setAttribute(player, "cutscene", cutscene);
                    break;
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{4662};
        }
    }
}
