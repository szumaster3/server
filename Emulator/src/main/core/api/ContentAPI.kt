package core.api

import com.moandjiezana.toml.Toml
import content.data.God
import core.ServerConstants
import core.api.item.itemDefinition
import core.api.movement.finishedMoving
import core.api.utils.PlayerCamera
import core.api.utils.PlayerStatsCounter
import core.api.utils.Vector
import core.cache.def.impl.AnimationDefinition
import core.cache.def.impl.ItemDefinition
import core.cache.def.impl.SceneryDefinition
import core.cache.def.impl.VarbitDefinition
import core.game.activity.Cutscene
import core.game.component.Component
import core.game.container.impl.EquipmentContainer
import core.game.dialogue.*
import core.game.dialogue.DialogueInterpreter.getDialogueKey
import core.game.interaction.Clocks
import core.game.interaction.InteractionListeners
import core.game.interaction.QueueStrength
import core.game.interaction.QueuedScript
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.impl.Animator
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.LogType
import core.game.node.entity.player.info.PlayerMonitor
import core.game.node.entity.player.link.HintIconManager
import core.game.node.entity.player.link.IronmanMode
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.player.link.audio.Audio
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.player.link.emote.Emotes
import core.game.node.entity.player.link.prayer.PrayerType
import core.game.node.entity.player.link.quest.QPCumulative
import core.game.node.entity.player.link.quest.QPReq
import core.game.node.entity.player.link.quest.QuestReq
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.config.ItemConfigParser
import core.game.system.config.ServerConfigParser
import core.game.system.task.Pulse
import core.game.system.timer.RSTimer
import core.game.system.timer.TimerRegistry
import core.game.world.GameWorld
import core.game.world.GameWorld.Pulser
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.map.RegionManager.getRegionChunk
import core.game.world.map.path.Pathfinder
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.game.world.repository.Repository
import core.game.world.update.flag.EntityFlag
import core.game.world.update.flag.chunk.AnimateSceneryUpdateFlag
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.ForceMoveCtx
import core.game.world.update.flag.context.Graphics
import core.net.packet.PacketRepository
import core.net.packet.context.DefaultContext
import core.net.packet.context.MusicContext
import core.net.packet.out.AudioPacket
import core.net.packet.out.MusicPacket
import core.tools.Log
import core.tools.SystemLogger
import core.tools.colorize
import core.tools.cyclesToTicks
import org.rs.consts.Items
import org.rs.consts.Sounds
import java.io.PrintWriter
import java.io.StringWriter
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

/**
 * Checks if the player has a level in the specified skill greater than or equal to the given level.
 *
 * @param player The player to check.
 * @param skill The skill to check.
 * @param level The required level to check against.
 * @return True if the player has the level or greater in the specified skill, false otherwise.
 */
fun hasLevelDyn(
    player: Player,
    skill: Int,
    level: Int,
): Boolean {
    return player.skills.getLevel(skill) >= level
}

/**
 * Checks if the player has a static level in the specified skill greater than or equal to the given level.
 *
 * @param player The player to check.
 * @param skill The skill to check.
 * @param level The required level to check against.
 * @return True if the player has the static level or greater in the specified skill, false otherwise.
 */
fun hasLevelStat(
    player: Player,
    skill: Int,
    level: Int,
): Boolean {
    return player.skills.getStaticLevel(skill) >= level
}

/**
 * Returns the amount of a specific item in the player's inventory.
 *
 * @param player The player whose inventory is being checked.
 * @param id The ID of the item to check.
 * @return The amount of the specified item in the player's inventory.
 */
fun amountInInventory(
    player: Player,
    id: Int,
): Int {
    return player.inventory.getAmount(id)
}

/**
 * Returns the total amount of a specific item in the player's bank, including the secondary bank if requested.
 *
 * @param player The player whose bank is being checked.
 * @param id The ID of the item to check.
 * @param includeSecondary Whether or not to include the secondary bank in the check. Defaults to true.
 * @return The total amount of the specified item in the player's bank.
 */
fun amountInBank(
    player: Player,
    id: Int,
    includeSecondary: Boolean = true,
): Int {
    return getAmountInBank(player, id) + if (includeSecondary) getAmountInBank(player, id, true) else 0
}

/**
 * Returns the amount of a specific item in the player's bank, from either the primary or secondary bank.
 *
 * @param player The player whose bank is being checked.
 * @param id The ID of the item to check.
 * @param secondary Whether to check the secondary bank (default is false for the primary bank).
 * @return The amount of the specified item in the player's bank.
 */
private fun getAmountInBank(
    player: Player,
    id: Int,
    secondary: Boolean = false,
): Int {
    val bank = if (secondary) player.bankSecondary.toArray() else player.bankPrimary.toArray()
    bank.forEach { if (it?.id == id) return it.amount }
    return 0
}

/**
 * Returns the amount of a specific item in the player's equipment.
 *
 * @param player The player whose equipment is being checked.
 * @param id The ID of the item to check.
 * @return The amount of the specified item in the player's equipment.
 */
fun amountInEquipment(
    player: Player,
    id: Int,
): Int {
    val slot = itemDefinition(id).getConfiguration(ItemConfigParser.EQUIP_SLOT, -1)
    val equipped = player.equipment[slot] ?: return 0
    return if (equipped.id == id) equipped.amount else 0
}

/**
 * Checks if the player has a specific amount of an item in their inventory.
 *
 * @param player The player whose inventory is being checked.
 * @param id The ID of the item to check.
 * @param amount The required amount of the item.
 * @return True if the player has the specified amount or more of the item in their inventory, false otherwise.
 */
fun inInventory(
    player: Player,
    id: Int,
    amount: Int = 1,
): Boolean {
    return player.inventory.contains(id, amount)
}

/**
 * Checks if the player has a specific amount of an item in their bank.
 *
 * @param player The player whose bank is being checked.
 * @param id The ID of the item to check.
 * @param amount The required amount of the item.
 * @return True if the player has the specified amount or more of the item in their bank, false otherwise.
 */
fun inBank(
    player: Player,
    id: Int,
    amount: Int = 1,
): Boolean {
    return amountInBank(player, id) >= amount
}

/**
 * Checks if the player has a specific amount of an item in their equipment.
 *
 * @param player The player whose equipment is being checked.
 * @param id The ID of the item to check.
 * @param amount The required amount of the item.
 * @return True if the player has the specified amount or more of the item in their equipment, false otherwise.
 */
fun inEquipment(
    player: Player,
    id: Int,
    amount: Int = 1,
): Boolean {
    return amountInEquipment(player, id) >= amount
}

/**
 * Checks if the player has a specific amount of an item in their equipment or inventory.
 *
 * @param player The player whose equipment and inventory are being checked.
 * @param id The ID of the item to check.
 * @param amount The required amount of the item.
 * @return True if the player has the specified amount or more of the item in either their equipment or inventory.
 */
fun inEquipmentOrInventory(
    player: Player,
    id: Int,
    amount: Int = 1,
): Boolean {
    return inEquipment(player, id, amount) || inInventory(player, id, amount)
}

/**
 * Checks if the player has all specified items in their equipment.
 *
 * @param player The player whose equipment is being checked.
 * @param ids The IDs of the items to check.
 * @return True if the player has all the specified items in their equipment, false otherwise.
 */
fun allInEquipment(
    player: Player,
    vararg ids: Int,
): Boolean {
    return ids.all { id -> inEquipment(player, id) }
}

/**
 * Checks if the player has any of the specified items in their equipment.
 *
 * @param player The player whose equipment is being checked.
 * @param ids The IDs of the items to check.
 * @return True if the player has at least one of the specified items in their equipment, false otherwise.
 */
fun anyInEquipment(
    player: Player,
    vararg ids: Int,
): Boolean {
    return ids.any { id -> inEquipment(player, id) }
}

/**
 * Checks if the player has any of the specified items in their inventory.
 *
 * @param player The player whose inventory is being checked.
 * @param ids The IDs of the items to check.
 * @return True if the player has at least one of the specified items in their inventory, false otherwise.
 */
fun anyInInventory(
    player: Player,
    vararg ids: Int,
): Boolean {
    return ids.any { id -> inInventory(player, id) }
}

/**
 * Returns an item from the player's equipment at a specific slot.
 *
 * @param player The player whose equipment is being checked.
 * @param slot The equipment slot to check.
 * @return The item in the specified equipment slot, or null if the slot is empty.
 */
fun getItemFromEquipment(
    player: Player,
    slot: EquipmentSlot,
): Item? {
    return player.equipment.get(slot.ordinal)
}

/**
 * Represents an item in a container, with methods to remove or check its existence.
 *
 * @param container The container holding the item.
 * @param itemId The ID of the item in the container.
 */
class ContainerisedItem(
    val container: core.game.container.Container?,
    val itemId: Int,
) {
    /**
     * Removes the item from the container.
     *
     * @return True if the item was removed successfully, false otherwise.
     */
    fun remove(): Boolean {
        return this.container?.remove(this.itemId.asItem()) ?: false
    }

    /**
     * Checks if the item exists in the container.
     *
     * @return True if the item exists in the container, false otherwise.
     */
    fun exists(): Boolean {
        return this.container != null && this.itemId > -1
    }
}

/**
 * Checks if the player has any of the specified items in their inventory, equipment, or bank.
 *
 * @param player The player to check.
 * @param ids The IDs of the items to search for.
 * @return A [ContainerisedItem] representing the item found, or one with a null container if none were found.
 */
fun hasAnItem(
    player: Player,
    vararg ids: Int,
): ContainerisedItem {
    for (searchSpace in arrayOf(player.inventory, player.equipment, player.bankPrimary, player.bankSecondary)) {
        for (id in ids) {
            if (searchSpace.containItems(id)) {
                return ContainerisedItem(searchSpace, id)
            }
        }
    }
    return ContainerisedItem(null, -1)
}

/**
 * Checks if the player has any of the valid god items equipped.
 *
 * @param player The player to check.
 * @param god The god to check against.
 * @return True if the player has any of the valid god items equipped, false otherwise.
 */
fun hasGodItem(
    player: Player,
    god: God,
): Boolean {
    god.validItems.forEach { if (inEquipment(player, it)) return true }
    return false
}

/**
 * Determines whether the player should remove certain items, such as the Ring of Wealth.
 *
 * @param player The player to check.
 * @return True if the player should remove certain items, false otherwise.
 */
fun shouldRemoveNothings(player: Player): Boolean {
    val ring = getItemFromEquipment(player, EquipmentSlot.RING)
    return ring != null && ring.id == Items.RING_OF_WEALTH_2572
}

/**
 * Removes an item from the player's specified container.
 *
 * @param player The player removing the item.
 * @param item The item to remove.
 * @param container The container from which to remove the item (defaults to inventory).
 * @return True if the item was removed successfully, false otherwise.
 */
fun <T> removeItem(
    player: Player,
    item: T,
    container: Container = Container.INVENTORY,
): Boolean {
    item ?: return false
    val it =
        when (item) {
            is Item -> item
            is Int -> Item(item)
            else -> throw IllegalStateException("Invalid value passed for item")
        }

    return when (container) {
        Container.INVENTORY -> player.inventory.remove(it)
        Container.BANK -> player.bank.remove(it) || player.bankSecondary.remove(it)
        Container.EQUIPMENT -> player.equipment.remove(it)
    }
}

/**
 * Adds an item to the player's specified container.
 *
 * @param player The player receiving the item.
 * @param id The ID of the item to add.
 * @param amount The amount of the item to add (defaults to 1).
 * @param container The container to add the item to (defaults to inventory).
 * @return True if the item was added successfully, false otherwise.
 */
fun addItem(
    player: Player,
    id: Int,
    amount: Int = 1,
    container: Container = Container.INVENTORY,
): Boolean {
    val cont =
        when (container) {
            Container.INVENTORY -> player.inventory
            Container.BANK -> player.bank
            Container.EQUIPMENT -> player.equipment
        }

    return cont.add(Item(id, amount))
}

/**
 * Attempts to add an item to the player's inventory or bank, and places it on the ground if both are full.
 *
 * @param player The player receiving the item.
 * @param id The ID of the item to add.
 * @param amount The amount of the item to add (defaults to 1).
 */
fun addItemOrBank(
    player: Player,
    id: Int,
    amount: Int = 1,
) {
    val item = Item(id, amount)
    if (!player.inventory.add(item)) {
        if (player.bankPrimary.add(item)) {
            sendMessage(player, colorize("%RThe ${item.name} has been sent to your bank."))
        } else if (player.bankSecondary.add(item)) {
            sendMessage(player, colorize("%RThe ${item.name} has been sent to your secondary bank."))
        } else {
            GroundItemManager.create(item, player)
            sendMessage(
                player,
                colorize(
                    "%RAs your inventory and bank account(s) are all full, the ${item.name} has been placed on the ground under your feet. Don't forget to grab it. (Also consider cleaning out some stuff, maybe? I mean, Jesus!)",
                ),
            )
        }
    }
}

