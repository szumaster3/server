package content.region.asgarnia.dialogue.portsarim

import core.api.quest.isQuestComplete
import core.api.sendDialogueOptions
import core.api.setTitle
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class LongbowBenDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HALF_GUILTY, "Arrr, matey!")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                setTitle(player, 2)
                if (!isQuestComplete(player, Quests.PIRATES_TREASURE)) {
                    sendDialogueOptions(
                        player,
                        "What would you like to say?",
                        "I'm looking for Redbeard Frank.",
                        "Why are you called Longbow Ben?",
                        "Have you got any quests I could do?",
                    ).also {
                        stage++
                    }
                } else {
                    sendDialogueOptions(
                        player,
                        "What would you like to say?",
                        "Why are you called Longbow Ben?",
                        "Have you got any quests I could do?",
                    ).also {
                        stage++
                    }
                }
            }
            1 ->
                if (!isQuestComplete(player, Quests.PIRATES_TREASURE)) {
                    when (buttonId) {
                        1 -> player("I'm looking for Redbeard Frank.").also { stage++ }
                        2 -> player(FaceAnim.HALF_GUILTY, "Why are you called Longbow Ben?").also { stage = 4 }
                        3 -> player(FaceAnim.HALF_GUILTY, "Have you got any quests I could do?").also { stage = 10 }
                    }
                } else {
                    when (buttonId) {
                        1 -> player(FaceAnim.HALF_GUILTY, "Why are you called Longbow Ben?").also { stage = 4 }
                        2 -> player(FaceAnim.HALF_GUILTY, "Have you got any quests I could do?").also { stage = 10 }
                    }
                }
            2 ->
                npcl(
                    FaceAnim.THINKING,
                    "Redbeard Frank ye say? He be outside. Says he likes the feel of the wind on his cheeks.",
                ).also {
                    stage++
                }
            3 -> player("Thanks.").also { stage = END_DIALOGUE }
            4 -> npc("Arrr, that's a strange yarn.").also { stage++ }
            5 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I was to be marooned, ye see. A scurvy troublemaker had",
                    "taken my ship, and he put me ashore on a little island.",
                ).also {
                    stage++
                }
            6 -> player(FaceAnim.HALF_GUILTY, "Gosh, how did you escape?").also { stage++ }
            7 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Arrr, ye see, he made one mistake! Before he sailed",
                    "away, he gave me a bow and one arrow so that I wouldn't have",
                    "to die slowly.",
                ).also {
                    stage++
                }
            8 -> npc(FaceAnim.NEUTRAL, "So I shot him and took my ship back.").also { stage++ }
            9 -> player(FaceAnim.HALF_GUILTY, "Right...").also { stage = 0 }
            10 ->
                if (isQuestComplete(player, Quests.PIRATES_TREASURE) &&
                    !isQuestComplete(player, Quests.GOBLIN_DIPLOMACY)
                ) {
                    npcl(FaceAnim.NEUTRAL, "Nay, I've got nothing for ye to do.").also { stage = 12 }
                } else {
                    npcl(
                        FaceAnim.HALF_GUILTY,
                        "Nay, but the barkeep hears most of the news around here.",
                    ).also { stage++ }
                }
            11 ->
                when {
                    isQuestComplete(player, Quests.PIRATES_TREASURE) &&
                        !isQuestComplete(player, Quests.GOBLIN_DIPLOMACY) ->
                        npcl(FaceAnim.NEUTRAL, "Perhaps ye should be asking him for a quest.").also {
                            stage =
                                13
                        }
                    else ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "Or Redbeard Frank, he's often spoken of buried treasure. Perhaps ye should be asking them for quests.",
                        ).also {
                            stage =
                                13
                        }
                }
            12 ->
                npcl(
                    FaceAnim.THINKING,
                    "But I hear there's an old landlubber in Draynor Village who's always a-looking for a lively ${if (player.isMale) "lad" else "lass"} to do him a favour.",
                ).also {
                    stage++
                }
            13 -> player(FaceAnim.NOD_YES, "Thanks.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LONGBOW_BEN_2691)
    }
}
