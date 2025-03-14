package content.global.skill.agility.grapple

import core.api.anyInEquipment
import core.api.inEquipment
import core.api.sendDialogue
import core.api.sendMessage
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.map.RegionManager.getObject
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Animations
import org.rs.consts.Items

@Initializable
class WaterOrbGrapple : OptionHandler() {
    companion object {
        private val REQUIREMENTS = HashMap<Int?, Int>()
        private var requirementsString: String? = null

        init {
            REQUIREMENTS.putIfAbsent(Skills.AGILITY, 36)
            REQUIREMENTS.putIfAbsent(Skills.RANGE, 39)
            REQUIREMENTS.putIfAbsent(Skills.STRENGTH, 22)
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
        SceneryDefinition.forId(17062).handlers["option:grapple"] = this
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        val destination: Location
        val current = player.location
        val rock = getObject(Location.create(2841, 3426, 0))
        val tree = getObject(Location.create(2841, 3434, 0))
        when (option) {
            "grapple" -> {
                destination = Location.create(2841, 3433, 0)
                for ((key, value) in REQUIREMENTS) {
                    if (player.getSkills().getLevel(key!!) < value) {
                        sendDialogue(player, requirementsString.toString())
                        return true
                    }
                }
                if (!anyInEquipment(player, *crossbowIds) || !inEquipment(player, grappleId)) {
                    sendMessage(player, "You need a mithril grapple tipped bolt with a rope to do that.")
                    return true
                }
                player.lock()
                Pulser.submit(
                    object : Pulse(1, player) {
                        var counter = 1

                        override fun pulse(): Boolean {
                            when (counter++) {
                                1 -> {
                                    player.faceLocation(destination)
                                    player.animate(Animation(Animations.FIRE_CROSSBOW_4230))
                                }

                                3 ->
                                    player.packetDispatch.sendPositionedGraphic(
                                        67,
                                        10,
                                        0,
                                        Location.create(2840, 3427, 0),
                                    )

                                4 -> {
                                    SceneryBuilder.replace(rock, rock!!.transform(rock.id + 1), 10)
                                    SceneryBuilder.replace(tree, tree!!.transform(tree.id + 1), 10)
                                }

                                13 -> player.properties.teleportLocation = destination
                                14 -> {
                                    player.unlock()
                                    player.achievementDiaryManager.finishTask(player, DiaryType.SEERS_VILLAGE, 2, 10)
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

    override fun getDestination(
        moving: Node,
        destination: Node,
    ): Location {
        return Location.create(2841, 3427, 0)
    }
}
