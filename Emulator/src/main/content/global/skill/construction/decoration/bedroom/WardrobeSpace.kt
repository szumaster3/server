package content.global.skill.construction.decoration.bedroom

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.system.task.Pulse
import org.rs.consts.Components
import org.rs.consts.Scenery

class WardrobeSpace : InteractionListener {
    private val wardrobeSpaceFurniture =
        intArrayOf(
            Scenery.SHOE_BOX_13155,
            Scenery.OAK_DRAWERS_13156,
            Scenery.OAK_WARDROBE_13157,
            Scenery.TEAK_DRAWERS_13158,
            Scenery.TEAK_WARDROBE_13159,
            Scenery.MAHOGANY_WARDROBE_13160,
            Scenery.GILDED_WARDROBE_13161,
        )

    override fun defineListeners() {
        on(wardrobeSpaceFurniture, IntType.SCENERY, "change-clothes") { player, node ->
            lock(player, 2)
            submitIndividualPulse(
                player,
                object : Pulse() {
                    var counter = 0

                    override fun pulse(): Boolean {
                        when (counter++) {
                            0 -> animate(player, 535)
                            2 -> {
                                if (node.id == Scenery.SHOE_BOX_13155) {
                                    openInterface(player, Components.YRSA_SHOE_STORE_200)
                                    sendString(player, node.name, Components.YRSA_SHOE_STORE_200, 13)
                                } else {
                                    if (player.appearance.isMale) {
                                        sendString(player, node.name, Components.YRSA_SHOE_STORE_200, 13)
                                        setComponentVisibility(player, Components.THESSALIA_CLOTHES_MALE_591, 179, true)
                                        openInterface(player, Components.THESSALIA_CLOTHES_MALE_591)
                                    } else {
                                        sendString(player, node.name, Components.THESSALIA_CLOTHES_FEMALE_594, 61)
                                        setComponentVisibility(
                                            player,
                                            Components.THESSALIA_CLOTHES_FEMALE_594,
                                            180,
                                            true,
                                        )
                                        openInterface(player, Components.THESSALIA_CLOTHES_FEMALE_594)
                                    }
                                }
                                return true
                            }
                        }
                        return false
                    }
                },
            )
            return@on true
        }
    }
}
