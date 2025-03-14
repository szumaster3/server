package content.global.skill.construction.decoration.portalchamber;

import content.global.skill.construction.Decoration;
import content.global.skill.construction.Hotspot;
import content.global.skill.runecrafting.runes.Rune;
import core.cache.def.impl.SceneryDefinition;
import core.game.dialogue.Dialogue;
import core.game.dialogue.DialogueInterpreter;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.plugin.ClassScanner;
import core.plugin.Initializable;
import core.plugin.Plugin;

import static core.api.ContentAPIKt.sendDialogueOptions;
import static core.api.ContentAPIKt.setTitle;
import static core.tools.GlobalsKt.DARK_RED;

/**
 * The type Portal chamber plugin.
 */
@Initializable
public class PortalChamberPlugin extends OptionHandler {

    private enum Locations {

        /**
         * The Varrock.
         */
        VARROCK(Location.create(3213, 3428, 0), new Item(Rune.FIRE.getRune().getId(), 100), new Item(Rune.AIR.getRune().getId(), 300), new Item(Rune.LAW.getRune().getId(), 100)),

        /**
         * The Lumbridge.
         */
        LUMBRIDGE(Location.create(3222, 3217, 0), new Item(Rune.EARTH.getRune().getId(), 100), new Item(Rune.AIR.getRune().getId(), 300), new Item(Rune.LAW.getRune().getId(), 100)),

        /**
         * The Falador.
         */
        FALADOR(Location.create(2965, 3380, 0), new Item(Rune.WATER.getRune().getId(), 100), new Item(Rune.AIR.getRune().getId(), 300), new Item(Rune.LAW.getRune().getId(), 100)),

        /**
         * The Camelot.
         */
        CAMELOT(Location.create(2730, 3485, 0), new Item(Rune.AIR.getRune().getId(), 500), new Item(Rune.LAW.getRune().getId(), 100)),

        /**
         * The Ardougne.
         */
        ARDOUGNE(Location.create(2663, 3305, 0), new Item(Rune.WATER.getRune().getId(), 200), new Item(Rune.LAW.getRune().getId(), 200)),

        /**
         * The Yanille.
         */
        YANILLE(Location.create(2554, 3114, 0), new Item(Rune.EARTH.getRune().getId(), 200), new Item(Rune.LAW.getRune().getId(), 200)),

        /**
         * The Kharyrll.
         */
        KHARYRLL(Location.create(3493, 3474, 0), new Item(Rune.BLOOD.getRune().getId(), 100), new Item(Rune.LAW.getRune().getId(), 200));
        private final Location location;
        private final Item[] runes;

        Locations(Location location, Item... runes) {
            this.location = location;
            this.runes = runes;
        }
    }

    /**
     * Direct.
     *
     * @param player     the player
     * @param identifier the identifier
     */
    public static void direct(Player player, String identifier) {
        player.getInterfaceManager().closeSingleTab();
        int dpId = player.getAttribute("con:dp-id", 1);
        Hotspot[] hotspots = player.getHouseManager().getRoom(player.getLocation()).getHotspots();
        for (int i = 0; i < hotspots.length; i++) {
            Hotspot h = hotspots[i];
            if (h.getHotspot().name().equalsIgnoreCase("PORTAL" + dpId)) {
                if (h.getDecorationIndex() == -1) {
                    player.sendMessage("You must build a portal frame first!");
                    return;
                }
                Decoration previous = h.getHotspot().getDecorations()[h.getDecorationIndex()];
                String name = previous.name();
                String prefix = "TEAK";
                if (name.toLowerCase().contains("mahogany")) {
                    prefix = "MAHOGANY";
                } else if (name.toLowerCase().contains("marble")) {
                    prefix = "MARBLE";
                }
                for (Locations l : Locations.values()) {
                    if (l.name().contains(identifier)) {
                        Item[] runes = l.runes;
                        if (!player.getInventory().containsItems(runes)) {
                            player.sendMessage("You do not have the required runes to build this portal");
                            return;
                        }
                        player.getInventory().remove(runes);
                        break;
                    }
                }
                player.animate(Animation.create(3705));
                h.setDecorationIndex(h.getHotspot().getDecorationIndex(Decoration.forName(prefix + "_" + identifier + "_" + "PORTAL")));
                player.getHouseManager().reload(player, player.getHouseManager().isBuildingMode());
            }
        }
    }

    @Override
    public Plugin<java.lang.Object> newInstance(java.lang.Object arg) throws Throwable {
        SceneryDefinition.forId(13639).getHandlers().put("option:direct-portal", this);
        SceneryDefinition.forId(13639).getHandlers().put("option:scry", this);
        SceneryDefinition.forId(13640).getHandlers().put("option:direct-portal", this);
        SceneryDefinition.forId(13641).getHandlers().put("option:direct-portal", this);
        for (int i = 13615; i <= 13635; i++) {
            SceneryDefinition.forId(i).getHandlers().put("option:enter", this);
        }
        ClassScanner.definePlugin(new DirectPortalDialogue());
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        Scenery scenery = node.asScenery();
        switch (option) {
            case "direct-portal":
                if (!player.getHouseManager().isBuildingMode()) {
                    player.sendMessage("You can currently only do this in building mode.");
                    return true;
                }
                player.getDialogueInterpreter().open(DialogueInterpreter.getDialogueKey("con:directportal"));
                return true;
            case "enter":
                String objectName = scenery.getName();
                for (Locations l : Locations.values()) {
                    if (objectName.toLowerCase().contains(l.name().toLowerCase())) {
                        player.teleport(l.location);
                        break;
                    }
                }
                return true;
        }
        return false;
    }

    private static final class DirectPortalDialogue extends Dialogue {

        /**
         * Instantiates a new Direct portal dialogue.
         */
        public DirectPortalDialogue() {

        }

        /**
         * Instantiates a new Direct portal dialogue.
         *
         * @param player the player
         */
        public DirectPortalDialogue(Player player) {
            super(player);
        }

        @Override
        public Dialogue newInstance(Player player) {
            return new DirectPortalDialogue(player);
        }

        @Override
        public boolean open(java.lang.Object... args) {
            sendDialogue(
                "To direct a portal you need enough runes for " + DARK_RED + "100</col> castings of that",
                "teleport spell.",
                "(Combination runes and staffs cannot be used.)"
            );
            stage = 0;
            return true;
        }

        @Override
        public boolean handle(int interfaceId, int buttonId) {
            switch (stage) {
                case 0:
                    setTitle(player, 3);
                    sendDialogueOptions(player, "Redirect which portal?", "1 Portal", "2 Portal", "3 Portal.");
                    stage = 1;
                    break;
                case 1:
                    end();
                    player.setAttribute("con:dp-id", buttonId);
                    player.getDialogueInterpreter().open(394857);
                    break;
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{DialogueInterpreter.getDialogueKey("con:directportal")};
        }

    }

}
