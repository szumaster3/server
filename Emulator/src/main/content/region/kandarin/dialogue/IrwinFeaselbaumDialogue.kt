package content.region.kandarin.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class IrwinFeaselbaumDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.HALF_GUILTY, "Can I help you?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("What do you do here?", "No, thanks.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player("What do you do here?").also { stage++ }
                    2 -> player("No, thanks.").also { stage = 15 }
                }
            2 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "I help the main man over there. He can get a bit cantankerous. Every now and then he'll just start attacking people without warning. I act as a sort of home help...",
                ).also {
                    stage++
                }
            3 -> player("How do you mean, home help?").also { stage++ }
            4 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Well, sometimes he'll fly right off the handle and just start, you know, 'raising the dead' to do his 'bidding'.",
                ).also {
                    stage++
                }
            5 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "That kind of activity is just so anti-social, so it tends to attract a lot of hero types, you know!",
                ).also {
                    stage++
                }
            6 -> player("Yes, yes, I know the sort. Full of themselves, aren't they?").also { stage++ }
            7 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Exactly! They come in shouting 'Have at thee!' and 'Death to you, evil one!' It's all a bit too dramatic for me, to be honest.",
                ).also {
                    stage++
                }
            8 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Anyway, he'll often end up getting killed, then nothing gets done around the house while he's awaiting his mortal return.",
                ).also {
                    stage++
                }
            9 -> player("Mortal return? Sounds interesting.").also { stage++ }
            10 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Well, it's kind of interesting the first few times, but it's like falling off a bike...",
                ).also {
                    stage++
                }
            11 -> player("What, you graze your knees a lot?").also { stage++ }
            12 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "I was going to say that you soon get fed up with it. I guess it's one of those perks of the job, though, the ability to raise yourself from the dead.",
                ).also {
                    stage++
                }
            13 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "You'd think he'd do better things with a gift like that, but all he does is get angry at people who come into his house without being invited, and then he's off again 'raising the dead'.",
                ).also {
                    stage++
                }
            14 -> player("Thank you.").also { stage = END_DIALOGUE }
            15 ->
                npcl(FaceAnim.HALF_GUILTY, "Okay. Please don't upset the boss while you're here.").also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return IrwinFeaselbaumDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.IRWIN_FEASELBAUM_2066)
    }
}

class IrwinFeaselbaumAboutTornPagesDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.IRWIN_FEASELBAUM_2066)
        when (stage) {
            0 ->
                playerl(
                    FaceAnim.HALF_ASKING,
                    "I found this torn page, do you know anything about it?",
                ).also { stage++ }
            1 ->
                npcl(
                    "Hey...I hope you didn't rip that page out of one of those books, they'll be trouble if it was.",
                ).also {
                    stage++
                }
            2 -> playerl("No, it wasn't actually, I got it from somewhere else, do you recognise it?").also { stage++ }
            3 ->
                npcl(
                    "Hmm, well it certainly looks like text from one of the books. Have you checked at the wizards' guild in Yanille? We sometime loan books to them.",
                ).also {
                    stage = END_DIALOGUE
                }
        }
    }
}

class IrwinFeaselbaumAboutNecromancyBookDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.IRWIN_FEASELBAUM_2066)
        when (stage) {
            0 -> playerl(FaceAnim.HALF_ASKING, "Hi, do you know anything about this book?").also { stage++ }
            1 ->
                npcl(
                    "Hey...that book has been loaned to the wizards' guild in Yanille, I hope you're not returning it in a damaged condition!",
                ).also {
                    stage++
                }
            2 ->
                playerl(
                    "Well, I was just wondering where the book came from actually and try to get some more information about it.",
                ).also {
                    stage++
                }
            3 ->
                npcl(
                    "Well, that book looks as if it's been through the wars...you're not going to return it in that condition. You take it back to the wizards at Yanille, they borrowed it, so it's their responsibility.",
                ).also {
                    stage = END_DIALOGUE
                }
        }
    }
}
