package content.region.karamja.dialogue.tzhaar

import core.api.getAttribute
import core.api.inInventory
import core.api.setAttribute
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueInterpreter
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.GameWorld.settings
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class TzHaarMejJehDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.CHILD_GUILTY, "You want help JalYt-Ket-" + player.username + "?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                if (settings!!.jad_practice_enabled) {
                    if (getAttribute(player, "fc_practice_jad", false)) {
                        options(
                            if (inInventory(
                                    player,
                                    Items.FIRE_CAPE_6570,
                                )
                            ) {
                                "I have a fire cape here."
                            } else {
                                "What is this place?"
                            },
                            "What did you call me?",
                            "About my challenge...",
                            "No I'm fine thanks.",
                        )
                    } else {
                        options(
                            if (inInventory(
                                    player,
                                    Items.FIRE_CAPE_6570,
                                )
                            ) {
                                "I have a fire cape here."
                            } else {
                                "What is this place?"
                            },
                            "What did you call me?",
                            "I want to challenge Jad directly.",
                            "No I'm fine thanks.",
                        )
                    }
                } else {
                    options(
                        if (inInventory(
                                player,
                                Items.FIRE_CAPE_6570,
                            )
                        ) {
                            "I have a fire cape here."
                        } else {
                            "What is this place?"
                        },
                        "What did you call me?",
                        "No I'm fine thanks.",
                    )
                }
                stage = 1
            }

            1 ->
                when (buttonId) {
                    1 -> {
                        if (inInventory(player, Items.FIRE_CAPE_6570)) {
                            interpreter.open(DialogueInterpreter.getDialogueKey("firecape-exchange"), npc)
                        }
                        player(FaceAnim.HALF_GUILTY, "What is this place?")
                        stage = 10
                    }

                    2 -> {
                        player(FaceAnim.HALF_GUILTY, "What did you call me?")
                        stage = 20
                    }

                    3 ->
                        stage =
                            if (settings!!.jad_practice_enabled) {
                                if (getAttribute(player, "fc_practice_jad", false)) {
                                    player(FaceAnim.HALF_GUILTY, "About my challenge...")
                                    64
                                } else {
                                    player(
                                        FaceAnim.HALF_GUILTY,
                                        "The challenge is too long.",
                                        "I want to challenge Jad directly.",
                                    )
                                    50
                                }
                            } else {
                                player(FaceAnim.HALF_GUILTY, "No I'm fine thanks.")
                                30
                            }

                    4 -> {
                        player(FaceAnim.HALF_GUILTY, "No I'm fine thanks.")
                        stage = 30
                    }
                }

            10 -> {
                npc(
                    FaceAnim.CHILD_FRIENDLY,
                    "This is the fight caves, TzHaar-Xil made it for practice,",
                    "but many JalYt come here to fight too.",
                    "Just enter the cave and make sure you're prepared.",
                )
                stage = 11
            }

            11 -> {
                options("Are there any rules?", "Ok thanks.")
                stage = 12
            }

            12 ->
                when (buttonId) {
                    1 -> {
                        player(FaceAnim.HALF_GUILTY, "Are there any rules?")
                        stage = 14
                    }

                    2 -> {
                        player(FaceAnim.HALF_GUILTY, "Ok thanks.")
                        stage = 13
                    }
                }

            13 -> end()
            14 -> {
                npc(FaceAnim.CHILD_GUILTY, "Rules? Survival is the only rule in there.")
                stage = 15
            }

            15 -> {
                options("Do I win anything?", "Sounds good.")
                stage = 16
            }

            16 ->
                when (buttonId) {
                    1 -> {
                        player(FaceAnim.HALF_GUILTY, "Do I win anything?")
                        stage = 17
                    }

                    2 -> {
                        player(FaceAnim.HALF_GUILTY, "Sounds good.")
                        stage = 13
                    }
                }

            17 -> {
                npc(
                    FaceAnim.CHILD_GUILTY,
                    "You ask a lot of questions.",
                    "Might give you TokKul if you last long enough.",
                )
                stage = 18
            }

            18 -> {
                player(FaceAnim.HALF_GUILTY, "...")
                stage = 19
            }

            19 -> {
                npc(FaceAnim.CHILD_GUILTY, "Before you ask, TokKul is like your Coins.")
                stage = 500
            }

            500 -> {
                npc(
                    FaceAnim.CHILD_GUILTY,
                    "Gold is like you JalYt, soft and easily broken, we use",
                    "hard rock forged in fire like TzHaar!",
                )
                stage = 501
            }

            501 -> end()
            20 -> {
                npc(FaceAnim.CHILD_GUILTY, "Are you not JalYt-Ket?")
                stage = 21
            }

            21 -> {
                options("What's a 'JalYt-Ket'?", "I guess so...", "No I'm not!")
                stage = 22
            }

            22 ->
                when (buttonId) {
                    1 -> {
                        player(FaceAnim.HALF_GUILTY, "What's a 'JalYt-Ket'?")
                        stage = 100
                    }

                    2 -> {
                        player(FaceAnim.HALF_GUILTY, "I guess so...")
                        stage = 200
                    }

                    3 -> {
                        player(FaceAnim.HALF_GUILTY, "No I'm not!")
                        stage = 300
                    }
                }

            100 -> {
                npc(FaceAnim.CHILD_GUILTY, "That what you are... you tough and strong no?")
                stage = 101
            }

            101 -> {
                player(FaceAnim.HALF_GUILTY, "Well yes I suppose I am...")
                stage = 102
            }

            102 -> end()
            200 -> {
                player(FaceAnim.HALF_GUILTY, "I guess so....")
                stage = 201
            }

            201 -> end()
            300 -> end()
            30 -> end()
            50 -> {
                npc(
                    FaceAnim.CHILD_SUSPICIOUS,
                    "I thought you strong and tough",
                    "but you want skip endurance training?",
                )
                stage = 57
            }

            57 -> {
                npc(FaceAnim.CHILD_GUILTY, "Maybe you not JalYt-Ket afterall.")
                stage = 58
            }

            58 -> {
                options("I don't have time for it, man.", "No, I'm JalYt-Ket!")
                stage = 51
            }

            51 ->
                when (buttonId) {
                    1 -> {
                        player(FaceAnim.HALF_GUILTY, "I don't have time for it, man.")
                        stage = 52
                    }

                    2 -> {
                        player(FaceAnim.HALF_GUILTY, "No, I'm JalYt-Ket! I swear!", "I'll do the training properly.")
                        stage = 30
                    }
                }

            52 -> {
                npc(FaceAnim.CHILD_GUILTY, "JalYt, you know you not get reward", "if you not do training properly, ok?")
                stage = 56
            }

            56 -> {
                options("That's okay, I don't need a reward.", "Oh, nevermind then.")
                stage = 53
            }

            53 ->
                when (buttonId) {
                    1 -> {
                        player(FaceAnim.HALF_GUILTY, "That's okay, I don't need a reward.")
                        stage = 54
                    }

                    2 -> {
                        player(FaceAnim.HALF_GUILTY, "Oh, nevermind then.")
                        stage = 30
                    }
                }

            54 -> {
                player(FaceAnim.NEUTRAL, "I just wanna fight the big guy.")
                stage = 55
            }

            55 -> {
                npc(FaceAnim.CHILD_FRIENDLY, "Okay JalYt.", "TzTok-Jad not show up for just anyone.")
                stage = 59
            }

            59 -> {
                npc(
                    FaceAnim.CHILD_FRIENDLY,
                    "You give 8000 TokKul, TzTok-Jad know you serious.",
                    "You get it back if you victorious.",
                )
                stage = 60
            }

            60 -> {
                options(
                    "That's fair, here's 8000 TokKul.",
                    "I don't have that much on me, but I'll go get it.",
                    "TzTok-Jad must be old and tired to not just accept my challenge.",
                )
                stage = 61
                when (buttonId) {
                    1 -> {
                        player(FaceAnim.HALF_GUILTY, "That's fair, here's 8000 TokKul.")
                        if (!player.inventory.containsItem(APPEARANCE_FEE)) {
                            stage = 62
                        }
                        stage =
                            if (player.inventory.remove(APPEARANCE_FEE)) {
                                69
                            } else {
                                62
                            }
                    }

                    2 -> {
                        player(FaceAnim.HALF_GUILTY, "I don't have that much on me, but I'll go get it.")
                        stage = 30
                    }

                    3 -> {
                        player(
                            FaceAnim.HALF_GUILTY,
                            "TzTok-Jad must be old and tired",
                            "to not just accept my challenge.",
                        )
                        stage = 63
                    }
                }
            }

            61 ->
                when (buttonId) {
                    1 -> {
                        player(FaceAnim.HALF_GUILTY, "That's fair, here's 8000 TokKul.")
                        if (!player.inventory.containsItem(APPEARANCE_FEE)) {
                            stage = 62
                        }
                        stage =
                            if (player.inventory.remove(APPEARANCE_FEE)) {
                                69
                            } else {
                                62
                            }
                    }

                    2 -> {
                        player(FaceAnim.HALF_GUILTY, "I don't have that much on me, but I'll go get it.")
                        stage = 30
                    }

                    3 -> {
                        player(
                            FaceAnim.HALF_GUILTY,
                            "TzTok-Jad must be old and tired",
                            "to not just accept my challenge.",
                        )
                        stage = 63
                    }
                }

            62 -> {
                npc(FaceAnim.CHILD_FRIENDLY, "JalYt, you not have the TokKul.", "You come back when you are serious.")
                stage = 30
            }

            63 -> {
                npc(
                    FaceAnim.CHILD_FRIENDLY,
                    "JalYt-Mor, you the old and tired one.",
                    "You the one not want to do proper training.",
                )
                stage = 30
            }

            64 -> {
                npc(FaceAnim.CHILD_FRIENDLY, "TzTok-Jad is waiting for you.", "Do not make TzTok-Jad wait long.")
                stage = 30
            }

            69 -> {
                npc(
                    FaceAnim.CHILD_FRIENDLY,
                    "Okay JalYt. Enter cave when you are prepared.",
                    "You find TzTok-Jad waiting for JalYt challenger.",
                )
                setAttribute(player, "fc_practice_jad", true)
                stage = 30
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(DialogueInterpreter.getDialogueKey("tzhaar-mej"), NPCs.TZHAAR_MEJ_JAL_2617)
    }

    companion object {
        private val APPEARANCE_FEE = Item(6529, 8000)
    }
}
