package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Stranger Plant familiar dialogue.
 */
@Initializable
class StrangerPlantDialogue : Dialogue {

    private var branch = -1

    override fun newInstance(player: Player?): Dialogue = StrangerPlantDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        branch = (Math.random() * 4).toInt()
        stage = 0

        when (branch) {
            0 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "I'M STRANGER PLANT!")
            1 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "WILL WE HAVE TO BE HERE LONG?")
            2 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "DIIIIVE!")
            3 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "I THINK I'M WILTING!")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "I know you are."); stage++ }
                    1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "I KNOW! I'M JUST SAYING!"); stage++ }
                    2 -> { playerl(FaceAnim.HALF_ASKING, "Do you have to shout like that all of the time?"); stage++ }
                    3 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "WHO'S SHOUTING?"); stage++ }
                    4 -> { playerl(FaceAnim.FRIENDLY, "If this is you speaking normally, I'd hate to hear you shouting."); stage = END_DIALOGUE }
                }
            }
            1 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "We'll be here until I am finished."); stage++ }
                    1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "BUT THERE'S NO DRAMA HERE!"); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "Well, how about you pretend to be an undercover agent."); stage++ }
                    3 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "WONDERFUL! WHAT'S MY MOTIVATION?"); stage++ }
                    4 -> { playerl(FaceAnim.FRIENDLY, "You're trying to remain stealthy and secretive, while looking out for clues."); stage++ }
                    5 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "I'LL JUST GET INTO CHARACTER! AHEM!"); stage++ }
                    6 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "PAPER! PAPER! VARROCK HERALD FOR SALE!"); stage++ }
                    7 -> { playerl(FaceAnim.HALF_ASKING, "What kind of spy yells loudly like that?"); stage++ }
                    8 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "ONE WHOSE COVER IDENTITY IS A PAPER-SELLER, OF COURSE!"); stage++ }
                    9 -> { playerl(FaceAnim.FRIENDLY, "Ask a silly question..."); stage = END_DIALOGUE }
                }
            }
            2 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.HALF_ASKING, "What? Help! Why dive?"); stage++ }
                    1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "OH, DON'T WORRY! I JUST LIKE TO YELL THAT FROM TIME TO TIME!"); stage++ }
                    2 -> { playerl(FaceAnim.HALF_ASKING, "Well, can you give me a little warning next time?"); stage++ }
                    3 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "WHAT, AND TAKE ALL THE FUN OUT OF LIFE?"); stage++ }
                    4 -> { playerl(FaceAnim.FRIENDLY, "If by 'fun' you mean 'sudden heart attacks', then yes, please take them out of my life!"); stage = END_DIALOGUE }
                }
            }
            3 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.HALF_ASKING, "Do you need some water?"); stage++ }
                    1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "DON'T BE SILLY! I CAN PULL THAT OUT OF THE GROUND!"); stage++ }
                    2 -> { playerl(FaceAnim.HALF_ASKING, "Then why are you wilting?"); stage++ }
                    3 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "IT'S SIMPLE: THERE'S A DISTINCT LACK OF DRAMA!"); stage++ }
                    4 -> { playerl(FaceAnim.HALF_ASKING, "Drama?"); stage++ }
                    5 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "YES, DRAMA!"); stage++ }
                    6 -> { playerl(FaceAnim.FRIENDLY, "Okay..."); stage++ }
                    7 -> { playerl(FaceAnim.FRIENDLY, "Let's see if we can find some for you."); stage++ }
                    8 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "LEAD ON!"); stage = END_DIALOGUE }
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.STRANGER_PLANT_6827, NPCs.STRANGER_PLANT_6828)
}