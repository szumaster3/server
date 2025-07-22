package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * The type Talon beast dialogue.
 */
@Initializable
class TalonBeastDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = TalonBeastDialogue(player)

    /**
     * Instantiates a new Talon beast dialogue.
     */
    constructor()

    /**
     * Instantiates a new Talon beast dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val randomChoice = (Math.random() * 4).toInt()
        when (randomChoice) {
            0 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Is this all you apes do all day, then?")
                stage = 0
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "This place smells odd...")
                stage = 4
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Hey!")
                stage = 7
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "C'mon! Lets go fight stuff!")
                stage = 11
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                playerl(FaceAnim.FRIENDLY, "Well, we do a lot of other things, too.")
                stage++
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "That's dull. Lets go find something and bite it.")
                stage++
            }

            2 -> {
                playerl(FaceAnim.FRIENDLY, "I wouldn't want to spoil my dinner.")
                stage++
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "So, I have to watch you trudge about again? Talk about boring.")
                stage = END_DIALOGUE
            }

            4 -> {
                playerl(FaceAnim.HALF_ASKING, "Odd?")
                stage++
            }

            5 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Yes, not enough is rotting...")
                stage++
            }

            6 -> {
                playerl(FaceAnim.FRIENDLY, "For which I am extremely grateful.")
                stage = END_DIALOGUE
            }

            7 -> {
                playerl(FaceAnim.FRIENDLY, "Aaaargh!")
                stage++
            }

            8 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Why d'you always do that?")
                stage++
            }

            9 -> {
                playerl(
                    FaceAnim.FRIENDLY,
                    "I don't think I'll ever get used to having a huge, ravenous feline sneaking around behind me all the time.",
                )
                stage++
            }

            10 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "That's okay, I doubt I'll get used to following an edible, furless monkey prancing in front of me all the time either.",
                )
                stage = END_DIALOGUE
            }

            11 -> {
                playerl(FaceAnim.ASKING, "What sort of stuff?")
                stage++
            }

            12 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "I dunno? Giants, monsters, vaguely-defined philosophical concepts. You know: stuff.",
                )
                stage++
            }

            13 -> {
                playerl(FaceAnim.ASKING, "How are we supposed to fight a philosophical concept?")
                stage++
            }

            14 -> {
                npcl(FaceAnim.CHILD_NORMAL, "With subtle arguments and pointy sticks!")
                stage++
            }

            15 -> {
                playerl(FaceAnim.FRIENDLY, "Well, I can see you're going to go far in debates.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.TALON_BEAST_7347, NPCs.TALON_BEAST_7348)
}
