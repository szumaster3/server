package content.global.handlers.iface

import core.api.*
import core.api.ui.sendInterfaceConfig
import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.appearance.Gender
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Components
import org.rs.consts.Items

private const val MALE_CHILD_ID = 90
private const val FEMALE_CHILD_ID = 92
private const val TEXT_CHILD = 88

private val skincolorButtons = (93..100)

@Initializable
class MakeOverInterface : ComponentPlugin() {
    override fun open(
        player: Player?,
        component: Component?,
    ) {
        component ?: return
        player ?: return
        super.open(player, component)

        player.packetDispatch.sendNpcOnInterface(1, component.id, MALE_CHILD_ID)
        player.packetDispatch.sendNpcOnInterface(5, component.id, FEMALE_CHILD_ID)

        sendAnimationOnInterface(player, FaceAnim.SILENT.animationId, component.id, MALE_CHILD_ID)
        sendAnimationOnInterface(player, FaceAnim.SILENT.animationId, component.id, FEMALE_CHILD_ID)

        if (inInventory(player, Items.MAKEOVER_VOUCHER_5606)) {
            sendString(player, "USE MAKEOVER VOUCHER", component.id, TEXT_CHILD)
        }

        val currentSkin = player.appearance.skin.color
        setAttribute(player, "mm-previous", currentSkin)
        sendInterfaceConfig(player, component.id, skincolorButtons.first + currentSkin, true)

        player.toggleWardrobe(true)
        component.setCloseEvent { pl, _ ->
            pl.toggleWardrobe(false)
            if (getAttribute(player, "mm-paid", false)) {
                val newColor = player.getAttribute("mm-previous", -1)
                val newGender = player.getAttribute("mm-gender", -1)
                if (newColor > -1) {
                    player.appearance.skin.changeColor(newColor)
                }
                if (newGender > -1) {
                    player.appearance.changeGender(Gender.values()[newGender])
                    player.appearance.skin.changeColor(newColor)
                }
                player.appearance.sync()
                removeAttribute(player, "mm-paid")
            }
            removeAttribute(pl, "mm-previous")
            removeAttribute(pl, "mm-gender")
            true
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
        if (skincolorButtons.contains(button)) {
            updateInterfaceConfigs(player, button)
            return true
        }
        when (button) {
            113, 101 -> updateGender(player, true)
            114, 103 -> updateGender(player, false)
            88 -> pay(player)
        }

        return true
    }

    fun updateGender(
        player: Player,
        male: Boolean,
    ) {
        setAttribute(player, "mm-gender", if (male) Gender.MALE.ordinal else Gender.FEMALE.ordinal)
    }

    fun pay(player: Player) {
        val newColor = player.getAttribute("mm-previous", player.appearance.skin.color)
        val newGender = player.getAttribute("mm-gender", player.appearance.gender.ordinal)

        if (newColor == player.appearance.skin.color && Gender.values()[newGender] == player.appearance.gender) {
            closeInterface(player)
        } else {
            val currency =
                if (inInventory(player, Items.MAKEOVER_VOUCHER_5606)) {
                    Item(Items.MAKEOVER_VOUCHER_5606, 1)
                } else {
                    Item(Items.COINS_995, 3000)
                }

            if (player.inventory.containsItem(currency)) {
                setAttribute(player, "mm-paid", true)
                removeItem(player, currency)
                closeInterface(player)
            } else {
                sendDialogue(player, "You can not afford that.")
            }
        }
    }

    fun updateInterfaceConfigs(
        player: Player,
        button: Int,
    ) {
        val old = player.getAttribute("mm-previous", 0)
        setAttribute(player, "mm-previous", button - skincolorButtons.first)
        sendInterfaceConfig(player, Components.MAKEOVER_MAGE_205, old + skincolorButtons.first, false)
        sendInterfaceConfig(player, Components.MAKEOVER_MAGE_205, button, true)
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.put(Components.MAKEOVER_MAGE_205, this)
        return this
    }
}
