package content.minigame.gnomecooking.plugin

import core.api.*
import core.cache.def.impl.ItemDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import shared.consts.Items

private val gnomeItems = arrayOf(
    Items.FRUIT_BATTA_2277,
    Items.TOAD_BATTA_2255,
    Items.CHEESE_PLUSTOM_BATTA_2259,
    Items.WORM_BATTA_2253,
    Items.VEGETABLE_BATTA_2281,
    Items.CHOCOLATE_BOMB_2185,
    Items.VEG_BALL_2195,
    Items.TANGLED_TOADS_LEGS_2187,
    Items.WORM_HOLE_2191,
    Items.TOAD_CRUNCHIES_2217,
    Items.WORM_CRUNCHIES_2205,
    Items.CHOCCHIP_CRUNCHIES_2209,
    Items.SPICY_CRUNCHIES_2213
)

@Initializable
class GCRewardTokenHandler : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        val def = ItemDefinition.forId(Items.REWARD_TOKEN_9474)
        def.handlers["option:check"] = this
        def.handlers["option:activate"] = this
        return this
    }

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        player ?: return false
        node ?: return false
        option ?: return false

        when (option) {
            "check" -> {
                val charges = getAttribute(player, "$GC_BASE_ATTRIBUTE:$GC_REDEEMABLE_FOOD", 0)
                sendDialogue(player, "You have $charges redeemable charges.")
            }

            "activate" -> {
                player.dialogueInterpreter.open(939382893)
            }
        }
        return true
    }

    @Initializable
    class RewardTokenActivationDialogue(player: Player? = null) : core.game.dialogue.Dialogue(player) {
        override fun newInstance(player: Player?): core.game.dialogue.Dialogue {
            return RewardTokenActivationDialogue(player)
        }

        override fun open(vararg args: Any?): Boolean {
            sendOptions(player, "How many charges?", "1", "5", "10")
            stage = 0
            return true
        }

        override fun handle(interfaceId: Int, buttonId: Int): Boolean {
            when (stage) {
                0 -> end().also {
                    when (buttonId) {
                        1 -> sendCharges(1, player)
                        2 -> sendCharges(5, player)
                        3 -> sendCharges(10, player)
                    }
                }
            }
            return true
        }

        private fun sendCharges(amount: Int, player: Player) {
            val playerCharges = getAttribute(player, "$GC_BASE_ATTRIBUTE:$GC_REDEEMABLE_FOOD", 0)
            if (playerCharges < amount) {
                sendDialogue(player, "You don't have that many charges.")
                return
            }

            if (freeSlots(player) < amount) {
                sendDialogue(player, "You don't have enough space in your inventory.")
                return
            }

            val itemList = ArrayList<Item>()

            for (charge in 0 until amount) {
                itemList.add(Item(gnomeItems.random()))
            }

            sendDialogue(player, "You put in for delivery of $amount items. Wait a bit...")
            GameWorld.Pulser.submit(DeliveryPulse(player, itemList))
            setAttribute(player, "/save:$GC_BASE_ATTRIBUTE:$GC_REDEEMABLE_FOOD", playerCharges - amount)
        }

        class DeliveryPulse(val player: Player, val items: ArrayList<Item>) : Pulse(RandomFunction.random(15, 30)) {
            override fun pulse(): Boolean {
                player.inventory.add(*items.toTypedArray())
                sendDialogue(player, "Your food delivery has arrived!")
                return true
            }
        }

        override fun getIds(): IntArray {
            return intArrayOf(939382893)
        }

    }

}