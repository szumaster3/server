package content.global.skill.summoning.familiar.dialogue

import content.global.skill.gathering.fishing.Fish
import content.global.skill.gathering.fishing.Fish.Companion.fishMap
import core.api.anyInEquipment
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import java.util.*

/**
 * The type Bunyip dialogue.
 */
@Initializable
class BunyipDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = BunyipDialogue(player)

    /**
     * Instantiates a new Bunyip dialogue.
     */
    constructor()

    /**
     * Instantiates a new Bunyip dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (anyInEquipment(player, *fishes)) {
            npcl(FaceAnim.CHILD_NORMAL, "I see you've got some fish there, mate.")
            stage = 0
            return true
        }
        when (Random().nextInt(4)) {
            0 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Where are we going and why is it not to the beach?")
                stage = 4
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Hey Bruce, can we go down to the beach t'day?")
                stage = 8
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Pass me another bunch of shrimps, mate!")
                stage = 10
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Sigh...")
                stage = 13
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
                playerl(FaceAnim.FRIENDLY, "Yeah, but I might cook them up before I give them to you!")
                stage++
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Humans...always ruining good fishes.")
                stage++
            }

            2 -> {
                playerl(FaceAnim.FRIENDLY, "You know, some people prefer them cooked.")
                stage++
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Yeah. We call 'em freaks.")
                stage = END_DIALOGUE
            }

            4 -> {
                playerl(
                    FaceAnim.FRIENDLY,
                    "Well, we have a fair few places to go, but I suppose we could go to the beach if we get time.",
                )
                stage++
            }

            5 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Bonza! I'll get my board ready!")
                stage++
            }

            6 -> {
                playerl(
                    FaceAnim.FRIENDLY,
                    "Well, even if we do go to the beach I don't know if we'll have time for that.",
                )
                stage++
            }

            7 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Awww, that's a drag...")
                stage = END_DIALOGUE
            }

            8 -> {
                playerl(FaceAnim.FRIENDLY, "Well, I have a lot of things to do today but maybe later.")
                stage++
            }

            9 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Bonza!")
                stage = END_DIALOGUE
            }

            10 -> {
                playerl(FaceAnim.FRIENDLY, "I don't know if I want any more water runes.")
                stage++
            }

            11 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Righty, but I do know that I want some shrimps!")
                stage++
            }

            12 -> {
                playerl(FaceAnim.FRIENDLY, "A fair point.")
                stage = END_DIALOGUE
            }

            13 -> {
                playerl(FaceAnim.HALF_ASKING, "What's the matter?")
                stage++
            }

            14 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I'm dryin' out in this sun, mate.")
                stage++
            }

            15 -> {
                playerl(FaceAnim.ASKING, "Well, what can I do to help?")
                stage++
            }

            16 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Well, fish oil is bonza for the skin, ya know.")
                stage++
            }

            17 -> {
                playerl(FaceAnim.FRIENDLY, "Oh, right, I think I see where this is going.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.BUNYIP_6813, NPCs.BUNYIP_6814)

    companion object {
        private val fishes: IntArray =
            fishMap.values
                .stream()
                .mapToInt { fish: Fish -> fish.id }
                .toArray()
    }
}
