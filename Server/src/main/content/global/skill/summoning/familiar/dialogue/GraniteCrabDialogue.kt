package content.global.skill.summoning.familiar.dialogue

import content.global.skill.gather.fishing.Fish
import content.global.skill.gather.fishing.Fish.Companion.fishMap
import core.api.anyInInventory
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Granite Crab familiar dialogue.
 */
@Initializable
class GraniteCrabDialogue : Dialogue {
    private var branch: Int = 0

    override fun newInstance(player: Player?) = GraniteCrabDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (anyInInventory(player, *fishes)) {
            npcl(FaceAnim.CHILD_NORMAL, "That is not a rock fish...")
            stage = END_DIALOGUE
            return true
        }

        branch = (0..3).random()
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> when (stage) {
                0 -> playerl(FaceAnim.FRIENDLY, "No, I have to cook these for later.").also { stage++ }
                1 -> npcl(FaceAnim.CHILD_NORMAL, "Free fish, please?").also { stage++ }
                2 -> playerl(FaceAnim.FRIENDLY, "No...I already told you you can't.").also { stage++ }
                3 -> npcl(FaceAnim.CHILD_NORMAL, "Can it be fish time soon?").also { stage++ }
                4 -> playerl(FaceAnim.FRIENDLY, "Great...I get stuck with the only granite crab in existence that can't take no for an answer...").also { stage = END_DIALOGUE }

            }
            1 -> when (stage) {
                0 -> playerl(FaceAnim.FRIENDLY, "Not right now. I don't have any rock fish.").also { stage = END_DIALOGUE }
            }
            2 -> when (stage) {
                0 -> playerl(FaceAnim.FRIENDLY, "When I need some fish. It's not that hard to work out, right?").also { stage = END_DIALOGUE }
            }
            3 -> when (stage) {
                0 -> playerl(FaceAnim.FRIENDLY, "Errr... of course you are.").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.GRANITE_CRAB_6796, NPCs.GRANITE_CRAB_6797)

    companion object {
        private val fishes: IntArray =
            fishMap.values
                .stream()
                .mapToInt(Fish::id)
                .toArray()
    }
}
