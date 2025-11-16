package content.global.skill.summoning.familiar.dialogue.spirit

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Spirit Tz-Kih familiar dialogue.
 */
@Initializable
class SpiritTzKihDialogue : Dialogue {

    private var branch = -1

    override fun newInstance(player: Player?): Dialogue = SpiritTzKihDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (branch == -1) branch = (Math.random() * 5).toInt()
        stage = 0

        when (branch) {
            0 -> playerl(FaceAnim.FRIENDLY, "How's it going, Tz-kih?")
            1 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Does JalYt think Tz-kih as strong as Jad Jad?")
            2 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Have you heard of blood bat, JalYt?")
            3 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Pray pray pray pray pray pray pray pray!")
            4 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "You drink pray, me drink pray.")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> {
                when (stage) {
                    0 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Pray pray?"); stage++ }
                    1 -> { playerl(FaceAnim.FRIENDLY, "Don't start with all that again."); stage++ }
                    2 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Hmph, silly JalYt."); stage = END_DIALOGUE }
                }
            }

            1 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "Are you as strong as TzTok-Jad? Yeah, sure, why not."); stage++ }
                    1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Really? Thanks, JalYt. Tz-Kih strong and happy."); stage = END_DIALOGUE }
                }
            }

            2 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "Blood bats? You mean vampire bats?"); stage++ }
                    1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Yes. Blood bat."); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "Yes, I've heard of them. What about them?"); stage++ }
                    3 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Tz-Kih like blood bat, but drink pray pray not blood blood. Blood blood is yuck."); stage++ }
                    4 -> { playerl(FaceAnim.FRIENDLY, "Thanks, Tz-Kih, that's nice to know."); stage = END_DIALOGUE }
                }
            }

            3 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "FRIENDLY down, Tz-Kih, we'll find you something to drink soon."); stage++ }
                    1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Pray praaaaaaaaaaaaaay!"); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "Okay, okay. FRIENDLY down!"); stage = END_DIALOGUE }
                }
            }

            4 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "What's that, Tz-Kih?"); stage++ }
                    1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "You got pray pray pot. Tz-Kih drink pray pray you, you drink pray pray pot."); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "You want to drink my Prayer points?"); stage++ }
                    3 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Yes. Pray pray."); stage++ }
                    4 -> { playerl(FaceAnim.FRIENDLY, "Err, not right now, Tz-Kih. I, er, need them myself."); stage++ }
                    5 -> { playerl(FaceAnim.FRIENDLY, "Sorry."); stage++ }
                    6 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "But, pray praaaay...?"); stage = END_DIALOGUE }
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SPIRIT_TZ_KIH_7361, NPCs.SPIRIT_TZ_KIH_7362)
}