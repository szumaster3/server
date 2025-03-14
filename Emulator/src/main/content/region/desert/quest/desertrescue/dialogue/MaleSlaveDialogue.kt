package content.region.desert.quest.desertrescue.dialogue

import content.region.desert.quest.desertrescue.TouristTrap
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.tools.RandomFunction
import org.rs.consts.Quests

class MaleSlaveDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var quest: Quest? = null

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        quest = player.getQuestRepository().getQuest(Quests.THE_TOURIST_TRAP)
        when (npc.getShownNPC(player).id) {
            4985, 825 ->
                when (quest!!.getStage(player)) {
                    40 -> npc("Do you want to trade clothes now?")
                    30 -> npc("Hello again, do you want to try and unlock my chains?", "I'd really appreciate it!")
                    20 -> npc("You look like a new 'recruit'. How long have you been", "here?")
                    100 ->
                        npc(
                            "Oh bother, I was caught by the guards again... Listen, if",
                            "you can get me some Desert Clothes, I'll trade you for my",
                            "slave clothes again... Do you want to trade?",
                        )

                    else -> npc("Please, just leave me alone my life is", "terrible as it is.")
                }

            826 ->
                stage =
                    when (quest!!.getStage(player)) {
                        else -> {
                            npc("Yay! I'm finally free.")
                            0
                        }
                    }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (npc.getShownNPC(player).id) {
            4985, 825 ->
                when (quest!!.getStage(player)) {
                    30 ->
                        when (stage) {
                            0 -> {
                                player("Yeah, okay, let's give it a go.")
                                stage++
                            }

                            1 -> {
                                quest!!.setStage(player, 40)
                                npc("Great! You did it! Do you want to trade clothes now?")
                                stage = 0
                            }
                        }

                    40 ->
                        when (stage) {
                            0 -> {
                                player("Yes, I'll trade.")
                                stage = 11
                            }

                            11 ->
                                if (!TouristTrap.hasDesertClothes(player)) {
                                    npc(
                                        "I need desert boots, a desert robe and a desert",
                                        "shirt if you want these clothes off me.",
                                    )
                                    stage++
                                } else {
                                    npc(
                                        "Great! You have the Desert Clothes! <col=08088A>-- the slave starts",
                                        "<col=08088A>undressing right in front of you --</col> Okay, here's the",
                                        "clothes, I won't need them anymore.",
                                    )
                                    stage += 2
                                }

                            12 -> end()
                            13 -> {
                                interpreter.sendItemMessage(1845, "The slave gives you his dirty, flea infested robe.")
                                stage++
                            }

                            14 -> {
                                interpreter.sendItemMessage(1844, "The slave gives you his muddy, sweat soaked shirt.")
                                stage++
                            }

                            15 -> {
                                interpreter.sendItemMessage(
                                    1846,
                                    "The slave gives you a smelly pair of decomposing boots.",
                                )
                                stage++
                            }

                            16 ->
                                if (player.equipment.remove(*TouristTrap.DESERT_CLOTHES)) {
                                    stage++
                                    npc("Right, I'm off! Good luck!")
                                    TouristTrap.addConfig(player, 1 shl 4)
                                    for (item in TouristTrap.SLAVE_CLOTHES) {
                                        player.equipment.add(item, true, false)
                                    }
                                    player.packetDispatch.sendMessages(
                                        "The slave takes your desert robe.",
                                        "The slave takes your desert shirt.",
                                        "The slave takes your desert boots.",
                                    )
                                }

                            17 -> player("Yeah, good luck to you too!")
                        }

                    20 ->
                        when (stage) {
                            0 -> {
                                player("I've just arrived.")
                                stage++
                            }

                            1 -> {
                                npc(
                                    "Yeah, it looks like it as well. It's a shame that I won't be",
                                    "around long enough to get to know you. I'm making a",
                                    "break for it today. I have a plan to get out of here! It's",
                                    "amazing in its sophistication.",
                                )
                                stage++
                            }

                            2 -> {
                                player("Oh yes, that sounds interesting.")
                                stage++
                            }

                            3 -> {
                                npc("Yes, it is actually. I have all the details figured out", "except for one.")
                                stage++
                            }

                            4 -> {
                                player("What's that then?")
                                stage++
                            }

                            5 -> {
                                npc(
                                    "<col=08088A>-- The slave rattles the chains on his arms loudly. --",
                                    "</col>These bracelets, I can't seem to get them off. If I",
                                    "could get them off, I'd be able to climb my way out of",
                                    "here.",
                                )
                                stage++
                            }

                            6 -> {
                                player("I can try to undo them for you.")
                                stage++
                            }

                            7 -> {
                                npc(
                                    "Really, that would be great... <col=08088A>--The slave looks at you",
                                    "<col=08088A>strangely. --</col> Hang on a minute... I suppose you want",
                                    "something for doing this?",
                                )
                                stage++
                            }

                            8 -> {
                                npc(
                                    "The last time I did a trade in this place, I nearly lost",
                                    "the shirt from my back!",
                                )
                                stage++
                            }

                            9 -> {
                                player("It's funny you should say that actually.")
                                stage++
                            }

                            10 -> {
                                npc("<col=08088A>-- the slave looks at you blankly. --")
                                stage++
                            }

                            11 -> {
                                npc("Yeah, go on!")
                                stage++
                            }

                            12 -> {
                                player("If I can get the chains off, you have to give me", "something, okay?")
                                stage++
                            }

                            13 -> {
                                npc("Sure, what do you want?")
                                stage++
                            }

                            14 -> {
                                player("I want your clothes!")
                                stage++
                            }

                            15 -> {
                                npc("Blimey!")
                                stage++
                            }

                            16 -> {
                                player("I can dress like a slave and gain access to the mine", "area to scout it out.")
                                stage++
                            }

                            17 -> {
                                npc(
                                    "You're either incredibly brave or incredibly stupid. But",
                                    "what would I wear if you take my clothes? Get me",
                                    "some nice desert clothes and I'll think about it?",
                                )
                                stage++
                            }

                            18 -> {
                                npc("Do you still want to try and undo the locks for me?")
                                stage++
                            }

                            19 -> {
                                player("Yeah, okay, let's give it a go.")
                                stage++
                            }

                            20 -> {
                                npc("Great!")
                                stage++
                            }

                            21 -> {
                                interpreter.sendDialogue(
                                    "You use some nearby bits of wood and wire to try and pick the lock.",
                                )
                                stage++
                            }

                            22 -> {
                                val random = RandomFunction.random(3)
                                if (random == 1) {
                                    interpreter.sendDialogue(
                                        "You didn't manage to pick the lock this time would you like another",
                                        "go?",
                                    )
                                    stage++
                                } else if (random == 2) {
                                    interpreter.sendDialogue("A nearby guard spots you!")
                                    stage = 24
                                } else {
                                    quest!!.setStage(player, 40)
                                    npc("Great! You did it! Do you want to trade clothes now?")
                                    stage = 0
                                }
                            }

                            23 -> {
                                player("Yeah, I'll give it another go.")
                                stage = 21
                            }

                            24 -> {
                                player.lock()
                                interpreter.sendDialogues(getIds()[0], -1, true, "Oh oh!")
                                Pulser.submit(
                                    object : Pulse(4, player) {
                                        var counter = 0

                                        override fun pulse(): Boolean {
                                            when (++counter) {
                                                1 ->
                                                    interpreter.sendDialogues(
                                                        4993,
                                                        -1,
                                                        true,
                                                        "Oi, what are you two doing?",
                                                    )
                                                2 -> {
                                                    end()
                                                    player.packetDispatch.sendMessage("Slave: Oh oh!")
                                                    player.packetDispatch.sendMessage(
                                                        "Guard: Oi, what are you two doing?",
                                                    )
                                                    player.packetDispatch.sendMessage("The guards search you!")
                                                }

                                                3 ->
                                                    player.packetDispatch.sendMessage(
                                                        "You are roughed up by the guards and manhandled into a cell.",
                                                    )
                                                4 -> {
                                                    player.unlock()
                                                    quest!!.setStage(player, 30)
                                                    player.properties.teleportLocation = Location.create(3285, 3034, 0)
                                                    return true
                                                }
                                            }
                                            return false
                                        }
                                    },
                                )
                                stage++
                            }

                            25 -> interpreter.sendDialogues(4993, -1, true, "Oi, what are you two doing?")
                        }

                    100 ->
                        when (stage) {
                            0 -> {
                                player("Yes, I'll trade.")
                                stage++
                            }

                            1 -> {
                                if (!TouristTrap.hasDesertClothes(player)) {
                                    npc(
                                        "I need desert boots, a desert robe and a desert",
                                        "shirt if you want these clothes off me.",
                                    )
                                    stage++
                                }
                                npc(
                                    "Great! You have the Desert Clothes! <col=08088A>-- the slave starts",
                                    "<col=08088A>undressing right in front of you --</col> Okay, here's the",
                                    "clothes, I won't need them anymore.",
                                )
                                stage = 13
                            }

                            2 -> end()
                            13 -> {
                                interpreter.sendItemMessage(1845, "The slave gives you his dirty, flea infested robe.")
                                stage++
                            }

                            14 -> {
                                interpreter.sendItemMessage(1844, "The slave gives you his muddy, sweat soaked shirt.")
                                stage++
                            }

                            15 -> {
                                interpreter.sendItemMessage(
                                    1846,
                                    "The slave gives you a smelly pair of decomposing boots.",
                                )
                                stage++
                            }

                            16 ->
                                if (player.equipment.remove(*TouristTrap.DESERT_CLOTHES)) {
                                    stage++
                                    npc("Right, I'm off! Good luck!")
                                    TouristTrap.addConfig(player, 1 shl 4)
                                    player.equipment.add(*TouristTrap.SLAVE_CLOTHES)
                                    player.packetDispatch.sendMessages(
                                        "The slave takes your desert robe.",
                                        "The slave takes your desert shirt.",
                                        "The slave takes your desert boots.",
                                    )
                                }

                            17 -> player("Yeah, good luck to you too!")
                        }

                    else ->
                        when (stage) {
                            0 -> end()
                        }
                }

            826 ->
                when (quest!!.getStage(player)) {
                    else -> end()
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(825, 826, 4985)
    }
}
