package content.global.skill.summoning.familiar.npc;

import content.global.skill.summoning.familiar.Familiar;
import content.global.skill.summoning.familiar.FamiliarSpecial;
import core.cache.def.impl.NPCDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.ImpactHandler.HitsplatType;
import core.game.node.entity.player.Player;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.tools.RandomFunction;

import java.util.List;

/**
 * The type Smoke devil npc.
 */
@Initializable
public class SmokeDevilNPC extends content.global.skill.summoning.familiar.Familiar {

    /**
     * Instantiates a new Smoke devil npc.
     */
    public SmokeDevilNPC() {
        this(null, 6865);
    }

    /**
     * Instantiates a new Smoke devil npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    public SmokeDevilNPC(Player owner, int id) {
        super(owner, id, 4800, 12085, 6);
    }

    @Override
    public content.global.skill.summoning.familiar.Familiar construct(Player owner, int id) {
        return new SmokeDevilNPC(owner, id);
    }

    @Override
    public void configureFamiliar() {
        NPCDefinition.setOptionHandler("flames", new OptionHandler() {
            @Override
            public Plugin<Object> newInstance(Object arg) throws Throwable {
                return this;
            }

            @Override
            public boolean handle(Player player, Node node, String option) {
                final content.global.skill.summoning.familiar.Familiar familiar = (Familiar) node;
                if (!player.getFamiliarManager().isOwner(familiar)) {
                    return true;
                }
                // TODO:
                return true;
            }
        });
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        if (!isOwnerAttackable()) {
            return false;
        }
        final List<Entity> entitys = RegionManager.getLocalEntitys(this, 1);
        entitys.remove(this);
        entitys.remove(owner);
        visualize(Animation.create(7820), Graphics.create(1375));
        for (Entity e : entitys) {
            if (super.canCombatSpecial(e, false)) {
                e.getImpactHandler().manualHit(this, RandomFunction.random(6), HitsplatType.NORMAL);
            }
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{6865, 6866};
    }

}
