package core.game.node.entity.combat.spell;

import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.InteractionType;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.SpellBookManager;
import core.game.node.entity.player.link.audio.Audio;
import core.game.node.item.Item;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import org.rs.consts.Sounds;

import java.util.ArrayList;
import java.util.List;

import static core.api.ContentAPIKt.playGlobalAudio;

/**
 * The type Combat spell.
 */
public abstract class CombatSpell extends MagicSpell {

    /**
     * The constant AUTOCAST_ANIMATION.
     */
    public static final Animation AUTOCAST_ANIMATION = new Animation(1162, Priority.HIGH);

    /**
     * The constant SPLASH_GRAPHICS.
     */
    public static final Graphics SPLASH_GRAPHICS = new Graphics(85, 96);

    /**
     * The Type.
     */
    protected final SpellType type;

    /**
     * The Projectile.
     */
    protected Projectile projectile;

    /**
     * The End graphics.
     */
    protected final Graphics endGraphics;

    /**
     * The Impact audio.
     */
    protected final int impactAudio;

    /**
     * Instantiates a new Combat spell.
     */
    public CombatSpell() {
        this(SpellType.NULL, SpellBookManager.SpellBook.MODERN, 1, 0.0, -1, -1, null, null, null, null);
    }

    /**
     * Instantiates a new Combat spell.
     *
     * @param type           the type
     * @param book           the book
     * @param level          the level
     * @param baseExperience the base experience
     * @param castAudio      the cast audio
     * @param impactAudio    the impact audio
     * @param animation      the animation
     * @param startGraphics  the start graphics
     * @param projectile     the projectile
     * @param endGraphics    the end graphics
     * @param runes          the runes
     */
    public CombatSpell(SpellType type, SpellBookManager.SpellBook book, int level, double baseExperience, int castAudio, int impactAudio, Animation animation, Graphics startGraphics, Projectile projectile, Graphics endGraphics, Item... runes) {
        super(book, level, baseExperience, animation, startGraphics, new Audio(castAudio, 1, 0), runes);
        this.type = type;
        this.impactAudio = impactAudio;
        this.projectile = projectile;
        this.endGraphics = endGraphics;
    }

    /**
     * Gets maximum impact.
     *
     * @param entity the entity
     * @param victim the victim
     * @param state  the state
     * @return the maximum impact
     */
    public abstract int getMaximumImpact(Entity entity, Entity victim, BattleState state);

    /**
     * Fire effect.
     *
     * @param entity the entity
     * @param victim the victim
     * @param state  the state
     */
    public void fireEffect(Entity entity, Entity victim, BattleState state) {

    }

    /**
     * Gets multihit targets.
     *
     * @param entity the entity
     * @param target the target
     * @param max    the max
     * @return the multihit targets
     */
    public List<Entity> getMultihitTargets(Entity entity, Entity target, int max) {
        List<Entity> list = new ArrayList<>(20);
        list.add(target);
        boolean npc = target instanceof NPC;
        for (Entity e : npc ? RegionManager.getSurroundingNPCs(target) : RegionManager.getSurroundingPlayers(target)) {
            if (e != target && e != entity && CombatStyle.MAGIC.getSwingHandler().canSwing(entity, e) != InteractionType.NO_INTERACT) {
                list.add(e);
            }
            if (--max < 1) {
                break;
            }
        }
        return list;
    }

    /**
     * Visualize impact.
     *
     * @param entity the entity
     * @param target the target
     * @param state  the state
     */
    public void visualizeImpact(Entity entity, Entity target, BattleState state) {
        if (state.getEstimatedHit() == -1) {
            playGlobalAudio(target.getLocation(), Sounds.SPELLFAIL_227, 20);
            target.graphics(SPLASH_GRAPHICS);
            return;
        }
        target.graphics(endGraphics);
        playGlobalAudio(target.getLocation(), impactAudio, 20);
    }

    @Override
    public void visualize(Entity entity, Node target) {
        entity.graphics(graphics);
        if (projectile != null) {
            if (target instanceof Entity) {
                projectile.transform(entity, (Entity) target, entity instanceof NPC, 58, 10).send();
            } else {
                projectile.transform(entity, target.getLocation(), entity instanceof NPC, 58, 10).send();
            }
        }
        if (entity.getProperties().getAutocastSpell() == this && (entity instanceof Player || animation == null)) {
            Player p = entity.asPlayer();
            if (p.getProperties().getAutocastSpell().spellId == 31) {
                entity.animate(new Animation(1576));
            } else {
                entity.animate(AUTOCAST_ANIMATION);
            }
        } else {
            if (entity instanceof NPC) {
                NPC n = entity.asNpc();
                if (n.getProperties().getMagicAnimation() != null) {
                    entity.animate(n.getProperties().getMagicAnimation());
                } else {
                    entity.animate(animation);
                }
            } else {
                entity.animate(animation);
            }
        }
        playGlobalAudio(entity.getLocation(), audio.id, 20);
    }

    @Override
    public boolean cast(Entity entity, Node target) {
        if (!meetsRequirements(entity, true, false)) {
            return false;
        }
        if (target instanceof Entity) {
            entity.face((Entity) target);
        }
        entity.getProperties().setSpell(this);
        if (entity.getProperties().getCombatPulse().isAttacking()) {
            entity.getProperties().getCombatPulse().setVictim(target);
            entity.getProperties().getCombatPulse().updateStyle();
            entity.getProperties().getCombatPulse().start();
            return true;
        }
        entity.getProperties().getCombatPulse().attack(target);
        return true;
    }

    /**
     * Get targets battle state [ ].
     *
     * @param entity the entity
     * @param target the target
     * @return the battle state [ ]
     */
    public BattleState[] getTargets(Entity entity, Entity target) {
        return new BattleState[]{new BattleState(entity, target)};
    }

    /**
     * Gets accuracy mod.
     *
     * @return the accuracy mod
     */
    public double getAccuracyMod() {
        return type.getAccuracyMod();
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public SpellType getType() {
        return type;
    }

    /**
     * Gets animation.
     *
     * @return the animation
     */
    public Animation getAnimation() {
        return animation;
    }

    /**
     * Gets splash graphic.
     *
     * @return the splash graphic
     */
    public Graphics getSplashGraphic() {
        return SPLASH_GRAPHICS;
    }

}
