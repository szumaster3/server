package content.global.skill.summoning.familiar.dialogue

import core.api.inEquipment
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * The type Minotaur dialogue.
 */
@Initializable
class MinotaurDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = MinotaurDialogue(player)

    /**
     * Instantiates a new Minotaur dialogue.
     */
    constructor()

    /**
     * Instantiates a new Minotaur dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (inEquipment(player, Items.GUTHANS_HELM_4724, 1)) {
            npcl(FaceAnim.CHILD_NORMAL, "...")
            stage = 0
            return true
        }
        when ((Math.random() * 4).toInt()) {
            0 -> {
                npcl(FaceAnim.CHILD_NORMAL, "All this walking about is making me angry.")
                stage = 6
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Can you tell me why we're not fighting yet?")
                stage = 10
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Hey, no-horns?")
                stage = 12
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Hey no-horns!")
                stage = 18
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
                playerl(FaceAnim.HALF_ASKING, "What?")
                stage++
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Are you having a laugh?")
                stage++
            }

            2 -> {
                playerl(FaceAnim.FRIENDLY, "I'm not sure I know what you-")
                stage++
            }

            3 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Listen, no-horns, you have two choices: take off the horns yourself or I'll headbutt you until they fall off.",
                )
                stage++
            }

            4 -> {
                playerl(FaceAnim.FRIENDLY, "Yessir.")
                stage++
            }

            5 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Good, no-horns. Let's not have this conversation again.")
                stage = END_DIALOGUE
            }

            6 -> {
                playerl(FaceAnim.FRIENDLY, "You seem to be quite happy about that.")
                stage++
            }

            7 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Yeah! There's nothing like getting a good rage on and then working it out on some no-horns.",
                )
                stage++
            }

            8 -> {
                playerl(FaceAnim.FRIENDLY, "I can't say I know what you mean.")
                stage++
            }

            9 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Well I didn't think a no-horns like you would get it!")
                stage = END_DIALOGUE
            }

            10 -> {
                playerl(FaceAnim.FRIENDLY, "Buck up; I'll find you something to hit soon.")
                stage++
            }

            11 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "You'd better, no-horns, because that round head of yours is looking mighty axeable.",
                )
                stage = END_DIALOGUE
            }

            12 -> {
                playerl(FaceAnim.HALF_ASKING, "Why do you keep calling me no-horns?")
                stage++
            }

            13 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Do I really have to explain that?")
                stage++
            }

            14 -> {
                playerl(FaceAnim.FRIENDLY, "No, thinking about it, it's pretty self-evident.")
                stage++
            }

            15 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Glad we're on the same page, no-horns.")
                stage++
            }

            16 -> {
                playerl(FaceAnim.FRIENDLY, "So, what did you want?")
                stage++
            }

            17 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I've forgotten, now. I'm sure it'll come to me later.")
                stage = END_DIALOGUE
            }

            18 -> {
                playerl(FaceAnim.HALF_ASKING, "Yes?")
                stage++
            }

            19 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Oh, I don't have anything to say, I was just yelling at you.")
                stage++
            }

            20 -> {
                playerl(FaceAnim.HALF_ASKING, "Why?")
                stage++
            }

            21 -> {
                npcl(FaceAnim.CHILD_NORMAL, "No reason. I do like to mess with the no-horns, though.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray =
        intArrayOf(
            NPCs.BRONZE_MINOTAUR_6853,
            NPCs.BRONZE_MINOTAUR_6854,
            NPCs.IRON_MINOTAUR_6855,
            NPCs.IRON_MINOTAUR_6856,
            NPCs.STEEL_MINOTAUR_6857,
            NPCs.STEEL_MINOTAUR_6858,
            NPCs.MITHRIL_MINOTAUR_6859,
            NPCs.MITHRIL_MINOTAUR_6860,
            NPCs.ADAMANT_MINOTAUR_6861,
            NPCs.ADAMANT_MINOTAUR_6862,
            NPCs.RUNE_MINOTAUR_6863,
            NPCs.RUNE_MINOTAUR_6864,
        )
}
