package content.global.handlers.iface

import core.api.*
import core.api.item.itemDefinition
import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.container.access.InterfaceContainer
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.colorize
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.rs.consts.Components
import org.rs.consts.Items

private val POLAR_CAMO_TOP = Item(Items.POLAR_CAMO_TOP_10065)
private val POLAR_CAMO_LEGS = Item(Items.POLAR_CAMO_LEGS_10067)

private val WOOD_CAMO_TOP = Item(Items.WOOD_CAMO_TOP_10053)
private val WOOD_CAMO_LEGS = Item(Items.WOOD_CAMO_LEGS_10055)

private val JUNGLE_CAMO_TOP = Item(Items.JUNGLE_CAMO_TOP_10057)
private val JUNGLE_CAMO_LEGS = Item(Items.JUNGLE_CAMO_LEGS_10059)

private val DESERT_CAMO_TOP = Item(Items.DESERT_CAMO_TOP_10061)
private val DESERT_CAMO_LEGS = Item(Items.DESERT_CAMO_LEGS_10063)

private val LARUPIA_HAT = Item(Items.LARUPIA_HAT_10045)
private val LARUPIA_TOP = Item(Items.LARUPIA_TOP_10043)
private val LARUPIA_LEGS = Item(Items.LARUPIA_LEGS_10041)

private val GRAAHK_HEADDRESS = Item(Items.GRAAHK_HEADDRESS_10051)
private val GRAAHK_TOP = Item(Items.GRAAHK_TOP_10049)
private val GRAAHK_LEGS = Item(Items.GRAAHK_LEGS_10047)

private val KYATT_HAT = Item(Items.KYATT_HAT_10039)
private val KYATT_TOP = Item(Items.KYATT_TOP_10037)
private val KYATT_LEGS = Item(Items.KYATT_LEGS_10035)

private val GLOVES_OF_SILENCE = Item(Items.GLOVES_OF_SILENCE_10075)
private val SPOTTED_CAPE = Item(Items.SPOTTED_CAPE_10069)
private val SPOTTIER_CAPE = Item(Items.SPOTTIER_CAPE_10071)

private val OPTIONS = arrayOf("Buy 10", "Buy 5", "Buy 1", "Value")

private const val FUR_CLOTHING_COMPONENT_ID = Components.CUSTOM_FUR_CLOTHING_477

private val ITEMS =
    arrayOf(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        POLAR_CAMO_TOP,
        null,
        null,
        POLAR_CAMO_LEGS,
        null,
        null,
        null,
        null,
        null,
        WOOD_CAMO_TOP,
        null,
        null,
        WOOD_CAMO_LEGS,
        null,
        null,
        null,
        null,
        JUNGLE_CAMO_TOP,
        null,
        null,
        JUNGLE_CAMO_LEGS,
        null,
        null,
        null,
        null,
        DESERT_CAMO_TOP,
        null,
        null,
        DESERT_CAMO_LEGS,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        LARUPIA_HAT,
        null,
        null,
        LARUPIA_TOP,
        null,
        null,
        LARUPIA_LEGS,
        null,
        null,
        null,
        null,
        GRAAHK_HEADDRESS,
        null,
        null,
        GRAAHK_TOP,
        null,
        null,
        GRAAHK_LEGS,
        null,
        null,
        null,
        null,
        null,
        KYATT_HAT,
        null,
        null,
        KYATT_TOP,
        null,
        null,
        KYATT_LEGS,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        GLOVES_OF_SILENCE,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        SPOTTED_CAPE,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        SPOTTIER_CAPE,
        null,
    )

@Initializable
class FurClothingInterface : ComponentPlugin() {
    override fun open(
        player: Player?,
        component: Component?,
    ) {
        super.open(player, component)
        player ?: return
        InterfaceContainer.generateItems(player, ITEMS, OPTIONS, FUR_CLOTHING_COMPONENT_ID, 26, 12, 33, 5000)
        GlobalScope.launch {
            val checkedFurs = arrayListOf<String>()
            for (clothing in FUR_CLOTHING.values()) {
                if (checkedFurs.contains(clothing.textContent)) continue
                if (inInventory(player, clothing.requiredFur.id, clothing.requiredFur.amount)) {
                    sendString(
                        player,
                        colorize("%G${clothing.textContent}"),
                        FUR_CLOTHING_COMPONENT_ID,
                        clothing.textChildID,
                    )
                    checkedFurs.add(clothing.textContent)
                }
            }
        }
    }

