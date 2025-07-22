package content.global.skill.summoning.familiar.dialogue.titan

import core.api.inBorders
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * The type Ice titan dialogue.
 */
@Initializable
class IceTitanDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = IceTitanDialogue(player)

    /**
     * Instantiates a new Ice titan dialogue.
     */
    constructor()

    /**
     * Instantiates a new Ice titan dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (inBorders(player, 3113, 2753, 3391, 3004)) {
            npcl(FaceAnim.CHILD_NORMAL, "I'm melting!")
            stage = 0
            return true
        }
        when ((Math.random() * 4).toInt()) {
            0 -> {
                playerl(FaceAnim.HALF_ASKING, "How are you feeling?")
                stage = 3
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Can we just stay away from fire for a while?")
                stage = 19
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I could murder an ice-cream.")
                stage = 28
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "It's too hot here.")
                stage = 42
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
                playerl(FaceAnim.FRIENDLY, "I have to admit, I am rather on the hot side myself.")
                stage++
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "No, I mean I'm actually melting! My legs have gone dribbly.")
                stage++
            }

            2 -> {
                playerl(FaceAnim.FRIENDLY, "Urk! Well, try hold it together.")
                stage = END_DIALOGUE
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Hot.")
                stage++
            }

            4 -> {
                playerl(FaceAnim.HALF_ASKING, "Are you ever anything else?")
                stage++
            }

            5 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Sometimes I'm just the right temperature: absolute zero.")
                stage++
            }

            6 -> {
                playerl(FaceAnim.HALF_ASKING, "What's that then, when it's not at home with its feet up on the couch?")
                stage++
            }

            7 -> {
                npcl(FaceAnim.CHILD_NORMAL, "What?")
                stage++
            }

            8 -> {
                playerl(FaceAnim.HALF_ASKING, "Absolute zero; what is it?")
                stage++
            }

            9 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Oh...it's the lowest temperature that can exist.")
                stage++
            }

            10 -> {
                playerl(FaceAnim.HALF_ASKING, "Like the temperature of ice?")
                stage++
            }

            11 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Um, no. Rather a lot colder.")
                stage++
            }

            12 -> {
                playerl(FaceAnim.HALF_ASKING, "Like a deepest, darkest winter day?")
                stage++
            }

            13 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Nah, that's warm by comparison.")
                stage++
            }

            14 -> {
                playerl(FaceAnim.HALF_ASKING, "Like an Ice Barrage in your jammies?")
                stage++
            }

            15 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Even colder than that.")
                stage++
            }

            16 -> {
                playerl(FaceAnim.FRIENDLY, "Yikes! That's rather chilly.")
                stage++
            }

            17 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Yeah. Wonderful, isn't it?")
                stage++
            }

            18 -> {
                playerl(FaceAnim.FRIENDLY, "If you say so.")
                stage = END_DIALOGUE
            }

            19 -> {
                playerl(FaceAnim.FRIENDLY, "I like fire, it's so pretty.")
                stage++
            }

            20 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Personally, I think it's terrifying.")
                stage++
            }

            21 -> {
                playerl(FaceAnim.FRIENDLY, "Why?")
                stage++
            }

            22 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I'm not so keen on hot things.")
                stage++
            }

            23 -> {
                playerl(FaceAnim.FRIENDLY, "Ah.")
                stage++
            }

            24 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Indeed.")
                stage++
            }

            25 -> {
                playerl(FaceAnim.FRIENDLY, "I see.")
                stage++
            }

            26 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Yes. Well...")
                stage++
            }

            27 -> {
                playerl(FaceAnim.FRIENDLY, "...let's get on with it.")
                stage = END_DIALOGUE
            }

            28 -> {
                playerl(FaceAnim.FRIENDLY, "Is that a Slayer creature?")
                stage++
            }

            29 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Um...")
                stage++
            }

            30 -> {
                playerl(FaceAnim.HALF_ASKING, "What does it drop?")
                stage++
            }

            31 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Erm...")
                stage++
            }

            32 -> {
                playerl(FaceAnim.HALF_ASKING, "What level is it?")
                stage++
            }

            33 -> {
                npcl(FaceAnim.CHILD_NORMAL, "It...")
                stage++
            }

            34 -> {
                playerl(FaceAnim.HALF_ASKING, "Where can I find it?")
                stage++
            }

            35 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I...")
                stage++
            }

            36 -> {
                playerl(FaceAnim.HALF_ASKING, "What equipment will I need?")
                stage++
            }

            37 -> {
                npcl(FaceAnim.CHILD_NORMAL, "What...")
                stage++
            }

            38 -> {
                playerl(FaceAnim.FRIENDLY, "I don't think it will be high enough level.")
                stage++
            }

            39 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Urm...")
                stage++
            }

            40 -> {
                playerl(FaceAnim.FRIENDLY, "...")
                stage++
            }

            41 -> {
                npcl(FaceAnim.CHILD_NORMAL, "We should get on with what we were doing.")
                stage = END_DIALOGUE
            }

            42 -> {
                playerl(FaceAnim.FRIENDLY, "It's really not that hot. I think it's rather pleasant.")
                stage++
            }

            43 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Well, it's alright for some. Some of us don't like the heat. I burn easily - well, okay, melt.",
                )
                stage++
            }

            44 -> {
                playerl(FaceAnim.FRIENDLY, "Well, at least I know where to get a nice cold drink if I need one.")
                stage++
            }

            45 -> {
                npcl(FaceAnim.CHILD_NORMAL, "What was that?")
                stage++
            }

            46 -> {
                playerl(FaceAnim.FRIENDLY, "Nothing. Hehehehe")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.ICE_TITAN_7359, NPCs.ICE_TITAN_7360)
}
