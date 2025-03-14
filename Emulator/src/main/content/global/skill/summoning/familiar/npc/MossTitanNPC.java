package content.global.skill.summoning.familiar.npc;

import content.global.skill.summoning.familiar.Familiar;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;

/**
 * The type Moss titan npc.
 */
@Initializable
public class MossTitanNPC extends ElementalTitanNPC {

    /**
     * Instantiates a new Moss titan npc.
     */
    public MossTitanNPC() {
        this(null, 7357);
    }

    /**
     * Instantiates a new Moss titan npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    public MossTitanNPC(Player owner, int id) {
        super(owner, id, 5800, 12804, 20, WeaponInterface.STYLE_AGGRESSIVE);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new MossTitanNPC(owner, id);
    }

    @Override
    public int[] getIds() {
        return new int[]{7357, 7358};
    }

}
