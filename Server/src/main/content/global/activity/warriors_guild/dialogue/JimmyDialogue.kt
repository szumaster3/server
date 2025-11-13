package content.global.activity.warriors_guild.dialogue

import core.api.sendDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Jimmy dialogue.
 */
@Initializable
class JimmyDialogue(player: Player? = null) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.DRUNK, "'Ello there.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> showTopics(
                Topic("Tell me about this room.", 1, false),
                Topic("Tell me how to balance kegs.", 8, false),
                Topic("What's with the multicoloured furniture?", 14, false),
                Topic("Bye!", 20, false),
            )
            1 -> npcl(FaceAnim.DRUNK, "Well... s'like thish... Thish here'sh a shtore room right?").also { stage++ }
            2 -> playerl(FaceAnim.HALF_THINKING, "A store roorr. you mean?").also { stage++ }
            3 -> npcl(FaceAnim.DRUNK, "That'sh what I shaid! *HIC* A shtore room.... Now technic'ly shpeaking, I should be outshide guarding it...").also { stage++ }
            4 -> playerl(FaceAnim.HALF_THINKING, "But you just nipped in to have a quick drink?").also { stage++ }
            5 -> npcl(FaceAnim.DRUNK, "Yep... and to practish.").also { stage++ }
            6 -> playerl(FaceAnim.HALF_THINKING, "Practish? I mean.. practise what?").also { stage++ }
            7 -> npcl(FaceAnim.DRUNK, "Keg balancin. I'm the besht.").also { stage = 0 }

            8 -> npcl(FaceAnim.DRUNK, "Yer very very shtrange. But.... you pick the keg up, and balance it on yer head, then you pick another keg up and put that on top. S'really very eashy.").also { stage++ }
            9 -> playerl(FaceAnim.HALF_THINKING, "Eashy?").also { stage++ }
            10 -> npcl(FaceAnim.DRUNK, "Yesh. Eashy.").also { stage++ }
            11 -> sendDialogue(player, "Jimmy pauses for a short moment, causing the dialogue bar to briefly disappear, before continuing.").also { stage++ }
            12 -> npcl(FaceAnim.DRUNK, "But you couldn't ever balansh ash many ash meee!").also { stage++ }
            13 -> playerl(FaceAnim.NOD_YES, "That sounds like a challenge, I'll show you!").also { stage = 0 }

            14 -> npcl(FaceAnim.DRUNK, "Keg tag. S'my invenshun.").also { stage++ }
            15 -> playerl(FaceAnim.HALF_THINKING, "Keg tag? How does that work?").also { stage++ }
            16 -> npcl(FaceAnim.DRUNK, "Eashy. Balance kegsh on yer head, then shee if you can tag the coloured shtuff in order, before you loshe control of yer kegsh.").also { stage++ }
            17 -> playerl(FaceAnim.HALF_THINKING, "So, I put some kegs on my head, then go around tagging each of the coloured furniture in order. Sounds easy! What do I get?").also { stage++ }
            18 -> npcl(FaceAnim.DRUNK, "Well...I could teash yer shumfin' abou' strength.").also { stage++ }
            19 -> playerl(FaceAnim.NEUTRAL, "Knowledge in strength. Hmm, could be useful.").also { stage = 0 }

            20 -> npcl(FaceAnim.DRUNK, "Shure you wouldn't like an ickle drinkie fore yer go?").also { stage++ }
            21 -> player("No thanks, got things to do, people to see, tokens", "to earn...").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.JIMMY_4298)
}
