package content.global.skill.construction.decoration.workshop

import content.data.GameAttributes
import core.api.*
import core.api.interaction.getSceneryName
import core.game.dialogue.DialogueFile
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Items
import org.rs.consts.Scenery

class HeraldrySpace : InteractionListener {
    override fun defineListeners() {
        /*
         * Handles decorate helmets, kiteshields, and banners.
         */

        on(
            intArrayOf(
                Scenery.HELMET_PLUMING_STAND_13716,
                Scenery.PAINTING_STAND_13717,
                Scenery.BANNER_MAKING_STAND_13718,
            ),
            IntType.SCENERY,
            "make-helmet",
            "use",
        ) { player, node ->
            if (node.id == 13716 && getDynLevel(player, Skills.CRAFTING) < 38) {
                sendMessage(player, "You need a crafting level of 38 in order to do this.")
                return@on true
            }
            if (node.id == 13717 && getDynLevel(player, Skills.CRAFTING) < 43) {
                sendMessage(player, "You need a crafting level of 43 in order to do this.")
                return@on true
            }
            if (node.id == 13718 && getDynLevel(player, Skills.CRAFTING) < 48) {
                sendMessage(player, "You need a crafting level of 48 in order to do this.")
                return@on true
            }
            val crest = player.getAttribute(GameAttributes.FAMILY_CREST, 0)
            if (crest == null) {
                sendMessage(
                    player,
                    "You need to set your family crest before you can use the ${getSceneryName(node.id).lowercase()}.",
                )
                return@on true
            }
            openDialogue(
                player,
                object : DialogueFile() {
                    override fun handle(
                        componentID: Int,
                        buttonID: Int,
                    ) {
                        val sceneryID = node.asScenery().id
                        when (stage) {
                            0 -> {
                                setTitle(
                                    player,
                                    when (sceneryID) {
                                        13716 -> 2
                                        13717 -> 4
                                        else -> 5
                                    },
                                )
                                sendDialogueOptions(
                                    player,
                                    "What do you want to make?",
                                    *when (sceneryID) {
                                        // Pluming stand.
                                        13716 -> arrayOf("Steel helmet", "Runite helmet")
                                        // Shield easel.
                                        13717 ->
                                            arrayOf(
                                                "Steel helmet",
                                                "Runite helmet",
                                                "Steel heraldic shield",
                                                "Runite heraldic shield",
                                            )
                                        // Banner easel.
                                        else ->
                                            arrayOf(
                                                "Steel helmet",
                                                "Runite helmet",
                                                "Steel heraldic shield",
                                                "Runite heraldic shield",
                                                "Heraldic banner",
                                            )
                                    },
                                )
                                stage++
                            }

                            1 -> {
                                end()
                                lock(player, 1)
                                val (itemID, animationID, productID) =
                                    when (buttonID) {
                                        // Steel full helms.
                                        1 ->
                                            Triple(
                                                first = Items.STEEL_FULL_HELM_1157,
                                                second = 3656,
                                                third = if (crest in 1..16) 8682 + (crest - 1) * 2 else 1157,
                                            )
                                        // Rune full helms.
                                        2 ->
                                            Triple(
                                                first = Items.RUNE_FULL_HELM_1163,
                                                second = 3656,
                                                third = if (crest in 1..16) 8464 + (crest - 1) * 2 else 1163,
                                            )
                                        // Steel shields.
                                        3 ->
                                            Triple(
                                                first = Items.STEEL_KITESHIELD_1193,
                                                second = 3654,
                                                third = if (crest in 1..16) 8746 + (crest - 1) * 2 else 1193,
                                            )
                                        // Rune shields.
                                        4 ->
                                            Triple(
                                                first = Items.RUNE_KITESHIELD_1201,
                                                second = 3654,
                                                third = if (crest in 1..16) 8714 + (crest - 1) * 2 else 1201,
                                            )
                                        // Banners.
                                        5 ->
                                            Triple(
                                                first = null,
                                                second = 3655,
                                                third = if (crest in 1..16) 8650 + (crest - 1) * 2 else null,
                                            )

                                        else -> return
                                    }
                                if (buttonID == 5 &&
                                    (
                                        !inInventory(
                                            player,
                                            Items.BOLT_OF_CLOTH_8790,
                                        ) &&
                                            !inInventory(player, Items.PLANK_960)
                                    )
                                ) {
                                    sendDialogue(player, "You need 1 bolt of cloth and 1 plank to do this.")
                                    return
                                }
                                if (itemID != null && !inInventory(player, itemID)) {
                                    sendDialogue(player, "You need ${getItemName(itemID)} to do this.")
                                    return
                                }
                                if (itemID != null) {
                                    removeItem(player, Item(itemID, 1), Container.INVENTORY)
                                }
                                if (buttonID == 5) {
                                    removeItem(player, Item(Items.BOLT_OF_CLOTH_8790, 1), Container.INVENTORY)
                                    removeItem(player, Item(Items.PLANK_960, 1), Container.INVENTORY)
                                }

                                face(player, node)
                                animate(player, animationID)
                                runTask(player, 3) {
                                    sendMessage(player, "You make an item with your symbol on it.")
                                    addItem(player, productID ?: return@runTask, 1)
                                    rewardXP(
                                        player,
                                        Skills.CRAFTING,
                                        when (productID) {
                                            in 8746..8776 -> 40.0
                                            in 8714..8744 -> 42.5
                                            else -> 37.0
                                        },
                                    )
                                }
                            }
                        }
                    }
                },
            )
            return@on true
        }
    }
}
