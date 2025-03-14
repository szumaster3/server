package content.region.kandarin.quest.fishingcompo.dialogue

import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class JoshuaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc("Yeah? What do you want?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                options(
                    "Um... nothing really...",
                    "Can I fish here instead of you?",
                    "Do you have any tips for me?",
                )
                stage++
            }

            1 ->
                when (buttonId) {
                    1 -> {
                        player("Um... nothing really...")
                        stage = 10
                    }

                    2 -> {
                        player("Can I fish here instead of you?")
                        stage = 20
                    }

                    3 -> {
                        player("Do you have any tips for me?")
                        stage = 30
                    }
                }

            9 -> end()
            10 -> {
                npc("Quit bugging me then, dude!", "I got me some fish to catch!")
                stage = 9
            }

            20 -> {
                npc("nuh uh dude. Less talk, more fishing!")
                stage = 9
            }

            30 -> {
                npc(
                    "Dude! Why should I help you?",
                    "You like, might beat me!",
                    "I'm not giving away my secrets like that",
                    "dude Grandpa Jack does!",
                )
                stage++
            }

            31 -> {
                player("Who's Grandpa Jack?")
                stage++
            }

            32 -> {
                npc(
                    "Who's Grandpa Jack you say!",
                    "He won this competition four years in a row!",
                    "He lives in the house just outside the gate.",
                )
                stage = 9
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.JOSHUA_229)
    }
}
