package content.region.kandarin.quest.waterfall.handlers

import core.api.*
import core.cache.def.impl.ItemDefinition
import core.cache.def.impl.NPCDefinition
import core.cache.def.impl.SceneryDefinition
import core.game.dialogue.FaceAnim
import core.game.global.action.ClimbActionHandler.climb
import core.game.global.action.ClimbActionHandler.climbLadder
import core.game.global.action.DoorActionHandler.handleAutowalkDoor
import core.game.interaction.NodeUsageEvent
import core.game.interaction.OptionHandler
import core.game.interaction.UseWithHandler
import core.game.node.Node
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.ClassScanner.definePlugin
import core.plugin.Plugin
import org.rs.consts.Quests

class WaterfallPlugin : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        definePlugin(WaterfallUseWithHandler())
        NPCDefinition.forId(305).handlers["option:talk-to"] = this
        SceneryDefinition.forId(1987).handlers["option:board"] = this
        SceneryDefinition.forId(2020).handlers["option:climb"] = this
        SceneryDefinition.forId(2022).handlers["option:get in"] = this
        SceneryDefinition.forId(10283).handlers["option:swim"] = this
        SceneryDefinition.forId(1996).handlers["option:swim to"] = this
        SceneryDefinition.forId(1989).handlers["option:search"] = this
        SceneryDefinition.forId(1990).handlers["option:search"] = this
        SceneryDefinition.forId(1991).handlers["option:open"] = this
        SceneryDefinition.forId(37247).handlers["option:open"] = this
        SceneryDefinition.forId(32711).handlers["option:open"] = this
        SceneryDefinition.forId(33046).handlers["option:open"] = this
        SceneryDefinition.forId(42313).handlers["option:open"] = this
        SceneryDefinition.forId(33047).handlers["option:search"] = this
        SceneryDefinition.forId(33047).handlers["option:close"] = this
        SceneryDefinition.forId(33066).handlers["option:search"] = this
        SceneryDefinition.forId(1999).handlers["option:search"] = this
        SceneryDefinition.forId(42319).handlers["option:climb-up"] = this
        SceneryDefinition.forId(2002).handlers["option:open"] = this
        SceneryDefinition.forId(2014).handlers["option:take treasure"] = this
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        val id = node.id
        val quest = player.getQuestRepository().getQuest(Quests.WATERFALL_QUEST)
        if (quest == null) {
            player.sendMessage("Error! Waterfall quest cannot be found.")
            return true
        }
        when (id) {
            37247 -> {
                player.packetDispatch.sendMessage("The door opens...")
                player.pulseManager.run(
                    object : Pulse(2, player) {
                        override fun pulse(): Boolean {
                            if ((
                                    player.equipment.containsAtLeastOneItem(295) ||
                                        player.inventory.contains(
                                            295,
                                            1,
                                        )
                                ) ||
                                player.getQuestRepository().isComplete("Waterfall")
                            ) {
                                player.packetDispatch.sendMessage("You walk through the door.")
                                player.teleport(Location(2575, 9861))
                            } else {
                                player.packetDispatch.sendMessage("The cave floods and washes you away!")
                                player.teleport(Location(2527, 3413))
                            }
                            return true
                        }
                    },
                )
            }

            2014 ->
                if (quest.getStage(player) != 100) {
                    player.packetDispatch.sendMessage("The cavern floods!")
                    player.lock(6)
                    player.pulseManager.run(
                        object : Pulse(5, player) {
                            override fun pulse(): Boolean {
                                player.teleport(Location(2566, 9901))
                                player.packetDispatch.sendMessage("You are washed out of the chalice room.")
                                removeAttribute(player, "waterfall_placed_runes")
                                return true
                            }
                        },
                    )
                } else {
                    player.packetDispatch.sendMessage("You have already looted the treasure.")
                }

            1999 ->
                if (inInventory(player, 298, 1) || quest.getStage(player) < 30) {
                    sendMessage(player, "You search the crate and find nothing.")
                } else if (quest.getStage(player) >= 30 && !inInventory(player, 298, 1)) {
                    sendMessage(player, "You find a large key.")
                    addItemOrDrop(player, 298, 1)
                }

            2002 -> {
                if (player.location == Location(2568, 9893)) {
                    player.packetDispatch.sendMessage("The door is locked.")
                } else if (node.location == Location(2566, 9901) || player.location == Location(2568, 9894, 0)) {
                    if (quest.getStage(player) >= 100 &&
                        node.location ==
                        Location(
                            2566,
                            9901,
                        ) &&
                        player.location == Location(2566, 9901)
                    ) {
                        player.teleport(Location(2604, 9901))
                    } else {
                        handleAutowalkDoor(player, (node as Scenery))
                    }
                }
                if (node.location == Location(2604, 9900)) {
                    player.teleport(Location(2566, 9901))
                    removeAttribute(player, "waterfall_placed_runes")
                }
            }

            33046 ->
                SceneryBuilder.add(
                    Scenery(
                        33047,
                        Location.create(2530, 9844, 0),
                        10,
                        1,
                    ),
                )

            42319 ->
                if (node.location == Location(2556, 9844)) {
                    climb(player, Animation(828), Location.create(2557, 3444, 0))
                } else {
                    climbLadder(player, node as Scenery, option)
                }

            33047 ->
                when (option) {
                    "open", "search" ->
                        if (quest.getStage(player) >= 30) {
                            if (!player.hasItem(Item(295))) {
                                player.packetDispatch.sendMessage("You search the chest and find a small amulet.")
                                player.inventory.add(Item(295, 1))
                            } else {
                                player.packetDispatch.sendMessage("You search the chest and find nothing.")
                            }
                        }

                    "close" ->
                        SceneryBuilder.add(
                            Scenery(
                                33046,
                                Location.create(2530, 9844, 0),
                                10,
                                1,
                            ),
                        )
                }

            33066 ->
                if (quest.getStage(player) >= 30) {
                    if (!player.inventory.contains(296, 1)) {
                        player.packetDispatch.sendMessage(
                            "You search the coffin and inside you find an urn full of ashes.",
                        )
                        player.inventory.add(Item(296, 1))
                    } else {
                        player.packetDispatch.sendMessage("You search the coffin and find nothing.")
                    }
                }

            32711 -> player.teleport(Location(2511, 3463))
            2020 -> player.dialogueInterpreter.open("waterfall_tree_dialogue", 0)
            305 -> player.dialogueInterpreter.open(305, node)
            1990 ->
                if (inInventory(player, 293, 1) || quest.getStage(player) < 30) {
                    sendMessage(player, "You search the crate and find nothing.")
                } else if (quest.getStage(player) >= 30 && !inInventory(player, 293, 1)) {
                    sendMessage(player, "You find a large key.")
                    addItemOrDrop(player, 293, 1)
                }

            1991 ->
                if (player.location.y >= 9576) {
                    handleAutowalkDoor(player, (node as Scenery))
                    player.packetDispatch.sendMessage("You open the gate and walk through.")
                } else if (player.inventory.contains(293, 1) && player.location.y < 9576) {
                    player.packetDispatch.sendMessage(
                        "The gate is locked. You need to use the key on the door to enter.",
                    )
                } else {
                    player.dialogueInterpreter.sendDialogues(
                        306,
                        FaceAnim.OLD_DEFAULT,
                        "Hello? Ah yes, the door is still locked.",
                        "If you want to get in here, you'll need to find the key",
                        "that I hid in some crates in the eastern room.",
                    )
                }

            1989 ->
                if (player.inventory.contains(292, 1) || quest.getStage(player) != 20) {
                    player.packetDispatch.sendMessage("You search the bookcase and find nothing of interest.")
                } else if (!player.hasItem(Item(292)) && quest.getStage(player) == 20) {
                    player.inventory.add(Item(292, 1))
                    player.packetDispatch.sendMessage(
                        "You search the bookcase and find a book named 'Book on Baxtorian'",
                    )
                }

            10283, 1996 -> {
                player.logoutListeners["waterfall"] = { player1: Player ->
                    player1.location = player.location.transform(0, 0, 0)
                }
                player.packetDispatch.sendGraphic(68)
                player.lock(6)
                content.global.skill.agility.AgilityHandler.walk(
                    player,
                    -1,
                    player.location,
                    Location(2512, 3471, 0),
                    Animation.create(164),
                    0.0,
                    null,
                )
                player.packetDispatch.sendMessage("It looks like a long distance, but you swim out into the water.")
                player.packetDispatch.sendMessage("The current is too strong, you feel yourself being pulled under", 3)
                player.pulseManager.run(
                    object : Pulse(6, player) {
                        override fun pulse(): Boolean {
                            player.packetDispatch.sendMessage("You are washed downstream but feel lucky to be alive.")
                            player.teleport(Location(2527, 3413))
                            return true
                        }
                    },
                )
            }

            2022 -> {
                player.packetDispatch.sendMessage("You get in the barrel and start rocking.")
                player.packetDispatch.sendMessage("The barrel falls off the ledge.")
                player.teleport(Location(2527, 3413))
            }

            1987 -> {
                if (quest.getStage(player) >= 10) {
                    player.packetDispatch.sendMessage("You board the small raft", 2)
                    player.lock(13)
                    player.packetDispatch.sendMessage("and push off down stream.", 6)
                    player.packetDispatch.sendMessage("The raft is pulled down stream by strong currents", 9)
                    player.pulseManager.run(
                        object : Pulse(12, player) {
                            override fun pulse(): Boolean {
                                player.packetDispatch.sendMessage("You crash into a small land mound.")
                                player.teleport(Location(2512, 3481))
                                return true
                            }
                        },
                    )
                } else {
                    player.dialogueInterpreter.sendDialogue("You have no reason to board this raft.")
                }
                return true
            }
        }
        return true
    }

    override fun getDestination(
        node: Node,
        n: Node,
    ): Location? {
        if (n is NPC) {
            if (n.id == 305) {
                return Location.create(2512, 3481, 0)
            }
        }
        if (n is Scenery) {
            val obj = n
            if (obj.id == 1996 || obj.id == 1997) {
                return Location.create(2512, 3476, 0)
            }
        }
        return null
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

    fun handleObjects(
        add: Boolean,
        player: Player?,
    ) {
        if (add) {
            ROPES.add(Scenery(1997, Location.create(2512, 3468, 0), 10, 0))
            ROPES.add(Scenery(1998, Location.create(2512, 3469, 0), 10, 0))
            ROPES.add(Scenery(1998, Location.create(2512, 3470, 0), 10, 0))
            ROPES.add(Scenery(1998, Location.create(2512, 3471, 0), 10, 0))
            ROPES.add(Scenery(1998, Location.create(2512, 3472, 0), 10, 0))
            ROPES.add(Scenery(1998, Location.create(2512, 3473, 0), 10, 0))
            ROPES.add(Scenery(1998, Location.create(2512, 3474, 0), 10, 0))
            ROPES.add(Scenery(1998, Location.create(2512, 3475, 0), 10, 0))
            for (rope in ROPES) {
                SceneryBuilder.add(rope)
            }
        } else {
            for (rope in ROPES) {
                SceneryBuilder.remove(rope)
            }
            ROPES.clear()
            SceneryBuilder.add(
                Scenery(
                    1996,
                    Location.create(2512, 3468, 0),
                    10,
                    0,
                ),
            )
        }
    }

    fun playRunestoneGraphics(player: Player) {
        player.packetDispatch.sendGlobalPositionGraphic(580, Location(2562, 9914))
        player.packetDispatch.sendGlobalPositionGraphic(580, Location(2562, 9912))
        player.packetDispatch.sendGlobalPositionGraphic(580, Location(2562, 9910))
        player.packetDispatch.sendGlobalPositionGraphic(580, Location(2569, 9914))
        player.packetDispatch.sendGlobalPositionGraphic(580, Location(2569, 9912))
        player.packetDispatch.sendGlobalPositionGraphic(580, Location(2569, 9910))
    }

    inner class WaterfallUseWithHandler :
        UseWithHandler(
            ROPE.id,
            KEY.id,
            PEBBLE.id,
            KEY_2.id,
            AMULET.id,
            URN.id,
            AIR_RUNE.id,
            EARTH_RUNE.id,
            WATER_RUNE.id,
        ) {
        private val OBJECTS = intArrayOf(1996, 1997, 2020, 1991, 1992, 2002, 2006, 2014, 2004)

        override fun newInstance(arg: Any?): Plugin<Any> {
            for (id in OBJECTS) {
                addHandler(id, OBJECT_TYPE, this)
            }
            return this
        }

        override fun getDestination(
            playa: Player,
            n: Node,
        ): Location? {
            if (n is Scenery) {
                val obj = n
                if (obj.id == 1996 || obj.id == 1997) {
                    return Location.create(2512, 3476, 0)
                }
            }
            return null
        }

        override fun handle(event: NodeUsageEvent): Boolean {
            val player = event.player
            val useditem = event.usedItem
            val quest = player.getQuestRepository().getQuest(Quests.WATERFALL_QUEST)
            val scenery = event.usedWith as Scenery

            if (useditem.id == ROPE.id && scenery.id == 1996 || scenery.id == 1997) {
                player.lock(8)
                player.animate(Animation.create(774))
                SWIMMERS.add(player)
                if (SWIMMERS.size == 0 || ROPES.size == 0) {
                    handleObjects(true, player)
                }
                player.logoutListeners["waterfall"] = { player1: Player ->
                    player1.location = player.location.transform(0, 0, 0)
                }
                player.pulseManager.run(
                    object : Pulse(2, player) {
                        override fun pulse(): Boolean {
                            player.faceLocation(Location(2512, 3468, 0))
                            content.global.skill.agility.AgilityHandler.walk(
                                player,
                                -1,
                                player.location,
                                Location(2512, 3469, 0),
                                Animation.create(273),
                                0.0,
                                null,
                            )
                            player.pulseManager.run(
                                object : Pulse(8, player) {
                                    override fun pulse(): Boolean {
                                        player.teleport(Location(2513, 3468))
                                        player.faceLocation(Location(2512, 3468, 0))
                                        player.animate(Animation.create(780))
                                        player.logoutListeners.remove("waterfall")
                                        return true
                                    }

                                    override fun stop() {
                                        super.stop()
                                        if (SWIMMERS.remove(player) && SWIMMERS.size == 0) {
                                            handleObjects(false, player)
                                        }
                                    }
                                },
                            )
                            return true
                        }
                    },
                )
            }

            if (useditem.id == ROPE.id && scenery.id == 2020) {
                player.packetDispatch.sendMessage("You tie the rope to the tree and let yourself down on the ledge.")
                player.teleport(Location(2511, 3463))
            }

            if (useditem.id == KEY.id && scenery.id == 1991) {
                player.packetDispatch.sendMessage("The key fits the gate.")
                player.packetDispatch.sendMessage("You open the gate and walk through.")
                handleAutowalkDoor(player, scenery)
            }

            if (useditem.id == KEY_2.id && scenery.id == 2002 && scenery.location != Location(2566, 9901)) {
                player.packetDispatch.sendMessage("You open the door and walk through.")
                handleAutowalkDoor(player, scenery)
            }

            if (useditem.id == PEBBLE.id && scenery.id == 1992 && ItemDefinition.canEnterEntrana(player)) {
                if (player.familiarManager.hasFamiliar()) {
                    sendMessage(player, "You can't take a follower into the tomb.")
                    return false
                }
                player.packetDispatch.sendMessage("You place the pebble in the gravestone's small indent.")
                player.packetDispatch.sendMessage("It fits perfectly.")
                player.packetDispatch.sendMessage("You hear a loud creak.", 4)
                player.packetDispatch.sendMessage("The stone slab slides back revealing a ladder down.", 7)
                player.packetDispatch.sendMessage("You climb down to an underground passage.", 12)
                player.pulseManager.run(
                    object : Pulse(13, player) {
                        override fun pulse(): Boolean {
                            player.getSkills().prayerPoints = 0.0
                            player.teleport(Location(2555, 9844))
                            return true
                        }
                    },
                )
            } else if (!ItemDefinition.canEnterEntrana(player) && useditem.id == PEBBLE.id && scenery.id == 1992) {
                player.packetDispatch.sendMessage("You place the pebble in the gravestone's small indent.")
                player.packetDispatch.sendMessage("It fits perfectly.")
                player.packetDispatch.sendMessage("But nothing happens.", 4)
            }
            if ((useditem.id == AIR_RUNE.id || useditem.id == EARTH_RUNE.id || useditem.id == WATER_RUNE.id) &&
                scenery.id == 2004
            ) {
                if (player.inventory.contains(555, 6) &&
                    player.inventory.contains(556, 6) &&
                    player.inventory.contains(
                        557,
                        6,
                    )
                ) {
                    if (player.getAttribute<Any?>("waterfall_placed_runes") != null) {
                        player.packetDispatch.sendMessage("You have already placed the runes on the pillars.")
                        return false
                    }
                    playRunestoneGraphics(player)
                    player.packetDispatch.sendMessage("You place one of each runestone on all six of the pillars.")
                    setAttribute(player, "waterfall_placed_runes", 1)
                    player.inventory.remove(Item(AIR_RUNE.id, 6))
                    player.inventory.remove(Item(EARTH_RUNE.id, 6))
                    player.inventory.remove(Item(WATER_RUNE.id, 6))
                } else {
                    player.packetDispatch.sendMessage("You do not have enough runestones to place on the pillars.")
                }
            }

            if (useditem.id == AMULET.id &&
                scenery.id == 2006 &&
                quest.getStage(player) != 100 &&
                player.location !=
                Location(
                    2603,
                    9914,
                )
            ) {
                if (player.getAttribute<Any?>("waterfall_placed_runes") == null) {
                    player.packetDispatch.sendMessage("You place the amulet around the neck of the statue.")
                    player.lock(4)
                    player.pulseManager.run(
                        object : Pulse(3, player) {
                            override fun pulse(): Boolean {
                                player.packetDispatch.sendGraphic(74)
                                player.impactHandler.manualHit(player, 20, HitsplatType.NORMAL)
                                player.packetDispatch.sendMessage(
                                    "Rocks fall from the ceiling and hit you in the head.",
                                )
                                return true
                            }
                        },
                    )
                } else if (player.getAttribute<Any?>("waterfall_placed_runes") != null) {
                    player.lock(7)
                    player.inventory.remove(AMULET)
                    player.packetDispatch.sendMessage("You place the amulet around the neck of the statue.")
                    player.packetDispatch.sendMessage("You hear a loud rumble from beneath...", 3)
                    player.packetDispatch.sendMessage("The ground raises up before you!", 6)
                    player.pulseManager.run(
                        object : Pulse(6, player) {
                            override fun pulse(): Boolean {
                                player.teleport(Location(2603, 9914))
                                return true
                            }
                        },
                    )
                }
            }

            if (useditem.id == URN.id && scenery.id == 2014 && quest.getStage(player) != 100) {
                if (player.inventory.freeSlots() < 5) {
                    player.packetDispatch.sendMessage("You'll need 5 free inventory slots to take Glarial's treasure.")
                    return false
                } else {
                    player.lock(10)
                    player.inventory.remove(URN)
                    player.inventory.add(URN_EMPTY)
                    player.packetDispatch.sendMessage("You carefully pour the ashes into the chalice")
                    player.packetDispatch.sendMessage("as you remove the Treasure of Baxtorian", 3)
                    player.packetDispatch.sendMessage("The chalice remains standing.", 6)
                    player.packetDispatch.sendMessage("Inside you find a mithril case", 6)
                    player.packetDispatch.sendMessage("containing 40 seeds,", 6)
                    player.packetDispatch.sendMessage("two diamonds and two gold bars.", 6)
                    player.pulseManager.run(
                        object : Pulse(8, player) {
                            override fun pulse(): Boolean {
                                quest.finish(player)
                                return true
                            }
                        },
                    )
                }
            }
            return true
        }
    }

    companion object {
        val ROPE: Item = Item(954)
        val KEY: Item = Item(293)
        val KEY_2: Item = Item(298)
        val PEBBLE: Item = Item(294)
        val AMULET: Item = Item(295)
        val URN: Item = Item(296)
        val URN_EMPTY: Item = Item(297)
        val AIR_RUNE: Item = Item(556, 6)
        val WATER_RUNE: Item = Item(555, 6)
        val EARTH_RUNE: Item = Item(557, 6)

        private val SWIMMERS: MutableList<Player> = ArrayList(20)
        private val ROPES: MutableList<Scenery> = ArrayList(20)
    }
}
