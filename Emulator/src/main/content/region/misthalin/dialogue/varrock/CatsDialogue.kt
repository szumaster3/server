package content.region.misthalin.dialogue.varrock

import core.api.inEquipment
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class CatsDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (isQuestComplete(player, Quests.ICTHLARINS_LITTLE_HELPER) &&
            inEquipment(
                player,
                Items.CATSPEAK_AMULET_4677,
            )
        ) {
            playerl(
                FaceAnim.HALF_GUILTY,
                "Did you understand what went on in that quest with the devourer and Icthlarin?",
            ).also { stage = 0 }
        } else {
            npcl(FaceAnim.CHILD_NEUTRAL, "Meow!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npcl(FaceAnim.CHILD_NEUTRAL, "It wasn't all that difficult to understand.").also { stage++ }
            1 -> playerl(FaceAnim.HALF_GUILTY, "Really? I was, no actually am, still confused.").also { stage++ }
            2 -> npcl(FaceAnim.CHILD_NEUTRAL, "Ok, I'll tell you what I think happened.").also { stage++ }
            3 -> playerl(FaceAnim.HALF_GUILTY, "Great! Story time.").also { stage++ }
            4 ->
                npcl(
                    FaceAnim.CHILD_NEUTRAL,
                    "There once was a high priest of some crazy religion called Klenter.",
                ).also { stage++ }

            5 ->
                playerl(
                    FaceAnim.HALF_GUILTY,
                    "What, a religion called Klenter? What kind of name is that?",
                ).also { stage++ }

            6 ->
                npcl(
                    FaceAnim.CHILD_NEUTRAL,
                    "No no. The high priest was called Klenter and he was the high priest of Icthlarin, god of the dead.",
                ).also { stage++ }

            7 -> playerl(FaceAnim.HALF_GUILTY, "Ok, I'm with you that far.").also { stage++ }
            8 ->
                npcl(
                    FaceAnim.CHILD_NEUTRAL,
                    "He dies, and a struggle starts for his soul. Another god the Devourer wants to destroy his soul because she has some dispute with Icthlarin.",
                ).also { stage++ }

            9 -> npcl(FaceAnim.CHILD_NEUTRAL, "So she takes on the guise of a human.").also { stage++ }
            10 -> playerl(FaceAnim.HALF_GUILTY, "Ah the wanderer.").also { stage++ }
            11 ->
                npcl(
                    FaceAnim.CHILD_NEUTRAL,
                    "Very good. Now she needs help in getting Klenter's soul so she tricks some gullible fool into helping her.",
                ).also { stage++ }

            12 -> playerl(FaceAnim.HALF_GUILTY, "So who was that?").also { stage++ }
            13 -> npcl(FaceAnim.CHILD_NEUTRAL, "You!").also { stage++ }
            14 ->
                playerl(
                    FaceAnim.HALF_GUILTY,
                    "Oh. So why doesn't she have a high priest too like Icthlarin.",
                ).also { stage++ }

            15 ->
                npcl(
                    FaceAnim.CHILD_NEUTRAL,
                    "Well she's the god of destruction. If she had a priest or a temple or anything she would just destroy it.",
                ).also { stage++ }

            16 ->
                playerl(
                    FaceAnim.HALF_GUILTY,
                    "So if she's so powerful why does she need this gullible fool's help?",
                ).also { stage++ }

            17 -> npcl(FaceAnim.CHILD_NEUTRAL, "Because of my kind.").also { stage++ }
            18 -> playerl(FaceAnim.HALF_GUILTY, "Your kind?").also { stage++ }
            19 -> npcl(FaceAnim.CHILD_NEUTRAL, "Cats!").also { stage++ }
            20 ->
                playerl(
                    FaceAnim.HALF_GUILTY,
                    "What? Why would she care about you? All you ever want is a bit of attention and the odd fish or two.",
                ).also { stage++ }

            21 ->
                npcl(
                    FaceAnim.CHILD_NEUTRAL,
                    "We have other powers, not clear to you. Anyway I'm moving away from the story. The wanderer recruits your help by hypnotising you. Do you remember that?",
                ).also { stage++ }

            22 ->
                playerl(
                    FaceAnim.HALF_GUILTY,
                    "Vaguely, I think. Why did she choose me to help her then.",
                ).also { stage++ }

            23 -> npcl(FaceAnim.CHILD_NEUTRAL, "Because of me.").also { stage++ }
            24 -> playerl(FaceAnim.HALF_GUILTY, "I don't understand.").also { stage++ }
            25 ->
                npcl(
                    FaceAnim.CHILD_NEUTRAL,
                    "Cats are the only things that can open the pyramid's door, so she needed an adventurer with one.",
                ).also { stage++ }

            26 ->
                npcl(
                    FaceAnim.CHILD_NEUTRAL,
                    "So you entered the pyramid with me under her mind control and stole a canopic jar containing an organ belonging to Klenter.",
                ).also { stage++ }

            27 ->
                playerl(
                    FaceAnim.HALF_GUILTY,
                    "Ahh I think I'm beginning to get a better understanding.",
                ).also { stage++ }

            28 ->
                npcl(
                    FaceAnim.CHILD_NEUTRAL,
                    "You then started to return with the jar to the Devourer, but as a sting on the tail she made you plant one of her symbols in the ceremonial room of the pyramid.",
                ).also { stage++ }

            29 ->
                npcl(
                    FaceAnim.CHILD_NEUTRAL,
                    "You then tried to flee the pyramid but Icthlarin appeared just as you reached the exit.",
                ).also { stage++ }

            30 -> playerl(FaceAnim.HALF_GUILTY, "The guy with the head of a dog?").also { stage++ }
            31 ->
                npcl(
                    FaceAnim.CHILD_NEUTRAL,
                    "Yes Player. Well he either broke the devourer's hold on you or else Klenter did.",
                ).also { stage++ }

            32 -> playerl(FaceAnim.HALF_GUILTY, "Klenter? I thought he was dead.").also { stage++ }
            33 ->
                npcl(
                    FaceAnim.CHILD_NEUTRAL,
                    "He was, and still is. Ok, Icthlarin summoned Klenter's soul to torment you into returning the jar, the end result was that you were freed from the devourer's grasp.",
                ).also { stage++ }

            34 ->
                playerl(
                    FaceAnim.HALF_GUILTY,
                    "So that's when I woke up with that jar in my inventory and had that intolerable ghost harassing me.",
                ).also { stage++ }

            35 ->
                npcl(
                    FaceAnim.CHILD_NEUTRAL,
                    "You then bumbled around for a bit and returned the jar, discovering that the high priest still hadn't completed the final ceremony so you got him all the bits and pieces.",
                ).also { stage++ }

            36 ->
                playerl(
                    FaceAnim.HALF_GUILTY,
                    "So then I remembered about the devourer's symbol which I placed in the ceremonial room and had to rush back and warn the priests about the devourer.",
                ).also { stage++ }

            37 -> npcl(FaceAnim.CHILD_NEUTRAL, "And the rest is simple enough to piece together.").also { stage++ }
            38 ->
                playerl(
                    FaceAnim.HALF_GUILTY,
                    "Thanks cat, you know you're quite smart for a fish eating animated ball of fluff.",
                ).also { stage++ }

            39 -> npcl(FaceAnim.CHILD_NEUTRAL, "You say the sweetest things. Hiss.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return CatsDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(
            NPCs.KITTEN_761,
            NPCs.KITTEN_762,
            NPCs.KITTEN_763,
            NPCs.KITTEN_764,
            NPCs.KITTEN_765,
            NPCs.KITTEN_766,
        )
    }
}
