package content.region.morytania.handlers.phasmatys

import core.api.quest.isQuestComplete
import core.api.removeItem
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Direction
import core.game.world.map.Location
import org.rs.consts.Items
import org.rs.consts.Quests

object EnergyBarrierUtils {
    fun passGate(
        player: Player,
        node: Node,
    ) {
        val force =
            node.location.equals(3659, 3508, 0) &&
                player.location.y < 3508 ||
                node.location.equals(3652, 3485, 0) &&
                player.location.x > 3652

        if (force || removeItem(player, Item(Items.ECTO_TOKEN_4278, 2))) {
            val direction = Direction.getLogicalDirection(player.location, node.location)
            var end = player.location.transform(direction, 2)

            if (player.location.y >= 3508) {
                end = node.location.transform(0, -1, 0)
            }
            if (node.location == Location(3652, 3485, 0)) {
                end = node.location.transform(if (player.location.x >= 3653) -1 else 1, 0, 0)
            }

            content.global.skill.agility.AgilityHandler
                .walk(player, -1, player.location, end, null, 0.0, null)
        }
    }

    fun passGateByTalk(
        player: Player,
        node: Node,
    ) {
        if (removeItem(player, Item(Items.ECTO_TOKEN_4278, 2))) {
            val direction = Direction.getLogicalDirection(player.location, node.location)
            var end = node.location.transform(direction, 2)

            if (player.location == Location(3661, 3509, 0)) {
                end = node.location.transform(-1, -1, 0)
            }
            if (player.location == Location(3658, 3509, 0)) {
                end = node.location.transform(1, -1, 0)
            }
            if (player.location == Location(3651, 3487, 0)) {
                end = node.location.transform(1, -1, 0)
            }
            if (player.location == Location(3651, 3484, 0)) {
                end = node.location.transform(1, 1, 0)
            }

            content.global.skill.agility.AgilityHandler
                .walk(player, -1, player.location, end, null, 0.0, null)
        }
    }

    fun passGateAfterQuest(
        player: Player,
        node: Node,
    ) {
        val force =
            node.location.equals(3659, 3508, 0) &&
                player.location.y < 3508 ||
                node.location.equals(3652, 3485, 0) &&
                player.location.x > 3652

        if (force || isQuestComplete(player, Quests.GHOSTS_AHOY)) {
            val direction = Direction.getLogicalDirection(player.location, node.location)
            var end = player.location.transform(direction, 2)

            if (player.location.y >= 3508) {
                end = node.location.transform(0, -1, 0)
            }
            if (node.location == Location(3652, 3485, 0)) {
                end = node.location.transform(if (player.location.x >= 3653) -1 else 1, 0, 0)
            }

            content.global.skill.agility.AgilityHandler
                .walk(player, -1, player.location, end, null, 0.0, null)
        }
    }
}
