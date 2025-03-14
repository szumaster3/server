package content.minigame.mta.handlers

import core.api.*
import core.game.component.CloseEvent
import core.game.component.Component
import core.game.container.Container
import core.game.container.ContainerType
import core.game.container.access.InterfaceContainer
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import org.rs.consts.Components
import org.rs.consts.Items

class MTAShop : InterfaceListener {
    private val container = Container(ITEMS.size, ContainerType.SHOP)

    private val viewers: MutableList<Player> = ArrayList(100)

    private val component: Component =
        Component(Components.MAGICTRAINING_SHOP_197).setCloseEvent(
            CloseEvent { player, c ->
                if (player == null) {
                    return@CloseEvent true
                }
                viewers.remove(player)
                true
            },
        )

    init {
        container.add(*ITEMS)
        Pulser.submit(
            object : Pulse(100) {
                override fun pulse(): Boolean {
                    for (i in container.toArray().indices) {
                        val main = true
                        val item = container.toArray()[i] ?: continue
                        if (main) {
                            if (item.amount < 100) {
                                item.amount += 1
                            }
                            this@MTAShop.update()
                        }
                    }
                    return false
                }
            },
        )
    }

    private fun update() {
        for (p in viewers) {
            if (p == null || !p.isActive) {
                continue
            }
            InterfaceContainer.generateItems(
                p,
                container.toArray(),
                arrayOf("Buy", "Value"),
                Components.MAGICTRAINING_SHOP_197,
                16,
                4,
                7,
            )
        }
    }

    fun buy(
        player: Player,
        item: Item,
        slot: Int,
    ) {
        var item = item
        val prices = PRICES[slot]

        if (item.id == Items.ARENA_BOOK_6891) {
            if (!removeItem(player, Item(Items.COINS_995, 200))) {
                sendMessage(player, "You don't have enough coins.")
            } else {
                addItem(player, Items.ARENA_BOOK_6891)
            }
            return
        }

        if (item.amount < 1) {
            sendMessage(player, "The shop has run out of stock.")
            return
        }
        item = Item(item.id, 1)
        if (!hasSpaceFor(player, item)) {
            sendMessage(player, "You don't have enough inventory space.")
            return
        }
        for (i in prices.indices) {
            if (getPoints(player, i) < prices[i]) {
                sendMessage(player, "You cannot afford that item.")
                return
            }
        }
        if (item.id == Items.BONES_TO_PEACHES_6926 && player.getSavedData().activityData.isBonesToPeaches) {
            sendMessage(player, "You already unlocked that spell.")
            return
        }
        var itemToRemove: ContainerisedItem? = null
        if (slot >= 1 && slot <= 3) {
            val required = ITEMS[slot - 1]
            itemToRemove = hasAnItem(player, required.id)
            if (!itemToRemove.exists() && !player.hasItem(Item(Items.MASTER_WAND_6914, 1))) {
                sendMessage(player, "You don't have the required wand to buy this upgrade.")
                return
            }
        }
        if (container.getAmount(item) - 1 <= 0) {
            container[slot].amount = 0
        } else {
            container.remove(item)
        }
        if (item.id == Items.BONES_TO_PEACHES_6926) {
            player.getSavedData().activityData.isBonesToPeaches = true
            sendDialogue(player, "The Guardian teaches you how to use the Bones to Peaches spell!")
        } else {
            if (itemToRemove == null || itemToRemove.remove()) {
                player.inventory.add(item)
            }
        }
        for (i in prices.indices) {
            decrementPoints(player, i, prices[i])
        }
        updatePoints(player)
        update()
    }

    fun value(
        player: Player,
        item: Item,
        slot: Int,
    ) {
        val prices = PRICES[slot]
        if (item.id != Items.ARENA_BOOK_6891) {
            sendMessage(
                player,
                "The " + item.name + " costs " + prices[0] + " Telekinetic, " + prices[1] + " Alchemist,",
            )
            sendMessage(player, prices[2].toString() + " Enchantment and " + prices[3] + " Graveyard Pizazz Points.")
        } else {
            sendMessage(player, "The arena book costs 200 gold coins.")
        }
    }

    fun updatePoints(player: Player) {
        sendString(player, getPoints(player, 0).toString(), Components.MAGICTRAINING_SHOP_197, 8)

        sendString(player, getPoints(player, 2).toString(), Components.MAGICTRAINING_SHOP_197, 9)

        sendString(player, getPoints(player, 1).toString(), Components.MAGICTRAINING_SHOP_197, 10)

        sendString(player, getPoints(player, 3).toString(), Components.MAGICTRAINING_SHOP_197, 11)
    }

