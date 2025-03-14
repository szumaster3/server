package content.region.misthalin.dialogue.draynor

import core.api.quest.updateQuestTab
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class VeronicaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        val quest = player.getQuestRepository().getQuest(Quests.ERNEST_THE_CHICKEN)
        when (quest.getStage(player)) {
            0 -> {
                npc(FaceAnim.HALF_ASKING, "Can you please help me? I'm in a terrible spot of", "trouble.")
                stage = 0
            }

            10 -> {
                npc(FaceAnim.HALF_ASKING, "Have you found my sweetheart yet?")
                stage = 0
            }

            20 -> {
                npc(FaceAnim.HALF_ASKING, "Have you found my sweetheart yet?")
                stage = 0
            }

            100 -> {
                npc(FaceAnim.HALF_ASKING, "Thank you for rescuing Ernest.")
                stage = 0
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        val quest = player.getQuestRepository().getQuest(Quests.ERNEST_THE_CHICKEN)
        when (quest.getStage(player)) {
            0 ->
                when (stage) {
                    0 -> {
                        options("Aha, sounds like a quest. I'll help.", "No, I'm looking for something to kill.")
                        stage = 1
                    }

                    1 ->
                        when (buttonId) {
                            1 -> {
                                player(FaceAnim.HALF_GUILTY, "Aha, sounds like a quest. I'll help.")
                                stage = 4
                            }

                            2 -> {
                                player(FaceAnim.HALF_GUILTY, "No, I'm looking for something to kill.")
                                stage = 2
                            }
                        }

                    2 -> {
                        npc(FaceAnim.HALF_GUILTY, "Oooh, you violent person you.")
                        stage = 3
                    }

                    3 -> end()
                    4 -> {
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "Yes yes, I suppose it is a quest. My fiance Ernest and",
                            "I came upon this house.",
                        )
                        stage = 5
                    }

                    5 -> {
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "Seeing as we were a little lost Ernest decided to go in",
                            "and ask for direction.",
                        )
                        stage = 6
                    }

                    6 -> {
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "That was an hour ago. That house looks spooky, can",
                            "you go and see if you can find him for me?",
                        )
                        stage = 7
                    }

                    7 -> {
                        player(FaceAnim.HALF_GUILTY, "Ok, I'll see what I can do.")
                        stage = 8
                    }

                    8 -> {
                        npc(FaceAnim.HALF_GUILTY, "Thank you, thank you. I'm very grateful.")
                        quest.start(player)
                        updateQuestTab(player)
                        stage = 9
                    }

                    9 -> end()
                }

            10 ->
                when (stage) {
                    9 -> end()
                    0 -> {
                        player(FaceAnim.HALF_GUILTY, "No, not yet.")
                        stage = 1
                    }

                    1 -> end()
                }

            20 ->
                when (stage) {
                    0 -> {
                        player(FaceAnim.HALF_GUILTY, "Yes, he's a chicken.")
                        stage = 1
                    }

                    1 -> {
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "I know he's not exactly brave but I think you're being",
                            "a bit harsh.",
                        )
                        stage = 2
                    }

                    2 -> {
                        player(
                            FaceAnim.HALF_GUILTY,
                            "No no, he's been turned into an actual chicken by a",
                            "mad scientist.",
                        )
                        stage = 3
                    }

                    3 -> {
                        npc(FaceAnim.HALF_GUILTY, "Eeeeeek!")
                        stage = 4
                    }

                    4 -> {
                        npc(FaceAnim.HALF_GUILTY, "My poor darling, why must these things happen to us.")
                        stage = 5
                    }

                    5 -> {
                        player(FaceAnim.HALF_GUILTY, "Well I'm doing my best to turn him back.")
                        stage = 6
                    }

                    6 -> {
                        npc(FaceAnim.HALF_GUILTY, "Well be quick, I'm sure being a chicken can't be good", "for him.")
                        stage = 7
                    }

                    7 -> end()
                }

            100 ->
                when (stage) {
                    0 -> {
                        player(FaceAnim.HALF_GUILTY, "Where is he now?")
                        stage = 1
                    }

                    1 -> {
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "Oh he went off to talk to some green warty guy. I'm",
                            "sure he'll be back soon.",
                        )
                        stage = 2
                    }

                    2 -> end()
                }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return VeronicaDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.VERONICA_285)
    }
}
