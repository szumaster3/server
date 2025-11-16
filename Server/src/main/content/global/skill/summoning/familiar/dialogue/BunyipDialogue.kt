package content.global.skill.summoning.familiar.dialogue

import content.global.skill.gather.fishing.Fish.Companion.fishMap
import core.api.anyInEquipment
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs
import kotlin.random.Random

/**
 * Represents the Bunyip familiar dialogue.
 */
@Initializable
class BunyipDialogue : Dialogue {

    private var branch: Int = -1

    override fun newInstance(player: Player?) = BunyipDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as? NPC ?: return false

        branch = if (anyInEquipment(player, *fishes)) 0 else (1..4).random()
        stage = 0

        when (branch) {
            0 -> npcl(FaceAnim.CHILD_NORMAL, "Hey, I see you've got some fish there!")
            1 -> npcl(FaceAnim.CHILD_NORMAL, "Fancy going to the beach later?")
            2 -> npcl(FaceAnim.CHILD_NORMAL, "Got a lot to do today, eh?")
            3 -> npcl(FaceAnim.CHILD_NORMAL, "I hope you don't want more water runes.")
            4 -> npcl(FaceAnim.CHILD_NORMAL, "It's hot today, isn't it?")
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> when (stage) {
                0 -> { playerl(FaceAnim.FRIENDLY, "Yeah, but I might cook them up before I give them to you!"); stage++ }
                1 -> { npcl(FaceAnim.CHILD_NORMAL, "Humans...always ruining good fishes."); stage++ }
                2 -> { playerl(FaceAnim.FRIENDLY, "You know, some people prefer them cooked."); stage++ }
                3 -> { npcl(FaceAnim.CHILD_NORMAL, "Yeah. We call 'em freaks."); stage = END_DIALOGUE }
            }

            1 -> when (stage) {
                0 -> { playerl(FaceAnim.FRIENDLY, "Well, we have a fair few places to go, but I suppose we could go to the beach if we get time."); stage++ }
                1 -> { npcl(FaceAnim.CHILD_NORMAL, "Bonza! I'll get my board ready!"); stage++ }
                2 -> { playerl(FaceAnim.FRIENDLY, "Well, even if we do go to the beach I don't know if we'll have time for that."); stage++ }
                3 -> { npcl(FaceAnim.CHILD_NORMAL, "Awww, that's a drag..."); stage = END_DIALOGUE }
            }

            2 -> when (stage) {
                0 -> { playerl(FaceAnim.FRIENDLY, "Well, I have a lot of things to do today but maybe later."); stage++ }
                1 -> { npcl(FaceAnim.CHILD_NORMAL, "Bonza!"); stage = END_DIALOGUE }
            }

            3 -> when (stage) {
                0 -> { playerl(FaceAnim.FRIENDLY, "I don't know if I want any more water runes."); stage++ }
                1 -> { npcl(FaceAnim.CHILD_NORMAL, "Righty, but I do know that I want some shrimps!"); stage++ }
                2 -> { playerl(FaceAnim.FRIENDLY, "A fair point."); stage = END_DIALOGUE }
            }

            4 -> when (stage) {
                0 -> { playerl(FaceAnim.HALF_ASKING, "What's the matter?"); stage++ }
                1 -> { npcl(FaceAnim.CHILD_NORMAL, "I'm dryin' out in this sun, mate."); stage++ }
                2 -> { playerl(FaceAnim.ASKING, "Well, what can I do to help?"); stage++ }
                3 -> { npcl(FaceAnim.CHILD_NORMAL, "Well, fish oil is bonza for the skin, ya know."); stage++ }
                4 -> { playerl(FaceAnim.FRIENDLY, "Oh, right, I think I see where this is going."); stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.BUNYIP_6813, NPCs.BUNYIP_6814)

    companion object {
        private val fishes: IntArray =
            fishMap.values.stream().mapToInt { it.id }.toArray()
    }
}
