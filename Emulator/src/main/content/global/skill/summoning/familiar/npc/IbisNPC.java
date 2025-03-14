package content.global.skill.summoning.familiar.npc;

import content.global.skill.gathering.fishing.Fish;
import content.global.skill.summoning.familiar.Familiar;
import content.global.skill.summoning.familiar.FamiliarSpecial;
import content.global.skill.summoning.familiar.Forager;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.SkillBonus;
import core.game.node.entity.skill.Skills;
import core.game.node.item.GroundItemManager;
import core.game.node.item.Item;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.tools.RandomFunction;

/**
 * The type Ibis npc.
 */
@Initializable
public class IbisNPC extends Forager {

    private static final Fish[] FISH = new Fish[]{Fish.SHRIMP, Fish.BASS, Fish.COD, Fish.MACKEREL};

    /**
     * Instantiates a new Ibis npc.
     */
    public IbisNPC() {
        this(null, 6991);
    }

    /**
     * Instantiates a new Ibis npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    public IbisNPC(Player owner, int id) {
        super(owner, id, 3800, 12531, 12, new Item(361), new Item(373));
        boosts.add(new SkillBonus(Skills.FISHING, 3));
    }

    @Override
    public void handlePassiveAction() {
        if (RandomFunction.random(15) < 4) {
            produceItem();
        }
    }

    @Override
    public boolean produceItem(Item item) {
        if (super.produceItem(item)) {
            if (item.getId() == 373) {
                owner.getSkills().addExperience(Skills.FISHING, 10);
            }
            return true;
        }
        return false;
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new IbisNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        animate(Animation.create(8201));
        GameWorld.getPulser().submit(new Pulse(3, owner, this) {
            @Override
            public boolean pulse() {
                Location loc = null;
                for (int i = 0; i < 2; i++) {
                    loc = owner.getLocation().transform(RandomFunction.random(2), RandomFunction.random(2), 0);
                    if (RegionManager.getObject(loc) != null) {
                        continue;
                    }
                    GroundItemManager.create(FISH[RandomFunction.random(FISH.length)].getItem(), loc, owner);
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{6991};
    }

}
