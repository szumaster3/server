package content.region.kandarin.quest.arena.cutscene

import content.region.kandarin.quest.arena.handlers.FightArenaListener.Companion.Jeremy
import content.region.kandarin.quest.arena.handlers.npc.OgreNPC.Companion.spawnOgre
import core.api.*
import core.game.activity.Cutscene
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import org.rs.consts.Animations
import org.rs.consts.NPCs

class JeremyRescueCutscene(
    player: Player,
) : Cutscene(player) {
    override fun setup() {
        setExit(location(2603, 3155, 0))
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                face(player, Jeremy, 1)
                playerDialogueUpdate(FaceAnim.NEUTRAL, "Jeremy look, I have the keys.")
            }

            1 -> {
                face(Jeremy, player, 1)
                dialogueUpdate(
                    NPCs.JEREMY_SERVIL_265,
                    FaceAnim.CHILD_NORMAL,
                    "Wow! Please set me free, then we can find my dad. I overheard a guard talking. I think they're taken him to the arena.",
                )
            }

            2 -> {
                resetFace(Jeremy)
                resetFace(player)
                playerDialogueUpdate(FaceAnim.NEUTRAL, "Ok, we'd better hurry.")
            }

            3 -> {
                move(Jeremy, 56, 31)
                face(player, location(2616, 3167, 0))
                animate(player, Animations.HUMAN_OPEN_CELL_DOOR_2098)
                timedUpdate(3)
            }

            4 -> {
                move(player, 57, 32)
                timedUpdate(2)
            }

            5 -> {
                DoorActionHandler.handleAutowalkDoor(Jeremy, getObject(57, 31, 0)!!)
                timedUpdate(3)
            }

            6 -> {
                face(player, Jeremy, 1)
                sendChat(Jeremy, "I'll run ahead.")
                move(Jeremy, 57, 20)
                timedUpdate(5)
            }

            7 -> {
                teleport(Jeremy, 56, 31)
                loadRegion(10289)
                addNPC(JEREMY_RESCUE, 41, 17, Direction.NORTH)
                addNPC(GENERAL, 45, 19, Direction.NORTH)
                addNPC(OGRE, 48, 30, Direction.NORTH)
                addNPC(JUSTIN, 41, 32, Direction.EAST)
                timedUpdate(1)
            }

            8 -> {
                teleport(player, 47, 15)
                moveCamera(47, 20)
                rotateCamera(45, 15)
                timedUpdate(2)
            }

            9 -> {
                DoorActionHandler.handleAutowalkDoor(player, getObject(46, 16)!!)
                timedUpdate(1)
            }

            10 -> {
                moveCamera(41, 26, 300, 4)
                rotateCamera(45, 15, 300, 4)
                timedUpdate(1)
            }

            11 -> {
                move(player, 43, 18)
                timedUpdate(1)
            }

            12 -> {
                move(player, 43, 19)
                timedUpdate(6)
            }

            13 -> {
                face(player, getNPC(JEREMY_RESCUE)!!, 1)
                playerDialogueUpdate(FaceAnim.HALF_ASKING, "Jeremy, where's your father?")
            }

            14 -> {
                move(getNPC(JUSTIN)!!, 42, 32)
                face(getNPC(JEREMY_RESCUE)!!, player, 1)
                dialogueUpdate(
                    JEREMY_RESCUE,
                    FaceAnim.CHILD_SAD,
                    "Quick help him! That beast will kill him. He's too old to fight.",
                )
            }

            15 -> {
                moveCamera(40, 25, 400)
                rotateCamera(44, 29)
                teleport(getNPC(OGRE)!!, 47, 29)
                registerHintIcon(player, getNPC(OGRE)!!)
                timedUpdate(1)
            }

            16 -> {
                DoorActionHandler.handleAutowalkDoor(getNPC(OGRE)!!, getObject(46, 29)!!)
                timedUpdate(2)
            }

            17 -> {
                move(getNPC(OGRE)!!, 45, 29)
                move(getNPC(OGRE)!!, 43, 31)
                timedUpdate(3)
            }

            18 -> {
                rotateCamera(44, 30, 300, 5)
                timedUpdate(1)
            }

            19 -> {
                rotateCamera(44, 38, 300, 3)
                timedUpdate(1)
            }

            20 -> {
                getNPC(OGRE)!!.faceLocation(
                    getNPC(
                        JUSTIN,
                    )!!.location,
                )
                animate(getNPC(OGRE)!!, 359)
                animate(getNPC(JUSTIN)!!, 404)
                timedUpdate(1)
            }

            21 -> {
                end {
                    clearHintIcon(player)
                    spawnOgre(player)
                }
            }
        }
    }

    companion object {
        private const val GENERAL = NPCs.GENERAL_KHAZARD_258
        private const val JEREMY_RESCUE = NPCs.JEREMY_SERVIL_266
        private const val JUSTIN = NPCs.JUSTIN_SERVIL_267
        private const val OGRE = NPCs.KHAZARD_OGRE_270
    }
}