/**
 * Replaces an item in a specified slot of the player's inventory, equipment, or bank, and logs a dupe attempt if necessary.
 *
 * @param player The player whose item is being replaced.
 * @param slot The slot to replace the item in.
 * @param item The new item to add.
 * @param currentItem The current item to replace.
 * @param container The container to perform the replacement in (defaults to inventory).
 * @return The replaced item, or null if the replacement failed.
 */
fun replaceSlot(
    player: Player,
    slot: Int,
    item: Item,
    currentItem: Item? = null,
    container: Container = Container.INVENTORY,
): Item? {
    val cont =
        when (container) {
            Container.INVENTORY -> player.inventory
            Container.EQUIPMENT -> player.equipment
            Container.BANK -> player.bank
        }

    if (item.id == 65535 || item.amount <= 0) {
        return cont.replace(null, slot)
    }

    if (currentItem == null) {
        return cont.replace(item, slot)
    }

    if (cont.remove(currentItem, slot, true)) {
        return cont.replace(item, slot)
    }

    PlayerMonitor.log(
        player,
        LogType.DUPE_ALERT,
        "Potential slot-replacement-based dupe attempt, slot: $slot, item: $item",
    )
    val other =
        when (container) {
            Container.INVENTORY -> Container.EQUIPMENT
            else -> Container.INVENTORY
        }
    if (removeItem(player, currentItem, other)) return cont.replace(item, slot)
    return null
}

/**
 * Attempts to add an item to the player's inventory or drops it if there is no room.
 *
 * @param player The player receiving the item.
 * @param id The ID of the item to add.
 * @param amount The amount of the item to add (defaults to 1).
 */
fun addItemOrDrop(
    player: Player,
    id: Int,
    amount: Int = 1,
) {
    val item = Item(id, amount)
    if (amount == 1 || item.definition.isStackable()) {
        if (!player.inventory.add(item)) GroundItemManager.create(item, player)
    } else {
        val singleItem = Item(id, 1)
        for (i in 0 until amount) {
            if (!player.inventory.add(singleItem)) GroundItemManager.create(singleItem, player)
        }
    }
}

/**
 * Makes the NPC invisible and plays a graphics effect, then clears the NPC after a few ticks.
 *
 * @param npc The NPC to make invisible and clear.
 */
fun poofClear(npc: NPC) {
    submitWorldPulse(
        object : Pulse() {
            var counter = 0

            override fun pulse(): Boolean {
                when (counter++) {
                    2 -> {
                        npc.isInvisible = true
                        Graphics.send(
                            Graphics(86),
                            npc.location,
                        )
                    }

                    3 -> npc.clear().also { return true }
                }
                return false
            }
        },
    )
}

/**
 * Returns the number of free slots in the player's inventory.
 *
 * @param player The player whose inventory is being checked.
 * @return The number of free slots in the player's inventory.
 */
fun freeSlots(player: Player): Int {
    return player.inventory.freeSlots()
}

/**
 * Returns an animation corresponding to the given ID.
 *
 * @param id The ID of the animation.
 * @return The animation object corresponding to the specified ID.
 */
fun getAnimation(id: Int): Animation {
    return Animation(id)
}

/**
 * Returns an animation corresponding to the given ID, with priority.
 *
 * @param id The ID of the animation.
 * @param priority The priority level of the animation.
 * @return The animation object corresponding to the specified ID with priority.
 */
fun getAnimationWithPriority(
    id: Int,
    priority: Animator.Priority,
): Animation {
    return Animation(id, Animator.Priority.values()[priority.ordinal])
}

/**
 * Resets the player's animation to a neutral state.
 *
 * @param player The player whose animation is being reset.
 */
fun resetAnimator(player: Player) {
    player.animator.animate(Animation(-1, Animator.Priority.VERY_HIGH))
}

/**
 * Returns the duration of an animation in ticks.
 *
 * @param animation The animation object.
 * @return The duration of the animation in ticks.
 */
fun animationDuration(animation: Animation): Int {
    return animation.definition.durationTicks
}

/**
 * Returns the number of cycles for a specific animation.
 *
 * @param animation The ID of the animation.
 * @return The number of cycles the animation takes.
 */
fun animationCycles(animation: Int): Int {
    val def = AnimationDefinition.forId(animation)
    return def!!.cycles
}

/**
 * Rewards the player with experience in a specified skill.
 *
 * @param player The player to reward.
 * @param skill The skill to reward experience in.
 * @param amount The amount of experience to reward.
 */
fun rewardXP(
    player: Player,
    skill: Int,
    amount: Double,
) {
    player.skills.addExperience(skill, amount)
}

/**
 * Replaces a scenery object with another for a specified duration or permanently.
 *
 * @param toReplace The scenery object to replace.
 * @param with The ID of the new scenery object.
 * @param forTicks The number of ticks the replacement should last, or -1 for permanent replacement.
 * @param loc The location to replace the scenery at, or null to use the current location of the scenery.
 */
fun replaceScenery(
    toReplace: Scenery,
    with: Int,
    forTicks: Int,
    loc: Location? = null,
) {
    val newLoc =
        when (loc) {
            null -> toReplace.location
            else -> loc
        }
    if (forTicks == -1) {
        SceneryBuilder.replace(toReplace, toReplace.transform(with, toReplace.rotation, newLoc))
    } else {
        SceneryBuilder.replace(toReplace, toReplace.transform(with, toReplace.rotation, newLoc), forTicks)
    }
    toReplace.isActive = false
}

/**
 * Adds a scenery object to the game world.
 *
 * @param scenery The scenery object to be added.
 */
fun addScenery(scenery: Scenery) {
    SceneryBuilder.add(scenery)
}

/**
 * Creates and adds a scenery object to the game world at the specified location.
 *
 * @param sceneryId The ID of the scenery to be added.
 * @param location The location where the scenery should be placed.
 * @param rotation The rotation of the scenery. Default is 0.
 * @param type The type of scenery. Default is 22.
 * @return The created scenery object.
 */
fun addScenery(
    sceneryId: Int,
    location: Location,
    rotation: Int = 0,
    type: Int = 22,
): Scenery {
    val scenery = Scenery(sceneryId, location, type, rotation)
    SceneryBuilder.add(scenery)
    return scenery
}

/**
 * Removes a scenery object from the game world.
 *
 * @param scenery The scenery object to be removed.
 */
fun removeScenery(scenery: Scenery) {
    SceneryBuilder.remove(scenery)
}

/**
 * Replaces one scenery object with another in the game world for a specified duration.
 *
 * @param toReplace The scenery object to be replaced.
 * @param with The ID of the scenery to replace with.
 * @param forTicks The duration (in ticks) to replace the scenery. Use -1 for permanent replacement.
 * @param rotation The direction to rotate the new scenery to.
 * @param loc The location of the new scenery. Default is the location of the original scenery.
 */
fun replaceScenery(
    toReplace: Scenery,
    with: Int,
    forTicks: Int,
    rotation: Direction,
    loc: Location? = null,
) {
    val newLoc =
        when (loc) {
            null -> toReplace.location
            else -> loc
        }
    val rot =
        when (rotation) {
            Direction.NORTH_WEST -> 0
            Direction.NORTH -> 1
            Direction.NORTH_EAST -> 2
            Direction.EAST -> 4
            Direction.SOUTH_EAST -> 7
            Direction.SOUTH -> 6
            Direction.SOUTH_WEST -> 5
            Direction.WEST -> 3
        }
    if (forTicks == -1) {
        SceneryBuilder.replace(toReplace, toReplace.transform(with, rot, newLoc))
    } else {
        SceneryBuilder.replace(toReplace, toReplace.transform(with, rot, newLoc), forTicks)
    }
    toReplace.isActive = false
}

/**
 * Gets the name of an item given its ID.
 *
 * @param id The ID of the item.
 * @return The name of the item.
 */
fun getItemName(id: Int): String {
    return ItemDefinition.forId(id).name
}

/**
 * Checks if the player has space in their inventory for a item.
 *
 * @param player The player to check.
 * @param item The item to check space for.
 * @return True if there is space for the item in the player's inventory.
 */
fun hasSpaceFor(
    player: Player,
    item: Item,
): Boolean {
    return player.inventory.hasSpaceFor(item)
}

/**
 * Gets the current number of world ticks.
 *
 * @return The current world tick count.
 */
fun getWorldTicks(): Int {
    return GameWorld.ticks
}

/**
 * Creates an audio object with the id and other parameters.
 *
 * @param id The ID of the audio.
 * @param volume The volume of the audio. Default is 10.
 * @param delay The delay before the audio plays. Default is 1.
 * @return The created Audio object.
 */
fun getAudio(
    id: Int,
    volume: Int = 10,
    delay: Int = 1,
): Audio {
    return Audio(id, volume, delay)
}

/**
 * Plays a jingle for a player.
 *
 * @param player The player to play the jingle for.
 * @param jingleId The ID of the jingle to be played.
 */
fun playJingle(
    player: Player,
    jingleId: Int,
) {
    PacketRepository.send(MusicPacket::class.java, MusicContext(player, jingleId, true))
}

/**
 * Applies an impact to an entity, affecting its health or other attributes.
 *
 * @param entity The entity to apply the impact to.
 * @param amount The amount of damage or impact.
 * @param type The type of hitsplat (impact effect) to apply.
 */
fun impact(
    entity: Entity,
    amount: Int,
    type: ImpactHandler.HitsplatType = ImpactHandler.HitsplatType.NORMAL,
) {
    entity.impactHandler.manualHit(entity, amount, type)
}

/**
 * Applies an impact to an entity, affecting its health or other attributes.
 *
 * @param entity The entity to apply the impact to.
 * @param amount The amount of damage or impact.
 * @param type The type of hitsplat (impact effect) to apply.
 * @param ticks The delay in ticks before the damage punch.
 */
fun impact(
    entity: Entity,
    amount: Int,
    type: ImpactHandler.HitsplatType = ImpactHandler.HitsplatType.NORMAL,
    ticks: Int,
) {
    entity.impactHandler.manualHit(entity, amount, type, ticks)
}

/**
 * Checks if a specific node (NPC, Scenery, or Item) has a particular option available.
 *
 * @param node The node to check.
 * @param option The option to check for.
 * @return True if the option exists on the node.
 * @throws IllegalArgumentException If the node is not of a valid type.
 */
fun hasOption(
    node: Node,
    option: String,
): Boolean {
    return when (node) {
        is NPC -> node.definition.hasAction(option)
        is Scenery -> node.definition.hasAction(option)
        is Item -> node.definition.hasAction(option)
        else -> throw IllegalArgumentException("Expected an NPC, Scenery or an Item, got ${node.javaClass.simpleName}.")
    }
}

/**
 * Animates a scenery object.
 *
 * @param player The player to animate the scenery for.
 * @param obj The scenery object to animate.
 * @param animationId The ID of the animation to apply.
 * @param global If true, the animation is visible to all players. Default is false.
 */
fun animateScenery(
    player: Player,
    obj: Scenery,
    animationId: Int,
    global: Boolean = false,
) {
    player.packetDispatch.sendSceneryAnimation(obj, getAnimation(animationId), global)
}

/**
 * Animates a scenery object.
 *
 * @param obj The scenery object to animate.
 * @param animationId The ID of the animation to apply.
 */
fun animateScenery(
    obj: Scenery,
    animationId: Int,
) {
    val animation = Animation(animationId)
    animation.setObject(obj)
    getRegionChunk(obj.location).flag(
        AnimateSceneryUpdateFlag(
            animation,
        ),
    )
}

/**
 * Spawns a projectile between two entities.
 *
 * @param source The source entity that fires the projectile.
 * @param dest The target entity to hit with the projectile.
 * @param projectileId The ID of the projectile.
 */
fun spawnProjectile(
    source: Entity,
    dest: Entity,
    projectileId: Int,
) {
    Projectile.create(source, dest, projectileId).send()
}

/**
 * Spawns a projectile from a source location to a destination location.
 *
 * @param source The source location of the projectile.
 * @param dest The destination location of the projectile.
 * @param projectile The ID of the projectile.
 * @param startHeight The starting height of the projectile.
 * @param endHeight The end height of the projectile.
 * @param delay The delay before the projectile is fired.
 * @param speed The speed of the projectile.
 * @param angle The angle at which the projectile travels.
 */
fun spawnProjectile(
    source: Location,
    dest: Location,
    projectile: Int,
    startHeight: Int,
    endHeight: Int,
    delay: Int,
    speed: Int,
    angle: Int,
) {
    Projectile
        .create(
            source,
            dest,
            projectile,
            startHeight,
            endHeight,
            delay,
            speed,
            angle,
            source.getDistance(dest).toInt(),
        ).send()
}

/**
 * Faces an entity towards a specific node (NPC or Location) for a specified duration.
 *
 * @param entity The entity to face.
 * @param toFace The node (NPC or Location) to face.
 * @param duration The duration (in ticks) to face the node. Use -1 for indefinite facing.
 */
