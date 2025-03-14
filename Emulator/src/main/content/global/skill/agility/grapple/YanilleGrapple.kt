package content.global.skill.agility.grapple

import core.api.*
import core.api.ui.restoreTabs
import core.api.ui.setMinimapState
import core.cache.def.impl.SceneryDefinition
import core.game.component.Component
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Animations
import org.rs.consts.Components
import org.rs.consts.Items

@Initializable
class YanilleGrapple : OptionHandler() {
    companion object {
        private val REQUIREMENTS = HashMap<Int?, Int>()
        private var requirementsString: String?

        init {
            REQUIREMENTS.putIfAbsent(Skills.AGILITY, 39)
            REQUIREMENTS.putIfAbsent(Skills.RANGE, 21)
            REQUIREMENTS.putIfAbsent(Skills.STRENGTH, 38)
            requirementsString =
                "You need at least " + REQUIREMENTS[Skills.AGILITY] + " " + Skills.SKILL_NAME[Skills.AGILITY] + ", " +
                REQUIREMENTS[Skills.RANGE] +
                " " +
                Skills.SKILL_NAME[Skills.RANGE] +
                ", and " +
                REQUIREMENTS[Skills.STRENGTH] +
                " " +
                Skills.SKILL_NAME[Skills.STRENGTH] +
                " to use this shortcut."
        }

        private val crossbowIds =
            intArrayOf(
                Items.DORGESHUUN_CBOW_8880,
                Items.MITH_CROSSBOW_9181,
                Items.ADAMANT_CROSSBOW_9183,
                Items.RUNE_CROSSBOW_9185,
                Items.KARILS_CROSSBOW_4734,
                Items.HUNTERS_CROSSBOW_10156,
            )
        private val grappleId = Items.MITH_GRAPPLE_9419
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(17047).handlers["option:grapple"] = this
        SceneryDefinition.forId(17048).handlers["option:jump"] = this
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        val destination: Location
        val current = player.location
        when (option) {
            "jump" ->
                ForceMovement.run(
                    player,
                    current,
                    if (current.y < 3074) Location.create(2556, 3072, 0) else Location.create(2556, 3075, 0),
                    Animation(7268),
                    10,
                )

            "grapple" -> {
                destination = if (current.y < 3073) Location.create(2556, 3073, 1) else Location.create(2556, 3074, 1)
                for ((key, value) in REQUIREMENTS) {
                    if (getStatLevel(player, key!!) < value) {
                        sendDialogue(player, requirementsString.toString())
                        return true
                    }
                }
                if (!anyInEquipment(player, *crossbowIds) || !inEquipment(player, grappleId)) {
                    sendMessage(player, "You need a mithril grapple tipped bolt with a rope to do that.")
                    return true
                }
                lock(player, 1000)
                Pulser.submit(
                    object : Pulse(1, player) {
                        var counter = 1
                        var tab: Component? = null

                        override fun pulse(): Boolean {
                            when (counter++) {
                                1 -> {
                                    player.faceLocation(destination)
                                    visualize(
                                        player,
                                        Animation(Animations.FIRE_CROSSBOW_TO_CLIMB_WALL_4455),
                                        Graphics(
                                            org.rs.consts.Graphics.MITHRIL_GRAPPLE_760,
                                            100,
                                        ),
                                    )
                                }

                                8 -> {
                                    tab = player.interfaceManager.singleTab
                                    openOverlay(player, Components.FADE_TO_BLACK_115)
                                    setMinimapState(player, 2)
                                    removeTabs(player, 0, 1, 2, 3, 4, 5, 6, 11, 12)
                                }

                                13 -> player.properties.teleportLocation = destination
                                14 -> {
                                    restoreTabs(player)
                                    if (tab != null) {
                                        player.interfaceManager.openTab(tab)
                                    }
                                    setMinimapState(player, 0)
                                    closeOverlay(player)
                                    closeInterface(player)
                                    unlock(player)
                                    return true
                                }
                            }
                            return false
                        }
                    },
                )
            }
        }
        return true
    }
}
