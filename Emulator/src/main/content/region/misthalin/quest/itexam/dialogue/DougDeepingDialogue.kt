package content.region.misthalin.quest.itexam.dialogue

import core.api.addItemOrDrop
import core.api.inInventory
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class DougDeepingDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        playerl(FaceAnim.FRIENDLY, "Hello.").also { stage = 1 }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            1 -> npcl(FaceAnim.FRIENDLY, "Well, well... I have a visitor. What are you doing here?").also { stage++ }
            2 ->
                options(
                    "I have been invited to research here",
                    "I'm not really sure.",
                    "I'm here to get rich, rich, rich!",
                    "How could I move a large pile of rocks?",
                ).also { stage++ }

            3 ->
                when (buttonId) {
                    1 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Indeed, you must be someone special to be allowed down here.",
                        ).also { stage++ }

                    2 -> npcl(FaceAnim.FRIENDLY, "A miner without a clue - how funny!").also { stage = END_DIALOGUE }
                    3 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Oh, well, don't forget that wealth and riches aren't everything.",
                        ).also { stage = END_DIALOGUE }

                    4 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "There used to be this chap that worked in the other shaft. He was working on an explosive chemical mixture to be used for clearing blocked areas underground.",
                        ).also { stage = 5 }
                }

            4 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "He left in a hurry one day. Apparently, something in the shaft scared him to death, but he didn't say what.",
                ).also { stage++ }

            5 -> playerl(FaceAnim.FRIENDLY, "Oh?").also { stage++ }
            6 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Rumour has it he'd been writing a book on his chemical mixture. I'm not sure what was in it, but he left in such a hurry, he probably left something behind in the other dig shaft.",
                ).also { stage++ }

            7 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "In fact, I still have a chest key he gave me to look after - perhaps it's more useful to you.",
                ).also { stage++ }

            8 -> {
                end()
                if (inInventory(player, Items.CHEST_KEY_709)) {
                    playerl(FaceAnim.FRIENDLY, "It's okay, I already have one.")
                } else {
                    addItemOrDrop(player, Items.CHEST_KEY_709)
                    playerl(FaceAnim.FRIENDLY, "Thanks for the key!")
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.DOUG_DEEPING_614)
}
