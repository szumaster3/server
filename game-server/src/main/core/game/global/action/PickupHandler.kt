package core.game.global.action

import content.data.GodType
import content.global.skill.runecrafting.RunePouch
import core.api.*
import core.game.dialogue.FaceAnim
import core.game.event.PickUpEvent
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.LogType
import core.game.node.entity.player.info.PlayerMonitor
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.system.config.GroundSpawnLoader
import core.game.world.GameWorld
import core.game.world.map.RegionManager
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Sounds

/**
 * The `PickupHandler` object manages the logic for picking up ground items by players.
 */
object PickupHandler {
    /**
     * Handles the action of a player picking up a ground item.
     *
     * @param player The player attempting to pick up the item.
     * @param item The ground item the player wants to pick up.
     * @return `true` if the item was successfully picked up or if the conditions were not met.
     */
    @JvmStatic
    fun take(
        player: Player,
        item: GroundItem,
    ): Boolean {
        // Check if the item's location is valid.
        if (item.location == null) {
            sendMessage(player, "Invalid ground item!")
            return true
        }

        // Check if the item still exists on the ground.
        if (!GroundItemManager.getItems().contains(item)) {
            sendMessage(player, "Too late - it's gone!")
            return true
        }

        // Check if the player recently attempted to take the item.
        if (getAttribute(player, "droppedItem:" + item.id, 0) > GameWorld.ticks) {
            return true
        }

        // Check if the item is private and not dropped by the player.
        if (item !is GroundSpawnLoader.GroundSpawn && item.isRemainPrivate && !item.droppedBy(player)) {
            sendMessage(player, "You can't take that item!")
            return true
        }

        // Create a new item from the ground item.
        val add = Item(item.id, item.amount, item.charge)

        // Check if the player has space in their inventory.
        if (!hasSpaceFor(player, add)) {
            sendMessage(player, "You don't have enough inventory space to hold that item.")
            return true
        }

        // Check if the player is allowed to take the item.
        if (!canTake(player, item, 0)) {
            return true
        }

        // If the item is active and the player can pick it up, add it to the inventory.
        if (item.isActive && player.inventory.add(add)) {
            // Log the item pickup if it was dropped by another player.
            if (item.dropper is Player && item.dropper!!.details.uid != player.details.uid) {
                PlayerMonitor.log(
                    item.dropper!!,
                    LogType.DROP_TRADE,
                    "${getItemName(item.id)} x${item.amount} picked up by ${player.name}.",
                )
            }

            // Check if the item location permits teleportation.
            if (!RegionManager.isTeleportPermitted(item.location)) {
                player.animate(Animation.create(Animations.OPEN_POH_WARDROBE_535))
            }

            // Destroy the ground item after being picked up.
            GroundItemManager.destroy(item)

            // Play a sound for the item pickup.
            playAudio(player, Sounds.PICK2_2582)

            // Dispatch the pickup event.
            player.dispatch(PickUpEvent(item.id))
        }
        return true
    }

    /**
     * Determines if a player is allowed to pick up a specific ground item.
     *
     * @param player The player attempting to pick up the item.
     * @param item The ground item being considered for pickup.
     * @param type The type of the item, used to determine if additional conditions apply.
     * @return `true` if the player can pick up the item, otherwise `false`.
     */
    @JvmStatic
    fun canTake(
        player: Player,
        item: GroundItem,
        type: Int,
    ): Boolean {
        // Check if the item was dropped by another player and if the player is restricted from picking it up.
        if (item.dropper != null && !item.droppedBy(player) && player.ironmanManager.checkRestriction()) {
            return false
        }

        // Check if the item is a special guild item (e.g., shot).
        if (item.id == Items.EIGHTEEN_LB_SHOT_8858 || item.id == Items.TWENTY_TWO_LB_SHOT_8859) {
            sendNPCDialogue(
                player,
                NPCs.REF_4300,
                "Hey! You can't take that, it's guild property. Take one from the pile.",
                FaceAnim.FURIOUS,
            )
            return false
        }

        // Check if the item is a sacred cape and if the player already owns one.
        if (GodType.forCape(item) != null) {
            if (GodType.hasAny(player)) {
                sendDialogueLines(
                    player,
                    "You may only possess one sacred cape at a time.",
                    "The conflicting powers of the capes drive them apart.",
                )
                return false
            }
        }

        // Check if the item is a Rune Pouch and if the player already has it.
        if (RunePouch.forItem(item) != null) {
            if (player.hasItem(item)) {
                sendDialogueLines(player, "A mystical force prevents you from picking up the pouch.")
                return false
            }
        }

        // If the item has a plugin, check if the plugin allows the player to pick it up.
        return if (item.hasItemPlugin()) {
            item.plugin!!.canPickUp(player, item, type)
        } else {
            true
        }
    }
}
