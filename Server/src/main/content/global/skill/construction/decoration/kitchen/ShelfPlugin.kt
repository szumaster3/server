package content.global.skill.construction.decoration.kitchen

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Scenery

class ShelfPlugin : InteractionListener {

    override fun defineListeners() {
        on(SHELVES_FURNITURE_IDS, IntType.SCENERY, "search") { player, node ->
            if (freeSlots(player) == 0) {
                sendMessage(player, "You need at least one free inventory space to take from the shelves.")
                return@on true
            }

            openDialogue(player, ShelfDialogue(node.id))
            return@on true
        }
    }

    private class ShelfDialogue(val shelfId: Int) : DialogueFile() {
        private val itemMap: Map<Int, Int> = mapOf(
            1  to Items.KETTLE_7688,
            2  to getTeapot(shelfId),
            3  to getPorcelainCup(shelfId),
            4  to Items.BEER_GLASS_1919,
            6  to Items.CAKE_TIN_1887,
            7  to Items.BOWL_1923,
            8  to Items.PIE_DISH_2313,
            9  to Items.EMPTY_POT_1931,
            10 to Items.CHEFS_HAT_1949
        )

        override fun handle(componentID: Int, buttonID: Int) {
            when (stage) {
                0 -> showTopics(
                    Topic("Kettle",             1, true),
                    Topic("Teapot",             2, true),
                    IfTopic("Clay cup",         3, shelfId  in listOf(13545, 13546), true),
                    IfTopic("Porcelain cup",    3, shelfId !in listOf(13545, 13546), true),
                    IfTopic("Beer glass",       4, shelfId  != 13545, true),
                    IfTopic("Cake tin",         5, shelfId  in listOf(13547, 13550, 13551), true),
                    IfTopic("More...",          5, shelfId  in 13548..13551, true),
                    title = "What do you want to take?"
                )

                in itemMap.keys -> {
                    end()
                    player?.lock(3)
                    val item = itemMap[buttonID]!!
                    animate(player!!, Animations.TAKE_FROM_SHELF_8904)
                    addItem(player!!, item, 1, Container.INVENTORY)
                    sendMessage(player!!, "You take a ${getItemName(item).lowercase()}.")
                }

                5 -> showTopics(
                    Topic("Cake tin",       6, true),
                    IfTopic("Bowl",         7,  shelfId !in 13545..13547, true),
                    IfTopic("Pie dish",     8,  shelfId  in 13549..13551, true),
                    IfTopic("Empty pot",    9,  shelfId  in listOf(13550, 13551), true),
                    IfTopic("Chef's hat",   10, shelfId  in listOf(13550, 13551), true),
                    title = "What do you want to take?"
                )
            }
        }
    }

    companion object {
        private val SHELVES_FURNITURE_IDS = intArrayOf(
            Scenery.SHELVES_13545,
            Scenery.SHELVES_13546,
            Scenery.SHELVES_13547,
            Scenery.SHELVES_13548,
            Scenery.SHELVES_13549,
            Scenery.SHELVES_13550,
            Scenery.SHELVES_13551
        )

        private fun getTeapot(id: Int) = when (id) {
            13550, 13551 -> Items.TEAPOT_7726
            13549, 13547 -> Items.TEAPOT_7714
            else -> Items.TEAPOT_7702
        }

        private fun getPorcelainCup(id: Int) = when (id) {
            13550, 13551 -> Items.PORCELAIN_CUP_7735
            13549, 13548, 13547 -> Items.PORCELAIN_CUP_7732
            else -> Items.EMPTY_CUP_7728
        }
    }
}
