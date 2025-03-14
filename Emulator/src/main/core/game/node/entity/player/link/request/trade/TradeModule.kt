package core.game.node.entity.player.link.request.trade

import core.api.sendString
import core.api.setVarp
import core.game.bots.AIRepository
import core.game.bots.impl.DoublingMoney
import core.game.component.Component
import core.game.container.Container
import core.game.container.ContainerType
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.PlayerMonitor
import core.game.node.entity.player.link.request.RequestModule
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import org.rs.consts.Components
import java.text.DecimalFormat
import java.util.*

class TradeModule(
    player: Player?,
    target: Player?,
) : RequestModule {
    var isRetained = false

    var player: Player? = player
        private set

    var target: Player? = target
        private set

    var container: TradeContainer? = null
        private set

    var isAccepted = false
        private set

    var isModified = false

    var stage = 0
        private set

    override fun open(
        player: Player?,
        target: Player?,
    ) {
        extend(player, target)
        if (getExtension(target) == null || getExtension(player) == null) {
            return
        }
        getExtension(player)!!.openInterface(getInterface(stage)).display(stage)
        if (getExtension(target) == null || getExtension(player) == null) {
            return
        }
        getExtension(target)!!.openInterface(getInterface(stage)).display(stage)
        player!!.dialogueInterpreter.close()
        target!!.dialogueInterpreter.close()
    }

    fun update() {
        display(stage)
        getExtension(target)!!.display(stage)
    }

    private fun openInterface(component: Component): TradeModule {
        return if (component === MAIN_INTERFACE) openMain() else openSecond()
    }

    private val acceptMessage: String
        get() {
            val otherAccept = getExtension(target)!!.isAccepted
            return if (!otherAccept &&
                !isAccepted
            ) {
                ""
            } else if (otherAccept) {
                "Other player has accepted."
            } else {
                "Waiting for other player..."
            }
        }

    private fun display(stage: Int): TradeModule {
        when (stage) {
            0 -> {
                for (i in HIDDEN_CHILDS) {
                    player!!.packetDispatch.sendString("", MAIN_INTERFACE.id, i)
                }
                player!!.packetDispatch.sendString("Trading With: " + target!!.username, Components.TRADEMAIN_335, 15)
                player!!.packetDispatch.sendString(
                    target!!.username + " has " +
                        (if (target!!.inventory.freeSlots() == 0) "no" else target!!.inventory.freeSlots()) +
                        " free inventory slots.",
                    Components.TRADEMAIN_335,
                    21,
                )
                player!!.packetDispatch.sendString(acceptMessage, Components.TRADEMAIN_335, 36)
            }

            1 -> {
                player!!.packetDispatch.sendString(
                    "<col=00FFFF>Trading with:<br>" + "<col=00FFFF>" +
                        target!!
                            .username
                            .substring(
                                0,
                                1,
                            ).uppercase(Locale.getDefault()) + target!!.username.substring(1),
                    Components.TRADECONFIRM_334,
                    2,
                )
                val acceptMessage = acceptMessage
                if (acceptMessage !== "") {
                    sendString(player!!, acceptMessage, Components.TRADECONFIRM_334, 33)
                }
                player!!.interfaceManager.restoreTabs()
            }
        }
        displayModified(stage)
        container!!.update(true)
        return this
    }

    fun getInterface(stage: Int = this.stage): Component {
        return if (stage == 0) MAIN_INTERFACE else ACCEPT_INTERFACE
    }

    fun getInterface(): Component {
        return getInterface(stage)
    }

    private fun displayModified(stage: Int) {
        val otherModified = getExtension(target)!!.isModified
        if (stage == 0) {
            // Main interface
            if (otherModified) {
                setVarp(player!!, 1043, 1)
            }
            if (isModified) {
                setVarp(player!!, 1042, 1)
            }
        } else {
            // Confirm
            player!!.packetDispatch.sendInterfaceConfig(Components.TRADECONFIRM_334, 45, !isModified)
            player!!.packetDispatch.sendInterfaceConfig(Components.TRADECONFIRM_334, 46, !otherModified)
        }
    }

    fun alert(slot: Int) {
        player!!.packetDispatch.sendRunScript(143, "Iiii", *arrayOf<Any>(slot, 7, 4, 21954594))
        target!!.packetDispatch.sendRunScript(143, "Iiii", *arrayOf<Any>(slot, 7, 4, 21954593))
    }

    fun decline() {
        player!!.interfaceManager.close()
        target!!.packetDispatch.sendMessage("<col=FF0000>Other player has declined trade!</col>")
    }

    private fun nextStage() {
        if (isAccepted && getExtension(target)!!.isAccepted) {
            if (stage == 0) {
                if (!hasSpace()) {
                    return
                }
                incrementStage()
                openInterface(getInterface(stage))
                getExtension(target)!!.incrementStage()
                getExtension(target)!!.openInterface(getInterface(stage))
                getExtension(target)!!.setAccepted(false, false)
                setAccepted(false, false)
            } else {
                giveContainers(this)
                incrementStage()
                getExtension(target)!!.incrementStage()
                getExtension(target)!!.setAccepted(false, false)
                setAccepted(false, false)
                player!!.interfaceManager.close()
                return
            }
        }
        update()
    }

    private fun openMain(): TradeModule {
        player!!.interfaceManager.closeDefaultTabs()
        player!!.interfaceManager.open(MAIN_INTERFACE)
        player!!.interfaceManager.openSingleTab(OVERLAY_INTERFACE)
        player!!.inventory.refresh()
        player!!.packetDispatch.sendIfaceSettings(1278, 30, Components.TRADEMAIN_335, 0, 27)
        player!!.packetDispatch.sendIfaceSettings(1026, 32, Components.TRADEMAIN_335, 0, 27)
        player!!.packetDispatch.sendIfaceSettings(1278, 0, Components.TRADESIDE_336, 0, 27)
        player!!.packetDispatch.sendIfaceSettings(2360446, 0, Components.TRADEMAIN_335, 0, 27)
        player!!.packetDispatch.sendRunScript(150, "IviiiIsssssssss", *INVENTORY_PARAMS)
        player!!.packetDispatch.sendRunScript(150, "IviiiIsssssssss", *TRADE_PARAMS)
        player!!.packetDispatch.sendRunScript(695, "IviiiIsssssssss", *PARTENER_PARAMS)
        return this
    }

    private fun openSecond(): TradeModule {
        player!!.interfaceManager.open(ACCEPT_INTERFACE)
        player!!.interfaceManager.closeSingleTab()
        displayOffer(container, 37)
        displayOffer(getExtension(target)!!.container, 41)
        return this
    }

    fun setAccepted(
        accept: Boolean,
        update: Boolean,
    ) {
        isAccepted = accept
        if (update) {
            nextStage()
        }
    }

    private fun displayOffer(
        container: Container?,
        child: Int,
    ) {
        val split = container!!.itemCount() > 14
        if (split) {
            player!!.packetDispatch.sendInterfaceConfig(Components.TRADECONFIRM_334, child + 1, false)
            player!!.packetDispatch.sendInterfaceConfig(Components.TRADECONFIRM_334, child + 2, false)
            val messages =
                arrayOf(
                    getDisplayMessage(splitList(container.toArray(), 0, 14)),
                    getDisplayMessage(
                        splitList(
                            container.toArray(),
                            14,
                            container.toArray().size,
                        ),
                    ),
                )
            player!!.packetDispatch.sendString(messages[0], Components.TRADECONFIRM_334, child + 1)
            player!!.packetDispatch.sendString(messages[1], Components.TRADECONFIRM_334, child + 2)
        } else {
            player!!.packetDispatch.sendInterfaceConfig(Components.TRADECONFIRM_334, child, false)
            player!!.packetDispatch.sendString(
                getDisplayMessage(container.toArray()),
                Components.TRADECONFIRM_334,
                child,
            )
        }
    }

    private fun getDisplayMessage(items: Array<Item?>): String {
        var message = "Absolutely nothing!"
        if (items.isNotEmpty()) {
            message = ""
            for (i in items.indices) {
                if (items[i] == null) {
                    continue
                }
                message = message + "<col=FF9040>" + items[i]!!.name
                if (items[i]!!.amount > 1) {
                    message = "$message<col=FFFFFF> x "
                    message = message + "<col=FFFF00>" + getFormattedNumber(items[i]!!.amount) + "<br>"
                } else {
                    message = "$message<br>"
                }
            }
        }
        return message
    }

    private fun hasSpace(): Boolean {
        var hasSpace = true
        if (!player!!.inventory.hasSpaceFor(getExtension(target)!!.container)) {
            target!!.packetDispatch.sendMessage(
                "Other player doesn't have enough space in their inventory for this trade.",
            )
            player!!.packetDispatch.sendMessage("You don't have enough inventory space for this.")
            hasSpace = false
        } else if (!target!!.inventory.hasSpaceFor(container)) {
            player!!.packetDispatch.sendMessage(
                "Other player doesn't have enough space in their inventory for this trade.",
            )
            target!!.packetDispatch.sendMessage("You don't have enough inventory space for this.")
            hasSpace = false
        }
        if (!hasSpace) {
            setAccepted(false, true)
            getExtension(target)!!.setAccepted(false, true)
        }
        return hasSpace
    }

    private fun getFormattedNumber(amount: Int): String {
        return DecimalFormat("#,###,##0").format(amount.toLong()).toString()
    }

    private fun giveContainers(module: TradeModule) {
        val pContainer: Container = module.container ?: return
        val oContainer: Container = getExtension(module.target)!!.container ?: return

        PlayerMonitor.logTrade(module.player!!, module.target!!, pContainer, oContainer)

        (AIRepository.PulseRepository[module.player!!.username.lowercase()]?.botScript as DoublingMoney?)
            ?.itemsReceived(
                module.target!!,
                oContainer,
            )
        (AIRepository.PulseRepository[module.target!!.username.lowercase()]?.botScript as DoublingMoney?)
            ?.itemsReceived(
                module.player!!,
                pContainer,
            )

        addContainer(module.player, oContainer)
        addContainer(module.target, pContainer)
        module.target!!.packetDispatch.sendMessage("Accepted trade.")
        module.player!!.packetDispatch.sendMessage("Accepted trade.")
    }

    private fun addContainer(
        player: Player?,
        container: Container?,
    ) {
        val c = Container(container!!.itemCount(), ContainerType.ALWAYS_STACK)
        c.addAll(container)
        for (i in container.toArray()) {
            if (i == null) {
                continue
            }
            if (i.amount > player!!.inventory.getMaximumAdd(i)) {
                i.amount = player.inventory.getMaximumAdd(i)
            }
            if (!player.inventory.add(i)) {
                GroundItemManager.create(i, player)
            }
        }
    }

    private fun splitList(
        items: Array<Item?>,
        min: Int,
        max: Int,
    ): Array<Item?> {
        val list: MutableList<Item?> = ArrayList(20)
        for (i in min until max) {
            if (items[i] == null) {
                continue
            }
            list.add(items[i])
        }
        val array = arrayOfNulls<Item>(list.size)
        for (i in list.indices) {
            if (list[i] == null) {
                continue
            }
            array[i] = list[i]
        }
        return array
    }

    fun incrementStage() {
        stage++
    }

    companion object {
        val OVERLAY_INTERFACE = Component(336)

        val MAIN_INTERFACE: Component = Component(335).setCloseEvent(TradeCloseEvent())

        val ACCEPT_INTERFACE: Component = Component(334).setCloseEvent(TradeCloseEvent())

        val INVENTORY_PARAMS =
            arrayOf<Any>(
                "",
                "",
                "",
                "Lend",
                "Offer-X",
                "Offer-All",
                "Offer-10",
                "Offer-5",
                "Offer",
                -1,
                0,
                7,
                4,
                93,
                336 shl 16,
            )

        val TRADE_PARAMS =
            arrayOf<Any>(
                "",
                "",
                "",
                "",
                "Remove-X",
                "Remove-All",
                "Remove-10",
                "Remove-5",
                "Remove",
                -1,
                0,
                7,
                4,
                90,
                335 shl 16 or 30,
            )

        val PARTENER_PARAMS = arrayOf<Any>("", "", "", "", "", "", "", "", "", -1, 0, 7, 4, 91, 335 shl 16 or 32)

        val HIDDEN_CHILDS = intArrayOf(42, 43, 44, 42, 44, 40, 41)

        fun extend(
            player: Player?,
            target: Player?,
        ) {
            player!!.addExtension(TradeModule::class.java, TradeModule(player, target))
            target!!.addExtension(TradeModule::class.java, TradeModule(target, player))
        }

        @JvmStatic
        fun getExtension(player: Player?): TradeModule? {
            return player!!.getExtension(TradeModule::class.java)
        }
    }

    init {
        container = TradeContainer(player)
    }
}
