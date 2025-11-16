package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Karam Overlord familiar dialogues.
 */
@Initializable
class KaramOverlordDialogue : Dialogue {
    private var branch: Int = 0

    override fun newInstance(player: Player?) = KaramOverlordDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        branch = (0..3).random()
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> when (stage) {
                0 -> { playerl(FaceAnim.FRIENDLY, "Do you want-"); stage++ }
                1 -> { npcl(FaceAnim.OLD_NORMAL, "Silence!"); stage++ }
                2 -> { playerl(FaceAnim.FRIENDLY, "But I only..."); stage++ }
                3 -> { npcl(FaceAnim.OLD_NORMAL, "Silence!"); stage++ }
                4 -> { playerl(FaceAnim.FRIENDLY, "Now, listen here..."); stage++ }
                5 -> { npcl(FaceAnim.OLD_NORMAL, "SIIIIIILLLLLEEEEENCE!"); stage++ }
                6 -> { playerl(FaceAnim.FRIENDLY, "Fine!"); stage++ }
                7 -> { npcl(FaceAnim.OLD_NORMAL, "Good!"); stage++ }
                8 -> { playerl(FaceAnim.FRIENDLY, "But I only..."); stage++ }
                9 -> { playerl(FaceAnim.FRIENDLY, "Maybe I'll be so silent you'll think I never existed"); stage++ }
                10 -> { npcl(FaceAnim.OLD_NORMAL, "Oh, how I long for that day..."); stage = END_DIALOGUE }
            }

            1 -> when (stage) {
                0 -> { npcl(FaceAnim.OLD_NORMAL, "Kneel before my awesome might!"); stage++ }
                1 -> { playerl(FaceAnim.FRIENDLY, "I would, but I have a bad knee you see..."); stage++ }
                2 -> { npcl(FaceAnim.OLD_NORMAL, "Your feeble prattlings matter not, air-breather! Kneel or face my wrath!"); stage++ }
                3 -> { playerl(FaceAnim.FRIENDLY, "I'm not afraid of you. You're only a squid in a bowl!"); stage++ }
                4 -> { npcl(FaceAnim.OLD_NORMAL, "Only? I, radiant in my awesomeness, am 'only' a squid in a bowl? Clearly you need to be shown in your place, lung-user!"); stage++ }
                5 -> { sendDialogue("The Karamthulhu overlord narrows its eye and you find yourself unable to breathe!"); stage++ }
                6 -> { playerl(FaceAnim.FRIENDLY, "Gaak! Wheeeze!"); stage++ }
                7 -> { npcl(FaceAnim.OLD_NORMAL, "Who rules?"); stage++ }
                8 -> { playerl(FaceAnim.FRIENDLY, "You rule!"); stage++ }
                9 -> { npcl(FaceAnim.OLD_NORMAL, "And don't forget it!"); stage = END_DIALOGUE }
            }

            2 -> when (stage) {
                0 -> { playerl(FaceAnim.HALF_ASKING, "..."); stage++ }
                1 -> { npcl(FaceAnim.OLD_NORMAL, "The answer 'be silent'!"); stage++ }
                2 -> { playerl(FaceAnim.FRIENDLY, "You have no idea what I was going to ask you."); stage++ }
                3 -> { npcl(FaceAnim.OLD_NORMAL, "Yes I do; I know all!"); stage++ }
                4 -> { playerl(FaceAnim.FRIENDLY, "You have no idea what I was going to ask you."); stage++ }
                5 -> { npcl(FaceAnim.OLD_NORMAL, "Yes I do; I know all!"); stage++ }
                6 -> { playerl(FaceAnim.FRIENDLY, "Then you will not be surprised to know I was going to ask you what you wanted to do today."); stage++ }
                7 -> { npcl(FaceAnim.OLD_NORMAL, "You dare doubt me!"); stage++ }
                8 -> { npcl(FaceAnim.OLD_NORMAL, "The answer 'be silent' because your puny compressed brain could not even begin to comprehend my needs!"); stage++ }
                9 -> { playerl(FaceAnim.FRIENDLY, "Well, how about I dismiss you so you can go and do what you like?"); stage++ }
                10 -> { npcl(FaceAnim.OLD_NORMAL, "Well, how about I topple your nations into the ocean and dance my tentacle-waving victory dance upon your watery graves?"); stage++ }
                11 -> { playerl(FaceAnim.HALF_ASKING, "Yeah...well..."); stage++ }
                12 -> { npcl(FaceAnim.OLD_NORMAL, "Silence! Your burbling vexes me greatly!"); stage = END_DIALOGUE }
            }

            3 -> when (stage) {
                0 -> { playerl(FaceAnim.FRIENDLY, "Errr...Have you FRIENDLYed down yet?"); stage++ }
                1 -> { npcl(FaceAnim.OLD_NORMAL, "FRIENDLY'ed down? Why would I need to FRIENDLY down?"); stage++ }
                2 -> { playerl(FaceAnim.FRIENDLY, "Well there is that whole 'god complex' thing..."); stage++ }
                3 -> { npcl(FaceAnim.OLD_NORMAL, "Complex? What 'complex' are you drooling about this time, minion?"); stage++ }
                4 -> { playerl(FaceAnim.FRIENDLY, "I don't really think sheep really make mewling noises..."); stage++ }
                5 -> { npcl(FaceAnim.OLD_NORMAL, "Silence!"); stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.KARAMTHULHU_OVERLORD_6809, NPCs.KARAMTHULHU_OVERLORD_6810)
}
