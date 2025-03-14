package content.region.misthalin.handlers;

import org.rs.consts.Animations;
import core.cache.def.impl.NPCDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * The type Shoo away stray dog plugin.
 */
@Initializable
public final class ShooAwayStrayDogPlugin extends OptionHandler {

    private static final Animation ANIMATION = new Animation(Animations.HUMAN_BLOW_RASPBERRY_2110);

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        NPCDefinition.forId(5917).getHandlers().put("option:shoo-away", this);
        NPCDefinition.setOptionHandler("shoo-away", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        player.sendChat("Thbbbbt!");
        player.animate(ANIMATION);
        NPC dog = (NPC) node;
        dog.sendChat("Whine!");
        dog.moveStep();
        dog.getPulseManager().clear();
        return true;
    }

}