fun face(
    entity: Entity,
    toFace: Node,
    duration: Int = -1,
) {
    if (duration == -1) {
        when (toFace) {
            is Location -> entity.faceLocation(toFace)
            is Entity -> entity.face(toFace)
        }
    } else {
        when (toFace) {
            is Location -> entity.faceTemporary(toFace.asNpc(), duration)
            else -> entity.faceTemporary(toFace as Entity, duration)
        }
    }
}

/**
 * Resets the entity's facing direction to its original location.
 *
 * @param entity The entity whose facing direction will be reset.
 */
fun resetFace(entity: Entity) {
    entity.face(null)
    entity.faceLocation(entity.location)
}

/**
 * Faces an entity towards a specified location.
 *
 * @param entity The entity to face.
 * @param location The location to face towards.
 */
fun faceLocation(
    entity: Entity,
    location: Location,
) {
    entity.faceLocation(location)
}

/**
 * Opens an interface for a player.
 *
 * @param player The player to open the interface for.
 * @param id The ID of the interface to open.
 */
fun openInterface(
    player: Player,
    id: Int,
) {
    player.interfaceManager.open(Component(id))
}

/**
 * Opens an overlay for a player.
 *
 * @param player The player to open the overlay for.
 * @param id The ID of the overlay to open.
 */
fun openOverlay(
    player: Player,
    id: Int,
) {
    player.interfaceManager.openOverlay(Component(id))
}

/**
 * Opens a chatbox for a player.
 *
 * @param player The player to open the chatbox for.
 * @param id The ID of the chatbox to open.
 */
fun openChatbox(
    player: Player,
    id: Int,
) {
    player.interfaceManager.openChatbox(Component(id))
}

/**
 * Closes the overlay for a player.
 *
 * @param player The player whose overlay will be closed.
 */
fun closeOverlay(player: Player) {
    player.interfaceManager.closeOverlay()
}

/**
 * Closes all interfaces for the given player.
 *
 * @param player The player whose interfaces will be closed.
 */
fun closeAllInterfaces(player: Player) {
    player.interfaceManager.close()
    player.interfaceManager.closeChatbox()
    player.dialogueInterpreter.close()
}

/**
 * Unlocks a specific emote for the given player.
 *
 * @param player The player for whom the emote will be unlocked.
 * @param emoteId The ID of the emote to unlock.
 */
fun unlockEmote(
    player: Player,
    emoteId: Int,
) {
    player.emoteManager.unlock(Emotes.forId(emoteId))
}

/**
 * Sends a message to the player, splitting it into lines of up to 86 characters.
 *
 * @param player The player to send the message to.
 * @param message The message to send.
 */
fun sendMessage(
    player: Player,
    message: String,
) {
    player.sendMessages(*splitLines(message, 86))
}

/**
 * Sends a tutorial message to the player, displayed in bold input format.
 *
 * @param player The player to send the tutorial message to.
 * @param message The tutorial message.
 */
fun sendTutorialMessage(
    player: Player,
    message: String,
) {
    player.dialogueInterpreter.sendBoldInput(message)
}

/**
 * Sends multiple messages to the player.
 *
 * @param player The player to send the messages to.
 * @param message The messages to send.
 */
fun sendMessages(
    player: Player,
    vararg message: String,
) {
    player.packetDispatch.sendMessages(*message)
}

/**
 * Sends a message to the player with a specified delay in ticks.
 *
 * @param player The player to send the message to.
 * @param message The message to send.
 * @param ticks The delay in ticks before the message is sent.
 */
fun sendMessageWithDelay(
    player: Player,
    message: String,
    ticks: Int,
) {
    player.sendMessage(message, ticks)
}

/**
 * Sends a chat message to the entity, optionally with a delay.
 *
 * @param entity The entity to send the chat message to.
 * @param message The message to send.
 * @param delay The delay in ticks before sending the message. Defaults to -1 (no delay).
 */
fun sendChat(
    entity: Entity,
    message: String,
    delay: Int = -1,
) {
    if (delay > -1) {
        queueScript(entity, delay, QueueStrength.SOFT) {
            entity.sendChat(message)
            return@queueScript stopExecuting(entity)
        }
    } else {
        entity.sendChat(message)
    }
}

/**
 * Sends a dialogue message to the player, splitting it into lines if necessary.
 *
 * @param player The player to send the dialogue message to.
 * @param message The dialogue message.
 */
fun sendDialogue(
    player: Player,
    message: String,
) {
    player.dialogueInterpreter.sendDialogue(*splitLines(message))
}

/**
 * Sends a tutorial NPC dialogue to the player.
 *
 * @param player The player to send the dialogue to.
 * @param npc The ID of the NPC initiating the dialogue.
 * @param message The dialogue messages.
 */
fun sendTutorialNPCDialogue(
    player: Player,
    npc: Int,
    vararg message: String,
) {
    Component.setUnclosable(player, player.dialogueInterpreter.sendDialogues(npc, FaceAnim.FRIENDLY, *message))
}

/**
 * Sends a tutorial NPC dialogue to the player with an optional facial expression.
 *
 * @param player The player to send the dialogue to.
 * @param npc The ID of the NPC initiating the dialogue.
 * @param expr The facial expression of the NPC. Defaults to FaceAnim.FRIENDLY.
 * @param message The dialogue messages.
 */
fun sendTutorialNPCDialogue(
    player: Player,
    npc: Int,
    expr: FaceAnim = FaceAnim.FRIENDLY,
    vararg message: String,
) {
    Component.setUnclosable(player, player.dialogueInterpreter.sendDialogues(npc, expr, *message))
}

/**
 * Sends a dialogue for destroying an item, with an optional message.
 *
 * @param player The player to send the dialogue to.
 * @param item The ID of the item to be destroyed.
 * @param message An optional message to include in the dialogue.
 */
fun sendDestroyItemDialogue(
    player: Player,
    item: Int,
    message: String?,
) {
    player.dialogueInterpreter.sendDestroyItem(item, message)
}

/**
 * Sends multiple lines of dialogue to the player.
 *
 * @param player The player to send the dialogue to.
 * @param message The dialogue lines.
 */
fun sendDialogueLines(
    player: Player,
    vararg message: String,
) {
    player.dialogueInterpreter.sendDialogue(*message)
}

/**
 * Sends a set of dialogue options to the player.
 *
 * @param player The player to send the options to.
 * @param title The title of the options dialogue.
 * @param options The options available to the player.
 */
fun sendDialogueOptions(
    player: Player,
    title: String,
    vararg options: String,
) {
    player.dialogueInterpreter.sendOptions(title, *options)
}

/**
 * Sends a plain dialogue message to the player, with an option to hide the continue button.
 *
 * @param player The player to send the dialogue to.
 * @param hideContinue Whether to hide the continue button. Defaults to false.
 * @param message The dialogue message.
 */
fun sendPlainDialogue(
    player: Player,
    hideContinue: Boolean = false,
    vararg message: String,
) {
    player.dialogueInterpreter.sendPlainMessage(hideContinue, *message)
}

/**
 * Sends a plain dialogue message that cannot be closed, with an option to hide the continue button.
 *
 * @param player The player to send the dialogue to.
 * @param hideContinue Whether to hide the continue button. Defaults to false.
 * @param message The dialogue message.
 */
fun sendUnclosablePlainDialogue(
    player: Player,
    hideContinue: Boolean = false,
    vararg message: String,
) {
    Component.setUnclosable(player, player.dialogueInterpreter.sendPlainMessage(hideContinue, *message))
}

/**
 * Sends a set of unclosable dialogue messages to the player.
 *
 * @param player The player to send the dialogue to.
 * @param message The dialogue messages.
 */
fun sendUnclosableDialogue(
    player: Player,
    vararg message: String,
) {
    Component.setUnclosable(player, player.dialogueInterpreter.sendDialogues(*message))
}

/**
 * Animates an entity using the provided animation, with an option to force the animation.
 *
 * @param entity The entity to animate.
 * @param anim The animation to apply, either as an Int (ID) or Animation object.
 * @param forced Whether to force the animation, overriding any existing ones. Defaults to false.
 */
fun <T> animate(
    entity: Entity,
    anim: T,
    forced: Boolean = false,
) {
    val animation =
        when (anim) {
            is Int -> Animation(anim)
            is Animation -> anim
            else -> throw IllegalStateException("Invalid value passed for anim")
        }

    if (forced) {
        entity.animator.forceAnimation(animation)
    } else {
        entity.animator.animate(animation)
    }
}

/**
 * Sends an animation to the player with a specified delay.
 *
 * @param player The player to send the animation to.
 * @param animation The animation ID.
 * @param delay The delay in ticks before the animation is sent.
 */
fun sendAnimation(
    player: Player,
    animation: Int,
    delay: Int,
) {
    player.packetDispatch.sendAnimation(animation, delay)
}

/**
 * Plays an audio file for the player, with optional delay, loops, and location parameters.
 *
 * @param player The player to play the audio for.
 * @param id The audio file ID.
 * @param delay The delay in ticks before the audio starts. Defaults to 0.
 * @param loops The number of times the audio should loop. Defaults to 1.
 * @param location The location to play the audio from. Defaults to null.
 * @param radius The radius within which the audio is heard. Defaults to Audio.defaultAudioRadius.
 */
@JvmOverloads
fun playAudio(
    player: Player,
    id: Int,
    delay: Int = 0,
    loops: Int = 1,
    location: Location? = null,
    radius: Int = Audio.defaultAudioRadius,
) {
    PacketRepository.send(
        AudioPacket::class.java,
        DefaultContext(player, Audio(id, delay, loops, radius), location),
    )
}

/**
 * Plays an audio file for all players within a specified radius of a given location.
 *
 * @param location The location where the audio will be played from.
 * @param id The audio file ID.
 * @param delay The delay in ticks before the audio starts. Defaults to 0.
 * @param loops The number of times the audio should loop. Defaults to 1.
 * @param radius The radius within which players will hear the audio. Defaults to Audio.defaultAudioRadius.
 */
@JvmOverloads
fun playGlobalAudio(
    location: Location,
    id: Int,
    delay: Int = 0,
    loops: Int = 1,
    radius: Int = Audio.defaultAudioRadius,
) {
    val nearbyPlayers = RegionManager.getLocalPlayers(location, radius)
    for (player in nearbyPlayers) {
        PacketRepository.send(
            AudioPacket::class.java,
            DefaultContext(player, Audio(id, delay, loops, radius), location),
        )
    }
}

/**
 * Plays a hurt audio sound for the player, selecting the appropriate sound based on their gender.
 *
 * @param player The player to play the hurt audio for.
 * @param delay The delay in ticks before the audio starts. Defaults to 0.
 */
fun playHurtAudio(
    player: Player,
    delay: Int = 0,
) {
    val maleHurtAudio =
        intArrayOf(Sounds.HUMAN_HIT4_516, Sounds.HUMAN_HIT5_517, Sounds.HUMAN_HIT_518, Sounds.HUMAN_HIT_6_522)
    val femaleHurtAudio =
        intArrayOf(Sounds.FEMALE_HIT_506, Sounds.FEMALE_HIT_507, Sounds.FEMALE_HIT2_508, Sounds.FEMALE_HIT_2_510)
    if (player.isMale) {
        playAudio(player, maleHurtAudio.random(), delay)
    } else {
        playAudio(player, femaleHurtAudio.random(), delay)
    }
}

/**
 * Opens a dialogue for the player, using various types of dialogue objects.
 *
 * @param player The player to open the dialogue for.
 * @param dialogue The dialogue to open, either as an ID, String, DialogueFile, or SkillDialogueHandler.
 * @param args Any additional arguments to pass to the dialogue.
 */
fun openDialogue(
    player: Player,
    dialogue: Any,
    vararg args: Any,
) {
    player.dialogueInterpreter.close()
    when (dialogue) {
        is Int -> player.dialogueInterpreter.open(dialogue, *args)
        is String -> player.dialogueInterpreter.open(getDialogueKey(dialogue), *args)
        is DialogueFile -> player.dialogueInterpreter.open(dialogue, *args)
        is SkillDialogueHandler -> dialogue.open()
        else ->
            log(
                ContentAPI::class.java,
                Log.ERR,
                "Invalid object type passed to openDialogue() -> ${dialogue.javaClass.simpleName}",
            )
    }
}

/**
 * Finds an NPC by its ID.
 *
 * @param id The ID of the NPC to find.
 * @return The NPC with the specified ID, or null if not found.
 */
fun findNPC(id: Int): NPC? {
    return Repository.findNPC(id)
}

/**
 * Gets the scenery at the specified location.
 *
 * @param x The x-coordinate of the location.
 * @param y The y-coordinate of the location.
 * @param z The z-coordinate (height level) of the location.
 * @return The Scenery object at the specified location, or null if not found.
 */
fun getScenery(
    x: Int,
    y: Int,
    z: Int,
): Scenery? {
    return RegionManager.getObject(z, x, y)
}

/**
 * Gets the scenery at the specified location.
 *
 * @param loc The location.
 * @return The Scenery object at the location, or null if not found.
 */
