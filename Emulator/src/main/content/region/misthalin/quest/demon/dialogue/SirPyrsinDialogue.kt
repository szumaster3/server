package content.region.misthalin.quest.demon.dialogue

import content.region.misthalin.quest.demon.handlers.DemonSlayerUtils
import core.api.*
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.NPCs
import org.rs.consts.Quests

class SirPyrsinDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var quest: Quest? = null
    private var id = 0

    override fun open(vararg args: Any): Boolean {
        if (args[0] is NPC) {
            npc = args[0] as NPC
            id = npc.id
        } else if (args[0] is Int) {
            id = args[0] as Int
        }
        quest = player.getQuestRepository().getQuest(Quests.DEMON_SLAYER)
        when (quest!!.getStage(player)) {
            30 -> {
                npc(id, "Have you sorted that demon out yet?")
                stage = 0
            }

            20 -> {
                if (args.size > 1) {
                    player("That must be the key Sir Prysin dropped.")
                    stage = 800
                    return true
                }
                npc(id, "So how are you doing with getting the keys?")
                stage = 0
            }

            else -> {
                npc(id, "Hello, who are you?")
                stage = 0
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (quest!!.getStage(player)) {
            30 ->
                when (stage) {
                    0 ->
                        if (!inInventory(player, DemonSlayerUtils.SILVERLIGHT.id) &&
                            !inBank(player, DemonSlayerUtils.SILVERLIGHT.id) &&
                            !inEquipment(player, DemonSlayerUtils.SILVERLIGHT.id)
                        ) {
                            player("Not yet. And I, um, lost Silverlight.")
                            stage = 1
                        } else {
                            player("No, not yet.")
                            stage = 3
                        }

                    1 ->
                        if (player.inventory.add(DemonSlayerUtils.SILVERLIGHT)) {
                            npc(id, "Yes, I know, someone returned it to me. Take better", "care of it this time.")
                            stage = 2
                        }

                    2 -> end()
                    3 -> {
                        npc(id, "Well get on with it. He'll be pretty powerful when he", "gets to full strength.")
                        stage = 4
                    }

                    4 -> end()
                    302 -> end()
                }

            20 ->
                when (stage) {
                    0 -> {
                        if (inInventory(player, DemonSlayerUtils.FIRST_KEY.id) &&
                            inInventory(player, DemonSlayerUtils.SECOND_KEY.id) &&
                            inInventory(player, DemonSlayerUtils.THIRD_KEY.id)
                        ) {
                            player("I've got all three keys!")
                            stage = 300
                        } else {
                            player("I haven't found them all yet.")
                            stage = 1
                        }
                    }

                    1 -> {
                        options("Can you remind me where all the keys were again?", "I'm still looking.")
                        stage = 2
                    }

                    2 ->
                        when (buttonId) {
                            1 -> {
                                player("Can you remind me where all the keys were again?")
                                stage = 62
                            }

                            2 -> {
                                player("I'm still looking.")
                                stage = 20
                            }
                        }

                    20 -> {
                        npc(id, "Ok, tell me when you've got them all.")
                        stage = 21
                    }

                    21 -> end()
                    62 -> {
                        npc(id, "I kept one of the keys. I gave the other two to other", "people for safe keeping.")
                        stage = 63
                    }

                    63 -> {
                        npc(id, "One I gave to Rovin, the captain of the palace guard.")
                        stage = 64
                    }

                    64 -> {
                        npc(id, "I gave the other to the wizard Traiborn.")
                        stage = 65
                    }

                    65 -> end()
                    300 -> {
                        npc(id, "Excellent! Now I can give you Silverlight.")
                        stage = 301
                    }

                    301 -> {
                        if (player.inventory.freeSlots() == 0) {
                            npc(id, "You don't have any inventory space for Silverlight.")
                            stage = 302
                            return true
                        }
                        close()
                        player.lock()
                        Pulser.submit(
                            object : Pulse(1, player) {
                                var counter = 0

                                override fun pulse(): Boolean {
                                    when (counter++) {
                                        1 -> {
                                            player.properties.teleportLocation = Location.create(3204, 3470, 0)
                                            if (npc.location != Location.create(3204, 3469, 0)) {
                                                npc.properties.teleportLocation = Location.create(3204, 3469, 0)
                                            }
                                            npc.lock()
                                            npc.walkingQueue.reset()
                                            npc.pulseManager.clear()
                                            npc.faceLocation(Location.create(3204, 3468, 0))
                                            player.face(npc)
                                        }

                                        2 -> npc.animate(Animation(4597))
                                        9 -> {
                                            setVarp(player, 222, 5653570, true)
                                            setAttribute(player, "/save:demon-slayer:received", true)
                                            npc.animate(Animation(4607))
                                        }

                                        10 -> npc.transform(4657)
                                        11 -> npc.faceLocation(player.location)
                                        12 -> {
                                            npc.transform(883)
                                            if (player.inventory.remove(
                                                    DemonSlayerUtils.FIRST_KEY,
                                                    DemonSlayerUtils.SECOND_KEY,
                                                    DemonSlayerUtils.THIRD_KEY,
                                                )
                                            ) {
                                                if (player.inventory.add(DemonSlayerUtils.SILVERLIGHT)) {
                                                    npc.animate(Animation(4608))
                                                    player.animate(Animation(4604))
                                                    quest!!.setStage(player, 30)
                                                }
                                            }
                                        }

                                        13 -> {
                                            npc.walkingQueue.reset()
                                            npc.unlock()
                                            interpreter.sendItemMessage(
                                                DemonSlayerUtils.SILVERLIGHT.id,
                                                "Sir Prysin hands you a very shiny sword.",
                                            )
                                            stage = 302
                                            player.unlock()
                                            return true
                                        }
                                    }
                                    return false
                                }
                            },
                        )
                    }

                    302 -> end()
                    800 -> {
                        player(
                            "I don't seem to be able to reach it. I wonder if I can",
                            "dislodge it somehow. That way it may go down into the",
                            "sewers.",
                        )
                        stage = 801
                    }

                    801 -> end()
                }

            10 ->
                when (stage) {
                    0 -> {
                        options(
                            "I am a mighty adventurer. Who are you?",
                            "I'm not sure, I was hoping you could tell me.",
                            "Gypsy Aris said I should come and talk to you.",
                        )
                        stage = 1
                    }

                    1 ->
                        when (buttonId) {
                            1, 2 -> handleDefault(buttonId)
                            3 -> {
                                player("Gypsy Aris said I should come and talk to you.")
                                stage = 2
                            }
                        }

                    2 -> {
                        npc(
                            id,
                            "Gypsy Aris? Is she still alive? I remember her from",
                            "when I was pretty young. Well what do you need to",
                            "talk to me about?",
                        )
                        stage = 3
                    }

                    3 -> {
                        options("I need to find the Silverlight.", "Yes, she is still alive.")
                        stage = 4
                    }

                    4 ->
                        when (buttonId) {
                            1 -> {
                                player("I need to find the Silverlight.")
                                stage = 40
                            }

                            2 -> {
                                player("Yes, she is still alive. She lives right outside the castle!")
                                stage = 50
                            }
                        }

                    40 -> {
                        npc(id, "What do you need to find that for?")
                        stage = 41
                    }

                    41 -> {
                        player("I need it to fight Delrith.")
                        stage = 42
                    }

                    42 -> {
                        npc(id, "Delrith? I thought the world was rid of him, thanks to", "my great-grandfather.")
                        stage = 43
                    }

                    43 -> {
                        options(
                            "Well, the gypsy's crystal ball seems to think otherwise.",
                            "He's back and unfortunately I've got to deal with him.",
                        )
                        stage = 44
                    }

                    44 ->
                        when (buttonId) {
                            1 -> {
                                player("Well the gypsy's crystal ball seems to think otherwise.")
                                stage = 45
                            }

                            2 -> {
                                player("He's back and unfortunately I've got to deal with him.")
                                stage = 48
                            }
                        }

                    45 -> {
                        npc(id, "Well if the ball says so, I'd better help you.")
                        stage = 46
                    }

                    46 -> {
                        npc(id, "The problem is getting Silverlight.")
                        stage = 47
                    }

                    47 -> {
                        player("You mean you don't have it?")
                        stage = 60
                    }

                    48 -> {
                        npc(
                            id,
                            "You don't look up to much. I suppose Silverlight may be",
                            "good enough to carry you though though.",
                        )
                        stage = 46
                    }

                    60 -> {
                        npc(
                            id,
                            "Oh I do have it, but it is so powerful that the king",
                            "made me put it in a special box which needs three",
                            "different keys to open it. That way it won't fall into the",
                            "wrong hands.",
                        )
                        stage = 61
                    }

                    61 -> {
                        player("And why is this a problem?")
                        stage = 62
                    }

                    62 -> {
                        npc(id, "I kept one of the keys. I gave the other two to other", "people for safe keeping.")
                        stage = 63
                    }

                    63 -> {
                        npc(id, "One I gave to Rovin, the captain of the palace guard.")
                        stage = 64
                    }

                    64 -> {
                        npc(id, "I gave the other to the wizard Traiborn.")
                        stage = 65
                    }

                    65 -> {
                        player("Can you give me your key?")
                        stage = 66
                    }

                    66 -> {
                        npc(id, "Um.... ah....")
                        stage = 67
                    }

                    67 -> {
                        npc(id, "Well there's a problem there as well.")
                        stage = 68
                    }

                    68 -> {
                        npc(
                            id,
                            "I managed to drop the key in the drain just outside the",
                            "palace kitchen. It is just inside and I can't reach it.",
                        )
                        stage = 69
                    }

                    69 -> {
                        player("So what does the drain lead to?")
                        stage = 70
                    }

                    70 -> {
                        npc(
                            id,
                            "It is the drain for the drainpipe running from the sink",
                            "in the kitchen down to the palace sewers.",
                        )
                        stage = 71
                    }

                    71 -> {
                        options(
                            "Where can I find Captain Rovin?",
                            "Where does the wizard live?",
                            "Well I'd better go key hunting.",
                        )
                        stage = 72
                    }

                    72 ->
                        when (buttonId) {
                            1 -> {
                                player("Where can I find Captain Rovin?")
                                stage = 110
                            }

                            2 -> {
                                player("Where does the wizard live?")
                                stage = 120
                            }

                            3 -> {
                                player("Well I'd better go key hunting.")
                                stage = 130
                            }
                        }

                    130 -> {
                        npc(id, "Ok, goodbye.")
                        stage = 131
                    }

                    131 -> {
                        quest!!.setStage(player, 20)
                        end()
                    }

                    120 -> {
                        npc(id, "Wizard Traiborn?")
                        stage = 121
                    }

                    121 -> {
                        npc(
                            id,
                            "He is one of the wizards who lives in the tower on the",
                            "little island just south coast. I believe his",
                            "quarters are on the first floor of the tower.",
                        )
                        stage = 122
                    }

                    122 -> {
                        options("Where can I find Captain Rovin?", "Well I'd better go key hunting.")
                        stage = 123
                    }

                    123 ->
                        when (buttonId) {
                            1 -> {
                                player("Where can I find Captain Rovin?")
                                stage = 110
                            }

                            2 -> {
                                player("Well I'd better go key hunting.")
                                stage = 130
                            }
                        }

                    110 -> {
                        npc(
                            id,
                            "Captain Rovin lives at the top of the guards' quarters in",
                            "the north-west wing of this palace.",
                        )
                        stage = 111
                    }

                    111 -> {
                        options(
                            "Can you give me your key?",
                            "Where does the wizard live?",
                            "Well I'd better go key hunting.",
                        )
                        stage = 112
                    }

                    112 ->
                        when (buttonId) {
                            1 -> {
                                player("Can you give me your key?")
                                stage = 66
                            }

                            2 -> {
                                player("Where does the wizard live?")
                                stage = 120
                            }

                            3 -> {
                                player("Well I'd better go key hunting.")
                                stage = 130
                            }
                        }

                    50 -> {
                        npc(
                            id,
                            "Oh , is that the same gypsy? I would have thought she",
                            "would have died by now. She was pretty old when I",
                            "was a lad.",
                        )
                        stage = 51
                    }

                    51 -> {
                        npc(id, "Anyway, what can I do for you?")
                        stage = 52
                    }

                    52 -> {
                        player("I need to find the Silverlight.")
                        stage = 40
                    }

                    else -> handleDefault(buttonId)
                }

            else -> handleDefault(buttonId)
        }
        return true
    }

    private fun handleDefault(buttonId: Int) {
        when (stage) {
            0 -> {
                options("I am a mighty adventurer. Who are you?", "I'm not sure, I was hoping you could tell me.")
                stage = 1
            }

            1 ->
                when (buttonId) {
                    1 -> {
                        player("I am a mighty adventurer, Who are you?")
                        stage = 10
                    }

                    2 -> {
                        player("I'm not sure, I was hoping you could tell me.")
                        stage = 20
                    }
                }

            10 -> {
                npc(id, "I am Sir Prysin. A bold and famous knight of the", "realm.")
                stage = 11
            }

            11 -> end()
            20 -> {
                npc(id, "Well I've never met you before.")
                stage = 21
            }

            21 -> end()
        }
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SIR_PRYSIN_883, NPCs.SIR_PRYSIN_4657)
    }
}
