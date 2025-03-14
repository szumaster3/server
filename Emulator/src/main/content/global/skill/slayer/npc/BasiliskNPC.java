package content.global.skill.slayer.npc;

import content.global.skill.slayer.items.MirrorShieldHandler;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatSwingHandler;
import core.game.node.entity.npc.AbstractNPC;
import core.game.world.map.Location;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

/**
 * The type Basilisk npc.
 */
@Initializable
public class BasiliskNPC extends AbstractNPC {

    /**
     * Instantiates a new Basilisk npc.
     */
    public BasiliskNPC() {
        super(0, null);
    }

    /**
     * Instantiates a new Basilisk npc.
     *
     * @param id       the id
     * @param location the location
     */
    public BasiliskNPC(int id, Location location) {
        super(id, location);
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new BasiliskNPC(id, location);
    }

    @Override
    public CombatSwingHandler getSwingHandler(boolean swing) {
        return MirrorShieldHandler.SINGLETON;
    }

    @Override
    public void checkImpact(BattleState state) {
        super.checkImpact(state);
        MirrorShieldHandler.SINGLETON.checkImpact(state);
    }

    @Override
    public int[] getIds() {
        return new int[] { NPCs.BASILISK_1616, NPCs.BASILISK_1617 };
    }
}
