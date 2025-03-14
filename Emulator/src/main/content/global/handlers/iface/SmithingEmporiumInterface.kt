package content.global.handlers.iface

import core.api.*
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs.consts.Components

private const val BRACELET_INTERFACE_CHILD_ID = 69
private val COINS = Item(995, 500)
private val WRISTGUARD_MODELS =
    mapOf(
        122 to 0,
        123 to 27703,
        124 to 27704,
        125 to 27706,
        126 to 27707,
        127 to 27697,
        128 to 27699,
        129 to 0,
        130 to 27698,
        131 to 27708,
        132 to 27702,
        133 to 27705,
        134 to 27700,
        135 to 27709,
    )

class SmithingEmporiumInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        onOpen(Components.REINALD_SMITHING_EMPORIUM_593) { player, component ->
            setAttribute(player, "wrists-look", player.appearance.wrists.look)
            player.toggleWardrobe(true)
            component.setCloseEvent { p, _ ->
                handleCloseEvent(p)
                return@setCloseEvent true
            }
            return@onOpen true
        }

        on(Components.REINALD_SMITHING_EMPORIUM_593) { player, _, _, buttonID, _, _ ->
            WRISTGUARD_MODELS[buttonID]?.let { sendModel(it, player) } ?: run {
                if (buttonID == 117) confirm(player)
            }
            return@on true
        }
    }

    private fun handleCloseEvent(player: Player) {
        val originalIndex = getAttribute(player, "wrists-look", if (player.isMale) 34 else 68)
        if (!getAttribute(player, "bracelet-paid", false)) {
            player.appearance.wrists.changeLook(originalIndex)
            player.appearance.sync()
        }
        removeAttribute(player, "bracelet-paid")
        player.toggleWardrobe(false)
    }

    private fun confirm(player: Player) {
        if (!removeItem(player, COINS)) {
            sendDialogue(player, "You cannot afford that.")
        } else {
            setAttribute(player, "bracelet-paid", true)
            closeInterface(player)
        }
    }

    private fun sendModel(
        id: Int,
        player: Player,
    ) {
        val appearanceIndex = calculateAppearanceIndex(id, player)
        sendModelOnInterface(player, Components.REINALD_SMITHING_EMPORIUM_593, BRACELET_INTERFACE_CHILD_ID, id, 1)
        setComponentVisibility(player, Components.REINALD_SMITHING_EMPORIUM_593, BRACELET_INTERFACE_CHILD_ID, id == 0)
        player.appearance.wrists.changeLook(appearanceIndex)
        player.debug("USING WRIST APPEARANCE ID $appearanceIndex")
        player.appearance.sync()
        sendPlayerOnInterface(player, Components.REINALD_SMITHING_EMPORIUM_593, 60)
    }

    private fun calculateAppearanceIndex(
        id: Int,
        player: Player,
    ): Int {
        var appearanceIndex =
            when (id) {
                27704 -> 117
                27708 -> 118
                27697 -> 119
                27700 -> 120
                27699 -> 123
                27709 -> 124
                27707 -> 121
                27705 -> 122
                27706 -> 125
                27702 -> 126
                27703 -> if (player.isMale) 33 else 67
                27698 -> if (player.isMale) 84 else 127
                0 -> if (player.isMale) 34 else 68
                else -> 0
            }
        if (!player.isMale && id != 27703 && id != 27698 && id != 0) {
            appearanceIndex += 42
        }
        return appearanceIndex
    }
}
