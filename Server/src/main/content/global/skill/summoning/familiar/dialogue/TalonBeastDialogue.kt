package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Talon Beast familiar dialogue.
 */
@Initializable
class TalonBeastDialogue : Dialogue {

    private var branch = -1

    override fun newInstance(player: Player?): Dialogue = TalonBeastDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        branch = (Math.random() * 4).toInt()
        stage = 0

        when (branch) {
            0 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Is this all you apes do all day, then?")
            1 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "This place smells odd...")
            2 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Hey!")
            3 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "C'mon! Lets go fight stuff!")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "Well, we do a lot of other things, too."); stage++ }
                    1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "That's dull. Lets go find something and bite it."); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "I wouldn't want to spoil my dinner."); stage++ }
                    3 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "So, I have to watch you trudge about again? Talk about boring."); stage = END_DIALOGUE }
                }
            }
            1 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.HALF_ASKING, "Odd?"); stage++ }
                    1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Yes, not enough is rotting..."); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "For which I am extremely grateful."); stage = END_DIALOGUE }
                }
            }
            2 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "Aaaargh!"); stage++ }
                    1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Why d'you always do that?"); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "I don't think I'll ever get used to having a huge, ravenous feline sneaking around behind me all the time."); stage++ }
                    3 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "That's okay, I doubt I'll get used to following an edible, furless monkey prancing in front of me all the time either."); stage = END_DIALOGUE }
                }
            }
            3 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.ASKING, "What sort of stuff?"); stage++ }
                    1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "I dunno? Giants, monsters, vaguely-defined philosophical concepts. You know: stuff."); stage++ }
                    2 -> { playerl(FaceAnim.ASKING, "How are we supposed to fight a philosophical concept?"); stage++ }
                    3 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "With subtle arguments and pointy sticks!"); stage++ }
                    4 -> { playerl(FaceAnim.FRIENDLY, "Well, I can see you're going to go far in debates."); stage = END_DIALOGUE }
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.TALON_BEAST_7347, NPCs.TALON_BEAST_7348)
}
