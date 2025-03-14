package content.global.skill.slayer.npc;

import core.game.node.entity.combat.BattleState;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.map.Location;
import core.plugin.Initializable;
import org.rs.consts.Items;
import org.rs.consts.NPCs;

/**
 * The type Bug swarm npc.
 */
@Initializable
public class BugSwarmNPC extends AbstractNPC {

    private static final Item LIT_LANTERN = new Item(Items.LIT_BUG_LANTERN_7053);

    /**
     * Instantiates a new Bug swarm npc.
     */
    public BugSwarmNPC() {
        super(0, null);
    }


    /**
     * Instantiates a new Bug swarm npc.
     *
     * @param id       the id
     * @param location the location
     */
    public BugSwarmNPC(int id, Location location) {
        super(id, location);
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new BugSwarmNPC(id, location);
    }

    @Override
    public void checkImpact(BattleState state) {
        super.checkImpact(state);
        if (state.getAttacker() instanceof Player) {
            Player player = (Player) state.getAttacker();
            if (!player.getEquipment().containsItem(LIT_LANTERN)) {
                if (state.getEstimatedHit() > -1) {
                    state.setEstimatedHit(0);
                }
                if (state.getSecondaryHit() > -1) {
                    state.setSecondaryHit(0);
                }
            }
        }
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.HARPIE_BUG_SWARM_3153};
    }
}
