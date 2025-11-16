package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Abyssal Lurker familiar dialogue.
 */
@Initializable
class AbyssalLurkerDialogue : Dialogue {

    override fun newInstance(player: Player?): Dialogue = AbyssalLurkerDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    private val responses = listOf(
        "What? Are we in danger, or something?",
        "What? Is that even a language?",
        "What? Do you want something?",
        "What? Is there somebody down an old well, or something?"
    )

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val stageIndex = (Math.random() * responses.size).toInt()
        stage = stageIndex
        when (stageIndex) {
            0 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Djrej gf'ig sgshe...")
            1 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "To poshi v'kaa!")
            2 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "G-harrve shelmie?")
            3 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Jehifk i'ekfh skjd.")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        if (stage in responses.indices) {
            playerl(FaceAnim.HALF_ASKING, responses[stage])
            stage = END_DIALOGUE
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.ABYSSAL_LURKER_6820, NPCs.ABYSSAL_LURKER_6821)
}