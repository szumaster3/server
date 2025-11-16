package content.global.skill.summoning.familiar.dialogue.spirit

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Spirit Jelly familiar dialogue.
 */
@Initializable
class SpiritJellyDialogue : Dialogue {

    private var branch = -1

    override fun newInstance(player: Player?): Dialogue = SpiritJellyDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        branch = (Math.random() * 4).toInt()
        stage = when (branch) {
            0 -> 0   // "Play play play"
            1 -> 4   // "It's playtime now"
            2 -> 8   // "Go over there please"
            3 -> 14  // "What game are we playing"
            else -> 0
        }

        when (branch) {
            0 -> npcl(FaceAnim.OLD_NORMAL, "Play play play play!")
            1 -> npcl(FaceAnim.OLD_NORMAL, "It's playtime now!")
            2 -> npcl(FaceAnim.OLD_NORMAL, "Can we go over there now, pleasepleasepleasepleeeeeease?")
            3 -> npcl(FaceAnim.OLD_NORMAL, "What game are we playing now?")
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> { // Play play play
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "The only game I have time to play is the 'Staying Very Still' game."); stage++ }
                    1 -> { npcl(FaceAnim.OLD_NORMAL, "But that game is soooooo booooooring..."); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "How about we use the extra house rule, that makes it the 'Staying Very Still and Very Quiet' game."); stage++ }
                    3 -> { npcl(FaceAnim.OLD_NORMAL, "Happy happy! I love new games!"); stage = END_DIALOGUE }
                }
            }
            1 -> { // It's playtime now
                when (stage) {
                    4 -> { playerl(FaceAnim.FRIENDLY, "Okay, how about we play the 'Staying Very Still' game."); stage++ }
                    5 -> { npcl(FaceAnim.OLD_NORMAL, "But that game is booooooring..."); stage++ }
                    6 -> { playerl(FaceAnim.FRIENDLY, "If you win then you can pick the next game, how about that?"); stage++ }
                    7 -> { npcl(FaceAnim.OLD_NORMAL, "Happy happy!"); stage = END_DIALOGUE }
                }
            }
            2 -> { // Can we go over there
                when (stage) {
                    8 -> { playerl(FaceAnim.FRIENDLY, "Go over where?"); stage++ }
                    9 -> { npcl(FaceAnim.OLD_NORMAL, "I dunno, someplace fun, pleasepleaseplease!"); stage++ }
                    10 -> { playerl(FaceAnim.FRIENDLY, "Okay, but first, let's play the 'Sitting Very Still' game."); stage++ }
                    11 -> { npcl(FaceAnim.OLD_NORMAL, "But that game is booooooring..."); stage++ }
                    12 -> { playerl(FaceAnim.FRIENDLY, "Well, if you win we can go somewhere else, okay?"); stage++ }
                    13 -> { npcl(FaceAnim.OLD_NORMAL, "Happy happy!"); stage = END_DIALOGUE }
                }
            }
            3 -> { // What game are we playing
                when (stage) {
                    14 -> { playerl(FaceAnim.FRIENDLY, "It's called the 'Staying Very Still' game."); stage++ }
                    15 -> { npcl(FaceAnim.OLD_NORMAL, "This game is booooooring..."); stage++ }
                    16 -> { playerl(FaceAnim.FRIENDLY, "Hey, all that moping doesn't look very still to me."); stage++ }
                    17 -> { npcl(FaceAnim.OLD_NORMAL, "I never win at this game..."); stage++ }
                    18 -> { playerl(FaceAnim.FRIENDLY, "You know what? I think I'll not count it this one time"); stage++ }
                    19 -> { npcl(FaceAnim.OLD_NORMAL, "Happy happy! You're the best friend ever!"); stage = END_DIALOGUE }
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SPIRIT_JELLY_6992, NPCs.SPIRIT_JELLY_6993)
}