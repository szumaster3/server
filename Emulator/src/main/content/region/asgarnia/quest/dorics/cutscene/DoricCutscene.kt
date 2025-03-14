package content.region.asgarnia.quest.dorics.cutscene

import core.api.animate
import core.api.sendChat
import core.api.setAttribute
import core.game.activity.Cutscene
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import org.rs.consts.NPCs

class DoricCutscene(
    player: Player,
) : Cutscene(player) {
    override fun setup() {
        setExit(Location(2952, 3450))
        loadRegion(11829)
        addNPC(NPCs.DORIC_284, 9, 58, Direction.WEST)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(6)
            }

            1 -> {
                teleport(player, 8, 58)
                moveCamera(6, 60, 250)
                rotateCamera(8, 58, 75)
                player.faceLocation(getNPC(NPCs.DORIC_284)!!.location)
                getNPC(NPCs.DORIC_284)!!.faceLocation(player.location)
                timedUpdate(2)
            }

            2 -> {
                fadeFromBlack()
                getNPC(NPCs.DORIC_284)!!.faceLocation(player.location)
                timedUpdate(2)
            }

            3 -> {
                sendChat(player, "Mind your own business, shortstuff!")
                timedUpdate(6)
            }

            4 -> {
                sendChat(
                    getNPC(NPCs.DORIC_284)!!,
                    "I guess your mother never taught you manners. Lucky for you, I'll do just that!",
                )
                timedUpdate(5)
            }

            5 -> {
                animate(getNPC(NPCs.DORIC_284)!!, 99)
                timedUpdate(1)
            }

            6 -> {
                animate(player, 837)
                timedUpdate(4)
            }

            7 -> {
                animate(player, 838)
                timedUpdate(2)
            }

            8 -> {
                fadeToBlack()
                timedUpdate(5)
            }

            9 -> {
                animate(player, 0)
                timedUpdate(1)
            }

            10 -> {
                animate(player, 856)
                fadeFromBlack()
                timedUpdate(2)
            }

            11 -> {
                sendChat(player, "Okay, I'm sorry!")
                animate(player, 856)
                timedUpdate(5)
            }

            12 -> {
                sendChat(
                    getNPC(NPCs.DORIC_284)!!,
                    "That's what I thought. Watch your mouth the next time you speak to me.",
                )
                timedUpdate(3)
            }

            13 -> {
                end {
                    setAttribute(player, "/save:pre-dq:doric-calm", true)
                }
            }
        }
    }
}
