package content.region.kandarin.west_ardougne.quest.sheepherder.plugin;

import content.region.kandarin.west_ardougne.quest.sheepherder.SheepHerder;
import content.region.kandarin.west_ardougne.quest.sheepherder.npc.HerderSheepNPC;
import core.game.interaction.*;
import core.game.node.Node;
import core.game.node.entity.impl.PulseType;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.tools.RandomFunction;

/**
 * The type Prod action handler.
 */
@Initializable
public class ProdActionHandler extends PluginInteraction {

    @Override
    public boolean handle(Player player, NPC npc, Option option) {
        HerderSheepNPC n = (HerderSheepNPC) npc;
        if (option.getName().equalsIgnoreCase("prod")) {
            player.getPulseManager().run(new MovementPulse(player, getDestination(player, n)) {
                @Override
                public boolean pulse() {
                    handleProd(player, n);
                    return true;
                }
            }, PulseType.STANDARD);
            return true;
        }
        return false;
    }

    /**
     * Gets destination.
     *
     * @param p the p
     * @param n the n
     * @return the destination
     */
    public Location getDestination(Player p, Node n) {
        return DestinationFlag.ENTITY.getDestination(p, n);
    }

    /**
     * Handle prod.
     *
     * @param p the p
     * @param n the n
     */
    public void handleProd(Player p, HerderSheepNPC n) {
        p.faceLocation(n.getLocation());
        Pulse prodPulse = new Pulse() {
            @Override
            public boolean pulse() {
                if (p.getEquipment().containsItem(SheepHerder.CATTLE_PROD)) {
                    p.getAnimator().reset();
                    p.getAnimator().animate(new Animation(412));
                    int diffX = n.getLocation().getX() - p.getLocation().getX();
                    int diffY = n.getLocation().getY() - p.getLocation().getY();
                    int steps = RandomFunction.random(3, 5);
                    Location destination = n.getLocation().transform((diffX) * steps, (diffY) * steps, 0);
                    n.sendChat("BAAAAA!");
                    n.moveTo(destination);
                    n.setAttribute("recently-prodded", true);
                    n.ticksTilReturn = GameWorld.getTicks() + 20;
                    return true;
                } else {
                    p.sendMessage("You can't prod a sheep with your bare hands.");
                    return true;
                }
            }
        };
        p.getPulseManager().run(prodPulse);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        setIds(new int[]{SheepHerder.RED_SHEEP, SheepHerder.BLUE_SHEEP, SheepHerder.GREEN_SHEEP, SheepHerder.YELLOW_SHEEP});
        PluginInteractionManager.register(this, PluginInteractionManager.InteractionType.NPC);
        return this;
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }
}
