package content.global.travel.glider

import core.api.isQuestComplete
import core.api.openInterface
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import shared.consts.Components
import shared.consts.Quests

class GnomeDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when(stage) {
            0 -> npc(FaceAnim.OLD_DEFAULT, "What do you want human?").also { stage++ }
            1 -> player(FaceAnim.HALF_GUILTY, "May you fly me somewhere on your glider?").also { stage++ }
            2 -> if (!isQuestComplete(player!!, Quests.THE_GRAND_TREE)) {
                end()
                npc(FaceAnim.OLD_ANGRY3, "I only fly friends of the gnomes!")
            } else {
                npc(FaceAnim.OLD_DEFAULT, "If you wish.")
                stage++
            }
            3 -> {
                end()
                openInterface(player!!, Components.GLIDERMAP_138)
            }
        }
    }
}