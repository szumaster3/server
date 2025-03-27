package content.global.skill.magic.spells.modern

import core.api.playGlobalAudio
import core.cache.def.impl.ItemDefinition
import core.game.container.impl.EquipmentContainer
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.combat.spell.Runes
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.impl.Animator.Priority
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Sounds

/**
 * The God spell.
 */
@Initializable
class GodSpell : CombatSpell {
    constructor()

    private constructor(
        type: SpellType,
        sound: Int,
        impactAudio: Int,
        start: Graphics?,
        projectile: Projectile?,
        end: Graphics,
        vararg runes: Item,
    ) : super(type, SpellBook.MODERN, 60, 35.0, sound, impactAudio, ANIMATION, start, projectile, end, *runes)

    private val spellIndex: Int
        get() {
            var index = -1
            when (castRunes!![1]!!.amount) {
                2 -> index = 0
                1 -> index = 1
                4 -> index = 2
            }
            return index
        }

    override fun meetsRequirements(
        caster: Entity,
        message: Boolean,
        remove: Boolean,
    ): Boolean {
        if (caster is NPC) {
            return true
        }
        if (caster is Player) {
            val staffId = caster.equipment.getNew(EquipmentContainer.SLOT_WEAPON).id
            val index = spellIndex
            if (index < 0) {
                return false
            }
            val required = GOD_STAVES[index]
            val p = caster
            if (p.getSavedData().activityData.godCasts[index] < 100 && !p.zoneMonitor.isInZone("mage arena")) {
                p.sendMessage(
                    "You need to cast " + NAMES[index] + " " + (100 - p.getSavedData().activityData.godCasts[index]) +
                        " more times inside the Mage Arena.",
                )
                return false
            }

            if (staffId != required && !(index == 1 && staffId == Items.VOID_KNIGHT_MACE_8841)) {
                if (message) {
                    caster.packetDispatch.sendMessage(
                        "You need to wear a " + ItemDefinition.forId(required).name + "  to cast this spell.",
                    )
                }
                return false
            }
        }
        return super.meetsRequirements(caster, message, remove)
    }

    override fun fireEffect(
        entity: Entity,
        victim: Entity,
        state: BattleState,
    ) {
        when (spellIndex) {
            0 -> victim.getSkills().decrementPrayerPoints(1.0)
            1 -> victim.getSkills().drainLevel(Skills.DEFENCE, 0.05, 0.05)
            2 -> victim.getSkills().drainLevel(Skills.MAGIC, 0.05, 0.05)
        }
    }

    override fun visualize(
        entity: Entity,
        target: Node,
    ) {
        super.visualize(entity, target)
        if (entity is NPC) {
            val n = entity
            if (n.id > NPCs.KOLODION_911 &&
                n.id < NPCs.LEELA_915 ||
                (n.id > NPCs.KOLODION_906 && n.id < NPCs.BATTLE_MAGE_912)
            ) {
                n.animator.forceAnimation(n.properties.attackAnimation)
            }
        }
    }

    override fun visualizeImpact(
        entity: Entity,
        target: Entity,
        state: BattleState,
    ) {
        if (entity is Player) {
            val index = spellIndex
            val p = entity
            if (p.getSavedData().activityData.godCasts[index] < 100) {
                p.getSavedData().activityData.godCasts[index]++
                if (p.getSavedData().activityData.godCasts[index] >= 100) {
                    p.sendMessage("You can now cast " + NAMES[index] + " outside the Arena.")
                }
            }
            if (state.estimatedHit == -1) {
                target.graphics(SPLASH_GRAPHICS)
                if (projectile === SARA_PROJECTILE) {
                    playGlobalAudio(target.location, Sounds.SARADOMIN_STRIKE_FAIL_1656, 20)
                }
                if (projectile === GUTHIX_PROJECTILE) {
                    playGlobalAudio(target.location, Sounds.CLAWS_OF_GUTHIX_FAIL_1652, 20)
                }
                if (projectile === ZAM_PROJECTILE) {
                    playGlobalAudio(target.location, Sounds.FLAMES_OF_ZAMORAK_FAIL_1654, 20)
                }
                return
            }
        }
        target.graphics(endGraphics)
        playGlobalAudio(target.location, impactAudio)
    }

    override fun getMaximumImpact(
        entity: Entity,
        victim: Entity,
        state: BattleState,
    ): Int {
        return getType().getImpactAmount(entity, victim, 0)
    }

    @Throws(Throwable::class)
    override fun newInstance(type: SpellType?): Plugin<SpellType?> {
        SpellBook.MODERN.register(
            41,
            GodSpell(
                SpellType.GOD_STRIKE,
                -1,
                Sounds.SARADOMIN_STRIKE_1659,
                SARA_START,
                SARA_PROJECTILE,
                SARA_END,
                Runes.BLOOD_RUNE.getItem(2),
                Runes.FIRE_RUNE.getItem(2),
                Runes.AIR_RUNE.getItem(4),
            ),
        )
        SpellBook.MODERN.register(
            42,
            GodSpell(
                SpellType.GOD_STRIKE,
                -1,
                Sounds.CLAWS_OF_GUTHIX_1653,
                GUTHIX_START,
                GUTHIX_PROJECTILE,
                GUTHIX_END,
                Runes.BLOOD_RUNE.getItem(2),
                Runes.FIRE_RUNE.getItem(1),
                Runes.AIR_RUNE.getItem(4),
            ),
        )
        SpellBook.MODERN.register(
            43,
            GodSpell(
                SpellType.GOD_STRIKE,
                -1,
                Sounds.FLAMES_OF_ZAMORAK_1655,
                ZAM_START,
                ZAM_PROJECTILE,
                ZAM_END,
                Runes.BLOOD_RUNE.getItem(2),
                Runes.FIRE_RUNE.getItem(4),
                Runes.AIR_RUNE.getItem(1),
            ),
        )
        return this
    }

    companion object {
        private val NAMES = arrayOf("Saradomin strike", "Guthix claws", "Flames of Zamorak")
        private val GOD_STAVES =
            intArrayOf(Items.SARADOMIN_STAFF_2415, Items.GUTHIX_STAFF_2416, Items.ZAMORAK_STAFF_2417)
        private val SARA_START: Graphics? = null
        private val SARA_PROJECTILE: Projectile? = null
        private val SARA_END = Graphics(org.rs.consts.Graphics.SARADOMIN_STRIKE_76, 0)
        private val GUTHIX_START: Graphics? = null
        private val GUTHIX_PROJECTILE: Projectile? = null
        private val GUTHIX_END = Graphics(org.rs.consts.Graphics.CLAWS_OF_GUTHIX_77, 0)
        private val ZAM_START: Graphics? = null
        private val ZAM_PROJECTILE: Projectile? = null
        private val ZAM_END = Graphics(org.rs.consts.Graphics.FLAMES_OF_ZAMORAK_78, 0)
        private val ANIMATION = Animation(Animations.HUMAN_CAST_SPELL_LONG_811, Priority.HIGH)
    }
}