    override fun handle(
        player: Player?,
        component: Component?,
        opcode: Int,
        button: Int,
        slot: Int,
        itemId: Int,
    ): Boolean {
        player ?: return false

        val clothingItem: FUR_CLOTHING? =
            when (slot) {
                34 -> FUR_CLOTHING.POLAR_KEBBIT_TOP
                37 -> FUR_CLOTHING.POLAR_KEBBIT_BOT
                43 -> FUR_CLOTHING.COMMON_KEBBIT_TOP
                46 -> FUR_CLOTHING.COMMON_KEBBIT_BOT
                51 -> FUR_CLOTHING.FELDIP_WEASEL_TOP
                54 -> FUR_CLOTHING.FELDIP_WEASEL_LEGS
                59 -> FUR_CLOTHING.DESERT_DEVIL_TOP
                62 -> FUR_CLOTHING.DESERT_DEVIL_LEGS
                166 -> FUR_CLOTHING.LARUPIA_HEAD
                169 -> FUR_CLOTHING.LARUPIA_CHEST
                172 -> FUR_CLOTHING.LARUPIA_BOT
                177 -> FUR_CLOTHING.GRAAHK_HEAD
                180 -> FUR_CLOTHING.GRAAHK_CHEST
                183 -> FUR_CLOTHING.GRAAHK_BOT
                189 -> FUR_CLOTHING.KYATT_HEAD
                192 -> FUR_CLOTHING.KYATT_CHEST
                195 -> FUR_CLOTHING.KYATT_BOT
                334 -> FUR_CLOTHING.GLOVES_SILENCE
                345 -> FUR_CLOTHING.SPOT_CAPE
                357 -> FUR_CLOTHING.DASH_CAPE
                else -> null
            }

        when (opcode) {
            196 -> clothingItem?.let { buy(player, it, 1) }
            124 -> clothingItem?.let { buy(player, it, 5) }
            199 -> clothingItem?.let { buy(player, it, 10) }
            155 -> clothingItem?.let { value(player, it) }
            9 -> clothingItem?.let { examine(player, it) }
        }
        return true
    }

    private fun value(
        player: Player,
        clothing: FUR_CLOTHING,
    ) {
        sendMessage(
            player,
            "${clothing.product.name} requires ${clothing.requiredFur.amount} ${clothing.requiredFur.name.lowercase()} and costs ${clothing.price} coins.",
        )
    }

    private fun examine(
        player: Player,
        clothing: FUR_CLOTHING,
    ) {
        sendMessage(player, itemDefinition(clothing.product.id).examine)
    }

    private fun buy(
        player: Player,
        clothing: FUR_CLOTHING,
        amount: Int,
    ) {
        val coins = Item(995, clothing.price * amount)
        val amtFurRequired = clothing.requiredFur.amount * amount
        val requiredFur = Item(clothing.requiredFur.id, amtFurRequired)

        if (!inInventory(player, requiredFur.id, requiredFur.amount)) {
            sendDialogue(player, "You don't have enough fur for that.")
            return
        }

        if (!inInventory(player, coins.id, coins.amount)) {
            sendDialogue(player, "You can't afford that.")
            return
        }

        removeItem(player, requiredFur, Container.INVENTORY)
        removeItem(player, coins, Container.INVENTORY)
        addItem(player, clothing.product.id, amount)
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.put(FUR_CLOTHING_COMPONENT_ID, this)
        return this
    }

