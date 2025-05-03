package content.global.skill.construction.decoration.costume

import content.global.handlers.iface.DiangoReclaimInterface
import core.api.animate
import core.api.getUsedOption
import core.api.replaceScenery
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Animations
import org.rs.consts.Scenery

class ToyBoxSpace : InteractionListener {
    override fun defineListeners() {
        on(TOY_BOX_CLOSE, IntType.SCENERY, "open") { player, node ->
            animate(player, Animations.OPEN_CHEST_536)
            replaceScenery(node.asScenery(), node.id + 1, -1)
            return@on true
        }

        on(TOY_BOX_OPEN, IntType.SCENERY, "search", "close") { player, node ->
            val option = getUsedOption(player)
            if(option == "close") {
                animate(player, Animations.CLOSE_CHEST_539)
                replaceScenery(node.asScenery(), node.id - 1, -1)
            } else {
                DiangoReclaimInterface.open(player)
            }
            return@on true
        }
    }

    companion object {
        val TOY_BOX_CLOSE = intArrayOf(Scenery.TOY_BOX_18798,Scenery.TOY_BOX_18800,Scenery.TOY_BOX_18802)
        val TOY_BOX_OPEN = intArrayOf(Scenery.TOY_BOX_18799,Scenery.TOY_BOX_18801,Scenery.TOY_BOX_18803)


    }
}
