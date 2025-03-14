package core.api.interaction

import content.data.consumables.Consumables
import content.global.skill.slayer.SlayerManager
import content.global.skill.slayer.Tasks
import core.api.findNPC
import core.cache.def.impl.NPCDefinition
import core.game.consumable.Consumable
import core.game.consumable.Potion
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.shops.Shops
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import org.rs.consts.NPCs
import java.util.regex.Pattern

/**
 * Retrieves the name of the NPC based on its id.
 *
 * @param id The NPC id.
 * @return The name of the NPC as a String.
 */
fun getNPCName(id: Int): String {
    return NPCDefinition.forId(id).name
}

/**
 * Decants items in a container, organizing them by dosage and creating new items with the correct dosage.
 * It removes the original items and adds the newly decanted items to the container.
 *
 * @param container The container to decant the items from.
 * @return A pair of lists where:
 *  - The first list contains the items to be removed from the container.
 *  - The second list contains the items to be added to the container.
 */
fun decantContainer(container: core.game.container.Container): Pair<List<Item>, List<Item>> {
    val doseCount = HashMap<Consumable, Int>()
    val toRemove = ArrayList<Item>()
    val toAdd = ArrayList<Item>()
    val doseRegex = Pattern.compile("(\\([0-9]\\))")

    for (item in container.toArray()) {
        if (item == null) continue
        val potionType = Consumables.getConsumableById(item.id)?.consumable as? Potion ?: continue
        val matcher = doseRegex.matcher(item.name)
        if (!matcher.find()) continue
        val dosage =
            matcher
                .group(1)
                .replace("(", "")
                .replace(")", "")
                .toIntOrNull() ?: continue
        doseCount[potionType] = (doseCount[potionType] ?: 0) + dosage
        toRemove.add(item)
    }

    for ((consumable, count) in doseCount) {
        val maxDose = consumable.ids.size
        val totalMaxDosePotions = count / maxDose
        val remainderDose = count % maxDose
        if (totalMaxDosePotions > 0) toAdd.add(Item(consumable.ids[0], totalMaxDosePotions))
        if (remainderDose > 0) toAdd.add(Item(consumable.ids[consumable.ids.size - remainderDose]))
    }

    val emptyVials = toRemove.size - toAdd.sumOf { it.amount }
    if (emptyVials > 0) toAdd.add(Item(229, emptyVials))
    return Pair<List<Item>, List<Item>>(toRemove, toAdd)
}

/**
 * Opens the shop associated with a given NPC for a player.
 *
 * @param player The player who is opening the shop.
 * @param npc The NPC whose shop should be opened.
 * @return True if the shop was successfully opened, false otherwise.
 */
fun openNpcShop(
    player: Player,
    npc: Int,
): Boolean {
    val shop = Shops.shopsByNpc[npc]

    if (shop != null) {
        shop.openFor(player)
        return true
    }

    return false
}

/**
 * Retrieves the Slayer Master assigned to the player.
 *
 * @param player The player whose Slayer Master is being retrieved.
 * @return The [NPC] representing the player's Slayer Master.
 */
fun getSlayerMaster(player: Player): NPC {
    return findNPC(SlayerManager.getInstance(player).master?.npc as Int) as NPC
}

/**
 * Retrieves the location of the player's assigned Slayer Master.
 *
 * @param player The player whose Slayer Master location is being retrieved.
 * @return A string representing the location of the Slayer Master.
 */
fun getSlayerMasterLocation(player: Player): String {
    return when (getSlayerMaster(player).id) {
        NPCs.CHAELDAR_1598 -> "Zanaris"
        NPCs.DURADEL_8275 -> "Shilo Village"
        NPCs.MAZCHNA_8274 -> "Canifis"
        NPCs.TURAEL_8273 -> "Taverley"
        NPCs.VANNAKA_1597 -> "Edgeville Dungeon"
        else -> "The Backrooms"
    }
}

/**
 * Retrieves the player's current Slayer task.
 *
 * @param player The player whose Slayer task is being retrieved.
 * @return The active [Tasks] assigned to the player, or null if no task is assigned.
 */
fun getSlayerTask(player: Player): Tasks? {
    return SlayerManager.getInstance(player).activeTask
}

/**
 * Checks if the player has an active Slayer task.
 *
 * @param player The player whose task status is being checked.
 * @return True if the player has an active Slayer task, otherwise false.
 */
fun hasSlayerTask(player: Player): Boolean {
    return SlayerManager.getInstance(player).hasTask()
}

/**
 * Retrieves the name of the player's current Slayer task.
 *
 * @param player The player whose Slayer task name is being retrieved.
 * @return A string representing the name of the Slayer task.
 */
fun getSlayerTaskName(player: Player): String {
    return SlayerManager.getInstance(player).taskName
}

/**
 * Retrieves the number of kills remaining for the player's current Slayer task.
 *
 * @param player The player whose remaining kills are being retrieved.
 * @return An int representing the number of kills left to complete the Slayer task.
 */
fun getSlayerTaskKillsRemaining(player: Player): Int {
    return SlayerManager.getInstance(player).amount
}

/**
 * Retrieves the task flags for the player's current Slayer task.
 *
 * @param player The player whose task flags are being retrieved.
 * @return An int representing the task flags.
 */
fun getSlayerTaskFlags(player: Player): Int {
    return SlayerManager.getInstance(player).flags.taskFlags
}

/**
 * Provides a tip or hint about the player's current Slayer task.
 *
 * @param player The player whose Slayer task tip is being retrieved.
 * @return An array of string containing the Slayer task tip.
 */
fun getSlayerTip(player: Player): Array<out String> {
    return if (hasSlayerTask(player)) {
        getSlayerTask(player)?.tip!!
    } else {
        arrayOf("You need something new to hunt.")
    }
}

/**
 * Transforms an NPC into another NPC for a specified duration before reverting back.
 *
 * @param npc The [NPC] to be transformed.
 * @param transformTo The id of the NPC to transform into.
 * @param restoreTicks The number of game ticks before the NPC reverts to its original form.
 */
fun transformNpc(
    npc: NPC,
    transformTo: Int,
    restoreTicks: Int,
) {
    npc.transform(transformTo)
    Pulser.submit(
        object : Pulse(restoreTicks) {
            override fun pulse(): Boolean {
                npc.reTransform()
                return true
            }
        },
    )
}

private class NPCInteractionAPI
