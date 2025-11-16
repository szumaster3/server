package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Smoke Devil familiar dialogue.
 */
@Initializable
class SmokeDevilDialogue : Dialogue {

    private var branch = -1

    override fun newInstance(player: Player?): Dialogue = SmokeDevilDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        branch = (Math.random() * 4).toInt()
        stage = 0

        when (branch) {
            0 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "When are you going to be done with that?")
            1 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Hey!")
            2 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Ah, this is the life!")
            3 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Why is it always so cold here?")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "Soon, I hope."); stage++ }
                    1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Good, because this place is too breezy."); stage++ }
                    2 -> { playerl(FaceAnim.HALF_ASKING, "What do you mean?"); stage++ }
                    3 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "I mean, it's tricky to keep hovering in this draft."); stage++ }
                    4 -> { playerl(FaceAnim.FRIENDLY, "Ok, we'll move around a little if you like."); stage++ }
                    5 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Yes please!"); stage = END_DIALOGUE }
                }
            }
            1 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.HALF_ASKING, "Yes?"); stage++ }
                    1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Where are we going again?"); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "Well, I have a lot of things to do today, so we might go a lot of places."); stage++ }
                    3 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Are we there yet?"); stage++ }
                    4 -> { playerl(FaceAnim.FRIENDLY, "No, not yet."); stage++ }
                    5 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "How about now?"); stage++ }
                    6 -> { playerl(FaceAnim.FRIENDLY, "No."); stage++ }
                    7 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Are we still not there?"); stage++ }
                    8 -> { playerl(FaceAnim.ANNOYED, "NO!"); stage++ }
                    9 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Okay, just checking."); stage = END_DIALOGUE }
                }
            }
            2 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.HALF_ASKING, "Having a good time up there?"); stage++ }
                    1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Yeah! It's great to feel the wind in your tentacles."); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "Sadly, I don't know what that feels like."); stage++ }
                    3 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Why not?"); stage++ }
                    4 -> { playerl(FaceAnim.FRIENDLY, "No tentacles for a start."); stage++ }
                    5 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Well, nobody's perfect."); stage = END_DIALOGUE }
                }
            }
            3 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "I don't think it's that cold."); stage++ }
                    1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "It is compared to back home."); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "How hot is it where you are from?"); stage++ }
                    3 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "I can never remember. What is the vaporisation point of steel again?"); stage++ }
                    4 -> { playerl(FaceAnim.FRIENDLY, "Pretty high."); stage++ }
                    5 -> { playerl(FaceAnim.FRIENDLY, "No wonder you feel cold here..."); stage = END_DIALOGUE }
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SMOKE_DEVIL_6865, NPCs.SMOKE_DEVIL_6866)
}