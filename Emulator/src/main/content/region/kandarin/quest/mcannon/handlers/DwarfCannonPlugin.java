package content.region.kandarin.quest.mcannon.handlers;

import org.rs.consts.Quests;
import content.region.kandarin.quest.mcannon.DwarfCannon;
import core.cache.def.impl.SceneryDefinition;
import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.dialogue.DialogueAction;
import core.game.global.action.ClimbActionHandler;
import core.game.global.action.DoorActionHandler;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.OptionHandler;
import core.game.interaction.UseWithHandler;
import core.game.node.Node;
import core.game.node.entity.combat.ImpactHandler.HitsplatType;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.quest.Quest;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.tools.Log;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.plugin.Plugin;
import core.tools.RandomFunction;

import static core.api.ContentAPIKt.*;

/**
 * The type Dwarf cannon plugin.
 */
public class DwarfCannonPlugin extends OptionHandler {

    private static NPC lollk;

    @Override
    public Plugin<java.lang.Object> newInstance(java.lang.Object arg) throws Throwable {
        SceneryDefinition.forId(3).getHandlers().put("option:open", this);
        SceneryDefinition.forId(5).getHandlers().put("option:inspect", this);
        SceneryDefinition.forId(2).getHandlers().put("option:enter", this);
        SceneryDefinition.forId(13).getHandlers().put("option:climb-over", this);
        SceneryDefinition.forId(15601).getHandlers().put("option:inspect", this);
        for (int i = 15; i < 21; i++) {
            SceneryDefinition.forId(i).getHandlers().put("option:inspect", this);
        }
        SceneryDefinition.forId(15596).getHandlers().put("option:take", this);
        SceneryDefinition.forId(1).getHandlers().put("option:search", this);
        UseWithHandler.addHandler(5, UseWithHandler.OBJECT_TYPE, new UseWithHandler(1) {

            @Override
            public Plugin<java.lang.Object> newInstance(java.lang.Object arg) throws Throwable {
                return this;
            }

            @Override
            public boolean handle(NodeUsageEvent event) {
                final Player player = event.getPlayer();
                final Quest quest = player.getQuestRepository().getQuest(Quests.DWARF_CANNON);
                if (quest.getStage(player) > 50) {
                    player.getDialogueInterpreter().sendDialogues(player, null, "This should work nicely now that I've fixed it.");
                    return true;
                }

                setVarp(player, 0, 8, true);
                player.getQuestRepository().getQuest(Quests.DWARF_CANNON).setStage(player, 60);
                player.sendMessage("Well done! You've fixed the cannon! Better go and tell Captain Lawgof.");

                return true;
            }

        });
        return this;
    }

