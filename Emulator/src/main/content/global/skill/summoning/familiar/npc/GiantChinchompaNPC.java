package content.global.skill.summoning.familiar.npc;

import content.global.skill.summoning.familiar.Familiar;
import content.global.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.ImpactHandler.HitsplatType;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.tools.RandomFunction;
import org.rs.consts.Items;
import org.rs.consts.NPCs;

import java.util.List;

/**
 * The type Giant chinchompa npc.
 */
@Initializable
public class GiantChinchompaNPC extends content.global.skill.summoning.familiar.Familiar {

    /**
     * Instantiates a new Giant chinchompa npc.
     */
    public GiantChinchompaNPC() {
        this(null, NPCs.GIANT_CHINCHOMPA_7353);
    }

    /**
     * Instantiates a new Giant chinchompa npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    public GiantChinchompaNPC(Player owner, int id) {
        super(owner, id, 3100, Items.GIANT_CHINCHOMPA_POUCH_12800, 3, WeaponInterface.STYLE_RANGE_ACCURATE);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new GiantChinchompaNPC(owner, id);
    }

    @Override
    public void onAttack(Entity entity) {
        super.onAttack(entity);
        if (RandomFunction.random(20) == 10) {
            executeSpecialMove(new content.global.skill.summoning.familiar.FamiliarSpecial(null));
        }
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        if (!isOwnerAttackable()) {
            return false;
        }
        final List<Entity> entitys = RegionManager.getLocalEntitys(owner, 6);
        entitys.remove(owner);
        entitys.remove(this);
        sendChat("Squeak!");
        animate(Animation.create(7758));
        graphics(Graphics.create(1364));
        GameWorld.getPulser().submit(new Pulse(3, owner, this) {
            @Override
            public boolean pulse() {
                for (Entity entity : entitys) {
                    if (canCombatSpecial(entity, false)) {
                        entity.getImpactHandler().manualHit(GiantChinchompaNPC.this, RandomFunction.random(13), HitsplatType.NORMAL);
                    }
                }
                dismiss();
                return true;
            }
        });
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.GIANT_CHINCHOMPA_7353, NPCs.GIANT_CHINCHOMPA_7354};
    }

}
