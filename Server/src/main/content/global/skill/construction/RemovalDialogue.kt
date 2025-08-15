package content.global.skill.construction

import core.api.sendDialogueOptions
import core.api.sendPlainDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueInterpreter
import core.game.node.entity.player.Player
import core.plugin.Initializable

@Initializable
class RemovalDialogue() : Dialogue() {

    private lateinit var pos: IntArray
    private var plane = 0
    private var room: Room? = null

    constructor(player: Player?) : this() {
        this.player = player
    }

    override fun newInstance(player: Player?): Dialogue = RemovalDialogue(player)

    override fun open(vararg args: Any?): Boolean {
        pos = args[1] as IntArray
        plane = if (HouseManager.isInDungeon(player)) 3 else player.location.z
        room = player.houseManager.rooms[plane][pos[0]][pos[1]]

        val roomName = room?.properties?.name
            ?.lowercase()
            ?.replace("_", " ")
            ?: "room"

        sendDialogueOptions(player, "Remove the $roomName?", "Yes", "No")
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        if (stage == 0 && buttonId == 1) {
            val manager = player.houseManager

            /*
             * Do not remove a room if it supports another.
             */

            if (plane == 0 && manager.hasRoomAt(1, pos[0], pos[1])) {
                sendPlainDialogue(player, false, "You can't remove a room supporting another room.")
                stage = 1
                return true
            }

            /*
             * Do not remove the garden with the portal.
             */

            if (room?.properties?.isLand == true) {
                val h = room?.hotspots?.getOrNull(0)
                if (h?.decorationIndex == 0 && manager.portalAmount <= 1) {
                    sendPlainDialogue(player, false, "You can't remove the garden with your portal in it.")
                    stage = 1
                    return true
                }
            }

            /*
             * Removal of a room and roof.
             */

            manager.rooms[plane][pos[0]][pos[1]] = null
            for (i in plane..2) {
                if (manager.rooms[i][pos[0]][pos[1]]?.properties?.isRoof == true) {
                    manager.rooms[i][pos[0]][pos[1]] = null
                }
            }
            manager.reload(player, true)
        }

        end()
        return true
    }

    override fun getIds(): IntArray =
        intArrayOf(DialogueInterpreter.getDialogueKey("con:remove"))
}