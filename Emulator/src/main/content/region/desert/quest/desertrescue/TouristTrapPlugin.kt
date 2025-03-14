package content.region.desert.quest.desertrescue

import content.global.skill.smithing.smelting.Bar
import content.region.desert.quest.desertrescue.TouristTrapPlugin.AnnaCartHandler.AnnaCartCutscene
import content.region.desert.quest.desertrescue.TouristTrapPlugin.BedabinAnvilHandler.AnnaWinchHandler
import core.api.*
import core.cache.def.impl.AnimationDefinition
import core.cache.def.impl.NPCDefinition
import core.cache.def.impl.SceneryDefinition
import core.game.activity.ActivityManager
import core.game.activity.ActivityPlugin
import core.game.activity.CutscenePlugin
import core.game.component.Component
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueInterpreter
import core.game.global.action.ClimbActionHandler
import core.game.global.action.DoorActionHandler
import core.game.interaction.NodeUsageEvent
import core.game.interaction.OptionHandler
import core.game.interaction.UseWithHandler
import core.game.node.Node
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.SkillPulse
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.GameWorld.ticks
import core.game.world.map.Location
import core.game.world.map.RegionManager.getNpc
import core.game.world.map.RegionManager.getObject
import core.game.world.map.build.DynamicRegion
import core.game.world.update.flag.context.Animation
import core.plugin.ClassScanner.definePlugin
import core.plugin.Plugin
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.Quests
import org.rs.consts.Vars

