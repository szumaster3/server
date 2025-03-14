package content.global.skill.magic.spells.modern;

import core.cache.def.impl.ItemDefinition;
import core.game.container.impl.EquipmentContainer;
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
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;
import org.rs.consts.Items;
import org.rs.consts.Sounds;

import static core.api.ContentAPIKt.playGlobalAudio;

/**
 * The type God spell.
 */
@Initializable
public final class GodSpell extends CombatSpell {

    private static final String[] NAMES = new String[]{"Saradomin strike", "Guthix claws", "Flames of Zamorak"};
    private static final int[] GOD_STAVES = new int[]{Items.SARADOMIN_STAFF_2415, Items.GUTHIX_STAFF_2416, Items.ZAMORAK_STAFF_2417};
    private static final Graphics SARA_START = null;
    private static final Projectile SARA_PROJECTILE = null;
    private static final Graphics SARA_END = new Graphics(76, 0);
    private static final Graphics GUTHIX_START = null;
    private static final Projectile GUTHIX_PROJECTILE = null;
    private static final Graphics GUTHIX_END = new Graphics(77, 0);
    private static final Graphics ZAM_START = null;
    private static final Projectile ZAM_PROJECTILE = null;
    private static final Graphics ZAM_END = new Graphics(78, 0);
    private static final Animation ANIMATION = new Animation(811, Priority.HIGH);

    /**
     * Instantiates a new God spell.
     */
    public GodSpell() {

    }

    private GodSpell(SpellType type, int sound, int impactAudio, Graphics start, Projectile projectile, Graphics end, Item... runes) {
        super(type, SpellBook.MODERN, 60, 35.0, sound, impactAudio, ANIMATION, start, projectile, end, runes);
    }

    private int getSpellIndex() {
        int index = -1;
        switch (getCastRunes()[1].getAmount()) {
            case 2:
                index = 0;
                break;
            case 1:
                index = 1;
                break;
            case 4:
                index = 2;
                break;
        }
        return index;
    }

    @Override
    public boolean meetsRequirements(Entity caster, boolean message, boolean remove) {
        if (caster instanceof NPC) {
            return true;
        }
        if (caster instanceof Player) {
            int staffId = ((Player) caster).getEquipment().getNew(EquipmentContainer.SLOT_WEAPON).getId();
            int index = getSpellIndex();
            if (index < 0) {
                return false;
            }
            int required = GOD_STAVES[index];
            Player p = (Player) caster;
            if (p.getSavedData().activityData.getGodCasts()[index] < 100 && !p.getZoneMonitor().isInZone("mage arena")) {
                p.sendMessage("You need to cast " + NAMES[index] + " " + (100 - p.getSavedData().activityData.getGodCasts()[index]) + " more times inside the Mage Arena.");
                return false;
            }

            if (staffId != required && !(index == 1 && staffId == Items.VOID_KNIGHT_MACE_8841)) {
                if (message) {
                    ((Player) caster).getPacketDispatch().sendMessage("You need to wear a " + ItemDefinition.forId(required).getName() + "  to cast this spell.");
                }
                return false;
            }
        }
        return super.meetsRequirements(caster, message, remove);
    }

    @Override
    public void fireEffect(Entity entity, Entity victim, BattleState state) {
        switch (getSpellIndex()) {
            case 0:
                victim.getSkills().decrementPrayerPoints(1);
                break;
            case 1:
                victim.getSkills().drainLevel(Skills.DEFENCE, 0.05, 0.05);
                break;
            case 2:
                victim.getSkills().drainLevel(Skills.MAGIC, 0.05, 0.05);
                break;
        }
    }

    @Override
    public void visualize(Entity entity, Node target) {
        super.visualize(entity, target);
        if (entity instanceof NPC) {
            NPC n = (NPC) entity;
            if (n.getId() > 911 && n.getId() < 915 || (n.getId() > 906 && n.getId() < 912)) {
                n.getAnimator().forceAnimation(n.getProperties().getAttackAnimation());
            }
        }
    }

    @Override
    public void visualizeImpact(Entity entity, Entity target, BattleState state) {
        if (entity instanceof Player) {
            int index = getSpellIndex();
            Player p = (Player) entity;
            if (p.getSavedData().activityData.getGodCasts()[index] < 100) {
                p.getSavedData().activityData.getGodCasts()[index]++;
                if (p.getSavedData().activityData.getGodCasts()[index] >= 100) {
                    p.sendMessage("You can now cast " + NAMES[index] + " outside the Arena.");
                }
            }
            if (state.getEstimatedHit() == -1) {
                target.graphics(SPLASH_GRAPHICS);
                if (projectile == SARA_PROJECTILE) {
                    playGlobalAudio(target.getLocation(), Sounds.SARADOMIN_STRIKE_FAIL_1656, 20);
                }
                if (projectile == GUTHIX_PROJECTILE) {
                    playGlobalAudio(target.getLocation(), Sounds.CLAWS_OF_GUTHIX_FAIL_1652, 20);
                }
                if (projectile == ZAM_PROJECTILE) {
                    playGlobalAudio(target.getLocation(), Sounds.FLAMES_OF_ZAMORAK_FAIL_1654, 20);
                }
                return;
            }
        }
        target.graphics(endGraphics);
        playGlobalAudio(target.getLocation(), impactAudio);
    }

    @Override
    public int getMaximumImpact(Entity entity, Entity victim, BattleState state) {
        return getType().getImpactAmount(entity, victim, 0);
    }

    @Override
    public Plugin<SpellType> newInstance(SpellType type) throws Throwable {
        SpellBook.MODERN.register(41, new GodSpell(SpellType.GOD_STRIKE, -1, Sounds.SARADOMIN_STRIKE_1659, SARA_START, SARA_PROJECTILE, SARA_END, Runes.BLOOD_RUNE.getItem(2), Runes.FIRE_RUNE.getItem(2), Runes.AIR_RUNE.getItem(4)));
        SpellBook.MODERN.register(42, new GodSpell(SpellType.GOD_STRIKE, -1, Sounds.CLAWS_OF_GUTHIX_1653, GUTHIX_START, GUTHIX_PROJECTILE, GUTHIX_END, Runes.BLOOD_RUNE.getItem(2), Runes.FIRE_RUNE.getItem(1), Runes.AIR_RUNE.getItem(4)));
        SpellBook.MODERN.register(43, new GodSpell(SpellType.GOD_STRIKE, -1, Sounds.FLAMES_OF_ZAMORAK_1655, ZAM_START, ZAM_PROJECTILE, ZAM_END, Runes.BLOOD_RUNE.getItem(2), Runes.FIRE_RUNE.getItem(4), Runes.AIR_RUNE.getItem(1)));
        return this;
    }

}
