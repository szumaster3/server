package content.region.misthalin.draynor_village.quest.anma.plugin;

import content.data.items.SkillingTool;
import content.region.misthalin.draynor_village.quest.anma.AnimalMagnetism;
import core.api.Container;
import core.cache.def.impl.ItemDefinition;
import core.cache.def.impl.NPCDefinition;
import core.cache.def.impl.SceneryDefinition;
import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.OptionHandler;
import core.game.interaction.UseWithHandler;
import core.game.node.Node;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.TeleportManager;
import core.game.node.entity.player.link.quest.Quest;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.map.zone.MapZone;
import core.game.world.map.zone.ZoneBorders;
import core.game.world.map.zone.ZoneBuilder;
import core.game.world.update.flag.context.Animation;
import core.plugin.ClassScanner;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.tools.RandomFunction;
import org.rs.consts.*;

import java.util.HashMap;
import java.util.Map;

import static core.api.ContentAPIKt.*;
import static core.api.interaction.NPCInteractionAPIKt.openNpcShop;
import static core.api.quest.QuestAPIKt.getQuestStage;
import static core.api.quest.QuestAPIKt.hasRequirement;

/**
 * The type Animal magnetism plugin.
 */
@Initializable
public class AnimalMagnetismPlugin extends OptionHandler {

