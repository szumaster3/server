package content.region.asgarnia.quest.fortress.handlers

import core.api.allInEquipment
import core.api.sendMessage
import core.cache.def.impl.ItemDefinition
import core.cache.def.impl.SceneryDefinition
import core.game.global.action.DoorActionHandler.handleAutowalkDoor
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.world.repository.Repository.findNPC
import core.plugin.Plugin
import org.rs.consts.Items

class BlackKnightsFortressPlugin : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        registerHandlers()
        return this
    }

    private fun registerHandlers() {
        val itemHandlers =
            mapOf(
                74 to "option:open",
                73 to "option:open",
                2337 to "option:open",
                2338 to "option:open",
                2341 to "option:push",
                17148 to "option:climb-up",
                17149 to "option:climb-down",
                17160 to "option:climb-down",
            )
        itemHandlers.forEach { (id, option) ->
            ItemDefinition.forId(id).handlers[option] = this
            SceneryDefinition.forId(id).handlers[option] = this
        }
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        val id = if (node is Item) node.getId() else node.id
        when (id) {
            2341 -> handleSecretPassage(player, node)
            2338 -> handleBigTableRoom(player, node)
            2337 -> handleDoor(player, node)
            74, 73 -> handleLargeDoor(player, node)
        }
        return true
    }

    private fun handleSecretPassage(
        player: Player,
        node: Node,
    ) {
        sendMessage(player, "You push against the wall. You find a secret passage.")
        handleAutowalkDoor(player, node as Scenery)
    }

    private fun handleBigTableRoom(
        player: Player,
        node: Node,
    ) {
        if (player.location.x > 3019) {
            handleAutowalkDoor(player, node as Scenery)
        } else {
            player.dialogueInterpreter.open(4605, findNPC(4605), true, true)
        }
    }

    private fun handleDoor(
        player: Player,
        node: Node,
    ) {
        when (player.location.y) {
            3514 ->
                if (allInEquipment(player, Items.BRONZE_MED_HELM_1139, Items.IRON_CHAINBODY_1101)) {
                    handleAutowalkDoor(player, node as Scenery)
                } else {
                    player.dialogueInterpreter.open(4605, findNPC(4604), true)
                }

            3515 -> handleAutowalkDoor(player, node as Scenery)
        }
    }

    private fun handleLargeDoor(
        player: Player,
        node: Node,
    ) {
        if (player.location.x == 3008) {
            handleAutowalkDoor(player, node as Scenery)
        } else {
            sendMessage(player, "You can't open this door.")
        }
    }

    override fun isWalk(): Boolean {
        return false
    }

    override fun isWalk(
        player: Player,
        node: Node,
    ): Boolean {
        return node !is Item
    }
}
