package content.minigame.fishingtrawler.plugin

import core.api.ContainerListener
import core.game.container.Container
import core.game.container.ContainerEvent
import core.game.container.ContainerType
import core.game.container.SortType
import core.game.container.access.InterfaceContainer
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.net.packet.PacketRepository
import core.net.packet.context.ContainerContext
import core.net.packet.out.ContainerPacket
import shared.consts.Components

/**
 * Represents the Fishing Trawler reward container.
 */
class FishingTrawlerContainer(val player: Player) :
    Container(CONTAINER_SIZE, ContainerType.ALWAYS_STACK, SortType.ID) {

    var isClosed = false
    var rewardListener: RewardListener? = null

    init {
        listeners.add(RewardListener().also { rewardListener = it })
    }

    /**
     * Opens the reward interface
     */
    fun open() {
        val rolls = player.getAttribute<Int>("/save:ft-rolls") ?: 0
        if (rolls > 0) {
            val fishingLevel = player.skills.getLevel(core.game.node.entity.skill.Skills.FISHING)
            val exp = (((0.015 * fishingLevel)) * fishingLevel) * rolls
            player.skills.addExperience(core.game.node.entity.skill.Skills.FISHING, exp)
            val loot = TrawlerLoot.getLoot(fishingLevel, rolls, skipJunk = false)
            for (item in loot) {
                add(item)
            }
            player.removeAttribute("ft-rolls")
        }

        player.setAttribute("ft-container", this)
        player.interfaceManager.openComponent(INTERFACE_ID)
        rewardListener?.update(this, null)
        rewardListener?.refresh(this)
    }

    /**
     * Withdraw item from the container.
     *
     * @param slot The slot index.
     * @param amount The item amount.
     */
    fun withdraw(slot: Int, amount: Int) {
        val item = get(slot) ?: return
        var take = amount.coerceAtMost(item.amount)
        if (take < 1) return

        if (item.definition.isStackable()) {
            val freeSlots = player.inventory.freeSlots()
            val add = if (player.inventory.contains(item.id, item.amount)) take else take.coerceAtMost(freeSlots)
            if (add > 0) {
                val removing = Item(item.id, add)
                if (remove(removing, slot, true)) {
                    if (!player.inventory.add(removing)) {
                        add(removing)
                    }
                }
            }
            if (add < take) {
                player.sendMessage("You don't have enough inventory space for all of that.")
            }
        } else {
            var remaining = take
            val freeSlots = player.inventory.freeSlots()
            val canTake = remaining.coerceAtMost(freeSlots)
            repeat(canTake) {
                val removing = Item(item.id, 1)
                if (remove(removing, slot, true)) {
                    player.inventory.add(removing)
                }
                remaining--
            }
            if (remaining > 0) {
                player.sendMessage("You don't have enough inventory space for that.")
            }
        }

        shift()
        rewardListener?.update(this, null)
        rewardListener?.refresh(this)
    }

    /**
     * Closes the container.
     */
    fun close() {
        if (isClosed) return
        isClosed = true
        dropAllRemainingItems()
        clear()
        rewardListener?.update(this, null)
        rewardListener?.refresh(this)
    }

    /**
     * Handles drop for all remaining items on the ground.
     */
    private fun dropAllRemainingItems() {
        for (slot in 0 until CONTAINER_SIZE) {
            val item = get(slot) ?: continue

            if (item.definition.isStackable()) {
                GroundItemManager.create(item, player.location, player)
            } else {
                repeat(item.amount) {
                    GroundItemManager.create(Item(item.id, 1), player.location, player)
                }
            }

            remove(item, slot, true)
        }
    }

    inner class RewardListener : ContainerListener {
        override fun update(c: Container?, event: ContainerEvent?) {
            InterfaceContainer.generateItems(
                player,
                c!!.toList(),
                WITHDRAW_OPTIONS,
                INTERFACE_ID,
                CONTAINER_CHILD,
                8,
                5,
                75
            )
        }

        override fun refresh(c: Container?) {
            PacketRepository.send(
                ContainerPacket::class.java,
                ContainerContext(player, INTERFACE_ID, CONTAINER_CHILD, -1, c!!.toArray(), false)
            )
        }
    }

    companion object {
        /**
         * The available options.
         */
        private val WITHDRAW_OPTIONS = listOf("Withdraw 1", "Withdraw All")

        /**
         * The fishing trawler reward interface id.
         */
        const val INTERFACE_ID = Components.TRAWLER_REWARD_367

        /**
         * The container id.
         */
        const val CONTAINER_CHILD = 75

        /**
         * The size of the container.
         */
        const val CONTAINER_SIZE = 40
    }
}
