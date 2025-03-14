package content.region.misthalin.handlers.rc_guild

import core.api.*
import core.game.dialogue.InputType
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.tools.Log
import org.rs.consts.Components
import org.rs.consts.Items

/**
 * Data class representing an item in the shop.
 * @property id The unique identifier for the item.
 * @property price The cost of the item in tokens.
 * @property amount The quantity of the item available for purchase.
 */
data class ShopItem(
    val id: Int,
    val price: Int,
    val amount: Int,
)

class RunecraftingGuildRewards : InterfaceListener {
    override fun defineInterfaceListeners() {
        /*
         * Handles the opening of the RC Guild Rewards interface.
         */

        onOpen(Components.RCGUILD_REWARDS_779) { player, _ ->
            sendTokens(player)
            return@onOpen true
        }

        /*
         * Handles interaction with the RC Guild Rewards interface.
         */

        on(Components.RCGUILD_REWARDS_779) { player, _, opcode, button, _, _ ->
            var choice: ShopItem
            when (button) {
                6 -> choice = airTalisman
                13 -> choice = mindTalisman
                15 -> choice = waterTalisman
                10 -> choice = earthTalisman
                11 -> choice = fireTalisman
                7 -> choice = bodyTalisman
                9 -> choice = cosmicTalisman
                8 -> choice = chaosTalisman
                14 -> choice = natureTalisman
                12 -> choice = lawTalisman
                36 -> choice = blueRCHat
                37 -> choice = yellowRCHat
                38 -> choice = greenRCHat
                39 -> choice = blueRCRobe
                40 -> choice = yellowRCRobe
                41 -> choice = greenRCRobe
                42 -> choice = blueRCBottom
                43 -> choice = yellowRCBottom
                44 -> choice = greenRCBottom
                45 -> choice = blueRCGloves
                46 -> choice = yellowRCGloves
                47 -> choice = greenRCGloves
                72 -> choice = airTablet
                80 -> choice = mindTablet
                83 -> choice = waterTablet
                77 -> choice = earthTablet
                78 -> choice = fireTablet
                73 -> choice = bodyTablet
                75 -> choice = cosmicTablet
                74 -> choice = chaosTablet
                81 -> choice = astralTablet
                82 -> choice = natureTablet
                79 -> choice = lawTablet
                76 -> choice = deathTablet
                84 -> choice = bloodTablet
                85 -> choice = guildTablet
                114 -> choice = rcStaff
                115 -> choice = pureEssence
                else ->
                    log(
                        this::class.java,
                        Log.WARN,
                        "Unhandled button ID for RC shop interface: $button",
                    ).also { return@on true }
            }

            handleOpcode(choice, opcode, player)
            if (opcode == 155) {
                when (button) {
                    163 -> sendMessage(player, "You must select something to buy before you can confirm your purchase")
                }
            }
            sendItem(choice, choice.amount, player)
            return@on true
        }
    }

    /**
     * Handles the purchase option for an item.
     * @param item The item to be purchased.
     * @param amount The quantity of the item to purchase.
     * @param player The player making the purchase.
     */
    private fun handleBuyOption(
        item: ShopItem,
        amount: Int,
        player: Player,
    ) {
        val neededTokens = Item(Items.RUNECRAFTING_GUILD_TOKEN_13650, item.price * amount)
        if (freeSlots(player) == 0) {
            sendMessage(player, "You don't have enough space in your inventory.")
            return
        }
        if (!player.inventory.containsItem(neededTokens)) {
            sendMessage(player, "You don't have enough tokens to purchase that.")
            return
        }

        sendMessage(player, "Your purchase has been added to your inventory.")
        player.inventory.remove(neededTokens)
        player.inventory.add(Item(item.id, amount))
        sendString(player, " ", Components.RCGUILD_REWARDS_779, 136)
        sendTokens(player)
    }

    /**
     * Handles the opcode for the selected item.
     * @param item The selected shop item.
     * @param opcode The operation code indicating the action to perform.
     * @param player The player interacting with the shop.
     */
    private fun handleOpcode(
        item: ShopItem,
        opcode: Int,
        player: Player,
    ) {
        when (opcode) {
            155 -> handleBuyOption(item, 1, player)
            196 -> {
                sendInputDialogue(player, InputType.AMOUNT, "Enter the amount to buy:") { value ->
                    val amt = value.toString().toIntOrNull()
                    if (amt == null || amt <= 0) {
                        sendDialogue(player, "Please enter a valid amount greater than zero.")
                        return@sendInputDialogue
                    } else {
                        handleBuyOption(item, amt, player)
                    }
                }
            }
        }
    }

