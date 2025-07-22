package content.global.skill.construction.decoration.bedroom

import core.api.animate
import core.api.openInterface
import core.api.sendInterfaceConfig
import core.api.sendString
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Animations
import org.rs.consts.Components
import org.rs.consts.Scenery

class DresserPlugin : InteractionListener {
    private val dresserSpaceFurniture =
        intArrayOf(
            Scenery.SHAVING_STAND_13162,
            Scenery.SHAVING_STAND_13163,
            Scenery.DRESSER_13164,
            Scenery.DRESSER_13165,
            Scenery.DRESSER_13166,
            Scenery.DRESSER_13167,
            Scenery.DRESSER_13168,
        )

    override fun defineListeners() {
        on(dresserSpaceFurniture, IntType.SCENERY, "preen") { player, node ->
            animate(player, Animations.PREEN_OPTION_TO_CHECK_SELF_IN_MIRROR_POH_3670)

            val isMale = player.appearance.isMale
            val interfaceId = if (isMale) Components.HAIRDRESSER_MALE_596 else Components.HAIRDRESSER_FEMALE_592
            val stringComponentId = if (isMale) 64 else 18
            val configId = if (isMale) 197 else 202

            openInterface(player, interfaceId)
            sendString(player, node.name, interfaceId, stringComponentId)
            sendInterfaceConfig(player, interfaceId, configId, true)

            return@on true
        }
    }
}
