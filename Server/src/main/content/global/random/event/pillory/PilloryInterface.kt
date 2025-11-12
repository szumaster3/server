package content.global.random.event.pillory

import core.api.openInterface
import core.api.sendMessage
import core.api.sendPlainDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.tools.DARK_RED
import shared.consts.Scenery

/**
 * Represents the Pillory random event interface.
 * @author szu
 */
class PilloryInterface :
    InterfaceListener,
    InteractionListener {
    override fun defineInterfaceListeners() {
        on(content.global.random.event.pillory.PilloryUtils.INTERFACE) { player, _, _, buttonID, _, _ ->
            when (buttonID) {
                8 -> content.global.random.event.pillory.PilloryUtils.selectedKey(player, 1)
                9 -> content.global.random.event.pillory.PilloryUtils.selectedKey(player, 2)
                10 -> content.global.random.event.pillory.PilloryUtils.selectedKey(player, 3)
            }
            return@on true
        }

        onOpen(content.global.random.event.pillory.PilloryUtils.INTERFACE) { _, _ ->
            return@onOpen true
        }
    }

    override fun defineListeners() {
        on(Scenery.CAGE_6836, IntType.SCENERY, "unlock") { player, _ ->
            if (player.location in content.global.random.event.pillory.PilloryUtils.LOCATIONS) {
                content.global.random.event.pillory.PilloryUtils.randomPillory(player)
                openInterface(player, content.global.random.event.pillory.PilloryUtils.INTERFACE)
                sendPlainDialogue(
                    player,
                    true,
                    "",
                    "Pick the$DARK_RED swinging key</col> that matches the",
                    "hole in the$DARK_RED spinning lock</col>.",
                )
            } else {
                sendMessage(player, "You can't unlock the pillory, you'll let all the prisoners out!")
            }
            return@on true
        }
    }
}
