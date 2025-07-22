package content.region.kandarin.yanille.quest.itwatchtower.cutscene

import core.api.*
import core.api.setQuestStage
import core.game.activity.Cutscene
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.impl.Animator.Priority
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import org.rs.consts.Animations
import org.rs.consts.NPCs
import org.rs.consts.Quests

class EnclaveCutscene(player: Player) : Cutscene(player) {

    override fun setup() {
        setExit(Location.create(2588, 9410, 0))
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
        addNPC(NPCs.BLUE_DRAGON_5178, 31,26, Direction.SOUTH)
        addNPC(NPCs.OGRE_SHAMAN_5187, 31,25, Direction.SOUTH_WEST)
        loadRegion(10289)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(6)
            }
            1 -> {
                fadeFromBlack()
                teleport(player, 28,2)
                timedUpdate(1)
            }
            2 -> {
                rotateCamera(31,30)
                moveCamera(31,18,500)
                timedUpdate(1)
            }

            3 -> dialogueUpdate("Meanwhile, in the Shaman's enclave...")
            4 -> {
                visualize(
                    getNPC(NPCs.BLUE_DRAGON_5178)!!,
                    Animation(Animations.DRAGON_BREATH_81, Priority.HIGH),
                    Graphics(org.rs.consts.Graphics.DRAGON_BREATH_1, 64)
                )
                impact(getNPC(NPCs.BLUE_DRAGON_5178)!!, 30, ImpactHandler.HitsplatType.NORMAL)
                face(getNPC(NPCs.OGRE_SHAMAN_5187)!!, getNPC(NPCs.BLUE_DRAGON_5178)!!)
                timedUpdate(3)
            }
            5 -> {
                sendChat(getNPC(NPCs.OGRE_SHAMAN_5187)!!, "Hur, hut, hur. Stoopid dragon!")
                timedUpdate(1)
            }
            6 -> {
                sendChat(getNPC(NPCs.OGRE_SHAMAN_5187)!!, "Take dis!")
                animate(getNPC(NPCs.OGRE_SHAMAN_5187)!!, CAST_SPELL_ANIMATION)
                timedUpdate(3)
            }
            7 -> {
                impact(getNPC(NPCs.BLUE_DRAGON_5178)!!, 75, ImpactHandler.HitsplatType.NORMAL)
                timedUpdate(1)
            }
            8 -> {
                animate(getNPC(NPCs.BLUE_DRAGON_5178)!!, 92)
                timedUpdate(1)
            }
            9 -> {
                end {
                    setQuestStage(player, Quests.WATCHTOWER, 60)
                }
            }

        }
    }

    companion object {
        val CAST_SPELL_ANIMATION = 5357
    }
}
