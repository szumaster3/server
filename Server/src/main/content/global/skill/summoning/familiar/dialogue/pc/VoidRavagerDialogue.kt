package content.global.skill.summoning.familiar.dialogue.pc

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Void Ravager familiar dialogue.
 */
@Initializable
class VoidRavagerDialogue : Dialogue {

    private var branch = -1

    override fun newInstance(player: Player?): Dialogue = VoidRavagerDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        if (branch == -1) branch = (Math.random() * 4).toInt()
        stage = 0

        when (branch) {
            0 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "You look delicious!")
            1 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Take me to the rift!")
            2 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Pardon me. Could I trouble you for a moment?")
            3 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "How do you bear life without ravaging?")
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "Don't make me dismiss you!"); stage = END_DIALOGUE }
                }
            }
            1 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "I'm not taking you there! Goodness knows what you'd get up to."); stage++ }
                    1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "I promise not to destroy your world..."); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "If only I could believe you..."); stage = END_DIALOGUE }
                }
            }
            2 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "Yeah, sure."); stage++ }
                    1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Oh, it's just a trifling thing. Mmm, trifle...you look like trifle...So, will you help?"); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "Fire away!"); stage++ }
                    3 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Oh, just be honest. I just want a second opinion...Is this me? Mmm trifle..."); stage++ }
                    4 -> { playerl(FaceAnim.FRIENDLY, "Huh?"); stage++ }
                    5 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Oh! The claws! The whiskers! The single, yellow eye! Oh! Is it me? Is it truly me?"); stage++ }
                    6 -> { playerl(FaceAnim.FRIENDLY, "Erm...why yes...of course. It definitely reflects the inner you."); stage++ }
                    7 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Oh, I knew it! You've been an absolute delight. An angel delight! And everyone said it was just a phase!"); stage = END_DIALOGUE }
                }
            }
            3 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "It's not always easy."); stage++ }
                    1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "I could show you how to ravage, if you like..."); stage = END_DIALOGUE }
                }
            }
        }

        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.VOID_RAVAGER_7370, NPCs.VOID_RAVAGER_7371)
}