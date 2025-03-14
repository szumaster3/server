package content.region.asgarnia.dialogue.portsarim

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class WydinDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        var door = false
        if (args.size == 2) door = args[1] as Boolean
        if (door) {
            if (player.getSavedData().globalData.isWydinEmployee()) {
                npc(FaceAnim.HALF_GUILTY, "Can you put your white apron on before going in there", "please.")
                stage = END_DIALOGUE
            } else {
                npc(FaceAnim.ANGRY, "Hey, you can't go in there. Only employees of the", "grocery store can go in.")
                stage = END_DIALOGUE
            }
            return true
        } else {
            npc = args[0] as NPC
        }
        if (player.getSavedData().globalData.isWydinEmployee()) {
            npc(FaceAnim.ASKING, "Is it nice and tidy round the back now?")
            stage = 0
        } else {
            npc(FaceAnim.HAPPY, "Welcome to my food store! Would you like to buy", "anything?")
            stage = 0
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        if (player.getSavedData().globalData.isWydinEmployee()) {
            when (stage) {
                0 -> {
                    options(
                        "Yes, can I work out front now?",
                        "Yes, are you going to pay me yet?",
                        "No, it's a complete mess.",
                        "Can I buy something please?",
                    )
                    stage = 1
                }

                1 ->
                    when (buttonId) {
                        1 -> {
                            player(FaceAnim.ASKING, "Yes, can I work out front now?")
                            stage = 10
                        }

                        2 -> {
                            player(FaceAnim.ASKING, "Yes, are you going to pay me yet?")
                            stage = 20
                        }

                        3 -> {
                            player(FaceAnim.HALF_GUILTY, "No, it's a complete mess.")
                            stage = 30
                        }

                        4 -> {
                            player(FaceAnim.HALF_GUILTY, "Can I buy something please?")
                            stage = 40
                        }
                    }

                10 -> {
                    npc(FaceAnim.NEUTRAL, "No, I'm the one who works here.")
                    stage = END_DIALOGUE
                }

                20 -> {
                    npc(FaceAnim.THINKING, "Umm... No, not yet.")
                    stage = END_DIALOGUE
                }

                30 -> {
                    player(FaceAnim.HALF_GUILTY, "No, it's a complete mess.")
                    stage = 31
                }

                31 -> {
                    npc(FaceAnim.THINKING, "Ah well, it'll give you something to do, won't it.")
                    stage = END_DIALOGUE
                }

                40 -> {
                    npc(FaceAnim.HAPPY, "Yes, ofcourse.")
                    stage = 41
                }

                41 -> {
                    end()
                    openNpcShop(player, npc.id)
                }
            }
        } else {
            when (stage) {
                0 -> {
                    options("Yes please.", "No, thank you.", "What can you recommend?", "Can I get a job here?")
                    stage = 1
                }

                1 ->
                    when (buttonId) {
                        1 -> {
                            player(FaceAnim.HAPPY, "Yes please.")
                            stage = 10
                        }

                        2 -> {
                            player(FaceAnim.HALF_GUILTY, "No, thank you.")
                            stage = END_DIALOGUE
                        }

                        3 -> {
                            player(FaceAnim.ASKING, "What can you recommend?")
                            stage = 30
                        }

                        4 -> {
                            player(FaceAnim.ASKING, "Can I get a job here?")
                            stage = 40
                        }
                    }

                10 -> {
                    end()
                    openNpcShop(player, npc.id)
                }

                30 -> {
                    npc(
                        FaceAnim.HAPPY,
                        "We have this really exotic fruit all the way from",
                        "Karamja. It's called a banana.",
                    )
                    stage = 31
                }

                31 -> {
                    options("Hmm, I think I'll try one.", "I don't like the sound of that.")
                    stage = 32
                }

                32 ->
                    when (buttonId) {
                        1 -> {
                            player(FaceAnim.FRIENDLY, "Hmm, I think I'll try one.")
                            stage = 10
                        }

                        2 -> {
                            player(FaceAnim.HALF_GUILTY, "I don't like the sound of that.")
                            stage = END_DIALOGUE
                        }
                    }

                40 -> {
                    npc(
                        FaceAnim.HAPPY,
                        "Well, you're keen, I'll give you that. Okay, I'll give you",
                        "a go. Have you got your own white apron?",
                    )
                    stage = 41
                }

                41 ->
                    if (!player.inventory.contains(1005, 1) &&
                        !player.equipment.contains(1005, 1) &&
                        !player.bank.contains(1005, 1)
                    ) {
                        player(FaceAnim.SAD, "No, I haven't.")
                        stage = 42
                    } else {
                        player(FaceAnim.HAPPY, "Yes, I have one.")
                        stage = 50
                    }

                42 -> {
                    npc(
                        FaceAnim.FRIENDLY,
                        "Well, you can't work here unless you have a white",
                        "apron. Health and safety regulations, you understand.",
                    )
                    stage = 43
                }

                43 -> {
                    player(FaceAnim.ASKING, "Where can I get one of those?")
                    stage = 44
                }

                44 -> {
                    npc(
                        FaceAnim.FRIENDLY,
                        "Well, I get all of mine over at the clothing shop in",
                        "Varrock. They sell them cheap there.",
                    )
                    stage = 45
                }

                45 -> {
                    npc(
                        FaceAnim.FRIENDLY,
                        "Oh, and I'm sure that I've seen a spare one over in",
                        "Gerrant's fish store somewhere. It's the little place just",
                        "north of here.",
                    )
                    stage = END_DIALOGUE
                }

                50 -> {
                    player.getSavedData().globalData.setWydinEmployee(true)
                    npc(
                        FaceAnim.HAPPY,
                        "Wow - you are well prepared! You're hired. Go through",
                        "to the back and tidy up for me, please.",
                    )
                    stage = END_DIALOGUE
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.WYDIN_557)
    }
}
