package content.global.skill.summoning.familiar.dialogue

import core.api.anyInEquipment
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import java.util.*

/**
 * Represents the Desert Wyrm familiar dialogues.
 */
@Initializable
class DesertWyrmDialogue : Dialogue {

    private var branch = -1

    override fun newInstance(player: Player?): Dialogue = DesertWyrmDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (anyInEquipment(player, *PICKAXE)) {
            branch = 0
            npcl(FaceAnim.FAMILIAR_NEUTRAL, "If you have that pick, why make me dig?")
            stage = 0
            return true
        }

        if (branch == -1) branch = (Random().nextInt(4) + 1)
        stage = 0

        when (branch) {
            1 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "This is so unsafe...I should have a hard hat for this work...")
            2 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "You can't touch me, I'm part of the union!")
            3 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "You know, you might want to register with the union.")
            4 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Why are you ignoring that good ore seam, " + player.username + "?")
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "Because it's a little quicker and easier on my arms."); stage++ }
                    1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "I should take industrial action over this..."); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "You mean you won't work for me any more?"); stage++ }
                    3 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "No. It means me and the lads feed you legs-first into some industrial machinery, maybe the Blast Furnace."); stage++ }
                    4 -> { playerl(FaceAnim.FRIENDLY, "I'll just be over here, digging."); stage++ }
                    5 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "That's the spirit, lad!"); stage = END_DIALOGUE }
                }
            }
            1 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "Well, I could get you a rune helm if you like - those are pretty hard."); stage++ }
                    1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Keep that up and you'll have the union on your back, " + player.username + "."); stage = END_DIALOGUE }
                }
            }
            2 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.HALF_ASKING, "Is that some official no touching policy or something?"); stage++ }
                    1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "You really don't get it, do you " + player.username + "?"); stage = END_DIALOGUE }
                }
            }
            3 -> {
                when (stage) {
                    0 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "I stop bugging you to join the union."); stage++ }
                    1 -> { playerl(FaceAnim.FRIENDLY, "Ask that again later; I'll have to consider that generous proposal."); stage = END_DIALOGUE }
                }
            }
            4 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.HALF_ASKING, "Which ore seam?"); stage++ }
                    1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "There's a good ore seam right underneath us at this very moment."); stage++ }
                    2 -> { playerl(FaceAnim.HALF_ASKING, "Great! How long will it take for you to get to it?"); stage++ }
                    3 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Five years, give or take."); stage++ }
                    4 -> { playerl(FaceAnim.FRIENDLY, "Five years!"); stage++ }
                    5 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "That's if we go opencast, mind. I could probably reach it in three if I just dug."); stage++ }
                    6 -> { playerl(FaceAnim.FRIENDLY, "Right. I see. I think I'll skip it thanks."); stage = END_DIALOGUE }
                }
            }
        }

        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.DESERT_WYRM_6831, NPCs.DESERT_WYRM_6832)

    companion object {
        private val PICKAXE: IntArray = intArrayOf(
            Items.BRONZE_PICKAXE_1265,
            Items.IRON_PICKAXE_1267,
            Items.STEEL_PICKAXE_1269,
            Items.MITHRIL_PICKAXE_1273,
            Items.ADAMANT_PICKAXE_1271,
            Items.RUNE_PICKAXE_1275,
            Items.INFERNO_ADZE_13661
        )
    }
}
