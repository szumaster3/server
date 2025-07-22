package content.region.kandarin.seers.quest.fishingcompo.plugin;

import content.data.GameAttributes;
import core.api.Container;
import core.game.dialogue.FaceAnim;
import core.game.interaction.MovementPulse;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.PluginInteraction;
import core.game.interaction.PluginInteractionManager;
import core.game.node.Node;
import core.game.node.entity.impl.PulseType;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.plugin.Plugin;
import org.rs.consts.Items;

import static core.api.ContentAPIKt.*;

/**
 * Represents the wall pipe interactions in Fishing Contest quest.
 */
@Initializable
public class WallPipePlugin extends PluginInteraction {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        setIds(new int[]{Items.GARLIC_1550, 41});
        PluginInteractionManager.register(this, PluginInteractionManager.InteractionType.USE_WITH);
        PluginInteractionManager.register(this, PluginInteractionManager.InteractionType.OBJECT);
        return this;
    }

    @Override
    public boolean handle(Player player, NodeUsageEvent event) {
        if (event.getUsed() instanceof Item && event.getUsedWith() instanceof Scenery) {
            Scenery usedWith = event.getUsedWith().asScenery();
            Item used = event.getUsedItem();

            if (used.getId() == Items.GARLIC_1550 && usedWith.getId() == 41 && usedWith.getLocation().equals(Location.create(2638, 3446, 0)) && !player.getAttribute(GameAttributes.QUEST_FISHINGCOMPO_STASH_GARLIC, false)) {
                player.getPulseManager().run(new MovementPulse(player, usedWith.getLocation().transform(0, -1, 0)) {
                    @Override
                    public boolean pulse() {
                        removeItem(player, new Item(Items.GARLIC_1550, 1), Container.INVENTORY);
                        setAttribute(player, GameAttributes.QUEST_FISHINGCOMPO_STASH_GARLIC, true);
                        sendItemDialogue(player, used, "You stash the garlic in the pipe.");
                        return true;
                    }
                }, PulseType.STANDARD);
                return true;
            } else {
                faceLocation(player, usedWith.getLocation());
                player.getDialogueInterpreter().sendDialogue("I shoved garlic up here.");
            }
        }
        return false;
    }

    @Override
    public boolean handle(Player player, Node node) {
        if (node instanceof Scenery) {
            Scenery object = node.asScenery();
            if (object.getId() == 41 && object.getLocation().equals(Location.create(2638, 3446, 0)) && player.getAttribute(GameAttributes.QUEST_FISHINGCOMPO_STASH_GARLIC, false)) {
                player.getPulseManager().run(new MovementPulse(player, object.getLocation().transform(0, -1, 0)) {
                    @Override
                    public boolean pulse() {
                        player.getDialogueInterpreter().sendDialogue("I shoved garlic up here.");
                        return true;
                    }
                }, PulseType.STANDARD);
                return true;
            } else {
                faceLocation(player, object.getLocation());
                sendPlayerDialogue(player, "Ewww - it's a smelly sewage pipe.", FaceAnim.DISGUSTED);
            }
        }
        return false;
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }
}
