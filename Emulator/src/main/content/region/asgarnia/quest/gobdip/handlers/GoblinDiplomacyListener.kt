package content.region.asgarnia.quest.gobdip.handlers

import content.global.skill.crafting.items.armour.capes.Dyes
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.world.GameWorld.ticks
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery
import java.util.*

class GoblinDiplomacyListener : InteractionListener {

    companion object {
        private val GOBLIN_MAIL = Item(Items.GOBLIN_MAIL_288)
        private val CRATES = intArrayOf(Scenery.CRATE_16561, Scenery.CRATE_16560, Scenery.CRATE_16559)

        private val GOBLIN_MAIL_BY_DYE: Map<Dyes, Int> = mapOf(
            Dyes.RED    to Items.RED_GOBLIN_MAIL_9054,
            Dyes.ORANGE to Items.ORANGE_GOBLIN_MAIL_286,
            Dyes.YELLOW to Items.YELLOW_GOBLIN_MAIL_9056,
            Dyes.GREEN  to Items.GREEN_GOBLIN_MAIL_9057,
            Dyes.BLUE   to Items.BLUE_GOBLIN_MAIL_287,
            Dyes.PURPLE to Items.PURPLE_GOBLIN_MAIL_9058,
            Dyes.BLACK  to Items.BLACK_GOBLIN_MAIL_9055,
            Dyes.PINK   to Items.PINK_GOBLIN_MAIL_9059
        )
        // Land of the Goblin.
        // Dyes.WHITE to Items.WHITE_GOBLIN_MAIL_11791
    }

    override fun defineListeners() {
        val wearItems = GOBLIN_MAIL_BY_DYE.values.toSet() + Items.GOBLIN_MAIL_288 + Items.WHITE_GOBLIN_MAIL_11791
        for (itemId in wearItems) {
            on(itemId, IntType.ITEM, "wear") { player, _ ->
                sendMessage(player, "That armour is to small for a human.")
                return@on true
            }
        }

        for (crateId in CRATES) {
            on(crateId, IntType.SCENERY, "search") { player, _ ->
                if (player.getAttribute("crate:$crateId", 0) < ticks) {
                    setAttribute(player, "crate:$crateId", ticks + 500)
                    if (!player.inventory.add(GOBLIN_MAIL)) {
                        GroundItemManager.create(GOBLIN_MAIL, player)
                    }
                    sendItemDialogue(player, GOBLIN_MAIL.id, "You find some goblin armour.")
                } else {
                    sendMessage(player, "You search the crate but find nothing.")
                }
                return@on true
            }
        }

        /*
         * Handles using dyes on goblin mail (and mixing dyes).
         */

        val dyeIds = Dyes.values().map { it.item.id }.toIntArray()
        val goblinMailIds = intArrayOf(Items.GOBLIN_MAIL_288) + GOBLIN_MAIL_BY_DYE.values.toIntArray()
        onUseWith(IntType.ITEM, dyeIds, *goblinMailIds) { player, used, with ->
            dyeGoblinMail(player, used.asItem(), with.asItem())
        }

        /*
         * Handles mixing dyes.
         */

        onUseWith(IntType.ITEM, dyeIds, *dyeIds) { player, used, with ->
            val usedDye = Dyes.forItem(used.asItem())
            val withDye = Dyes.forItem(with.asItem())
            if (usedDye != null && withDye != null && usedDye != withDye) {
                handleDyeCombine(player, usedDye, withDye)
                return@onUseWith true
            } else {
                return@onUseWith false
            }
        }
    }

    private fun dyeGoblinMail(player: Player, used: Item, with: Item): Boolean {
        val usedDye = Dyes.forItem(used)
        val baseDye = Dyes.forItem(with)

        val dye = usedDye ?: baseDye ?: return false
        val productId = GOBLIN_MAIL_BY_DYE[dye] ?: return false

        if (used.id != Items.GOBLIN_MAIL_288 && with.id != Items.GOBLIN_MAIL_288) return false

        if (removeItem(player, dye.item)) {
            if (used.id == Items.GOBLIN_MAIL_288) {
                replaceSlot(player, used.index, Item(productId, 1))
            } else {
                replaceSlot(player, with.index, Item(productId, 1))
            }
            sendMessage(player, "You dye the goblin armour ${dye.name.lowercase(Locale.getDefault())}.")
        }
        return true
    }

    private fun handleDyeCombine(player: Player, dye1: Dyes, dye2: Dyes) {
        val (mixedDye, anim) =
            when
            {
                (dye1 == Dyes.RED && dye2 == Dyes.YELLOW) ||
                        (dye1 == Dyes.YELLOW && dye2 == Dyes.RED) -> Dyes.ORANGE to Animations.DYE_COMBINE_4348
                (dye1 == Dyes.YELLOW && dye2 == Dyes.BLUE) ||
                        (dye1 == Dyes.BLUE && dye2 == Dyes.YELLOW) -> Dyes.GREEN to Animations.DYE_COMBINE_4349
                (dye1 == Dyes.RED && dye2 == Dyes.BLUE) ||
                        (dye1 == Dyes.BLUE && dye2 == Dyes.RED) -> Dyes.PURPLE to Animations.DYE_COMBINE_4350

                else -> null to null
            }

        if (mixedDye == null) {
            sendMessage(player, "Those dyes don't mix together.")
            return
        }

        if (!player.inventory.contains(dye1.item.id, 1) || !player.inventory.contains(dye2.item.id, 1)) {
            sendMessage(player, "You don't have the required dyes to mix.")
            return
        }

        player.animate(anim?.let { Animation.create(it) })

        player.inventory.remove(dye1.item)
        player.inventory.remove(dye2.item)
        player.inventory.add(mixedDye.item)

        val article = if (mixedDye.name.lowercase()[0] in "aeiou") "an" else "a"
        sendMessage(player, "You mix the two dyes and make $article ${mixedDye.name.lowercase()} dye.")
    }
}
