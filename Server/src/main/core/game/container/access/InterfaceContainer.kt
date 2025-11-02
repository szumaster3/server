package core.game.container.access

import core.api.IfaceSettingsBuilder
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.net.packet.PacketRepository
import core.net.packet.context.ContainerContext
import core.net.packet.out.ContainerPacket

/**
 * Utility for generating item containers and options on game interfaces.
 *
 * @author Stacx
 * @date 5/02/2013
 */
object InterfaceContainer {

    /**
     * The client script index used for setting options on interfaces.
     */
    private const val CLIENT_SCRIPT_INDEX = 150

    /**
     * Internal container key index, incremented with each generated container.
     */
    private var index = 600

    /**
     * Internal function to send items and right-click options to an interface.
     *
     * @param player The target player.
     * @param items List of items to display in the interface. Can contain nulls.
     * @param options Right-click options for the items. Defaults to empty list.
     * @param interfaceIndex The interface id.
     * @param childIndex The child index where the items are displayed.
     * @param x Number of items per row.
     * @param y Number of item rows.
     * @param key The container key.
     * @return The container key used for this interface.
     */
    private fun generate(player: Player, items: List<Item?>, options: List<String> = emptyList(), interfaceIndex: Int, childIndex: Int, x: Int, y: Int, key: Int): Int {
        val scriptArgs = arrayOfNulls<Any>(options.size + 7)

        for (i in options.indices) {
            scriptArgs[i] = options[i]
        }

        scriptArgs[options.size] = -1
        scriptArgs[options.size + 1] = 0
        scriptArgs[options.size + 2] = x
        scriptArgs[options.size + 3] = y
        scriptArgs[options.size + 4] = key
        scriptArgs[options.size + 5] = interfaceIndex shl 16 or childIndex
//      scriptArgs[options.size + 6] = 0

        player.packetDispatch.sendRunScript(
            CLIENT_SCRIPT_INDEX,
            generateScriptArguments(options.size),
            *scriptArgs
        )

        val itemArray = Array(items.size) { i -> items[i] ?: Item() }
        val settings = IfaceSettingsBuilder().enableAllOptions().build()
        player.packetDispatch.sendIfaceSettings(settings, childIndex, interfaceIndex, 0, items.size)
        PacketRepository.send(ContainerPacket::class.java, ContainerContext(player, -1, -2, key, itemArray, items.size, false))
        return increment()
    }

    /**
     * Sends only options without items to an interface.
     *
     * @param player The target player.
     * @param options Right-click options for the interface.
     * @param interfaceIndex The interface id.
     * @param childIndex The child index where options will be displayed.
     * @param x Number of items per row.
     * @param y Number of item rows.
     * @param key The container key.
     * @return The container key used for this interface.
     */
    fun generateOptions(player: Player, options: List<String> = emptyList(), interfaceIndex: Int, childIndex: Int, x: Int, y: Int, key: Int): Int = generate(player, emptyList(), options, interfaceIndex, childIndex, x, y, key)

    /**
     * Generates options for the interface with a default 7x3 grid.
     *
     * @param player The target player.
     * @param interfaceId The interface id.
     * @param childId The child index.
     * @param itemLength Number of items the interface will display.
     * @param options Right-click options.
     * @return The container key used for this interface.
     */
    fun generate(player: Player, interfaceId: Int, childId: Int, itemLength: Int, vararg options: String): Int =
        generate(player, interfaceId, childId, itemLength, 7, 3, options.toList())

    /**
     * Generates options for the interface with custom grid size.
     *
     * @param player The target player.
     * @param interfaceId The interface id.
     * @param childId The child index.
     * @param itemLength Number of items the interface will display.
     * @param x Number of items per row.
     * @param y Number of item rows.
     * @param options Right-click options.
     * @return The container key used for this interface.
     */
    fun generate(player: Player, interfaceId: Int, childId: Int, itemLength: Int, x: Int, y: Int, options: List<String>): Int = generate(player, List(itemLength) { Item() }, options, interfaceId, childId, x, y, increment())

    /**
     * Increments the container key index and wraps at 6999.
     *
     * @return The previous index value.
     */
    private fun increment(): Int {
        if (index == 6999) index = 600
        return index++
    }

    /**
     * Builds the script argument type string for the client.
     *
     * @param length Number of right-click options.
     * @return The script argument string.
     */
    private fun generateScriptArguments(length: Int): String = buildString {
        append("IviiiI")
        repeat(length) { append("s") }
    }

    /**
     * Sends a list of items with optional right-click options to an interface.
     *
     * @param player The target player.
     * @param items List of items to display.
     * @param options Right-click options (default empty list).
     * @param interfaceIndex The interface id.
     * @param childIndex The child index.
     * @param x Number of items per row (default 7).
     * @param y Number of item rows (default 3).
     * @param key The container key (default incremented).
     * @return The container key used for this interface.
     */
    fun generateItems(player: Player, items: List<Item?>, options: List<String> = emptyList(), interfaceIndex: Int, childIndex: Int, x: Int = 7, y: Int = 3, key: Int = increment()): Int = generate(player, items, options, interfaceIndex, childIndex, x, y, key)

    /**
     * Player extension to send inventory items easily to an interface.
     *
     * @param interfaceId The interface id.
     * @param childId The child index.
     * @param options Right-click options (default empty list).
     * @param x Items per row (default 7).
     * @param y Rows of items (default 3).
     * @param key Container key (default 0 → auto-incremented).
     * @return The container key used for this interface.
     */
    fun Player.generateItems(interfaceId: Int, childId: Int, options: List<String> = emptyList(), x: Int = 7, y: Int = 3, key: Int = 0): Int {
        val actualKey = if (key == 0) increment() else key
        return generateItems(this, inventory.toList(), options, interfaceId, childId, x, y, actualKey)
    }

    /**
     * Player extension to send arbitrary items (like rewards) to an interface.
     *
     * @param items Items to display.
     * @param interfaceId The interface id.
     * @param childId The child index.
     * @param options Right-click options (default empty list).
     * @param x Items per row (default 7).
     * @param y Rows of items (default 3).
     * @param key Container key (default 0 → auto-incremented).
     * @return The container key used for this interface.
     */
    fun Player.generateItems(items: List<Item?>, interfaceId: Int, childId: Int, options: List<String> = emptyList(), x: Int = 7, y: Int = 3, key: Int = 0): Int {
        val actualKey = if (key == 0) increment() else key
        return generateItems(this, items, options, interfaceId, childId, x, y, actualKey)
    }
}
