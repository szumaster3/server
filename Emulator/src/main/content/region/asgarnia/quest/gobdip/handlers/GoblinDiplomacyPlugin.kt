package content.region.asgarnia.quest.gobdip.handlers

import content.region.asgarnia.quest.gobdip.handlers.GoblinDiplomacyPlugin.GoblinMailPlugin.GoblinMail
import core.api.removeItem
import core.api.replaceSlot
import core.api.sendMessage
import core.api.setAttribute
import core.cache.def.impl.ItemDefinition
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.NodeUsageEvent
import core.game.interaction.OptionHandler
import core.game.interaction.UseWithHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.world.GameWorld.ticks
import core.plugin.ClassScanner.definePlugin
import core.plugin.Plugin
import core.tools.StringUtils
import java.util.*

class GoblinDiplomacyPlugin : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        ItemDefinition.forId(288).handlers["option:wear"] = this
        for (`object` in CRATES) {
            SceneryDefinition.forId(`object`).handlers["option:search"] = this
        }
        for (mail in GoblinMail.values()) {
            ItemDefinition.forId(mail.product.id).handlers["option:wear"] = this
        }
        definePlugin(GoblinMailPlugin())
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        val id = if (node is Item) node.getId() else node.id
        when (option) {
            "wear" -> player.packetDispatch.sendMessage("That armour is to small for a human.")
        }
        when (id) {
            16559, 16557, 16561, 16560 -> {
                if (player.getAttribute("crate:$id", 0) < ticks) {
                    setAttribute(player, "crate:$id", ticks + 500)
                    if (!player.inventory.add(GOBLIN_MAIL)) {
                        GroundItemManager.create(GOBLIN_MAIL, player)
                    }
                    player.dialogueInterpreter.sendItemMessage(GOBLIN_MAIL.id, "You find some goblin armour.")
                    return true
                }
                player.packetDispatch.sendMessage("You search the crate but find nothing.")
            }
        }
        return true
    }

    override fun isWalk(): Boolean {
        return false
    }

    override fun isWalk(
        player: Player,
        node: Node,
    ): Boolean {
        return node !is Item
    }

    class GoblinMailPlugin : UseWithHandler(1763, 1769, 1765, 1771, 1767, 1773, 4622, 11808, 6955) {
        override fun newInstance(arg: Any?): Plugin<Any> {
            addHandler(288, ITEM_TYPE, this)
            val ids = intArrayOf(1763, 1769, 1765, 1771, 1767, 1773, 4622, 11808, 6955)
            for (i in ids) {
                addHandler(i, ITEM_TYPE, this)
            }
            return this
        }

        override fun handle(event: NodeUsageEvent): Boolean {
            val player = event.player
            val mail = GoblinMail.forItem(event.usedItem)
            val dye = Dyes.forItem(event.usedItem, event.baseItem)
            if (dye != null && (event.usedItem.id != 288 || event.baseItem.id != 288)) {
                handleDyeMix(player, dye, event)
                return true
            }
            if (mail == null || (event.usedItem.id != 288 && event.baseItem.id != 288)) {
                return false
            }
            if (removeItem(player, mail.dye)) {
                replaceSlot(player, event.baseItem.index, mail.product)
                sendMessage(player, "You dye the goblin armour " + mail.name.lowercase(Locale.getDefault()) + ".")
            }
            return true
        }

        fun handleDyeMix(
            player: Player,
            dye: Dyes?,
            event: NodeUsageEvent?,
        ) {
            if (dye == null) {
                player.packetDispatch.sendMessage("Those dyes dont mix together.")
                return
            }
            if (player.inventory.remove(*dye.materials)) {
                player.inventory.add(dye.product)
            }
            player.packetDispatch.sendMessage(
                "You mix the two dyes and make " +
                    (
                        if (StringUtils.isPlusN(
                                dye.product.name.lowercase(Locale.getDefault()).replace("dye", "").trim {
                                    it <=
                                        ' '
                                },
                            )
                        ) {
                            "an "
                        } else {
                            "a "
                        }
                    ) +
                    dye.product.name
                        .lowercase(Locale.getDefault())
                        .replace("dye", "")
                        .trim { it <= ' ' } +
                    " one.",
            )
        }

        enum class GoblinMail(
            val dye: Item,
            val product: Item,
        ) {
            RED(dye = Item(1763), product = Item(9054)),
            ORANGE(dye = Item(1769), product = Item(286)),
            YELLOW(dye = Item(1765), product = Item(9056)),
            GREEN(dye = Item(1771), product = Item(9057)),
            BLUE(dye = Item(1767), product = Item(287)),
            PURPLE(dye = Item(1773), product = Item(9058)),
            BLACK(dye = Item(4622), product = Item(9055)),
            WHITE(dye = Item(11808), product = Item(11791)),
            PINK(dye = Item(6955), product = Item(9059)),
            ;

            companion object {
                @JvmStatic
                fun forItem(item: Item): GoblinMail? {
                    for (mail in values()) {
                        if (mail.dye.id == item.id) {
                            return mail
                        }
                    }
                    return null
                }
            }
        }

        enum class Dyes(
            val product: Item,
            vararg materials: Item,
        ) {
            ORANGE(Item(1769), Item(1763), Item(1765)),
            GREEN(Item(1771), Item(1765), Item(1767)),
            PURPLE(Item(1773), Item(1767), Item(1763)),
            ;

            val materials: Array<Item> = materials as Array<Item>

            companion object {
                @JvmStatic
                fun forItem(
                    item: Item,
                    second: Item,
                ): Dyes? {
                    for (dye in values()) {
                        if (dye.materials[0].id == item.id &&
                            dye.materials[1].id == second.id ||
                            dye.materials[0].id == second.id &&
                            dye.materials[1].id == item.id
                        ) {
                            return dye
                        }
                    }
                    return null
                }
            }
        }
    }

    companion object {
        private val GOBLIN_MAIL = Item(288)
        private val CRATES = intArrayOf(16557, 16561, 16560, 16559)
    }
}