fun getScenery(loc: Location): Scenery? {
    return RegionManager.getObject(loc)
}

/**
 * Finds an NPC by location and ID.
 *
 * @param refLoc The reference location.
 * @param id The ID of the NPC to find.
 * @return The NPC with the specified ID at the given location, or null if not found.
 */
fun findNPC(
    refLoc: Location,
    id: Int,
): NPC? {
    return Repository.npcs.firstOrNull { it.id == id && it.location.withinDistance(refLoc) }
}

/**
 * Finds a local NPC by its ID for the specified entity.
 *
 * @param entity The entity to check local NPCs for.
 * @param id The ID of the NPC to find.
 * @return The NPC with the specified ID, or null if not found.
 */
fun findLocalNPC(
    entity: Entity,
    id: Int,
): NPC? {
    return RegionManager.getLocalNpcs(entity).firstOrNull { it.id == id }
}

/**
 * Finds local NPCs within a specified distance of a location.
 *
 * @param location The location to check for nearby NPCs.
 * @param distance The maximum distance from the location to check.
 * @return A list of NPCs within the specified distance of the location.
 */
fun findLocalNPCs(
    location: Location,
    distance: Int,
): MutableList<NPC> {
    return RegionManager.getLocalNpcs(location, distance)
}

/**
 * Finds local NPCs by their IDs for the specified entity.
 *
 * @param entity The entity to check local NPCs for.
 * @param ids The IDs of the NPCs to find.
 * @return A list of NPCs with the specified IDs.
 */
fun findLocalNPCs(
    entity: Entity,
    ids: IntArray,
): List<NPC> {
    return RegionManager.getLocalNpcs(entity).filter { it.id in ids }.toList()
}

/**
 * Finds local NPCs by their IDs for the specified entity and within a certain distance.
 *
 * @param entity The entity to check local NPCs for.
 * @param ids The IDs of the NPCs to find.
 * @param distance The maximum distance from the entity to check.
 * @return A list of NPCs with the specified IDs within the specified distance.
 */
fun findLocalNPCs(
    entity: Entity,
    ids: IntArray,
    distance: Int,
): List<NPC> {
    return RegionManager.getLocalNpcs(entity, distance).filter { it.id in ids }.toList()
}

/**
 * Gets the zone borders for a specified region ID.
 *
 * @param regionId The ID of the region.
 * @return The zone borders for the specified region.
 */
fun getRegionBorders(regionId: Int): ZoneBorders {
    return ZoneBorders.forRegion(regionId)
}

/**
 * Retrieves an attribute from the entity, providing a default value if not found.
 *
 * @param entity The entity from which to get the attribute.
 * @param attribute The name of the attribute.
 * @param default The default value to return if the attribute is not found.
 * @return The value of the attribute, or the default if not found.
 */
fun <T> getAttribute(
    entity: Entity,
    attribute: String,
    default: T,
): T {
    return entity.getAttribute(attribute, default)
}

/**
 * Sets an attribute on the entity.
 *
 * @param entity The entity to set the attribute for.
 * @param attribute The name of the attribute.
 * @param value The value to set for the attribute.
 */
fun <T> setAttribute(
    entity: Entity,
    attribute: String,
    value: T,
) {
    entity.setAttribute(attribute, value)
}

/**
 * Removes an attribute from the entity.
 *
 * @param entity The entity to remove the attribute from.
 * @param attribute The name of the attribute to remove.
 */
fun removeAttribute(
    entity: Entity,
    attribute: String,
) {
    entity.removeAttribute(attribute.replace("/save:", ""))
}

/**
 * Removes multiple attributes from the entity.
 *
 * @param entity The entity to remove the attributes from.
 * @param attributes The attributes to remove.
 */
fun removeAttributes(
    entity: Entity,
    vararg attributes: String,
) {
    for (attribute in attributes) removeAttribute(entity, attribute)
}

/**
 * Registers a timer for the specified entity.
 *
 * @param entity The entity to register the timer for.
 * @param timer The timer to register.
 */
fun registerTimer(
    entity: Entity,
    timer: RSTimer?,
) {
    if (timer == null) return
    entity.timers.registerTimer(timer)
}

/**
 * Retrieves or starts a timer for the specified entity.
 *
 * @param entity The entity to retrieve or start the timer for.
 * @param args Any arguments required to start the timer.
 * @return The existing or newly spawned timer.
 */
inline fun <reified T : RSTimer> getOrStartTimer(
    entity: Entity,
    vararg args: Any,
): T {
    val existing = getTimer<T>(entity)
    if (existing != null) return existing
    return spawnTimer<T>(*args).also { registerTimer(entity, it) }
}

/**
 * Spawns a new timer with the specified identifier and arguments.
 *
 * @param identifier The identifier of the timer.
 * @param args The arguments to spawn the timer with.
 * @return The spawned timer, or null if the timer could not be spawned.
 */
fun spawnTimer(
    identifier: String,
    vararg args: Any,
): RSTimer? {
    return TimerRegistry.getTimerInstance(identifier, *args)
}

/**
 * Spawns a new timer of the specified type with the given arguments.
 *
 * @param args The arguments to spawn the timer with.
 * @return The spawned timer.
 */
inline fun <reified T : RSTimer> spawnTimer(vararg args: Any): T {
    return TimerRegistry.getTimerInstance<T>(*args)!!
}

/**
 * Checks if a timer of the specified type is active for the given entity.
 *
 * @param entity The entity to check for an active timer.
 * @return True if the timer is active, false otherwise.
 */
inline fun <reified T : RSTimer> hasTimerActive(entity: Entity): Boolean {
    return getTimer<T>(entity) != null
}

/**
 * Retrieves the active timer of the specified type for the entity.
 *
 * @param entity The entity to retrieve the timer from.
 * @return The active timer, or null if not found.
 */
inline fun <reified T : RSTimer> getTimer(entity: Entity): T? {
    return entity.timers.getTimer<T>()
}

/**
 * Removes the active timer of the specified type for the given entity.
 *
 * @param entity The entity to remove the timer from.
 */
inline fun <reified T : RSTimer> removeTimer(entity: Entity) {
    entity.timers.removeTimer<T>()
}

/**
 * Checks if a timer with the specified identifier is active for the given entity.
 *
 * @param entity The entity to check for an active timer.
 * @param identifier The identifier of the timer.
 * @return True if the timer is active, false otherwise.
 */
fun hasTimerActive(
    entity: Entity,
    identifier: String,
): Boolean {
    return getTimer(entity, identifier) != null
}

/**
 * Retrieves the timer associated with the given identifier for the specified entity.
 *
 * @param entity The entity whose timer is to be retrieved.
 * @param identifier The unique identifier for the timer.
 * @return The timer associated with the identifier, or null if not found.
 */
fun getTimer(
    entity: Entity,
    identifier: String,
): RSTimer? {
    return entity.timers.getTimer(identifier)
}

/**
 * Removes the timer associated with the given identifier from the specified entity.
 *
 * @param entity The entity whose timer is to be removed.
 * @param identifier The unique identifier for the timer to be removed.
 */
fun removeTimer(
    entity: Entity,
    identifier: String,
) {
    entity.timers.removeTimer(identifier)
}

/**
 * Removes the specified timer from the entity.
 *
 * @param entity The entity from which the timer is to be removed.
 * @param timer The timer to be removed.
 */
fun removeTimer(
    entity: Entity,
    timer: RSTimer,
) {
    entity.timers.removeTimer(timer)
}

/**
 * Locks the entity for a specified duration, preventing actions like movement or interaction.
 *
 * @param entity The entity to be locked.
 * @param duration The duration (in seconds) for which the entity will be locked.
 */
fun lock(
    entity: Entity,
    duration: Int,
) {
    entity.lock(duration)
}

/**
 * Locks the entity's ability to interact with others for a specified duration.
 *
 * @param entity The entity whose interactions are to be locked.
 * @param duration The duration (in seconds) for which interactions are locked.
 */
fun lockInteractions(
    entity: Entity,
    duration: Int,
) {
    entity.locks.lockInteractions(duration)
}

/**
 * Locks the entity's movement for a specified duration.
 *
 * @param entity The entity whose movement is to be locked.
 * @param duration The duration (in seconds) for which movement is locked.
 */
fun lockMovement(
    entity: Entity,
    duration: Int,
) {
    entity.locks.lockMovement(duration)
}

/**
 * Locks the entity's equipment for a specified duration.
 *
 * @param entity The entity whose equipment is to be locked.
 * @param duration The duration (in seconds) for which equipment is locked.
 */
fun lockEquipment(
    entity: Entity,
    duration: Int,
) {
    entity.locks.equipmentLock.lock(duration)
}

/**
 * Checks if the entity's teleportation is currently locked.
 *
 * @param entity The entity whose teleportation lock status is to be checked.
 * @return `true` if teleportation is locked, `false` otherwise.
 */
fun lockTeleport(entity: Entity) {
    entity.locks.isTeleportLocked
}

/**
 * Locks the entity's teleportation for a specified number of ticks.
 *
 * @param entity The entity whose teleportation is to be locked.
 * @param ticks The number of ticks for which teleportation is locked.
 */
fun lockTeleport(
    entity: Entity,
    ticks: Int,
) {
    entity.locks.lockTeleport(ticks)
}

/**
 * Unlocks the entity, allowing them to perform actions such as movement and interactions.
 *
 * @param entity The entity to be unlocked.
 */
fun unlock(entity: Entity) {
    entity.unlock()
}

/**
 * Creates a location with the specified coordinates.
 *
 * @param x The x-coordinate of the location.
 * @param y The y-coordinate of the location.
 * @param z The z-coordinate of the location.
 * @return A new location object.
 */
fun location(
    x: Int,
    y: Int,
    z: Int,
): Location {
    return Location.create(x, y, z)
}

/**
 * Checks if the entity is within the boundaries of a specified zone.
 *
 * @param entity The entity whose position is to be checked.
 * @param borders The borders of the zone to check.
 * @return `true` if the entity is inside the borders, `false` otherwise.
 */
fun inBorders(
    entity: Entity,
    borders: ZoneBorders,
): Boolean {
    return borders.insideBorder(entity)
}

/**
 * Checks if the entity is within a zone defined by its southwest and northeast coordinates.
 *
 * @param entity The entity whose position is to be checked.
 * @param swX The x-coordinate of the southwest corner.
 * @param swY The y-coordinate of the southwest corner.
 * @param neX The x-coordinate of the northeast corner.
 * @param neY The y-coordinate of the northeast corner.
 * @return `true` if the entity is inside the specified zone, `false` otherwise.
 */
fun inBorders(
    entity: Entity,
    swX: Int,
    swY: Int,
    neX: Int,
    neY: Int,
): Boolean {
    return ZoneBorders(swX, swY, neX, neY).insideBorder(entity)
}

/**
 * Checks if the entity is within a named zone.
 *
 * @param entity The entity whose position is to be checked.
 * @param name The name of the zone.
 * @return `true` if the entity is inside the specified zone, `false` otherwise.
 */
fun inZone(
    entity: Entity,
    name: String,
): Boolean {
    return entity.zoneMonitor.isInZone(name)
}

/**
 * Heals the entity by the specified amount.
 *
 * @param entity The entity to be healed.
 * @param amount The amount of health to heal.
 */
fun heal(
    entity: Entity,
    amount: Int,
) {
    entity.skills.heal(amount)
}

/**
 * Retrieves the value of a varp for the player.
 *
 * @param player The player whose varp value is to be retrieved.
 * @param varpIndex The index of the varp.
 * @return The value of the varp at the specified index.
 */
fun getVarp(
    player: Player,
    varpIndex: Int,
): Int {
    return player.varpMap[varpIndex] ?: 0
}

/**
 * Retrieves the value of a varbit for the player based on the provided varbit definition.
 *
 * @param player The player whose varbit value is to be retrieved.
 * @param def The varbit definition containing the necessary details.
 * @return The value of the varbit.
 */
fun getVarbit(
    player: Player,
    def: VarbitDefinition,
): Int {
    val mask = def.getMask()
    val current = getVarp(player, def.varpId)
    return (current shr def.startBit) and mask
}

/**
 * Retrieves the value of a varbit for the player based on the provided varbit ID.
 *
 * @param player The player whose varbit value is to be retrieved.
 * @param varbitId The ID of the varbit.
 * @return The value of the varbit.
 */
fun getVarbit(
    player: Player,
    varbitId: Int,
): Int {
    val def = VarbitDefinition.forId(varbitId)
    return getVarbit(player, def)
}

/**
 * Sets the value of a varp for the player.
 *
 * @param player The player whose varp value is to be set.
 * @param varpIndex The index of the varp to be set.
 * @param value The value to set for the varp.
 * @param save If `true`, the varp value will be saved.
 */
@JvmOverloads
fun setVarp(
    player: Player,
    varpIndex: Int,
    value: Int,
    save: Boolean = false,
) {
    player.varpMap[varpIndex] = value
    player.saveVarp[varpIndex] = save
    player.packetDispatch.sendVarp(varpIndex, value)
}

