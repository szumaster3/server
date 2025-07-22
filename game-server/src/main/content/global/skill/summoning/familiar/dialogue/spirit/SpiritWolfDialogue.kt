package content.global.skill.summoning.familiar.dialogue.spirit

import content.global.skill.prayer.Bones
import core.api.anyInInventory
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * The type Spirit wolf dialogue.
 */
@Initializable
class SpiritWolfDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = SpiritWolfDialogue(player)

    /**
     * Instantiates a new Spirit wolf dialogue.
     */
    constructor()

    /**
     * Instantiates a new Spirit wolf dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (anyInInventory(player, *bones.stream().mapToInt { obj: Int -> obj.toInt() }.toArray())) {
            npc(FaceAnim.CHILD_NORMAL, "Whuff-Whuff! Arf!", "(Throw the bone! I want to chase it!)")
            stage = 0
            return true
        }
        when ((Math.random() * 4).toInt()) {
            0 -> {
                npc(FaceAnim.CHILD_NORMAL, "Whurf?", "(What are you doing?)")
                stage = 1
            }

            1 -> {
                npc(FaceAnim.CHILD_NORMAL, "Bark Bark!", "(Danger!)")
                stage = 2
            }

            2 -> {
                npc(FaceAnim.CHILD_NORMAL, "Whuff whuff. Pantpant awff!", "(I smell something good! Hunting time!)")
                stage = 4
            }

            3 -> {
                npc(FaceAnim.CHILD_NORMAL, "Pant pant whine?", "(When am I going to get to chase something?)")
                stage = 5
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
                playerl(FaceAnim.FRIENDLY, "I can't just throw bones away - I need them to train my Prayer!")
                stage = END_DIALOGUE
            }

            1 -> {
                playerl(FaceAnim.FRIENDLY, "Oh, just some... biped things. I'm sure it would bore you.")
                stage = END_DIALOGUE
            }

            2 -> {
                playerl(FaceAnim.FRIENDLY, "Where?!")
                stage++
            }

            3 -> {
                npc(FaceAnim.CHILD_NORMAL, "Whiiiine...", "(False alarm...)")
                stage = END_DIALOGUE
            }

            4 -> {
                playerl(
                    FaceAnim.FRIENDLY,
                    "We can go hunting in a moment. I just have to take care of something first.",
                )
                stage = END_DIALOGUE
            }

            5 -> {
                playerl(FaceAnim.FRIENDLY, "Oh, I'm sure we'll find something for you in a bit.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SPIRIT_WOLF_6829, NPCs.SPIRIT_WOLF_6830)

    companion object {
        private val bones: MutableList<Int> = ArrayList()

        init {
            for (bone in Bones.values()) {
                bones.add(bone.itemId)
            }
        }
    }
}
