package content.global.skill.magic.spells.modern;

import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.spell.CombatSpell;
import core.game.node.entity.combat.spell.Runes;
import core.game.node.entity.combat.spell.SpellType;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.SpellBookManager.SpellBook;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;
import org.rs.consts.Animations;
import org.rs.consts.NPCs;
import org.rs.consts.Sounds;

/**
 * The type Crumble undead spell.
 */
@Initializable
public final class CrumbleUndeadSpell extends CombatSpell {

    /**
     * Instantiates a new Crumble undead spell.
     */
    public CrumbleUndeadSpell() {
        super(SpellType.CRUMBLE_UNDEAD,
                SpellBook.MODERN,
                39,
                24.5,
                Sounds.CRUMBLE_CAST_AND_FIRE_122,
                Sounds.CRUMBLE_HIT_124,
                new Animation(Animations.CAST_SPELL_PUSH_724, Priority.HIGH),
                new Graphics(org.rs.consts.Graphics.CRUMBLE_UNDEAD_CAST_145, 96),
                Projectile.create((Entity) null, null, org.rs.consts.Graphics.CRUMBLE_UNDEAD_PROJECTILE_146, 40, 36, 52, 75, 15, 11),
                new Graphics(org.rs.consts.Graphics.CRUMBLE_UNDEAD_IMPACT_147, 96),
                Runes.EARTH_RUNE.getItem(2),
                Runes.AIR_RUNE.getItem(2),
                Runes.CHAOS_RUNE.getItem(1));
    }

    @Override
    public boolean cast(Entity entity, Node target) {
        NPC npc = target instanceof NPC ? (NPC) target : null;
        if (npc == null || npc.getTask() == null || !npc.getTask().undead || npc.getId() != NPCs.SLASH_BASH_2060) {
            ((Player) entity).getPacketDispatch().sendMessage("This spell only affects the undead.");
            return false;
        }
        return super.cast(entity, target);
    }

    @Override
    public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
        SpellBook.MODERN.register(22, this);
        return this;
    }

    @Override
    public int getMaximumImpact(Entity entity, Entity victim, BattleState state) {
        return type.getImpactAmount(entity, victim, 0);
    }

}
