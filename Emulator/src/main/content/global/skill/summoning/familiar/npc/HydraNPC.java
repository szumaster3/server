package content.global.skill.summoning.familiar.npc;

import content.global.skill.farming.FarmingPatch;
import content.global.skill.farming.Patch;
import content.global.skill.summoning.familiar.Familiar;
import content.global.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.Node;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.plugin.Initializable;

/**
 * The type Hydra npc.
 */
@Initializable
public class HydraNPC extends content.global.skill.summoning.familiar.Familiar {

    /**
     * Instantiates a new Hydra npc.
     */
    public HydraNPC() {
        this(null, 6811);
    }

    /**
     * Instantiates a new Hydra npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    public HydraNPC(Player owner, int id) {
        super(owner, id, 4900, 12025, 6, WeaponInterface.STYLE_RANGE_ACCURATE);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new HydraNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        Node node = special.getNode();
        if (node instanceof Scenery) {
            Scenery scenery = (Scenery) node;
            FarmingPatch farmingPatch = FarmingPatch.forObject(scenery);
            if (farmingPatch != null) {
                Patch patch = farmingPatch.getPatchFor(owner, true);
                patch.regrowIfTreeStump();
                return true;
            }
        }

        return false;
    }

    @Override
    public int[] getIds() {
        return new int[]{6811, 6812};
    }

}
