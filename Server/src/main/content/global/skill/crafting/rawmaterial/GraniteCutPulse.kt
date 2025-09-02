package content.global.skill.crafting.rawmaterial

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.item.Item
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Sounds

class GraniteCutPulse(player: Player, node: Item, var amount: Int) : SkillPulse<Item>(player, node) {

    companion object {
        private val GRANITE = intArrayOf(Items.GRANITE_2KG_6981, Items.GRANITE_5KG_6983)
        private val SLOTS = 1..4
    }

    private var ticks = 0
    init { resetAnimation = false }

    override fun checkRequirements(): Boolean {
        if (!inInventory(player, Items.CHISEL_1755)) return false
        if (!anyInInventory(player, *GRANITE)) {
            sendMessage(player, "You have ran out of granite.")
            return false
        }
        val requiredSlots = SLOTS.last
        if (freeSlots(player) < requiredSlots) {
            sendDialogue(player, "You'll need $requiredSlots empty inventory spaces to hold the granite.")
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
        if (++ticks % 2 != 0) return false
        if (amount < 1) return true

        if (removeItem(player, Item(node.id, 1))) {
            when (node.id) {
                Items.GRANITE_5KG_6983 -> {
                    addItem(player, Items.GRANITE_2KG_6981, 2)
                    addItem(player, Items.GRANITE_500G_6979, 2)
                }
                else -> addItem(player, Items.GRANITE_500G_6979, 4)
            }
        }
        amount--
        return amount < 1
    }
}