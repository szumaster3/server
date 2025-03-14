package content.region.kandarin.handlers.guilds.wizards

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.global.Skillcape
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * Robe store dialogue.
 */
@Initializable
class RobeStoreDialogue(
    player: Player? = null,
) : Dialogue(player) {
    /*
     * Info: Sells mystic equipment in the Wizards' Guild through
     * the Mystic Robes store. He is located on the 1st floor of
     * the guild, next to the Magic Store owner.
     */

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (Skillcape.isMaster(player, Skills.MAGIC)) {
            options("Ask about Skillcape.", "Something else").also { stage = 3 }
        } else {
            npc("Welcome to the Magic Guild Store. Would you like to", "buy some magic supplies?").also { stage = 0 }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("Yes please.", "No thank you.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player("Yes please.").also { stage++ }
                    2 -> player("No thank you.").also { stage = END_DIALOGUE }
                }

            2 -> {
                end()
                openNpcShop(player, NPCs.ROBE_STORE_OWNER_1658)
            }

            3 ->
                when (buttonId) {
                    1 -> player("Can I buy a Skillcape of Magic?").also { stage++ }
                    2 ->
                        npc("Welcome to the Magic Guild Store. Would you like to", "buy some magic supplies?").also {
                            stage =
                                0
                        }
                }

            4 -> npc("Certainly! Right when you give me 99000 coins.").also { stage++ }
            5 -> options("Okay, here you go.", "No, thanks.").also { stage++ }
            6 ->
                when (buttonId) {
                    1 -> player("Okay, here you go.").also { stage++ }
                    2 -> player("No, thanks.").also { stage = END_DIALOGUE }
                }

            7 ->
                if (Skillcape.purchase(player, Skills.MAGIC)) {
                    npc("There you go! Enjoy.").also { stage = END_DIALOGUE }
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ROBE_STORE_OWNER_1658)
    }
}
