package content.global.skill.summoning.familiar.npc;

import content.global.skill.summoning.familiar.Familiar;
import content.global.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.Items;
import org.rs.consts.NPCs;

/**
 * The type Swamp titan npc.
 */
@Initializable
public class SwampTitanNPC extends content.global.skill.summoning.familiar.Familiar {

    /**
     * Instantiates a new Swamp titan npc.
     */
    public SwampTitanNPC() {
        this(null, NPCs.SWAMP_TITAN_7329);
    }

    /**
     * Instantiates a new Swamp titan npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    public SwampTitanNPC(Player owner, int id) {
        super(owner, id, 5600, Items.SWAMP_TITAN_POUCH_12776, 6, WeaponInterface.STYLE_ACCURATE);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new SwampTitanNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        return false;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.SWAMP_TITAN_7329, NPCs.SWAMP_TITAN_7330};
    }

}
