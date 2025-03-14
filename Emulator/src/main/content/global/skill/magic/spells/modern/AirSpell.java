package content.global.skill.magic.spells.modern;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.spell.CombatSpell;
import core.game.node.entity.combat.spell.Runes;
import core.game.node.entity.combat.spell.SpellType;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.player.link.SpellBookManager.SpellBook;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;
import org.rs.consts.Sounds;

/**
 * The type Air spell.
 */
@Initializable
public final class AirSpell extends CombatSpell {

    private static final Graphics STRIKE_START = new Graphics(90, 96);
    private static final Projectile STRIKE_PROJECTILE = Projectile.create((Entity) null, null, 91, 40, 36, 52, 75, 15, 11);
    private static final Graphics STRIKE_END = new Graphics(92, 96);
    private static final Graphics BOLT_START = new Graphics(117, 96);
    private static final Projectile BOLT_PROJECTILE = Projectile.create((Entity) null, null, 118, 40, 36, 52, 75, 15, 11);
    private static final Graphics BOLT_END = new Graphics(119, 96);
    private static final Graphics BLAST_START = new Graphics(132, 96);
    private static final Projectile BLAST_PROJECTILE = Projectile.create((Entity) null, null, 133, 40, 36, 52, 75, 15, 11);
    private static final Graphics BLAST_END = new Graphics(134, 96);
    private static final Graphics WAVE_START = new Graphics(158, 96);
    private static final Projectile WAVE_PROJECTILE = Projectile.create((Entity) null, null, 159, 40, 36, 52, 75, 15, 11);
    private static final Graphics WAVE_END = new Graphics(160, 96);
    private static final Animation ANIMATION = new Animation(711, Priority.HIGH);

    /**
     * Instantiates a new Air spell.
     */
    public AirSpell() {

    }

    private AirSpell(SpellType type, int level, double baseExperience, int sound, Graphics start, Projectile projectile, Graphics end, Item... runes) {
        super(type, SpellBook.MODERN, level, baseExperience, sound, sound + 1, ANIMATION, start, projectile, end, runes);
    }

    @Override
    public int getMaximumImpact(Entity entity, Entity victim, BattleState state) {
        return getType().getImpactAmount(entity, victim, 1);
    }

    @Override
    public Plugin<SpellType> newInstance(SpellType type) throws Throwable {
        SpellBook.MODERN.register(1, new AirSpell(SpellType.STRIKE, 1, 5.5, Sounds.WINDSTRIKE_CAST_AND_FIRE_220, STRIKE_START, STRIKE_PROJECTILE, STRIKE_END, Runes.MIND_RUNE.getItem(1), Runes.AIR_RUNE.getItem(1)));
        SpellBook.MODERN.register(10, new AirSpell(SpellType.BOLT, 17, 13.5, Sounds.WINDBOLT_CAST_AND_FIRE_218, BOLT_START, BOLT_PROJECTILE, BOLT_END, Runes.CHAOS_RUNE.getItem(1), Runes.AIR_RUNE.getItem(2)));
        SpellBook.MODERN.register(24, new AirSpell(SpellType.BLAST, 41, 25.5, Sounds.WINDBLAST_CAST_AND_FIRE_216, BLAST_START, BLAST_PROJECTILE, BLAST_END, Runes.DEATH_RUNE.getItem(1), Runes.AIR_RUNE.getItem(3)));
        SpellBook.MODERN.register(45, new AirSpell(SpellType.WAVE, 62, 36.0, Sounds.WINDWAVE_CAST_AND_FIRE_222, WAVE_START, WAVE_PROJECTILE, WAVE_END, Runes.BLOOD_RUNE.getItem(1), Runes.AIR_RUNE.getItem(5)));
        return this;
    }
}
