package content.global.plugins.interfaces.player_kit

import core.api.*
import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Components
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Sounds

/**
 * Represents the Yrsa Shoe Store.
 */
@Initializable
class YrsaStoreInterface : ComponentPlugin() {
    private val paymentCheck = "yrsa-paid"
    private val previousColor = "yrsa-previous"
    private val pictureId = intArrayOf(
        Items.PICTURE_3680,
        Items.PICTURE_3681,
        Items.PICTURE_3682,
        Items.PICTURE_3683,
        Items.PICTURE_3684,
        Items.PICTURE_3685
    )
    private val selectButtonId = intArrayOf(15, 16, 17, 18, 19, 20)
    private val colorId = intArrayOf(0, 1, 2, 3, 4, 5)
    private val shopInterface = Components.YRSA_SHOE_STORE_200

    /**
     * Opens the shoe store interface.
     *
     * @param player The player opening the interface.
     * @param component The interface component instance.
     */
    override fun open(
        player: Player,
        component: Component?,
    ) {
        component ?: return
        super.open(player, component)

        val originalColor = player.appearance.feet.color
        setAttribute(player, previousColor, originalColor)
        playGlobalAudio(player.location, Sounds.WARDROBE_OPEN_96, 1)
        for (
        i in selectButtonId.indices.also {
            for (i in pictureId.indices) {
                sendItemOnInterface(player, shopInterface, selectButtonId[i], pictureId[i])
                if (!player.houseManager.isInHouse(player)) {
                    sendString(player, "CONFIRM (500 GOLD)", Components.YRSA_SHOE_STORE_200, 14)
                } else {
                    sendString(player, "CONFIRM (FREE)", Components.YRSA_SHOE_STORE_200, 14)
                }
            }
        }
        ) {
            player.toggleWardrobe(true)
        }

        component.setUncloseEvent { p, _ ->
            p.toggleWardrobe(false)
            playGlobalAudio(player.location, Sounds.WARDROBE_CLOSE_95, 1)
            if (!getAttribute(player, paymentCheck, false)) {
                p.appearance.feet.changeColor(getAttribute(p, previousColor, originalColor))
                refreshAppearance(player)
            }

            refreshAppearance(player)
            removeAttribute(p, paymentCheck)
            true
        }

        refreshAppearance(player)
        removeAttribute(player, previousColor)
    }

    /**
     * Handles interaction events in the shoe store interface.
     */
    override fun handle(
        player: Player?,
        component: Component?,
        opcode: Int,
        button: Int,
        slot: Int,
        itemId: Int,
    ): Boolean {
        player ?: return false
        when (button) {
            14 -> pay(player)
            else ->
                when (component?.id) {
                    shopInterface -> {
                        if (selectButtonId.contains(button)) {
                            updateFeet(player, button)
                            return true
                        }
                    }
                }
        }
        return true
    }

    /**
     * Processes the payment for shoe color change.
     *
     * @param player The player making the payment.
     */
    private fun pay(player: Player) {
        val newColor = getAttribute(player, previousColor, player.appearance.skin.color)

        if (newColor == player.appearance.feet.color) {
            closeInterface(player)
            return
        }

        if (!player.houseManager.isInHouse(player)) {
            val paymentSuccessful = removeItem(player, Item(Items.COINS_995, 500))
            if (!paymentSuccessful) {
                sendDialogue(player, "You cannot afford that.")
                return
            }
        }

        setAttribute(player, paymentCheck, true)

        closeInterface(player)
        setVarp(player, 261, 0)
        openDialogue(player, EndDialogue())
    }

    inner class EndDialogue : DialogueFile() {
        override fun handle(componentID: Int, buttonID: Int) {
            when(stage) {
                0 -> npc(NPCs.YRSA_1301, FaceAnim.FRIENDLY, "I think they suit you.").also { stage++ }
                1 -> player(FaceAnim.HAPPY, "Thanks!").also { stage++ }
                2 -> end()
            }
        }
    }

    /**
     * Updates appearance.
     */
    private fun updateFeet(
        player: Player,
        button: Int,
    ) {
        var subtract = 0
        when (button) {
            15, 16, 17, 18, 19, 20 -> subtract += 15
        }
        setVarp(player, 261, (button - 14))
        player.appearance.feet.changeColor(colorId[button - subtract])
        refreshAppearance(player)
    }

    /**
     * Registers the interface plugin.
     */
    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.put(Components.YRSA_SHOE_STORE_200, this)
        return this
    }

}