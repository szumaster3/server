package content.global.skill.construction

import core.api.sendDialogueOptions
import core.api.sendPlainDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueInterpreter
import core.game.node.entity.player.Player
import core.plugin.Initializable

@Initializable
class RemovalDialogue : Dialogue {
    private lateinit var pos: IntArray

    private var plane = 0

    private var room: Room? = null

    constructor() : super()

    constructor(player: Player?) : super(player)

    override fun newInstance(player: Player): Dialogue {
        return RemovalDialogue(player)
    }

    override fun open(vararg args: Any): Boolean {
        pos = args[1] as IntArray
        plane = player.location.z
        if (HouseManager.isInDungeon(player)) {
            plane = 3
        }
        room = player.houseManager.rooms[plane][pos[0]][pos[1]]
        sendDialogueOptions(
            player,
            "Remove the " + (
                if (room != null) {
                    room!!
                        .properties.name
                        .lowercase()
                        .replace("_", " ")
                } else {
                    "room"
                }
            ) + "?",
            "Yes",
            "No",
        )
        stage = 0
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        if (stage == 0) {
            if (buttonId == 1) {
                if (plane == 0 && player.houseManager.hasRoomAt(1, pos[0], pos[1])) {
                    sendPlainDialogue(player, false, "You can't remove a room supporting another room.")
                    stage = 1
                    return true
                }
                if (room != null && room!!.properties.isLand) {
                    val h = room!!.hotspots[0]
                    if (h != null && h.decorationIndex == 0 && player.houseManager.portalAmount <= 1) {
                        sendPlainDialogue(player, false, "You can't remove the garden with your portal in it.")
                        stage = 1
                        return true
                    }
                }
                player.houseManager.rooms[plane][pos[0]][pos[1]] = null
                for (i in plane..2) {
                    val r = player.houseManager.rooms[i][pos[0]][pos[1]]
                    if (r != null && r.properties.isRoof) {
                        player.houseManager.rooms[i][pos[0]][pos[1]] = null
                    }
                }
                player.houseManager.reload(player, true)
            }
        }
        end()
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(DialogueInterpreter.getDialogueKey("con:remove"))
    }
}
