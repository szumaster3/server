package content.global.skill.construction.decoration;

import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.plugin.Plugin;
import org.rs.consts.Items;

/**
 * The type Beer barrel plugin.
 */
@Initializable
public class BeerBarrelPlugin extends UseWithHandler {

    private static final int[] OBJECTS = new int[]{
        13568, 13569, 13570, 13571, 13572, 13573
    };

    /**
     * Instantiates a new Beer barrel plugin.
     */
    public BeerBarrelPlugin() {
        super(1919);
    }

    @Override
    public Plugin<java.lang.Object> newInstance(java.lang.Object arg) throws Throwable {

        for (int id : OBJECTS) {
            addHandler(id, OBJECT_TYPE, this);
        }
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        Player player = event.getPlayer();
        final Scenery scenery = (Scenery) event.getUsedWith();

        if (player.getInventory().remove(new Item(Items.BEER_GLASS_1919))) {
            player.animate(Animation.create(833));
            player.sendMessage("You fill up your glass.");
            player.getInventory().add(new Item(getReward(scenery.getId()), 1));
        }
        return true;
    }

    /**
     * Gets reward.
     *
     * @param barrelId the barrel id
     * @return the reward
     */
    public int getReward(int barrelId) {
        switch (barrelId) {
            case 13568:
                return 1917;
            case 13569:
                return 5763;
            case 13570:
                return 1905;
            case 13571:
                return 1909;
            case 13572:
                return 1911;
            case 13573:
                return 5755;
            default:
                return 1917;
        }
    }
}
