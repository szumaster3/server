package content.global.skill.summoning.familiar.npc;

import content.global.skill.summoning.familiar.Familiar;
import content.global.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;

/**
 * The type Karamthulhu overlord npc.
 */
@Initializable
public class KaramthulhuOverlordNPC extends content.global.skill.summoning.familiar.Familiar {

    /**
     * Instantiates a new Karamthulhu overlord npc.
     */
    public KaramthulhuOverlordNPC() {
        this(null, 6809);
    }

    /**
     * Instantiates a new Karamthulhu overlord npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    public KaramthulhuOverlordNPC(Player owner, int id) {
        super(owner, id, 4400, 12023, 3, WeaponInterface.STYLE_RANGE_ACCURATE);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new KaramthulhuOverlordNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        return false;
    }

    @Override
    public int[] getIds() {
        return new int[]{6809, 6810};
    }

}
