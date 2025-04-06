package content.region.asgarnia.dialogue.craftguild

import content.global.skill.crafting.Tanning
import core.api.inInventory
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * The Tanner dialogue.
 */
@Initializable
class TannerDialogue : Dialogue {

    constructor()

    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.NEUTRAL, "Greetings friend. I am a manufacturer of leather.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                var hasHides = false
                for (tanningProduct in Tanning.values()) {
                    if (inInventory(player, tanningProduct.item, 1)) {
                        hasHides = true
                        break
                    }
                }

                if (hasHides) {
                    npcl(
                        FaceAnim.FRIENDLY,
                        "I see you have brought me some hides. Would you like me to tan them for you?"
                    )
                    stage = 10
                } else {
                    options("Can I buy some leather then?", "Leather is rather weak stuff.")
                    stage = 20
                }
            }

            10 -> {
                options("Yes please.", "No thanks.")
                stage++
            }

            11 -> if (buttonId == 1) {
                playerl(FaceAnim.HAPPY, "Yes please.")
                stage = 12
            } else {
                playerl(FaceAnim.NEUTRAL, "No thanks.")
                stage = 13
            }

            12 -> {
                end()
                Tanning.open(player, NPCs.TANNER_804)
            }

            13 -> {
                npcl(FaceAnim.FRIENDLY, "Very well, @g[sir,madam], as you wish.")
                stage = END_DIALOGUE
            }

            20 -> if (buttonId == 1) {
                playerl(FaceAnim.ASKING, "Can I buy some leather?")
                stage = 21
            } else {
                playerl(FaceAnim.SUSPICIOUS, "Leather is rather weak stuff.")
                stage = 22
            }

            21 -> {
                npcl(
                    FaceAnim.FRIENDLY,
                    "I make leather from animal hides. Bring me some cowhides and one gold coin per hide, and I'll tan them into soft leather for you."
                )
                stage = END_DIALOGUE
            }

            22 -> {
                npc(
                    FaceAnim.NOD_YES, "Normal leather may be quite weak, but it's very cheap -",
                    "I make it from cowhides for only 1 gp per hide - and",
                    "it's so easy to craft that anyone can work with it."
                )
                stage++
            }

            23 -> {
                npc(
                    FaceAnim.HALF_THINKING, "Alternatively you could try hard leather. It's not so",
                    "easy to craft, but I only charge 3gp per cowhide to",
                    "prepare it, and it makes much studier armour."
                )
                stage++
            }

            24 -> {
                npc(
                    FaceAnim.HAPPY, "I can also tan snake hides and dragonhides, suitable for",
                    "crafting into the highest quality armour for rangers."
                )
                stage++
            }

            25 -> {
                player(FaceAnim.HALF_GUILTY, "Thanks, I'll bear it in mind.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return TannerDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TANNER_804)
    }
}
