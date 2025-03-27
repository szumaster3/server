package content.global.skill.magic.spells.modern;

import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.spell.CombatSpell;
import core.game.node.entity.combat.spell.Runes;
import core.game.node.entity.combat.spell.SpellType;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.SpellBookManager.SpellBook;
import core.game.node.entity.player.link.prayer.PrayerType;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;
import org.rs.consts.Sounds;

import static core.api.ContentAPIKt.*;

/**
 * The Teleblock spell.
 */
@Initializable
public final class TeleblockSpell extends CombatSpell {

    private static final Projectile TELEBLOCK_ORB = Projectile.create((Entity) null, null, 1842, 40, 36, 52, 75, 15, 11);

    private static final Graphics TELEBLOCK_SUCCESS = new Graphics(org.rs.consts.Graphics.CORP_STOMP_1834, 0);

    private static final Graphics TELEBLOCK_START = new Graphics(1841, 0);

    /**
     * Instantiates a new Teleblock spell.
     */
    public TeleblockSpell() {

    }

    /**
     * Instantiates a new Teleblock spell.
     *
     * @param type           the type
     * @param level          the level
     * @param baseExperience the base experience
     * @param impactSound    the impact sound
     * @param anim           the anim
     * @param start          the start
     * @param projectile     the projectile
     * @param end            the end
     * @param runes          the runes
     */
    public TeleblockSpell(SpellType type, int level, double baseExperience, int impactSound, Animation anim, Graphics start, Projectile projectile, Graphics end, Item... runes) {
        super(type, SpellBook.MODERN, level, baseExperience, Sounds.TPBLOCK_CAST_202, Sounds.TPBLOCK_IMPACT_203, anim, TELEBLOCK_START, TELEBLOCK_ORB, TELEBLOCK_SUCCESS, runes);
    }

    @Override
    public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
        SpellBook.MODERN.register(63, new TeleblockSpell(SpellType.TELEBLOCK, 85, 80, Sounds.TPBLOCK_IMPACT_203, new Animation(10503, Priority.HIGH), TELEBLOCK_START, TELEBLOCK_ORB, TELEBLOCK_SUCCESS, Runes.LAW_RUNE.getItem(1), Runes.DEATH_RUNE.getItem(1), Runes.CHAOS_RUNE.getItem(1)));
        return this;
    }

    @Override
    public void visualize(Entity entity, Node target) {
        entity.graphics(graphics);
        if (projectile != null) {
            projectile.transform(entity, (Entity) target, false, 58, 10).send();
        }
        entity.animate(animation);
        playGlobalAudio(entity.getLocation(), audio.id, 1, 20);
    }

    @Override
    public boolean cast(Entity entity, Node target) {
        if (!(target instanceof Player)) {
            entity.asPlayer().sendMessage("You can only cast this spell on another player.");
            return false;
        }
        if (!entity.getZoneMonitor().isInZone("Wilderness") || !((Player) target).getZoneMonitor().isInZone("Wilderness")) {
            entity.asPlayer().sendMessage("You and your opponent must both be in the wilderness for you to use this spell.");
            return false;
        }
        if (hasTimerActive(target.asPlayer(), "teleblock")) {
            entity.asPlayer().sendMessage("That player is already affected by this spell.");
            return false;
        }
        if (!meetsRequirements(entity, true, false)) {
            return false;
        }
        return super.cast(entity, target);
    }

    @Override
    public void visualizeImpact(Entity entity, Entity target, BattleState state) {
        super.visualizeImpact(entity, target, state);
    }

    @Override
    public int getMaximumImpact(Entity entity, Entity victim, BattleState state) {
        return 0;
    }

    @Override
    public void fireEffect(Entity entity, Entity victim, BattleState state) {
        if (!hasTimerActive(victim, "teleblock") && victim instanceof Player && state.getStyle().getSwingHandler().isAccurateImpact(entity, victim)) {
            int ticks = 500;
            if (((Player) victim).getPrayer().get(PrayerType.PROTECT_FROM_MAGIC)) {
                ticks /= 2;
            }
            registerTimer(victim, spawnTimer("teleblock", ticks));
        } else if (hasTimerActive(victim, "teleblock")) {
            entity.asPlayer().sendMessage("Your target is already blocked from teleporting.");
        }
    }
}
