package content.global.skill.magic.spells.modern;

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
import core.game.node.item.Item;
import core.game.world.GameWorld;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;
import org.rs.consts.Sounds;

/**
 * The type Bind spell.
 */
@Initializable
public final class BindSpell extends CombatSpell {

    private static final Graphics BIND_START = new Graphics(177, 96);
    private static final Projectile BIND_PROJECTILE = Projectile.create((Entity) null, null, 178, 40, 36, 52, 75, 15, 11);
    private static final Graphics BIND_END = new Graphics(181, 96);
    private static final Graphics SNARE_START = new Graphics(177, 96);
    private static final Projectile SNARE_PROJECTILE = Projectile.create((Entity) null, null, 178, 40, 36, 52, 75, 15, 11);
    private static final Graphics SNARE_END = new Graphics(180, 96);
    private static final Graphics ENTANGLE_START = new Graphics(177, 96);
    private static final Projectile ENTANGLE_PROJECTILE = Projectile.create((Entity) null, null, 178, 40, 36, 52, 75, 15, 11);
    private static final Graphics ENTANGLE_END = new Graphics(179, 96);
    private static final Animation ANIMATION = new Animation(710, Priority.HIGH);

    /**
     * Instantiates a new Bind spell.
     */
    public BindSpell() {

    }

    private BindSpell(SpellType type, int level, double baseExperience, int sound, int impactAudio, Graphics start, Projectile projectile, Graphics end, Item... runes) {
        super(type, SpellBook.MODERN, level, baseExperience, sound, impactAudio, ANIMATION, start, projectile, end, runes);
    }

    @Override
    public void fireEffect(Entity entity, Entity victim, BattleState state) {
        if (victim instanceof NPC) {
            NPC npc = (NPC) victim;
            if (npc.getName().contains("impling")) {
                state.setEstimatedHit(-2);
            }
        }
        if (state.getEstimatedHit() == -1) {
            return;
        }
        int tick = 9;
        if (getType() == SpellType.BIND) {
            state.setEstimatedHit(-2);
        }
        if (state.getSpell().spellId == 30) {
            tick = 17;
        } else if (state.getSpell().spellId == 56) {
            tick = 25;
        }
        if (!victim.getLocks().isMovementLocked() && victim instanceof Player) {
            ((Player) victim).getPacketDispatch().sendMessage("A magical force stops you from moving!");
        }
        victim.getWalkingQueue().reset();
        victim.getLocks().lockMovement(tick);
        entity.setAttribute("entangleDelay", GameWorld.getTicks() + tick + 2);
    }

    @Override
    public int getMaximumImpact(Entity entity, Entity victim, BattleState state) {
        return getType() == SpellType.ENTANGLE ? 5 : 3;
    }

    @Override
    public Plugin<SpellType> newInstance(SpellType type) throws Throwable {

        SpellBook.MODERN.register(12, new BindSpell(SpellType.BIND, 20, 30.0, Sounds.BIND_CAST_101, Sounds.BIND_IMPACT_99, BIND_START, BIND_PROJECTILE, BIND_END, Runes.NATURE_RUNE.getItem(2), Runes.EARTH_RUNE.getItem(3), Runes.WATER_RUNE.getItem(3)));
        SpellBook.MODERN.register(30, new BindSpell(SpellType.SNARE, 50, 60.0, Sounds.SNARE_CAST_AND_FIRE_3003, Sounds.SNARE_IMPACT_3002, SNARE_START, SNARE_PROJECTILE, SNARE_END, Runes.NATURE_RUNE.getItem(3), Runes.EARTH_RUNE.getItem(4), Runes.WATER_RUNE.getItem(4)));
        SpellBook.MODERN.register(56, new BindSpell(SpellType.ENTANGLE, 79, 89.0, Sounds.ENTANGLE_CAST_AND_FIRE_151, Sounds.ENTANGLE_HIT_153, ENTANGLE_START, ENTANGLE_PROJECTILE, ENTANGLE_END, Runes.NATURE_RUNE.getItem(4), Runes.EARTH_RUNE.getItem(5), Runes.WATER_RUNE.getItem(5)));
        return this;
    }

}
