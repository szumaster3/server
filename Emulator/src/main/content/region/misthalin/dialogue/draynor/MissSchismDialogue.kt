package content.region.misthalin.dialogue.draynor

import core.api.quest.isQuestComplete
import core.api.sendDialogueOptions
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class MissSchismDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HALF_GUILTY, "Oooh, my dear, have you heard the news?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                sendDialogueOptions(
                    player,
                    "What would you like to say?",
                    "Ok, tell me about the news.",
                    "Who are you?",
                    "I'm not talking to you, you horrible woman.",
                ).also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "Ok, tell me about the news.").also { stage++ }
                    2 -> player(FaceAnim.HALF_GUILTY, "Who are you?").also { stage = 15 }
                    3 -> player(FaceAnim.HALF_GUILTY, "I'm not talking to you, you horrible woman.").also { stage = 20 }
                }
            2 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Well, there's just to much to tell at once! What would",
                    "you like to hear first: the vampire or the bank?",
                ).also {
                    stage++
                }
            3 -> options("Tell me about the vampire.", "Tell me about the bank.").also { stage++ }
            4 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "Tell me about the vampire.").also { stage++ }
                    2 -> player(FaceAnim.HALF_GUILTY, "What about the bank?").also { stage = 10 }
                }
            5 ->
                if (isQuestComplete(player, Quests.VAMPIRE_SLAYER)) {
                    npc(FaceAnim.HALF_GUILTY, "Well, there's nothing to tell NOW. You killed it.").also { stage++ }
                } else {
                    npc("There is an evil Vampire terrorizing the city!").also { stage = 9 }
                }
            6 -> player(FaceAnim.HALF_GUILTY, "You could sound a little grateful.").also { stage++ }
            7 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I'm sure I could, but I don't see why. The vampire wasn't",
                    "bothering me.",
                ).also {
                    stage++
                }
            8 -> player(FaceAnim.HALF_GUILTY, "...").also { stage = END_DIALOGUE }
            9 -> player("Oh, that's not good.").also { stage = END_DIALOGUE }
            10 -> npc(FaceAnim.HALF_GUILTY, "It's terrible, absolutely terrible! Those poor people!").also { stage++ }
            11 -> player(FaceAnim.HALF_GUILTY, "Ok, yeah.").also { stage++ }
            12 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "And who'd have ever thought such a sweet old gentleman",
                    "would do such a thing?",
                ).also {
                    stage++
                }
            13 -> player(FaceAnim.HALF_GUILTY, "Are we talking about the bank robbery?").also { stage++ }
            14 -> npc(FaceAnim.HALF_GUILTY, "Oh yes, my dear. It was terrible! TERRIBLE!").also { stage = END_DIALOGUE }
            15 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I, my dear, am a concerned citizen of Draynor Village.",
                    "Ever since the Council allowed those farmers to set up",
                    "their stalls here, we've had a constant flow of thieves and",
                    "murderers through our fair village, and I decided that",
                ).also {
                    stage++
                }
            16 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "someone HAD to stand up and",
                    "keep an eye on the situation.",
                ).also { stage++ }
            17 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I also do voluntary work for the Draynor Manor",
                    "Restoration Fund. We're campaigning to have",
                    "Draynor manor turned into a museum before the wet-rot",
                    "destroys it completely.",
                ).also {
                    stage++
                }
            18 ->
                if (isQuestComplete(player, Quests.VAMPIRE_SLAYER)) {
                    player(
                        FaceAnim.HALF_GUILTY,
                        "Well, now that I've cleared the vampire out of the manor,",
                        "I guess you won't have too much trouble turning it into a",
                        "museum.",
                    ).also {
                        stage++
                    }
                } else {
                    end()
                }
            19 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "That's all very well dear, but no vampire was ever going to",
                    "stop me making it a museum.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            20 -> npc(FaceAnim.HALF_GUILTY, "Oooh.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return MissSchismDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MISS_SCHISM_2634)
    }
}
