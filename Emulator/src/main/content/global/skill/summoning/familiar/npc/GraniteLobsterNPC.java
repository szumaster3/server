package content.global.skill.summoning.familiar.npc;

import content.global.skill.gathering.fishing.Fish;
import content.global.skill.summoning.familiar.Familiar;
import content.global.skill.summoning.familiar.FamiliarSpecial;
import content.global.skill.summoning.familiar.Forager;
import core.game.node.entity.Entity;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.SkillBonus;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.tools.RandomFunction;

/**
 * The type Granite lobster npc.
 */
@Initializable
public class GraniteLobsterNPC extends Forager {

    private static final Item[] FISH = new Item[]{new Item(383), new Item(371)};

    /**
     * Instantiates a new Granite lobster npc.
     */
    public GraniteLobsterNPC() {
        this(null, 6849);
    }

    /**
     * Instantiates a new Granite lobster npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    public GraniteLobsterNPC(Player owner, int id) {
        super(owner, id, 4700, 12069, 6);
        boosts.add(new SkillBonus(Skills.FISHING, 4));
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new GraniteLobsterNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        Entity target = special.getTarget();
        if (!canCombatSpecial(target)) {
            return false;
        }
        animate(new Animation(8118));
        graphics(Graphics.create(1351));
        Projectile.ranged(this, target, 1352, 60, 40, 1, 45).send();
        sendFamiliarHit(target, 14);
        return true;
    }

    @Override
    public void handlePassiveAction() {
        if (RandomFunction.random(40) == 1) {
            final Item item = FISH[RandomFunction.random(FISH.length)];
            animate(Animation.create(8107));
            Fish fish = Fish.forItem(item);
            owner.getSkills().addExperience(Skills.FISHING, fish.getExperience() * 0.10);
            produceItem(item);
        }
    }

    @Override
    public int[] getIds() {
        return new int[]{6849, 6850};
    }

}
