package content.global.plugins.iface.appearance

import core.api.*
import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.appearance.Gender
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Components
import shared.consts.Items
import shared.consts.NPCs

private const val MALE_CHILD_ID = 90
private const val FEMALE_CHILD_ID = 92
private const val TEXT_CHILD = 88

private val skinColorButtons = (93..100)

/**
 * Represents the Makeover interface.
 * @author Ceikry
 */
@Initializable
class MakeoverMageInterface : ComponentPlugin() {

    override fun open(player: Player?, component: Component?) {
        component ?: return
        player ?: return
        super.open(player, component)

        sendNpcOnInterface(player, 1, component.id, MALE_CHILD_ID)
        sendNpcOnInterface(player,5, component.id, FEMALE_CHILD_ID)

        sendAnimationOnInterface(player, FaceAnim.NEUTRAL.animationId, component.id, MALE_CHILD_ID)
        sendAnimationOnInterface(player, FaceAnim.NEUTRAL.animationId, component.id, FEMALE_CHILD_ID)

        if (inInventory(player, Items.MAKEOVER_VOUCHER_5606)) {
            sendString(player, "USE MAKEOVER VOUCHER", component.id, TEXT_CHILD)
        }

        val currentSkin = player.appearance.skin.color
        setAttribute(player, "mm-previous", currentSkin)
        setVarp(player, 262, currentSkin)

        player.toggleWardrobe(true)
        component.setUncloseEvent { pl, _ ->
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
                removeAttribute(player, "mm-paid")
                refreshAppearance(player)
            }
            removeAttribute(pl, "mm-previous")
            removeAttribute(pl, "mm-gender")
            true
        }
    }

    override fun handle(player: Player?, component: Component?, opcode: Int, button: Int, slot: Int, itemId: Int): Boolean {
        player ?: return false
        if (skinColorButtons.contains(button)) {
            updateInterfaceConfigs(player, button)
            return true
        }
        when (button) {
            113, 101 -> updateGender(player, true)
            114, 103 -> updateGender(player, false)
            TEXT_CHILD -> pay(player)
        }

        return true
    }

    private fun updateGender(player: Player, male: Boolean) {
        setAttribute(player, "mm-gender", if (male) Gender.MALE.ordinal else Gender.FEMALE.ordinal)
    }

    private fun pay(player: Player) {
        val oldGender = player.appearance.gender
        val newColor = player.getAttribute("mm-previous", player.appearance.skin.color)
        val newGender = Gender.values()[player.getAttribute("mm-gender", oldGender.ordinal)]

        if (newColor == player.appearance.skin.color && newGender == oldGender) {
            closeInterface(player)
            return
        }

        val currency = if (inInventory(player, Items.MAKEOVER_VOUCHER_5606)) {
            Item(Items.MAKEOVER_VOUCHER_5606, 1)
        } else {
            Item(Items.COINS_995, 3000)
        }

        if (player.inventory.containsItem(currency)) {
            setAttribute(player, "mm-paid", true)
            removeItem(player, currency)
            closeInterface(player)

            val npc = findNPC(NPCs.MAKE_OVER_MAGE_2676)
            if (npc!=null && oldGender != newGender) {
                when {
                    oldGender == Gender.MALE && newGender == Gender.FEMALE -> {
                        sendChat(npc, "Ooh!")
                        npc.transform(NPCs.MAKE_OVER_MAGE_2676)
                    }

                    oldGender == Gender.FEMALE && newGender == Gender.MALE -> {
                        sendChat(npc, "Aha!")
                        npc.transform(NPCs.MAKE_OVER_MAGE_599)
                    }
                }

                object : Pulse(5) {
                    override fun pulse(): Boolean {
                        npc.transform(NPCs.MAKE_OVER_MAGE_599)
                        return true
                    }
                }
            }
        } else {
            sendDialogue(player, "You cannot afford that.")
        }
    }

    private fun updateInterfaceConfigs(player: Player, button: Int) {
        val newIndex = when (button) {
            in 93..99 -> button - 92
            100 -> 8
            else -> return
        }

        setAttribute(player, "mm-previous", button - skinColorButtons.first)
        setVarp(player, 262, newIndex)
    }


    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.put(Components.MAKEOVER_MAGE_205, this)
        return this
    }
}
