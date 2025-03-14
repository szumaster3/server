package content.global.skill.summoning.familiar.npc;

import content.global.skill.summoning.familiar.Familiar;
import content.global.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.SkillBonus;
import core.game.node.entity.skill.Skills;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import org.rs.consts.Animations;
import org.rs.consts.Items;
import org.rs.consts.NPCs;

/**
 * The type Wolpertinger npc.
 */
@Initializable
public class WolpertingerNPC extends content.global.skill.summoning.familiar.Familiar {

    /**
     * Instantiates a new Wolpertinger npc.
     */
    public WolpertingerNPC() {
        this(null, NPCs.WOLPERTINGER_6869);
    }

    /**
     * Instantiates a new Wolpertinger npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    public WolpertingerNPC(Player owner, int id) {
        super(owner, id, 6200, Items.WOLPERTINGER_POUCH_12089, 1, WeaponInterface.STYLE_CAST);
        boosts.add(new SkillBonus(Skills.HUNTER, 5));
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new WolpertingerNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        owner.getSkills().updateLevel(Skills.MAGIC, 7, (owner.getSkills().getStaticLevel(Skills.MAGIC) + 7));
        visualize(Animation.create(8267), Graphics.create(1464));
        return true;
    }

    @Override
    public void visualizeSpecialMove() {
        owner.visualize(Animation.create(Animations.CAST_FAMILIAR_SCROLL_7660), Graphics.create(org.rs.consts.Graphics.WHITE_FAMILIAR_GRAPHIC_1306));
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.WOLPERTINGER_6869, NPCs.WOLPERTINGER_6870};
    }

}
