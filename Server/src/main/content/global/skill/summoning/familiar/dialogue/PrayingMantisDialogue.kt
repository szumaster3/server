package content.global.skill.summoning.familiar.dialogue

import core.api.inEquipmentOrInventory
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the Praying Mantis familiar dialogue.
 */
@Initializable
class PrayingMantisDialogue : Dialogue {

    private var branch = -1

    override fun newInstance(player: Player?): Dialogue = PrayingMantisDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        branch = when {
            inEquipmentOrInventory(player, Items.BUTTERFLY_NET_10010, 1) ||
                    inEquipmentOrInventory(player, Items.MAGIC_BUTTERFLY_NET_11259, 1) -> 0
            else -> (Math.random() * 4).toInt() + 1
        }

        stage = 0

        when (branch) {
            0 -> npc(
                FaceAnim.FAMILIAR_NEUTRAL,
                "Clatter click chitter click?",
                "(Wouldn't you learn focus better if you used chopsticks?)"
            )
            1 -> npc(
                FaceAnim.FAMILIAR_NEUTRAL,
                "Chitter chirrup chirrup?",
                "(Have you been following your training, grasshopper?)"
            )
            2 -> npc(
                FaceAnim.FAMILIAR_NEUTRAL,
                "Chitterchitter chirrup clatter.",
                "(Today, grasshopper, I will teach you to walk on rice paper.)"
            )
            3 -> npc(
                FaceAnim.FAMILIAR_NEUTRAL,
                "Clatter chirrup chirp chirrup clatter clatter.",
                "(A wise man once said; 'Feed your mantis and it will be happy'.)"
            )
            4 -> npc(FaceAnim.FAMILIAR_NEUTRAL, "Clatter chirrupchirp-", "(Today, grasshopper, we will-)")
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> when (stage) {
                0 -> { playerl(FaceAnim.FRIENDLY, "Huh?"); stage++ }
                1 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "Clicker chirrpchirrup.", "(For catching the butterflies, grasshopper.)"); stage++ }
                2 -> { playerl(FaceAnim.FRIENDLY, "Oh, right! Well, if I use anything but the net I squash them."); stage++ }
                3 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "Chirrupchirrup click!", "(Then, I could have them!)"); stage = END_DIALOGUE }
            }

            1 -> when (stage) {
                0 -> { playerl(FaceAnim.FRIENDLY, "Yes, almost every day."); stage++ }
                1 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "Chirrupchirrup chirrup.", "('Almost' is not good enough.)"); stage++ }
                2 -> { playerl(FaceAnim.FRIENDLY, "Well, I'm trying as hard as I can."); stage++ }
                3 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "Chirrup chitter chitter chirrup?", "(How do you expect to achieve enlightenment at this rate, grasshopper?)"); stage++ }
                4 -> { playerl(FaceAnim.FRIENDLY, "Spontaneously."); stage = END_DIALOGUE }
            }

            2 -> when (stage) {
                0 -> { playerl(FaceAnim.HALF_ASKING, "What if I can't find any?"); stage++ }
                1 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "Clatter chitter click chitter...", "(Then we will wander about and punch monsters in the head...)"); stage++ }
                2 -> { playerl(FaceAnim.HALF_ASKING, "I could do in an enlightened way if you want?"); stage++ }
                3 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "Chirrupchitter!", "(That will do!)"); stage = END_DIALOGUE }
            }

            3 -> when (stage) {
                0 -> { playerl(FaceAnim.HALF_ASKING, "Is there any point to that saying?"); stage++ }
                1 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "Clatter chirrupchirrup chirp.", "(I find that a happy mantis is its own point.)"); stage = END_DIALOGUE }
            }

            4 -> when (stage) {
                0 -> { playerl(FaceAnim.FRIENDLY, "You know, I'd rather you call me something other than grasshopper."); stage++ }
                1 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "Clitterchirp?", "(Is there a reason for this?)"); stage++ }
                2 -> { playerl(FaceAnim.FRIENDLY, "You drool when you say it."); stage++ }
                3 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "Clickclatter! Chirrup chirpchirp click chitter...", "(I do not! Why would I drool when I call you a juicy...)"); stage++ }
                4 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "...clickclick chitter clickchitter click...", "(...succulent, nourishing, crunchy...)"); stage++ }
                5 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "*Drooool*"); stage++ }
                6 -> { playerl(FaceAnim.FRIENDLY, "You're doing it again!"); stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.PRAYING_MANTIS_6798, NPCs.PRAYING_MANTIS_6799)
}
