package content.region.kandarin.dialogue.witchaven

import content.region.kandarin.handlers.FishingPlatform
import content.region.kandarin.quest.seaslug.dialogue.HolgartDialogueFile
import core.api.openDialogue
import core.api.quest.isQuestComplete
import core.api.quest.isQuestInProgress
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class HolgartDialogue(
    player: Player? = null,
) : Dialogue(player) {
    fun gender(
        male: String = "Sir",
        female: String = "Madam",
    ) = if (player.isMale) male else female

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        when {
            isQuestComplete(player, Quests.SEA_SLUG) -> player("Hello again Holgart.").also { stage = 4 }
            isQuestInProgress(player, Quests.SEA_SLUG, 2, 99) ->
                end().also {
                    openDialogue(
                        player,
                        HolgartDialogueFile(),
                    )
                }

            else -> player(FaceAnim.FRIENDLY, "Hello there.")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.FRIENDLY, "Well hello good " + gender() + ", beautiful day isn't it?").also { stage++ }
            1 -> player(FaceAnim.FRIENDLY, "Not bad I suppose.").also { stage++ }
            2 -> npc(FaceAnim.FRIENDLY, "Just smell that sea air... beautiful.").also { stage++ }
            3 -> player(FaceAnim.FRIENDLY, "Hmm... lovely...").also { stage = END_DIALOGUE }
            4 ->
                npcl(
                    FaceAnim.HALF_ASKING,
                    "Well hello again m'hearty. Your land loving legs getting bored? Fancy some cold wet underfoot?",
                ).also {
                    stage++
                }
            5 -> player(FaceAnim.FRIENDLY, "Pardon?").also { stage++ }
            6 -> npc(FaceAnim.FRIENDLY, "Fancy going out to sea?").also { stage++ }
            7 -> options("I'll come back later.", "Okay, let's do it.").also { stage++ }
            8 ->
                when (buttonId) {
                    1 -> player(FaceAnim.FRIENDLY, "I'll come back later.").also { stage++ }
                    2 -> player(FaceAnim.FRIENDLY, "Okay, let's do it.").also { stage = 10 }
                }
            9 -> npc(FaceAnim.FRIENDLY, "Okay then. I'll wait here for you.").also { stage++ }
            10 -> npc(FaceAnim.FRIENDLY, "Hold on tight!").also { stage++ }
            11 -> {
                end()
                FishingPlatform.sail(player, FishingPlatform.Travel.WITCHAVEN_TO_FISHING_PLATFORM)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return HolgartDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HOLGART_698)
    }
}
