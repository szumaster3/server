package content.global.skill.slayer.dungeon

import content.global.skill.agility.AgilityHandler
import core.api.*
import core.api.ui.restoreTabs
import core.api.ui.setMinimapState
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.Option
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.game.world.map.zone.ZoneRestriction
import core.game.world.update.flag.context.Animation
import core.plugin.ClassScanner
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class AncientCavern :
    MapZone("ancient cavern", true, ZoneRestriction.CANNON),
    Plugin<Any?> {
    companion object {
        private val LOOTS =
            arrayOf(
                Item(Items.BONES_526),
                Item(Items.MANGLED_BONES_11337),
                Item(Items.ANCIENT_PAGE_11341),
            )
        private val SKELETONS =
            intArrayOf(
                NPCs.SKELETON_HERO_6103,
                NPCs.SKELETON_HEAVY_6106,
            )
    }

    override fun newInstance(arg: Any?): Plugin<Any?> {
        ZoneBuilder.configure(this)
        ClassScanner.definePlugin(
            object : OptionHandler() {
                override fun newInstance(arg: Any?): Plugin<Any?> {
                    SceneryDefinition.forId(org.rs.consts.Scenery.WHIRLPOOL_25274).handlers["option:dive in"] = this
                    return this
                }

                override fun handle(
                    player: Player,
                    node: Node,
                    option: String,
                ): Boolean {
                    lock(player, 30)
                    AgilityHandler.forceWalk(
                        player,
                        -1,
                        player.location,
                        player.location.transform(0, -6, 0),
                        Animation.create(Animations.ANCIENT_CAVERN_DIVE_6723),
                        10,
                        0.0,
                        null,
                    )
                    GameWorld.Pulser.submit(
                        object : Pulse(1, player) {
                            private var count = 0

                            override fun pulse(): Boolean {
                                return when (++count) {
                                    4 -> {
                                        openOverlay(player, Components.FADE_TO_BLACK_115)
                                        return false
                                    }

                                    7 -> {
                                        resetAnimator(player)
                                        closeOverlay(player)
                                        closeInterface(player)
                                        teleport(player, Location.create(1763, 5365, 1))
                                        sendMessages(
                                            player,
                                            "You dive into the swirling maelstrom of the whirlpool.",
                                            "You are swirled beneath the water, the darkness and pressure are overwhelming.",
                                            "Mystical forces guide you into a cavern below the whirlpool.",
                                        )
                                        return false
                                    }

                                    8 -> {
                                        unlock(player)
                                        return true
                                    }

                                    else -> return false
                                }
                            }
                        },
                    )
                    return true
                }

                override fun getDestination(
                    node: Node,
                    n: Node,
                ): Location {
                    return if (node.location.x <= 2511) {
                        Location.create(2511, 3516, 0)
                    } else {
                        Location.create(2512, 3516, 0)
                    }
                }
            },
        )
        return this
    }

    override fun interact(
        e: Entity,
        target: Node,
        option: Option,
    ): Boolean {
        if (e is Player) {
            return when (target.id) {
                25216 -> {
                    handleLog(e)
                    return true
                }

                25362 -> {
                    rummageSkeleton(e, target as Scenery)
                    return true
                }

                else -> super.interact(e, target, option)
            }
        }
        return super.interact(e, target, option)
    }

    private fun handleLog(player: Player) {
        player.lock(14)
        player.impactHandler.disabledTicks = 13
        GameWorld.Pulser.submit(
            object : Pulse(1) {
                private var count = 0

                override fun pulse(): Boolean {
                    return when (count++) {
                        1 -> {
                            openOverlay(player, Components.FADE_TO_BLACK_120)
                            return false
                        }

                        2 -> {
                            setMinimapState(player, 2)
                            removeTabs(player, 0, 1, 2, 3, 4, 5, 6, 11, 12)
                            return false
                        }

                        13 -> {
                            teleport(player, Location.create(2532, 3412, 0))
                            openInterface(player, Components.FADE_FROM_BLACK_170)
                            return false
                        }

                        14 -> {
                            setMinimapState(player, 0)
                            restoreTabs(player)
                            closeInterface(player)
                            closeOverlay(player)
                            unlock(player)
                            return true
                        }

                        else -> false
                    }
                }
            },
        )
    }

    private fun rummageSkeleton(
        player: Player,
        obj: Scenery,
    ) {
        val random = RandomFunction.random(0, 2)
        sendMessage(player, "You rummage in the sharp, slimy pile of bones in search of something useful...")
        when (random) {
            0 -> {
                if (freeSlots(player) > 1) {
                    player.inventory.add(LOOTS.random(), player)
                    sendMessage(player, "...you find something and stow it in your pack.")
                } else {
                    sendMessage(player, "...you find something, but it drops to the floor.")
                }
            }

            1 -> {
                val spawn = NPC.create(SKELETONS.random(), obj.location)
                spawn.init()
                spawn.isRespawn = false
                removeSkeleton(obj, spawn)
                spawn.properties.combatPulse.attack(player)
                sendMessage(player, "...the bones object.")
            }

            else -> sendMessage(player, "...but there's nothing remotely valuable.")
        }
        if (RandomFunction.random(10) < 3) {
            impact(player, RandomFunction.random(14), HitsplatType.NORMAL)
        }
    }

    private fun removeSkeleton(
        obj: Scenery,
        spawn: NPC,
    ) {
        SceneryBuilder.remove(obj)
        GameWorld.Pulser.submit(
            object : Pulse(200) {
                override fun pulse(): Boolean {
                    if (spawn.isActive) {
                        spawn.clear()
                    }
                    SceneryBuilder.add(obj)
                    return true
                }
            },
        )
    }

    override fun fireEvent(
        identifier: String,
        vararg args: Any?,
    ): Any? = this

    override fun configure() {
        register(ZoneBorders(1723, 5296, 1831, 5394))
    }
}
