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
 * The type Fire spell.
 */
@Initializable
public final class FireSpell extends CombatSpell {

    private static final Graphics STRIKE_START = new Graphics(99, 96);
    private static final Projectile STRIKE_PROJECTILE = Projectile.create((Entity) null, null, 100, 40, 36, 52, 75, 15, 11);
    private static final Graphics STRIKE_END = new Graphics(101, 96);
    private static final Graphics BOLT_START = new Graphics(126, 96);
    private static final Projectile BOLT_PROJECTILE = Projectile.create((Entity) null, null, 127, 40, 36, 52, 75, 15, 11);
    private static final Graphics BOLT_END = new Graphics(128, 96);
    private static final Graphics BLAST_START = new Graphics(129, 96);
    private static final Projectile BLAST_PROJECTILE = Projectile.create((Entity) null, null, 130, 40, 36, 52, 75, 15, 11);
    private static final Graphics BLAST_END = new Graphics(131, 96);
    private static final Graphics WAVE_START = new Graphics(155, 96);
    private static final Projectile WAVE_PROJECTILE = Projectile.create((Entity) null, null, 156, 40, 36, 52, 75, 15, 11);
    private static final Graphics WAVE_END = new Graphics(157, 96);
    private static final Animation ANIMATION = new Animation(711, Priority.HIGH);

    /**
     * Instantiates a new Fire spell.
     */
    public FireSpell() {

    }

    private FireSpell(SpellType type, int level, double baseExperience, int sound, Graphics start, Projectile projectile, Graphics end, Item... runes) {
        super(type, SpellBook.MODERN, level, baseExperience, sound, sound + 1, ANIMATION, start, projectile, end, runes);
    }

    @Override
    public int getMaximumImpact(Entity entity, Entity victim, BattleState state) {
        return getType().getImpactAmount(entity, victim, 4);
    }

    @Override
    public Plugin<SpellType> newInstance(SpellType type) throws Throwable {
        SpellBook.MODERN.register(8, new FireSpell(SpellType.STRIKE, 13, 11.5, Sounds.FIRESTRIKE_CAST_AND_FIRE_160, STRIKE_START, STRIKE_PROJECTILE, STRIKE_END, Runes.MIND_RUNE.getItem(1), Runes.FIRE_RUNE.getItem(3), Runes.AIR_RUNE.getItem(2)));
        SpellBook.MODERN.register(20, new FireSpell(SpellType.BOLT, 35, 22.5, Sounds.FIREBOLT_CAST_AND_FIRE_157, BOLT_START, BOLT_PROJECTILE, BOLT_END, Runes.CHAOS_RUNE.getItem(1), Runes.FIRE_RUNE.getItem(4), Runes.AIR_RUNE.getItem(3)));
        SpellBook.MODERN.register(38, new FireSpell(SpellType.BLAST, 59, 34.5, Sounds.FIREBLAST_CAST_AND_FIRE_155, BLAST_START, BLAST_PROJECTILE, BLAST_END, Runes.DEATH_RUNE.getItem(1), Runes.FIRE_RUNE.getItem(5), Runes.AIR_RUNE.getItem(4)));
        SpellBook.MODERN.register(55, new FireSpell(SpellType.WAVE, 75, 42.5, Sounds.FIREWAVE_CAST_AND_FIRE_162, WAVE_START, WAVE_PROJECTILE, WAVE_END, Runes.BLOOD_RUNE.getItem(1), Runes.FIRE_RUNE.getItem(7), Runes.AIR_RUNE.getItem(5)));
        return this;
    }

}
