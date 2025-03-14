package content.global.skill.crafting.glassblowing.lamps

import core.api.log
import core.game.container.Container
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import core.tools.Log

class LightSourceExtinguisherListener : InteractionListener {
    override fun defineListeners() {
        fun Container.replace(
            item: Item,
            with: Item,
        ) {
            if (remove(item)) {
                add(with)
            }
        }

        on(IntType.ITEM, "extinguish") { player, node ->
            val lightSources = LightSources.forId(node.id)

            lightSources ?: return@on false.also {
                log(this::class.java, Log.WARN, "UNHANDLED EXTINGUISH OPTION: ID = ${node.id}")
            }

            player.inventory.replace(node.asItem(), Item(lightSources.fullId))
            return@on true
        }
    }
}
