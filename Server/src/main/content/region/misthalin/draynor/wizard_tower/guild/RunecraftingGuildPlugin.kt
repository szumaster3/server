package content.region.misthalin.draynor.wizard_tower.guild

import content.global.skill.runecrafting.Talisman
import content.region.misthalin.draynor.wizard_tower.guild.RunecraftingGuildPlugin.ShopItem
import core.api.*
import core.cache.def.impl.ItemDefinition
import core.game.dialogue.FaceAnim
import core.game.dialogue.InputType
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import shared.consts.*

/*
 * TODO CHECKLIST
 * [ ] - Entering the portal in the Dagon'Hai caves now works with an omni-talisman. 26 November 2008
 * [ ] - Unlike the Omni-tiara, the Omni-talisman cannot grant access to the free to play altars, such as air, mind, water, earth, fire, and body, while in a free players' world.
 *          1. https://youtu.be/EAhQXrs4TOo?si=mDf3NpWxcE3svq6w&t=448
 * [ ] - The Omni-talisman counts for a Soul talisman, even if the player obtained their omni-talisman prior to the soul talisman's release.
 * [ ] - Access to all Elriss dialogues requires Ring of Charos (a) and a set of Runecrafter robes (any colour) equipped.
 */
class RunecraftingGuildPlugin : InteractionListener, InterfaceListener, MapArea {

    data class ShopItem(val id: Int, val price: Int, val amount: Int)

    companion object {
        private val RC_HAT = intArrayOf(
            Items.RUNECRAFTER_HAT_13626,
            Items.RUNECRAFTER_HAT_13625,
            Items.RUNECRAFTER_HAT_13621,
            Items.RUNECRAFTER_HAT_13620,
            Items.RUNECRAFTER_HAT_13616,
            Items.RUNECRAFTER_HAT_13615,
        )

        private val WIZARD_NPCs = intArrayOf(
            NPCs.WIZARD_8033,
            NPCs.WIZARD_8034,
            NPCs.WIZARD_8035,
            NPCs.WIZARD_8036,
            NPCs.WIZARD_8037,
            NPCs.WIZARD_8038,
            NPCs.WIZARD_8039,
            NPCs.WIZARD_8040,
        )

        // Components for each altar icon that shows on the map table interface.
        private val altarComponents = intArrayOf(35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 47, 48)

        // Item IDs of all talismans. In-game command [::talismankit] add all talisman items to inventory :).
        val talismanIDs = Talisman.values().map { it.item }.toIntArray()

        // Map to link talisman item IDs with interface component IDs.
        // (The component 45 and 46 IDs was for: Elemental talisman [ID: 5516] and Soul talisman [ID: 1460]).
        private val talismanToComponentMap = mapOf(
            Items.AIR_TALISMAN_1438 to 35,
            Items.BODY_TALISMAN_1446 to 36,
            Items.MIND_TALISMAN_1448 to 37,
            Items.EARTH_TALISMAN_1440 to 38,
            Items.WATER_TALISMAN_1444 to 39,
            Items.FIRE_TALISMAN_1442 to 40,
            Items.CHAOS_TALISMAN_1452 to 41,
            Items.LAW_TALISMAN_1458 to 42,
            Items.BLOOD_TALISMAN_1450 to 43,
            Items.NATURE_TALISMAN_1462 to 44,
            Items.DEATH_TALISMAN_1456 to 47,
            Items.COSMIC_TALISMAN_1454 to 48,
        )

        val hatToggleMap = mapOf(
            Items.RUNECRAFTER_HAT_13626 to Items.RUNECRAFTER_HAT_13625,
            Items.RUNECRAFTER_HAT_13625 to Items.RUNECRAFTER_HAT_13626,
            Items.RUNECRAFTER_HAT_13621 to Items.RUNECRAFTER_HAT_13620,
            Items.RUNECRAFTER_HAT_13620 to Items.RUNECRAFTER_HAT_13621,
            Items.RUNECRAFTER_HAT_13616 to Items.RUNECRAFTER_HAT_13615,
            Items.RUNECRAFTER_HAT_13615 to Items.RUNECRAFTER_HAT_13616,
        )
    }

    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(ZoneBorders.forRegion(6741))
    override fun getRestrictions(): Array<ZoneRestriction> = arrayOf(ZoneRestriction.CANNON, ZoneRestriction.RANDOM_EVENTS, ZoneRestriction.GRAVES, ZoneRestriction.FIRES)

