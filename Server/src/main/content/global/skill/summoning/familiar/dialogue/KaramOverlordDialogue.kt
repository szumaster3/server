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
    private var branch: Int = -1

    override fun newInstance(player: Player?) = KaramOverlordDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        branch = (0..3).random()
        stage = 0

        when (branch) {
            0 -> playerl(FaceAnim.FRIENDLY, "Do you want-")
            1 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Kneel before my awesome might!")
            2 -> playerl(FaceAnim.HALF_ASKING, "...")
            3 -> playerl(FaceAnim.FRIENDLY, "Errr...Have you FRIENDLYed down yet?")
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> when (stage) {
                0 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Silence!"); stage++ }
                1 -> { playerl(FaceAnim.FRIENDLY, "But I only..."); stage++ }
                2 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Silence!"); stage++ }
                3 -> { playerl(FaceAnim.FRIENDLY, "Now, listen here..."); stage++ }
                4 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "SIIIIIILLLLLEEEEENCE!"); stage++ }
                5 -> { playerl(FaceAnim.FRIENDLY, "Fine!"); stage++ }
                6 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Good!"); stage++ }
                7 -> { playerl(FaceAnim.FRIENDLY, "But I only..."); stage++ }
                8 -> { playerl(FaceAnim.FRIENDLY, "Maybe I'll be so silent you'll think I never existed"); stage++ }
                9 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Oh, how I long for that day..."); stage = END_DIALOGUE }
            }

            1 -> when (stage) {
                0 -> { playerl(FaceAnim.FRIENDLY, "I would, but I have a bad knee you see..."); stage++ }
                1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Your feeble prattlings matter not, air-breather! Kneel or face my wrath!"); stage++ }
                2 -> { playerl(FaceAnim.FRIENDLY, "I'm not afraid of you. You're only a squid in a bowl!"); stage++ }
                3 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Only? I, radiant in my awesomeness, am 'only' a squid in a bowl? Clearly you need to be shown in your place, lung-user!"); stage++ }
                4 -> { sendDialogue("The Karamthulhu overlord narrows its eye and you find yourself unable to breathe!"); stage++ }
                5 -> { playerl(FaceAnim.FRIENDLY, "Gaak! Wheeeze!"); stage++ }
                6 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Who rules?"); stage++ }
                7 -> { playerl(FaceAnim.FRIENDLY, "You rule!"); stage++ }
                8 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "And don't forget it!"); stage = END_DIALOGUE }
            }

            2 -> when (stage) {
                0 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "The answer 'be silent'!"); stage++ }
                1 -> { playerl(FaceAnim.FRIENDLY, "You have no idea what I was going to ask you."); stage++ }
                2 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Yes I do; I know all!"); stage++ }
                3 -> { playerl(FaceAnim.FRIENDLY, "You have no idea what I was going to ask you."); stage++ }
                4 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Yes I do; I know all!"); stage++ }
                5 -> { playerl(FaceAnim.FRIENDLY, "Then you will not be surprised to know I was going to ask you what you wanted to do today."); stage++ }
                6 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "You dare doubt me!"); stage++ }
                7 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "The answer 'be silent' because your puny compressed brain could not even begin to comprehend my needs!"); stage++ }
                8 -> { playerl(FaceAnim.FRIENDLY, "Well, how about I dismiss you so you can go and do what you like?"); stage++ }
                9 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Well, how about I topple your nations into the ocean and dance my tentacle-waving victory dance upon your watery graves?"); stage++ }
                10 -> { playerl(FaceAnim.HALF_ASKING, "Yeah...well..."); stage++ }
                11 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Silence! Your burbling vexes me greatly!"); stage = END_DIALOGUE }
            }

            3 -> when (stage) {
                0 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "FRIENDLY'ed down? Why would I need to FRIENDLY down?"); stage++ }
                1 -> { playerl(FaceAnim.FRIENDLY, "Well there is that whole 'god complex' thing..."); stage++ }
                2 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Complex? What 'complex' are you drooling about this time, minion?"); stage++ }
                3 -> { playerl(FaceAnim.FRIENDLY, "I don't really think sheep really make mewling noises..."); stage++ }
                4 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Silence!"); stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.KARAMTHULHU_OVERLORD_6809, NPCs.KARAMTHULHU_OVERLORD_6810)
}
