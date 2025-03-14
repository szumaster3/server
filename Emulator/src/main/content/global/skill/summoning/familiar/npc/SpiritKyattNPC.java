package content.global.skill.summoning.familiar.npc;

import content.global.skill.summoning.familiar.Familiar;
import content.global.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.SkillBonus;
import core.game.node.entity.skill.Skills;
import core.plugin.Initializable;

/**
 * The type Spirit kyatt npc.
 */
@Initializable
public class SpiritKyattNPC extends content.global.skill.summoning.familiar.Familiar {

    /**
     * Instantiates a new Spirit kyatt npc.
     */
    public SpiritKyattNPC() {
        this(null, 7366);
    }

    /**
     * Instantiates a new Spirit kyatt npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    public SpiritKyattNPC(Player owner, int id) {
        super(owner, id, 4900, 12812, 3, WeaponInterface.STYLE_ACCURATE);
        boosts.add(new SkillBonus(Skills.HUNTER, 5));
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new SpiritKyattNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        if (!super.isOwnerAttackable()) {
            return false;
        }
        call();
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{7365, 7366};
    }

}
