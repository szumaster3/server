package content.global.skill.summoning.familiar.dialogue.titan

import core.api.inBorders
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Ice Titan familiar dialogue.
 */
@Initializable
class IceTitanDialogue : Dialogue {

    private var branch: Int = -1

    override fun newInstance(player: Player?): Dialogue = IceTitanDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as? NPC ?: return false

        if (inBorders(player, 3113, 2753, 3391, 3004)) {
            npcl(FaceAnim.FAMILIAR_NEUTRAL, "I'm melting!")
            stage = 0
            branch = 0
            return true
        }

        if (branch == -1) branch = (Math.random() * 4).toInt()

        stage = 0

        when (branch) {
            0 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Hot.")
            1 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Can we just stay away from fire for a while?")
            2 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "I could murder an ice-cream.")
            3 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "It's too hot here.")
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> when (stage) {
                0 -> { playerl(FaceAnim.FRIENDLY, "I have to admit, I am rather on the hot side myself."); stage++ }
                1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "No, I mean I'm actually melting! My legs have gone dribbly."); stage++ }
                2 -> { playerl(FaceAnim.FRIENDLY, "Urk! Well, try hold it together."); stage = END_DIALOGUE }
                3 -> { playerl(FaceAnim.HALF_ASKING, "Are you ever anything else?"); stage++ }
                4 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Sometimes I'm just the right temperature: absolute zero."); stage++ }
                5 -> { playerl(FaceAnim.HALF_ASKING, "What's that then, when it's not at home with its feet up on the couch?"); stage++ }
                6 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "What?"); stage++ }
                7 -> { playerl(FaceAnim.HALF_ASKING, "Absolute zero; what is it?"); stage++ }
                8 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Oh...it's the lowest temperature that can exist."); stage++ }
                9 -> { playerl(FaceAnim.HALF_ASKING, "Like the temperature of ice?"); stage++ }
                10 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Um, no. Rather a lot colder."); stage++ }
                11 -> { playerl(FaceAnim.HALF_ASKING, "Like a deepest, darkest winter day?"); stage++ }
                12 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Nah, that's warm by comparison."); stage++ }
                13 -> { playerl(FaceAnim.HALF_ASKING, "Like an Ice Barrage in your jammies?"); stage++ }
                14 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Even colder than that."); stage++ }
                15 -> { playerl(FaceAnim.FRIENDLY, "Yikes! That's rather chilly."); stage++ }
                16 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Yeah. Wonderful, isn't it?"); stage++ }
                17 -> { playerl(FaceAnim.FRIENDLY, "If you say so."); stage = END_DIALOGUE }
            }

            1 -> when (stage) {
                0 -> { playerl(FaceAnim.FRIENDLY, "I like fire, it's so pretty."); stage++ }
                1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Personally, I think it's terrifying."); stage++ }
                2 -> { playerl(FaceAnim.FRIENDLY, "Why?"); stage++ }
                3 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "I'm not so keen on hot things."); stage++ }
                4 -> { playerl(FaceAnim.FRIENDLY, "Ah."); stage++ }
                5 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Indeed."); stage++ }
                6 -> { playerl(FaceAnim.FRIENDLY, "I see."); stage++ }
                7 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Yes. Well..."); stage++ }
                8 -> { playerl(FaceAnim.FRIENDLY, "...let's get on with it."); stage = END_DIALOGUE }
            }

            2 -> when (stage) {
                0 -> { playerl(FaceAnim.FRIENDLY, "Is that a Slayer creature?"); stage++ }
                1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Um..."); stage++ }
                2 -> { playerl(FaceAnim.HALF_ASKING, "What does it drop?"); stage++ }
                3 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Erm..."); stage++ }
                4 -> { playerl(FaceAnim.HALF_ASKING, "What level is it?"); stage++ }
                5 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "It..."); stage++ }
                6 -> { playerl(FaceAnim.HALF_ASKING, "Where can I find it?"); stage++ }
                7 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "I..."); stage++ }
                8 -> { playerl(FaceAnim.HALF_ASKING, "What equipment will I need?"); stage++ }
                9 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "What..."); stage++ }
                10 -> { playerl(FaceAnim.FRIENDLY, "I don't think it will be high enough level."); stage++ }
                11 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Urm..."); stage++ }
                12 -> { playerl(FaceAnim.FRIENDLY, "..."); stage++ }
                13 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "We should get on with what we were doing."); stage = END_DIALOGUE }
            }

            3 -> when (stage) {
                0 -> { playerl(FaceAnim.FRIENDLY, "It's really not that hot. I think it's rather pleasant."); stage++ }
                1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Well, it's alright for some. Some of us don't like the heat. I burn easily - well, okay, melt."); stage++ }
                2 -> { playerl(FaceAnim.FRIENDLY, "Well, at least I know where to get a nice cold drink if I need one."); stage++ }
                3 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "What was that?"); stage++ }
                4 -> { playerl(FaceAnim.FRIENDLY, "Nothing. Hehehehe"); stage = END_DIALOGUE }
            }
        }

        stage++
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.ICE_TITAN_7359, NPCs.ICE_TITAN_7360)
}
