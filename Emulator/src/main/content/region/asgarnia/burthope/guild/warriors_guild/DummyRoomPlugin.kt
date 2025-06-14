package content.region.asgarnia.burthope.guild.warriors_guild

import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.GameWorld.ticks
import core.game.world.map.Location
import core.game.world.map.RegionManager.getObject
import core.game.world.map.RegionManager.getRegionChunk
import core.game.world.update.flag.chunk.AnimateObjectUpdateFlag
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import org.rs.consts.Components

@Initializable
class DummyRoomPlugin : OptionHandler() {
    private enum class Dummy(val scenery: Scenery, val attackStyle: Int, val bonusType: Int, ) {
        STAB(Scenery(org.rs.consts.Scenery.DUMMY_15629, 2857, 3549, 0, 10, 2), -1, WeaponInterface.BONUS_STAB),
        SLASH(Scenery(org.rs.consts.Scenery.DUMMY_15625, 2858, 3554, 0), -1, WeaponInterface.BONUS_SLASH),
        CRUSH(Scenery(org.rs.consts.Scenery.DUMMY_15628, 2859, 3549, 0, 10, 2), -1, WeaponInterface.BONUS_CRUSH),
        CONTROLLED(Scenery(org.rs.consts.Scenery.DUMMY_15627, 2855, 3552, 0, 10, 3), WeaponInterface.STYLE_CONTROLLED, -1),
        DEFENCE(Scenery(org.rs.consts.Scenery.DUMMY_15630, 2855, 3550, 0, 10, 3), WeaponInterface.STYLE_DEFENSIVE, -1,),
        AGGRESSIVE(Scenery(org.rs.consts.Scenery.DUMMY_15626, 2860, 3553, 0, 10, 1), WeaponInterface.STYLE_AGGRESSIVE, -1,),
        ACCURATE(Scenery(org.rs.consts.Scenery.DUMMY_15624, 2856, 3554, 0), WeaponInterface.STYLE_ACCURATE, -1),
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(org.rs.consts.Scenery.INFORMATION_SCROLL_15656).handlers["option:view"] = this
        for (dummy in Dummy.values()) {
            SceneryDefinition.forId(dummy.scenery.id).handlers["option:hit"] = this
        }
        Pulser.submit(
            object : Pulse(10) {
                var activeDummy: Boolean = false
                var controlled: Scenery? = null

                override fun pulse(): Boolean {
                    if (!activeDummy) {
                        delay = 10
                        timeStamp = ticks
                        dummy = RandomFunction.getRandomElement(Dummy.values())
                        SceneryBuilder.replace(getObject(dummy!!.scenery.location), dummy!!.scenery, 11)
                        activeDummy = true
                        if (dummy == Dummy.CONTROLLED && controlled == null) {
                            val l = Location.create(2860, 3551, 0)
                            controlled =
                                Scenery(dummy!!.scenery.id, l, 10, 1)
                            SceneryBuilder.replace(getObject(l), controlled, 11)
                        }
                        return false
                    }
                    delay = 4
                    var animation = Animation.create(4164)
                    animation.setObject(dummy!!.scenery)
                    getRegionChunk(dummy!!.scenery.location).flag(AnimateObjectUpdateFlag(animation),)
                    activeDummy = false
                    if (controlled != null) {
                        animation = Animation.create(4164)
                        animation.setObject(controlled)
                        getRegionChunk(controlled!!.location).flag(AnimateObjectUpdateFlag(animation))
                        controlled = null
                    }
                    return false
                }
            },
        )
        return this
    }

    override fun handle(player: Player, node: Node, option: String, ): Boolean {
        val scenery = node as Scenery
        if (scenery.id == org.rs.consts.Scenery.INFORMATION_SCROLL_15656) {
            openInterface(player, Components.WARGUILD_DUMMY_412)
            return true
        }
        if (scenery.id == dummy!!.scenery.id) {
            if (timeStamp == player.getAttribute("dummy_ts", -1)) {
                sendMessage(player, "You have already hit a dummy this turn.")
                return true
            }
            if (player.properties.attackStyle.style != dummy!!.attackStyle && player.properties.attackStyle.bonusType != dummy!!.bonusType) {
                lock(player, 5)
                visualize(player, player.properties.attackAnimation, Graphics(org.rs.consts.Graphics.STUN_BIRDIES_ABOVE_HEAD_80, 96))
                sendMessage(player, "You whack the dummy with the wrong attack style.")
            } else {
                player.getSkills().addExperience(Skills.ATTACK, 15.0, true)
                animate(player, player.properties.attackAnimation)
                sendMessage(player, "You whack the dummy successfully!")
                setAttribute(player, "dummy_ts", timeStamp)
                player.getSavedData().activityData.updateWarriorTokens(2)
            }
            return true
        }
        return false
    }

    companion object {
        private var dummy: Dummy? = null
        private var timeStamp = 0
    }
}