    @Override
    public boolean handle(final Player player, final Node node, String option) {
        final Quest quest = player.getQuestRepository().getQuest(Quests.DWARF_CANNON);
        switch (node.getId()) {
            case 3:
                if (!node.getLocation().equals(new Location(3015, 3453, 0))) {
                    return DoorActionHandler.handleAutowalkDoor(player, node.asScenery());
                }
                if (quest.getStage(player) < 70) {
                    player.sendMessage("The door is locked.");
                    break;
                }
                return DoorActionHandler.handleAutowalkDoor(player, node.asScenery());
            case 5:
                if (quest.getStage(player) == 50) {
                    player.getDialogueInterpreter().sendDialogues(player, null, "I guess I'd better fix it with the toolkit I was given.");
                    break;
                } else if (quest.getStage(player) > 50) {
                    player.getDialogueInterpreter().sendDialogues(player, null, "This should work nicely now that I've fixed it.");
                    break;
                }
                player.getDialogueInterpreter().sendDialogue("The Black Guard sent Lawgof this cannon to help defend the mines", "against the goblins.");
                break;
            case 2:
                player.animate(Animation.create(845));
                player.teleport(new Location(2619, 9797, 0), 2);
                break;
            case 13:
                ClimbActionHandler.climb(player, new Animation(828), new Location(2623, 3391, 0));
                break;
            case 15601:
                if (node.getId() != node.asScenery().getWrapper().getId()) {
                    player.getDialogueInterpreter().sendDialogues(player, null, "I think I've already fixed this one.");
                }
                return true;
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
                int index = 15 - node.getId();
                final int shift = 10 - (20 - node.getId());
                player.faceLocation(node.getLocation());
                player.getDialogueInterpreter().sendDialogue("The railing is broken and needs to be replaced.");
                player.getDialogueInterpreter().addAction(new DialogueAction() {
                    @Override
                    public void handle(final Player player, int buttonId) {
                        if (quest.getStage(player) < 10) {
                            return;
                        }
                        if (!player.getInventory().contains(14, 1)) {
                            player.getDialogueInterpreter().sendDialogues(player, null, "I'm not going to be able to fix this without a new", "railing. Lawgof should have some spare ones.");
                            return;
                        }
                        player.sendMessage("You attempt to replace the broken railing...");
                        if (!player.getInventory().contains(2347, 1)) {
                            player.sendMessage("You will need a hammer to fix the railings.");
                            return;
                        }
                        player.animate(Animation.create(4190));
                        player.getPulseManager().run(new Pulse(1, player) {
                            int count;
                            final boolean failed = RandomFunction.random(5) == 2;

                            @Override
                            public boolean pulse() {
                                switch (++count) {
                                    case 1:
                                        if (RandomFunction.random(3) == 1) {
                                            player.sendChat("Urrghh!");
                                        }
                                        break;
                                    case 3:
                                        if (failed) {
                                            player.sendChat("Ow!");
                                            player.getImpactHandler().manualHit(player, 2, HitsplatType.NORMAL);
                                        }
                                        break;
                                    case 5:
                                        if (failed) {
                                            player.sendMessage("You accidentally crush your hand in the railing.");
                                            return true;
                                        }
                                        player.getInventory().remove(new Item(14));
                                        player.sendMessage("The railing is now fixed.");
                                        setVarbit(player, ((Scenery) node).getDefinition().getVarbitID(), 1, true);
                                        if (DwarfCannon.allRailsFixed(player)) {
                                            player.getDialogueInterpreter().sendDialogues(player, null, "I've fixed all these railings now.");
                                        }
                                        return true;
                                }
                                return false;
                            }

                        });
                    }
                });
                break;
            case 15596:
                if (player.hasItem(DwarfCannon.DWARF_REMAINS)) {
                    player.getDialogueInterpreter().sendDialogues(player, null, "What? Carrying one set of Dwarf remains is enough...");
                    return true;
                }
                player.getDialogueInterpreter().sendDialogues(player, null, "I had better take these remains.");
                player.getDialogueInterpreter().addAction(new DialogueAction() {

                    @Override
                    public void handle(Player player, int buttonId) {
                        if (!player.getInventory().hasSpaceFor(DwarfCannon.DWARF_REMAINS)) {
                            return;
                        }
                        setVarp(player, 0, 4, true);
                        player.getInventory().add(DwarfCannon.DWARF_REMAINS);
                    }

                });
                break;
            case 1:
                if (quest.getStage(player) != 30 || isSpawned()) {
                    player.sendMessage("You search the crate but find nothing.");
                    return true;
                }
                quest.setStage(player, 40);
                setVarp(player, 0, 6, true);
                spawnLollk(player);
                break;
        }
        return true;
    }

    private void spawnLollk(Player player) {
        player.sendMessages("You search the crate...", "Inside you see a dwarf child, tied up!", "You untie the child.");
        lollk.setInvisible(false);
        lollk.sendChat("Hooray!");
        GameWorld.getPulser().submit(new Pulse(150, lollk) {

            @Override
            public boolean pulse() {
                lollk.setInvisible(true);
                return true;
            }

        });
    }

    private boolean isSpawned() {
        if (lollk == null) {
            lollk = NPC.create(207, new Location(2571, 9851, 0));
            lollk.init();
            lollk.setInvisible(true);
            lollk.setWalks(false);
        }
        return !lollk.isInvisible();
    }

    @Override
    public Location getDestination(Node node, Node n) {
        if (n.getId() == 3) {
            return DoorActionHandler.getDestination(node.asPlayer(), n.asScenery());
        } else if (n.getId() == 15601) {
            if (n.getLocation().equals(new Location(2565, 3456, 0))) {
                return new Location(2566, 3456, 0);
            }
        }
        return null;
    }