    @Override
    public Plugin<java.lang.Object> newInstance(java.lang.Object arg) throws Throwable {
        ItemDefinition.forId(Items.BUTTONS_688).getHandlers().put("option:polish", this);
        ItemDefinition.forId(Items.ECTOPHIAL_4251).getHandlers().put("option:empty", this);
        ItemDefinition.forId(Items.ECTOPHIAL_4251).getHandlers().put("option:drop", this);
        ItemDefinition.forId(Items.ECTOPHIAL_4252).getHandlers().put("option:drop", this);
        NPCDefinition.forId(NPCs.AVA_5198).getHandlers().put("option:trade", this);
        SceneryDefinition.forId(Scenery.MEMORIAL_5167).getHandlers().put("option:push", this);
        AnimalMagnetism.RESEARCH_NOTES.getDefinition().getHandlers().put("option:translate", this);
        ItemDefinition.forId(AnimalMagnetism.CRONE_AMULET.getId()).getHandlers().put("option:wear", this);
        ItemDefinition.forId(AnimalMagnetism.CRONE_AMULET.getId()).getHandlers().put("option:equip", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        switch (node.getId()) {
            case Scenery.MEMORIAL_5167:
                if (!hasRequirement(player, Quests.CREATURE_OF_FENKENSTRAIN)) {
                    return true;
                }
                teleport(player, new Location(3577, 9927), TeleportManager.TeleportType.INSTANT);
                break;
            case NPCs.AVA_5198:
            case NPCs.AVA_5199:
                if (getQuestStage(player, Quests.ANIMAL_MAGNETISM) == 0) {
                    player.getDialogueInterpreter().sendDialogues((NPC) node, null,
                            "Hello there, I'm busy with my research. Come back in a",
                            "bit, could you?");
                    return true;
                }
                openNpcShop(player, node.getId());
                break;
            case Items.CRONE_MADE_AMULET_10500:
                sendMessage(player, "Perhaps you should wait a few hundred years or so?");
                break;
            case Items.RESEARCH_NOTES_10492:
                open(player);
                break;
            case Items.BUTTONS_688:
                lock(player, 1);
                if (getStatLevel(player, Skills.CRAFTING) < 3) {
                    sendMessage(player, "You need a Crafting level of at least 3 in order to do that.");
                    return true;
                }
                rewardXP(player, Skills.CRAFTING, 5.0);
                player.getInventory().replace(AnimalMagnetism.POLISHED_BUTTONS, ((Item) node).getSlot());
                break;
        }
        return true;
    }

    private void clearCache(Player player) {
        removeAttribute(player, "note-cache");
        removeAttribute(player, "note-disabled");
    }

    /**
     * Open.
     *
     * @param player the player
     */
    public void open(Player player) {
        clearCache(player);
        openInterface(player, Components.ANMA_RGB_480);
        sendMessage(player, "You fiddle with the notes.");
    }

    /**
     * The type Hammer magnet plugin.
     */
    public static class HammerMagnetPlugin extends UseWithHandler {

        private static final Animation ANIMATION = new Animation(Animations.ANMA_MAKE_IRON_MAGNET_5365);

        /**
         * Instantiates a new Hammer magnet plugin.
         */
        public HammerMagnetPlugin() {
            super(Items.HAMMER_2347);
        }

        @Override
        public Plugin<java.lang.Object> newInstance(Object arg) {
            ZoneBuilder.configure(new MapZone("rimmington mine", true) {
                @Override
                public void configure() {
                    register(new ZoneBorders(2970, 3230, 2984, 3249));
                }
            });
            addHandler(AnimalMagnetism.SELECTED_IRON.getId(), ITEM_TYPE, this);
            return this;
        }

        @Override
        public boolean handle(NodeUsageEvent event) {
            Player player = event.getPlayer();
            animate(player, ANIMATION, false);
            lock(player, ANIMATION.getDefinition().getDurationTicks());
            GameWorld.getPulser().submit(new Pulse(ANIMATION.getDefinition().getDurationTicks(), player) {
                @Override
                public boolean pulse() {
                    if (!player.getZoneMonitor().isInZone("rimmington mine")) {
                        sendMessage(player, "You aren't in the right area for this to work.");
                    } else {
                        if (player.getDirection() != Direction.NORTH) {
                            sendMessage(player, "You think that facing North might work better.");
                        } else {
                            player.getInventory().replace(new Item(Items.BAR_MAGNET_10489), event.getUsedItem().getSlot());
                            sendMessage(player, "You hammer the iron bar and create a magnet.");
                        }
                    }
                    return true;
                }
            });
            return true;
        }
    }

    /**
     * The type Undead tree plugin.
     */
    public static class UndeadTreePlugin extends UseWithHandler {

        private static final int[] IDS = {Items.MITHRIL_AXE_1355, Items.ADAMANT_AXE_1357, Items.RUNE_AXE_1359, Items.DRAGON_AXE_6739};

        /**
         * Instantiates a new Undead tree plugin.
         */
        public UndeadTreePlugin() {
            super(Items.MITHRIL_AXE_1355, Items.ADAMANT_AXE_1357, Items.RUNE_AXE_1359, Items.DRAGON_AXE_6739);
        }

        @Override
        public Plugin<java.lang.Object> newInstance(Object arg) {
            ClassScanner.definePlugin(new OptionHandler() {
                @Override
                public Plugin<java.lang.Object> newInstance(Object arg) {
                    NPCDefinition.forId(NPCs.UNDEAD_TREE_5208).getHandlers().put("option:chop", this);
                    return this;
                }

                @Override
                public boolean handle(Player player, Node node, String option) {
                    Quest quest = player.getQuestRepository().getQuest(Quests.ANIMAL_MAGNETISM);
                    if (quest.getStage(player) <= 28) {
                        SkillingTool tool = SkillingTool.getHatchet(player);
                        if (tool == null || tool.ordinal() < 4) {
                            sendMessage(player, "You don't have the required axe in order to do that.");
                            return true;
                        }
                        Animation animation = getAnimation(tool.animation);
                        player.animate(animation, 2);
                        if (quest.getStage(player) == 28) {
                            quest.setStage(player, 29);
                        }
                        sendMessage(player, "The axe bounces off the undead wood." +
                                (quest.getStage(player) == 28 || quest.getStage(player) == 29 ? " I should report this to Ava." : ""));
                        return true;
                    }
                    if (freeSlots(player) < 1) {
                        sendMessage(player, "Your inventory is full right now.");
                        return true;
                    }
                    if (!inInventory(player, Items.BLESSED_AXE_10491, 1) && !inEquipment(player, Items.BLESSED_AXE_10491, 1)) {
                        sendMessage(player, "You don't have an axe which could possibly affect this wood.");
                        return true;
                    }
                    Animation animation = getAnimation(Items.MITHRIL_AXE_1355);
                    lock(player, animation.getDefinition().getDurationTicks());
                    if (RandomFunction.random(10) < 3) {
                        sendMessage(player, "You almost remove a suitable twig, but you don't quite manage it.");
                    } else {
                        addItem(player, Items.UNDEAD_TWIGS_10490, 1, Container.INVENTORY);
                        sendMessage(player, "You cut some undead twigs.");
                        rewardXP(player, Skills.WOODCUTTING, 5.0);
                    }
                    player.animate(animation, 2);
                    return true;
                }
            });
            addHandler(NPCs.UNDEAD_TREE_5208, NPC_TYPE, this);
            return this;
        }

        @Override
        public boolean handle(NodeUsageEvent event) {
            Player player = event.getPlayer();
            Animation animation = getAnimation(event.getUsedItem().getId());
            Quest quest = player.getQuestRepository().getQuest(Quests.ANIMAL_MAGNETISM);
            player.animate(animation, 2);
            if (quest.getStage(player) == 28) {
                quest.setStage(player, 29);
            }
            sendMessage(player, "The axe bounces off the undead wood." +
                    (quest.getStage(player) == 28 || quest.getStage(player) == 29 ? " I should report this to Ava." : ""));
            return true;
        }

        private Animation getAnimation(int itemId) {
            for (int i = 0; i < IDS.length; i++) {
                if (IDS[i] == itemId) {
                    return new Animation(Animations.ANMA_BLESSED_AXE_5366 + i, Priority.HIGH);
                }
            }
            return null;
        }
    }

    /**
     * The type Research note handler.
     */
    public static class ResearchNoteHandler extends ComponentPlugin {

        private static final int[][] BUTTONS = {
                {40, 39, 6},
                {42, 41, 3},
                {44, 43, 7},
                {46, 45, 8},
                {48, 47, 4},
                {50, 49, 9},
                {52, 51, 10},
                {54, 53, 11},
                {56, 55, 5}
        };

        @Override
        public Plugin<java.lang.Object> newInstance(Object arg) {
            ComponentDefinition.forId(Components.ANMA_RGB_480).plugin = this;
            return this;
        }

        @Override
        public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
            if (player.getAttribute("note-disabled", false)) {
                return true;
            }
            Object[] data = getIndex(button);
            boolean toggled = (boolean) data[1];
            int[] configs = getConfigs((int) data[0]);
            Quest quest = player.getQuestRepository().getQuest(Quests.ANIMAL_MAGNETISM);
            player.getPacketDispatch().sendInterfaceConfig(Components.ANMA_RGB_480, configs[0], !toggled);
            player.getPacketDispatch().sendInterfaceConfig(Components.ANMA_RGB_480, (int) data[2], toggled);
            if (quest.getStage(player) == 33) {
                setNoteCache(player, (int) data[0], !toggled);
                if (isTranslated(player)) {
                    if (player.getInventory().remove(AnimalMagnetism.RESEARCH_NOTES)) {
                        player.setAttribute("note-disabled", true);
                        player.getInventory().add(AnimalMagnetism.TRANSLATED_NOTES);
                        playAudio(player, 3283);
                        sendMessage(player, "It suddenly all makes sense.");
                    }
                }
            }
            return true;
        }

        private void setNoteCache(Player player, int index, boolean toggled) {
            Map<Integer, Boolean> cache = getNoteCache(player);
            cache.put(index, toggled);
            player.setAttribute("note-cache", cache);
        }

        private boolean isTranslated(Player player) {
            Map<Integer, Boolean> cache = getNoteCache(player);
            int[] correct = {0, 2, 3, 5, 6, 7};
            int[] wrong = {1, 4, 8};
            for (int i : correct) {
                if (cache.get(i)) {
                    return false;
                }
            }
            for (int i : wrong) {
                if (!cache.get(i)) {
                    return false;
                }
            }
            return true;
        }

        private Map<Integer, Boolean> getNoteCache(Player player) {
            Map<Integer, Boolean> cache = player.getAttribute("note-cache", null);
            if (cache == null) {
                cache = new HashMap<>();
                for (int i = 0; i < BUTTONS.length; i++) {
                    cache.put(i, true);
                }
            }
            return cache;
        }

        private int[] getConfigs(int index) {
            return new int[]{21 + index, 0};
        }

        private Object[] getIndex(int buttonId) {
            for (int i = 0; i < BUTTONS.length; i++) {
                for (int k = 0; k < BUTTONS[i].length - 1; k++) {
                    if (buttonId == BUTTONS[i][k]) {
                        return new Object[]{i, k == 0, BUTTONS[i][2]};
                    }
                }
            }
            return new Object[]{0, true};
        }
    }

