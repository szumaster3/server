package content.global.plugin.item.withobject

import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Items

@Initializable
class GWDKnightSceneryPlugin : OptionHandler() {

    private val noteIds = listOf(
        Items.KNIGHTS_NOTES_11734,
        Items.KNIGHTS_NOTES_11735
    )

    private val sceneryIds = listOf(26306, 26446)

    override fun newInstance(arg: Any?): Plugin<Any>? {
        sceneryIds.forEach { id ->
            SceneryDefinition.forId(id).handlers["option:search"] = this
        }
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        val hasNotes = noteIds.any { id -> inInventory(player, id) || inBank(player, id) }

        if (hasNotes) {
            sendMessage(player, "You find nothing of value on the knight.")
        } else {
            sendItemDialogue(player, Items.KNIGHTS_NOTES_11734, "You find some handwritten notes on the knight.")
            addItemOrBank(player, Items.KNIGHTS_NOTES_11734)
        }
        return true
    }

}