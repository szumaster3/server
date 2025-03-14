package content.global.skill.magic.spells.ancient;

import core.game.container.impl.EquipmentContainer;
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
import core.game.node.item.Item;
import core.game.world.GameWorld;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;
import org.rs.consts.Items;

import java.util.List;

import static core.api.ContentAPIKt.*;

/**
 * The type Miasmic spells.
 */
@Initializable
public final class MiasmicSpells extends CombatSpell {
    private static final Graphics RUSH_START = new Graphics(org.rs.consts.Graphics.MIASMIC_SPELL_1845, 0, 15);
    private static final Graphics RUSH_END = new Graphics(org.rs.consts.Graphics.MIASMIC_SPELL_1847, 40);
    private static final Graphics BURST_START = new Graphics(org.rs.consts.Graphics.RED_CLOUD_1848, 0);
    private static final Graphics BURST_END = new Graphics(org.rs.consts.Graphics.RED_CLOUD_1849, 20, 30);
    private static final Graphics BLITZ_START = new Graphics(1850, 15);
    private static final Graphics BLITZ_END = new Graphics(1851, 0);
    private static final Graphics BARRAGE_START = new Graphics(1853, 0);
    private static final Graphics BARRAGE_END = new Graphics(1854, 0, 30);
    private static final int[] VALID_STAFF_IDS = {Items.ZURIELS_STAFF_13867, Items.ZURIELS_STAFF_DEG_13869, Items.NULL_13841, Items.NULL_13843};

    /**
     * Instantiates a new Miasmic spells.
     */
    public MiasmicSpells() {
    }

    private MiasmicSpells(SpellType type, int level, double baseExperience, int castAudio, int impactSound, Animation anim, Graphics start, Projectile projectile, Graphics end, Item... runes) {
        super(type, SpellBook.ANCIENT, level, baseExperience, castAudio, impactSound, anim, start, projectile, end, runes);
    }

    @Override
    public void visualize(Entity entity, Node target) {
        entity.graphics(graphics);
        if (projectile != null) {
            projectile.transform(entity, (Entity) target, false, 58, 10).send();
        }
        entity.animate(animation);
        playGlobalAudio(entity.getLocation(), audio.id, 20);
    }

    @Override
    public void visualizeImpact(Entity entity, Entity target, BattleState state) {
        super.visualizeImpact(entity, target, state);
    }

    @Override
    public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
        SpellBook.ANCIENT.register(16, new MiasmicSpells(SpellType.RUSH, 61, 36.0, 5368, 5365, new Animation(10513, Priority.HIGH), RUSH_START, null, RUSH_END, Runes.EARTH_RUNE.getItem(1), Runes.SOUL_RUNE.getItem(1), Runes.CHAOS_RUNE.getItem(2)));
        SpellBook.ANCIENT.register(17, new MiasmicSpells(SpellType.BURST, 73, 42.0, 5366, 5372, new Animation(10516, Priority.HIGH), BURST_START, null, BURST_END, Runes.EARTH_RUNE.getItem(3), Runes.SOUL_RUNE.getItem(3), Runes.CHAOS_RUNE.getItem(4)));
        SpellBook.ANCIENT.register(18, new MiasmicSpells(SpellType.BLITZ, 85, 48.0, 5370, 5367, new Animation(10524, Priority.HIGH), BLITZ_START, null, BLITZ_END, Runes.EARTH_RUNE.getItem(3), Runes.SOUL_RUNE.getItem(3), Runes.BLOOD_RUNE.getItem(2)));
        SpellBook.ANCIENT.register(19, new MiasmicSpells(SpellType.BARRAGE, 97, 54.0, 5371, 5369, new Animation(10518, Priority.HIGH), BARRAGE_START, null, BARRAGE_END, Runes.EARTH_RUNE.getItem(4), Runes.SOUL_RUNE.getItem(4), Runes.BLOOD_RUNE.getItem(4)));
        return this;
    }

    @Override
    public void fireEffect(Entity entity, Entity victim, BattleState state) {
        if (!hasTimerActive(victim, "miasmic:immunity")) {
            registerTimer(victim, spawnTimer("miasmic", (spellId - 15) * 20));
        }
    }

    /**
     * Valid staff equipped boolean.
     *
     * @param entity the entity
     * @return the boolean
     */
    public boolean validStaffEquipped(Entity entity) {
        for (int validStaffId : VALID_STAFF_IDS) {
            if (((Player) entity).getEquipment().getNew(EquipmentContainer.SLOT_WEAPON).getId() == validStaffId) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean cast(Entity entity, Node target) {
        if (!validStaffEquipped(entity) && !GameWorld.getSettings().isDevMode()) {
            ((Player) entity).getPacketDispatch().sendMessage("You need to be wielding Zuriel's staff in order to cast this spell.");
            return false;
        }
        if (!meetsRequirements(entity, true, false)) {
            return false;
        }
        return super.cast(entity, target);
    }

    @Override
    public BattleState[] getTargets(Entity entity, Entity target) {
        if (animation.getId() == 10513 || animation.getId() == 10524
                || !entity.getProperties().isMultiZone() || !target.getProperties().isMultiZone()) {
            return super.getTargets(entity, target);
        }
        List<Entity> list = getMultihitTargets(entity, target, 9);
        BattleState[] targets = new BattleState[list.size()];
        int index = 0;
        for (Entity e : list) {
            targets[index++] = new BattleState(entity, e);
        }
        return targets;
    }

    @Override
    public int getMaximumImpact(Entity entity, Entity victim, BattleState state) {
        int add = 9;
        if (animation.getId() == 10524 || animation.getId() == 10516) {
            add = 6;
        }
        if (animation.getId() == 10513) {
            add = 4;
        }
        return getType().getImpactAmount(entity, victim, add);
    }

}
