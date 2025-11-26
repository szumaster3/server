package content.global.plugins.`object`

import core.api.animate
import core.api.playAudio
import core.api.replaceScenery
import core.api.sendMessage
import core.cache.def.impl.SceneryDefinition
import core.game.global.action.DoorActionHandler
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Animations
import shared.consts.Sounds

/**
 * Plugin used for handling the opening and closing
 * of (double) door & gates & fences.
 *
 * @author Emperor
 */
@Initializable
class ObjectManagerPlugin : OptionHandler() {

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.setOptionHandler("open", this)
        SceneryDefinition.setOptionHandler("close", this)
        SceneryDefinition.setOptionHandler("shut", this)
        SceneryDefinition.setOptionHandler("go-through", this)
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        val scenery = node as Scenery

        if (scenery.type != 9 && !player.location.equals(node.location) && !player.location.isNextTo(scenery) && scenery.name.contains("cupboard", ignoreCase = true)) {
            return true
        }

        val name = scenery.name.lowercase()

        if (name.contains("drawers") || name.contains("wardrobe") || name.contains("cupboard")) {
            when (option) {
                "open" -> {
                    when {
                        name.contains("drawers") -> {
                            player.animate(Animation(546))
                            playAudio(player, Sounds.DRAWER_OPEN_64)
                            player.packetDispatch.sendMessage("You open the drawers.")
                        }
                        name.contains("wardrobe") -> {
                            player.animate(Animation(Animations.OPEN_WARDROBE_545))
                            playAudio(player, Sounds.WARDROBE_OPEN_96)
                            player.packetDispatch.sendMessage("You open the wardrobe.")
                        }
                        name.contains("cupboard") -> {
                            if(scenery.id >= 33502) {
                                player.animate(Animation(542))
                            } else {
                                player.animate(Animation(535))
                            }
                            playAudio(player, Sounds.CUPBOARD_OPEN_58)
                            player.packetDispatch.sendMessage("You open the cupboard.")
                        }
                    }
                    replaceScenery(node, node.id + 1, -1)
                    return true
                }

                "go-through" -> {
                    if (scenery.isActive) {
                        SceneryBuilder.replace(node, node.transform(node.getId() + 1), 80)
                    }
                    return true
                }
                "close", "shut" -> {
                    when {
                        name.contains("drawers") -> {
                            player.animate(Animation(547))
                            animate(player, 547, false)
                            playAudio(player, Sounds.DRAWER_CLOSE_63)
                            player.packetDispatch.sendMessage("You close the drawers.")
                        }
                        name.contains("wardrobe") -> {
                            player.animate(Animation(546))
                            playAudio(player, Sounds.WARDROBE_CLOSE_95)
                            player.packetDispatch.sendMessage("You close the wardrobe.")
                        }
                        name.contains("cupboard") -> {
                            if(scenery.id >= 33502) {
                                player.animate(Animation(541))
                            } else {
                                player.animate(Animation(536))
                            }
                            playAudio(player, Sounds.CUPBOARD_CLOSE_57)
                            player.packetDispatch.sendMessage("You close the cupboard.")
                        }
                    }
                    replaceScenery(node, node.id - 1, -1)
                    return true
                }
            }
            return true
        }

        if (name.contains("trapdoor") || name.contains("trap door")) {
            val destination = scenery.location.transform(0, 6400, 0)
            if (!RegionManager.isTeleportPermitted(destination)) {
                sendMessage(player,"This doesn't seem to go anywhere.")
                return true
            }
            player.properties.teleportLocation = destination
            return true
        }

        if (!(name.contains("door") || name.contains("gate") || name.contains("fence") || name.contains("wall") || name.contains("exit") || name.contains("entrance"))) {
            return false
        }

        DoorActionHandler.handleDoor(player, scenery)
        return true
    }

    override fun getDestination(n: Node, node: Node): Location? {
        val o = node as Scenery
        return if (o.type < 4 || o.type == 9) {
            DoorActionHandler.getDestination(n as Player, o)
        } else {
            null
        }
    }
}
