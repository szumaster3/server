package content.region.feldip_hills.gutanoth.plugin

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.GameWorld
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.NPCs
import java.util.concurrent.TimeUnit

private const val CHEST = org.rs.consts.Scenery.CHEST_2827
/**
 * Handles interactions with the Gutanoth chest.
 */
class GutanothChestPlugin : InteractionListener {

    override fun defineListeners() {
        on(CHEST, IntType.SCENERY, "open") { player, node ->
            val delay = getAttribute(player, "gutanoth-chest-delay", 0L)
            GameWorld.Pulser.submit(ChestPulse(player, System.currentTimeMillis() > delay, node as Scenery))
            return@on true
        }
    }
}

/**
 * Handles the chest opening pulse.
 */
private class ChestPulse(
    val player: Player,
    val isLooted: Boolean,
    val chest: Scenery,
) : Pulse() {

    private var ticks = 0

    override fun pulse(): Boolean {
        when (ticks++) {
            0 -> {
                lock(player, 3)
                animate(player, Animations.OPEN_CHEST_536)
                SceneryBuilder.replace(
                    chest,
                    Scenery(2828, chest.location, chest.rotation),
                    5
                )
            }
            3 -> {
                lootChest()
                return true
            }
        }
        return false
    }

    /**
     * Gives loot to the player or spawns an NPC as a reward.
     */
    private fun lootChest() {
        if (isLooted) {
            sendMessage(player, "You have already searched this chest.")
            setAttribute(player, "/save:gutanoth-chest-delay", System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(15))
        } else {
            sendMessage(player, "You open the chest and find nothing.")
            return
        }

        val reward = Rewards.values().random()
        player.sendChat(reward.message)

        when (reward.type) {
            RewardType.ITEM -> {
                if (!player.inventory.add(Item(reward.id))) {
                    GroundItemManager.create(
                        Item(reward.id),
                        player.location
                    )
                }
            }
            RewardType.NPC -> {
                val npc = NPC(reward.id).apply {
                    location = player.location
                    isAggressive = true
                    isRespawn = false
                    properties.combatPulse.attack(player)
                    init()
                }
            }
        }
    }
}

/**
 * Represents the possible rewards from the Gutanoth chest.
 */
private enum class Rewards(val id: Int, val type: RewardType, val message: String) {
    BONES(Items.BONES_2530, RewardType.ITEM, "Oh! Some bones. Delightful."),
    EMERALD(Items.EMERALD_1605, RewardType.ITEM, "Ooh! A lovely emerald!"),
    ROTTEN_APPLE(Items.ROTTEN_APPLE_1984, RewardType.ITEM, "Oh, joy, spoiled fruit! My favorite!"),
    CHAOS_DWARF(NPCs.CHAOS_DWARF_119, RewardType.NPC, "You've gotta be kidding me, a dwarf?!"),
    RAT(NPCs.RAT_47, RewardType.NPC, "Eek!"),
    SCORPION(NPCs.SCORPION_1477, RewardType.NPC, "Zoinks!"),
    SPIDER(NPCs.SPIDER_1004, RewardType.NPC, "Awh, a cute lil spidey!")
}

/**
 * Types of rewards possible from the chest.
 */
private enum class RewardType {
    ITEM,
    NPC
}