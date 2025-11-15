package content.global.skill.construction.decoration.workpace

import content.data.GameAttributes
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import shared.consts.Items
import shared.consts.Scenery

/**
 * Handles the crafting of helms, shields, and banners in the player's family crest colors.
 */
class HeraldryPlugin : InteractionListener {

    enum class Heraldry(val id: Int, val requiredLevel: Int, val anim: Int, val options: Array<String>, val baseItems: IntArray, val products: IntArray, val xp: DoubleArray) {
        HELMET(
            Scenery.HELMET_PLUMING_STAND_13716,
            38,
            3655,
            arrayOf("Steel helmet", "Runite helmet"),
            intArrayOf(Items.STEEL_FULL_HELM_1157, Items.RUNE_FULL_HELM_1163),
            intArrayOf(Items.STEEL_HERALDIC_HELM_8682, Items.RUNE_HERALDIC_HELM_8464),
            doubleArrayOf(37.0, 37.0)
        ),
        PAINTING(
            Scenery.PAINTING_STAND_13717,
            43,
            3655,
            arrayOf("Steel helmet", "Runite helmet", "Steel shield", "Runite shield"),
            intArrayOf(Items.STEEL_FULL_HELM_1157, Items.RUNE_FULL_HELM_1163, Items.STEEL_KITESHIELD_1193, Items.RUNE_KITESHIELD_1201),
            intArrayOf(Items.STEEL_HERALDIC_HELM_8682, Items.RUNE_HERALDIC_HELM_8464, Items.STEEL_KITESHIELD_8746, Items.RUNE_KITESHIELD_8714),
            doubleArrayOf(37.0, 37.0, 40.0, 42.5)
        ),
        BANNER(
            Scenery.BANNER_MAKING_STAND_13718,
            48,
            3656,
            arrayOf("Steel helmet", "Runite helmet", "Steel shield", "Runite shield", "Heraldic banner"),
            intArrayOf(Items.STEEL_FULL_HELM_1157, Items.RUNE_FULL_HELM_1163, Items.STEEL_KITESHIELD_1193, Items.RUNE_KITESHIELD_1201, 0),
            intArrayOf(Items.STEEL_HERALDIC_HELM_8682, Items.RUNE_HERALDIC_HELM_8464, Items.STEEL_KITESHIELD_8746, Items.RUNE_KITESHIELD_8714, Items.BANNER_8650),
            doubleArrayOf(37.0, 37.0, 40.0, 42.5, 37.0)
        );

        companion object {
            private val map = values().associateBy { it.id }
            fun of(id: Int) = map[id]
        }
    }

    override fun defineListeners() {
        val ids = Heraldry.values().map { it.id }.toIntArray()
        on(ids, IntType.SCENERY, "make-helmet", "use") { player, node ->
            val station = Heraldry.of(node.id) ?: return@on true
            val level = getDynLevel(player, Skills.CRAFTING)
            if (level < station.requiredLevel) {
                sendMessage(player, "You need Crafting level ${station.requiredLevel} to do this.")
                return@on false
            }

            val crest = player.getAttribute(GameAttributes.FAMILY_CREST, -1)
            if (crest !in 1..16) {
                sendMessage(player, "You must set your family crest before using this.")
                return@on true
            }

            openDialogue(player, HeraldryDialogue(station, crest))
            return@on true
        }
    }

    inner class HeraldryDialogue(private val station: Heraldry, private val crest: Int) : DialogueFile() {

        override fun handle(componentID: Int, buttonID: Int) {
            val p = player ?: return
            when (stage) {
                0 -> {
                    val titleLines = when (station) {
                        Heraldry.HELMET -> 2
                        Heraldry.PAINTING -> 4
                        Heraldry.BANNER -> 5
                    }
                    setTitle(p, titleLines)
                    sendOptions(p, "What do you want to make?", *station.options)
                    stage = 1
                }
                1 -> {
                    stage = 2
                    craft(p, buttonID - 1)
                    end()
                }
            }
        }

        private fun craft(p: Player, index: Int) {
            if (index !in station.options.indices) return

            val requiredItemId = station.baseItems[index]
            val baseItemId = station.products[index]
            val xp = station.xp[index]

            if (requiredItemId == 0) {
                if (!inInventory(p, Items.BOLT_OF_CLOTH_8790, 1) || !inInventory(p, Items.PLANK_960, 1)) {
                    sendMessage(p, "You need a bolt of cloth and a plank to make a banner.")
                    return
                }
                removeItem(p, Item(Items.BOLT_OF_CLOTH_8790, 1), Container.INVENTORY)
                removeItem(p, Item(Items.PLANK_960, 1), Container.INVENTORY)
            } else if (!inInventory(p, requiredItemId, 1)) {
                sendMessage(p, "You need ${getItemName(requiredItemId).lowercase()} to do this.")
                return
            } else {
                removeItem(p, Item(requiredItemId, 1), Container.INVENTORY)
            }

            val productId = if (requiredItemId != 0) baseItemId + (crest - 1) * 2 else baseItemId

            animate(p, station.anim)
            addItem(p, productId, 1)
            rewardXP(p, Skills.CRAFTING, xp)

            val message = when (index) {
                0, 1 -> "helm"
                2, 3 -> "shield"
                else -> "banner"
            }

            sendMessage(p, "You make a $message with your symbol on.")
        }
    }
}