    /**
     * Sends the current token balance to the player.
     * @param player The player to whom the token balance is sent.
     */
    private fun sendTokens(player: Player) {
        sendString(
            player,
            "Tokens: ${amountInInventory(player, Items.RUNECRAFTING_GUILD_TOKEN_13650)}",
            Components.RCGUILD_REWARDS_779,
            135,
        )
    }

    /**
     * Sends an item display to the player.
     * @param item The item to display.
     * @param amount The quantity of the item.
     * @param player The player to whom the item is sent.
     */
    private fun sendItem(
        item: ShopItem,
        amount: Int,
        player: Player,
    ) {
        sendString(player, "${getItemName(item.id)}($amount)", Components.RCGUILD_REWARDS_779, 136)
    }

    private val airTalisman = ShopItem(Items.AIR_TALISMAN_1438, 50, 1)
    private val mindTalisman = ShopItem(Items.MIND_TALISMAN_1448, 50, 1)
    private val waterTalisman = ShopItem(Items.WATER_TALISMAN_1444, 50, 1)
    private val earthTalisman = ShopItem(Items.EARTH_TALISMAN_1440, 50, 1)
    private val fireTalisman = ShopItem(Items.FIRE_TALISMAN_1442, 50, 1)
    private val bodyTalisman = ShopItem(Items.BODY_TALISMAN_1446, 50, 1)
    private val cosmicTalisman = ShopItem(Items.COSMIC_TALISMAN_1454, 125, 1)
    private val chaosTalisman = ShopItem(Items.CHAOS_TALISMAN_1452, 125, 1)
    private val natureTalisman = ShopItem(Items.NATURE_TALISMAN_1462, 125, 1)
    private val lawTalisman = ShopItem(Items.LAW_TALISMAN_1458, 125, 1)
    private val blueRCHat = ShopItem(Items.RUNECRAFTER_HAT_13626, 1000, 1)
    private val yellowRCHat = ShopItem(Items.RUNECRAFTER_HAT_13616, 1000, 1)
    private val greenRCHat = ShopItem(Items.RUNECRAFTER_HAT_13621, 1000, 1)
    private val blueRCRobe = ShopItem(Items.RUNECRAFTER_ROBE_13624, 1000, 1)
    private val yellowRCRobe = ShopItem(Items.RUNECRAFTER_ROBE_13614, 1000, 1)
    private val greenRCRobe = ShopItem(Items.RUNECRAFTER_ROBE_13619, 1000, 1)
    private val blueRCBottom = ShopItem(Items.RUNECRAFTER_SKIRT_13627, 1000, 1)
    private val yellowRCBottom = ShopItem(Items.RUNECRAFTER_SKIRT_13617, 1000, 1)
    private val greenRCBottom = ShopItem(Items.RUNECRAFTER_SKIRT_13622, 1000, 1)
    private val blueRCGloves = ShopItem(Items.RUNECRAFTER_GLOVES_13628, 1000, 1)
    private val yellowRCGloves = ShopItem(Items.RUNECRAFTER_GLOVES_13618, 1000, 1)
    private val greenRCGloves = ShopItem(Items.RUNECRAFTER_GLOVES_13623, 1000, 1)
    private val rcStaff = ShopItem(Items.RUNECRAFTING_STAFF_13629, 10000, 1)
    private val pureEssence = ShopItem(Items.PURE_ESSENCE_7937, 100, 1)
    private val airTablet = ShopItem(Items.AIR_ALTAR_TP_13599, 30, 1)
    private val mindTablet = ShopItem(Items.MIND_ALTAR_TP_13600, 32, 1)
    private val waterTablet = ShopItem(Items.WATER_ALTAR_TP_13601, 34, 1)
    private val earthTablet = ShopItem(Items.EARTH_ALTAR_TP_13602, 36, 1)
    private val fireTablet = ShopItem(Items.FIRE_ALTAR_TP_13603, 37, 1)
    private val bodyTablet = ShopItem(Items.BODY_ALTAR_TP_13604, 38, 1)
    private val cosmicTablet = ShopItem(Items.COSMIC_ALTAR_TP_13605, 39, 1)
    private val chaosTablet = ShopItem(Items.CHAOS_ALTAR_TP_13606, 40, 1)
    private val astralTablet = ShopItem(Items.ASTRAL_ALTAR_TP_13611, 41, 1)
    private val natureTablet = ShopItem(Items.NATURE_ALTAR_TP_13607, 42, 1)
    private val lawTablet = ShopItem(Items.LAW_ALTAR_TP_13608, 43, 1)
    private val deathTablet = ShopItem(Items.DEATH_ALTAR_TP_13609, 44, 1)
    private val bloodTablet = ShopItem(Items.BLOOD_ALTAR_TP_13610, 45, 1)
    private val guildTablet = ShopItem(Items.RUNECRAFTING_GUILD_TP_13598, 15, 1)
}
