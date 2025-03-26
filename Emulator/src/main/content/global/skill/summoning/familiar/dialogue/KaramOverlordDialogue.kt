package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * The type Karam overlord dialogue.
 */
@Initializable
class KaramOverlordDialogue : Dialogue {
    override fun newInstance(player: Player): Dialogue {
        return KaramOverlordDialogue(player)
    }

    /**
     * Instantiates a new Karam overlord dialogue.
     */
    constructor()

    /**
     * Instantiates a new Karam overlord dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        when ((Math.random() * 4).toInt()) {
            0 -> {
                playerl(FaceAnim.FRIENDLY, "Do you want-")
                stage = 0
            }

            1 -> {
                npcl(FaceAnim.OLD_NORMAL, "Kneel before my awesome might!")
                stage = 10
            }

            2 -> {
                playerl(FaceAnim.HALF_ASKING, "...")
                stage = 19
            }

            3 -> {
                playerl(FaceAnim.FRIENDLY, "Errr...Have you FRIENDLYed down yet?")
                stage = 31
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                npcl(FaceAnim.OLD_NORMAL, "Silence!")
                stage++
            }

            1 -> {
                playerl(FaceAnim.FRIENDLY, "But I only...")
                stage++
            }

            2 -> {
                npcl(FaceAnim.OLD_NORMAL, "Silence!")
                stage++
            }

            3 -> {
                playerl(FaceAnim.FRIENDLY, "Now, listen here...")
                stage++
            }

            4 -> {
                npcl(FaceAnim.OLD_NORMAL, "SIIIIIILLLLLEEEEENCE!")
                stage++
            }

            5 -> {
                playerl(FaceAnim.FRIENDLY, "Fine!")
                stage++
            }

            6 -> {
                npcl(FaceAnim.OLD_NORMAL, "Good!")
                stage++
            }

            7 -> {
                playerl(FaceAnim.FRIENDLY, "But I only...")
                stage++
            }

            8 -> {
                playerl(FaceAnim.FRIENDLY, "Maybe I'll be so silent you'll think I never existed")
                stage++
            }

            9 -> {
                npcl(FaceAnim.OLD_NORMAL, "Oh, how I long for that day...")
                stage = END_DIALOGUE
            }

            10 -> {
                playerl(FaceAnim.FRIENDLY, "I would, but I have a bad knee you see...")
                stage++
            }

            11 -> {
                npcl(FaceAnim.OLD_NORMAL, "Your feeble prattlings matter not, air-breather! Kneel or face my wrath!")
                stage++
            }

            12 -> {
                playerl(FaceAnim.FRIENDLY, "I'm not afraid of you. You're only a squid in a bowl!")
                stage++
            }

            13 -> {
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Only? I, radiant in my awesomeness, am 'only' a squid in a bowl? Clearly you need to be shown in your place, lung-user!"
                )
                stage++
            }

            14 -> {
                sendDialogue("The Karamthulhu overlord narrows its eye and you find yourself unable to breathe!")
                stage++
            }

            15 -> {
                playerl(FaceAnim.FRIENDLY, "Gaak! Wheeeze!")
                stage++
            }

            16 -> {
                npcl(FaceAnim.OLD_NORMAL, "Who rules?")
                stage++
            }

            17 -> {
                playerl(FaceAnim.FRIENDLY, "You rule!")
                stage++
            }

            18 -> {
                npcl(FaceAnim.OLD_NORMAL, "And don't forget it!")
                stage = END_DIALOGUE
            }

            19 -> {
                npcl(FaceAnim.OLD_NORMAL, "The answer 'be silent'!")
                stage++
            }

            20 -> {
                playerl(FaceAnim.FRIENDLY, "You have no idea what I was going to ask you.")
                stage++
            }

            21 -> {
                npcl(FaceAnim.OLD_NORMAL, "Yes I do; I know all!")
                stage++
            }

            22 -> {
                playerl(FaceAnim.FRIENDLY, "You have no idea what I was going to ask you.")
                stage++
            }

            23 -> {
                npcl(FaceAnim.OLD_NORMAL, "Yes I do; I know all!")
                stage++
            }

            24 -> {
                playerl(
                    FaceAnim.FRIENDLY,
                    "Then you will not be surprised to know I was going to ask you what you wanted to do today."
                )
                stage++
            }

            25 -> {
                npcl(FaceAnim.OLD_NORMAL, "You dare doubt me!")
                stage++
            }

            26 -> {
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "The answer 'be silent' because your puny compressed brain could not even begin to comprehend my needs!"
                )
                stage++
            }

            27 -> {
                playerl(FaceAnim.FRIENDLY, "Well, how about I dismiss you so you can go and do what you like?")
                stage++
            }

            28 -> {
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Well, how about I topple your nations into the ocean and dance my tentacle-waving victory dance upon your watery graves?"
                )
                stage++
            }

            29 -> {
                playerl(FaceAnim.HALF_ASKING, "Yeah...well...")
                stage++
            }

            30 -> {
                npcl(FaceAnim.OLD_NORMAL, "Silence! Your burbling vexes me greatly!")
                stage = END_DIALOGUE
            }

            31 -> {
                npcl(FaceAnim.OLD_NORMAL, "FRIENDLYed down? Why would I need to FRIENDLY down?")
                stage++
            }

            32 -> {
                playerl(FaceAnim.FRIENDLY, "Well there is that whole 'god complex' thing...")
                stage++
            }

            33 -> {
                npcl(FaceAnim.OLD_NORMAL, "Complex? What 'complex' are you drooling about this time, minion?")
                stage++
            }

            34 -> {
                playerl(FaceAnim.FRIENDLY, "I don't really think sheep really make mewling noises...")
                stage++
            }

            35 -> {
                npcl(FaceAnim.OLD_NORMAL, "Silence!")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.KARAMTHULHU_OVERLORD_6809, NPCs.KARAMTHULHU_OVERLORD_6810)
    }
}
