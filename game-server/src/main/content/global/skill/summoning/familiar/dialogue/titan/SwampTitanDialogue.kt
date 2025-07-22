package content.global.skill.summoning.familiar.dialogue.titan

import core.api.inInventory
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * The type Swamp titan dialogue.
 */
@Initializable
class SwampTitanDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = SwampTitanDialogue(player)

    /**
     * Instantiates a new Swamp titan dialogue.
     */
    constructor()

    /**
     * Instantiates a new Swamp titan dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (inInventory(player, Items.SWAMP_TAR_1939, 1)) {
            npcl(
                FaceAnim.CHILD_NORMAL,
                "Do you smell that? Swamp tar, master. I LOVE the smell of swamp tar in the morning. Smells like...victorin.",
            )
            stage = 0
            return true
        }
        when ((Math.random() * 4).toInt()) {
            0 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I'm alone, all alone I say.")
                stage = 4
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Oh, Guthix! I'm so alone, I have no fr")
                stage = 11
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Are you my friend, master?")
                stage = 23
            }

            3 -> {
                playerl(FaceAnim.FRIENDLY, "Cheer up, it might never happen!")
                stage = 27
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
                playerl(FaceAnim.FRIENDLY, "You actually LIKE the smell of this stuff? It's gross.")
                stage++
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Of course! I am made of swamp, after all.")
                stage++
            }

            2 -> {
                playerl(
                    FaceAnim.FRIENDLY,
                    "Oh, I'm sorry. I didn't mean...I meant the swamp tar itself smells gross, not you. You smell like lavender. Yes, lavender.",
                )
                stage++
            }

            3 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "*sob* Lavender? Lavender! Why would you be so mean? I'm supposed to smell bad.",
                )
                stage = END_DIALOGUE
            }

            4 -> {
                playerl(FaceAnim.FRIENDLY, "Oh, stop being so melodramatic.")
                stage++
            }

            5 -> {
                npcl(FaceAnim.CHILD_NORMAL, "It's not easy being greenery...well, decomposing greenery.")
                stage++
            }

            6 -> {
                playerl(
                    FaceAnim.HALF_ASKING,
                    "Surely, you're not the only swamp...thing in the world? What about the other swamp titans?",
                )
                stage++
            }

            7 -> {
                npcl(FaceAnim.CHILD_NORMAL, "They're not my friends...they pick on me...they're so mean...")
                stage++
            }

            8 -> {
                playerl(FaceAnim.ASKING, "Why would they do that?")
                stage++
            }

            9 -> {
                npcl(FaceAnim.CHILD_NORMAL, "They think I DON'T smell.")
                stage++
            }

            10 -> {
                playerl(FaceAnim.FRIENDLY, "Oh, yes. That is, er, mean...")
                stage = END_DIALOGUE
            }

            11 -> {
                playerl(FaceAnim.FRIENDLY, "Oh, not again. Look, I'll be your friend.")
                stage++
            }

            12 -> {
                npcl(FaceAnim.CHILD_NORMAL, "You'll be my friend, master?")
                stage++
            }

            13 -> {
                playerl(FaceAnim.FRIENDLY, "Yeah, sure, why not.")
                stage++
            }

            14 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Really?")
                stage++
            }

            15 -> {
                playerl(FaceAnim.FRIENDLY, "Really really...")
                stage++
            }

            16 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Oh, I'm so happy!")
                stage++
            }

            17 -> {
                playerl(FaceAnim.FRIENDLY, "...even if you do smell like a bog of eternal stench.")
                stage++
            }

            18 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Wait...you think I smell bad?")
                stage++
            }

            19 -> {
                playerl(FaceAnim.FRIENDLY, "Erm, yes, but I didn't me")
                stage++
            }

            20 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Oh, that's the nicest thing anyone's ever said to me! Thank you, master, thank you so much.",
                )
                stage++
            }

            21 -> {
                npcl(FaceAnim.CHILD_NORMAL, "You're my friend AND you think I smell. I'm so very happy!")
                stage++
            }

            22 -> {
                playerl(FaceAnim.FRIENDLY, "I guess I did mean it like that.")
                stage = END_DIALOGUE
            }

            23 -> {
                playerl(FaceAnim.ASKING, "Of course I am. I summoned you, didn't I?")
                stage++
            }

            24 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Yes, but that was just to do some fighting. When you're done with me you'll send me back.",
                )
                stage++
            }

            25 -> {
                playerl(FaceAnim.FRIENDLY, "I'm sure I'll need you again later.")
                stage++
            }

            26 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Please don't send me back.")
                stage = END_DIALOGUE
            }

            27 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Oh, why did you have to go and say something like that?")
                stage++
            }

            28 -> {
                playerl(FaceAnim.FRIENDLY, "Like what? I'm trying to cheer you up.")
                stage++
            }

            29 -> {
                npcl(FaceAnim.CHILD_NORMAL, "There's no hope for me, oh woe, oh woe.")
                stage++
            }

            30 -> {
                playerl(FaceAnim.FRIENDLY, "I'll leave you alone, then.")
                stage++
            }

            31 -> {
                npcl(FaceAnim.CHILD_NORMAL, "NO! Don't leave me, master!")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SWAMP_TITAN_7329, NPCs.SWAMP_TITAN_7330)
}