    fun incrementPoints(
        player: Player,
        index: Int,
        increment: Int,
    ) {
        player.getSavedData().activityData.incrementPizazz(index, increment)
    }

    private fun decrementPoints(
        player: Player,
        index: Int,
        decrement: Int,
    ) {
        player.getSavedData().activityData.decrementPizazz(index, decrement)
    }

    fun getPoints(
        player: Player,
        index: Int,
    ): Int {
        return player.getSavedData().activityData.getPizazzPoints(index)
    }

    companion object {
        @JvmStatic
        val ITEMS: Array<Item> =
            arrayOf(
                Item(Items.BEGINNER_WAND_6908, 100),
                Item(Items.APPRENTICE_WAND_6910, 100),
                Item(Items.TEACHER_WAND_6912, 100),
                Item(Items.MASTER_WAND_6914, 100),
                Item(Items.INFINITY_TOP_6916, 100),
                Item(Items.INFINITY_HAT_6918, 100),
                Item(Items.INFINITY_BOOTS_6920, 100),
                Item(Items.INFINITY_GLOVES_6922, 100),
                Item(Items.INFINITY_BOTTOMS_6924, 100),
                Item(Items.MAGES_BOOK_6889, 100),
                Item(Items.BONES_TO_PEACHES_6926, 100),
                Item(Items.BATTLESTAFF_1391, 100),
                Item(Items.MIST_RUNE_4695, 100),
                Item(Items.DUST_RUNE_4696, 100),
                Item(Items.SMOKE_RUNE_4697, 100),
                Item(Items.MUD_RUNE_4698, 100),
                Item(Items.STEAM_RUNE_4694, 100),
                Item(Items.LAVA_RUNE_4699, 100),
                Item(Items.COSMIC_RUNE_564, 100),
                Item(Items.CHAOS_RUNE_562, 100),
                Item(Items.NATURE_RUNE_561, 100),
                Item(Items.DEATH_RUNE_560, 100),
                Item(Items.LAW_RUNE_563, 100),
                Item(Items.SOUL_RUNE_566, 100),
                Item(Items.BLOOD_RUNE_565, 100),
                Item(Items.ARENA_BOOK_6891, 1),
            )

        @JvmStatic
        val PRICES: Array<IntArray> =
            arrayOf(
                intArrayOf(30, 30, 300, 30),
                intArrayOf(60, 60, 600, 60),
                intArrayOf(150, 200, 1500, 150),
                intArrayOf(240, 240, 2400, 240),
                intArrayOf(400, 450, 4000, 400),
                intArrayOf(350, 400, 3000, 350),
                intArrayOf(120, 120, 1200, 120),
                intArrayOf(175, 225, 1500, 175),
                intArrayOf(450, 500, 5000, 450),
                intArrayOf(500, 550, 6000, 500),
                intArrayOf(200, 300, 2000, 200),
                intArrayOf(1, 2, 20, 2),
                intArrayOf(1, 1, 15, 1),
                intArrayOf(1, 1, 15, 1),
                intArrayOf(1, 1, 15, 1),
                intArrayOf(1, 1, 15, 1),
                intArrayOf(1, 1, 15, 1),
                intArrayOf(1, 1, 15, 1),
                intArrayOf(0, 0, 5, 0),
                intArrayOf(0, 1, 5, 1),
                intArrayOf(0, 1, 0, 1),
                intArrayOf(2, 1, 20, 1),
                intArrayOf(2, 0, 0, 0),
                intArrayOf(2, 2, 25, 2),
                intArrayOf(2, 2, 25, 2),
                intArrayOf(0, 0, 0, 0),
            )
    }

    override fun defineInterfaceListeners() {
        onOpen(Components.MAGICTRAINING_SHOP_197) { player, _ ->
            viewers.add(player)
            update()
            updatePoints(player)
            Pulser.submit(
                object : Pulse(1, player) {
                    override fun pulse(): Boolean {
                        updatePoints(player)
                        return true
                    }
                },
            )
            return@onOpen true
        }

        onClose(Components.MAGICTRAINING_SHOP_197) { player, _ ->
            viewers.remove(player)
            update()
            return@onClose true
        }

        on(Components.MAGICTRAINING_SHOP_197) { player, _, opcode, _, slot, _ ->
            val item = container[slot] ?: return@on true
            if (opcode == 155) {
                value(player, item, slot)
            } else if (opcode == 196) {
                buy(player, item, slot)
            }
            update()
            return@on true
        }
    }
}
