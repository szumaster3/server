package content.global.skill.summoning.familiar.npc;

import content.global.skill.summoning.familiar.Familiar;
import content.global.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.ImpactHandler.HitsplatType;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.tools.RandomFunction;
import org.rs.consts.Items;
import org.rs.consts.NPCs;

/**
 * The type Vampire bat npc.
 */
@Initializable
public class VampireBatNPC extends content.global.skill.summoning.familiar.Familiar {

    /**
     * Instantiates a new Vampire bat npc.
     */
    public VampireBatNPC() {
        this(null, NPCs.VAMPIRE_BAT_6835);
    }

    /**
     * Instantiates a new Vampire bat npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    public VampireBatNPC(Player owner, int id) {
        super(owner, id, 3300, Items.VAMPIRE_BAT_POUCH_12053, 4, WeaponInterface.STYLE_CONTROLLED);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new VampireBatNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        final Entity target = (Entity) special.getNode();
        if (!canCombatSpecial(target)) {
            return false;
        } else {
            if (RandomFunction.random(10) < 4) {
                owner.getSkills().heal(2);
            }
            visualize(Animation.create(8275), Graphics.create(1323));
            target.getImpactHandler().manualHit(this, RandomFunction.random(12), HitsplatType.NORMAL);
            return true;
        }
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.VAMPIRE_BAT_6835, NPCs.VAMPIRE_BAT_6836};
    }

}
