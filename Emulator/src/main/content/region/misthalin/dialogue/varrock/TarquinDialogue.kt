package content.region.misthalin.dialogue.varrock

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class TarquinDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HALF_GUILTY, "Hello there.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                npc(FaceAnim.HALF_GUILTY, "Hello old bean. Is there something I can help you", "with?")
                stage = 1
            }

            1 -> {
                options("Who are you?", "Can you teach me about Canoeing?")
                stage = 2
            }

            2 ->
                when (buttonId) {
                    1 -> {
                        player(FaceAnim.HALF_GUILTY, "Who are you?")
                        stage = 10
                    }

                    2 -> {
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "It's really quite simple. Just walk down to that tree on",
                            "the water bank and chop it down.",
                        )
                        stage = 24
                    }
                }

            10 -> {
                npc(FaceAnim.HALF_GUILTY, "My name is Tarquin Marjoribanks.")
                stage = 11
            }

            11 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I'd be surprised if you haven't already heard of me?",
                )
                stage = 12
            }

            12 -> {
                player(
                    FaceAnim.HALF_GUILTY,
                    "Why would I have heard of you Mr. Marjoribanks?",
                )
                stage = 13
            }

            13 -> {
                npc(FaceAnim.HALF_GUILTY, "It's pronounced 'Marchbanks'!")
                stage = 14
            }

            14 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "You should know of me because I am a member of the",
                    "royal family of Misthalin!",
                )
                stage = 15
            }

            15 -> {
                player(FaceAnim.HALF_GUILTY, "Are you related to King Roald?")
                stage = 16
            }

            16 -> {
                npc(FaceAnim.HALF_GUILTY, "Oh yes! Quite closely actually.")
                stage = 17
            }

            17 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I'm his 4th cousin, once removed on his mothers side.",
                )
                stage = 18
            }

            18 -> {
                player(
                    FaceAnim.HALF_GUILTY,
                    "Er... Okay. What are you doing here then?",
                )
                stage = 19
            }

            19 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I'm canoeing on the river! It's enormous fun! Would",
                    "you like to know how?",
                )
                stage = 20
            }

            20 -> {
                options("Yes", "No")
                stage = 21
            }

            21 ->
                when (buttonId) {
                    1 -> {
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "It's really quite simple. Just walk down to that tree on",
                            "the water bank and chop it down.",
                        )
                        stage = 24
                    }

                    2 -> end()
                }

            24 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Then take your axe to it and shape it how you like!",
                )
                stage = 26
            }

            26 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "You look like you know your way around a tree,",
                    "you can make many canoes.",
                )
                stage = 27
            }

            27 -> end()
            25 -> end()
            100 -> {}
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TARQUIN_3328)
    }
}
