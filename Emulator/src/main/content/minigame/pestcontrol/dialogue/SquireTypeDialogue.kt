package content.minigame.pestcontrol.dialogue

import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.GameWorld.settings
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class SquireTypeDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc("Hi, how can I help you?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("Who are you?", "What's going on here?", "I'm fine thanks.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player("Who are you?").also { stage++ }
                    2 -> player("What's going on here?").also { stage = 5 }
                    3 -> player("I'm fine thanks.").also { stage = END_DIALOGUE }
                }

            2 -> npc("I'm a squire for the Void Knights.").also { stage++ }
            3 -> player("The who?").also { stage++ }
            4 ->
                npc(
                    "The Void Knights, they are great warriors of balance",
                    "who do Guthix's work here in " + settings!!.name + ".",
                ).also { stage = END_DIALOGUE }

            5 ->
                npc(
                    "This is where we launch our lander to combat the",
                    "invasion of the nearby islands. You'll see three lander - ",
                    "one for novice, intermediate and veteran adventurers.",
                    "Each has a different difficulty, but they therefore offer",
                ).also { stage++ }

            6 -> npc("varying rewards.").also { stage++ }
            7 -> player("And this lander is...?").also { stage++ }
            8 ->
                npc(
                    "The " + (
                        if (npc.id ==
                            3802
                        ) {
                            "novice"
                        } else if (npc.id == 6140) {
                            "intermediate"
                        } else {
                            "veteran"
                        }
                    ) + ".",
                ).also { stage++ }
            9 -> player("So how do they work?").also { stage++ }
            10 ->
                npc(
                    "Simple. We send each lander out every five minutes,",
                    "however we need at least five volunteers or it's a suicide",
                    "mission. The lander can only hold a maximum of",
                    "twenty five people though, so we do send them out",
                ).also { stage++ }

            11 ->
                npc(
                    "early if they get full. If you'd be willing to help us then",
                    "just wait in the lander and we'll launch it as soon as it's",
                    "ready. However you will need a combat level of " +
                        (
                            if (npc.id == 3802) {
                                "40"
                            } else if (npc.id == 6140) {
                                "70"
                            } else {
                                "100"
                            }
                        ) +
                        " to",
                    "use this lander.",
                ).also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SQUIRE_NOVICE_3802, NPCs.SQUIRE_INTERMEDIATE_6140, NPCs.SQUIRE_VETERAN_6141)
    }
}