    override fun defineListeners() {

        /*
         * Handles interactions with various objects inside the guild.
         */

        on(Scenery.CONTAINMENT_UNIT_38327, IntType.SCENERY, "activate") { _, node ->
            animateScenery(node.asScenery(), 10193)
            return@on true
        }
        on(Scenery.GLASS_SPHERES_38331, IntType.SCENERY, "activate") { _, node ->
            animateScenery(node.asScenery(), 10129)
            return@on true
        }
        on(Scenery.GYROSCOPE_38330, IntType.SCENERY, "activate") { _, node ->
            animateScenery(node.asScenery(), 10127)
            return@on true
        }
        on(Scenery.RUNESTONE_ACCELERATOR_38329, IntType.SCENERY, "activate") { _, node ->
            animateScenery(node.asScenery(), 10196)
            return@on true
        }

        /*
         * Handles the interaction with the map table scenery to open the study interface.
         */

        on(Scenery.MAP_TABLE_38315, IntType.SCENERY, "Study") { player, _ ->
            openInterface(player, Components.RCGUILD_MAP_780)
            return@on true
        }

        /*
         * Handles the interaction with the map scenery to open the study interface.
         */

        on(Scenery.MAP_38422, IntType.SCENERY, "Study") { player, _ ->
            openInterface(player, Components.RCGUILD_MAP_780)
            return@on true
        }

        /*
         * Handles the interaction with the map scenery to open the study interface.
         */

        on(Scenery.MAP_38421, IntType.SCENERY, "Study") { player, _ ->
            openInterface(player, Components.RCGUILD_MAP_780)
            return@on true
        }

        /*
         * Handles use talisman item to reveal altar on the map.
         */

        onUseWith(IntType.SCENERY, talismanIDs, Scenery.MAP_TABLE_38315) { player, used, _ ->
            openInterface(player, Components.RCGUILD_MAP_780)
            val componentID = talismanToComponentMap[used.id] ?: 0
            if (componentID != 0) {
                setComponentVisibility(player, Components.RCGUILD_MAP_780, componentID, false)
            }
            return@onUseWith true
        }

        /*
         * Handles the interaction with the Omni items on the map table.
         * If the Omni Talisman or Omni Tiara equipped, gain access to all
         * altar locations on the map.
         */

        onUseWith(IntType.SCENERY, Items.OMNI_TALISMAN_13649, Scenery.MAP_TABLE_38315) { player, _, _ ->
            if (!inEquipment(player, Items.OMNI_TALISMAN_13649) || !inEquipment(player, Items.OMNI_TIARA_13655)) {
                openInterface(player, Components.RCGUILD_MAP_780)
                for (componentID in altarComponents) {
                    setComponentVisibility(player, Components.RCGUILD_MAP_780, componentID, false).also {
                        sendString(player, "All the altars of " + GameWorld.settings!!.name + ".", Components.RCGUILD_MAP_780, 33)
                    }
                }
            }
            return@onUseWith true
        }

        /*
         * Handles the interaction with the RC Portal scenery.
         * Checks if the player has the required RC level and has completed the Rune Mysteries quest.
         */

        on(Scenery.PORTAL_38279, IntType.SCENERY, "Enter") { player, _ ->
            if (getStatLevel(player, Skills.RUNECRAFTING) < 50) {
                sendDialogue(player, "You require 50 Runecrafting to enter the Runecrafters' Guild.")
                return@on true
            }
            if (!isQuestComplete(player, Quests.RUNE_MYSTERIES)) {
                sendDialogue(player, "You need to complete Rune Mysteries to enter the Runecrafting guild.")
                return@on true
            }

            val destination = if (player.viewport.region!!.regionId == 12337) {
                Location.create(1696, 5461, 2)
            } else {
                Location.create(3106, 3160, 1)
            }

            player.lock(4)
            visualize(player, Animations.RC_TP_A_10180, Graphics.RC_GUILD_TP)
            queueScript(player, 3, QueueStrength.SOFT) {
                teleport(player, destination)
                visualize(player, Animations.RC_TP_B_10182, Graphics.RC_GUILD_TP)
                face(player, destination)
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        /*
         * Handles the interaction with the RC Hat item by toggling between two variations.
         * You can switch between wearing the goggles on the hat or without them.
         */

        on(RC_HAT, IntType.ITEM, "Goggles") { player, node ->
            val newHatId = hatToggleMap[node.id] ?: return@on false
            replaceSlot(player, node.asItem().slot, Item(newHatId))
            return@on true
        }

        /*
         * Handles interaction with Wizard Elriss NPC to open the rewards interface.
         */

        on(NPCs.WIZARD_ELRISS_8032, IntType.NPC, "Exchange") { player, _ ->
            openInterface(player, Components.RCGUILD_REWARDS_779)
            return@on true
        }

        /*
         * Handles dialogue interaction with Wizards.
         */

        on(WIZARD_NPCs, IntType.NPC, "talk-to") { player, _ ->
            sendOptions(player, "Select an option", "I want to join the orb project!", "Never mind.")
            addDialogueAction(player) { _, _ ->
                closeDialogue(player)
            }
            return@on true
        }

        /*
         * Handles dialogue interaction with Wizard Vief.
         */

        on(NPCs.WIZARD_VIEF_8030, IntType.NPC, "talk-to") { player, node ->
            sendNPCDialogue(player, node.id, "Ah! You'll help me, won't you?", FaceAnim.HAPPY)
            return@on true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, intArrayOf(Scenery.PORTAL_38279), "enter") { player, node ->
            if (player.viewport.region!!.regionId == 12337) {
                return@setDest node.asScenery().location
            } else {
                return@setDest Location.create(1696, 5461, 2)
            }
        }
    }

    override fun defineInterfaceListeners() {


        /*
         * Handles the opening of the study interface.
         */

        onOpen(Components.RCGUILD_MAP_780) { player, _ ->
            if (inEquipment(player, Items.OMNI_TALISMAN_STAFF_13642) || inEquipment(player, Items.OMNI_TIARA_13655)) {
                for (rune in altarComponents) {
                    setComponentVisibility(player, Components.RCGUILD_MAP_780, rune, false).also {
                        sendString(player, "All the altars of " + GameWorld.settings!!.name + ".", Components.RCGUILD_MAP_780, 33)
                    }
                }
            }
            return@onOpen true
        }

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
            val stock = RcShopStock.fromButton(button)
            when (opcode) {
                155 -> {
                    if (stock != null) {
                        val shopItem = stock.itemId
                        player.setAttribute("rc-selected-item", shopItem)
                        player.setAttribute("rc-selected-amount", 1)
                        selectObject(shopItem, 1, player)
                    }

                    if (button == 163) {
                        val selected = player.getAttribute<ShopItem>("rc-selected-item")
                        val amount = player.getAttribute<Int>("rc-selected-amount") ?: 1

                        if (selected != null) {
                            /* [Variant for buy as much as we can.]

                            val isStackable = ItemDefinition.forId(selected.id).isStackable()
                            val amt = if (isStackable) {
                                amount
                            } else {
                                val free = freeSlots(player)
                                if (free <= 0) {
                                    sendMessage(player, "You don't have enough space in your inventory.")
                                    return@on true
                                }
                                if (amount > free) free else amount
                            }
                            */

                            val isStackable = ItemDefinition.forId(selected.id).isStackable()
                            if (!isStackable && amount > freeSlots(player)) {
                                sendMessage(player, "You don't have enough space in your inventory.")
                                return@on true
                            }

                            handleBuyOption(selected, amount, player)
                            player.removeAttribute("rc-selected-item")
                            player.removeAttribute("rc-selected-amount")
                        } else {
                            sendMessage(player, "You must select something to buy before you can confirm your purchase.")
                        }
                    }
                }

                196 -> {
                    if (stock != null) {
                        val shopItem = stock.itemId
                        sendInputDialogue(player, InputType.AMOUNT, "Enter the amount to buy:") { value ->
                            val amt = value.toString().toIntOrNull()
                            if (amt == null || amt <= 0) {
                                sendDialogue(player, "Please enter a valid amount greater than zero.")
                                return@sendInputDialogue
                            }

                            player.setAttribute("rc-selected-item", shopItem)
                            player.setAttribute("rc-selected-amount", amt)
                            selectObject(shopItem, amt, player)
                        }
                    }
                }
            }
            return@on true
        }

        onClose(Components.RCGUILD_REWARDS_779) { player, _ ->
            player.attributes.remove("rc-selected-item")
            player.attributes.remove("rc-selected-amount")
            return@onClose true
        }
    }

    /**
     * Handles the purchase option for an item.
     */
    private fun handleBuyOption(item: ShopItem, amount: Int, player: Player) {
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
     * Sends the current token balance to the player.
     */
    private fun sendTokens(player: Player) {
        sendString(player, "Tokens: ${amountInInventory(player, Items.RUNECRAFTING_GUILD_TOKEN_13650)}", Components.RCGUILD_REWARDS_779, 135)
    }

    /**
     * Select an item display to the player.
     */
    private fun selectObject(item: ShopItem, amount: Int, player: Player) {
        sendString(player, "${getItemName(item.id)}($amount)", Components.RCGUILD_REWARDS_779, 136)
    }
}

private enum class RcShopStock(val buttonId: Int, val itemId: ShopItem) {
    // Talismans
    AIR_TALISMAN(6, ShopItem(Items.AIR_TALISMAN_1438, 50, 1)),
    MIND_TALISMAN(13, ShopItem(Items.MIND_TALISMAN_1448, 50, 1)),
    WATER_TALISMAN(15, ShopItem(Items.WATER_TALISMAN_1444, 50, 1)),
    EARTH_TALISMAN(10, ShopItem(Items.EARTH_TALISMAN_1440, 50, 1)),
    FIRE_TALISMAN(11, ShopItem(Items.FIRE_TALISMAN_1442, 50, 1)),
    BODY_TALISMAN(7, ShopItem(Items.BODY_TALISMAN_1446, 50, 1)),
    COSMIC_TALISMAN(9, ShopItem(Items.COSMIC_TALISMAN_1454, 125, 1)),
    CHAOS_TALISMAN(8, ShopItem(Items.CHAOS_TALISMAN_1452, 125, 1)),
    NATURE_TALISMAN(14, ShopItem(Items.NATURE_TALISMAN_1462, 125, 1)),
    LAW_TALISMAN(12, ShopItem(Items.LAW_TALISMAN_1458, 125, 1)),

    // Runecrafting clothes
    BLUE_RC_HAT(36, ShopItem(Items.RUNECRAFTER_HAT_13626, 1000, 1)),
    YELLOW_RC_HAT(37, ShopItem(Items.RUNECRAFTER_HAT_13616, 1000, 1)),
    GREEN_RC_HAT(38, ShopItem(Items.RUNECRAFTER_HAT_13621, 1000, 1)),

    BLUE_RC_ROBE(39, ShopItem(Items.RUNECRAFTER_ROBE_13624, 1000, 1)),
    YELLOW_RC_ROBE(40, ShopItem(Items.RUNECRAFTER_ROBE_13614, 1000, 1)),
    GREEN_RC_ROBE(41, ShopItem(Items.RUNECRAFTER_ROBE_13619, 1000, 1)),

    BLUE_RC_BOTTOM(42, ShopItem(Items.RUNECRAFTER_SKIRT_13627, 1000, 1)),
    YELLOW_RC_BOTTOM(43, ShopItem(Items.RUNECRAFTER_SKIRT_13617, 1000, 1)),
    GREEN_RC_BOTTOM(44, ShopItem(Items.RUNECRAFTER_SKIRT_13622, 1000, 1)),

    BLUE_RC_GLOVES(45, ShopItem(Items.RUNECRAFTER_GLOVES_13628, 1000, 1)),
    YELLOW_RC_GLOVES(46, ShopItem(Items.RUNECRAFTER_GLOVES_13618, 1000, 1)),
    GREEN_RC_GLOVES(47, ShopItem(Items.RUNECRAFTER_GLOVES_13623, 1000, 1)),

    // Staff
    RC_STAFF(114, ShopItem(Items.RUNECRAFTING_STAFF_13629, 10000, 1)),

    // Essences
    // TODO: DROP PRICE TO 1 AFTER IMPL MINI_GAME.
    PURE_ESSENCE(115, ShopItem(Items.PURE_ESSENCE_7937, 100, 1)),

    // Teleport tablets
    AIR_TABLET(72, ShopItem(Items.AIR_ALTAR_TP_13599, 30, 1)),
    MIND_TABLET(80, ShopItem(Items.MIND_ALTAR_TP_13600, 32, 1)),
    WATER_TABLET(83, ShopItem(Items.WATER_ALTAR_TP_13601, 34, 1)),
    EARTH_TABLET(77, ShopItem(Items.EARTH_ALTAR_TP_13602, 36, 1)),
    FIRE_TABLET(78, ShopItem(Items.FIRE_ALTAR_TP_13603, 37, 1)),
    BODY_TABLET(73, ShopItem(Items.BODY_ALTAR_TP_13604, 38, 1)),
    COSMIC_TABLET(75, ShopItem(Items.COSMIC_ALTAR_TP_13605, 39, 1)),
    CHAOS_TABLET(74, ShopItem(Items.CHAOS_ALTAR_TP_13606, 40, 1)),
    ASTRAL_TABLET(81, ShopItem(Items.ASTRAL_ALTAR_TP_13611, 41, 1)),
    NATURE_TABLET(82, ShopItem(Items.NATURE_ALTAR_TP_13607, 42, 1)),
    LAW_TABLET(79, ShopItem(Items.LAW_ALTAR_TP_13608, 43, 1)),
    DEATH_TABLET(76, ShopItem(Items.DEATH_ALTAR_TP_13609, 44, 1)),
    BLOOD_TABLET(84, ShopItem(Items.BLOOD_ALTAR_TP_13610, 45, 1)),
    GUILD_TABLET(85, ShopItem(Items.RUNECRAFTING_GUILD_TP_13598, 15, 1));

    companion object {
        private val byButton = values().associateBy { it.buttonId }
        fun fromButton(id: Int): RcShopStock? = byButton[id]
    }
}