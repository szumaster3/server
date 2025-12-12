package content.global.skill.magic.spells.modern

import content.global.skill.magic.spells.ModernSpells
import core.api.getItemName
import core.api.playGlobalAudio
import core.api.sendMessage
import core.game.container.impl.EquipmentContainer
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.impl.Animator.Priority
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Animations
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the God spell.
 */
@Initializable
class GodSpell private constructor(
    private val data: GodSpellDefinition
) : CombatSpell(
    SpellType.GOD_STRIKE,
    SpellBook.MODERN,
    60,
    35.0,
    -1,
    data.impactAudio,
    ANIMATION,
    null,
    data.projectile,
    data.endGraphics,
    *data.runes
) {
    constructor() : this(GodSpellDefinition.SARADOMIN)

    override fun fireEffect(entity: Entity, victim: Entity, state: BattleState) {
        data.effect(entity, victim)
    }

    override fun visualize(entity: Entity, target: Node) {
        super.visualize(entity, target)
        if (entity is NPC) {
            val id = entity.id
            if ((id in (NPCs.KOLODION_911 + 1) until NPCs.LEELA_915) || (id in (NPCs.KOLODION_906 + 1) until NPCs.BATTLE_MAGE_912)) {
                entity.animator.forceAnimation(entity.properties.attackAnimation)
            }
        }
    }

    override fun visualizeImpact(entity: Entity, target: Entity, state: BattleState) {
        if (entity is Player) {
            val p = entity
            val index = INDEX_CACHE[data] ?: run {
                val idx = GOD_SPELLS.indexOf(data)
                INDEX_CACHE[data] = idx
                idx
            }
            val castData = p.getSavedData().activityData
            val spellName = getSpellName(data.name)
            if (castData.godCasts[index] < data.requiredCasts) {
                castData.godCasts[index]++
                if (castData.godCasts[index] == data.requiredCasts) {
                    sendMessage(p, "You can now cast $spellName outside the Arena.")
                }
            }

            if (state.estimatedHit == -1) {
                target.graphics(SPLASH_GRAPHIC)
                playGlobalAudio(target.location, data.failAudio, 20)
                return
            }
        }

        target.graphics(data.endGraphics)
        playGlobalAudio(target.location, data.impactAudio)
    }

    override fun meetsRequirements(caster: Entity, message: Boolean, remove: Boolean): Boolean {
        if (caster !is Player) return true

        val staffId = caster.equipment.getNew(EquipmentContainer.SLOT_WEAPON).id
        val castData = caster.getSavedData().activityData
        val index = INDEX_CACHE[data] ?: run {
            val idx = GOD_SPELLS.indexOf(data)
            INDEX_CACHE[data] = idx
            idx
        }
        val spellName = getSpellName(data.name)
        if (castData.godCasts[index] < data.requiredCasts && !caster.zoneMonitor.isInZone("mage arena")) {
            sendMessage(caster, "You need to cast $spellName ${data.requiredCasts - castData.godCasts[index]} more times inside the Mage Arena.")
            return false
        }

        val hasCorrectStaff = staffId == data.staffId || (data == GodSpellDefinition.GUTHIX && staffId == Items.VOID_KNIGHT_MACE_8841)
        val staffName = getItemName(data.staffId)
        if (!hasCorrectStaff) {
            if (message) {
                sendMessage(caster, "You need to wear a $staffName to cast this spell.")
            }
            return false
        }

        return super.meetsRequirements(caster, message, remove)
    }

    override fun getMaximumImpact(entity: Entity, victim: Entity, state: BattleState): Int {
        return getType().getImpactAmount(entity, victim, 0)
    }

    private fun getSpellName(name: String): String {
        return when (name) {
            "SARADOMIN" -> "Saradomin strike"
            "ZAMORAK" -> "Flames of Zamorak"
            else -> "Claws of Guthix"
        }
    }

    override fun newInstance(type: SpellType?): Plugin<SpellType?> {
        val buttons = intArrayOf(
            ModernSpells.SARADOMIN_STRIKE,
            ModernSpells.CLAWS_OF_GUTHIX,
            ModernSpells.FLAMES_OF_ZAMORAK,
        )
        for (i in buttons.indices) {
            SpellBook.MODERN.register(buttons[i], GodSpell(GOD_SPELLS[i]))
        }
        return this
    }

    companion object {
        private val ANIMATION = Animation(Animations.HUMAN_CAST_SPELL_LONG_811, Priority.HIGH)
        private val SPLASH_GRAPHIC = Graphics(shared.consts.Graphics.SPELL_SPLASH_85, 0, 100)
        private val GOD_SPELLS = enumValues<GodSpellDefinition>()
        private val INDEX_CACHE = mutableMapOf<GodSpellDefinition, Int>()
    }
}
