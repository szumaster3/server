package content.global.skill.prayer

import core.api.*
import core.api.skill.clockReady
import core.api.skill.delayClock
import core.game.event.BoneBuryEvent
import core.game.interaction.Clocks
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Animations
import org.rs.consts.Sounds

class BonesBuryListener : InteractionListener {
    private val bones = Bones.values().map { it.itemId }.toIntArray()

    override fun defineListeners() {
        on(bones, IntType.ITEM, "bury") { player, node ->
            val boneType = Bones.values().find { it.itemId == node.id } ?: return@on true

            if (!clockReady(player, Clocks.SKILLING)) return@on true
            if (!inInventory(player, node.id)) return@on true

            stopWalk(player)
            lock(player, 2)
            delayClock(player, Clocks.SKILLING, 2)
            sendMessage(player, "You dig a hole in the ground.")
            animate(player, Animations.HUMAN_BURYING_BONES_827)
            playAudio(player, Sounds.BONES_DOWN_2738)

            queueScript(player, 1, QueueStrength.STRONG) {
                if (removeBones(player, node.asItem())) {
                    sendMessage(player, "You bury the bones.")
                    rewardXP(player, Skills.PRAYER, boneType.experience)
                    player.dispatch(BoneBuryEvent(boneType.itemId))
                }
                return@queueScript stopExecuting(player)
            }

            return@on true
        }
    }

    private fun removeBones(
        player: Player,
        item: Item,
    ): Boolean {
        val removedItem = replaceSlot(player, item.slot, Item())
        return removedItem == item && removedItem.slot == item.slot
    }
}
