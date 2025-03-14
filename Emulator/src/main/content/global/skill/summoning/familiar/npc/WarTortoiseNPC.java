package content.global.skill.summoning.familiar.npc;

import content.global.skill.summoning.familiar.BurdenBeast;
import content.global.skill.summoning.familiar.Familiar;
import content.global.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import org.rs.consts.Animations;
import org.rs.consts.Items;
import org.rs.consts.NPCs;

/**
 * The type War tortoise npc.
 */
@Initializable
public final class WarTortoiseNPC extends BurdenBeast {

    /**
     * Instantiates a new War tortoise npc.
     */
    public WarTortoiseNPC() {
        this(null, NPCs.WAR_TORTOISE_6815);
    }

    /**
     * Instantiates a new War tortoise npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    public WarTortoiseNPC(Player owner, int id) {
        super(owner, id, 4300, Items.WAR_TORTOISE_POUCH_12031, 20, 18, WeaponInterface.STYLE_DEFENSIVE);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new WarTortoiseNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        owner.getSkills().updateLevel(Skills.DEFENCE, 9, owner.getSkills().getStaticLevel(Skills.DEFENCE) + 9);
        visualize(Animation.create(8288), Graphics.create(org.rs.consts.Graphics.TURTLE_SHELL_SPIN_1414));
        return true;
    }

    @Override
    public void visualizeSpecialMove() {
        owner.visualize(Animation.create(Animations.CAST_FAMILIAR_SCROLL_7660), Graphics.create(org.rs.consts.Graphics.YELLOW_FAMILIAR_GRAPHIC_1310));
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.WAR_TORTOISE_6815, NPCs.WAR_TORTOISE_6816};
    }

}
