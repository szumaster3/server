package content.global.skill.magic.items

import content.global.skill.magic.SpellListener
import content.global.skill.magic.spells.ModernSpells
import core.api.*
import core.game.event.ResourceProducedEvent
import core.game.node.Node
import core.game.node.entity.impl.Animator
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import shared.consts.Items
import shared.consts.Scenery
import shared.consts.Sounds
import shared.consts.Graphics as Gfx

class ChargeOrbSpell : SpellListener("modern") {
    private val chargeOrbSpellAnimation = Animation(726, Animator.Priority.HIGH)

    private enum class ChargeOrb(
        val obelisk: IntArray,
        val requiredRunes: IntArray,
        val requiredAmount: IntArray,
        val level: Int,
        val experience: Double,
        val graphics: Graphics,
        val sound: Int,
        val product: Int,
    ) {
        CHARGE_WATER_ORB(
            obelisk = intArrayOf(Scenery.OBELISK_OF_WATER_2151, Scenery.TILE_40994, 41338),
            requiredRunes = intArrayOf(Items.COSMIC_RUNE_564, Items.WATER_RUNE_555, Items.UNPOWERED_ORB_567),
            requiredAmount = intArrayOf(3, 30, 1),
            level = 56,
            experience = 66.0,
            graphics = Graphics(Gfx.POWERING_WATER_ORB_149, 92),
            sound = Sounds.CHARGE_WATER_ORB_118,
            product = Items.WATER_ORB_571
        ),
        CHARGE_EARTH_ORB(
            obelisk = intArrayOf(Scenery.OBELISK_OF_EARTH_29415, Scenery.TILE_40994, 41338),
            requiredRunes = intArrayOf(Items.COSMIC_RUNE_564, Items.EARTH_RUNE_557, Items.UNPOWERED_ORB_567),
            requiredAmount = intArrayOf(3, 30, 1),
            level = 60,
            experience = 70.0,
            graphics = Graphics(Gfx.POWERING_EARTH_ORB_151, 92),
            sound = Sounds.CHARGE_EARTH_ORB_115,
            product = Items.EARTH_ORB_575
        ),
        CHARGE_FIRE_ORB(
            obelisk = intArrayOf(Scenery.OBELISK_OF_FIRE_2153, Scenery.TILE_40994, 41338),
            requiredRunes = intArrayOf(Items.COSMIC_RUNE_564, Items.FIRE_RUNE_554, Items.UNPOWERED_ORB_567),
            requiredAmount = intArrayOf(3, 30, 1),
            level = 63,
            experience = 73.0,
            graphics = Graphics(Gfx.POWERING_FIRE_ORB_152, 92),
            sound = Sounds.CHARGE_FIRE_ORB_117,
            product = Items.FIRE_ORB_569
        ),
        CHARGE_AIR_ORB(
            obelisk = intArrayOf(Scenery.OBELISK_OF_AIR_2152, Scenery.TILE_40994, 41338),
            requiredRunes = intArrayOf(Items.COSMIC_RUNE_564, Items.AIR_RUNE_556, Items.UNPOWERED_ORB_567),
            requiredAmount = intArrayOf(3, 30, 1),
            level = 66,
            experience = 76.0,
            graphics = Graphics(Gfx.POWERING_AIR_ORB_150, 92),
            sound = Sounds.CHARGE_AIR_ORB_116,
            product = Items.AIR_ORB_573
        );

        companion object {
            val spellMap = HashMap<Int, ChargeOrb>()

            init {
                for (spell in values()) {
                    for (obeliskId in spell.obelisk) {
                        spellMap[obeliskId] = spell
                    }
                }
            }
        }
    }

    override fun defineListeners() {
        onCast(
            ModernSpells.CHARGE_WATER_ORB,
            OBJECT,
            Scenery.OBELISK_OF_WATER_2151,
            41338, // Wrapper
            Scenery.TILE_40994,
            range = 3,
            method = ::chargeOrb
        )
        onCast(
            ModernSpells.CHARGE_EARTH_ORB,
            OBJECT,
            Scenery.OBELISK_OF_EARTH_29415,
            41338, // Wrapper
            Scenery.TILE_40994,
            range = 3,
            method = ::chargeOrb
        )
        onCast(
            ModernSpells.CHARGE_FIRE_ORB,
            OBJECT,
            Scenery.OBELISK_OF_FIRE_2153,
            41338, // Wrapper
            Scenery.TILE_40994,
            range = 3,
            method = ::chargeOrb
        )
        onCast(
            ModernSpells.CHARGE_AIR_ORB,
            OBJECT,
            Scenery.OBELISK_OF_AIR_2152,
            41338, // Wrapper
            Scenery.TILE_40994,
            range = 3,
            method = ::chargeOrb
        )
    }

    private fun chargeOrb(player: Player, node: Node?) {
        if (node == null) return
        val spell = ChargeOrb.spellMap[node.id] ?: return

        requires(player, spell.level)
        removeAttribute(player, "spell:runes")
        faceLocation(player, node.location)

        if (node.id == 41338 || node.id == Scenery.TILE_40994) {
            queueScript(player, 0) {
                visualizeSpell(player, chargeOrbSpellAnimation, spell.graphics, spell.sound)
                removeRunes(player, spell)
                addXP(player, spell.experience)
                setDelay(player, 3)
                setVarbit(player, 5551, 1, true)
                sendItemDialogue(
                    player,
                    Items.UNPOWERED_ORB_567,
                    "The orb shatters with the power of the magic, and the tile magically transforms into a trapdoor."
                )
                return@queueScript stopExecuting(player)
            }
            return
        }

        sendSkillDialogue(player) {
            withItems(spell.product)
            calculateMaxAmount { amountInInventory(player, Items.UNPOWERED_ORB_567) }
            create { _, amount ->
                var crafted = 0
                queueScript(player, 0) {
                    if (!hasLevelDyn(player, Skills.MAGIC, spell.level)) {
                        sendMessage(player, "You need a Magic level of ${spell.level} to cast this spell.")
                        return@queueScript stopExecuting(player)
                    }
                    for (i in spell.requiredRunes.indices) {
                        val runeId = spell.requiredRunes[i]
                        val quantity = spell.requiredAmount[i]
                        if (!player.inventory.contains(runeId, quantity)) {
                            val runeName = getItemName(runeId).lowercase()
                            sendMessage(player, "You don't have enough ${runeName}s to cast this spell.")
                            return@queueScript stopExecuting(player)
                        }
                    }

                    visualizeSpell(player, chargeOrbSpellAnimation, spell.graphics, spell.sound)
                    removeRunes(player, spell)
                    addItem(player, spell.product)
                    addXP(player, spell.experience)
                    setDelay(player, 3)
                    crafted++

                    if (crafted == 5 && spell.product == Items.WATER_ORB_571) {
                        player.dispatch(ResourceProducedEvent(spell.product, crafted, node))
                    }
                    if (crafted == amount) {
                        return@queueScript stopExecuting(player)
                    }
                    setCurrentScriptState(player, 0)
                    return@queueScript delayScript(player, 6)
                }
            }
        }
    }

    companion object {
        private fun removeRunes(player: Player, spell: ChargeOrb) {
            for (i in spell.requiredRunes.indices) {
                val runeId = spell.requiredRunes[i]
                val amount = spell.requiredAmount[i]
                removeItem(player, Item(runeId, amount))
            }
        }
    }
}