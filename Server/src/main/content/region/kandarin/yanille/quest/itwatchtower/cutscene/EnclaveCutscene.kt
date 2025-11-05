package content.region.kandarin.yanille.quest.itwatchtower.cutscene

import core.api.*
import core.game.activity.Cutscene
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.impl.Animator.Priority
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import shared.consts.NPCs
import shared.consts.Quests
import shared.consts.Sounds

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

            3 -> dialogueUpdate(true, "Meanwhile, in the Shaman's enclave...")
            4 -> {
                playAudio(player, Sounds.WATCHTOWER_DRAGON_ATTACK_3266)
                visualize(getNPC(NPCs.BLUE_DRAGON_5178)!!, Animation(81, Priority.HIGH), Graphics(shared.consts.Graphics.DRAGON_BREATH_1, 64))
                impact(getNPC(NPCs.BLUE_DRAGON_5178)!!, 30, ImpactHandler.HitsplatType.NORMAL)
                face(getNPC(NPCs.OGRE_SHAMAN_5187)!!, getNPC(NPCs.BLUE_DRAGON_5178)!!)
                timedUpdate(3)
            }
            5 -> {
                playAudio(player, Sounds.WATCHTOWER_SHAMAN_CRY_3268)
                sendChat(getNPC(NPCs.OGRE_SHAMAN_5187)!!, "Hur, hut, hur. Stoopid dragon!")
                timedUpdate(1)
            }
            6 -> {
                playAudio(player, Sounds.WATCHTOWER_SPELL_CAST_3270)
                sendChat(getNPC(NPCs.OGRE_SHAMAN_5187)!!, "Take dis!")
                animate(getNPC(NPCs.OGRE_SHAMAN_5187)!!, CAST_SPELL_ANIMATION)
                timedUpdate(3)
            }
            7 -> {
                playAudio(player, Sounds.WATCHTOWER_SPELL_HIT_3271)
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
