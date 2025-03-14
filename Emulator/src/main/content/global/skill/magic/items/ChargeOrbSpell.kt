package content.global.skill.magic.items

import content.global.skill.magic.SpellListener
import content.global.skill.magic.SpellUtils.hasRune
import content.global.skill.magic.spells.ModernSpells
import core.api.*
import core.api.skill.sendSkillDialogue
import core.game.event.ResourceProducedEvent
import core.game.node.Node
import core.game.node.entity.impl.Animator
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import org.rs.consts.Items
import org.rs.consts.Scenery
import org.rs.consts.Sounds

class ChargeOrbSpell : SpellListener("modern") {
    private val chargeOrbSpellAnimation = Animation(726, Animator.Priority.HIGH)

    override fun defineListeners() {
        onCast(ModernSpells.CHARGE_WATER_ORB, OBJECT, Scenery.OBELISK_OF_WATER_2151, 3, method = ::chargeOrb)
        onCast(ModernSpells.CHARGE_EARTH_ORB, OBJECT, Scenery.OBELISK_OF_EARTH_29415, 3, method = ::chargeOrb)
        onCast(ModernSpells.CHARGE_FIRE_ORB, OBJECT, Scenery.OBELISK_OF_FIRE_2153, 3, method = ::chargeOrb)
        onCast(ModernSpells.CHARGE_AIR_ORB, OBJECT, Scenery.OBELISK_OF_AIR_2152, 3, method = ::chargeOrb)
    }

    private fun chargeOrb(
        player: Player,
        node: Node?,
    ) {
        if (node == null) return
        val spell = ChargeOrb.spellMap[node.id] ?: return
        requires(player, spell.level, spell.requiredRunes)
        removeAttribute(player, "spell:runes")
        face(player, node)
        sendSkillDialogue(player) {
            withItems(spell.product)
            calculateMaxAmount { return@calculateMaxAmount amountInInventory(player, Items.UNPOWERED_ORB_567) }
            create { _, amount ->
                var crafted = 0
                queueScript(player, 0) {
                    if (!hasLevelDyn(player, Skills.MAGIC, spell.level)) {
                        sendMessage(player, "You need a magic level of ${spell.level} to cast this spell.")
                        return@queueScript stopExecuting(player)
                    }
                    for (rune in spell.requiredRunes) {
                        if (!hasRune(player, rune)) {
                            sendMessage(player, "You don't have enough ${rune.name.lowercase()}s to cast this spell.")
                            return@queueScript stopExecuting(player)
                        }
                    }
                    visualizeSpell(player, chargeOrbSpellAnimation, spell.graphics, spell.sound)
                    removeRunes(player)
                    addItem(player, spell.product)
                    addXP(player, spell.experience)
                    setDelay(player, 3)
                    crafted++

                    if (crafted == 5 && spell.product == Items.WATER_ORB_571) {
                        player.dispatch(ResourceProducedEvent(spell.product, crafted, node))
                    }
                    if (amount == crafted) {
                        return@queueScript stopExecuting(player)
                    }
                    setCurrentScriptState(player, 0)
                    return@queueScript delayScript(player, 6)
                }
            }
        }
        return
    }

    private enum class ChargeOrb(
        val obelisk: Int,
        val requiredRunes: Array<Item>,
        val level: Int,
        val experience: Double,
        val graphics: Graphics,
        val sound: Int,
        val product: Int,
    ) {
        CHARGE_WATER_ORB(
            Scenery.OBELISK_OF_WATER_2151,
            arrayOf(Item(Items.COSMIC_RUNE_564, 3), Item(Items.WATER_RUNE_555, 30), Item(Items.UNPOWERED_ORB_567)),
            56,
            66.0,
            Graphics(org.rs.consts.Graphics.POWERING_WATER_ORB_149, 90),
            Sounds.CHARGE_WATER_ORB_118,
            Items.WATER_ORB_571,
        ),
        CHARGE_EARTH_ORB(
            Scenery.OBELISK_OF_EARTH_29415,
            arrayOf(Item(Items.COSMIC_RUNE_564, 3), Item(Items.EARTH_RUNE_557, 30), Item(Items.UNPOWERED_ORB_567)),
            60,
            70.0,
            Graphics(org.rs.consts.Graphics.POWERING_EARTH_ORB_151, 90),
            Sounds.CHARGE_EARTH_ORB_115,
            Items.EARTH_ORB_575,
        ),
        CHARGE_FIRE_ORB(
            Scenery.OBELISK_OF_FIRE_2153,
            arrayOf(Item(Items.COSMIC_RUNE_564, 3), Item(Items.FIRE_RUNE_554, 30), Item(Items.UNPOWERED_ORB_567)),
            63,
            73.0,
            Graphics(org.rs.consts.Graphics.POWERING_FIRE_ORB_152, 90),
            Sounds.CHARGE_FIRE_ORB_117,
            Items.FIRE_ORB_569,
        ),
        CHARGE_AIR_ORB(
            Scenery.OBELISK_OF_AIR_2152,
            arrayOf(Item(Items.COSMIC_RUNE_564, 3), Item(Items.AIR_RUNE_556, 30), Item(Items.UNPOWERED_ORB_567)),
            66,
            76.0,
            Graphics(org.rs.consts.Graphics.POWERING_AIR_ORB_150, 90),
            Sounds.CHARGE_AIR_ORB_116,
            Items.AIR_ORB_573,
        ),
        ;

        companion object {
            val spellMap = HashMap<Int, ChargeOrb>()

            init {
                for (spell in ChargeOrb.values()) {
                    spellMap[spell.obelisk] = spell
                }
            }
        }
    }
}
