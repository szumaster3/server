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

class LarderPlugin : InteractionListener {

    override fun defineListeners() {
        on(LARDERS, IntType.SCENERY, "Search") { player, node ->
            if (freeSlots(player) == 0) {
                sendDialogue(player, "You need at least one free inventory space to take from the larder.")
                return@on true
            }
            openDialogue(player, LarderDialogue(node.id))
            return@on true
        }
    }

    private class LarderDialogue(private val larderId: Int) : DialogueFile() {
        override fun handle(componentID: Int, buttonID: Int) {
            when (stage) {
                0 -> showTopics(
                    Topic("Tea", 1, true),
                    Topic("Milk", 2, true),
                    IfTopic("Egg", 3, larderId != Scenery.WOODEN_LARDER_13565, true),
                    IfTopic("Flour", 4, larderId != Scenery.WOODEN_LARDER_13565, true),
                    IfTopic("More...", 5, larderId == Scenery.TEAK_LARDER_13567, true)
                )

                in items.keys -> {
                    end()
                    player?.lock(3)
                    items[stage]?.let { (item, message) ->
                        animate(player!!, SEARCH_ANIM)
                        addItem(player!!, item, 1, Container.INVENTORY)
                        sendMessage(player!!, message, 1)
                    }
                }

                5 -> showTopics(
                    Topic("Potatoes", 6, true),
                    Topic("Garlic", 7, true),
                    Topic("Onions", 8, true),
                    Topic("Cheese", 9, true)
                )
            }
        }

        companion object {
            private val items = mapOf(
                1 to Pair(Items.TEA_LEAVES_7738, "You take some tea leaves."),
                2 to Pair(Items.BUCKET_OF_MILK_1927, "You take a bucket of milk."),
                3 to Pair(Items.EGG_1944, "You take an egg."),
                4 to Pair(Items.POT_OF_FLOUR_1933, "You take some flour."),
                6 to Pair(Items.POTATO_1942, "You take potato."),
                7 to Pair(Items.GARLIC_1550, "You take garlic."),
                8 to Pair(Items.ONION_1957, "You take an onion."),
                9 to Pair(Items.CHEESE_1985, "You take a cheese."),
            )
        }
    }


    companion object {
        private val LARDERS = intArrayOf(Scenery.WOODEN_LARDER_13565, Scenery.OAK_LARDER_13566, Scenery.TEAK_LARDER_13567)
        private const val SEARCH_ANIM = Animations.HUMAN_SEARCH_LARDER_3659
    }
}