    internal enum class FUR_CLOTHING(
        val requiredFur: Item,
        val price: Int,
        val product: Item,
        val textChildID: Int,
        val textContent: String,
    ) {
        POLAR_KEBBIT_TOP(
            requiredFur = Item(Items.POLAR_KEBBIT_FUR_10117, 2),
            price = 20,
            product = POLAR_CAMO_TOP,
            textChildID = 18,
            textContent = "Polar kebbit",
        ),
        POLAR_KEBBIT_BOT(
            requiredFur = Item(Items.POLAR_KEBBIT_FUR_10117, 2),
            price = 20,
            product = POLAR_CAMO_LEGS,
            textChildID = 18,
            textContent = "Polar kebbit",
        ),
        COMMON_KEBBIT_TOP(
            requiredFur = Item(Items.COMMON_KEBBIT_FUR_10121, 2),
            price = 20,
            product = WOOD_CAMO_TOP,
            textChildID = 19,
            textContent = "Common kebbit",
        ),
        COMMON_KEBBIT_BOT(
            requiredFur = Item(Items.COMMON_KEBBIT_FUR_10121, 2),
            price = 20,
            product = WOOD_CAMO_LEGS,
            textChildID = 19,
            textContent = "Common kebbit",
        ),
        FELDIP_WEASEL_TOP(
            requiredFur = Item(Items.FELDIP_WEASEL_FUR_10119, 2),
            price = 20,
            product = JUNGLE_CAMO_TOP,
            textChildID = 20,
            textContent = "Feldip weasel",
        ),
        FELDIP_WEASEL_LEGS(
            requiredFur = Item(Items.FELDIP_WEASEL_FUR_10119, 2),
            price = 20,
            product = JUNGLE_CAMO_LEGS,
            textChildID = 20,
            textContent = "Feldip weasel",
        ),
        DESERT_DEVIL_TOP(
            requiredFur = Item(Items.DESERT_DEVIL_FUR_10123, 2),
            price = 20,
            product = DESERT_CAMO_TOP,
            textChildID = 21,
            textContent = "Desert devil",
        ),
        DESERT_DEVIL_LEGS(
            requiredFur = Item(Items.DESERT_DEVIL_FUR_10123, 2),
            price = 20,
            product = DESERT_CAMO_LEGS,
            textChildID = 21,
            textContent = "Desert devil",
        ),
        LARUPIA_HEAD(
            requiredFur = Item(Items.LARUPIA_FUR_10095),
            price = 500,
            product = LARUPIA_HAT,
            textChildID = 15,
            textContent = "Larupia",
        ),
        LARUPIA_CHEST(
            requiredFur = Item(Items.LARUPIA_FUR_10095),
            price = 100,
            product = LARUPIA_TOP,
            textChildID = 15,
            textContent = "Larupia",
        ),
        LARUPIA_BOT(
            requiredFur = Item(Items.LARUPIA_FUR_10095),
            price = 100,
            product = LARUPIA_LEGS,
            textChildID = 15,
            textContent = "Larupia",
        ),
        GRAAHK_HEAD(
            requiredFur = Item(Items.GRAAHK_FUR_10099),
            price = 750,
            product = GRAAHK_HEADDRESS,
            textChildID = 16,
            textContent = "Graahk",
        ),
        GRAAHK_CHEST(
            requiredFur = Item(Items.GRAAHK_FUR_10099),
            price = 150,
            product = GRAAHK_TOP,
            textChildID = 16,
            textContent = "Graahk",
        ),
        GRAAHK_BOT(
            requiredFur = Item(Items.GRAAHK_FUR_10099),
            price = 150,
            product = GRAAHK_LEGS,
            textChildID = 16,
            textContent = "Graahk",
        ),
        KYATT_HEAD(
            requiredFur = Item(Items.KYATT_FUR_10103),
            price = 1000,
            product = KYATT_HAT,
            textChildID = 17,
            textContent = "Kyatt",
        ),
        KYATT_CHEST(
            requiredFur = Item(Items.KYATT_FUR_10103),
            price = 200,
            product = KYATT_TOP,
            textChildID = 17,
            textContent = "Kyatt",
        ),
        KYATT_BOT(
            requiredFur = Item(Items.KYATT_FUR_10103),
            price = 200,
            product = KYATT_LEGS,
            textChildID = 17,
            textContent = "Kyatt",
        ),
        GLOVES_SILENCE(
            requiredFur = Item(Items.DARK_KEBBIT_FUR_10115, 2),
            price = 600,
            product = GLOVES_OF_SILENCE,
            textChildID = 22,
            textContent = "Dark kebbit",
        ),
        SPOT_CAPE(
            requiredFur = Item(Items.SPOTTED_KEBBIT_FUR_10125, 2),
            price = 400,
            product = SPOTTED_CAPE,
            textChildID = 23,
            textContent = "Spotted kebbit",
        ),
        DASH_CAPE(
            requiredFur = Item(Items.DASHING_KEBBIT_FUR_10127, 2),
            price = 800,
            product = SPOTTIER_CAPE,
            textChildID = 24,
            textContent = "Dashing kebbit",
        ),
    }
}
