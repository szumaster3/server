package content.region.kandarin.handlers.guthanoth

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

class GutanothChestListener : InteractionListener {

    override fun defineListeners() {
        on(CHEST, IntType.SCENERY, "open") { player, node ->
            val delay = getAttribute(player, "gutanoth-chest-delay", 0L)
            GameWorld.Pulser.submit(ChestPulse(player, System.currentTimeMillis() > delay, node as Scenery))
            return@on true
        }
    }

    class ChestPulse(
        val player: Player,
        val isLoot: Boolean,
        val chest: Scenery,
    ) : Pulse() {
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

        private fun lootChest(player: Player) {
            if (isLoot) {
                setAttribute(
                    player,
                    "/save:gutanoth-chest-delay",
                    System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(15),
                )
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

        enum class Rewards(
            val id: Int,
            val type: Type,
            val message: String,
        ) {
            BONES(id = Items.BONES_2530, type = Type.ITEM, message = "Oh! Some bones. Delightful."),
            EMERALD(id = Items.EMERALD_1605, type = Type.ITEM, message = "Ooh! A lovely emerald!"),
            ROTTEN_APPLE(id = Items.ROTTEN_APPLE_1984, type = Type.ITEM, message = "Oh, joy, spoiled fruit! My favorite!"),
            CHAOS_DWARF(id = NPCs.CHAOS_DWARF_119, type = Type.NPC, message = "You've gotta be kidding me, a dwarf?!"),
            RAT(id = NPCs.RAT_47, type = Type.NPC, message = "Eek!"),
            SCORPION(id = NPCs.SCORPION_1477, type = Type.NPC, message = "Zoinks!"),
            SPIDER(id = NPCs.SPIDER_1004, type = Type.NPC, message = "Awh, a cute lil spidey!"),
        }

        enum class Type {
            ITEM,
            NPC,
        }
    }
}
