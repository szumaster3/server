package content.global.skill.slayer.npc;

import core.game.node.entity.Entity;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

/**
 * The type Cave bug npc.
 */
@Initializable
public class CaveBugNPC extends AbstractNPC {

    /**
     * Instantiates a new Cave bug npc.
     */
    public CaveBugNPC() {
        super(0, null);
    }

    /**
     * Instantiates a new Cave bug npc.
     *
     * @param id       the id
     * @param location the location
     */
    public CaveBugNPC(int id, Location location) {
        super(id, location);
    }

    @Override
    public void finalizeDeath(Entity killer) {
        super.finalizeDeath(killer);
        if (killer instanceof Player) {
            Player p = (Player) killer;
        }
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new CaveBugNPC(id, location);
    }

    @Override
    public int[] getIds() {
        return new int[]{
            NPCs.CAVE_BUG_1832,
            NPCs.CAVE_BUG_LARVA_1833,
            NPCs.CAVE_BUG_5750
        };
    }
}
