package content.global.handlers.item.withnpc

import content.region.kandarin.quest.elena.handlers.PlagueCityUtils
import core.api.Container
import core.api.openDialogue
import core.api.quest.isQuestComplete
import core.api.removeItem
import core.api.sendMessage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

class CatOnWendyListener : InteractionListener {
    companion object {
        val cats = PlagueCityUtils.grownCatItemIds.map { it.id }.toIntArray()
    }

    override fun defineListeners() {
        onUseWith(IntType.NPC, NPCs.WENDY_8201, *cats) { player, _, _ ->
            if (isQuestComplete(player, Quests.SWEPT_AWAY)) {
                sendMessage(player, "You can't use your cat on Wendy.")
                return@onUseWith false
            }

            openDialogue(player, WendyRegularCatDialogueExtension())
            return@onUseWith true
        }

        onUseWith(IntType.NPC, NPCs.WENDY_8201, *PlagueCityUtils.purpleCats) { player, _, _ ->
            sendMessage(player, "You show your purple cat to Wendy.")
            openDialogue(player, WendyPurpleCatDialogueExtension())
            return@onUseWith true
        }
    }
}

class WendyRegularCatDialogueExtension : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.WENDY_8201)
        when (stage) {
            0 -> player("Could you make this cat purple for me?").also { stage++ }
            1 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I can, but I should warn you: I can make cats purple, but I don't know how to change them back. It's a permanent change! Is that okay?",
                ).also {
                    stage++
                }
            2 -> npcl(FaceAnim.FRIENDLY, "It's a permanent change! Is that okay?").also { stage++ }
            3 ->
                options(
                    "Yes, please make my cat permanently purple.",
                    "No, I think I'll hold off for the time being.",
                ).also {
                    stage++
                }
            4 ->
                when (buttonID) {
                    1 -> player("Yes, please make my cat permanently purple.").also { stage = 5 }
                    2 -> playerl(FaceAnim.NEUTRAL, "No, I think I'll hold off for the time being.").also { stage = 5 }
                }
            5 -> {
                val removeCat = CatOnWendyListener.cats.find { id -> player!!.inventory.contains(id, 1) }
                if (!removeItem(player!!, Item(removeCat!!, 1), Container.INVENTORY)) {
                    end()
                    player("I seem to have misplaced my cat!")
                    stage = END_DIALOGUE
                } else {
                    npc("There you go! Oh, such a beautiful colour.")
                    player!!.inventory.add(Item(PlagueCityUtils.purpleCats.random(), 1))
                    stage++
                }
                stage++
            }
            6 -> player("Thank you.").also { stage = END_DIALOGUE }
            7 -> npcl(FaceAnim.FRIENDLY, "Okay. If you change your mind, let me know.").also { stage = END_DIALOGUE }
        }
    }
}

class WendyPurpleCatDialogueExtension : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.WENDY_8201)
        when (stage) {
            0 -> npc("Oh, what a lovely cat you have there. Hello, kitty!").also { stage++ }
            1 -> options("Can you change this cat back to its normal colour?", "Thank you.").also { stage++ }
            2 ->
                when (buttonID) {
                    1 -> player("Can you change this cat back to its normal colour?").also { stage++ }
                    2 -> player("Thank you.").also { stage = END_DIALOGUE }
                }
            3 ->
                npcl(
                    FaceAnim.STRUGGLE,
                    "Um, did I forget to mention...once the spell is cast, it's cast for good. I can't undo the purple.",
                ).also {
                    stage++
                }
            4 -> player(FaceAnim.HALF_CRYING, "Ah, right. Hmm.").also { stage = END_DIALOGUE }
        }
    }
}