/**
 * Marks a varp to be saved for the player.
 *
 * @param player The player whose varp is to be saved.
 * @param varpIndex The index of the varp to be saved.
 */
fun saveVarp(
    player: Player,
    varpIndex: Int,
) {
    player.saveVarp[varpIndex] = true
}

/**
 * Unmarks a varp from being saved for the player.
 *
 * @param player The player whose varp is to be unsaved.
 * @param varpIndex The index of the varp to be unsaved.
 */
fun unsaveVarp(
    player: Player,
    varpIndex: Int,
) {
    player.saveVarp.remove(varpIndex)
}

/**
 * Sets the value of a varbit for the player based on the provided varbit definition.
 *
 * @param player The player whose varbit value is to be set.
 * @param def The varbit definition containing the necessary details.
 * @param value The value to set for the varbit.
 * @param save If `true`, the varbit value will be saved.
 */
@JvmOverloads
fun setVarbit(
    player: Player,
    def: VarbitDefinition,
    value: Int,
    save: Boolean = false,
) {
    val mask = def.getMask()
    val current = getVarp(player, def.varpId) and (mask shl def.startBit).inv()
    val newValue = (value and mask) shl def.startBit
    setVarp(player, def.varpId, current or newValue, save)
}

/**
 * Sets the value of a varbit for the player based on the provided varbit ID.
 *
 * @param player The player whose varbit value is to be set.
 * @param varbitId The ID of the varbit to be set.
 * @param value The value to set for the varbit.
 * @param save If `true`, the varbit value will be saved.
 */
@JvmOverloads
fun setVarbit(
    player: Player,
    varbitId: Int,
    value: Int,
    save: Boolean = false,
) {
    val def = VarbitDefinition.forId(varbitId)

    if (def == null) {
        logWithStack(ContentAPI::class.java, Log.ERR, "Trying to setVarbit $varbitId, which doesn't seem to exist.")
        return
    }

    setVarbit(player, def, value, save)
}

/**
 * Sets the value of a varc for the player.
 *
 * @param player The player whose varc value is to be set.
 * @param varc The varc to set.
 * @param value The value to set for the varc.
 */
fun setVarc(
    player: Player,
    varc: Int,
    value: Int,
) {
    player.packetDispatch.sendVarcUpdate(varc.toShort(), value)
}

/**
 * Reinitializes the varps for the player, applying any saved values.
 *
 * @param player The player whose varps are to be reinitialized.
 */
fun reinitVarps(player: Player) {
    for ((index, value) in player.varpMap) {
        setVarp(player, index, value, player.saveVarp[index] ?: false)
    }
}

/**
 * Forces the entity to walk to a specified destination using a specified type of pathfinding.
 *
 * @param entity The entity to move.
 * @param dest The destination location.
 * @param type The type of pathfinding to use.
 */
fun forceWalk(
    entity: Entity,
    dest: Location,
    type: String,
) {
    if (type == "clip") {
        ForceMovement(entity, dest, 10, 10).run()
        return
    }
    val pathfinder =
        when (type) {
            "smart" -> Pathfinder.SMART
            else -> Pathfinder.DUMB
        }
    val path = Pathfinder.find(entity, dest, true, pathfinder)
    path.walk(entity)
}

/**
 * Forces the player to move from one location to another with specific arrival times and optional animation.
 *
 * @param player The player to move.
 * @param start The starting location.
 * @param dest The destination location.
 * @param startArrive The time to start the arrival animation (in ticks).
 * @param destArrive The time to arrive at the destination (in ticks).
 * @param dir The direction to move in, or `null` to auto-calculate.
 * @param anim The animation to play while moving.
 * @param callback An optional callback to invoke once the player has arrived.
 */
