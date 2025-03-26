package content.global.skill.summoning.familiar.dialogue

import content.global.skill.gathering.fishing.Fish
import content.global.skill.gathering.fishing.Fish.Companion.fishMap
import core.api.anyInInventory
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * The type Granite crab dialogue.
 */
@Initializable
class GraniteCrabDialogue : Dialogue {
    override fun newInstance(player: Player): Dialogue {
        return GraniteCrabDialogue(player)
    }

    /**
     * Instantiates a new Granite crab dialogue.
     */
    constructor()

    /**
     * Instantiates a new Granite crab dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (anyInInventory(player, *fishes)) {
            npcl(FaceAnim.CHILD_NORMAL, "That is not a rock fish...")
            stage = END_DIALOGUE
            return true
        }

        when ((Math.random() * 4).toInt()) {
            0 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Can I have some fish?")
                stage = 0
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Rock fish now, please?")
                stage = 5
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "When can we go fishing? I want rock fish.")
                stage = 6
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I'm stealthy!")
                stage = 7
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                playerl(FaceAnim.FRIENDLY, "No, I have to cook these for later.")
                stage++
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Free fish, please?")
                stage++
            }

            2 -> {
                playerl(FaceAnim.FRIENDLY, "No...I already told you you can't.")
                stage++
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Can it be fish time soon?")
                stage++
            }

            4 -> {
                playerl(
                    FaceAnim.FRIENDLY,
                    "Great...I get stuck with the only granite crab in existence that can't take no for an answer..."
                )
                stage = END_DIALOGUE
            }

            5 -> {
                playerl(FaceAnim.FRIENDLY, "Not right now. I don't have any rock fish.")
                stage = END_DIALOGUE
            }

            6 -> {
                playerl(FaceAnim.FRIENDLY, "When I need some fish. It's not that hard to work out, right?")
                stage = END_DIALOGUE
            }

            7 -> {
                playerl(FaceAnim.FRIENDLY, "Errr... of course you are.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GRANITE_CRAB_6796, NPCs.GRANITE_CRAB_6797)
    }

    companion object {
        private val fishes: IntArray = fishMap.values.stream().mapToInt { fish: Fish -> fish.id }.toArray()
    }
}