    /**
     * The type Tool kit handler.
     */
    public static final class ToolKitHandler extends ComponentPlugin {

        private Tool tool;

        private final boolean[] toggled = new boolean[3];

        @Override
        public Plugin<java.lang.Object> newInstance(java.lang.Object arg) throws Throwable {
            ComponentDefinition.forId(409).setPlugin(this);
            return null;
        }

        @Override
        public boolean handle(final Player player, Component component, int opcode, int button, int slot, int itemId) {
            Tool tool = Tool.forId(button);
            if (tool != null) {
                this.tool = tool.select(player);
                return true;
            }
            if (this.tool == null) {
                player.sendMessage("You need to select a tool first.");
                return true;
            }
            Part part = Part.forId(button);
            if (part == null) {
                log(this.getClass(), Log.ERR, "Unhandled part id - " + button + "!");
                return true;
            }
            if (this.tool.getPart() != part) {
                player.sendMessage("This is the wrong tool to fix this part.");
                return true;
            }
            toggled[part.ordinal()] = !toggled[part.ordinal()];
            part.interact(player, this.tool, toggled[part.ordinal()]);
            if (toggled[0] && toggled[1] && toggled[2]) {
                player.lock(5);

                player.getQuestRepository().getQuest(Quests.DWARF_CANNON).setStage(player, 60);
                player.sendMessage("Well done! You've fixed the cannon! Better go and tell Captain Lawgof.");
                GameWorld.getPulser().submit(new Pulse(5, player) {
                    @Override
                    public boolean pulse() {
                        player.getInterfaceManager().close();
                        return true;
                    }
                });
            }

            return true;
        }

        /**
         * The enum Tool.
         */
        public enum Tool {

            /**
             * Wrench tool.
             */
            WRENCH(1, 2017, Part.GEAR),

            /**
             * Pliers tool.
             */
            PLIERS(2, 2018, Part.SAFETY_SWITCH),

            /**
             * Hook tool.
             */
            HOOK(3, 2020, Part.SPRING);

            private final int button;

            private final int configValue;

            private final Part part;

            Tool(int button, int configValue, Part part) {
                this.button = button;
                this.configValue = configValue;
                this.part = part;
            }

            /**
             * Select tool.
             *
             * @param player the player
             * @return the tool
             */
            public Tool select(Player player) {

                return this;
            }

            /**
             * For id tool.
             *
             * @param id the id
             * @return the tool
             */
            public static Tool forId(int id) {
                for (Tool tool : values()) {
                    if (tool.getButton() == id) {
                        return tool;
                    }
                }
                return null;
            }

            /**
             * Gets button.
             *
             * @return the button
             */
            public int getButton() {
                return button;
            }

            /**
             * Gets config value.
             *
             * @return the config value
             */
            public int getConfigValue() {
                return configValue;
            }

            /**
             * Gets part.
             *
             * @return the part
             */
            public Part getPart() {
                return part;
            }

        }

        /**
         * The enum Part.
         */
        public enum Part {

            /**
             * Spring part.
             */
            SPRING(8, 2025),

            /**
             * Safety switch part.
             */
            SAFETY_SWITCH(7, 2026),

            /**
             * Gear part.
             */
            GEAR(9, 2036);

            private final int button;

            private final int configValue;

            Part(int button, int configValue) {
                this.button = button;
                this.configValue = configValue;
            }

            /**
             * Interact.
             *
             * @param player  the player
             * @param tool    the tool
             * @param toggled the toggled
             */
            public void interact(Player player, Tool tool, boolean toggled) {

            }

            /**
             * For id part.
             *
             * @param id the id
             * @return the part
             */
            public static Part forId(int id) {
                for (Part part : values()) {
                    if (part.getButton() == id) {
                        return part;
                    }
                }
                return null;
            }

            /**
             * Gets button.
             *
             * @return the button
             */
            public int getButton() {
                return button;
            }

            /**
             * Gets config value.
             *
             * @return the config value
             */
            public int getConfigValue() {
                return configValue;
            }

        }
    }
}
