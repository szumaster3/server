package content.region.asgarnia.dialogue.burthope

import core.api.openDialogue
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * Represents the Reff dialogue.
 */
@Initializable
class ReffDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc("Greetings " + (if (player.appearance.isMale) "Sir" else "Madam") + ".")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "Tell me about the Shot Put area.",
                    "May I claim my tokens please?",
                    "Do you have any tips for me?",
                    "Bye!",
                ).also {
                    stage++
                }
            1 ->
                when (buttonId) {
                    1 -> player("Tell me about the Shot Put area.").also { stage = 10 }
                    2 -> {
                        end()
                        openDialogue(player, "wg:claim-tokens", npc.id)
                    }
                    3 -> player("Do you have any tips for me?").also { stage = 30 }
                    4 -> player("Bye!").also { stage = 40 }
                }
            10 ->
                npc(
                    "Of course " + (if (player.appearance.isMale) "Sir" else "Madam") +
                        ". There are two different weights of",
                    "shot...",
                ).also { stage++ }
            11 -> player("Shot?").also { stage++ }
            12 ->
                npc(
                    "Yes Madam. Shot. The iron spheres that are propelled",
                    "by chemical energy stored in your body.",
                ).also {
                    stage++
                }
            13 -> player("The... what?").also { stage++ }
            14 ->
                npc(
                    "Your strength " + (if (player.appearance.isMale) "Sir" else "Madam") +
                        ". The stronger you are, the",
                    "further you can throw the shot, but there are other",
                    "factors of course, like your technique.",
                ).also { stage++ }
            15 -> player("What's that then?").also { stage++ }
            16 ->
                npc(
                    "The style of the shot " + (if (player.appearance.isMale) "Sir" else "Madam") + ".",
                ).also { stage++ }
            17 -> player("Iron has style??").also { stage++ }
            18 -> core.api.sendDialogue(player, "The Referee sighs, rolls his eyes and continues....").also { stage++ }
            19 ->
                npc(
                    (if (player.appearance.isMale) "Sir" else "Madam") +
                        ", the style in which you throw the shot, not the",
                    "style of iron.",
                ).also { stage++ }
            20 -> player("OH! You mean the spinny round thing or the chuck it", "straight?").also { stage++ }
            23 ->
                npc(
                    "Crudely put " + (if (player.appearance.isMale) "Sir" else "Madam") +
                        ", but yes. Some are more difficult",
                    "than others. Experiment and see which you prefer.",
                ).also { stage++ }
            24 -> player("Thanks for the help!").also { stage++ }
            25 ->
                npc("You are welcome " + (if (player.appearance.isMale) "Sir" else "Madam") + ".").also {
                    stage =
                        END_DIALOGUE
                }
            30 -> npc("Tips " + (if (player.appearance.isMale) "Sir" else "Madam") + "?").also { stage++ }
            31 -> player("Yes, like how can I do better than everyone else.").also { stage++ }
            32 ->
                npc(
                    (if (player.appearance.isMale) "Sir" else "Madam") + " may find that a fine powder applied to the",
                    "hands may give one an advantage when putting the",
                    "shot.",
                ).also {
                    stage++
                }
            33 ->
                player(
                    "You mean if I grind something up and put dust on my",
                    "hands I'll chuck the ball further?",
                ).also {
                    stage++
                }
            34 -> npc("Yes " + (if (player.appearance.isMale) "Sir" else "Madam") + ".").also { stage++ }
            35 -> player("Thanks!").also { stage = 25 }
            40 ->
                npc("Good luck " + (if (player.appearance.isMale) "Sir" else "Madam") + ".").also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.REF_4299, NPCs.REF_4300)
    }
}
