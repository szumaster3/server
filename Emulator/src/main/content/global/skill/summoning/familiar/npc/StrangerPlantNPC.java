package content.global.skill.summoning.familiar.npc;

import content.global.skill.summoning.familiar.Familiar;
import content.global.skill.summoning.familiar.FamiliarSpecial;
import content.global.skill.summoning.familiar.Forager;
import core.game.node.entity.Entity;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.tools.RandomFunction;
import org.rs.consts.Items;
import org.rs.consts.NPCs;

import static core.api.event.EventAPIKt.applyPoison;

/**
 * The type Stranger plant npc.
 */
@Initializable
public class StrangerPlantNPC extends Forager {

    /**
     * Instantiates a new Stranger plant npc.
     */
    public StrangerPlantNPC() {
        this(null, NPCs.STRANGER_PLANT_6827);
    }

    /**
     * Instantiates a new Stranger plant npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    public StrangerPlantNPC(Player owner, int id) {
        super(owner, id, 4900, Items.STRANGER_PLANT_POUCH_12045, 6, new Item(Items.STRANGE_FRUIT_464));
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new StrangerPlantNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        if (!canCombatSpecial(special.getTarget())) {
            return false;
        }
        Entity target = special.getTarget();
        if (RandomFunction.random(2) == 1) {
            applyPoison(target, owner, 20);
        }
        animate(Animation.create(8211));
        Projectile.ranged(this, target, 1508, 50, 40, 1, 45).send();
        target.graphics(Graphics.create(1511));
        sendFamiliarHit(target, 2);
        return false;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.STRANGER_PLANT_6827, NPCs.STRANGER_PLANT_6828};
    }

}
