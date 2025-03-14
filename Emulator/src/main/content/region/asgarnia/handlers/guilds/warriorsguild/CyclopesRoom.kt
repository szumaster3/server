package content.region.asgarnia.handlers.guilds.warriorsguild

import core.api.clearLogoutListener
import core.api.registerLogoutListener
import core.api.removeAttribute
import core.api.setAttribute
import core.cache.def.impl.SceneryDefinition
import core.game.container.impl.EquipmentContainer
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueInterpreter
import core.game.global.action.DoorActionHandler.handleAutowalkDoor
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.map.RegionManager.getObject
import core.game.world.map.path.Pathfinder
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.game.world.map.zone.ZoneRestriction
import core.plugin.ClassScanner.definePlugin
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction

@Initializable
class CyclopesRoom :
    MapZone("wg cyclopes", true, ZoneRestriction.RANDOM_EVENTS),
    Plugin<Any> {
    override fun leave(
        e: Entity,
        logout: Boolean,
    ): Boolean {
        if (e is Player) {
            leave(e)
            PLAYERS.remove(e)
            if (logout) {
                e.setLocation(Location.create(2846, 3540, 2))
            }
            clearLogoutListener(e, "cyclopes")
        }
        return super.leave(e, logout)
    }

    override fun death(
        e: Entity,
        killer: Entity,
    ): Boolean {
        if (killer is Player && e is NPC && (e.getId() == 4292 || e.getId() == 4291)) {
            var defenderId = getDefenderIndex(killer)
            if (RandomFunction.randomize(50) == 10) {
                if (++defenderId == DEFENDERS.size) {
                    defenderId--
                }
                GroundItemManager.create(Item(DEFENDERS[defenderId]), e.getLocation(), killer)
            }
        }
        return false
    }

    override fun configure() {
        super.register(ZoneBorders(2838, 3534, 2876, 3556, 2))
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        ZoneBuilder.configure(this)
        PULSE.stop()
        definePlugin(KamfreenaCyclopsRoomDialogue())
        definePlugin(
            object : OptionHandler() {
                override fun newInstance(arg: Any?): Plugin<Any> {
                    SceneryDefinition.forId(15641).handlers["option:open"] = this
                    SceneryDefinition.forId(15644).handlers["option:open"] = this
                    return this
                }

                override fun handle(
                    player: Player,
                    node: Node,
                    option: String,
                ): Boolean {
                    if (player.location.x <= 2846) {
                        val tokens = Item(8851, 100)
                        val tokens10 = Item(8851, 10)
                        if (!player.inventory.containsItem(tokens)) {
                            player.dialogueInterpreter.sendItemMessage(
                                tokens,
                                "You don't have enough Warrior Guild Tokens to enter",
                                "the cyclopes enclosure yet, collect at least 100 then",
                                "come back.",
                            )
                            return true
                        }
                        if (!player.getAttribute("sent_dialogue", false)) {
                            player.dialogueInterpreter.open(
                                DialogueInterpreter.getDialogueKey("defender entry"),
                                getDefenderIndex(player),
                            )
                            return true
                        }
                        removeAttribute(player, "sent_dialogue")
                        player.inventory.remove(tokens10)
                        player.sendMessages("10 tokens are taken as you enter the room.")
                        Companion.enter(player)
                    } else {
                        leave(player)
                        PLAYERS.remove(player)
                    }
                    handleAutowalkDoor(player, (node as Scenery))
                    return true
                }
            },
        )
        return this
    }

    override fun fireEvent(
        identifier: String,
        vararg args: Any,
    ): Any? {
        return null
    }

    private class KamfreenaCyclopsRoomDialogue : Dialogue {
        private var defenderId = 0

        constructor() : super()
        constructor(player: Player?) : super(player)

        override fun newInstance(player: Player?): Dialogue {
            return KamfreenaCyclopsRoomDialogue(player)
        }

        override fun open(vararg args: Any): Boolean {
            defenderId = args[0] as Int
            if (defenderId == -1) {
                npc(4289, "Well, since you haven't shown me a defender to prove", "your prowess as a warrior,")
            } else {
                npc(4289, "Ahh I see that you have one of the defenders already!", "Well done.")
            }
            return true
        }

        override fun handle(
            interfaceId: Int,
            buttonId: Int,
        ): Boolean {
            when (stage) {
                0 -> {
                    setAttribute(player, "sent_dialogue", true)
                    if (defenderId == DEFENDERS.size - 1) {
                        npc(
                            4289,
                            "I'll release some cyclopes which might drop the same",
                            "rune defender for you as there isn't any higher! Have",
                            "fun in there.",
                        )
                    } else if (defenderId == -1) {
                        npc(
                            4289,
                            "I'll release some cyclopes which might drop bronze",
                            "defenders for you to start off with, unless you show me",
                            "another. Have fun in there.",
                        )
                    } else {
                        npc(
                            4289,
                            "I'll release some cyclopes which might drop the next",
                            "defender for you. Have fun in there.",
                        )
                    }
                    stage = 1
                }

                1 -> end()
            }
            return true
        }

        override fun getIds(): IntArray {
            return intArrayOf(DialogueInterpreter.getDialogueKey("defender entry"))
        }
    }

    companion object {
        private val DEFENDERS = intArrayOf(8844, 8845, 8846, 8847, 8848, 8849, 8850, 14663)
        private val PLAYERS: MutableList<Player> = ArrayList(20)

        private val PULSE: Pulse =
            object : Pulse(5) {
                override fun pulse(): Boolean {
                    if (PLAYERS.isEmpty()) {
                        return true
                    }
                    val it = PLAYERS.iterator()
                    while (it.hasNext()) {
                        val player = it.next()
                        val current = player.getAttribute("cyclops_ticks", 0) + 5
                        if (current % 100 == 0) {
                            val tokens = Item(8851, 10)
                            if (!player.inventory.containsItem(tokens)) {
                                if (!player.locks.isMovementLocked) {
                                    player.pulseManager.clear()
                                    Pathfinder.find(player.location, Location.create(2847, 3541, 2)).walk(player)
                                    player.lock(50)
                                } else {
                                    val scenery = getObject(2, 2847, 3541)
                                    if (scenery != null && player.location.x == 2847 && player.location.y == 3541) {
                                        handleAutowalkDoor(player, scenery)
                                        leave(player)
                                        player.unlock()
                                        player.lock(3)
                                        it.remove()
                                    }
                                }
                                continue
                            }
                            player.inventory.remove(tokens)
                            player.packetDispatch.sendMessage("10 of your tokens crumble away.")
                        }
                        player.setAttribute("cyclops_ticks", current)
                    }
                    return false
                }
            }

        private fun getDefenderIndex(player: Player): Int {
            var index = -1
            for (i in DEFENDERS.indices.reversed()) {
                val id = DEFENDERS[i]
                if (player.equipment.getNew(EquipmentContainer.SLOT_SHIELD).id == id ||
                    player.inventory.contains(
                        id,
                        1,
                    )
                ) {
                    index = i
                    break
                }
            }
            return index
        }

        private fun enter(player: Player) {
            if (!PLAYERS.contains(player)) {
                PLAYERS.add(player)
            }
            if (!PULSE.isRunning) {
                PULSE.restart()
                PULSE.start()
                Pulser.submit(PULSE)
            }

            registerLogoutListener(player, "cyclopes") { player1: Player ->
                player1.location = Location.create(2844, 3540, 2)
            }
        }

        private fun leave(player: Player) {
            removeAttribute(player, "cyclops_ticks")
        }
    }
}
