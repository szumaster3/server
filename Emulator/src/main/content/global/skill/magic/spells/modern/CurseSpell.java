package content.global.skill.magic.spells.modern;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.spell.CombatSpell;
import core.game.node.entity.combat.spell.Runes;
import core.game.node.entity.combat.spell.SpellType;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.player.link.SpellBookManager.SpellBook;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;
import org.rs.consts.Animations;
import org.rs.consts.Sounds;

/**
 * The type Curse spell.
 */
@Initializable
public final class CurseSpell extends CombatSpell {

    private static final Graphics CONFUSE_START = new Graphics(org.rs.consts.Graphics.CONFUSE_CAST_102, 96);
    private static final Projectile CONFUSE_PROJECTILE = Projectile.create((Entity) null, null, org.rs.consts.Graphics.CONFUSE_PROJECTILE_103, 40, 36, 52, 75, 15, 11);
    private static final Graphics CONFUSE_END = new Graphics(org.rs.consts.Graphics.CONFUSE_IMPACT_104, 96);
    private static final Graphics WEAKEN_START = new Graphics(org.rs.consts.Graphics.WEAKEN_CAST_105, 96);
    private static final Projectile WEAKEN_PROJECTILE = Projectile.create((Entity) null, null, org.rs.consts.Graphics.WEAKEN_PROJECTILE_106, 40, 36, 52, 75, 15, 11);
    private static final Graphics WEAKEN_END = new Graphics(org.rs.consts.Graphics.WEAKEN_IMPACT_107, 96);
    private static final Graphics CURSE_START = new Graphics(org.rs.consts.Graphics.CURSE_CAST_108, 96);
    private static final Projectile CURSE_PROJECTILE = Projectile.create((Entity) null, null, org.rs.consts.Graphics.CURSE_PROJECTILE_109, 40, 36, 52, 75, 15, 11);
    private static final Graphics CURSE_END = new Graphics(org.rs.consts.Graphics.CURSE_IMPACT_110, 96);
    private static final Graphics VULNER_START = new Graphics(org.rs.consts.Graphics.VULNERABILITY_CAST_167, 96);
    private static final Projectile VULNER_PROJECTILE = Projectile.create((Entity) null, null, org.rs.consts.Graphics.VULNERABILITY_PROJECTILE_168, 40, 36, 52, 75, 15, 11);
    private static final Graphics VULNER_END = new Graphics(org.rs.consts.Graphics.VULNERABILITY_IMPACT_169, 96, 1);
    private static final Graphics ENFEEBLE_START = new Graphics(org.rs.consts.Graphics.ENFEEBLE_CAST_170, 96);
    private static final Projectile ENFEEBLE_PROJECTILE = Projectile.create((Entity) null, null, org.rs.consts.Graphics.ENFEEBLE_PROJECTILE_171, 40, 36, 52, 75, 15, 11);
    private static final Graphics ENFEEBLE_END = new Graphics(org.rs.consts.Graphics.ENFEEBLE_IMPACT_172, 96);
    private static final Graphics STUN_START = new Graphics(org.rs.consts.Graphics.STUN_CAST_173, 96);
    private static final Projectile STUN_PROJECTILE = Projectile.create((Entity) null, null, org.rs.consts.Graphics.STUN_PROJECTILE_174, 40, 36, 52, 75, 15, 11);
    private static final Graphics STUN_END = new Graphics(org.rs.consts.Graphics.WEAKEN_IMPACT_107, 96);
    private static final Animation LOW_ANIMATION = new Animation(716, Priority.HIGH);
    private static final Animation HIGH_ANIMATION = new Animation(Animations.CAST_SPELL_B_729, Priority.HIGH);

    /**
     * Instantiates a new Curse spell.
     */
    public CurseSpell() {

    }

    private CurseSpell(SpellType type, int level, double baseExperience, int sound, int impactAudio, Graphics start, Projectile projectile, Graphics end, Item... runes) {
        super(type, SpellBook.MODERN, level, baseExperience, sound, impactAudio, type.ordinal() <= SpellType.CURSE.ordinal() ? LOW_ANIMATION : HIGH_ANIMATION, start, projectile, end, runes);
    }

