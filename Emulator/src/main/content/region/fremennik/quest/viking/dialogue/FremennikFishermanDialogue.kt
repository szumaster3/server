package content.region.fremennik.quest.viking.dialogue

import core.api.*
import core.api.quest.isQuestComplete
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class FremennikFishermanDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.FISHERMAN_1302)
        when (stage) {
            0 -> {
                if (inInventory(player!!, Items.SEA_FISHING_MAP_3704, 1)) {
                    playerl(FaceAnim.HAPPY, "Here. I got you your map.")
                    stage = 15
                } else if (inInventory(player!!, Items.UNUSUAL_FISH_3703, 1)) {
                    playerl(FaceAnim.ASKING, "I don't see what's so special about this so called exotic fish.")
                    stage = 16
                } else if (getAttribute(player!!, "sigmundreturning", false)) {
                    playerl(FaceAnim.ASKING, "Is this trade item for you?")
                    stage = 17
                }
                if (getAttribute(player!!, "sigmund-steps", 0) == 8) {
                    playerl(
                        FaceAnim.ASKING,
                        "I don't suppose you have any idea where I could find a map of deep sea fishing spots do you?",
                    )
                    stage = 10
                } else if (getAttribute(player!!, "sigmund-steps", 0) == 7) {
                    playerl(
                        FaceAnim.ASKING,
                        "I don't suppose you have any idea where I could find an exotic and extremely odd fish, do you?",
                    )
                    stage = 1
                } else if (!isQuestComplete(player!!, Quests.THE_FREMENNIK_TRIALS)) {
                    player(FaceAnim.FRIENDLY, "Hello there.").also { stage = 200 }
                } else if (isQuestComplete(player!!, Quests.THE_FREMENNIK_TRIALS)) {
                    player(FaceAnim.FRIENDLY, "Hello there.").also { stage = 100 }
                }
            }

            1 ->
                npcl(
                    FaceAnim.AMAZED,
                    "Ah, so even outerlanders have heard of my amazing catch the other day!",
                ).also { stage++ }

            2 -> playerl(FaceAnim.THINKING, "You have it? Can I trade you something for it?").also { stage++ }
            3 ->
                npcl(
                    FaceAnim.HAPPY,
                    "As exotic looking as it is, it is bad eating. I will happily trade it if you can find me the secret map of the best fishing spots that the navigator has hidden away.",
                ).also {
                    stage++
                }

            4 -> playerl(FaceAnim.AMAZED, "Is that all?").also { stage++ }
            5 ->
                npcl(
                    FaceAnim.HAPPY,
                    "Indeed it is, outerlander. The only reason I sit out here in the cold all day long is so I don't have to pay his outrageous prices.",
                ).also { stage++ }

            6 ->
                npcl(
                    FaceAnim.HAPPY,
                    "By getting me his copy of that map, I will finally be self sufficient. I might even make a profit!",
                ).also { stage++ }

            7 ->
                playerl(FaceAnim.THINKING, "I'll see what I can do.").also {
                    player!!.incrementAttribute("sigmund-steps", 1)
                    stage = 1000
                }

            10 ->
                npcl(
                    FaceAnim.ANNOYED,
                    "You should pay attention when I speak! I already told you, that rip off navigator has it, and I want it!",
                ).also { stage = 1000 }

            15 ->
                npcl(
                    FaceAnim.HAPPY,
                    "Great work outerlander! With this, I can finally catch enough fish to make an honest living from it! Here, have the stupid fish.",
                ).also {
                    removeItem(player!!, Items.SEA_FISHING_MAP_3704)
                    addItemOrDrop(player!!, Items.UNUSUAL_FISH_3703, 1)
                    stage = 1000
                }

            16 -> npcl(FaceAnim.ANNOYED, "Me neither, outerlander. That is why I gave it to you.").also { stage = 1000 }
            17 -> npcl(FaceAnim.ANNOYED, "I don't think so.").also { stage = 1000 }
            100 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "Welcome back, " + (if (player!!.isMale) "brother " else "sister ") + "${
                        getAttribute(
                            player!!,
                            "fremennikname",
                            "fremmyname",
                        )
                    }.",
                    "It has been too long since last we spoke!",
                    "The fish are really biting today!",
                ).also { stage = END_DIALOGUE }

            200 ->
                npcl(
                    FaceAnim.ANNOYED,
                    "Don't talk to me outerlander. Anything you have to say to me, you can say to the Chieftain. Goodbye.",
                ).also { stage = END_DIALOGUE }

            1000 -> end()
        }
    }
}
