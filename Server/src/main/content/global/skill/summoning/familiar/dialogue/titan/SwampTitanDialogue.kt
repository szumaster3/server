package content.global.skill.summoning.familiar.dialogue.titan

import core.api.inInventory
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import kotlin.random.Random

/**
 * Represents the Swamp Titan familiar dialogue.
 */
@Initializable
class SwampTitanDialogue : Dialogue {

    private var branch: Int = 0

    override fun newInstance(player: Player?) = SwampTitanDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        branch = if (inInventory(player, Items.SWAMP_TAR_1939, 1)) {
            0
        } else {
            Random.nextInt(1, 4)
        }
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> {
                when (stage) {
                    0 -> { npcl(FaceAnim.CHILD_NORMAL, "Do you smell that? Swamp tar, master. I LOVE the smell of swamp tar in the morning. Smells like...victorin."); stage++ }
                    1 -> { playerl(FaceAnim.FRIENDLY, "You actually LIKE the smell of this stuff? It's gross."); stage++ }
                    2 -> { npcl(FaceAnim.CHILD_NORMAL, "Of course! I am made of swamp, after all."); stage++ }
                    3 -> { playerl(FaceAnim.FRIENDLY, "Oh, I'm sorry. I didn't mean...I meant the swamp tar itself smells gross, not you. You smell like lavender. Yes, lavender."); stage++ }
                    4 -> { npcl(FaceAnim.CHILD_NORMAL, "*sob* Lavender? Lavender! Why would you be so mean? I'm supposed to smell bad."); stage = END_DIALOGUE }
                }
            }

            1 -> {
                when (stage) {
                    0 -> { npcl(FaceAnim.CHILD_NORMAL, "I'm alone, all alone I say."); stage++ }
                    1 -> { playerl(FaceAnim.FRIENDLY, "Oh, stop being so melodramatic."); stage++ }
                    2 -> { npcl(FaceAnim.CHILD_NORMAL, "It's not easy being greenery...well, decomposing greenery."); stage++ }
                    3 -> { playerl(FaceAnim.HALF_ASKING, "Surely, you're not the only swamp...thing in the world? What about the other swamp titans?"); stage++ }
                    4 -> { npcl(FaceAnim.CHILD_NORMAL, "They're not my friends...they pick on me...they're so mean..."); stage++ }
                    5 -> { playerl(FaceAnim.ASKING, "Why would they do that?"); stage++ }
                    6 -> { npcl(FaceAnim.CHILD_NORMAL, "They think I DON'T smell."); stage++ }
                    7 -> { playerl(FaceAnim.FRIENDLY, "Oh, yes. That is, er, mean..."); stage = END_DIALOGUE }
                }
            }

            2 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "Oh, not again. Look, I'll be your friend."); stage++ }
                    1 -> { npcl(FaceAnim.CHILD_NORMAL, "You'll be my friend, master?"); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "Yeah, sure, why not."); stage++ }
                    3 -> { npcl(FaceAnim.CHILD_NORMAL, "Really?"); stage++ }
                    4 -> { playerl(FaceAnim.FRIENDLY, "Really really..."); stage++ }
                    5 -> { npcl(FaceAnim.CHILD_NORMAL, "Oh, I'm so happy!"); stage++ }
                    6 -> { playerl(FaceAnim.FRIENDLY, "...even if you do smell like a bog of eternal stench."); stage++ }
                    7 -> { npcl(FaceAnim.CHILD_NORMAL, "Wait...you think I smell bad?"); stage++ }
                    8 -> { playerl(FaceAnim.FRIENDLY, "Erm, yes, but I didn't me"); stage++ }
                    9 -> { npcl(FaceAnim.CHILD_NORMAL, "Oh, that's the nicest thing anyone's ever said to me! Thank you, master, thank you so much."); stage++ }
                    10 -> { npcl(FaceAnim.CHILD_NORMAL, "You're my friend AND you think I smell. I'm so very happy!"); stage++ }
                    11 -> { playerl(FaceAnim.FRIENDLY, "I guess I did mean it like that."); stage = END_DIALOGUE }
                }
            }

            3 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "Cheer up, it might never happen!"); stage++ }
                    1 -> { npcl(FaceAnim.CHILD_NORMAL, "Oh, why did you have to go and say something like that?"); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "Like what? I'm trying to cheer you up."); stage++ }
                    3 -> { npcl(FaceAnim.CHILD_NORMAL, "There's no hope for me, oh woe, oh woe."); stage++ }
                    4 -> { playerl(FaceAnim.FRIENDLY, "I'll leave you alone, then."); stage++ }
                    5 -> { npcl(FaceAnim.CHILD_NORMAL, "NO! Don't leave me, master!"); stage = END_DIALOGUE }
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SWAMP_TITAN_7329, NPCs.SWAMP_TITAN_7330)
}