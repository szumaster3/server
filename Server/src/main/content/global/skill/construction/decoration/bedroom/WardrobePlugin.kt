package content.global.skill.construction.decoration.bedroom

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import shared.consts.Animations
import shared.consts.Components
import shared.consts.Scenery as Objects

class WardrobePlugin : InteractionListener {

    override fun defineListeners() {
        on(WARDROBE_FURNITURE_IDS, IntType.SCENERY, "change-clothes") { player, node ->
            val scenery = node as Scenery
            lock(player, 3)
            animate(player, Animations.OPEN_POH_WARDROBE_535)
            openWardrobe(player, scenery)
            return@on true
        }
    }

    private fun openWardrobe(player: Player, node: Scenery) {
        when (node.id) {
            Objects.SHOE_BOX_13155 -> openInterface(player, Components.YRSA_SHOE_STORE_200, node.name, 13)
            else -> {
                val (component, stringComponent) = if (player.appearance.isMale)
                    Components.THESSALIA_CLOTHES_MALE_591 to 179
                else
                    Components.THESSALIA_CLOTHES_FEMALE_594 to 180

                openInterface(player, component, node.name, stringComponent)
            }
        }
    }

    private fun openInterface(player: Player, component: Int, name: String, stringComponent: Int) {
        sendString(player, name, component, stringComponent)
        setComponentVisibility(player, component, stringComponent, true)
        openInterface(player, component)
    }

    companion object {
        private val WARDROBE_FURNITURE_IDS = intArrayOf(
            Objects.SHOE_BOX_13155,
            Objects.OAK_DRAWERS_13156,
            Objects.OAK_WARDROBE_13157,
            Objects.TEAK_DRAWERS_13158,
            Objects.TEAK_WARDROBE_13159,
            Objects.MAHOGANY_WARDROBE_13160,
            Objects.GILDED_WARDROBE_13161
        )
    }
}
