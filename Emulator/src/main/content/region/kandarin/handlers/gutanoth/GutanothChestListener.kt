package content.region.kandarin.handlers.gutanoth

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
class GutanothChestListener : InteractionListener {
    override fun defineListeners() {
        on(CHEST, IntType.SCENERY, "open") { player, node ->
            val delay = getAttribute(player, "gutanoth-chest-delay", 0L)
            GameWorld.Pulser.submit(ChestPulse(player, System.currentTimeMillis() > delay, node as Scenery))
            return@on true
        }
    }

    /**
     * Handles the chest opening.
     *
     * @property player the player.
     * @property isLooted whether the chest currently contains loot (based on cooldown)
     * @property chest the scenery object representing the chest
     */
    class ChestPulse(val player: Player, val isLooted: Boolean, val chest: Scenery, ) : Pulse() {
        var ticks = 0

        override fun pulse(): Boolean {
            when (ticks++) {
                0 -> {
                    lock(player, 3)
                    animate(player, Animations.OPEN_CHEST_536)
                    SceneryBuilder.replace(
                        chest,
                        Scenery(2828, chest.location, chest.rotation),
                        5,
                    )
                }

                3 -> {
                    lootChest(player)
                    return true
                }
            }
            return false
        }

        /**
         * Gives loot to the player or spawns an NPC as a reward.
         *
         * @param player the player to receive the reward
         */
        private fun lootChest(player: Player) {
            if (isLooted) {
                setAttribute(
                    player,
                    "/save:gutanoth-chest-delay",
                    System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(15),
                )
                sendMessage(player, "You have already searched this chest.")
            } else {
                sendMessage(player, "You open the chest and find nothing.")
                return
            }
            val reward = Rewards.values().random()
            player.sendChat(reward.message)
            when (reward.type) {
                Type.ITEM ->
                    if (!player.inventory.add(Item(reward.id))) {
                        GroundItemManager.create(
                            Item(reward.id),
                            player.location,
                        )
                    }

                Type.NPC -> {
                    val npc = NPC(reward.id)
                    npc.location = player.location
                    npc.isAggressive = true
                    npc.isRespawn = false
                    npc.properties.combatPulse.attack(player)
                    npc.init()
                }
            }
        }

        /**
         * Represents the rewards from the Gutanoth chest.
         *
         * @property id the item id.
         * @property type the type of reward.
         * @property message the message.
         */
        enum class Rewards(val id: Int, val type: Type, val message: String, ) {
            BONES(id = Items.BONES_2530, type = Type.ITEM, message = "Oh! Some bones. Delightful."),
            EMERALD(id = Items.EMERALD_1605, type = Type.ITEM, message = "Ooh! A lovely emerald!"),
            ROTTEN_APPLE(id = Items.ROTTEN_APPLE_1984, type = Type.ITEM, message = "Oh, joy, spoiled fruit! My favorite!"),
            CHAOS_DWARF(id = NPCs.CHAOS_DWARF_119, type = Type.NPC, message = "You've gotta be kidding me, a dwarf?!"),
            RAT(id = NPCs.RAT_47, type = Type.NPC, message = "Eek!"),
            SCORPION(id = NPCs.SCORPION_1477, type = Type.NPC, message = "Zoinks!"),
            SPIDER(id = NPCs.SPIDER_1004, type = Type.NPC, message = "Awh, a cute lil spidey!"),
        }

        /**
         * Types of rewards possible from the chest.
         */
        enum class Type {
            ITEM,
            NPC,
        }
    }
}
