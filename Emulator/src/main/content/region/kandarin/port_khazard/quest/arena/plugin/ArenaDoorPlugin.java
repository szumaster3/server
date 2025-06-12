package content.region.kandarin.port_khazard.quest.arena.plugin;

import core.cache.def.impl.SceneryDefinition;
import core.game.dialogue.FaceAnim;
import core.game.global.action.DoorActionHandler;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.plugin.Plugin;
import org.rs.consts.Quests;

/**
 * The type Arena door handler.
 */
@Initializable
public final class ArenaDoorPlugin extends OptionHandler {

    @Override
    public Plugin<java.lang.Object> newInstance(java.lang.Object arg) throws Throwable {
        SceneryDefinition.forId(82).getHandlers().put("option:open", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        if (option.equals("open")) {
            if (player.getQuestRepository().getStage(Quests.FIGHT_ARENA) <= 67) {
                player.getDialogueInterpreter().sendDialogues(255, FaceAnim.ANNOYED, "And where do you think you're going? Only General", "Khazard decides who fights in the arena. Get out of", "here.");

            } else if (player.getQuestRepository().getStage(Quests.FIGHT_ARENA) >= 68 && player.getQuestRepository().getStage(Quests.FIGHT_ARENA) < 91) {
                player.getDialogueInterpreter().sendDialogue("This door appears to be locked.");

            } else if (player.getQuestRepository().getStage(Quests.FIGHT_ARENA) >= 91 && node.getLocation().equals(new Location(2606, 3152, 0))) {
                DoorActionHandler.handleAutowalkDoor(player, (Scenery) node, player.getLocation().getX() >= 2606 ? Location.create(2605, 3153, 0) : Location.create(2607, 3151, 0));
            } else {
                DoorActionHandler.handleDoor(player, (Scenery) node);
            }
        }
        return true;
    }

    @Override
    public Location getDestination(Node node, Node n) {
        if (n instanceof Scenery) {
            final Scenery scenery = (Scenery) n;
            if (scenery.getId() == 82 && scenery.getLocation().equals(new Location(2606, 3152, 0))) {
                return node.getLocation().getX() >= 2606 ? Location.create(2607, 3151, 0) : Location.create(2605, 3153, 0);
            }
        }
        return null;
    }

}
