package content.region.misthalin.quest.phoenixgang.dialogue

import content.region.asgarnia.quest.hero.dialogue.StravenDialogueFile
import content.region.misthalin.quest.phoenixgang.ShieldofArrav
import core.api.openDialogue
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.item.GroundItemManager
import org.rs.consts.NPCs
import org.rs.consts.Quests

class StravenDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var quest: Quest? = null

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        quest = player.getQuestRepository().getQuest(Quests.SHIELD_OF_ARRAV)
        when (quest!!.getStage(player)) {
            100 -> {
                if (ShieldofArrav.isPhoenix(player)) {
                    val heroesQuest: Quest = player.getQuestRepository().getQuest(Quests.HEROES_QUEST)
                    if (0 < heroesQuest.getStage(player) && heroesQuest.getStage(player) < 100) {
                        openDialogue(player, StravenDialogueFile(), npc)
                        return true
                    }
                }
                if (ShieldofArrav.isPhoenix(player)) {
                    npc("Greetings fellow gang member.")
                    stage = 10
                } else {
                    player.packetDispatch.sendMessage("I wouldn't talk to him if I was you.")
                    end()
                }
            }

            70 ->
                if (ShieldofArrav.isPhoenix(player)) {
                    npc("Greetings fellow gang member.")
                    stage = 10
                } else {
                    player.packetDispatch.sendMessage("I wouldn't talk to him if I was you.")
                    end()
                }

            60 ->
                if (ShieldofArrav.isPhoenixMission(player)) {
                    npc("How's your little mission going?")
                    stage = 200
                } else {
                    player("What's through the door?")
                    stage = 100
                }

            else -> player("What's through the door?")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        if (quest!!.getStage(player) == 60 && stage >= 200) {
            when (stage) {
                200 ->
                    if (!player.inventory.containsItem(ShieldofArrav.INTEL_REPORT)) {
                        player("I haven't managed to find the report yet...")
                        stage = 201
                    } else {
                        player("I have the intelligence report!")
                        stage = 204
                    }

                201 -> {
                    npc("You need to kill Johnny the Beard, who should be in the", "Blue Moon Inn.")
                    stage = 202
                }

                202 -> {
                    npc("...I would guess. Not being a member of the Phoenix", "Gang and all.")
                    stage = 203
                }

                203 -> end()
                204 -> {
                    npc("Let's see it then.")
                    stage = 205
                }

                205 -> {
                    interpreter.sendDialogue("You hand over the report. The man reads the report.")
                    stage = 206
                }

                206 -> {
                    npc("Yes. Yes, this is very good.")
                    stage = 207
                }

                207 -> {
                    npc("Ok! You can join the Phoenix Gang! I am Straven, one", "of the gang leaders.")
                    stage = 208
                }

                208 -> {
                    player("Nice to meet you.")
                    stage = 209
                }

                209 -> {
                    npc("Take this key.")
                    stage = 210
                }

                210 ->
                    if (player.inventory.remove(ShieldofArrav.INTEL_REPORT)) {
                        if (!player.inventory.add(ShieldofArrav.KEY)) {
                            GroundItemManager.create(ShieldofArrav.KEY, player)
                        }
                        interpreter.sendItemMessage(ShieldofArrav.KEY.id, "Straven hands you a key.")
                        quest!!.setStage(player, 70)
                        ShieldofArrav.setPhoenix(player)
                        stage = 211
                    }
            }
            return true
        }
        when (quest!!.getStage(player)) {
            100, 70 ->
                when (stage) {
                    10 ->
                        if (!player.inventory.containsItem(ShieldofArrav.KEY) &&
                            !player.bank.containsItem(ShieldofArrav.KEY)
                        ) {
                            player("I'm afraid I've lost the key you gave me...")
                            stage = 80
                        } else {
                            options(
                                "I've heard you've got some cool treasure in this place.",
                                "Any suggestions for where I can go thieving?",
                                "Where's the Black Arm Gang hideout?",
                            )
                            stage = 11
                        }

                    80 -> {
                        npc(
                            "You really need to be more careful. We don't want",
                            "that key falling into the wrong hands. Ah well... Have",
                            "this spare. Don't lose THIS one.",
                        )
                        stage = 81
                    }

                    81 -> {
                        if (!player.inventory.add(ShieldofArrav.KEY)) {
                            GroundItemManager.create(ShieldofArrav.KEY, player)
                        }
                        interpreter.sendItemMessage(ShieldofArrav.KEY.id, "Straven hands you a key.")
                        stage = 82
                    }

                    82 -> end()
                    11 ->
                        when (buttonId) {
                            1 -> {
                                player("I've heard you've got some cool treasures in this place...")
                                stage = 110
                            }

                            2 -> {
                                player("Any suggestions on where I can go thieving?")
                                stage = 120
                            }

                            3 -> {
                                player("Where's the Black Arm Gang hideout? I wanna go", "sabotage 'em!")
                                stage = 130
                            }
                        }

                    130 -> {
                        npc("That would be a little tricky; their security is pretty", "good.")
                        stage = 131
                    }

                    131 -> end()
                    120 -> {
                        npc("You can always try the marketplace in Ardougne.", "LOTS of opportunity there!")
                        stage = 121
                    }

                    121 -> end()
                    110 -> {
                        npc(
                            "Oh yeah, we've all stolen some stuff in our time. Those",
                            "candelsticks down here, for example, were quite a",
                            "challenge to get out of the palace.",
                        )
                        stage = 111
                    }

                    111 -> {
                        player("And the shield of Arrav? I heard you got that!")
                        stage = 112
                    }

                    112 -> {
                        npc(
                            "Woah... thats a blast from the past! We stole that years",
                            "and years ago! We don't even have all the shield",
                            "anymore.",
                        )
                        stage = 113
                    }

                    113 -> end()
                    211 -> {
                        npc(
                            "This key will give you access to our weapons supply",
                            "depot round the front of this building.",
                        )
                        stage = 212
                    }

                    212 -> end()
                    else -> handleDefault(buttonId)
                }

            60, 50, 40 ->
                when (stage) {
                    0 -> {
                        npc(
                            "Hey! You can't go in there. Only authories personnel",
                            "of the VTAM Corporation are allowed beyond this point.",
                        )
                        stage = 1
                    }

                    1 -> {
                        options(
                            "I know who you are!",
                            "How do I get a job with the VTAM corporation?",
                            "Why not?",
                        )
                        stage = 2
                    }

                    2 ->
                        when (buttonId) {
                            1 -> {
                                player("I know who you are!")
                                stage = 10
                            }

                            2 -> {
                                player("How do I get a job with the VTAM corporation?")
                                stage = 20
                            }

                            3 -> {
                                player("Why not?")
                                stage = 30
                            }
                        }

                    10 -> {
                        npc("Really?")
                        stage = 11
                    }

                    11 -> {
                        npc("Well?")
                        stage = 12
                    }

                    12 -> {
                        npc("Who are we then?")
                        stage = 13
                    }

                    13 -> {
                        player(
                            "This is the headquarters of the Phoenix Gang, the most",
                            "powerful crime syndicate this city has ever seen!",
                        )
                        stage = 14
                    }

                    14 -> {
                        npc("No, this is a legitimate business run by legitimate", "businessmen.")
                        stage = 15
                    }

                    15 -> {
                        npc("Supposing we were this crime gang however, what would", "you want with us?")
                        stage = 16
                    }

                    16 -> {
                        options(
                            "I'd like to offer you my services.",
                            "I want nothing. I was just making sure you were them.",
                        )
                        stage = 17
                    }

                    17 ->
                        when (buttonId) {
                            1 -> {
                                player("I'd like to offer you my services.")
                                stage = 41
                            }

                            2 -> {
                                player("I want nothing. I was just making sure you were them.")
                                stage = 18
                            }
                        }

                    18 -> {
                        npc("Well then get lost and stop wasting my time.")
                        stage = 19
                    }

                    19 -> {
                        npc("...if you know what's good for you.")
                        stage = 40
                    }

                    40 -> end()
                    41 -> {
                        npc("You mean you'd like to join the Phoenix Gang?")
                        stage = 42
                    }

                    42 -> {
                        npc(
                            "well obviously I can't speak for them, but the Phoenix",
                            "Gang doesn't let people join just like that.",
                        )
                        stage = 43
                    }

                    43 -> {
                        npc("You can't be too careful, you understand.")
                        stage = 44
                    }

                    44 -> {
                        npc("Generally someone has to prove their loyalty before", "they can join.")
                        stage = 45
                    }

                    45 -> {
                        player("How would I go about doing that?")
                        stage = 46
                    }

                    46 -> {
                        npc("Obviously, I would have no idea about that.")
                        stage = 47
                    }

                    47 -> {
                        npc(
                            "Although having said that, a rival gang of ours, er,",
                            "theirs, called the Black Arm Gang is supposedly metting",
                            "a contact from Port Sarim today in the Blue Moon",
                            "Inn.",
                        )
                        stage = 48
                    }

                    48 -> {
                        npc(
                            "The Blue Moon Inn is just by the south entrance to",
                            "this city, and supposedly the name of the contact is",
                            "Jonny the Beard.",
                        )
                        stage = 49
                    }

                    49 -> {
                        npc(
                            "OBVIOUSLY I know NOTHING about the dealing",
                            "of the Phoenix Gang, but I bet if SOMEBODY were",
                            "to kill him and bring back his intelligence report, they",
                            "would be considered loyal enough to join.",
                        )
                        stage = 50
                    }

                    50 -> {
                        player("Ok, I'll get right on it.")
                        stage = 51
                    }

                    51 -> {
                        quest!!.setStage(player, 60)
                        ShieldofArrav.setPhoenixMission(player)
                        end()
                    }

                    20 -> {
                        npc(
                            "Get a copy of the Varrock Herald. If we have any",
                            "positions right now, they'll be advertised in there.",
                        )
                        stage = 21
                    }

                    21 -> end()
                    30 -> {
                        npc("Sorry. That's classified information.")
                        stage = 31
                    }

                    31 -> end()
                    else -> handleDefault(buttonId)
                }

            else ->
                when (stage) {
                    0 -> {
                        npc(
                            "Hey! You can't go in there. Only authories personnel",
                            "of the VTAM Corporation are allowed beyond this point.",
                        )
                        stage = 1
                    }

                    1 -> {
                        player("Ok, sorry.")
                        stage = 2
                    }

                    2 -> end()
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return StravenDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.STRAVEN_644)
    }

    fun handleDefault(buttonId: Int) {
        when (stage) {
            0 -> {
                npc(
                    "Hey! You can't go in there. Only authories personnel",
                    "of the VTAM Corporation are allowed beyond this point.",
                )
                stage = 1
            }

            1 -> {
                player("Ok, sorry.")
                stage = 2
            }

            2 -> end()
        }
    }
}
