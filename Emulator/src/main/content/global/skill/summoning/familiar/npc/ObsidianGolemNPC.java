package content.global.skill.summoning.familiar.npc;

import content.global.skill.summoning.familiar.Familiar;
import content.global.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.SkillBonus;
import core.game.node.entity.skill.Skills;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;

/**
 * The type Obsidian golem npc.
 */
@Initializable
public class ObsidianGolemNPC extends content.global.skill.summoning.familiar.Familiar {

    /**
     * Instantiates a new Obsidian golem npc.
     */
    public ObsidianGolemNPC() {
        this(null, 7345);
    }

    /**
     * Instantiates a new Obsidian golem npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    public ObsidianGolemNPC(Player owner, int id) {
        super(owner, id, 5500, 12792, 12, WeaponInterface.STYLE_AGGRESSIVE);
        boosts.add(new SkillBonus(Skills.MINING, 7));
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new ObsidianGolemNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        graphics(Graphics.create(1465));
        owner.getSkills().updateLevel(Skills.STRENGTH, 9);
        return true;
    }

    @Override
    public String getText() {
        return "Onwards, to Glory!";
    }

    @Override
    public int[] getIds() {
        return new int[]{7345, 7346};
    }

}
