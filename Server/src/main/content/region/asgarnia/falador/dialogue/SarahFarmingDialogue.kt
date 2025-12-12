package content.region.asgarnia.falador.dialogue

import core.api.openNpcShop
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Sarah Farming dialogue.
 */
class SarahFarmingDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> npc(FaceAnim.HALF_GUILTY, "Hello. How can I help you?").also { stage++ }
            1 -> showTopics(
                Topic("What are you selling?", 9, true),
                Topic("Can you give me any Farming advice?", 2),
                Topic("Can you tell me how to use the loom?", 3),
                Topic("I'm okay, thank you.", END_DIALOGUE),
            )
            2 -> npc(FaceAnim.HALF_GUILTY, "Yes - ask a gardener.").also { stage = END_DIALOGUE }
            3 -> npcl(FaceAnim.FRIENDLY, "Well, it's actually my loom, but I don't mind you using it, if you like. You can use it to weave sacks and baskets in which you can put vegetables and fruit.").also { stage++ }
            4 -> showTopics(
                Topic("What do I need to weave sacks?", 5),
                Topic("What do I need to weave baskets?",6),
                Topic("Thank you, that's very kind.", END_DIALOGUE),
            )
            5 -> npcl(FaceAnim.HAPPY, "Well, the best sacks are made with jute fibres; you can grow jute yourself in a hops patch. I'd say about 4 jute fibres should be enough to weave a sack.").also { stage = 8 }
            6 -> npcl(FaceAnim.HAPPY,"Well, the best baskets are made with young branches cut from a willow tree. You'll need a very young willow tree; otherwise, the branches will have grown too thick to be able to weave. I suggest growing your own.").also { stage++ }
            7 -> npcl(FaceAnim.HAPPY, "You can cut the branches with a standard pair of secateurs. You will probably need about 6 willow branches to weave a complete basket.").also { stage++ }
            8 -> player(FaceAnim.HAPPY, "Thank you, that's very kind.").also { stage = END_DIALOGUE }
            9 -> {
                end()
                openNpcShop(player!!, NPCs.SARAH_2304)
            }
        }
    }
}
