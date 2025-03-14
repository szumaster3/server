package content.region.misthalin.quest.crest.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class BootDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = (args[0] as NPC).getShownNPC(player)
        val qstage = player?.questRepository?.getStage(Quests.FAMILY_CREST) ?: -1

        if (qstage < 14 || qstage > 14) {
            npc(FaceAnim.OLD_NORMAL, "Hello tall person.")
            stage = 1
        } else {
            npc(FaceAnim.OLD_NORMAL, "Hello tall person.")
            stage = 2
        }

        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            1 -> options("Hello short person.", "Why are you called Boot?").also { stage = 10 }
            2 ->
                options(
                    "Hello. I'm in search of very high quality gold.",
                    "Hello short person.",
                    "Why are you called Boot?",
                ).also { stage = 20 }

            10 ->
                when (buttonId) {
                    1 -> player("Hello short person.").also { stage = 1000 }
                    2 ->
                        npc(
                            FaceAnim.OLD_ANGRY1,
                            "I'm called Boot, because when I was very young, ",
                            "I used to sleep, in a large boot.",
                        ).also { stage++ }
                }

            11 -> player("Yeah, great, I didn't want your life story.").also { stage = 1000 }
            20 ->
                when (buttonId) {
                    1 ->
                        npc(
                            FaceAnim.OLD_DEFAULT,
                            "High quality gold eh? Hmmm... ",
                            "Well, the very best quality gold that I know of ",
                            "can be found in an underground ruin near Witchaven.",
                        ).also { stage++ }

                    2 -> player("Hello short person.").also { stage = 1000 }
                    3 ->
                        npc(
                            FaceAnim.OLD_ANGRY1,
                            "I'm called Boot, because when I was very young, ",
                            "I used to sleep, in a large boot.",
                        ).also { stage = 11 }
                }

            21 ->
                npc("I don't believe it's exactly easy to get to though...").also {
                    stage = 1000
                    player.questRepository.getQuest(Quests.FAMILY_CREST).setStage(player, 15)
                }

            1000 -> end()
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BOOT_665)
    }
}