class TouristTrapPlugin : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(1528).handlers["option:open"] = this
        SceneryDefinition.forId(1529).handlers["option:close"] = this
        SceneryDefinition.forId(2673).handlers["option:open"] = this
        SceneryDefinition.forId(2673).handlers["option:search"] = this
        SceneryDefinition.forId(2674).handlers["option:open"] = this
        SceneryDefinition.forId(2674).handlers["option:search"] = this
        SceneryDefinition.forId(2675).handlers["option:open"] = this
        SceneryDefinition.forId(2675).handlers["option:watch"] = this
        SceneryDefinition.forId(2676).handlers["option:open"] = this
        SceneryDefinition.forId(2676).handlers["option:watch"] = this
        SceneryDefinition.forId(2677).handlers["option:open"] = this
        SceneryDefinition.forId(2678).handlers["option:search"] = this
        SceneryDefinition.forId(2680).handlers["option:look-in"] = this
        SceneryDefinition.forId(2680).handlers["option:search"] = this
        SceneryDefinition.forId(2681).handlers["option:look in"] = this
        SceneryDefinition.forId(2681).handlers["option:search"] = this
        SceneryDefinition.forId(2684).handlers["option:look at"] = this
        SceneryDefinition.forId(2684).handlers["option:look at"] = this
        SceneryDefinition.forId(2684).handlers["option:search"] = this
        SceneryDefinition.forId(2684).handlers["option:search"] = this
        SceneryDefinition.forId(2685).handlers["option:open"] = this
        SceneryDefinition.forId(2685).handlers["option:open"] = this
        SceneryDefinition.forId(2686).handlers["option:search"] = this
        SceneryDefinition.forId(2686).handlers["option:search"] = this
        SceneryDefinition.forId(2687).handlers["option:open"] = this
        SceneryDefinition.forId(2687).handlers["option:search"] = this
        SceneryDefinition.forId(2688).handlers["option:open"] = this
        SceneryDefinition.forId(2688).handlers["option:search"] = this
        SceneryDefinition.forId(2689).handlers["option:open"] = this
        SceneryDefinition.forId(2690).handlers["option:open"] = this
        SceneryDefinition.forId(2691).handlers["option:open"] = this
        SceneryDefinition.forId(2698).handlers["option:walk through"] = this
        SceneryDefinition.forId(2699).handlers["option:walk through"] = this

        SceneryDefinition.forId(18869).handlers["option:bend"] = this
        SceneryDefinition.forId(18870).handlers["option:escape"] = this
        SceneryDefinition.forId(18871).handlers["option:climb"] = this
        SceneryDefinition.forId(18875).handlers["option:inspect"] = this
        SceneryDefinition.forId(18878).handlers["option:look at"] = this
        SceneryDefinition.forId(18878).handlers["option:search"] = this
        SceneryDefinition.forId(18879).handlers["option:look at"] = this
        SceneryDefinition.forId(18879).handlers["option:search"] = this
        SceneryDefinition.forId(18888).handlers["option:look at"] = this
        SceneryDefinition.forId(18888).handlers["option:operate"] = this
        SceneryDefinition.forId(18898).handlers["option:look at"] = this
        SceneryDefinition.forId(18898).handlers["option:search"] = this
        SceneryDefinition.forId(18899).handlers["option:look at"] = this
        SceneryDefinition.forId(18899).handlers["option:search"] = this
        SceneryDefinition.forId(18902).handlers["option:search"] = this
        SceneryDefinition.forId(18923).handlers["option:climb-up"] = this
        SceneryDefinition.forId(18924).handlers["option:climb-down"] = this
        SceneryDefinition.forId(18951).handlers["option:look at"] = this
        SceneryDefinition.forId(18951).handlers["option:use"] = this
        SceneryDefinition.forId(18958).handlers["option:look at"] = this
        SceneryDefinition.forId(18958).handlers["option:search"] = this
        SceneryDefinition.forId(18959).handlers["option:look at"] = this
        SceneryDefinition.forId(18959).handlers["option:search"] = this
        SceneryDefinition.forId(18962).handlers["option:look in"] = this
        SceneryDefinition.forId(18962).handlers["option:search"] = this
        SceneryDefinition.forId(18963).handlers["option:look in"] = this
        SceneryDefinition.forId(18963).handlers["option:search"] = this
        SceneryDefinition.forId(36748).handlers["option:talk-to"] = this

        NPCDefinition.forId(830).handlers["option:watch"] = this
        NPCDefinition.forId(4975).handlers["option:talk-to"] = this
        NPCDefinition.forId(4976).handlers["option:talk-to"] = this
        NPCDefinition.forId(4977).handlers["option:talk-to"] = this
        NPCDefinition.forId(4978).handlers["option:talk-to"] = this
        NPCDefinition.forId(5002).handlers["option:talk-to"] = this

        TouristTrap.TECHNICAL_PLANS.definition.handlers["option:read"] = this
        TouristTrap.ANNA_BARREL.definition.handlers["option:look"] = this
        TouristTrap.ANNA_BARREL.definition.handlers["option:drop"] = this

        definePlugin(BedabinKeyHandler())
        definePlugin(BedabinAnvilHandler())
        definePlugin(AnnaCartHandler())
        definePlugin(AnnaCartCutscene())
        definePlugin(AnnaWinchHandler())
        definePlugin(WinchCutscene())
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        val quest = player.getQuestRepository().getQuest(Quests.THE_TOURIST_TRAP)
        val id = node.id
        when (option) {
            "read" ->
                sendDialogueLines(
                    player,
                    "The plans look very technical! But you can see that this item will",
                    "require a bronze bar and at least 10 feathers.",
                )

            "watch" ->
                when (id) {
                    2676, 2675 ->
                        sendDialogueLines(
                            player,
                            "You watch the doors for some time. You notice that only slaves seem",
                            "to do down there. You might be able to sneak down if you pass as a",
                            "slave.",
                        )

                    else ->
                        sendDialogueLines(
                            player,
                            "You watch the Mercenary Captain for some time. He has a large",
                            "metal key attached to his belt. You notice that he usually gets his",
                            "men to do his dirty work.",
                        )
                }

            "walk through" ->
                when (id) {
                    2698, 2699 -> {
                        if (quest.getStage(player) < 60) {
                            sendNPCDialogue(player, 5001, "Hey you! You're not allowed in there.")
                        }
                        if (!TouristTrap.hasSlaveClothes(player)) {
                            sendNPCDialogue(player, 5001, "Hey you're not a slave!")
                        }
                        player.properties.teleportLocation =
                            player.location.transform(if (player.location.x >= 3284) -4 else 4, 0, 0)
                        player.packetDispatch.sendMessages(
                            "You walk into the darkness of the cavern...",
                            "... and emerge in a different part of this huge underground complex.",
                        )
                    }
                }

            "look" -> sendNPCDialogue(player, 823, "Let me out of here, I feel sick!")
            "drop" -> sendNPCDialogue(player, 823, "Don't let me out!")
            "open" ->
                when (id) {
                    2688, 2687 -> {
                        if (!player.inventory.containsItem(TouristTrap.WROUGHT_IRON_KEY)) {
                            player.packetDispatch.sendMessage("This gate looks like it needs a key to open it.")
                        }
                        DoorActionHandler.handleAutowalkDoor(player, node as Scenery)
                    }

                    2686, 2685 ->
                        sendNPCDialogue(
                            player,
                            4999,
                            "Hey, move away from the gate. There's nothing interesting for you here.",
                        )

                    2677 -> {
                        if (!player.inventory.containsItem(TouristTrap.BEDABIN_KEY)) {
                            player.packetDispatch.sendMessage("This chest needs a key to unlock it.")
                        }
                        if (quest.getStage(player) <= 54 && quest.getStage(player) != 53) {
                            player.packetDispatch.sendMessage(
                                "The captain spots you before you manage to open the chest...",
                            )
                            player.lock(3)
                            Pulser.submit(
                                object : Pulse(2, player) {
                                    override fun pulse(): Boolean {
                                        player.dialogueInterpreter.open(831, getNpc(player, 831)!!)
                                        return true
                                    }
                                },
                            )
                        } else if (quest.getStage(player) == 53) {
                            if (!player.hasItem(TouristTrap.TECHNICAL_PLANS)) {
                                player.dialogueInterpreter.sendItemMessage(
                                    TouristTrap.TECHNICAL_PLANS,
                                    "While the Captain's distracted, you quickly unlock the",
                                    "chest with the Bedabins' copy of the key. You take out",
                                    "the plans.",
                                )
                                player.inventory.add(TouristTrap.TECHNICAL_PLANS, player)
                            }
                        }
                    }

                    2690, 2691 -> {
                        setAttribute(player, "ana-delay", ticks + 2)
                        player.properties.teleportLocation = Location.create(3301, 3035, 0)
                    }

                    2676, 2675 -> {
                        if (!TouristTrap.hasSlaveClothes(player)) {
                            sendNPCDialogue(player, 4997, "Watch it! Only slaves can travel into the mine.")
                        }
                        setAttribute(player, "ana-delay", ticks + 2)
                        player.properties.teleportLocation = Location.create(3278, 9427, 0)
                        player.dialogueInterpreter.sendDialogue(
                            "The huge doors open into a dark, dank and smelly tunnel. The",
                            "associated smells of a hundred sweaty miners greets your nostrils.",
                            "And your ears ring with the 'CLANG CLANG CLANG' as metal hits",
                            "rock.",
                        )
                    }

                    2674, 2673 -> {
                        if (quest.getStage(player) > 60 &&
                            quest.getStage(player) < 98 &&
                            player.inventory.containsItem(TouristTrap.ANNA_BARREL)
                        ) {
                            player.lock()
                            sendNPCDialogue(player, 4999, "Would you like me to take that heavy barrel for you?")
                            Pulser.submit(
                                object : Pulse(4, player) {
                                    var counter: Int = 0

                                    override fun pulse(): Boolean {
                                        when (++counter) {
                                            1 -> sendPlayerDialogue(player, "No, please don't.")
                                            2 -> {
                                                player.dialogueInterpreter.close()
                                                player.packetDispatch.sendMessage("The guards search you!")
                                            }

                                            3 ->
                                                player.packetDispatch.sendMessage(
                                                    "You are roughed up by the guards and manhandled into a cell.",
                                                )
                                            4 -> {
                                                player.unlock()
                                                player.inventory.remove(TouristTrap.ANNA_BARREL)
                                                TouristTrap.addConfig(player, (1 shl 4))
                                                quest.setStage(player, 61)
                                                player.properties.teleportLocation = Location.create(3285, 3034, 0)
                                                return true
                                            }
                                        }
                                        return false
                                    }
                                },
                            )
                            return true
                        }
                        if (node.location.withinDistance(Location(3273, 3028, 0)) && player.location.x < 3274) {
                            if (!player.inventory.containsItem(TouristTrap.METAL_KEY)) {
                                player.packetDispatch.sendMessage("The gate needs a key in order to be opened.")
                                return true
                            }
                            DoorActionHandler.handleAutowalkDoor(player, node as Scenery)
                            player.packetDispatch.sendMessage(
                                "The guards search you thoroughly as you go through the gates.",
                            )
                            return true
                        }
                        DoorActionHandler.handleAutowalkDoor(player, node as Scenery)
                    }

                    2689 -> {
                        if (!player.inventory.containsItem(TouristTrap.CELL_DOOR_KEY)) {
                            player.packetDispatch.sendMessage("The door seems to be pretty locked.")
                        }
                        DoorActionHandler.handleAutowalkDoor(player, node as Scenery)
                    }

                    1528 -> SceneryBuilder.replace(node as Scenery, node.transform(1529))
                }

            "close" ->
                if (id == 1529) {
                    SceneryBuilder.replace(node as Scenery, node.transform(1528))
                }

            "look-in", "look in" ->
                when (id) {
                    2681, 18962, 18963 ->
                        player.dialogueInterpreter.sendDialogue(
                            "This looks like an empty mining barrel. Slaves use this to load up the",
                            "rocks and stones that they're mining.",
                        )

                    2680 -> player.dialogueInterpreter.sendDialogue("You search the full barrel... It's full of rocks.")
                }

            "use" ->
                if (id == 18951) {
                    player.dialogueInterpreter.open("winch dialogue")
                }

            "search" ->
                when (id) {
                    2688, 2687 ->
                        player.dialogueInterpreter.sendDialogue(
                            "These wrought iron gates look like they're designed to keep people out.",
                            "it looks like you'll need a key to get past these.",
                        )

                    2686, 2685 ->
                        player.dialogueInterpreter.sendDialogue(
                            "It looks as if this is where very difficult prisoners are sent as a",
                            "punishment.",
                        )

                    2681, 18962, 18963 -> player.dialogueInterpreter.open("barrel dialogue", node)
                    2680 ->
                        player.dialogueInterpreter.sendDialogue(
                            "This looks like a full mining barrel. Slaves use this to load up the",
                            "rocks and stones that they're mining. This barrel is full of rocks.",
                        )

                    2684 -> player.dialogueInterpreter.open("cart dialogue", node)
                    2678 -> {
                        if (quest.getStage(player) == 51) {
                            quest.setStage(player, 52)
                        }
                        player.dialogueInterpreter.sendItemMessage(
                            9904,
                            "You notice several books on the subject of sailing.",
                        )
                    }

                    2673, 2674 ->
                        player.dialogueInterpreter.sendDialogue(
                            "You see what looks like a mining compound. There seems to be people",
                            "mining rocks. They look as if they're chained to the rocks and they're",
                            "being watched over by the guards. It's not a very happy place.",
                        )

                    18958, 18959 -> {
                        if (quest.getStage(player) == 90) {
                            player.dialogueInterpreter.open("ana cart dialogue")
                        }
                        player.packetDispatch.sendMessage(
                            "This looks like a mine cart which takes barrels out of the encampment to Al Kharid.",
                        )
                    }

                    18899, 18898, 18878, 18879 ->
                        player.dialogueInterpreter.sendDialogue(
                            "You search the footsteps more closely. You can see that there are",
                            "five sets of footprints. One set of footprints seem lighter than the",
                            "others. The four other footsteps were made by heavier people",
                            "with boots.",
                        )

                    18902 -> {
                        if (hasItem(player, TouristTrap.CELL_DOOR_KEY)) {
                            player.inventory.add(TouristTrap.CELL_DOOR_KEY, player)
                            player.dialogueInterpreter.sendItemMessage(
                                TouristTrap.CELL_DOOR_KEY,
                                "You find a cell door key.",
                            )
                        } else if (hasItem(player, TouristTrap.WROUGHT_IRON_KEY) &&
                            player
                                .getQuestRepository()
                                .isComplete(Quests.THE_TOURIST_TRAP)
                        ) {
                            player.inventory.add(TouristTrap.WROUGHT_IRON_KEY, player)
                            player.dialogueInterpreter.sendItemMessage(
                                TouristTrap.WROUGHT_IRON_KEY,
                                "You find the key to the main gate.",
                            )
                        }
                        player.packetDispatch.sendMessage("You search the captains desk while he's not looking...")
                        player.packetDispatch.sendMessage("...but you find nothing of interest.")
                    }
                }

            "operate" -> {
                if (quest.getStage(player) != 71) {
                    player.dialogueInterpreter.sendDialogue("There doesn't seem anything to lift.")
                }
                ActivityManager.start(player, "winch cutscene", false)
            }

            "look at" ->
                when (id) {
                    18888 ->
                        player.dialogueInterpreter.sendDialogue(
                            "This looks like a winch, it probably brings rocks up from",
                            "underground.",
                        )

                    18951 ->
                        player.dialogueInterpreter.sendDialogue(
                            "This looks like a lift of some sort. You see barrels of rocks being",
                            "placed on the lift and they're hauled up to the surface.",
                        )

                    2684 -> {
                        if (node.location.x == 3318) {
                            player.dialogueInterpreter.sendDialogue(
                                "This mine cart is being loaded up with new rocks and stone.",
                                "It gets sent to a different section of the mine for unloading.",
                            )
                        }
                        player.dialogueInterpreter.sendDialogue(
                            "This cart is being unloaded into this section of the mine. Before being",
                            "sent back to another section for another load.",
                        )
                    }

                    18958, 18959 ->
                        player.dialogueInterpreter.sendDialogue(
                            "A sturdy looking cart for carrying barrels of rocks out of ",
                            "the mining camp.",
                        )

                    18900, 18899, 18898, 18887, 18886, 18885, 18884, 18883, 18882, 18881, 18880, 18879, 18878, 18877 ->
                        player.dialogueInterpreter.sendDialogue(
                            "This looks like some disturbed sand. Footsteps seem to be heading off",
                            "towards the South.",
                        )
                }

            "talk-to" ->
                when (id) {
                    5002 -> (node as NPC).sendChat("Move along please, don't want any trouble today!")
                    36748 -> sendPlayerDialogue(player, "Mmm... looks like that camel would make a nice kebab.")

                    4975, 4977, 4978, 4976 -> (node as NPC).sendChat("Hey leave me alone, can't you see that i'm busy?")
                }

            "bend" -> {
                player.animate(Animation.create(5037))
                Pulser.submit(
                    object : Pulse(5, player) {
                        override fun pulse(): Boolean {
                            player.packetDispatch.sendMessage("You bend the bars back.")
                            setVarbit(player, Vars.VARBIT_QUEST_TOURIST_TRAP_CELL_WINDOW_2801, 1)
                            return true
                        }
                    },
                )
            }

            "escape" -> {
                player.packetDispatch.sendMessage("You prepare to squeeze through the bars.")
                content.global.skill.agility.AgilityHandler.forceWalk(
                    player,
                    0,
                    player.location,
                    player.location.transform(if (player.location.x <= node.location.x) 1 else -1, 0, 0),
                    Animation.create(5038),
                    4,
                    0.0,
                    null,
                )
            }

            "climb" -> {
                player.packetDispatch.sendMessage("You scrape your hands and knees as you climb up.")
                content.global.skill.agility.AgilityHandler.forceWalk(
                    player,
                    0,
                    player.location,
                    Location.create(3279, 3037, 0),
                    Animation.create(5041),
                    10,
                    0.0,
                    null,
                )
                Pulser.submit(
                    object : Pulse(3, player) {
                        override fun pulse(): Boolean {
                            player.animator.reset()
                            return true
                        }
                    },
                )
            }

            "climb-up" ->
                if (id == 18923) {
                    if (player.location.x <= 3278) {
                        return true
                    }
                    player.animate(Animation.create(5039))
                    Pulser.submit(
                        object : Pulse(6, player) {
                            override fun pulse(): Boolean {
                                player.animator.reset()
                                player.properties.teleportLocation = Location.create(3278, 3037, 0)
                                return true
                            }
                        },
                    )
                }

            "inspect" ->
                if (id == 18875) {
                    player.dialogueInterpreter.sendDialogue(
                        "You remember that Irena mentioned something about Ana wearing a red scarf before she left for the desert.",
                    )
                }

            "climb-down" ->
                if (id == 18924) {
                    if (player.location.x <= 3273) {
                        return true
                    }
                    content.global.skill.agility.AgilityHandler.forceWalk(
                        player,
                        0,
                        player.location,
                        Location.create(3270, 3039, 0),
                        Animation.create(5040),
                        20,
                        0.0,
                        null,
                    )
                    Pulser.submit(
                        object : Pulse(3, player) {
                            override fun pulse(): Boolean {
                                player.animator.reset()
                                return true
                            }
                        },
                    )
                }
        }
        return true
    }

    override fun isWalk(
        player: Player,
        node: Node,
    ): Boolean {
        return node !is Item
    }

    override fun isWalk(): Boolean {
        return false
    }

    override fun getDestination(
        node: Node,
        n: Node,
    ): Location? {
        if (n.id == 18923) {
            return Location(3279, 3037, 0)
        }
        return null
    }

    private fun hasItem(
        player: Player,
        item: Item,
    ): Boolean {
        return !player.inventory.containsItem(item) && !player.bank.containsItem(item)
    }

    class WinchDialogue(
        player: Player? = null,
    ) : Dialogue(player) {
        override fun open(vararg args: Any): Boolean {
            if (args.size >= 1) {
                interpreter.sendDialogue("The guard notices the barrel (with Ana in it) that you're carrying.")
                stage = 500
                return true
            }
            interpreter.sendDialogues(NPC(4999), -1, "Hey there, what do you want?")
            return true
        }

        override fun handle(
            interfaceId: Int,
            buttonId: Int,
        ): Boolean {
            when (stage) {
                500 -> {
                    interpreter.sendDialogues(NPC(5002), -1, "Hey, that Barrel looks heavy, do you need a hand?")
                    stage++
                }

                501 -> {
                    player("Yes please.")
                    stage++
                }

                502 -> {
                    interpreter.sendDialogue("The guard comes over and helps you. He takes one end of the", "barrel.")
                    stage++
                }

                503 -> {
                    interpreter.sendDialogues(NPC(5002), -1, "Blimey! This is heavy!")
                    stage++
                }

                504 -> {
                    interpreter.sendDialogues(NPC(823), -1, "Why you cheeky....!")
                    stage++
                }

                505 -> {
                    interpreter.sendDialogues(
                        NPC(
                            5002,
                        ),
                        -1,
                        "<col=08088A>- The guard looks around surprised at Ana's outburst. -",
                        "What was that?",
                    )
                    stage++
                }

                506 -> {
                    player("Oh, it was nothing.")
                    stage++
                }

                507 -> {
                    interpreter.sendDialogues(NPC(5002), -1, "I could have sworn I heard something!")
                    stage++
                }

                508 -> {
                    interpreter.sendDialogues(NPC(823), -1, "Yes you did you ignoramus.")
                    stage++
                }

                509 -> {
                    interpreter.sendDialogues(NPC(5002), -1, "What was that you said?")
                    stage++
                }

                510 -> {
                    player("I said you were very gregarious!")
                    stage++
                }

                511 -> {
                    interpreter.sendDialogues(NPC(823), -1, "You creep!")
                    stage++
                }

                512 -> {
                    interpreter.sendDialogues(
                        NPC(
                            5002,
                        ),
                        -1,
                        "Oh, right, how very nice of you to say so.",
                        "<col=08088A>-- The guard seems flattered. --",
                    )
                    stage++
                }

                513 -> {
                    interpreter.sendDialogues(
                        NPC(
                            5002,
                        ),
                        -1,
                        "Anyway, let's get this barrel up to the surface, plenty",
                        "more work for you to do!",
                    )
                    stage++
                }

                514 -> {
                    interpreter.sendDialogue("The guard places the barrel carefully on the lift platform.")
                    stage++
                }

                515 -> {
                    interpreter.sendDialogues(
                        NPC(
                            5002,
                        ),
                        -1,
                        "Oh, there's no one operating the lift up top, hope this",
                        "barrel isn't urgent? You'd better get back to work!",
                    )
                    stage = 516
                }

                516 -> {
                    player.inventory.remove(TouristTrap.ANNA_BARREL)
                    player.bank.remove(TouristTrap.ANNA_BARREL)
                    player.getQuestRepository().getQuest(Quests.THE_TOURIST_TRAP).setStage(player, 71)
                    end()
                }

                0 -> options("What is this thing?", "Can I use this?").also { stage++ }
                1 ->
                    when (buttonId) {
                        1 -> {
                            player("What is this thing?")
                            stage = 10
                        }

                        2 -> {
                            player("Can I use this?")
                            stage = 20
                        }
                    }

                10 -> {
                    interpreter.sendDialogues(
                        NPC(
                            4999,
                        ),
                        -1,
                        "It is quite clearly a lift. Any fool can see that it's used to",
                        "transport rock to the surface.",
                    )
                    stage++
                }

                11, 21 -> end()
                20 -> {
                    interpreter.sendDialogues(
                        NPC(
                            4999,
                        ),
                        -1,
                        "Of course not, you'd be doing me out of a job. Anyway",
                        "you haven't got any barrels that need to go to",
                        "the surface.",
                    )
                    stage++
                }
            }
            return true
        }

        override fun getIds(): IntArray {
            return intArrayOf(DialogueInterpreter.getDialogueKey("winch dialogue"))
        }
    }

    class AnnaCartHandler : UseWithHandler(TouristTrap.ANNA_BARREL.id) {
        override fun newInstance(arg: Any?): Plugin<Any> {
            addHandler(2684, OBJECT_TYPE, this)
            addHandler(18958, OBJECT_TYPE, this)
            return this
        }

        override fun handle(event: NodeUsageEvent): Boolean {
            val player = event.player
            val quest = player.getQuestRepository().getQuest(Quests.THE_TOURIST_TRAP)
            if (event.usedWith.id == 18958) {
                if (quest.getStage(player) == 72) {
                    player.lock(4)
                    player.animate(Animation.create(5050))
                    Pulser.submit(
                        object : Pulse(3, player) {
                            override fun pulse(): Boolean {
                                player.inventory.remove(event.usedItem)
                                TouristTrap.addConfig(player, 4096 + (2048 + (1 shl 4)))
                                quest.setStage(player, 80)
                                player.dialogueInterpreter.sendDialogue(
                                    "You place Ana (In the barrel) carefully on the cart. This  was the last",
                                    "barrel to go on the cart, but the cart driver doesn't seem to be",
                                    "any rush to get going. And the desert heat will soon get to Ana.",
                                )
                                return true
                            }
                        },
                    )
                }
                return true
            }
            if (event.usedWith.location != Location.create(3318, 9430, 0)) {
                return false
            }
            if (quest.getStage(player) != 61) {
                return false
            }
            player.dialogueInterpreter.sendDialogue(
                "You carefully place Ana in the barrel into the mine",
                "cart. Soon the cart moves out of sight and then it",
                "returns.",
            )
            setAttribute(player, "ana-delay", ticks + 100000000)
            ActivityManager.start(player, "ana cart", false)
            return true
        }

        class AnnaCartCutscene : CutscenePlugin {
            constructor() : super("ana cart")

            constructor(player: Player?) : super("ana cart") {
                this.player = player
            }

            override fun open() {
                super.open()
                setAttribute(player, "ana-delay", ticks + 100000000)
                player.faceLocation(base.transform(54, 22, 0))
                Pulser.submit(
                    object : Pulse(1, player) {
                        var counter: Int = 0
                        var cart: NPC? = null

                        override fun pulse(): Boolean {
                            when (++counter) {
                                1 -> player.animate(Animation.create(5052))
                                3 -> {
                                    SceneryBuilder.remove(getObject(base.transform(54, 22, 0)))
                                    cart = NPC.create(4980, base.transform(54, 22, 0))
                                    cart!!.init()
                                }

                                4 -> {
                                    cart!!.walkingQueue.reset()
                                    for (l in PATHS[1]) {
                                        val loc = base.transform(l.localX, l.localY, 0)
                                        cart!!.walkingQueue.addPath(loc.x, loc.y)
                                    }
                                }

                                18 -> cart!!.clear()
                                22 -> {
                                    cart = NPC.create(4981, base.transform(51, 9, 0))
                                    cart!!.init()
                                    cart!!.walkingQueue.reset()
                                    for (l in PATHS[0]) {
                                        val loc = base.transform(l.localX, l.localY, 0)
                                        cart!!.walkingQueue.addPath(loc.x, loc.y)
                                    }
                                }

                                33 -> {
                                    player.getQuestRepository().getQuest(Quests.THE_TOURIST_TRAP).setStage(player, 70)
                                    player.inventory.remove(TouristTrap.ANNA_BARREL)
                                    removeAttribute(player, "ana-delay")
                                    this@AnnaCartCutscene.stop(true)
                                    return true
                                }
                            }
                            return false
                        }
                    },
                )
            }

            override fun newInstance(p: Player): ActivityPlugin {
                return AnnaCartCutscene(p)
            }

            override fun getSpawnLocation(): Location? {
                return null
            }

            override fun getStartLocation(): Location {
                return base.transform(54, 23, 0)
            }

            override fun configure() {
                region = DynamicRegion.create(13203)
                setRegionBase()
                registerRegion(region.id)
            }

            companion object {
                private val PATHS =
                    arrayOf(
                        arrayOf(
                            Location.create(3315, 9417, 0),
                            Location.create(3317, 9417, 0),
                            Location.create(3318, 9418, 0),
                            Location.create(3318, 9428, 0),
                        ),
                        arrayOf(
                            Location.create(3318, 9430, 0),
                            Location.create(3318, 9418, 0),
                            Location.create(3317, 9417, 0),
                            Location.create(3316, 9417, 0),
                            Location.create(3314, 9417, 0),
                            Location.create(3303, 9417, 0),
                        ),
                    )
            }
        }
    }

    class CartDialogue(
        player: Player? = null,
    ) : Dialogue(player) {
        override fun open(vararg args: Any): Boolean {
            interpreter.sendDialogue("There is space on the cart for you get on, would you like to try?")
            return true
        }

        override fun handle(
            interfaceId: Int,
            buttonId: Int,
        ): Boolean {
            when (stage) {
                0 -> {
                    options("Yes, I'll get on.", "No, I've got other plans.")
                    stage++
                }

                1 ->
                    when (buttonId) {
                        1 -> {
                            interpreter.sendDialogue("You decide to climb onto the cart.")
                            stage++
                        }

                        2 -> end()
                    }

                2 -> {
                    player.lock()
                    player.animate(ClimbActionHandler.CLIMB_UP)
                    Pulser.submit(
                        object : Pulse(1, player) {
                            var counter: Int = 0

                            override fun pulse(): Boolean {
                                when (counter++) {
                                    1 -> player.interfaceManager.openOverlay(Component(115))
                                    4 ->
                                        player.dialogueInterpreter.sendDialogue(
                                            "As soon as you get on the cart, it starts to move.",
                                            "Before too long you are past the gates. You jump off",
                                            "the cart taking Ana with you.",
                                        )

                                    6 -> {
                                        player.unlock()
                                        player.interfaceManager.closeOverlay()
                                        player
                                            .getQuestRepository()
                                            .getQuest(Quests.THE_TOURIST_TRAP)
                                            .setStage(player, 95)
                                        player.interfaceManager.close()
                                        player.properties.teleportLocation = Location.create(3258, 3029, 0)
                                        player.inventory.add(TouristTrap.ANNA_BARREL)
                                        return true
                                    }
                                }
                                return false
                            }
                        },
                    )
                    end()
                }
            }
            return true
        }

        override fun getIds(): IntArray {
            return intArrayOf(DialogueInterpreter.getDialogueKey("ana cart dialogue"))
        }
    }

    class MineCartDialogue(
        player: Player? = null,
    ) : Dialogue(player) {
        private var cart: Scenery? = null

        override fun init() {
            super.init()
            definePlugin(MiningCartCutscene())
        }

        override fun open(vararg args: Any): Boolean {
            cart = args[0] as Scenery
            if (player.inventory.containsItem(TouristTrap.ANNA_BARREL)) {
                player("There's not enough room for both of us.")
                stage = -1
                return true
            }
            interpreter.sendDialogue("You search the mine cart...")
            return true
        }

        override fun handle(
            interfaceId: Int,
            buttonId: Int,
        ): Boolean {
            when (stage) {
                -1 -> end()
                0 -> {
                    interpreter.sendDialogue(
                        "There may be just enough space to squeeze yourself into the cart.",
                        "Would you like to try?",
                    )
                    stage++
                }

                1 -> {
                    options("Yes, of course.", "No thanks, it looks pretty dangerous.")
                    stage++
                }

                2 ->
                    when (buttonId) {
                        1 -> {
                            end()
                            enterCart(player)
                        }

                        2 -> end()
                    }
            }
            return true
        }

        fun enterCart(player: Player) {
            if (RandomFunction.random(3) == 1) {
                player.lock(FAIL_ANIMATION.delay)
                player.animate(FAIL_ANIMATION)
                player.impactHandler.manualHit(player, 2, HitsplatType.NORMAL)
                player.packetDispatch.sendMessages(
                    "You fail to fit yourself into the cart in time before it starts its journey.",
                    "You bang your head on the cart as you try to jump in.",
                )
            } else {
                Pulser.submit(
                    object : Pulse(3, player) {
                        override fun pulse(): Boolean {
                            player.animate(JUMP_ANIMATION)
                            return true
                        }
                    },
                )
                ActivityManager.start(player, "mining cart", false, if (cart!!.location.x == 3303) 0 else 1)
            }
        }

        class MiningCartCutscene : CutscenePlugin {
            private var index = 0

            constructor() : super("mining cart")

            constructor(player: Player?) : super("mining cart") {
                this.player = player
            }

            override fun start(
                player: Player,
                login: Boolean,
                vararg args: Any,
            ): Boolean {
                index = args[0] as Int
                return super.start(player, login, *args)
            }

            override fun open() {
                SceneryBuilder.remove(getObject(base.location.transform(path[0].localX, path[0].localY, 0)))
                SceneryBuilder.remove(
                    getObject(
                        base.location.transform(
                            path[path.size - 1].localX,
                            path[path.size - 1].localY,
                            0,
                        ),
                    ),
                )
                player.appearance.setAnimations(Animation.create(211))
                player.appearance.isRidingMinecart = true
                player.appearance.sync()
                player.walkingQueue.reset()
                for (l in path) {
                    val loc = base.transform(l.localX, l.localY, 0)
                    player.walkingQueue.addPath(loc.x, loc.y, true)
                }
                Pulser.submit(
                    object : Pulse(22, player) {
                        override fun pulse(): Boolean {
                            setAttribute(
                                player,
                                "real-end",
                                if (index == 0) Location.create(3319, 9431, 0) else Location.create(3303, 9416, 0),
                            )
                            setAttribute(
                                player,
                                "cutscene:original-loc",
                                if (index == 0) Location.create(3319, 9431, 0) else Location.create(3303, 9416, 0),
                            )
                            this@MiningCartCutscene.stop(true)
                            return true
                        }
                    },
                )
            }

            override fun end() {
                super.end()
                player.appearance.setDefaultAnimations()
                player.appearance.isRidingMinecart = false
                player.appearance.sync()
            }

            override fun fade() {
                if (index == 0) {
                    player.dialogueInterpreter.sendDialogue(
                        "You appear in a large open room with what looks like lots of miners",
                        "working away. This is a very rough looking area, the miners look like",
                        "they're on their last legs.",
                    )
                } else {
                    player.dialogueInterpreter.sendDialogue(
                        "You appear back in the barrel loading room. A nearby slave looks",
                        "surprised to see you popping out of the cart.",
                    )
                }
            }

            override fun newInstance(p: Player): ActivityPlugin {
                return MiningCartCutscene(p)
            }

            override fun getSpawnLocation(): Location? {
                return null
            }

            override fun getStartLocation(): Location {
                return base.transform(path[0].localX, path[0].localY, 0)
            }

            override fun configure() {
                region = DynamicRegion.create(13203)
                setRegionBase()
                registerRegion(region.id)
            }

            val path: Array<Location>

                get() = PATHS[index]

            companion object {
                private val PATHS =
                    arrayOf(
                        arrayOf(
                            Location.create(3303, 9417, 0),
                            Location.create(3316, 9417, 0),
                            Location.create(3317, 9417, 0),
                            Location.create(3318, 9418, 0),
                            Location.create(3318, 9430, 0),
                        ),
                        arrayOf(
                            Location.create(3318, 9430, 0),
                            Location.create(3318, 9418, 0),
                            Location.create(3317, 9417, 0),
                            Location.create(3303, 9417, 0),
                        ),
                    )
            }
        }

        override fun getIds(): IntArray {
            return intArrayOf(DialogueInterpreter.getDialogueKey("cart dialogue"))
        }

        companion object {
            private val FAIL_ANIMATION = Animation(5048)
            private val JUMP_ANIMATION = Animation(5049)
        }
    }

    class WinchCutscene : CutscenePlugin {
        constructor() : super("winch cutscene")

        constructor(player: Player?) : super("winch cutscene") {
            this.player = player
        }

        override fun newInstance(p: Player): ActivityPlugin {
            return WinchCutscene(p)
        }

        override fun open() {
            super.open()
            player.packetDispatch.sendMessage("You try to operate the winch.")
            player.faceLocation(base.transform(15, 9, 0))
            player.animate(Animation.create(5054))
            Pulser.submit(
                object : Pulse(AnimationDefinition.forId(Animations.UNTYING_5054)!!.durationTicks, player) {
                    override fun pulse(): Boolean {
                        TouristTrap.addConfig(player, 2048 + (1 shl 4))
                        this@WinchCutscene.stop(true)
                        player.dialogueInterpreter.open(822, true, true)
                        return true
                    }
                },
            )
        }

        override fun getSpawnLocation(): Location? {
            return null
        }

        override fun getStartLocation(): Location {
            return base.transform(16, 10, 0)
        }

        override fun configure() {
            region = DynamicRegion.create(13103)
            setRegionBase()
            registerRegion(region.id)
        }
    }

    class BedabinKeyHandler : UseWithHandler(TouristTrap.BEDABIN_KEY.id) {
        override fun newInstance(arg: Any?): Plugin<Any> {
            addHandler(org.rs.consts.Scenery.CHEST_2677, OBJECT_TYPE, this)
            return this
        }

        override fun handle(event: NodeUsageEvent): Boolean {
            val player = event.player
            player.dialogueInterpreter.open(831, getNpc(player, 831)!!)
            return true
        }
    }

    class BarrelDialogue(
        player: Player? = null,
    ) : Dialogue(player) {
        private var barrel: Scenery? = null

        private var quest: Quest? = null

        override fun open(vararg args: Any): Boolean {
            barrel = args[0] as Scenery
            quest = player.getQuestRepository().getQuest(Quests.THE_TOURIST_TRAP)
            if ((quest!!.getStage(player) == 70 || quest!!.getStage(player) == 72) &&
                !player.hasItem(TouristTrap.ANNA_BARREL)
            ) {
                interpreter.sendDialogue("You search the barrels and find Ana.")
                stage = 400
                return true
            }
            if (player.inventory.containsItem(TouristTrap.BARREL)) {
                player.packetDispatch.sendMessage("You can only manage to have one of these at a time.")
                end()
                return true
            }
            interpreter.sendItemMessage(
                TouristTrap.BARREL,
                "This barrel is quite big. but you may be able to carry one. Would you like to take one?",
            )
            return true
        }

        override fun handle(
            interfaceId: Int,
            buttonId: Int,
        ): Boolean {
            when (stage) {
                400 -> {
                    interpreter.sendDialogues(NPC(823), -1, "Let me out!")
                    stage++
                }

                401 -> {
                    if (!player.inventory.hasSpaceFor(TouristTrap.ANNA_BARREL)) {
                        end()
                        return true
                    }
                    player.packetDispatch.sendMessage("You pick up Ana in a Barrel.")
                    player.inventory.add(TouristTrap.ANNA_BARREL)
                    end()
                }

                402 -> end()
                0 -> {
                    options("Yeah, cool!", "No thanks.")
                    stage++
                }

                1 ->
                    when (buttonId) {
                        1 -> {
                            interpreter.sendItemMessage(
                                TouristTrap.BARREL,
                                "You take the barrel, it's not heavy, just awkward.",
                            )
                            stage = 3
                        }

                        2 -> end()
                    }

                3 -> {
                    end()
                    player.inventory.add(TouristTrap.BARREL, player)
                    SceneryBuilder.remove(barrel)
                    Pulser.submit(
                        object : Pulse(40) {
                            override fun pulse(): Boolean {
                                SceneryBuilder.add(barrel)
                                return true
                            }
                        },
                    )
                }
            }
            return true
        }

        override fun getIds(): IntArray {
            return intArrayOf(DialogueInterpreter.getDialogueKey("barrel dialogue"))
        }
    }

    class BedabinAnvilHandler : UseWithHandler(2349) {
        override fun newInstance(arg: Any?): Plugin<Any> {
            addHandler(2672, OBJECT_TYPE, this)
            definePlugin(PrototypeDartHandler())
            return this
        }

        override fun handle(event: NodeUsageEvent): Boolean {
            val player = event.player
            val quest = player.getQuestRepository().getQuest(Quests.THE_TOURIST_TRAP)
            if (quest.getStage(player) == 54 && player.inventory.containsItem(TouristTrap.TECHNICAL_PLANS)) {
                player.dialogueInterpreter.open("bedabin-anvil")
                return true
            } else {
                player.packetDispatch.sendMessage("You need technical plans in order to do this.")
            }
            return true
        }

        class AnnaWinchHandler : UseWithHandler(TouristTrap.ANNA_BARREL.id) {
            override fun newInstance(arg: Any?): Plugin<Any> {
                addHandler(18951, OBJECT_TYPE, this)
                return this
            }

            override fun handle(event: NodeUsageEvent): Boolean {
                val player = event.player
                val quest = player.getQuestRepository().getQuest(Quests.THE_TOURIST_TRAP)
                if (event.usedWith.location != Location.create(3292, 9423, 0)) {
                    return false
                }
                if (quest.getStage(player) == 70) {
                    player.dialogueInterpreter.open("winch dialogue", true)
                    return true
                }
                return false
            }
        }

        class PrototypeDartHandler : UseWithHandler(314) {
            override fun newInstance(arg: Any?): Plugin<Any> {
                addHandler(TouristTrap.PROTOTYPE_DART_TIP.id, ITEM_TYPE, this)
                return this
            }

            override fun handle(event: NodeUsageEvent): Boolean {
                val player = event.player
                player.dialogueInterpreter.open("prototype dart")
                return true
            }

            class ProtoTypeDialogue(
                player: Player? = null,
            ) : Dialogue(player) {
                override fun open(vararg args: Any): Boolean {
                    if (!player.inventory.containsItem(FEATHERS)) {
                        interpreter.sendDialogue("You need 10 feathers in order to do this.")
                        stage = 10
                        return false
                    }
                    interpreter.sendItemMessage(314, "You try to attach feathers to the bronze dart tip.")
                    stage = 1
                    return true
                }

                override fun handle(
                    interfaceId: Int,
                    buttonId: Int,
                ): Boolean {
                    when (stage) {
                        1 -> {
                            interpreter.sendDialogue("Following the plans is tricky, but you persevere.")
                            stage++
                        }

                        2 -> {
                            interpreter.sendItemMessage(
                                TouristTrap.PROTOTYPE_DART,
                                "You successfully attach the feathers to the dart tip.",
                            )
                            stage++
                        }

                        3 -> {
                            player.inventory.remove(FEATHERS, TouristTrap.PROTOTYPE_DART_TIP)
                            player.inventory.add(TouristTrap.PROTOTYPE_DART)
                            end()
                        }

                        4 -> end()
                    }
                    return true
                }

                override fun getIds(): IntArray {
                    return intArrayOf(DialogueInterpreter.getDialogueKey("prototype dart"))
                }

                companion object {
                    private val FEATHERS = Item(314, 10)
                }
            }
        }

        class BedabinAnvilDialogue(
            player: Player? = null,
        ) : Dialogue(player) {
            override fun open(vararg args: Any): Boolean {
                if (!player.inventory.containsItem(TouristTrap.TECHNICAL_PLANS)) {
                    player.packetDispatch.sendMessage("You need the plans to do this.")
                    return false
                }
                player.dialogueInterpreter.sendItemMessage(
                    TouristTrap.TECHNICAL_PLANS,
                    "Do you want to follow the technical plans ?",
                )
                return true
            }

            override fun handle(
                interfaceId: Int,
                buttonId: Int,
            ): Boolean {
                when (stage) {
                    0 -> {
                        options("Yes. I'd like to try.", "No, not just yet.")
                        stage++
                    }

                    1 ->
                        when (buttonId) {
                            1 -> {
                                end()
                                player.pulseManager.run(ProtoTypePulse(player))
                            }

                            2 -> end()
                        }
                }
                return true
            }

            class ProtoTypePulse(
                player: Player?,
            ) : SkillPulse<Scenery?>(player, null) {
                private var ticks = 0

                override fun checkRequirements(): Boolean {
                    if (!player.inventory.hasSpaceFor(TouristTrap.PROTOTYPE_DART_TIP)) {
                        player.packetDispatch.sendMessage("Not enough inventory space.")
                        return false
                    }
                    if (!player.inventory.containsItem(HAMMER)) {
                        player.packetDispatch.sendMessage("You need a hammer in order to work metal with.")
                        return false
                    }
                    return player.inventory.containsItem(Bar.BRONZE.product)
                }

                override fun animate() {
                    if (ticks % 4 == 0) {
                        player.animate(Animation.create(898))
                    }
                }

                override fun reward(): Boolean {
                    if (++ticks % 4 != 0) {
                        return false
                    }
                    if (ticks == 4) {
                        player.dialogueInterpreter.sendPlainMessage(
                            true,
                            "You begin experimenting in forging the weapon...",
                        )
                    } else if (ticks == 8) {
                        player.inventory.remove(Bar.BRONZE.product)
                        player.inventory.add(TouristTrap.PROTOTYPE_DART_TIP)
                        player.dialogueInterpreter.sendItemMessage(
                            TouristTrap.PROTOTYPE_DART_TIP,
                            "You follow the plans carefully, and after some careful",
                            "work, you finally manage to forge a sharp, pointed...",
                            "dart tip.",
                        )
                        return true
                    }
                    return false
                }

                override fun stop() {
                    super.stop()
                }

                companion object {
                    private val HAMMER = Item(2347)
                }
            }

            override fun getIds(): IntArray {
                return intArrayOf(DialogueInterpreter.getDialogueKey("bedabin-anvil"))
            }
        }
    }
}
