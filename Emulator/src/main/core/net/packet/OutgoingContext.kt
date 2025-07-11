package core.net.packet

import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.system.communication.ClanRepository
import core.game.world.map.Location
import core.game.world.map.Point
import core.game.world.map.RegionChunk
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics

/**
 * Base sealed class for outgoing contexts related to a player.
 */
sealed class OutgoingContext(override val player: Player, open val login: Boolean = false) : Context {
    data class PlayerContext(override val player: Player) : OutgoingContext(player)
    data class AccessMask(override val player: Player, val id: Int, val childId: Int, val interfaceId: Int, val offset: Int, val length: Int) : OutgoingContext(player)
    data class AnimateInterface(override val player: Player, val animationId: Int, val interfaceId: Int, val childId: Int) : OutgoingContext(player)
    data class AnimateObject(override val player: Player, val animation: Animation) : OutgoingContext(player)
    data class AreaPosition(override val player: Player, val location: Location, val offsetX: Int, val offsetY: Int) : OutgoingContext(player)
    data class BuildItem(override val player: Player, val item: Item, val oldAmount: Int = 0) : OutgoingContext(player)
    data class BuildScenery(override val player: Player, val scenery: Scenery) : OutgoingContext(player)
    data class SystemUpdate(override val player: Player, val time: Int) : OutgoingContext(player)
    data class WindowsPane(override val player: Player, val windowId: Int, val type: Int) : OutgoingContext(player)
    data class Varbit(override val player: Player, val varbitId: Int, val value: Int) : OutgoingContext(player)
    data class VarcUpdate(override val player: Player, val varcId: Int, val value: Int) : OutgoingContext(player)
    data class SkillContext(override val player: Player, val skillId: Int) : OutgoingContext(player)
    data class StringContext(override val player: Player, val string: String, val interfaceId: Int, val lineId: Int) : OutgoingContext(player)
    data class SceneGraph(override val player: Player, override val login: Boolean) : OutgoingContext(player, login)
    data class DynamicScene(override val player: Player, override val login: Boolean) : OutgoingContext(player, login)
    data class GrandExchange(override val player: Player, val idx: Byte, val state: Byte, val itemID: Short, val isSell: Boolean, val value: Int, val amt: Int, val completedAmt: Int, val totalCoinsExchanged: Int) : OutgoingContext(player)
    data class CSConfig(override val player: Player, val id: Int, val value: Int, val types: String, val parameters: Array<Any>) : OutgoingContext(player)
    data class Default(override val player: Player, val objects: Array<Any?>) : OutgoingContext(player)
    data class Music(override val player: Player, val musicId: Int, var secondary: Boolean = false) : OutgoingContext(player)
    data class ClearChunk(override val player: Player, val chunk: RegionChunk) : OutgoingContext(player)
    data class Config(override var player: Player, val id: Int, val value: Int, val cs2: Boolean = false) : OutgoingContext(player)
    data class GameMessage(override val player: Player, val message: String) : OutgoingContext(player)
    data class IntegerContext(override val player: Player, var integer: Int) : OutgoingContext(player)
    data class InteractionOptionContext(override val player: Player, val index: Int, val name: String, val remove: Boolean = false) : OutgoingContext(player)
    data class InterfaceConfigContext(override val player: Player, val interfaceId: Int, val childId: Int, val hide: Boolean) : OutgoingContext(player)
    data class ChildPosition(override val player: Player, val interfaceId: Int, val childId: Int, val position: Point) : OutgoingContext(player)
    data class Clan(override val player: Player, val clan: ClanRepository, val leave: Boolean) : OutgoingContext(player)
    data class MinimapState(override val player: Player, val state: Int) : OutgoingContext(player)
    data class RunScript(override var player: Player, val id: Int, val string: String, val objects: Array<Any>) : OutgoingContext(player)
    data class WalkOption(override var player: Player, val option: String) : OutgoingContext(player)
    data class LocationContext(override val player: Player, val location: Location, val teleport: Boolean) : OutgoingContext(player)
    data class Container(override val player: Player, val interfaceId: Int, val childId: Int, val containerId: Int, var items: Array<Item>? = null, var ids: IntArray? = null, var length: Int = 0, var split: Boolean = false, var slots: IntArray? = null, var clear: Boolean = false) : OutgoingContext(player) {
        constructor(player: Player, interfaceId: Int, childId: Int, clear: Boolean) : this(player, interfaceId, childId, containerId = 0, items = null, ids = null, length = 1, split = false, slots = null, clear = clear)
        constructor(player: Player, interfaceId: Int, childId: Int, containerId: Int, container: core.game.container.Container, split: Boolean) : this(player, interfaceId, childId, containerId, items = container.toArray(), ids = null, length = container.toArray().size, split = split, slots = null, clear = false)
        constructor(player: Player, interfaceId: Int, childId: Int, containerId: Int, items: Array<Item>, split: Boolean) : this(player, interfaceId, childId, containerId, items = items, ids = null, length = items.size, split = split, slots = null, clear = false)
        constructor(player: Player, interfaceId: Int, childId: Int, containerId: Int, items: Array<Item>, length: Int, split: Boolean) : this(player, interfaceId, childId, containerId, items = items, ids = null, length = length, split = split, slots = null, clear = false)
        constructor(player: Player, interfaceId: Int, childId: Int, containerId: Int, ids: IntArray) : this(player, interfaceId, childId, containerId, items = null, ids = ids, length = ids.size, split = false, slots = null, clear = false)
        constructor(player: Player, interfaceId: Int, childId: Int, containerId: Int, items: Array<Item>, split: Boolean, vararg slots: Int) : this(player, interfaceId, childId, containerId, items = items, ids = null, length = items.size, split = split, slots = if (slots.isNotEmpty()) slots else null, clear = false)
    }
    data class DisplayModel(override val player: Player, val type: ModelType = ModelType.PLAYER, val nodeId: Int = -1, var amount: Int = 0, val interfaceId: Int, val childId: Int, var zoom: Int = 0, ) : OutgoingContext(player) {
        enum class ModelType { PLAYER, NPC, ITEM, MODEL }
    }
    data class HintIcon(override val player: Player, val slot: Int, var arrowId: Int, var targetType: Int, val modelId: Int, val height: Int = 0, val index: Int, val location: Location?) : OutgoingContext(player) {
        constructor(player: Player, slot: Int, arrowId: Int, target: Node, modelId: Int) : this(player = player, slot = slot, arrowId = arrowId, targetType = targetType(target), modelId = modelId, height = 0, index = if (target is Entity) target.index else -1, location = if (target is Entity) null else target.location)
        constructor(player: Player, slot: Int, arrowId: Int, targetType: Int, target: Node, modelId: Int, height: Int = 0) : this(player = player, slot = slot, arrowId = arrowId, targetType = targetType, modelId = modelId, height = height, index = if (target is Entity) target.index else -1, location = if (target is Entity) null else target.location)
        companion object {
            private fun targetType(target: Node): Int {
                var type = 2
                if (target is Entity) {
                    type = if (target is Player) 10 else 1
                }
                return type
            }
        }
    }
    data class InterfaceContext(override val player: Player, val windowId: Int, val componentId: Int, val interfaceId: Int, val walkable: Boolean) : OutgoingContext(player) {
        fun transform(player: Player, id: Int): InterfaceContext =
            copy(player = player, interfaceId = id)
    }
    data class MessageContext(override val player: Player, val other: String, val chatIcon: Int, val opcode: Int, val message: String) : OutgoingContext(player) {
        companion object {
            const val SEND_MESSAGE = 71
            const val RECEIVE_MESSAGE = 0
            const val CLAN_MESSAGE = 54
        }
    }
    data class PositionedGraphic(override val player: Player, val graphic: Graphics, val location: Location, val offsetX: Int, val offsetY: Int, ) : OutgoingContext(player) {
        val sceneX: Int = location.getSceneX(player.playerFlags.lastSceneGraph)
        val sceneY: Int = location.getSceneY(player.playerFlags.lastSceneGraph)
    }
    data class Camera(override val player: Player, val type: CameraType, val x: Int, val y: Int, val height: Int, val speed: Int, val zoomSpeed: Int) : OutgoingContext(player) {
        fun transform(player: Player, xOffset: Int, yOffset: Int): Camera = copy(player = player, x = this.x + xOffset, y = this.y + yOffset)
        fun transform(heightOffset: Int): Camera = copy(height = this.height + heightOffset)
    }

    enum class CameraType(val opcode: Int) {
        POSITION(154),
        ROTATION(125),
        SET(187),
        SHAKE(27),
        RESET(24);
    }

    class Contact : OutgoingContext {
        companion object {
            const val UPDATE_STATE_TYPE = 0
            const val UPDATE_FRIEND_TYPE = 1
            const val IGNORE_LIST_TYPE = 2
        }

        override val player: Player
        var type: Int
        var name: String? = null
        var worldId: Int = 0

        constructor(player: Player, type: Int) : super(player) {
            this.player = player
            this.type = type
        }

        constructor(player: Player, name: String, worldId: Int) : super(player) {
            this.player = player
            this.name = name
            this.worldId = worldId
            this.type = UPDATE_FRIEND_TYPE
        }

        fun isOnline(): Boolean = worldId > 0
    }
}
