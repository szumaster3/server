package content.global.skill.construction.decoration.kitchen

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items
import org.rs.consts.Scenery

class ShelfSpace : InteractionListener {
    override fun defineListeners() {
        on(SHELF, IntType.SCENERY, "search") { player, node ->
            if (freeSlots(player) == 0) {
                sendDialogue(player, "You need at least one free inventory space to take from the shelves.")
                return@on true
            }
            openDialogue(
                player,
                object : DialogueFile() {
                    override fun handle(
                        componentID: Int,
                        buttonID: Int,
                    ) {
                        val itemMap =
                            mapOf(
                                1 to Items.KETTLE_7688,
                                2 to getTeapot(node.id),
                                3 to getPorcelainCup(node.id),
                                4 to Items.BEER_GLASS_1919,
                                6 to Items.CAKE_TIN_1887,
                                7 to Items.BOWL_1923,
                                8 to Items.PIE_DISH_2313,
                                9 to Items.EMPTY_POT_1931,
                                10 to Items.CHEFS_HAT_1949,
                            )
                        when (stage) {
                            0 ->
                                when (node.id) {
                                    13545 ->
                                        showTopics(
                                            Topic("Kettle", 1, true),
                                            Topic("Teapot", 2, true),
                                            Topic("Clay cup", 3, true),
                                        )

                                    13546 ->
                                        showTopics(
                                            Topic("Kettle", 1, true),
                                            Topic("Teapot", 2, true),
                                            Topic("Clay cup", 3, true),
                                            Topic("Beer glass", 4, true),
                                        )

                                    13547 ->
                                        showTopics(
                                            Topic("Kettle", 1, true),
                                            Topic("Teapot", 2, true),
                                            Topic("Porcelain cup", 3, true),
                                            Topic("Beer glass", 4, true),
                                            Topic("Cake tin", 5, true),
                                        )

                                    13548, 13549 ->
                                        showTopics(
                                            Topic("Kettle", 1, true),
                                            Topic("Teapot", 2, true),
                                            Topic("Porcelain cup", 3, true),
                                            Topic("Beer glass", 4, true),
                                            Topic("More...", 5, true),
                                        )

                                    13550, 13551 ->
                                        showTopics(
                                            Topic("Kettle", 1, true),
                                            Topic("Teapot", 2, true),
                                            Topic("Porcelain cup", 3, true),
                                            Topic("Beer glass", 4, true),
                                            Topic("More...", 5, true),
                                        )
                                }

                            5 ->
                                showTopics(
                                    Topic("Cake tin", 6, true),
                                    IfTopic("Bowl", 7, node.id !in (13545..13547).toIntArray(), true),
                                    IfTopic("Pie dish", 8, node.id in (13549..13551).toIntArray(), true),
                                    IfTopic("Empty pot", 9, node.id == 13550 || node.id == 13551, true),
                                    IfTopic("Chef's hat", 10, node.id == 13550 || node.id == 13551, true),
                                )

                            in itemMap.keys -> {
                                end()
                                itemMap[stage]?.let { item ->
                                    addItem(player, item, 1, Container.INVENTORY)
                                    sendMessage(player, "You take a ${getItemName(item).lowercase()}.")
                                }
                            }
                        }
                    }
                },
            )

            return@on true
        }
    }

    companion object {
        private val SHELF =
            intArrayOf(
                Scenery.SHELVES_13545,
                Scenery.SHELVES_13546,
                Scenery.SHELVES_13547,
                Scenery.SHELVES_13548,
                Scenery.SHELVES_13549,
                Scenery.SHELVES_13550,
                Scenery.SHELVES_13551,
            )

        private fun getTeapot(id: Int): Int {
            return when (id) {
                13550, 13551 -> Items.TEAPOT_7726
                13549, 13547 -> Items.TEAPOT_7714
                else -> Items.TEAPOT_7702
            }
        }

        private fun getPorcelainCup(id: Int): Int {
            return when (id) {
                13550, 13551 -> Items.PORCELAIN_CUP_7735
                13549, 13548, 13547 -> Items.PORCELAIN_CUP_7732
                else -> Items.EMPTY_CUP_7728
            }
        }
    }
}
