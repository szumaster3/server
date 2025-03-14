package content.region.misthalin.quest.surok.handlers

import core.api.removeItem
import core.api.sendDialogueLines
import core.api.sendMessage
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class FolderHandler : UseWithHandler(WhatLiesBelowListener.EMPTY_FOLDER, WhatLiesBelowListener.USED_FOLDER) {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        addHandler(WhatLiesBelowListener.RATS_PAPER, ITEM_TYPE, this)
        return this
    }

    override fun handle(event: NodeUsageEvent): Boolean {
        val player = event.player
        val paper = event.usedItem
        var folder = event.usedWith.asItem()

        if (removeItem(player, paper)) {
            if (folder.id == 11003) {
                player.inventory.replace(Item(WhatLiesBelowListener.USED_FOLDER), folder.slot)
                folder = player.inventory[folder.slot]
            }
            folder.charge += 1
            val remainingPages = 1005 - folder.charge

            sendMessage(player, "You add the page to the folder that Rat gave to you.")
            if (remainingPages <= 0) {
                player.inventory.replace(Item(WhatLiesBelowListener.FULL_FOLDER), folder.slot)
                sendMessage(
                    player,
                    "You add the last page to Rat's folder. You should take this back to Rat as soon as possible.",
                )
                sendDialogueLines(
                    player,
                    "You have added all the pages to the folder that Rat gave to you.",
                    "You should take this folder back to Rat.",
                )
            } else {
                sendMessage(player, "You need to find $remainingPages more page${if (remainingPages > 1) "s" else "."}")
            }
        }
        return true
    }
}
