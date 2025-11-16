package content.global.skill.summoning.familiar.dialogue

import core.api.anyInEquipment
import core.api.anyInInventory
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the Thorny Snail familiar dialogue.
 */
@Initializable
class ThornySnailDialogue : Dialogue {

    private var branch = -1

    override fun newInstance(player: Player?): Dialogue = ThornySnailDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        branch = if (anyInInventory(player, *snelmID) || anyInEquipment(player, *snelmID)) {
            0
        } else {
            (1..4).random()
        }

        stage = when (branch) {
            0 -> 0
            1 -> 7
            2 -> 12
            3 -> 15
            4 -> 20
            else -> 0
        }

        when (branch) {
            0 -> npcl(FaceAnim.OLD_NORMAL, "...")
            1 -> npcl(FaceAnim.OLD_NORMAL, "All this running around the place is fun!")
            2 -> npcl(FaceAnim.CHILD_NORMAL, "I think my stomach is drying out...")
            3 -> npcl(FaceAnim.CHILD_NORMAL, "Okay, I have to ask, what are those things you people totter about on?")
            4 -> npcl(FaceAnim.OLD_NORMAL, "Can you slow down?")
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> when (stage) {
                0 -> { npcl(FaceAnim.CHILD_NORMAL, "..."); stage++ }
                1 -> { playerl(FaceAnim.FRIENDLY, "What's the matter?"); stage++ }
                2 -> { npcl(FaceAnim.CHILD_NORMAL, "Check your head..."); stage++ }
                3 -> { playerl(FaceAnim.FRIENDLY, "What about it... Oh, wait! Oh, this is pretty awkward..."); stage++ }
                4 -> { npcl(FaceAnim.CHILD_NORMAL, "You're wearing the spine of one of my relatives as a hat..."); stage++ }
                5 -> { playerl(FaceAnim.FRIENDLY, "Well more of a faux-pas, then."); stage++ }
                6 -> { npcl(FaceAnim.CHILD_NORMAL, "Just a bit..."); stage = END_DIALOGUE }
            }

            1 -> when (stage) {
                7 -> { playerl(FaceAnim.FRIENDLY, "I'll bet it's a step up from your usually sedentary lifestyle!"); stage++ }
                8 -> { npcl(FaceAnim.OLD_NORMAL, "True, but it's mostly seeing the sort of sights you don't get at home."); stage++ }
                9 -> { playerl(FaceAnim.FRIENDLY, "Such as?"); stage++ }
                10 -> { npcl(FaceAnim.OLD_NORMAL, "Living things for a start."); stage++ }
                11 -> { playerl(FaceAnim.FRIENDLY, "Those are in short supply in Mort Myre, I admit."); stage = END_DIALOGUE }
            }

            2 -> when (stage) {
                12 -> { playerl(FaceAnim.FRIENDLY, "Your stomach? How do you know how it's feeling?"); stage++ }
                13 -> { npcl(FaceAnim.OLD_NORMAL, "I am walking on it, you know..."); stage++ }
                14 -> { playerl(FaceAnim.FRIENDLY, "Urrgh..."); stage = END_DIALOGUE }
            }

            3 -> when (stage) {
                15 -> { playerl(FaceAnim.HALF_ASKING, "You mean my legs?"); stage++ }
                16 -> { npcl(FaceAnim.OLD_NORMAL, "Yes, those. How are you supposed to eat anything through them?"); stage++ }
                17 -> { playerl(FaceAnim.FRIENDLY, "Well, we don't. That's what our mouths are for."); stage++ }
                18 -> { npcl(FaceAnim.OLD_NORMAL, "Oh, right! I thought those were for expelling waste gas and hot air!"); stage++ }
                19 -> { playerl(FaceAnim.HALF_ASKING, "Well, for a lot of people they are."); stage = END_DIALOGUE }
            }

            4 -> when (stage) {
                20 -> { playerl(FaceAnim.FRIENDLY, "Are we going too fast for you?"); stage++ }
                21 -> { npcl(FaceAnim.OLD_NORMAL, "I bet if you had to run on your internal organs you'd want a break now and then!"); stage = END_DIALOGUE }
            }
        }

        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.THORNY_SNAIL_6806, NPCs.THORNY_SNAIL_6807)

    companion object {
        private val snelmID = intArrayOf(
            Items.BLAMISH_MYRE_SHELL_3345,
            Items.MYRE_SNELM_3327,
            Items.BLAMISH_MYRE_SHELL_3355,
            Items.MYRE_SNELM_3337,
            Items.BLAMISH_OCHRE_SHELL_3349,
            Items.OCHRE_SNELM_3341,
            Items.BLAMISH_OCHRE_SHELL_3359,
            Items.BLAMISH_RED_SHELL_3347,
            Items.BLOODNTAR_SNELM_3329,
            Items.BLAMISH_RED_SHELL_3357,
            Items.BLOODNTAR_SNELM_3339,
            Items.BLAMISH_BLUE_SHELL_3351,
            Items.BRUISE_BLUE_SNELM_3333,
            Items.BLAMISH_BLUE_SHELL_3361,
            Items.BRUISE_BLUE_SNELM_3343,
            Items.BLAMISH_BARK_SHELL_3353,
            Items.BROKEN_BARK_SNELM_3335,
        )
    }
}
