package content.global.skill.summoning.familiar.dialogue

import core.api.hasHandsFree
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * The type Ravenous locust dialogue.
 */
@Initializable
class RavenousLocustDialogue : Dialogue {
    override fun newInstance(player: Player): Dialogue {
        return RavenousLocustDialogue(player)
    }

    /**
     * Instantiates a new Ravenous locust dialogue.
     */
    constructor()

    /**
     * Instantiates a new Ravenous locust dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (hasHandsFree(player)) {
            npcl(
                FaceAnim.CHILD_NORMAL,
                "Clatter click chitter click? (Wouldn't you learn focus better if you used chopsticks?)"
            )
            stage = 0
            return true
        }
        when ((Math.random() * 3).toInt()) {
            0 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Chitterchitter chirrup clatter. (Today, grasshopper, I will teach you to walk on rice paper.)"
                )
                stage = 5
            }

            1 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Clatter chirrup chirp chirrup clatter clatter. (A wise man once said; 'Feed your mantis and it will be happy'.)"
                )
                stage = 9
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Clatter chirrupchirp- (Today, grasshopper, we will-)")
                stage = 11
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                playerl(FaceAnim.FRIENDLY, "Yes, almost every day.")
                stage++
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Chirrupchirrup chirrup. ('Almost' is not good enough.)")
                stage++
            }

            2 -> {
                playerl(FaceAnim.FRIENDLY, "Well, I'm trying as hard as I can.")
                stage++
            }

            3 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Chirrup chitter chitter chirrup? (How do you expect to achieve enlightenment at this rate, grasshopper?)"
                )
                stage++
            }

            4 -> {
                playerl(FaceAnim.FRIENDLY, "Spontaneously.")
                stage = END_DIALOGUE
            }

            5 -> {
                playerl(FaceAnim.HALF_ASKING, "What if I can't find any?")
                stage++
            }

            6 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Clatter chitter click chitter... (Then we will wander about and punch monsters in the head...)"
                )
                stage++
            }

            7 -> {
                playerl(FaceAnim.HALF_ASKING, "I could do in an enlightened way if you want?")
                stage++
            }

            8 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Chirrupchitter! (That will do!)")
                stage = END_DIALOGUE
            }

            9 -> {
                playerl(FaceAnim.HALF_ASKING, "Is there any point to that saying?")
                stage++
            }

            10 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Clatter chirrupchirrup chirp. (I find that a happy mantis is its own point.)"
                )
                stage = END_DIALOGUE
            }

            11 -> {
                playerl(FaceAnim.FRIENDLY, "You know, I'd rather you call me something other than grasshopper.")
                stage++
            }

            12 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Clitterchirp? (Is there a reason for this?)")
                stage++
            }

            13 -> {
                playerl(FaceAnim.FRIENDLY, "You drool when you say it.")
                stage++
            }

            14 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Clickclatter! Chirrup chirpchirp click chitter... (I do not! Why would I drool when I call you a juicy...)"
                )
                stage++
            }

            15 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "...clickclick chitter clickchitter click... (...succulent, nourishing, crunchy...)"
                )
                stage++
            }

            16 -> {
                npcl(FaceAnim.CHILD_NORMAL, "*Drooool*")
                stage++
            }

            17 -> {
                playerl(FaceAnim.FRIENDLY, "You're doing it again!")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.RAVENOUS_LOCUST_7372, NPCs.RAVENOUS_LOCUST_7373)
    }
}
