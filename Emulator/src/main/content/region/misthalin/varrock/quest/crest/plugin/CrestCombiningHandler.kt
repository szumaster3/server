package content.region.misthalin.varrock.quest.crest.plugin

import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Items

@Initializable
class CrestCombiningHandler : UseWithHandler(Items.CREST_PART_779, Items.CREST_PART_780, Items.CREST_PART_781) {
    private val crestAvan: Item = Item(Items.CREST_PART_779)
    private val crestCaleb: Item = Item(Items.CREST_PART_780)
    private val crestJonathan: Item = Item(Items.CREST_PART_781)
    private val crestFull: Item = Item(Items.FAMILY_CREST_782)

    override fun newInstance(arg: Any?): Plugin<Any> {
        addHandler(Items.CREST_PART_779, ITEM_TYPE, this)
        addHandler(Items.CREST_PART_780, ITEM_TYPE, this)
        addHandler(Items.CREST_PART_781, ITEM_TYPE, this)
        return this
    }

    override fun handle(event: NodeUsageEvent?): Boolean {
        event ?: return false
        val used = event.used
        return when (used.id) {
            Items.CREST_PART_779 -> craftCrest(event.player, event)
            Items.CREST_PART_780 -> craftCrest(event.player, event)
            Items.CREST_PART_781 -> craftCrest(event.player, event)
            else -> false
        }
    }

    private fun craftCrest(
        player: Player,
        event: NodeUsageEvent,
    ): Boolean =
        when (event.usedWith.id) {
            Items.CREST_PART_779, Items.CREST_PART_780, Items.CREST_PART_781 -> {
                if (player.inventory.containItems(Items.CREST_PART_779, Items.CREST_PART_780, Items.CREST_PART_781)) {
                    player.inventory.remove(crestAvan)
                    player.inventory.remove(crestCaleb)
                    player.inventory.remove(crestJonathan)
                    player.inventory.add(crestFull)
                    true
                }
                false
            }

            else -> false
        }
}
