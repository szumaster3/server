package content.region.kandarin.quest.sheepherder.handler;

import content.region.kandarin.quest.sheepherder.SheepHerder;
import core.game.interaction.*;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.plugin.Plugin;

import java.util.Objects;

import static core.api.ContentAPIKt.sendDialogueLines;
import static core.api.ContentAPIKt.setAttribute;

/**
 * The type Incinerator handler.
 */
@Initializable
public class IncineratorHandler extends PluginInteraction {
    @Override
    public Plugin<java.lang.Object> newInstance(java.lang.Object arg) throws Throwable {
        setIds(new int[]{SheepHerder.RED_SHEEP_BONES.getId(), SheepHerder.GREEN_SHEEP_BONES.getId(), SheepHerder.BLUE_SHEEP_BONES.getId(), SheepHerder.YELLOW_SHEEP_BONES.getId()});
        PluginInteractionManager.register(this, PluginInteractionManager.InteractionType.USEWITH);
        return this;
    }

    @Override
    public boolean handle(Player player, NodeUsageEvent event) {
        Node n = event.getUsedWith();
        if (n instanceof Scenery) {
            Scenery obj = (Scenery) n;
            if (n.getId() == 165) {
                player.getPulseManager().run(new MovementPulse(player, DestinationFlag.OBJECT.getDestination(player, obj)) {
                    @Override
                    public boolean pulse() {
                        player.lock(2);
                        player.getInventory().remove(event.getUsedItem());
                        player.getAnimator().reset();
                        player.getAnimator().animate(new Animation(3243));
                        switch (Objects.requireNonNull(event.getUsedItem()).getId()) {
                            case 280:
                                setAttribute(player, "/save:sheep_herder:red_dead", true);
                                break;
                            case 281:
                                setAttribute(player, "/save:sheep_herder:green_dead", true);
                                break;
                            case 282:
                                setAttribute(player, "/save:sheep_herder:blue_dead", true);
                                break;
                            case 283:
                                setAttribute(player, "/save:sheep_herder:yellow_dead", true);
                                break;
                        }
                        if (player.getAttribute("sheep_herder:red_dead", false) && player.getAttribute("sheep_herder:green_dead", false) && player.getAttribute("sheep_herder:blue_dead", false) && player.getAttribute("sheep_herder:yellow_dead", false)) {
                            setAttribute(player, "/save:sheep_herder:all_dead", true);
                        }
                        sendDialogueLines(player, "You put the sheep remains in the furnace", "the remains burn to dust.");
                        return true;
                    }
                });
                return true;
            }
        }
        return false;
    }

    @Override
    public java.lang.Object fireEvent(String identifier, java.lang.Object... args) {
        return null;
    }
}
