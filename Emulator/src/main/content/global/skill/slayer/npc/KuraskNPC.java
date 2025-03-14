package content.global.skill.slayer.npc;

import content.global.skill.slayer.SlayerUtils;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

/**
 * The type Kurask npc.
 */
@Initializable
public class KuraskNPC extends AbstractNPC {

    /**
     * Instantiates a new Kurask npc.
     *
     * @param id       the id
     * @param location the location
     */
    public KuraskNPC(int id, Location location) {
        super(id, location);
    }

    /**
     * Instantiates a new Kurask npc.
     */
    public KuraskNPC() {
        super(0, null);
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new KuraskNPC(id, location);
    }

    @Override
    public void checkImpact(BattleState state) {
        super.checkImpact(state);
        boolean effective = false;
        if (state.getAttacker() instanceof Player) {
            Player player = (Player) state.getAttacker();
            effective = SlayerUtils.hasBroadWeaponEquipped(player, state);
        }
        if (!effective) {
            state.setEstimatedHit(0);
            if (state.getSecondaryHit() > 0) {
                state.setSecondaryHit(0);
            }
        }
    }

    @Override
    public int[] getIds() {
        return new int[] { NPCs.KURASK_1608, NPCs.KURASK_1609, NPCs.KURASK_4229 };
    }
}
