package content.global.skill.summoning.familiar.dialogue

import core.api.inEquipmentOrInventory
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * The type Praying mantis dialogue.
 */
@Initializable
class PrayingMantisDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = PrayingMantisDialogue(player)

    /**
     * Instantiates a new Praying mantis dialogue.
     */
    constructor()

    /**
     * Instantiates a new Praying mantis dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (inEquipmentOrInventory(player, Items.BUTTERFLY_NET_10010, 1) ||
            inEquipmentOrInventory(
                player,
                Items.MAGIC_BUTTERFLY_NET_11259,
                1,
            )
        ) {
            npc(
                FaceAnim.CHILD_NORMAL,
                "Clatter click chitter click?",
                "(Wouldn't you learn focus better if you used chopsticks?)",
            )
            stage = 0
            return true
        }
        when ((Math.random() * 4).toInt()) {
            0 -> {
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Chitter chirrup chirrup?",
                    "(Have you been following your training, grasshopper?)",
                )
                stage = 4
            }

            1 -> {
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Chitterchitter chirrup clatter.",
                    "(Today, grasshopper, I will teach you to walk on rice paper.)",
                )
                stage = 9
            }

            2 -> {
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Clatter chirrup chirp chirrup clatter clatter.",
                    "(A wise man once said; 'Feed your mantis and it will be happy'.)",
                )
                stage = 13
            }

            3 -> {
                npc(FaceAnim.CHILD_NORMAL, "Clatter chirrupchirp-", "(Today, grasshopper, we will-)")
                stage = 15
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
                playerl(FaceAnim.FRIENDLY, "Huh?")
                stage++
            }

            1 -> {
                npc(FaceAnim.CHILD_NORMAL, "Clicker chirrpchirrup.", "(For catching the butterflies, grasshopper.)")
                stage++
            }

            2 -> {
                playerl(FaceAnim.FRIENDLY, "Oh, right! Well, if I use anything but the net I squash them.")
                stage++
            }

            3 -> {
                npc(FaceAnim.CHILD_NORMAL, "Chirrupchirrup click!", "(Then, I could have them!)")
                stage = END_DIALOGUE
            }

            4 -> {
                playerl(FaceAnim.FRIENDLY, "Yes, almost every day.")
                stage++
            }

            5 -> {
                npc(FaceAnim.CHILD_NORMAL, "Chirrupchirrup chirrup.", "('Almost' is not good enough.)")
                stage++
            }

            6 -> {
                playerl(FaceAnim.FRIENDLY, "Well, I'm trying as hard as I can.")
                stage++
            }

            7 -> {
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Chirrup chitter chitter chirrup?",
                    "(How do you expect to achieve enlightenment at this rate, grasshopper?)",
                )
                stage++
            }

            8 -> {
                playerl(FaceAnim.FRIENDLY, "Spontaneously.")
                stage = END_DIALOGUE
            }

            9 -> {
                playerl(FaceAnim.HALF_ASKING, "What if I can't find any?")
                stage++
            }

            10 -> {
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Clatter chitter click chitter...",
                    "(Then we will wander about and punch monsters in the head...)",
                )
                stage++
            }

            11 -> {
                playerl(FaceAnim.HALF_ASKING, "I could do in an enlightened way if you want?")
                stage++
            }

            12 -> {
                npc(FaceAnim.CHILD_NORMAL, "Chirrupchitter!", "(That will do!)")
                stage = END_DIALOGUE
            }

            13 -> {
                playerl(FaceAnim.HALF_ASKING, "Is there any point to that saying?")
                stage++
            }

            14 -> {
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Clatter chirrupchirrup chirp.",
                    "(I find that a happy mantis is its own point.)",
                )
                stage = END_DIALOGUE
            }

            15 -> {
                playerl(FaceAnim.FRIENDLY, "You know, I'd rather you call me something other than grasshopper.")
                stage++
            }

            16 -> {
                npc(FaceAnim.CHILD_NORMAL, "Clitterchirp?", "(Is there a reason for this?)")
                stage++
            }

            17 -> {
                playerl(FaceAnim.FRIENDLY, "You drool when you say it.")
                stage++
            }

            18 -> {
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Clickclatter! Chirrup chirpchirp click chitter...",
                    "(I do not! Why would I drool when I call you a juicy...)",
                )
                stage++
            }

            19 -> {
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "...clickclick chitter clickchitter click...",
                    "(...succulent, nourishing, crunchy...)",
                )
                stage++
            }

            20 -> {
                npc(FaceAnim.CHILD_NORMAL, "*Drooool*")
                stage++
            }

            21 -> {
                playerl(FaceAnim.FRIENDLY, "You're doing it again!")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.PRAYING_MANTIS_6798, NPCs.PRAYING_MANTIS_6799)
}
