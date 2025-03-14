package content.global.skill.crafting.rawmaterial

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.item.Item
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Sounds

class GraniteCuttingPulse(
    player: Player,
    node: Item,
    var amount: Int,
) : SkillPulse<Item>(player, node) {
    companion object {
        val GRANITE = intArrayOf(Items.GRANITE_2KG_6981, Items.GRANITE_5KG_6983)
        val SLOTS = intArrayOf(1, 2, 3, 4)
    }

    private var ticks: Int = 0

    init {
        this.resetAnimation = false
    }

    override fun checkRequirements(): Boolean {
        if (!inInventory(player, Items.CHISEL_1755)) {
            return false
        }

        for (i in SLOTS.indices) {
            if (freeSlots(player) < i) {
                sendDialogue(
                    player,
                    "You'll need ${4 - i} empty inventory ${if (freeSlots(
                            player,
                        ) <= 1
                    ) {
                        "spaces"
                    } else {
                        "space"
                    }} to hold the granite once you've split this block.",
                )
                return false
            }
        }

        if (!anyInInventory(player, *GRANITE)) {
            sendMessage(player, "You have ran out of granite.")
            return false
        }
        return true
    }

    override fun animate() {
        if (ticks % 5 == 0 || ticks < 1) {
            playAudio(player, Sounds.CHISEL_2586)
            animate(player, Animations.HUMAN_CHISEL_GRANITE_11146)
        }
    }

    override fun reward(): Boolean {
        if (++ticks % 2 != 0) {
            return false
        }
        if (amount < 1) {
            return true
        }
        if (removeItem(player, Item(node.id, 1), Container.INVENTORY)) {
            if (node.id != Items.GRANITE_5KG_6983) {
                addItem(player, Items.GRANITE_500G_6979, 4, Container.INVENTORY)
            } else {
                addItem(player, Items.GRANITE_2KG_6981, 2, Container.INVENTORY)
                addItem(player, Items.GRANITE_500G_6979, 2, Container.INVENTORY)
            }
        }
        amount--
        return amount < 1
    }
}
