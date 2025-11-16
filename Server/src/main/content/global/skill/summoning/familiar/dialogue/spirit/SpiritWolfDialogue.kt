package content.global.skill.summoning.familiar.dialogue.spirit

import content.global.skill.prayer.Bones
import core.api.anyInInventory
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Spirit Wolf familiar dialogue.
 */
@Initializable
class SpiritWolfDialogue : Dialogue {

    private var branch: Int = -1

    override fun newInstance(player: Player?): Dialogue = SpiritWolfDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as? NPC ?: return false

        if (anyInInventory(player, *bones.toIntArray())) {
            npc(FaceAnim.FAMILIAR_NEUTRAL, "Whuff-Whuff! Arf!", "(Throw the bone! I want to chase it!)")
            stage = 0
            branch = -1
            return true
        }

        branch = (0..3).random()

        stage = when (branch) {
            0 -> 1
            1 -> 2
            2 -> 4
            3 -> 5
            else -> 1
        }

        when (branch) {
            0 -> npc(FaceAnim.FAMILIAR_NEUTRAL, "Whurf?", "(What are you doing?)")
            1 -> npc(FaceAnim.FAMILIAR_NEUTRAL, "Bark Bark!", "(Danger!)")
            2 -> npc(FaceAnim.FAMILIAR_NEUTRAL, "Whuff whuff. Pantpant awff!", "(I smell something good! Hunting time!)")
            3 -> npc(FaceAnim.FAMILIAR_NEUTRAL, "Pant pant whine?", "(When am I going to get to chase something?)")
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> if (stage == 1) {
                playerl(FaceAnim.FRIENDLY, "Oh, just some... biped things. I'm sure it would bore you.")
                stage = END_DIALOGUE
            }
            1 -> when (stage) {
                2 -> { playerl(FaceAnim.FRIENDLY, "Where?!"); stage++ }
                3 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "Whiiiine...", "(False alarm...)"); stage = END_DIALOGUE }
            }
            2 -> if (stage == 4) {
                playerl(FaceAnim.FRIENDLY, "We can go hunting in a moment. I just have to take care of something first.")
                stage = END_DIALOGUE
            }
            3 -> if (stage == 5) {
                playerl(FaceAnim.FRIENDLY, "Oh, I'm sure we'll find something for you in a bit.")
                stage = END_DIALOGUE
            }
        }

        if (stage == 0) stage = END_DIALOGUE

        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SPIRIT_WOLF_6829, NPCs.SPIRIT_WOLF_6830)

    companion object {
        private val bones: MutableList<Int> = Bones.values().map { it.itemId }.toMutableList()
    }
}
