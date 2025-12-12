package content.region.asgarnia.port_sarim.dialogue

import core.api.isQuestComplete
import core.api.setTitle
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.tools.END_DIALOGUE
import shared.consts.Quests

class LongbowBenDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        if (player == null || npc == null) return

        when (stage) {
            0 -> npc(FaceAnim.HALF_GUILTY, "Arrr, matey!").also { stage++ }
            1 -> {
                setTitle(player!!, if(!isQuestComplete(player!!, Quests.PIRATES_TREASURE)) 3 else 2)
                showTopics(
                    IfTopic(FaceAnim.NEUTRAL, "I'm looking for Redbeard Frank.", 2, !isQuestComplete(player!!, Quests.PIRATES_TREASURE)),
                    Topic(FaceAnim.ASKING, "Why are you called Longbow Ben?", 5),
                    Topic(FaceAnim.ASKING, "Have you got any quests I could do?", 11)
                )
            }
            2 -> player(FaceAnim.FRIENDLY, "I'm looking for Redbeard Frank.").also { stage++ }
            3 -> npc(FaceAnim.THINKING, "Redbeard Frank ye say? He be outside. Says he likes the feel of the wind on his cheeks.").also { stage++ }
            4 -> player(FaceAnim.HAPPY,"Thanks.").also { stage = END_DIALOGUE }
            5 -> npc(FaceAnim.HALF_GUILTY,"Arrr, that's a strange yarn.").also { stage++ }
            6 -> npc(FaceAnim.HALF_GUILTY, "I was to be marooned, ye see. A scurvy troublemaker had", "taken my ship, and he put me ashore on a little island.").also { stage++ }
            7 -> player(FaceAnim.HALF_GUILTY, "Gosh, how did you escape?").also { stage++ }
            8 -> npc(FaceAnim.HALF_GUILTY, "Arrr, ye see, he made one mistake! Before he sailed", "away, he gave me a bow and one arrow so that I wouldn't have", "to die slowly.").also { stage++ }
            9 -> npc(FaceAnim.NEUTRAL, "So I shot him and took my ship back.").also { stage++ }
            10 -> player(FaceAnim.HALF_GUILTY, "Right...").also { stage = 1 }
            11 -> {
                if (isQuestComplete(player!!, Quests.PIRATES_TREASURE) && !isQuestComplete(player!!, Quests.GOBLIN_DIPLOMACY)) {
                    npc(FaceAnim.NEUTRAL, "Nay, I've got nothing for ye to do.").also { stage = 13 }
                } else {
                    npc(FaceAnim.HALF_GUILTY, "Nay, but the barkeep hears most of the news around here.").also { stage++ }
                }
            }
            12 -> {
                if (isQuestComplete(player!!, Quests.PIRATES_TREASURE) && !isQuestComplete(player!!, Quests.GOBLIN_DIPLOMACY)) {
                    npc(FaceAnim.NEUTRAL, "Perhaps ye should be asking him for a quest.").also { stage = 14 }
                } else {
                    npc(FaceAnim.HALF_GUILTY, "Or Redbeard Frank, he's often spoken of buried treasure.", "Perhaps ye should be asking them for quests.").also { stage = 14 }
                }
            }
            13 -> npc(FaceAnim.THINKING, "But I hear there's an old landlubber in Draynor Village who's always", "a-looking for a lively ${if (player!!.isMale) "lad" else "lass"} to do him a favour.").also { stage++ }
            14 -> player(FaceAnim.NOD_YES, "Thanks.").also { stage = END_DIALOGUE }
        }
    }
}
