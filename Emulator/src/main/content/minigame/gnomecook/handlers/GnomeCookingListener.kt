package content.minigame.gnomecook.handlers

import core.api.*
import core.api.interaction.getNPCName
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery

class GnomeCookingListener : InteractionListener {

    private val sceneryIDs = intArrayOf(Scenery.GNOME_COOKER_17131, Scenery.RANGE_2728)

    // Crunchy cooking process items.

    private val halfCrunchy = intArrayOf(
        Items.HALF_MADE_CRUNCHY_9577,
        Items.HALF_MADE_CRUNCHY_9579,
        Items.HALF_MADE_CRUNCHY_9581,
        Items.HALF_MADE_CRUNCHY_9583,
        Items.RAW_CRUNCHIES_2202
    )

    // Gnome cooking recipe ingredients.

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
        Items.SPICY_CRUNCHIES_2213,
    )

    override fun defineListeners() {

        /*
         * Handles all interfaces.
         */

        val preparationItems = mapOf(Items.HALF_BAKED_BATTA_2249 to 434, Items.HALF_BAKED_BOWL_2177 to 435, Items.HALF_BAKED_CRUNCHY_2201 to 437)
        preparationItems.forEach { (item, interfaceId) ->
            on(item, IntType.ITEM, "prepare") { player, _ ->
                openInterface(player, interfaceId)
                return@on true
            }
        }

        // Map of raw and cooked items for the cooking process.
        val cookingMap = mapOf(
            Items.RAW_BATTA_2250 to Items.HALF_BAKED_BATTA_2249,
            Items.HALF_MADE_BATTA_9478 to Items.UNFINISHED_BATTA_9479,
            Items.HALF_MADE_BATTA_9480 to Items.UNFINISHED_BATTA_9481,
            Items.HALF_MADE_BATTA_9483 to Items.UNFINISHED_BATTA_9484,
            Items.HALF_MADE_BATTA_9485 to Items.UNFINISHED_BATTA_9486,
            Items.HALF_MADE_BATTA_9482 to Items.TOAD_BATTA_2255,

            Items.HALF_MADE_CRUNCHY_9577 to Items.UNFINISHED_CRUNCHY_9578,
            Items.HALF_MADE_CRUNCHY_9579 to Items.UNFINISHED_CRUNCHY_9580,
            Items.HALF_MADE_CRUNCHY_9581 to Items.UNFINISHED_CRUNCHY_9582,
            Items.HALF_MADE_CRUNCHY_9583 to Items.UNFINISHED_CRUNCHY_9584,
            Items.RAW_CRUNCHIES_2202 to Items.HALF_BAKED_CRUNCHY_2201
        )

        val garnishMap = mapOf(
            Pair(Items.UNFINISHED_BATTA_9486, Items.EQUA_LEAVES_2128) to Items.WORM_BATTA_2253,
            Pair(Items.UNFINISHED_BATTA_9484, Items.EQUA_LEAVES_2128) to Items.VEGETABLE_BATTA_2281,
            Pair(Items.UNFINISHED_BATTA_9479, Items.EQUA_LEAVES_2128) to Items.CHEESE_PLUSTOM_BATTA_2259,
            Pair(Items.UNFINISHED_BATTA_9481, Items.GNOME_SPICE_2169) to Items.FRUIT_BATTA_2277,

            Pair(Items.UNFINISHED_CRUNCHY_9578, Items.CHOCOLATE_DUST_1975) to Items.CHOCCHIP_CRUNCHIES_2209,
            Pair(Items.UNFINISHED_CRUNCHY_9584, Items.GNOME_SPICE_2169) to Items.WORM_CRUNCHIES_2205,
            Pair(Items.UNFINISHED_CRUNCHY_9580, Items.GNOME_SPICE_2169) to Items.SPICY_CRUNCHIES_2213,
            Pair(Items.UNFINISHED_CRUNCHY_9582, Items.EQUA_LEAVES_2128) to Items.TOAD_CRUNCHIES_2217
        )

        /*
         * Handles cooking raw items and turning them into cooked items.
         */

        cookingMap.forEach { (raw, cooked) ->
            onUseWith(IntType.SCENERY, raw, *sceneryIDs) { player, used, _ ->
                if (!inInventory(player, used.id)) return@onUseWith false
                lock(player, 2)
                animate(player, Animations.HUMAN_MAKE_PIZZA_883)

                queueScript(player, 2, QueueStrength.SOFT) {
                    removeItem(player, used)
                    addItem(player, cooked, 1)
                    rewardXP(player, Skills.COOKING, 40.0)
                    if (cooked == Items.TOAD_BATTA_2255) {
                        rewardXP(player, Skills.COOKING, 82.0)
                    }
                    return@queueScript stopExecuting(player)
                }
                return@onUseWith true
            }
        }

        /*
         * Handles combining garnish items.
         */

        garnishMap.forEach { (pair, result) ->
            onUseWith(IntType.ITEM, pair.first, pair.second) { player, used, with ->
                if(removeItem(player, used) && removeItem(player, with)) {
                    addItem(player, result, 1)
                    rewardXP(player, Skills.COOKING, 88.0)
                }
                return@onUseWith true
            }
        }

        /*
         * Listens for crunchy items being cooked in the scenery.
         */

        onUseWith(IntType.SCENERY, halfCrunchy, *sceneryIDs) { player, used, _ ->
            if (!inInventory(player, used.id)) return@onUseWith false
            lock(player, 2)
            animate(player, Animations.HUMAN_MAKE_PIZZA_883)

            queueScript(player, 2, QueueStrength.SOFT) {
                removeItem(player, used)
                addItem(player, used.id + 1, 1)
                rewardXP(player, Skills.COOKING, 30.0)
                return@queueScript stopExecuting(player)
            }
            return@onUseWith true
        }

        /*
         * Handles making raw gnome bowls from dough and mold.
         */

        onUseWith(IntType.ITEM, Items.GIANNE_DOUGH_2171, Items.GNOMEBOWL_MOULD_2166) { player, used, with ->
            if (removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                addItem(player, Items.RAW_GNOMEBOWL_2178, 1)
            }
            return@onUseWith true
        }

        /*
         * Handles making raw batta from dough and batta tin.
         */

        onUseWith(IntType.ITEM, Items.GIANNE_DOUGH_2171, Items.BATTA_TIN_2164) { player, used, with ->
            if (removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                addItem(player, Items.RAW_BATTA_2250, 1)
            }
            return@onUseWith true
        }

        /*
         * Handles making raw crunchy items from dough and crunchy tray
         */

        onUseWith(IntType.ITEM, Items.GIANNE_DOUGH_2171, Items.CRUNCHY_TRAY_2165) { player, used, with ->
            if (removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                addItem(player, Items.RAW_CRUNCHIES_2202, 1)
            }
            return@onUseWith true
        }

        /*
         * Handles checking the player current job and delivery status.
         */

        on(Items.ALUFT_ALOFT_BOX_9477, IntType.ITEM, "check") { player, _ ->
            val jobId = getAttribute(player, "$GC_BASE_ATTRIBUTE:$GC_JOB_ORDINAL", -1)

            if (jobId == -1) {
                sendDialogueLines(player, "You do not currently have a job.")
            } else {
                val job = GnomeCookingTask.values()[jobId]
                val item = getAttribute(player, "$GC_BASE_ATTRIBUTE:$GC_NEEDED_ITEM", Item(0))
                val npcName = getNPCName(job.npc_id).lowercase()
                sendDialogueLines(
                    player, "I need to deliver a ${item.name.lowercase()} to $npcName,", "who is ${job.tip}"
                )
            }
            return@on true
        }

        /*
         * Handles check option on the GC Reward Token item.
         */

        on(Items.REWARD_TOKEN_9474, IntType.ITEM, "check") { player, _ ->
            val charges = player.getAttribute("$GC_BASE_ATTRIBUTE:$GC_REDEEMABLE_FOOD", 0)
            sendDialogueLines(player, "You have $charges redeemable charges.")
            return@on true
        }

        /*
         * Handles activate option on the GC Reward Token item.
         */

        on(Items.REWARD_TOKEN_9474, IntType.ITEM, "activate") { player, _ ->
            sendDialogueOptions(player, "How many charges?", "1", "5", "10")
            addDialogueAction(player) { _, button ->
                when (button) {
                    2 -> sendCharges(1, player)
                    3 -> sendCharges(5, player)
                    4 -> sendCharges(10, player)
                }
            }
            return@on true
        }

    }

    /*
     * Handles the delivery of redeemed food charges.
     */

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

    /*
     * Handles the pulse of the delivery process.
     */

    class DeliveryPulse(val player: Player, val items: ArrayList<Item>) : Pulse(RandomFunction.random(15, 30)) {
        override fun pulse(): Boolean {
            player.inventory.add(*items.toTypedArray())
            sendDialogue(player, "Your food delivery has arrived!")
            return true
        }
    }
}
