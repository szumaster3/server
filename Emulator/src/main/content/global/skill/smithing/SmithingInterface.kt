package content.global.skill.smithing

import content.data.GameAttributes
import content.region.misc.handlers.tutorial.TutorialStage
import core.api.getAttribute
import core.api.sendInputDialogue
import core.api.submitIndividualPulse
import core.api.ui.sendInterfaceConfig
import core.game.interaction.InterfaceListener
import core.game.node.item.Item
import org.rs.consts.Components

class SmithingInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        /*
         * A special interaction to hide buttons during the tutorial.
         */

        onOpen(Components.SMITHING_NEW_300) { player, _ ->
            if (!getAttribute(player, GameAttributes.TUTORIAL_COMPLETE, false)) {
                for (i in intArrayOf(
                    25,
                    33,
                    41,
                    49,
                    57,
                    65,
                    73,
                    81,
                    105,
                    113,
                    121,
                    129,
                    137,
                    145,
                    153,
                    177,
                    185,
                    193,
                    201,
                    209,
                    217,
                    225,
                    233,
                    241,
                )) {
                    sendInterfaceConfig(player, Components.SMITHING_NEW_300, i, true)
                }
            }
            return@onOpen true
        }

        on(Components.SMITHING_NEW_300) { player, _, _, buttonID, _, _ ->
            val item = Bars.getItemId(buttonID, player.gameAttributes.getAttribute<Any>("smith-type") as BarType)
            val bar = Bars.forId(item) ?: return@on true
            val amount = SmithingType.forButton(player, bar, buttonID, bar.barType.barType)
            player.gameAttributes.setAttribute("smith-bar", bar)
            player.gameAttributes.setAttribute("smith-item", item)
            if (amount == -1) {
                sendInputDialogue(player, true, "Enter the amount:") { value: Any ->
                    submitIndividualPulse(
                        player,
                        SmithingPulse(
                            player,
                            Item(player.gameAttributes.getAttribute<Any>("smith-item") as Int, value as Int),
                            player.gameAttributes.getAttribute<Any>("smith-bar") as Bars,
                            value,
                        ),
                    )
                }
                return@on true
            }
            submitIndividualPulse(player, SmithingPulse(player, Item(item, amount), Bars.forId(item)!!, amount))
            return@on true
        }

        onClose(Components.SMITHING_NEW_300) { player, _ ->
            if (!getAttribute(player, GameAttributes.TUTORIAL_COMPLETE, false)) {
                TutorialStage.rollback(player)
            }
            return@onClose true
        }
    }
}
