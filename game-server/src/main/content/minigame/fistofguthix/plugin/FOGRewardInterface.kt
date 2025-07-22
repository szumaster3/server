package content.minigame.fistofguthix.plugin

import core.api.*
import core.cache.def.impl.ItemDefinition
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.tools.Log
import org.rs.consts.Components
import org.rs.consts.Items

class FOGRewardInterface : InterfaceListener {
    class ShopItem(
        val id: Int,
        val price: Int,
        val amount: Int,
    )

    override fun defineInterfaceListeners() {
        on(Components.FOG_REWARDS_732) { player, _, opcode, button, _, _ ->
            var stock = ShopItem(0, 0, 0)
            when (button) {
                2 -> stock = DruidicMageTop
                7 -> stock = DruidicMageBottom
                12 -> stock = DruidicMageHood
                19 -> stock = CombatRobeTop
                24 -> stock = CombatRobeBottom
                29 -> stock = CombatRobeHood
                36 -> stock = BattleRobeTop
                41 -> stock = BattleRobeBottom
                46 -> stock = BattleRobeHood
                53 -> stock = GreenCoif
                58 -> stock = BlueCoif
                63 -> stock = RedCoif
                68 -> stock = BlackCoif
                75 -> stock = BronzeGaunt
                80 -> stock = IronGaunt
                85 -> stock = SteelGaunt
                90 -> stock = BlackGaunt
                95 -> stock = MithrilGaunt
                100 -> stock = AdamantGaunt
                105 -> stock = RuneGaunt
                110 -> stock = DragonGaunt
                117 -> stock = AddySpike
                122 -> stock = AddyBeserk
                127 -> stock = RuneSpike
                132 -> stock = RuneBeserk
                139 -> stock = IritGloves
                144 -> stock = AvantoeGloves
                149 -> stock = KwuarmGloves
                154 -> stock = CadantineGloves
                161 -> stock = SwordfishGloves
                166 -> stock = SharkGloves
                171 -> stock = DragonGloves
                176 -> stock = AirGloves
                181 -> stock = WaterGloves
                186 -> stock = EarthGloves
                else ->
                    log(this::class.java, Log.WARN, "Unhandled button ID for FOG interface: $button").also {
                        return@on true
                    }
            }

            when (opcode) {
                in listOf(196, 124, 199) -> {
                    handlePurchase(player, stock, opcode)
                }

                155 -> sendMessage(player, "${getItemName(stock.id).replace("100", "")} costs ${stock.price} tokens.")
                234 -> sendMessage(player, ItemDefinition.forId(stock.id).examine.replace("100", ""))
            }
            true
        }
    }

    private fun handlePurchase(
        player: Player,
        stock: ShopItem,
        opcode: Int,
    ) {
        val multiplier =
            when (opcode) {
                196 -> 1
                124 -> 5
                199 -> 10
                else -> return
            }

        val neededTokens = Item(Items.FIST_OF_GUTHIX_TOKEN_12852, stock.price * multiplier)
        if (player.inventory.containsItem(neededTokens)) {
            if (hasSpaceFor(player, Item(stock.id, multiplier))) {
                removeItem(player, neededTokens)
                addItem(player, stock.id, multiplier)
            } else {
                sendMessage(player, "You don't have enough space in your inventory.")
            }
        }
    }

    val DruidicMageTop = ShopItem(Items.DRUIDIC_MAGE_TOP_100_12894, 300, 1)
    val DruidicMageHood = ShopItem(Items.DRUIDIC_MAGE_HOOD_100_12887, 100, 1)
    val DruidicMageBottom = ShopItem(Items.DRUIDIC_MAGE_BOTTOM_100_12901, 200, 1)
    val CombatRobeTop = ShopItem(Items.COMBAT_ROBE_TOP_100_12971, 150, 1)
    val CombatRobeHood = ShopItem(Items.COMBAT_HOOD_100_12964, 50, 1)
    val CombatRobeBottom = ShopItem(Items.COMBAT_ROBE_BOTTOM_100_12978, 100, 1)
    val BattleRobeHood = ShopItem(Items.BATTLE_HOOD_100_12866, 250, 1)
    val BattleRobeTop = ShopItem(Items.BATTLE_ROBE_TOP_100_12873, 1500, 1)
    val BattleRobeBottom = ShopItem(Items.BATTLE_ROBE_BOTTOM_100_12880, 1000, 1)
    val GreenCoif = ShopItem(Items.GREEN_DHIDE_COIF_100_12936, 150, 1)
    val BlueCoif = ShopItem(Items.BLUE_DHIDE_COIF_100_12943, 200, 1)
    val RedCoif = ShopItem(Items.RED_DHIDE_COIF_100_12950, 300, 1)
    val BlackCoif = ShopItem(Items.BLACK_DHIDE_COIF_100_12957, 500, 1)
    val BronzeGaunt = ShopItem(Items.BRONZE_GAUNTLETS_12985, 15, 1)
    val IronGaunt = ShopItem(Items.IRON_GAUNTLETS_12988, 30, 1)
    val SteelGaunt = ShopItem(Items.STEEL_GAUNTLETS_12991, 50, 1)
    val BlackGaunt = ShopItem(Items.BLACK_GAUNTLETS_12994, 75, 1)
    val MithrilGaunt = ShopItem(Items.MITHRIL_GAUNTLETS_12997, 100, 1)
    val AdamantGaunt = ShopItem(Items.ADAMANT_GAUNTLETS_13000, 150, 1)
    val RuneGaunt = ShopItem(Items.RUNE_GAUNTLETS_13003, 200, 1)
    val DragonGaunt = ShopItem(Items.DRAGON_GAUNTLETS_13006, 300, 1)
    val AddySpike = ShopItem(Items.ADAMANT_SPIKESHIELD_100_12908, 50, 1)
    val AddyBeserk = ShopItem(Items.ADAMANT_BERSERKER_SHIELD_100_12915, 100, 1)
    val RuneSpike = ShopItem(Items.RUNE_SPIKESHIELD_100_12922, 200, 1)
    val RuneBeserk = ShopItem(Items.RUNE_BERSERKER_SHIELD_100_12929, 300, 1)
    val AirGloves = ShopItem(Items.AIR_RUNECRAFTING_GLOVES_12863, 75, 1)
    val WaterGloves = ShopItem(Items.WATER_RUNECRAFTING_GLOVES_12864, 75, 1)
    val EarthGloves = ShopItem(Items.EARTH_RUNECRAFTING_GLOVES_12865, 75, 1)
    val IritGloves = ShopItem(Items.IRIT_GLOVES_12856, 75, 1)
    val AvantoeGloves = ShopItem(Items.AVANTOE_GLOVES_12857, 100, 1)
    val KwuarmGloves = ShopItem(Items.KWUARM_GLOVES_12858, 200, 1)
    val CadantineGloves = ShopItem(Items.CADANTINE_GLOVES_12859, 200, 1)
    val SwordfishGloves = ShopItem(Items.SWORDFISH_GLOVES_12860, 200, 1)
    val SharkGloves = ShopItem(Items.SHARK_GLOVES_12861, 200, 1)
    val DragonGloves = ShopItem(Items.DRAGON_SLAYER_GLOVES_12862, 200, 1)
}
