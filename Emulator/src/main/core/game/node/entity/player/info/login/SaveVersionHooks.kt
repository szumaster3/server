package core.game.node.entity.player.info.login

import content.data.GameAttributes
import core.ServerConstants
import core.api.*
import core.api.quest.getQuestStage
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs.consts.Items
import org.rs.consts.Quests

class SaveVersionHooks : LoginListener {
    // Override the login method to handle player login events
    override fun login(player: Player) {
        // Check if the player's version is less than the current save file version
        if (player.version < ServerConstants.CURRENT_SAVEFILE_VERSION) {
            // Send a message to the player about migrating their save file version
            sendMessage(
                player,
                "<col=CC6600>Migrating save file version ${player.version} to current save file version ${ServerConstants.CURRENT_SAVEFILE_VERSION}.</col>",
            )

            // Check if the player's version is less than 1
            if (player.version < 1) {
                // Initialize counters for hoods and capes
                var hasHoods = 0
                var hasCapes = 0
                // Define the search space for inventory and bank containers
                val searchSpace = arrayOf(player.inventory, player.bankPrimary, player.bankSecondary)
                // Iterate through each container in the search space
                for (container in searchSpace) {
                    // Count the number of crafting hoods in the container
                    for (hood in container.getAll(Item(Items.CRAFTING_HOOD_9782))) {
                        hasHoods += hood.amount
                    }
                    // Count the number of crafting capes in the container
                    for (id in arrayOf(Items.CRAFTING_CAPE_9780, Items.CRAFTING_CAPET_9781)) {
                        for (cape in container.getAll(Item(id))) {
                            hasCapes += cape.amount
                        }
                    }
                }
                // Calculate the number of hoods needed based on the capes owned
                val need = hasCapes - hasHoods
                // If the player needs more hoods, send a message and add them to the player's inventory
                if (need > 0) {
                    sendMessage(
                        player,
                        "<col=CC6600>You are being given $need crafting hood(s), because we think you bought $need crafting cape(s) when the hoods were still unobtainable.</col>",
                    )
                    addItemOrBank(player, Items.CRAFTING_HOOD_9782, need)
                }

                // Unlock music for players who have progressed in the quest "What Lies Below"
                if (getQuestStage(player, Quests.WHAT_LIES_BELOW) > 70) {
                    player.musicPlayer.unlock(250, false)
                }

                // Migrate old location attributes to the new format
                for (
                old in arrayOf(
                    "/save:drilldemon:original-loc",
                    "/save:evilbob:prevlocation",
                    "/save:freakyf:location",
                    "supexam:loc",
                )
                ) {
                    // Retrieve the old location attribute for the player
                    val oldloc = player.getAttribute(old, player.location)
                    // If the old location is different from the current location, set the new attribute
                    if (oldloc != player.location) {
                        player.setAttribute("/save:original-loc", oldloc)
                    }
                    // Remove the old location attribute
                    player.removeAttribute(old)
                }

                // If the tutorial is complete, set a specific variable for the player
                if (getAttribute(player, GameAttributes.TUTORIAL_COMPLETE, false)) {
                    setVarp(player, 281, 1000, true)
                }
            }

            // Update the player's version to the current save file version
            player.version = ServerConstants.CURRENT_SAVEFILE_VERSION
        }
    }
}