fun forceMove(
    player: Player,
    start: Location,
    dest: Location,
    startArrive: Int,
    destArrive: Int,
    dir: Direction? = null,
    anim: Int = 819,
    callback: (() -> Unit)? = null,
) {
    var direction: Direction

    if (dir == null) {
        var delta = Location.getDelta(start, dest)
        var x = abs(delta.x)
        var y = abs(delta.y)

        if (x > y) {
            direction = Direction.getDirection(delta.x, 0)
        } else {
            direction = Direction.getDirection(0, delta.y)
        }
    } else {
        direction = dir
    }

    val startLoc = Location.create(start)
    val destLoc = Location.create(dest)
    var startArriveTick = getWorldTicks() + cyclesToTicks(startArrive) + 1
    var destArriveTick = startArriveTick + cyclesToTicks(destArrive)
    var maskSet = false

    delayEntity(player, (destArriveTick - getWorldTicks()) + 1)
    queueScript(player, 0, QueueStrength.SOFT) {
        if (!finishedMoving(player)) return@queueScript keepRunning(player)
        if (!maskSet) {
            var ctx = ForceMoveCtx(startLoc, destLoc, startArrive, destArrive, direction)
            player.updateMasks.register(EntityFlag.ForceMove, ctx)
            maskSet = true
        }

        var tick = getWorldTicks()
        if (tick < startArriveTick) {
            return@queueScript keepRunning(player)
        } else if (tick < destArriveTick) {
            if (animationFinished(player)) animate(player, anim)
            return@queueScript keepRunning(player)
        } else if (tick >= destArriveTick) {
            try {
                callback?.invoke()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            player.properties.teleportLocation = dest
            return@queueScript stopExecuting(player)
        }
        return@queueScript stopExecuting(player)
    }
}

/**
 * Stops the walking action for the specified entity.
 *
 * @param entity The entity whose walking action will be stopped.
 */
fun stopWalk(entity: Entity) {
    entity.walkingQueue.reset()
}

/**
 * Retrieves the child object IDs associated with the specified scenery.
 *
 * @param scenery The scenery object ID.
 * @return An array of child object IDs for the given scenery.
 */
fun getChildren(scenery: Int): IntArray {
    return SceneryDefinition
        .forId(scenery)
        .configObjectIds!!
        .filter { it != -1 }
        .toIntArray()
}

/**
 * Adjusts the charge of a node (either an Item or a Scenery object) by a specified amount.
 *
 * @param node The node whose charge is to be adjusted (either an Item or Scenery).
 * @param amount The amount by which to adjust the charge.
 */
fun adjustCharge(
    node: Node,
    amount: Int,
) {
    when (node) {
        is Item -> node.charge += amount
        is Scenery -> node.charge += amount
        else ->
            log(
                ContentAPI::class.java,
                Log.ERR,
                "Attempt to adjust the charge of invalid type: ${node.javaClass.simpleName}",
            )
    }
}

/**
 * Retrieves the charge of a node (either an Item or a Scenery object).
 *
 * @param node The node whose charge is to be retrieved (either an Item or Scenery).
 * @return The charge of the node. Returns -1 if the node type is invalid.
 */
fun getCharge(node: Node): Int {
    when (node) {
        is Item -> return node.charge
        is Scenery -> return node.charge
        else ->
            log(
                ContentAPI::class.java,
                Log.ERR,
                "Attempt to get charge of invalid type: ${node.javaClass.simpleName}",
            ).also { return -1 }
    }
}

/**
 * Sets the charge of a node (either an Item or a Scenery object) to a specified value.
 *
 * @param node The node whose charge is to be set (either an Item or Scenery).
 * @param charge The charge value to set.
 */
fun setCharge(
    node: Node,
    charge: Int,
) {
    when (node) {
        is Item -> node.charge = charge
        is Scenery -> node.charge = charge
        else ->
            log(
                ContentAPI::class.java,
                Log.ERR,
                "Attempt to set the charge of invalid type: ${node.javaClass.simpleName}",
            )
    }
}

/**
 * Retrieves the option that the player used for interaction.
 *
 * @param player The player whose used option is to be retrieved.
 * @return The used interaction option.
 */
fun getUsedOption(player: Player): String {
    return player.getAttribute("interact:option", "INVALID")
}

/**
 * Visualizes an animation and graphics effect for the specified entity.
 *
 * @param entity The entity to visualize the animation and graphics on.
 * @param anim The animation to visualize.
 * @param gfx The graphic effect to visualize.
 * @throws IllegalStateException If invalid parameters for animation or graphics are provided.
 */
fun <A, G> visualize(
    entity: Entity,
    anim: A,
    gfx: G,
) {
    val animation =
        when (anim) {
            is Int -> Animation(anim)
            is Animation -> anim
            else -> throw IllegalStateException("Invalid parameter passed for animation.")
        }

    val graphics =
        when (gfx) {
            is Int -> Graphics(gfx)
            is Graphics -> gfx
            else -> throw IllegalStateException("Invalid parameter passed for graphics.")
        }

    entity.visualize(animation, graphics)
}

/**
 * Submits a world pulse to the pulse manager for processing.
 *
 * @param pulse The pulse to submit.
 */
fun submitWorldPulse(pulse: Pulse) {
    Pulser.submit(pulse)
}

/**
 * Runs a task in the world pulse system.
 *
 * @param task The task to run.
 * @return The Pulse object associated with the task.
 */
fun runWorldTask(task: () -> Unit): Pulse {
    val pulse =
        object : Pulse() {
            override fun pulse(): Boolean {
                task.invoke()
                return true
            }
        }

    submitWorldPulse(pulse)
    return pulse
}

/**
 * Teleports the entity to a specified location.
 *
 * @param entity The entity to teleport.
 * @param loc The location to teleport to.
 * @param type The type of teleportation (default is instant).
 * @return True if teleportation was successful, false otherwise.
 */
fun teleport(
    entity: Entity,
    loc: Location,
    type: TeleportManager.TeleportType = TeleportManager.TeleportType.INSTANT,
): Boolean {
    if (type == TeleportManager.TeleportType.INSTANT) {
        entity.properties.teleportLocation = loc
        return true
    } else {
        return entity.teleporter.send(loc, type)
    }
}

/**
 * Teleports the given entity to the specified location after the specified number of ticks.
 *
 * @param entity The entity that should be teleported.
 * @param location The location to which the entity should be teleported.
 * @param ticks The number of ticks after which the teleportation should occur.
 */
fun teleport(
    entity: Entity,
    location: Location,
    type: TeleportManager.TeleportType = TeleportManager.TeleportType.INSTANT,
    ticks: Int,
) {
    GameWorld.Pulser.submit(
        object : Pulse(ticks) {
            override fun pulse(): Boolean {
                teleport(entity, location, type)
                return true
            }
        },
    )
}

/**
 * Sets a temporary skill level for the entity.
 *
 * @param entity The entity whose skill level is to be set.
 * @param skill The skill to set the level for.
 * @param level The level to set the skill to.
 */
fun setTempLevel(
    entity: Entity,
    skill: Int,
    level: Int,
) {
    entity.skills.setLevel(skill, level)
}

/**
 * Retrieves the static (base) skill level for the entity.
 *
 * @param entity The entity whose skill level is to be retrieved.
 * @param skill The skill to retrieve the level for.
 * @return The static skill level of the entity.
 */
fun getStatLevel(
    entity: Entity,
    skill: Int,
): Int {
    return entity.skills.getStaticLevel(skill)
}

/**
 * Retrieves the dynamic skill level for the entity, considering bonuses.
 *
 * @param entity The entity whose skill level is to be retrieved.
 * @param skill The skill to retrieve the level for.
 * @return The dynamic skill level of the entity.
 */
fun getDynLevel(
    entity: Entity,
    skill: Int,
): Int {
    return entity.skills.getLevel(skill)
}

/**
 * Adjusts a skill level for the entity by a specified amount.
 *
 * @param entity The entity whose skill level is to be adjusted.
 * @param skill The skill to adjust.
 * @param amount The amount to adjust the skill level by.
 */
fun adjustLevel(
    entity: Entity,
    skill: Int,
    amount: Int,
) {
    entity.skills.setLevel(skill, entity.skills.getStaticLevel(skill) + amount)
}

/**
 * Removes all instances of a specified item from the player's specified container (Inventory, Equipment, Bank).
 *
 * @param player The player from whose container the item will be removed.
 * @param item The item or item ID to remove.
 * @param container The container to remove the item from (default is Inventory).
 * @return True if the item was successfully removed, false otherwise.
 */
fun <T> removeAll(
    player: Player,
    item: T,
    container: Container = Container.INVENTORY,
): Boolean {
    item ?: return false
    val it =
        when (item) {
            is Item -> item.id
            is Int -> item
            else -> throw IllegalStateException("Invalid value passed as item")
        }

    return when (container) {
        Container.EQUIPMENT -> player.equipment.remove(Item(it, amountInEquipment(player, it)))
        Container.BANK -> {
            val amountInPrimary = amountInBank(player, it, false)
            val amountInSecondary = amountInBank(player, it, true) - amountInPrimary
            player.bank.remove(Item(it, amountInPrimary)) && player.bankSecondary.remove(Item(it, amountInSecondary))
        }

        Container.INVENTORY -> player.inventory.remove(Item(it, amountInInventory(player, it)))
    }
}

/**
 * Sends a string to the player to display in a specified interface and child component.
 *
 * @param player The player to send the string to.
 * @param string The string to display.
 * @param iface The interface ID to display the string in.
 * @param child The child component ID to display the string in.
 */
fun sendString(
    player: Player,
    string: String,
    iface: Int,
    child: Int,
) {
    player.packetDispatch.sendString(string, iface, child)
}

/**
 * Closes all interfaces currently open for the player.
 *
 * @param player The player whose interfaces will be closed.
 */
fun closeInterface(player: Player) {
    player.interfaceManager.close()
}

/**
 * Closes a single tab interface for the player.
 *
 * @param player The player whose tab interface will be closed.
 */
fun closeTabInterface(player: Player) {
    player.interfaceManager.closeSingleTab()
}

/**
 * Closes the chatbox interface for the player.
 *
 * @param player The player whose chatbox will be closed.
 */
fun closeChatBox(player: Player) {
    player.interfaceManager.closeChatbox()
}

/**
 * Sets the visibility of a component in the player's interface.
 *
 * @param player The player whose interface component visibility will be set.
 * @param iface The interface ID of the component.
 * @param child The child component ID.
 * @param hide Whether to hide or show the component.
 */
fun setComponentVisibility(
    player: Player,
    iface: Int,
    child: Int,
    hide: Boolean,
) {
    player.packetDispatch.sendInterfaceConfig(iface, child, hide)
}

/**
 * Sets the title component visibility for the player based on the provided options.
 *
 * @param player The player whose UI component will be modified.
 * @param options The options determining the visibility of components (should be between 2 and 5).
 * @throws IllegalArgumentException If options are outside the valid range.
 */
fun setTitle(
    player: Player,
    options: Int,
) {
    setComponentVisibility(
        player,
        if (options == 5) {
            234
        } else if (options == 4) {
            232
        } else if (options == 3) {
            230
        } else {
            228
        },
        (4 + options),
        true,
    )
    setComponentVisibility(
        player,
        if (options == 5) {
            234
        } else if (options == 4) {
            232
        } else if (options == 3) {
            230
        } else {
            228
        },
        if (options == 2 || options == 4) 9 else 10,
        false,
    )
    if (options > 5) {
        throw java.lang.IllegalArgumentException(
            "Expected option value between 2 and 5, got ${options::class.java.simpleName}.",
        )
    }
}

/**
 * Sends a dialogue message to the player.
 *
 * @param player The player who will receive the dialogue.
 * @param msg The message to display to the player.
 * @param expr The facial expression of the player while the dialogue is being displayed. Defaults to [FaceAnim.FRIENDLY].
 */
fun sendPlayerDialogue(
    player: Player,
    msg: String,
    expr: FaceAnim = FaceAnim.FRIENDLY,
) {
    player.dialogueInterpreter.sendDialogues(player, expr, *splitLines(msg))
}

/**
 * Sends a message to the player on a specific interface.
 *
 * @param player The player receiving the interface.
 * @param iface The interface ID to show the player.
 * @param child The child component ID of the interface.
 */
fun sendPlayerOnInterface(
    player: Player,
    iface: Int,
    child: Int,
) {
    player.packetDispatch.sendPlayerOnInterface(iface, child)
}

/**
 * Sends a dialogue message from an NPC to the player.
 *
 * @param player The player receiving the dialogue.
 * @param npc The NPC ID sending the message.
 * @param msg The message to display.
 * @param expr The facial expression of the NPC. Defaults to [FaceAnim.FRIENDLY].
 */
fun sendNPCDialogue(
    player: Player,
    npc: Int,
    msg: String,
    expr: FaceAnim = FaceAnim.FRIENDLY,
) {
    player.dialogueInterpreter.sendDialogues(npc, expr, *splitLines(msg))
}

/**
 * Sends a delayed dialogue message from an NPC to the player.
 *
 * @param player The player receiving the dialogue.
 * @param tick The delay (in game ticks) before the dialogue is shown.
 * @param npc The NPC ID sending the message.
 * @param msg The message to display.
 * @param expr The facial expression of the NPC. Defaults to [FaceAnim.FRIENDLY].
 */
fun sendNPCDialogueWithDelay(
    player: Player,
    tick: Int,
    npc: Int,
    msg: String,
    expr: FaceAnim = FaceAnim.FRIENDLY,
) {
    player.dialogueInterpreter.sendDialogues(tick, npc, expr, *splitLines(msg))
}

/**
 * Sends multiple lines of dialogue from an NPC to the player.
 *
 * @param player The player receiving the dialogue.
 * @param npc The NPC ID sending the message.
 * @param expr The facial expression of the NPC.
 * @param hideContinue If true, the "Continue" button will be hidden.
 * @param msgs The dialogue lines to send.
 */
fun sendNPCDialogueLines(
    player: Player,
    npc: Int,
    expr: FaceAnim,
    hideContinue: Boolean,
    vararg msgs: String,
) {
    val dialogueComponent = player.dialogueInterpreter.sendDialogues(npc, expr, *msgs)
    player.packetDispatch.sendInterfaceConfig(dialogueComponent.id, msgs.size + 4, hideContinue)
}

/**
 * Sends an animation on a specific interface.
 *
 * @param player The player receiving the animation.
 * @param anim The animation ID to play.
 * @param iface The interface ID where the animation will be shown.
 * @param child The child component ID within the interface.
 */
fun sendAnimationOnInterface(
    player: Player,
    anim: Int,
    iface: Int,
    child: Int,
) {
    player.packetDispatch.sendAnimationInterface(anim, iface, child)
}

/**
 * Registers a logout listener for a specific player.
 *
 * @param player The player to register the listener for.
 * @param key The unique key identifying the listener.
 * @param handler The function to execute when the player logs out.
 */
fun registerLogoutListener(
    player: Player,
    key: String,
    handler: (p: Player) -> Unit,
) {
    player.logoutListeners[key] = handler
}

/**
 * Clears a logout listener for a specific player.
 *
 * @param player The player whose listener will be removed.
 * @param key The unique key identifying the listener to remove.
 */
fun clearLogoutListener(
    player: Player,
    key: String,
) {
    player.logoutListeners.remove(key)
}

/**
 * Clears the player's inventory.
 *
 * @param player The player whose inventory will be cleared.
 */
fun clearInventory(player: Player) {
    player.inventory.clear()
}

/**
 * Clears the player's equipment.
 *
 * @param player The player whose equipment will be cleared.
 */
fun clearEquipment(player: Player) {
    player.equipment.clear()
}

/**
 * Clears both the player's inventory and equipment.
 *
 * @param player The player whose inventory and equipment will be cleared.
 */
fun clearInventoryAndEquipment(player: Player) {
    player.equipment.clear()
    player.inventory.clear()
}

/**
 * Sends an item to the player on a specific interface.
 *
 * @param player The player receiving the item.
 * @param iface The interface ID to show the player.
 * @param child The child component ID of the interface.
 * @param item The item to send.
 * @param amount The amount of the item to send. Defaults to 1.
 */
fun sendItemOnInterface(
    player: Player,
    iface: Int,
    child: Int,
    item: Int,
    amount: Int = 1,
) {
    player.packetDispatch.sendItemOnInterface(item, amount, iface, child)
}

/**
 * Sends a model to the player on a specific interface.
 *
 * @param player The player receiving the model.
 * @param iface The interface ID to show the player.
 * @param child The child component ID of the interface.
 * @param model The model ID to send.
 * @param zoom The zoom level of the model. Defaults to 1.
 */
fun sendModelOnInterface(
    player: Player,
    iface: Int,
    child: Int,
    model: Int,
    zoom: Int = 1,
) {
    player.packetDispatch.sendModelOnInterface(model, iface, child, zoom)
}

/**
 * Sends an angle (pitch, yaw, zoom) to the player on a specific interface.
 *
 * @param player The player receiving the angle data.
 * @param iface The interface ID to show the player.
 * @param child The child component ID of the interface.
 * @param zoom The zoom level.
 * @param pitch The pitch (up/down) angle.
 * @param yaw The yaw (left/right) angle.
 */
fun sendAngleOnInterface(
    player: Player,
    iface: Int,
    child: Int,
    zoom: Int,
    pitch: Int,
    yaw: Int,
) {
    player.packetDispatch.sendAngleOnInterface(iface, child, zoom, pitch, yaw)
}

/**
 * Sends an item dialogue to the player.
 *
 * @param player The player receiving the dialogue.
 * @param item The item involved in the dialogue.
 * @param message The message to display alongside the item.
 */
fun sendItemDialogue(
    player: Player,
    item: Any,
    message: String,
) {
    val dialogueItem =
        when (item) {
            is Item -> item
            is Int -> Item(item)
            else -> {
                throw java.lang.IllegalArgumentException(
                    "Expected an Item or an Int, got ${item::class.java.simpleName}.",
                )
            }
        }
    player.dialogueInterpreter.sendItemMessage(dialogueItem, *splitLines(message))
}

/**
 * Sends a dialogue involving two items to the player.
 *
 * @param player The player receiving the dialogue.
 * @param item1 The first item involved in the dialogue.
 * @param item2 The second item involved in the dialogue.
 * @param message The message to display alongside the items.
 */
fun sendDoubleItemDialogue(
    player: Player,
    item1: Any,
    item2: Any,
    message: String,
) {
    when (item1) {
        is Item -> player.dialogueInterpreter.sendDoubleItemMessage(item1, item2 as Item, message)
        is Int -> player.dialogueInterpreter.sendDoubleItemMessage(item1, item2 as Int, message)
    }
}

/**
 * Sends an input dialogue to the player, allowing numeric or string input.
 *
 * @param player The player receiving the input dialogue.
 * @param numeric If true, the input will be numeric; otherwise, it will accept a string.
 * @param prompt The prompt message to show to the player.
 * @param handler The function to handle the player's input value.
 */
fun sendInputDialogue(
    player: Player,
    numeric: Boolean,
    prompt: String,
    handler: (value: Any) -> Unit,
) {
    if (numeric) {
        sendInputDialogue(player, InputType.NUMERIC, prompt, handler)
    } else {
        sendInputDialogue(player, InputType.STRING_SHORT, prompt, handler)
    }
}

/**
 * Sends an input dialogue to the player, with a specific input type.
 *
 * @param player The player receiving the input dialogue.
 * @param type The type of input (e.g., numeric, string, etc.).
 * @param prompt The prompt message to show to the player.
 * @param handler The function to handle the player's input value.
 */
fun sendInputDialogue(
    player: Player,
    type: InputType,
    prompt: String,
    handler: (value: Any) -> Unit,
) {
    when (type) {
        InputType.AMOUNT -> {
            setAttribute(player, "parseamount", true)
            player.dialogueInterpreter.sendInput(true, prompt)
        }

        InputType.NUMERIC, InputType.STRING_SHORT ->
            player.dialogueInterpreter.sendInput(
                type != InputType.NUMERIC,
                prompt,
            )

        InputType.STRING_LONG -> player.dialogueInterpreter.sendLongInput(prompt)
        InputType.MESSAGE -> player.dialogueInterpreter.sendMessageInput(prompt)
    }

    setAttribute(player, "runscript", handler)
    setAttribute(player, "input-type", type)
}

/**
 * Makes the entity flee from another entity.
 *
 * @param entity The entity that is fleeing.
 * @param from The entity that the first entity is fleeing from.
 */
fun flee(
    entity: Entity,
    from: Entity,
) {
    lock(entity, 5)
    face(entity, from, 5)

    val diffX = entity.location.x - from.location.x
    val diffY = entity.location.y - from.location.y

    forceWalk(entity, entity.location.transform(diffX, diffY, 0), "DUMB")
}

/**
 * Submits a pulse for an entity.
 *
 * @param entity The entity to execute the pulse for.
 * @param pulse The pulse to execute.
 */
fun submitIndividualPulse(
    entity: Entity,
    pulse: Pulse,
) {
    entity.pulseManager.run(pulse)
}

/**
 * Runs a task for an entity with a delay and repeat count.
 *
 * @param entity The entity to run the task for.
 * @param delay The delay before the task is run (in game ticks).
 * @param repeatTimes The number of times to repeat the task.
 * @param task The task to execute.
 */
fun runTask(
    entity: Entity,
    delay: Int = 0,
    repeatTimes: Int = 1,
    task: () -> Unit,
) {
    var cycles = repeatTimes
    entity.pulseManager.run(
        object : Pulse(delay) {
            override fun pulse(): Boolean {
                task.invoke()
                return --cycles <= 0
            }
        },
    )
}

/**
 * Sends an item with a zoom level to the player on a specific interface.
 *
 * @param player The player receiving the item.
 * @param iface The interface ID to show the player.
 * @param child The child component ID of the interface.
 * @param item The item ID to send.
 * @param zoom The zoom level of the item. Defaults to 230.
 */
fun sendItemZoomOnInterface(
    player: Player,
    iface: Int,
    child: Int,
    item: Int,
    zoom: Int = 230,
) {
    player.packetDispatch.sendItemZoomOnInterface(item, zoom, iface, child)
}

/**
 * Retrieves the scenery definition for a given ID.
 *
 * @param id The ID of the scenery.
 * @return The [SceneryDefinition] object corresponding to the ID.
 */
fun sceneryDefinition(id: Int): SceneryDefinition {
    return SceneryDefinition.forId(id)
}

/**
 * Registers a map zone with specified borders.
 *
 * @param zone The map zone to register.
 * @param borders The borders that define the zone.
 */
fun registerMapZone(
    zone: MapZone,
    borders: ZoneBorders,
) {
    ZoneBuilder.configure(zone)
    zone.register(borders)
}

/**
 * Animates a specific interface with a given animation.
 *
 * @param player The player to animate.
 * @param iface The interface ID to animate.
 * @param child The child component ID to animate.
 * @param anim The animation ID to play.
 */
fun animateInterface(
    player: Player,
    iface: Int,
    child: Int,
    anim: Int,
) {
    player.packetDispatch.sendAnimationInterface(anim, iface, child)
}

/**
 * Adds a climb destination for a ladder.
 *
 * @param ladderLoc The location of the ladder.
 * @param dest The destination location after climbing.
 */
fun addClimbDest(
    ladderLoc: Location,
    dest: Location,
) {
    core.game.global.action.SpecialLadder
        .add(ladderLoc, dest)
}

/**
 * Sends a news message globally.
 *
 * @param message The message to send.
 */
fun sendNews(message: String) {
    Repository.sendNews(message, 12, "CC6600")
}

/**
 * Sends graphics to a specified location.
 *
 * @param gfx The graphics to send (either an integer ID or a [Graphics] object).
 * @param location The location where the graphics should be displayed.
 */
fun <G> sendGraphics(
    gfx: G,
    location: Location,
) {
    when (gfx) {
        is Int ->
            Graphics.send(
                Graphics(
                    gfx,
                ),
                location,
            )

        is Graphics -> Graphics.send(gfx, location)
    }
}

/**
 * Executes a run script on the player's client with a given script ID and arguments.
 *
 * @param player The player executing the script.
 * @param scriptId The ID of the script to run.
 * @param arguments The arguments to pass to the script.
 */
fun runcs2(
    player: Player,
    scriptId: Int,
    vararg arguments: Any,
) {
    var typeString = StringBuilder()
    var finalArgs = Array<Any?>(arguments.size) { null }
    try {
        for (i in 0 until arguments.size) {
            val arg = arguments[i]
            if (arg is Int) {
                typeString.append("i")
            } else if (arg is String) {
                typeString.append("s")
            } else {
                throw IllegalArgumentException(
                    "Argument at index $i ($arg) is not an acceptable type! Only string and int are accepted.",
                )
            }
            finalArgs[arguments.size - i - 1] = arg
        }
        player.packetDispatch.sendRunScript(scriptId, typeString.toString(), *finalArgs)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * Sends an item selection interface to the player with the given options.
 *
 * @param player The player receiving the interface.
 * @param options The options to present to the player.
 * @param keepAlive If true, the interface will stay open after the selection.
 * @param callback The callback function to invoke when an option is selected.
 */
@JvmOverloads
fun sendItemSelect(
    player: Player,
    vararg options: String,
    keepAlive: Boolean = false,
    callback: (slot: Int, optionIndex: Int) -> Unit,
) {
    player.interfaceManager.openSingleTab(Component(12))
    val scriptArgs = arrayOf((12 shl 16) + 18, 93, 4, 7, 0, -1, "", "", "", "", "", "", "", "", "")
    for (i in 0 until min(9, options.size)) scriptArgs[6 + i] = options[i]
    runcs2(player, 150, *scriptArgs)
    val settings = IfaceSettingsBuilder().enableOptions(0 until 9).build()
    player.packetDispatch.sendIfaceSettings(settings, 18, 12, 0, 28)
    setAttribute(player, "itemselect-callback", callback)
    setAttribute(player, "itemselect-keepalive", keepAlive)
}

/**
 * Announces a rare item drop for the player, if the item is rare.
 *
 * @param player The player receiving the rare item.
 * @param item The item being dropped.
 */
fun announceIfRare(
    player: Player,
    item: Item,
) {
    if (item.definition.getConfiguration(ItemConfigParser.RARE_ITEM, false)) {
        sendNews("${player.username} has just received: ${item.amount} x ${item.name}.")
        PlayerStatsCounter.incrementRareDrop(player, item)
    }
}

/**
 * Checks if the player has met the requirements for a given quest.
 *
 * @param player The player checking the requirements.
 * @param req The quest requirements to check.
 * @param message If true, messages will be sent to the player about unmet requirements.
 * @return True if the player meets the requirements, false otherwise.
 */
fun hasRequirement(
    player: Player,
    req: QuestReq,
    message: Boolean = true,
): Boolean {
    var (isMet, unmetReqs) = req.evaluate(player)
    val messageList = ArrayList<String>()

    var totalSoftQp = 0
    var totalHardQp = 0

    for (req in unmetReqs) {
        when (req) {
            is QPCumulative -> totalSoftQp += req.amount
            is QPReq -> if (req.amount > totalHardQp) totalHardQp = req.amount
        }
    }

    var neededQp = min(max(totalSoftQp, totalHardQp), player.questRepository.availablePoints)

    isMet = isMet && neededQp <= player.questRepository.points

    if (isMet) return true

    if (unmetReqs.size == 2 && unmetReqs[0] is QuestReq) {
        messageList.add("You must complete ${(unmetReqs[0] as QuestReq).questReq.questName} to use this.")
    } else {
        messageList.add("You need the pre-reqs for ${req.questReq.questName} to access this.")
        messageList.add("Please check the quest journal for more information.")
    }

    if (message) for (message in messageList) sendMessage(player, message)

    return false
}

/**
 * Retrieves the names of skills the player has mastered (level 99).
 *
 * @param player The player whose mastered skills are to be retrieved.
 * @return A list of skill names the player has mastered.
 */
fun getMasteredSkillNames(player: Player): List<String> {
    val hasMastered = player.getSkills().masteredSkills > 0
    val masteredSkills = ArrayList<String>()

    if (hasMastered) {
        for ((skillId, skillName) in Skills.SKILL_NAME.withIndex()) {
            if (hasLevelStat(player, skillId, 99)) {
                masteredSkills.add(skillName)
            }
        }
    }
    return masteredSkills
}

/**
 * Dumps the contents of a container into the player's bank.
 *
 * @param player The player performing the action.
 * @param container The container whose items are being deposited.
 * @return The number of items successfully dumped into the bank.
 */
fun dumpContainer(
    player: Player,
    container: core.game.container.Container,
): Int {
    val bank = player.bank
    var dumpedCount = 0

    run beginDepositing@{
        container.toArray().filterNotNull().forEach { item ->
            if (!bank.hasSpaceFor(item)) {
                val message =
                    if (item.id ==
                        Items.COINS_995
                    ) {
                        "You have ran out of Bank Space. Please make more room."
                    } else {
                        "You have no more space in your bank."
                    }
                sendMessage(player, message)
                return@beginDepositing
            }

            if (!bank.canAdd(item)) {
                sendMessage(player, "A magical force prevents you from banking the ${item.name}.")
                return@forEach
            } else {
                if (container is EquipmentContainer) {
                    if (!InteractionListeners.run(item.id, player, item, false)) {
                        sendMessage(player, "A magical force prevents you from removing your ${item.name}.")
                        return@forEach
                    }
                }

                container.remove(item)
                bank.add(unnote(item), false)
                dumpedCount++
            }
        }
    }

    container.update()
    bank.update()

    return dumpedCount
}

/**
 * Dumps the contents of the player's Beast of Burden familiar into the bank.
 *
 * @param player The player performing the action.
 */
fun dumpBeastOfBurden(player: Player) {
    val famMan = player.familiarManager

    if (!famMan.hasFamiliar()) {
        sendMessage(player, "You don't have a familiar.")
        return
    }

    if (famMan.familiar !is content.global.skill.summoning.familiar.BurdenBeast) {
        sendMessage(player, "Your familiar is not a Beast of Burden.")
        return
    }

    val beast: content.global.skill.summoning.familiar.BurdenBeast =
        (famMan.familiar as content.global.skill.summoning.familiar.BurdenBeast)

    if (beast.container.isEmpty) {
        sendMessage(player, "Your familiar's inventory is empty.")
        return
    }

    val itemCount = beast.container.itemCount()
    val dumpedCount = dumpContainer(player, beast.container)

    when {
        dumpedCount == itemCount -> sendMessage(player, "Your familiar's inventory was deposited into your bank.")
        dumpedCount > 0 -> {
            val remainPhrase =
                when {
                    (itemCount - dumpedCount == 1) -> "item remains"
                    else -> "items remain"
                }

            sendMessage(player, "${itemCount - dumpedCount} $remainPhrase in your familiar's inventory.")
        }
    }
}

/**
 * Retrieves the skill boost provided by the player's familiar for a given skill.
 *
 * @param player The player whose familiar is being checked.
 * @param skill The skill ID to check the boost for.
 * @return The boost provided by the familiar.
 */
fun getFamiliarBoost(
    player: Player,
    skill: Int,
): Int {
    return player.familiarManager.getBoost(skill)
}

/**
 * Converts an item to its noted form, if possible.
 *
 * @param item The item to convert.
 * @return The noted version of the item.
 */
fun note(item: Item): Item {
    if (!item.definition.isUnnoted) return item

    if (item.definition.noteId < 0) return item

    return Item(item.definition.noteId, item.amount, item.charge)
}

/**
 * Converts an item from its noted form to its unnoted version.
 *
 * @param item The item to unnote.
 * @return The unnoted version of the item.
 */
fun unnote(item: Item): Item {
    if (item.definition.isUnnoted) return item

    return Item(item.noteChange, item.amount, item.charge)
}

/**
 * Checks if the player has the Seal of Passage in their inventory or equipment.
 *
 * @param player The player to check.
 * @return True if the player has the Seal of Passage, false otherwise.
 */
fun hasSealOfPassage(player: Player): Boolean {
    return inEquipmentOrInventory(player, Items.SEAL_OF_PASSAGE_9083)
}

/**
 * Checks if the player has a house.
 *
 * @param player The player to check.
 * @return True if the player has a house, false otherwise.
 */
fun hasHouse(player: Player): Boolean {
    return player.houseManager.hasHouse()
}

/**
 * Retrieves the cutscene currently assigned to the player.
 *
 * @param player The player to retrieve the cutscene for.
 * @return The [Cutscene] object assigned to the player, or null if none exists.
 */
fun Player.getCutscene(): Cutscene? {
    return getAttribute<Cutscene?>(this, Cutscene.ATTRIBUTE_CUTSCENE, null)
}

/**
 * Retrieves the current cutscene stage for the player.
 *
 * @return The cutscene stage.
 */
fun Player.getCutsceneStage(): Int {
    return getAttribute(this, Cutscene.ATTRIBUTE_CUTSCENE_STAGE, 0)
}

/**
 * Retrieves the server configuration as a Toml object.
 *
 * @return The server configuration.
 */
fun getServerConfig(): Toml {
    return ServerConfigParser.tomlData ?: Toml()
}

/**
 * Attempts to find a pathable random location within a specified radius around a given center location.
 *
 * @param target The target entity.
 * @param radius The radius within which to search for a pathable location.
 * @param center The center location from where the search will be initiated.
 * @param maxAttempts The maximum number of attempts to find a valid location (default is 3).
 * @return A pathable location if found, otherwise the target's current location.
 */
fun getPathableRandomLocalCoordinate(
    target: Entity,
    radius: Int,
    center: Location,
    maxAttempts: Int = 3,
): Location {
    var maxRadius = Vector.deriveWithEqualComponents(ServerConstants.MAX_PATHFIND_DISTANCE.toDouble()).x - 1
    var effectiveRadius = min(radius, maxRadius.toInt())
    val swCorner = center.transform(-effectiveRadius, -effectiveRadius, center.z)
    val neCorner = center.transform(effectiveRadius, effectiveRadius, center.z)
    val borders = ZoneBorders(swCorner.x, swCorner.y, neCorner.x, neCorner.y, center.z)

    var attempts = maxAttempts
    var success: Boolean
    while (attempts-- > 0) {
        val dest = borders.randomLoc
        val path = Pathfinder.find(center, dest, target.size())
        success = path.isSuccessful && !path.isMoveNear
        if (success) return dest
    }

    return target.location
}

/**
 * Attempts to find a pathable location in a cardinal direction from the given center location.
 *
 * @param target The target entity.
 * @param center The center location to check from.
 * @return A pathable location if found, otherwise the original center location.
 */
fun getPathableCardinal(
    target: Entity,
    center: Location,
): Location {
    var tiles = center.cardinalTiles

    for (tile in tiles) {
        val path = Pathfinder.find(center, tile, target.size())
        if (path.isSuccessful && !path.isMoveNear) return tile
    }

    return center
}

/**
 * Checks if the player has a specific Ironman mode restriction.
 *
 * @param player The player to check.
 * @param restriction The Ironman mode restriction to check for.
 * @return True if the player has the restriction, otherwise false.
 */
fun hasIronmanRestriction(
    player: Player,
    restriction: IronmanMode,
): Boolean {
    return player.ironmanManager.isIronman && player.ironmanManager.mode.ordinal >= restriction.ordinal
}

/**
 * Registers a hint icon for a player at the specified location.
 *
 * @param player The player to register the hint icon for.
 * @param location The location to place the hint icon.
 * @param height The height of the hint icon.
 */
fun registerHintIcon(
    player: Player,
    location: Location,
    height: Int,
) {
    setAttribute(
        player,
        "hinticon",
        HintIconManager.registerHintIcon(player, location, 1, -1, player.hintIconManager.freeSlot(), height, 3),
    )
}

/**
 * Registers a hint icon for a player at the location of a specific node.
 *
 * @param player The player to register the hint icon for.
 * @param node The node to register the hint icon for.
 */
fun registerHintIcon(
    player: Player,
    node: Node,
) {
    if (getAttribute(player, "hinticon", null) != null) return
    setAttribute(player, "hinticon", HintIconManager.registerHintIcon(player, node))
}

/**
 * Clears the hint icon for a player.
 *
 * @param player The player whose hint icon will be cleared.
 */
fun clearHintIcon(player: Player) {
    val slot = getAttribute(player, "hinticon", -1)
    removeAttribute(player, "hinticon")
    HintIconManager.removeHintIcon(player, slot)
}

/**
 * Checks if the player has their hands free (i.e., not equipped with a shield, weapon, or item in the hands slot).
 *
 * @param player The player to check.
 * @return True if the player's hands are free, otherwise false.
 */
fun hasHandsFree(player: Player): Boolean {
    val equipment = player.equipment
    return equipment[EquipmentContainer.SLOT_HANDS] == null &&
        equipment[EquipmentContainer.SLOT_SHIELD] == null &&
        equipment[EquipmentContainer.SLOT_WEAPON] == null
}

/**
 * Retrieves the equipment slot for a specific item, if any.
 *
 * @param item The item ID.
 * @return The corresponding equipment slot, or null if no slot is found.
 */
fun equipSlot(item: Int): EquipmentSlot? {
    return EquipmentSlot.values().getOrNull(itemDefinition(item).getConfiguration(ItemConfigParser.EQUIP_SLOT, -1))
}

/**
 * Checks if the provided node is a player.
 *
 * @param node The node to check.
 * @return True if the node is a player, otherwise false.
 */
fun isPlayer(node: Node): Boolean {
    return (node is Player)
}

/**
 * Adds a dialogue action to a player's dialogue queue.
 *
 * @param player The player to add the dialogue action to.
 * @param action The dialogue action to add.
 */
fun addDialogueAction(
    player: Player,
    action: DialogueAction,
) {
    player.dialogueInterpreter.addAction(action)
}

/**
 * Logs a message with a specific origin and type.
 *
 * @param origin The class from which the log is originating.
 * @param type The type of log (e.g., error, warning).
 * @param message The log message.
 */
fun log(
    origin: Class<*>,
    type: Log,
    message: String,
) {
    SystemLogger.processLogEntry(origin, type, message)
}

/**
 * Logs a message along with the stack trace of the exception.
 *
 * @param origin The class from which the log is originating.
 * @param type The type of log (e.g., error, warning).
 * @param message The log message.
 */
fun logWithStack(
    origin: Class<*>,
    type: Log,
    message: String,
) {
    try {
        throw Exception(message)
    } catch (e: Exception) {
        log(origin, type, "${exceptionToString(e)}")
    }
}

/**
 * Converts an exception to a string format including the stack trace.
 *
 * @param e The exception to convert.
 * @return The string representation of the exception with the stack trace.
 */
fun exceptionToString(e: Exception): String {
    val sw = StringWriter()
    val pw = PrintWriter(sw)
    e.printStackTrace(pw)
    return sw.toString()
}

/**
 * Delays the script execution for an entity by a specified number of ticks.
 *
 * @param entity The entity whose script is to be delayed.
 * @param ticks The number of ticks to delay.
 * @return False after delaying the script.
 */
fun delayScript(
    entity: Entity,
    ticks: Int,
): Boolean {
    entity.scripts.getActiveScript()?.let { it.nextExecution = GameWorld.ticks + ticks }
    return false
}

/**
 * Delays the entity's script execution by a specified number of ticks.
 *
 * @param entity The entity whose script will be delayed.
 * @param ticks The number of ticks to delay.
 */
fun delayEntity(
    entity: Entity,
    ticks: Int,
) {
    entity.scripts.delay = GameWorld.ticks + ticks
    lock(entity, ticks)
}

/**
 * Sets the action points range for the entity.
 *
 * @param entity The entity to modify.
 * @param apRange The range of action points to set.
 */
fun apRange(
    entity: Entity,
    apRange: Int,
) {
    entity.scripts.apRange = apRange
    entity.scripts.apRangeCalled = true
}

/**
 * Checks if an entity has line of sight to a target node.
 *
 * @param entity The entity to check.
 * @param target The target node.
 * @return True if the entity has line of sight to the target, otherwise false.
 */
fun hasLineOfSight(
    entity: Entity,
    target: Node,
): Boolean {
    return CombatSwingHandler.isProjectileClipped(entity, target, false)
}

/**
 * Checks if the animation for an entity has finished.
 *
 * @param entity The entity to check.
 * @return True if the animation has finished, otherwise false.
 */
fun animationFinished(entity: Entity): Boolean {
    return entity.clocks[Clocks.ANIMATION_END] < GameWorld.ticks
}

/**
 * Clears all scripts associated with an entity.
 *
 * @param entity The entity to clear scripts for.
 * @return True after clearing the scripts.
 */
fun clearScripts(entity: Entity): Boolean {
    entity.scripts.reset()
    return true
}

/**
 * Restarts the active script of an entity if it is persistent.
 *
 * @param entity The entity whose script should be restarted.
 * @return True if the script was restarted, otherwise false.
 */
fun restartScript(entity: Entity): Boolean {
    if (entity.scripts.getActiveScript()?.persist != true) {
        log(
            entity.scripts.getActiveScript()!!::class.java,
            Log.ERR,
            "Tried to call restartScript on a non-persistent script! Either use stopExecuting() or make the script persistent.",
        )
        return clearScripts(entity)
    }
    return true
}

/**
 * Ensures that the entity's script continues to run by setting the next execution tick.
 *
 * @param entity The entity whose script should keep running.
 * @return False after ensuring the script keeps running.
 */
fun keepRunning(entity: Entity): Boolean {
    entity.scripts.getActiveScript()?.nextExecution = getWorldTicks() + 1
    return false
}

/**
 * Stops the execution of an entity's script.
 *
 * @param entity The entity whose script should be stopped.
 * @return True if the script was stopped, otherwise false.
 */
fun stopExecuting(entity: Entity): Boolean {
    if (entity.scripts.getActiveScript()?.persist == true) {
        log(
            entity.scripts.getActiveScript()!!::class.java,
            Log.ERR,
            "Tried to call stopExecuting() on a persistent script! To halt execution of a persistent script, you MUST call clearScripts()!",
        )
        return clearScripts(entity)
    }
    return true
}

/**
 * Queues a script for execution with optional delay, strength, and persistence.
 *
 * @param entity The entity to queue the script for.
 * @param delay The number of ticks to delay before execution.
 * @param strength The strength of the queue (e.g., weak or strong).
 * @param persist Whether the script should persist across executions.
 * @param script The script to execute, which takes an integer stage.
 */
fun queueScript(
    entity: Entity,
    delay: Int = 1,
    strength: QueueStrength = QueueStrength.WEAK,
    persist: Boolean = false,
    script: (stage: Int) -> Boolean,
) {
    val s = QueuedScript(script, strength, persist)
    s.nextExecution = getWorldTicks() + delay
    entity.scripts.addToQueue(s, strength)
}

/**
 * Stuns an entity for a specified number of ticks.
 *
 * @param entity The entity to stun.
 * @param ticks The number of ticks to stun the entity.
 */
fun stun(
    entity: Entity,
    ticks: Int,
) {
    stun(entity, ticks, true)
}

/**
 * Stuns an entity for a specified number of ticks with an optional message.
 *
 * @param entity The entity to stun.
 * @param ticks The number of ticks to stun the entity.
 * @param sendMessage Whether to send a stun message to the player.
 */
fun stun(
    entity: Entity,
    ticks: Int,
    sendMessage: Boolean,
) {
    entity.walkingQueue.reset()
    entity.pulseManager.clear()
    entity.locks.lockMovement(ticks)
    entity.clocks[Clocks.STUN] = getWorldTicks() + ticks
    entity.graphics(Graphics(80, 96))
    if (entity is Player) {
        playAudio(entity.asPlayer(), Sounds.STUNNED_2727)
        entity.animate(Animation(424, Animator.Priority.VERY_HIGH))
        if (sendMessage) {
            sendMessage(entity, "You have been stunned!")
        }
    }
}

/**
 * Sets the current script state for an entity.
 *
 * @param entity The entity whose script state should be set.
 * @param state The new script state.
 */
fun setCurrentScriptState(
    entity: Entity,
    state: Int,
) {
    val script = entity.scripts.getActiveScript()
    if (script == null) {
        log(ContentAPI::class.java, Log.WARN, "Tried to set a script state when no script was being ran!")
        if (GameWorld.settings?.isDevMode != true) return
        throw IllegalStateException("Script execution mistake - check stack trace and above warning log!")
    }
    script.state =
        state - 1
}

/**
 * Modifies the prayer points of a player.
 *
 * @param player The player to modify prayer points for.
 * @param amount The amount to modify prayer points by (can be positive or negative).
 */
fun modPrayerPoints(
    player: Player,
    amount: Double,
) {
    if (amount > 0) {
        player.skills.incrementPrayerPoints(amount)
    } else if (amount < 0) {
        player.skills.decrementPrayerPoints(amount.absoluteValue)
    } else {
        return
    }
}

/**
 * Checks if a player's achievement diary is complete for a specific type and level.
 *
 * @param player The player to check.
 * @param type The type of the diary (e.g., a specific region).
 * @param level The level of the diary to check.
 * @return True if the diary is complete, otherwise false.
 */
fun isDiaryComplete(
    player: Player,
    type: DiaryType,
    level: Int,
): Boolean {
    return player.achievementDiaryManager.getDiary(type)!!.isComplete(level)
}

/**
 * Checks if a specific diary task is completed for a player.
 *
 * @param player The player to check.
 * @param type The type of the diary (e.g., a specific region).
 * @param level The level of the diary.
 * @param index The index of the task to check.
 * @return True if the task is completed, otherwise false.
 */
fun hasDiaryTaskComplete(
    player: Player,
    type: DiaryType,
    level: Int,
    index: Int,
): Boolean {
    return player.achievementDiaryManager.hasCompletedTask(type, level, index)
}

/**
 * Checks if an entity is within a specified distance from another location.
 *
 * @param entity The entity to check.
 * @param other The location to check distance to.
 * @param distance The distance to check (optional, defaults to the entity's distance).
 * @return True if within the specified distance, otherwise false.
 */
fun withinDistance(
    entity: Entity,
    other: Location,
    distance: Int? = null,
): Boolean {
    if (distance != null) {
        entity.location.withinDistance(other, distance)
    } else {
        entity.location.withinDistance(other)
    }

    return true
}

/**
 * Completes a specific diary task for a player.
 *
 * @param player The player to complete the task for.
 * @param type The type of the diary.
 * @param level The level of the diary.
 * @param task The task to complete.
 */
fun finishDiaryTask(
    player: Player,
    type: DiaryType,
    level: Int,
    task: Int,
) {
    player.achievementDiaryManager.finishTask(player, type, level, task)
}

/**
 * Checks if a specific prayer is active for a player.
 *
 * @param player The player to check.
 * @param type The prayer type to check.
 * @return True if the prayer is active, otherwise false.
 */
fun isPrayerActive(
    player: Player,
    type: PrayerType,
): Boolean {
    return player.prayer.active.contains(type)
}

/**
 * Removes one or more interface tabs from a player's interface.
 *
 * @param player The player to remove the tabs from.
 * @param tabs The tabs to remove.
 */
fun removeTabs(
    player: Player,
    vararg tabs: Int,
) {
    return player.interfaceManager.removeTabs(*tabs)
}

/**
 * Resets a player's camera view.
 *
 * @param player The player whose camera should be reset.
 */
fun resetCamera(player: Player) {
    try {
        PlayerCamera(player).reset()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * Refreshes a player's inventory to reflect the current state.
 *
 * @param player The player whose inventory will be refreshed.
 */
fun refreshInventory(player: Player) {
    player.inventory.refresh()
}

private class ContentAPI
