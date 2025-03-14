package content.region.karamja.handlers.tzhaar;

import core.cache.def.impl.SceneryDefinition;
import core.game.activity.ActivityManager;
import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * The type Tzhaar city plugin.
 */
@Initializable
public final class TzhaarCityPlugin extends OptionHandler {

    private static final Location[] LOCATIONS = new Location[]{Location.create(2480, 5175, 0), Location.create(2866, 9571, 0)};

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        SceneryDefinition.forId(31284).getHandlers().put("option:enter", this);

        SceneryDefinition.forId(31292).getHandlers().put("option:go-through", this);

        SceneryDefinition.forId(9358).getHandlers().put("option:go-through", this);

        SceneryDefinition.forId(9359).getHandlers().put("option:enter", this);

        SceneryDefinition.forId(9356).getHandlers().put("option:enter", this);
        SceneryDefinition.forId(9369).getHandlers().put("option:pass", this);
        new TzhaarDialogue().init();
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        int id = node.getId();
        switch (option) {
            case "enter":
                switch (id) {
                    case 9358:
                    case 31292:
                        player.getPacketDispatch().sendMessage("Nothing interesting happens.");
                        break;
                    case 31284:
                        player.getProperties().setTeleportLocation(LOCATIONS[0]);
                        break;
                    case 9359:
                        player.getProperties().setTeleportLocation(LOCATIONS[1]);
                        break;
                    case 9356:
                        if (player.getFamiliarManager().hasFamiliar()) {
                            player.getPacketDispatch().sendMessage("You can't enter this with a follower.");
                            break;
                        }
                        ActivityManager.start(player, "fight caves", false);
                        break;
                }
                break;
            case "pass":
                switch (id) {
                    case 9369:
                        ActivityManager.start(player, "fight pits", false);
                        break;
                }
                break;
        }
        return true;
    }

    /**
     * The type Tzhaar dialogue.
     */
    public static final class TzhaarDialogue extends Dialogue {

        /**
         * Instantiates a new Tzhaar dialogue.
         */
        public TzhaarDialogue() {

        }

        /**
         * Instantiates a new Tzhaar dialogue.
         *
         * @param player the player
         */
        public TzhaarDialogue(Player player) {
            super(player);
        }

        @Override
        public Dialogue newInstance(Player player) {
            return new TzhaarDialogue(player);
        }

        @Override
        public boolean open(Object... args) {
            npc = (NPC) args[0];
            npc(FaceAnim.CHILD_FRIENDLY, "Can I help you JalYt-Ket-" + player.getUsername() + "?");
            return true;
        }

        @Override
        public boolean handle(int interfaceId, int buttonId) {
            switch (stage) {
                case 0:
                    options("What do you have to trade?", "What did you call me?", "No I'm fine thanks.");
                    stage = 1;
                    break;
                case 1:
                    switch (buttonId) {
                        case 1:
                            end();
                            npc.openShop(player);
                            break;
                        case 2:
                            player("What did you call me?");
                            stage = 20;
                            break;
                        case 3:
                            player("No I'm fine thanks.");
                            stage = 30;
                            break;
                    }
                    break;
                case 10:
                    break;
                case 20:
                    npc(FaceAnim.CHILD_FRIENDLY, "Are you not JalYt-Ket?");
                    stage = 21;
                    break;
                case 21:
                    options("What's a 'JalYt-Ket'?", "I guess so...", "No I'm not!");
                    stage = 22;
                    break;
                case 22:
                    switch (buttonId) {
                        case 1:
                            player("What's a 'JalYt-Ket'?");
                            stage = 100;
                            break;
                        case 2:
                            player("I guess so...");
                            stage = 120;
                            break;
                        case 3:
                            player("No I'm not!");
                            stage = 130;
                            break;
                    }
                    break;
                case 100:
                    npc(FaceAnim.CHILD_FRIENDLY, "That what you are... you tough and strong no?");
                    stage = 101;
                    break;
                case 101:
                    player("Well yes I suppose I am...");
                    stage = 102;
                    break;
                case 102:
                    npc(FaceAnim.CHILD_FRIENDLY, "Then you JalYt-Ket!");
                    stage = 103;
                    break;
                case 103:
                    end();
                    break;
                case 120:
                    npc(FaceAnim.CHILD_FRIENDLY, "Well then, no problems.");
                    stage = 121;
                    break;
                case 121:
                    end();
                    break;
                case 130:
                    end();
                    break;
                case 23:
                    end();
                    break;
                case 30:
                    end();
                    break;
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{2620, 2622, 2623};
        }

    }
}
