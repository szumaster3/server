package content.global.skill.summoning.familiar.npc;

import content.global.skill.summoning.familiar.Familiar;
import content.global.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.SkillBonus;
import core.game.node.entity.skill.Skills;
import core.plugin.Initializable;

/**
 * The type Lava titan npc.
 */
@Initializable
public class LavaTitanNPC extends content.global.skill.summoning.familiar.Familiar {

    /**
     * Instantiates a new Lava titan npc.
     */
    public LavaTitanNPC() {
        this(null, 7341);
    }

    /**
     * Instantiates a new Lava titan npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    public LavaTitanNPC(Player owner, int id) {
        super(owner, id, 6100, 12788, 4, WeaponInterface.STYLE_AGGRESSIVE);
        boosts.add(new SkillBonus(Skills.MINING, 10));
        boosts.add(new SkillBonus(Skills.FIREMAKING, 10));
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new LavaTitanNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        return false;
    }

    @Override
    public int[] getIds() {
        return new int[]{7341, 7342};
    }

}
