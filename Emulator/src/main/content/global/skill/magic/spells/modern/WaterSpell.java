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
import org.rs.consts.Animations;
import org.rs.consts.Sounds;

/**
 * The type Water spell.
 */
@Initializable
public final class WaterSpell extends CombatSpell {
    private static final Graphics STRIKE_START = new Graphics(org.rs.consts.Graphics.WATER_STRIKE_CAST_93, 96);
    private static final Projectile STRIKE_PROJECTILE = Projectile.create((Entity) null, null, org.rs.consts.Graphics.WATER_STRIKE_PROJECTILE_94, 40, 36, 52, 75, 15, 11);
    private static final Graphics STRIKE_END = new Graphics(org.rs.consts.Graphics.WATER_STRIKE_IMPACT_95, 96);
    private static final Graphics BOLT_START = new Graphics(org.rs.consts.Graphics.WATER_BOLT_CAST_120, 96);
    private static final Projectile BOLT_PROJECTILE = Projectile.create((Entity) null, null, org.rs.consts.Graphics.WATER_BOLT_PROJECTILE_121, 40, 36, 52, 75, 15, 11);
    private static final Graphics BOLT_END = new Graphics(org.rs.consts.Graphics.WATER_BOLT_IMPACT_122, 96);
    private static final Graphics BLAST_START = new Graphics(org.rs.consts.Graphics.WATER_BLAST_CAST_135, 96);
    private static final Projectile BLAST_PROJECTILE = Projectile.create((Entity) null, null, org.rs.consts.Graphics.WATER_BLAST_PROJECTILE_136, 40, 36, 52, 75, 15, 11);
    private static final Graphics BLAST_END = new Graphics(org.rs.consts.Graphics.WATER_BLAST_IMPACT_137, 96);
    private static final Graphics WAVE_START = new Graphics(org.rs.consts.Graphics.WATER_WAVE_CAST_161, 96);
    private static final Projectile WAVE_PROJECTILE = Projectile.create((Entity) null, null, org.rs.consts.Graphics.WATER_WAVE_PROJECTILE_162, 40, 36, 52, 75, 15, 11);
    private static final Graphics WAVE_END = new Graphics(org.rs.consts.Graphics.WATER_WAVE_IMPACT_163, 96);
    private static final Animation ANIMATION = new Animation(Animations.CAST_SPELL_711, Priority.HIGH);

    /**
     * Instantiates a new Water spell.
     */
    public WaterSpell() {

    }

    private WaterSpell(SpellType type, int level, double baseExperience, int sound, Graphics start, Projectile projectile, Graphics end, Item... runes) {
        super(type, SpellBook.MODERN, level, baseExperience, sound, sound + 1, ANIMATION, start, projectile, end, runes);
    }

    @Override
    public int getMaximumImpact(Entity entity, Entity victim, BattleState state) {
        return getType().getImpactAmount(entity, victim, 2);
    }

    @Override
    public Plugin<SpellType> newInstance(SpellType type) throws Throwable {
        SpellBook.MODERN.register(4, new WaterSpell(SpellType.STRIKE, 5, 7.5, Sounds.WATERSTRIKE_CAST_AND_FIRE_211, STRIKE_START, STRIKE_PROJECTILE, STRIKE_END, Runes.MIND_RUNE.getItem(1), Runes.WATER_RUNE.getItem(1), Runes.AIR_RUNE.getItem(1)));
        SpellBook.MODERN.register(14, new WaterSpell(SpellType.BOLT, 23, 16.5, Sounds.WATERBOLT_CAST_AND_FIRE_209, BOLT_START, BOLT_PROJECTILE, BOLT_END, Runes.CHAOS_RUNE.getItem(1), Runes.WATER_RUNE.getItem(2), Runes.AIR_RUNE.getItem(2)));
        SpellBook.MODERN.register(27, new WaterSpell(SpellType.BLAST, 47, 28.5, Sounds.WATERBLAST_CAST_AND_FIRE_207, BLAST_START, BLAST_PROJECTILE, BLAST_END, Runes.DEATH_RUNE.getItem(1), Runes.WATER_RUNE.getItem(3), Runes.AIR_RUNE.getItem(3)));
        SpellBook.MODERN.register(48, new WaterSpell(SpellType.WAVE, 65, 37.5, Sounds.WATERWAVE_CAST_AND_FIRE_213, WAVE_START, WAVE_PROJECTILE, WAVE_END, Runes.BLOOD_RUNE.getItem(1), Runes.WATER_RUNE.getItem(7), Runes.AIR_RUNE.getItem(5)));
        return this;
    }

}