    @Override
    public int getMaximumImpact(Entity entity, Entity victim, BattleState state) {
        return 1;
    }

    @Override
    public void fireEffect(Entity entity, Entity victim, BattleState state) {
        if (state.getEstimatedHit() == -1) {
            return;
        }
        state.setEstimatedHit(-2);
        switch (getType()) {
            case CONFUSE:
                victim.getSkills().drainLevel(Skills.ATTACK, 0.05, 0.05);
                break;
            case WEAKEN:
                victim.getSkills().drainLevel(Skills.STRENGTH, 0.05, 0.05);
                break;
            case CURSE:
                victim.getSkills().drainLevel(Skills.DEFENCE, 0.05, 0.05);
                break;
            case VULNERABILITY:
                victim.getSkills().drainLevel(Skills.DEFENCE, 0.10, 0.10);
                break;
            case ENFEEBLE:
                victim.getSkills().drainLevel(Skills.STRENGTH, 0.10, 0.10);
                break;
            case STUN:
                victim.getSkills().drainLevel(Skills.ATTACK, 0.10, 0.10);
                break;
            default:
        }
    }

    @Override
    public void addExperience(Entity entity, int hit) {
        entity.getSkills().addExperience(Skills.MAGIC, experience);
    }

    @Override
    public Plugin<SpellType> newInstance(SpellType type) throws Throwable {
        SpellBook.MODERN.register(2, new CurseSpell(SpellType.CONFUSE, 3, 13.0, Sounds.CONFUSE_CAST_AND_FIRE_119, Sounds.CONFUSE_HIT_121, CONFUSE_START, CONFUSE_PROJECTILE, CONFUSE_END, Runes.BODY_RUNE.getItem(1), Runes.EARTH_RUNE.getItem(2), Runes.WATER_RUNE.getItem(3)));
        SpellBook.MODERN.register(7, new CurseSpell(SpellType.WEAKEN, 11, 21.0, Sounds.WEAKEN_CAST_AND_FIRE_3011, Sounds.WEAKEN_HIT_3010, WEAKEN_START, WEAKEN_PROJECTILE, WEAKEN_END, Runes.BODY_RUNE.getItem(1), Runes.EARTH_RUNE.getItem(2), Runes.WATER_RUNE.getItem(3)));
        SpellBook.MODERN.register(11, new CurseSpell(SpellType.CURSE, 19, 29.0, Sounds.CURSE_CAST_AND_FIRE_127, Sounds.CURSE_HIT_126, CURSE_START, CURSE_PROJECTILE, CURSE_END, Runes.BODY_RUNE.getItem(1), Runes.EARTH_RUNE.getItem(3), Runes.WATER_RUNE.getItem(2)));
        SpellBook.MODERN.register(50, new CurseSpell(SpellType.VULNERABILITY, 66, 76.0, Sounds.VULNERABILITY_CAST_AND_FIRE_3009, Sounds.VULNERABILITY_IMPACT_3008, VULNER_START, VULNER_PROJECTILE, VULNER_END, Runes.SOUL_RUNE.getItem(1), Runes.EARTH_RUNE.getItem(5), Runes.WATER_RUNE.getItem(5)));
        SpellBook.MODERN.register(53, new CurseSpell(SpellType.ENFEEBLE, 73, 83.0, Sounds.ENFEEBLE_CAST_AND_FIRE_148, Sounds.ENFEEBLE_HIT_150, ENFEEBLE_START, ENFEEBLE_PROJECTILE, ENFEEBLE_END, Runes.SOUL_RUNE.getItem(1), Runes.EARTH_RUNE.getItem(8), Runes.WATER_RUNE.getItem(8)));
        SpellBook.MODERN.register(57, new CurseSpell(SpellType.STUN, 80, 90.0, Sounds.STUN_CAST_AND_FIRE_3004, Sounds.STUN_IMPACT_3005, STUN_START, STUN_PROJECTILE, STUN_END, Runes.SOUL_RUNE.getItem(1), Runes.EARTH_RUNE.getItem(12), Runes.WATER_RUNE.getItem(12)));
        return this;
    }

}