    /**
     * The type Container handler.
     */
    public static class ContainerHandler extends UseWithHandler {

        /**
         * Instantiates a new Container handler.
         */
        public ContainerHandler() {
            super(Items.POLISHED_BUTTONS_10496, Items.HARD_LEATHER_1743);
        }

        @Override
        public Plugin<java.lang.Object> newInstance(Object arg) {
            addHandler(AnimalMagnetism.PATTERN.getId(), ITEM_TYPE, this);
            return this;
        }

        @Override
        public boolean handle(NodeUsageEvent event) {
            Player player = event.getPlayer();
            if (!player.getInventory().containsItem(AnimalMagnetism.HARD_LEATHER)) {
                player.sendMessage("You need hard leather as well as these 2 items.");
                return true;
            }
            if (!player.getInventory().containsItem(AnimalMagnetism.POLISHED_BUTTONS)) {
                player.sendMessage("You need polished buttons as well as these 2 items.");
                return true;
            }
            if (player.getInventory().remove(AnimalMagnetism.HARD_LEATHER, AnimalMagnetism.POLISHED_BUTTONS, AnimalMagnetism.PATTERN)) {
                playAudio(player, Sounds.ANMA_POLISH_BUTTONS_3281);
                player.getInventory().add(AnimalMagnetism.CONTAINER);
            }
            return true;
        }
    }

    @Override
    public boolean isWalk(Player player, Node node) {
        return !(node instanceof Item);
    }

    @Override
    public boolean isWalk() {
        return false;
    }
}
