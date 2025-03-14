package content.region.kandarin.quest.fishingcompo.dialogue

import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class BigDaveDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc("Hey lad! Always nice to see a fresh face!")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                options(
                    "So you're the champ?",
                    "Can I fish here instead of you?",
                    "Do you have any tips for me?",
                )
                stage++
            }

            1 ->
                when (buttonId) {
                    1 -> {
                        player("So you're the champ?")
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
                npc("That's right, lad!", "Ain't nobody better at fishing round here", "than me! That's for sure!")
                stage = 9
            }

            20 -> {
                npc("Sorry lad! This is my lucky spot!")
                stage = 9
            }

            30 -> {
                npc(
                    "Why would I help you? I wanna stay the best!",
                    "I'm not givin' away my secrets like",
                    "old Grandpa Jack does!",
                )
                stage++
            }

            31 -> {
                player("Who's Grandpa Jack?")
                stage++
            }

            32 -> {
                npc(
                    "You really have no clue do you!",
                    "He won this competition four years in a row!",
                    "He lives in the house just outside the gate.",
                )
                stage = 9
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BIG_DAVE_228)
    }
}
