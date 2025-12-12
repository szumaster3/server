package content.region.asgarnia.falador.dialogue

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import shared.consts.Items

/**
 * Dialogue handler for the bartenders in the Rising Sun Inn.
 */
class RisingSunInnBartenderDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        if (player == null || npc == null) return

        when (stage) {
            0 -> npc(FaceAnim.HAPPY, "Hi! What can I get you?").also { stage++ }
            1 -> showTopics(
                Topic(FaceAnim.ASKING, "What ales are you serving?", 2),
                IfTopic(FaceAnim.HAPPY, "I've got some beer glasses...", 7, hasAnyBeerGlasses())
            )
            2 -> npc(FaceAnim.FRIENDLY, "Well, we've got Asgarnian Ale, Wizard's Mind Bomb and Dwarven Stout.", "Each for only 3 coins.").also { stage++ }
            3 -> showTopics(
                Topic(FaceAnim.HAPPY, "One Asgarnian Ale, please.", 4),
                Topic(FaceAnim.HAPPY, "I'll try the Mind Bomb.", 5),
                Topic(FaceAnim.ASKING, "Can I have a Dwarven Stout?", 6),
                Topic(FaceAnim.NEUTRAL, "I don't feel like any of those.", END_DIALOGUE)
            )
            4 -> end().also { attemptPurchase(Items.ASGARNIAN_ALE_1905) }
            5 -> end().also { attemptPurchase(Items.WIZARDS_MIND_BOMB_1907) }
            6 -> end().also { attemptPurchase(Items.DWARVEN_STOUT_1913) }
            7 -> npc(FaceAnim.HALF_GUILTY, "Oh, we will buy those from you if you're interested.", "We offer 2 coins for each glass.").also { stage++ }
            8 -> showTopics(
                Topic(FaceAnim.HAPPY, "Yes, please!", 9),
                Topic(FaceAnim.NEUTRAL, "No thanks, I like my empty beer glasses.", END_DIALOGUE)
            )
            9 -> {
                end()
                sellAllBeerGlasses()
                npc(FaceAnim.FRIENDLY, "There you go!").also { stage = END_DIALOGUE }
            }
            10 -> player(FaceAnim.FRIENDLY, "Thanks, ${npc!!.name}.").also { stage = END_DIALOGUE }
        }
    }

    /**
     * Attempts to purchase a brew if the player has enough coins.
     */
    private fun attemptPurchase(brewId: Int) {
        if (player == null) return
        if (!inInventory(player!!, Items.COINS_995, 3)) {
            npc(FaceAnim.ANGRY, "I said 3 coins! You haven't got 3 coins!").also { stage = END_DIALOGUE }
            return
        }
        removeItem(player!!, Item(Items.COINS_995, 3))
        addItemOrDrop(player!!, brewId)
        sendItemDialogue(player!!, Item(brewId), "You hand 3 coins over to ${npc!!.name} and receive ${
            when (brewId) {
                Items.ASGARNIAN_ALE_1905 -> "an Asgarnian Ale"
                Items.WIZARDS_MIND_BOMB_1907 -> "a Wizard's Mind Bomb"
                Items.DWARVEN_STOUT_1913 -> "a Dwarven Stout"
                else -> "something unusual"
            }
        }.").also { stage = 10 }
    }

    /**
     * Checks if the player has any beer glasses.
     */
    private fun hasAnyBeerGlasses(): Boolean =
        inInventory(player!!, Items.BEER_GLASS_1919) || inInventory(player!!, Items.BEER_GLASS_1920)

    /**
     * Sells all beer glasses the player has.
     */
    private fun sellAllBeerGlasses() {
        if (player == null) return

        val regular = amountInInventory(player!!, Items.BEER_GLASS_1919)
        val noted = amountInInventory(player!!, Items.BEER_GLASS_1920)

        if (regular > 0) {
            removeItem(player!!, Item(Items.BEER_GLASS_1919, regular))
            addItem(player!!, Items.COINS_995, regular * 2)
        }
        if (noted > 0) {
            removeItem(player!!, Item(Items.BEER_GLASS_1920, noted))
            addItem(player!!, Items.COINS_995, noted * 2)
        }
    }
}
